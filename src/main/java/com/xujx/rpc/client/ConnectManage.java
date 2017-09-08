package com.xujx.rpc.client;

import com.xujx.rpc.client.handler.RpcClientHandler;
import com.xujx.rpc.decoder.RpcDecoder;
import com.xujx.rpc.dto.request.RpcRequest;
import com.xujx.rpc.dto.response.RpcResponse;
import com.xujx.rpc.encoder.RpcEncoder;
import com.xujx.rpc.util.threadpool.ThreadPoolUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xujinxin on 2017/9/7.
 */
public class ConnectManage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectManage.class);

    private volatile static ConnectManage connectManage;

    private static final EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

    private CopyOnWriteArraySet<String> connect = new CopyOnWriteArraySet<>();
    private CopyOnWriteArrayList<RpcClientHandler> connectedHandlers = new CopyOnWriteArrayList<>();

    private ReentrantLock lock = new ReentrantLock();
    private Condition connected = lock.newCondition();

    private AtomicInteger roundRobin = new AtomicInteger(0);

    private ConnectManage() {

    }

    /**
     * 获取ConnectManage 的单例
     * @return connectManage 单例
     */
    public static ConnectManage getInstance() {
        if (connectManage == null) {
            synchronized (ConnectManage.class) {
                if (connectManage == null) {
                    connectManage = new ConnectManage();
                }
            }
        }
        return connectManage;
    }


    public void updateConnectedServer(List<String> allServerAddress) {
        if (allServerAddress != null && allServerAddress.size() > 0) {
                //解析出最新的远程服务提供的地址

            Set<String> set = new HashSet<>(allServerAddress);

            for(String str :set){
                if(!connect.contains(str)){
                    connectServerNode(str);
                }
            }
        }
    }

    public RpcClientHandler chooseHandler() {
        CopyOnWriteArrayList<RpcClientHandler> handlers = (CopyOnWriteArrayList<RpcClientHandler>) this.connectedHandlers.clone();
        int size = handlers.size();
        while (size <= 0) {
            try {
                boolean available = waitingForHandler();
                if (available) {
                    handlers = (CopyOnWriteArrayList<RpcClientHandler>) this.connectedHandlers.clone();
                    size = handlers.size();
                }
            } catch (InterruptedException e) {
                LOGGER.error("Waiting for available node is interrupted! ", e);
                throw new RuntimeException("Can't connect any servers!", e);
            }
        }
        int index = (roundRobin.getAndAdd(1) + size) % size;
        return handlers.get(index);
    }



    private void connectServerNode(String str) {

        String[] array = str.split(":");
        if (array.length == 2) { // Should check IP and port
            String host = array[0];
            int port = Integer.parseInt(array[1]);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new RpcDecoder<>(RpcResponse.class))
                                    .addLast(new RpcClientHandler());
                        }
                    });

            ChannelFuture channelFuture = null;
            try {
                channelFuture = bootstrap.connect().sync();
                channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                    if (channelFuture1.isSuccess()) {
                        RpcClientHandler handler = channelFuture1.channel().pipeline().get(RpcClientHandler.class);
                        addHandler(handler,str);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void addHandler(RpcClientHandler handler,String locate) {
        connectedHandlers.add(handler);
        connect.add(locate);
        signalAvailableHandler();
    }

    private void signalAvailableHandler() {
        lock.lock();
        try {
            connected.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private boolean waitingForHandler() throws InterruptedException {
        lock.lock();
        try {
            return connected.await(6000, TimeUnit.MILLISECONDS);
        } finally {
            lock.unlock();
        }
    }
}
