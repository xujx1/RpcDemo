package com.xujx.rpc;

import com.xujx.rpc.dto.request.RpcRequest;
import com.xujx.rpc.dto.response.RpcResponse;
import com.xujx.rpc.util.protocol.SerializationUtil;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by xujinxin on 2017/9/6.
 */
public class SerializationTest {

    @Test
    public void serializationTest() throws InstantiationException, IllegalAccessException {

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(10L);
        rpcRequest.setClassName(RpcRequest.class.getName());
        rpcRequest.setMethodName("getRequestId");
        rpcRequest.setParameterTypes(new Class[]{RpcRequest.class, RpcResponse.class});
        rpcRequest.setParameters(new Object[]{new RpcRequest(),new RpcResponse()});
        byte[] bytes =SerializationUtil.serialize(rpcRequest);

        System.out.println(Arrays.toString(bytes));

        System.out.println(SerializationUtil.deserialize(bytes,RpcRequest.class));
    }
}
