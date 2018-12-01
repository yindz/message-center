package com.apifan.biz.messagecenter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 极光推送参数
 *
 * @author yinzl
 */
@Component
@ConfigurationProperties(prefix = "jpush")
public class JPushProperties {

    private String masterSecret;

    private String appKey;

    public String getMasterSecret() {
        return masterSecret;
    }

    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
