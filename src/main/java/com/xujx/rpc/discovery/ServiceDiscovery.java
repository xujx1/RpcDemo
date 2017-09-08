package com.xujx.rpc.discovery;

import com.xujx.rpc.client.ConnectManage;
import com.xujx.rpc.util.zk.ZkUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xujinxin on 2017/9/6.
 * 服务探查
 */
public class ServiceDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class);

    @Value("${zk.base_path}")
    private String zkBasePath;

    @Resource
    private CuratorFramework curatorFramework;

    @Resource
    private ZkUtil zkUtil;

    private void watchNode()  {
        try {
            List<String> childList = curatorFramework.getChildren().usingWatcher((Watcher) event -> {
                if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    watchNode();
                }
            }).forPath(zkBasePath);

            if(childList!=null && childList.size()!=0){
                List<String> dataList = new ArrayList<>();
                for (String node : childList) {
                    String data = zkUtil.getData(zkBasePath.concat("/").concat(node));
                    dataList.add(data);
                }
                ConnectManage.getInstance().updateConnectedServer(dataList);
            }
        } catch (Exception e) {
            LOGGER.error("监听子节点变化异常,{}", e);
        }

    }

    public void init()  {
        Thread thread = new Thread(this::watchNode,this.getClass().getName());
        thread.setDaemon(true);
        thread.start();
    }
}
