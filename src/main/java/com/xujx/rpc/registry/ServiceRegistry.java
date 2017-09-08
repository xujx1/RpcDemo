package com.xujx.rpc.registry;

import com.xujx.rpc.util.zk.ZkUtil;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Created by xujinxin on 2017/9/6.
 * 服务注册
 */
@Component
public class ServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

    @Value("${zk.data_path}")
    private String zkDataPath;

    @Value("${zk.base_path}")
    private String zkBasePath;

    @Resource
    private ZkUtil zkUtil;

    /**
     * 服务注册
     * @throws Exception
     */
    public void register(String data) {
       if(!StringUtils.isEmpty(data)){
           try {

               if(zkUtil.exists(zkBasePath)){
                   zkUtil.create(zkBasePath, data, CreateMode.PERSISTENT);
               }

               zkUtil.create(zkDataPath, data, CreateMode.EPHEMERAL_SEQUENTIAL);
           } catch (Exception e) {
               LOGGER.error("服务注册异常,{}", e);
           }
       }
    }
}
