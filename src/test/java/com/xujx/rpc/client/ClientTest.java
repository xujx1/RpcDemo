package com.xujx.rpc.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by xujinxin on 2017/9/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config/spring-client.xml")
public class ClientTest {

    @Autowired
    private RpcClient rpcClient;

    @Test
    public void helloTest() throws InterruptedException {
        HelloService helloService = rpcClient.create(HelloService.class);
        helloService.hello("World");
        Thread.sleep(10000L);
    }
}
