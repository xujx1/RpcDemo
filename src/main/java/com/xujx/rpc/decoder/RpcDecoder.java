package com.xujx.rpc.decoder;

import com.xujx.rpc.util.protocol.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by xujinxin on 2017/9/7.
 * 解码器
 */
public class RpcDecoder<T> extends ByteToMessageDecoder{

    private Class<T> genericClass;

    public RpcDecoder(Class<T> genericClass) {
        this.genericClass = genericClass;
    }



    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf==null || byteBuf.readableBytes()<4){
            return;
        }
        byteBuf.markReaderIndex();
        int length = byteBuf.readableBytes();


        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        T t= SerializationUtil.deserialize(bytes,genericClass);
        list.add(t);
    }
}
