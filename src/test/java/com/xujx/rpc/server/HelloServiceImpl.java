package com.xujx.rpc.server;

import com.xujx.rpc.annotation.RpcService;
import com.xujx.rpc.client.HelloService;
import org.springframework.stereotype.Service;

@RpcService(HelloService.class)
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public void hello(String name) {
        System.out.println(name);
    }

}
