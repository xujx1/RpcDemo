package com.xujx.rpc.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ServiceTest {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-config/spring-server.xml");
    }

}
