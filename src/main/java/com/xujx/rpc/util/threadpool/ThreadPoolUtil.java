package com.xujx.rpc.util.threadpool;

import com.xujx.rpc.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * Created by xujinxin on 2017/9/7.
 * 自定义线程池方法
 */
public class ThreadPoolUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

    private static volatile ThreadPoolTaskExecutor threadPoolExecutor;

    private ThreadPoolUtil(){

    }

    private static void init(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16);
        executor.setMaxPoolSize(32);
        executor.setKeepAliveSeconds(600);
        executor.setQueueCapacity(1000);
        executor.setRejectedExecutionHandler((r, executor1) -> {
            LOGGER.error("线程池已满,{}",r);
            executor1.submit(r);
        });
        executor.initialize();
        threadPoolExecutor = executor;
    }


    public static void submit(Runnable task){
        if(threadPoolExecutor == null){
            synchronized (ThreadPoolUtil.class){
                if(threadPoolExecutor==null){
                    init();
                }
            }
            threadPoolExecutor.submit(task);
        }
    }
}
