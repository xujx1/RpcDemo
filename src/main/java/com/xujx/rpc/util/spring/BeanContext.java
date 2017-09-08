package com.xujx.rpc.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by xujinxin on 2017/9/6.
 * 辅助访问Spring
 */
@Component
public class BeanContext implements ApplicationContextAware{

    private static ApplicationContext context = null;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext(){
        return context;
    }

}
