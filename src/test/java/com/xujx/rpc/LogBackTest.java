package com.xujx.rpc;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xujinxin on 2017/9/6.
 */
public class LogBackTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogBackTest.class);
    @Test
    public void test(){
        LOGGER.info("[{}]的{}",this.getClass(),Thread.currentThread().getStackTrace()[1]);
    }
}
