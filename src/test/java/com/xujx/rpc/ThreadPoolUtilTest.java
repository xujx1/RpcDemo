package com.xujx.rpc;

import com.xujx.rpc.util.threadpool.ThreadPoolUtil;
import org.junit.Test;

/**
 * Created by xujinxin on 2017/9/7.
 */
public class ThreadPoolUtilTest {

    @Test
    public void test() throws InterruptedException {
        ThreadPoolUtil.submit(() -> System.out.println("====="));
        Thread.sleep(1000L);
    }
}
