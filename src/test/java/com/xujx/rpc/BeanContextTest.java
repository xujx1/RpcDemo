package com.xujx.rpc;


import com.xujx.rpc.util.spring.BeanContext;
import org.apache.curator.framework.CuratorFramework;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by xujinxin on 2017/7/7.
 * 测试BeanContext
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config/spring-server.xml")
public class BeanContextTest {

    @Test
    public void test() throws Exception {
        CuratorFramework curatorFramework = BeanContext.getContext().getBean(CuratorFramework.class);
        System.out.println(curatorFramework);
        curatorFramework.getChildren().forPath("/curator").forEach(System.out::println);
    }
}
