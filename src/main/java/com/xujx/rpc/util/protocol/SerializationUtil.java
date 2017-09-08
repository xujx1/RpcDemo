package com.xujx.rpc.util.protocol;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * Created by xujinxin on 2017/9/6.
 * 序列号工具
 */
public class SerializationUtil {

    private SerializationUtil(){

    }

    /**
     * 序列化（对象 -> 字节数组）
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T t){
        Class<T> clazz= (Class<T>) t.getClass();
        Schema<T> schema = RuntimeSchema.getSchema(clazz);

        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

        return ProtostuffIOUtil.toByteArray(t, schema, buffer);
    }

    /**
     * 反序列化（字节数组 -> 对象）
     */
    public static <T> T deserialize(byte[] bytes,Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T t =  clazz.newInstance();
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        ProtostuffIOUtil.mergeFrom(bytes, t, schema);
        return t;
    }
}
