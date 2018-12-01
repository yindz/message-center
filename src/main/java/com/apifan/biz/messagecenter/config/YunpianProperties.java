package com.apifan.biz.messagecenter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 云片参数
 *
 * @author yinzl
 */
@Component
@ConfigurationProperties(prefix = "yunpian")
public class YunpianProperties {

    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
