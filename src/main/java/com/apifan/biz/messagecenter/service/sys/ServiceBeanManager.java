package com.apifan.biz.messagecenter.service.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 服务Bean管理器
 *
 * @author yinzl
 */
@Component
public class ServiceBeanManager {

    @Autowired
    private ApplicationContext context;

    /**
     * 获取Spring管理的Bean
     * @param name
     * @param type
     * @param <T>
     * @return
     */
    public <T> T getServiceBean(String name, Class<T> type){
        return context.getBean(name, type);
    }

    /**
     * 获取Spring管理的Bean名称列表
     * @param type
     * @param <T>
     * @return
     */
    public <T> List<String> getServiceNameList(Class<T> type){
        if(type == null){
            return Arrays.asList(context.getBeanDefinitionNames());
        }
        else{
            return Arrays.asList(context.getBeanNamesForType(type));
        }
    }
}
