package com.xujx.rpc;

import com.xujx.rpc.client.ConnectManage;
import com.xujx.rpc.util.zk.ZkUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.Watcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by xujinxin on 2017/9/7.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-config/spring-server.xml")
public class ZkUtilTest {

    @Value("${zk.base_path}")
    private String zkBasePath;

    @Resource
    private ZkUtil zkUtil;

    @Resource
    private CuratorFramework curatorFramework;

    @Test
    public void test() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        List<String> childList = curatorFramework.getChildren().usingWatcher((Watcher) event -> {
            if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
            }
        }).forPath(zkBasePath);

        if(childList!=null && childList.size()!=0){
            List<String> dataList = new ArrayList<>();
            for (String node : childList) {
                String data = zkUtil.getData(zkBasePath.concat("/").concat(node));
                dataList.add(data);
            }
            dataList.forEach(System.out::println);
        }
        countDownLatch.await();
    }
}
