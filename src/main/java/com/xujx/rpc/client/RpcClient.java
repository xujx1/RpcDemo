package com.xujx.rpc.client;

import com.xujx.rpc.proxy.ObjectProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

/**
 * Created by xujinxin on 2017/9/7.
 */
@Component
public class RpcClient {

    @SuppressWarnings("unchecked")
    public  <T> T create(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ObjectProxy()
        );
    }
}
