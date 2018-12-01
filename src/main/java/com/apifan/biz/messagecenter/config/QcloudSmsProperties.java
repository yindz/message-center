package com.apifan.biz.messagecenter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 腾讯云短信接口参数
 *
 * @author yinzl
 */
@Component
@ConfigurationProperties(prefix = "qcloudsms")
public class QcloudSmsProperties {

    private Integer appId;
    private String appKey;
    private String signName;

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }
}
