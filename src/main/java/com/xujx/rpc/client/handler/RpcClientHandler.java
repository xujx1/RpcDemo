package com.xujx.rpc.client.handler;

import com.xujx.rpc.dto.request.RpcRequest;
import com.xujx.rpc.dto.response.RpcResponse;
import com.xujx.rpc.util.protocol.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;


/**
 * Created by xujinxin on 2017/9/7.
 * 服务器端信息处理器
 */
@ChannelHandler.Sharable
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse>{

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClientHandler.class);

    private volatile ChannelHandlerContext channelHandlerContext;

    private CountDownLatch countDownLatch = new CountDownLatch(1);


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channelHandlerContext = ctx;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {

        System.out.println("Client received: "+rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("client caught exception", cause);
        ctx.close();
    }

    public Object send(RpcRequest request){
        ByteBuf byteBuf= Unpooled.copiedBuffer(SerializationUtil.serialize(request));
        channelHandlerContext.writeAndFlush(byteBuf).addListener((ChannelFutureListener) channelFuture -> {
            System.out.println("Client send: "+request);
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "send";
    }
}
