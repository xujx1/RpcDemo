package com.xujx.rpc.util.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by xujinxin on 2017/9/6.
 * CuratorFrameworkFactoryBean
 */
@Component
public class CuratorFrameworkFactoryBean implements FactoryBean<CuratorFramework> {

    @Value("${registry.address}")
    private String registryAddress;

    @Value("${zk.timeout}")
    private String zkTimeOut;



    public CuratorFramework getObject() throws Exception {

        CuratorFramework curatorFramework =
                CuratorFrameworkFactory.builder()
                        .connectString(registryAddress)
                        .sessionTimeoutMs(Integer.parseInt(zkTimeOut))
                        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                        .build();
        curatorFramework.start();


        return curatorFramework;
    }

    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
