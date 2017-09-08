package com.xujx.rpc.encoder;

import com.xujx.rpc.util.protocol.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by xujinxin on 2017/9/7.
 * 编码器
 */
public class RpcEncoder extends MessageToByteEncoder{

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf byteBuf) throws Exception {
        byte[] data = SerializationUtil.serialize(obj);
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }
}
