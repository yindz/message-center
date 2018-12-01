package com.apifan.biz.messagecenter.controller;

import com.apifan.biz.messagecenter.service.app.IAppMsgPushService;
import com.apifan.biz.messagecenter.service.sms.ISmsMsgPushService;
import com.apifan.biz.messagecenter.service.sys.ServiceBeanManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 提供商查询接口
 *
 * @author yinzl
 */
@RequestMapping("/provider")
@RestController
public class ProviderController {

    @Autowired
    private ServiceBeanManager serviceBeanManager;

    /**
     * 查询短信推送提供商的名称
     * @return
     */
    @RequestMapping(value = "/smsProviderList")
    public List<String> smsProviderList(){
        return serviceBeanManager.getServiceNameList(ISmsMsgPushService.class);
    }

    /**
     * 查询短信推送提供商的名称
     * @return
     */
    @RequestMapping(value = "/appMsgProviderList")
    public List<String> appMsgProviderList(){
        return serviceBeanManager.getServiceNameList(IAppMsgPushService.class);
    }
}
