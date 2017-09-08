package com.xujx.rpc.scanner;

import com.xujx.rpc.annotation.RpcService;
import com.xujx.rpc.decoder.RpcDecoder;
import com.xujx.rpc.dto.request.RpcRequest;
import com.xujx.rpc.encoder.RpcEncoder;
import com.xujx.rpc.registry.ServiceRegistry;
import com.xujx.rpc.server.handler.RpcServerHandler;
import com.xujx.rpc.util.spring.BeanContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xujinxin on 2017/9/7.
 * RpcService scanner
 */
public class RpcServer {

    private Map<String, Object> handlerMap = new ConcurrentHashMap<>();

    @Value("${server.address}")
    private String serverAddress;

    @Resource
    private ServiceRegistry serviceRegistry;

    public void bind() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        String[] array = serverAddress.split(":");
        int port = Integer.parseInt(array[1]);

        try {
        bootstrap
                .group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(port)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new RpcDecoder<>(RpcRequest.class))
                                .addLast(new RpcServerHandler(handlerMap))
                                .addLast(new RpcEncoder());

                    }
                });

            ChannelFuture future = bootstrap.bind().sync();


            serviceRegistry.register(serverAddress);

            future.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void init() throws Exception {
        getHandlerMap();
        bind();
    }

    private void getHandlerMap(){
        Map<String, Object> map= BeanContext.getContext().getBeansWithAnnotation(RpcService.class);

        if(null!=map&&!map.isEmpty()){
            map.forEach((s, o) -> {
                String interfaceName= o.getClass().getAnnotation(RpcService.class).value().getName();
                handlerMap.put(interfaceName,o);
            });
        }
    }
}
