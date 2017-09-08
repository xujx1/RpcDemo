package com.xujx.rpc.util.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xujinxin on 2017/9/6.
 * zookeeper 工具类
 */
@Component
public class ZkUtil {

    @Resource
    private  CuratorFramework curatorFramework;

    //增
    public void create(String path, String data, CreateMode mode) throws Exception {
        curatorFramework.create()
                .withMode(mode)
                .forPath(path, data.getBytes());
    }

    //删
    public void delete(String path) throws Exception {
        curatorFramework.delete()
                .deletingChildrenIfNeeded()
                .inBackground()
                .forPath(path);
    }

    //获取子节点
    public List<String> getChildren(String path) throws Exception {
        return curatorFramework.getChildren().forPath(path);
    }

    //查
    public String getData(String path) throws Exception {
        return new String(curatorFramework.getData().forPath(path));
    }

    //改
    public void update(String path, String data) throws Exception {
        curatorFramework.setData().inBackground().forPath(path, data.getBytes());
    }

    //是否存在
    public boolean exists(String path) throws Exception {
        return curatorFramework.checkExists().forPath(path) == null;
    }

}
