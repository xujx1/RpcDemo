package com.xujx.rpc;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by xujinxin on 2017/9/7.
 */
public class DaemonTest {

    @Test
    public void daemonTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread thread=  new Thread(new Runnable() {
            public void run() {
                System.out.println("test");
            }
        });
        thread.setDaemon(true);
        thread.start();
        countDownLatch.await();
    }
}
