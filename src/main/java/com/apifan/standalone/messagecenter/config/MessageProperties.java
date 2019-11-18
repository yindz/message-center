package com.apifan.standalone.messagecenter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 消息配置参数
 *
 * @author yindz
 */
@Component
@ConfigurationProperties(prefix = "msg")
public class MessageProperties {

    /**
     * app消息线程池大小
     */
    private Integer appMsgPoolSize = 4;

    /**
     * 短信线程池大小
     */
    private Integer smsPoolSize = 4;

    /**
     * 每秒钟最多发送几条短信
     */
    private Integer maxSmsPerSecond;

    /**
     * 邮件线程池大小
     */
    private Integer emailPoolSize = 4;

    /**
     * 是否启用极光推送
     */
    private boolean jpushEnabled = false;

    /**
     * 极光推送的appKey
     */
    private String jpushAppKey;

    /**
     * 极光推送的密钥
     */
    private String jpushMasterSecret;

    /**
     * 是否启用个推
     */
    private boolean getuiEnabled = false;

    /**
     * 个推的appId
     */
    private String getuiAppId;

    /**
     * 个推的url
     */
    private String getuiUrl;

    /**
     * 个推的密钥
     */
    private String getuiMasterSecret;

    /**
     * 个推的appKey
     */
    private String getuiAppKey;

    /**
     * 是否启用腾讯云短信
     */
    private boolean qcloudSmsEnabled;

    /**
     * 腾讯云短信appId
     */
    private String qcloudSmsAppId;

    /**
     * 腾讯云短信appKey
     */
    private String qcloudSmsAppKey;

    /**
     * 是否启用阿里云短信
     */
    private boolean aliyunSmsEnabled;

    /**
     * 阿里云短信区域
     */
    private String aliyunSmsRegion = "cn-hangzhou";

    /**
     * 阿里云短信accessKey
     */
    private String aliyunSmsAccessKey;

    /**
     * 阿里云短信accessSecret
     */
    private String aliyunSmsAccessSecret;

    /**
     * 是否启用云片短信
     */
    private boolean yunpianSmsEnabled;

    /**
     * 云片apiKey
     */
    private String yunpianSmsApiKey;

    /**
     * 获取 app消息线程池大小
     *
     * @return appMsgPoolSize app消息线程池大小
     */
    public Integer getAppMsgPoolSize() {
        return this.appMsgPoolSize;
    }

    /**
     * 设置 app消息线程池大小
     *
     * @param appMsgPoolSize app消息线程池大小
     */
    public void setAppMsgPoolSize(Integer appMsgPoolSize) {
        this.appMsgPoolSize = appMsgPoolSize;
    }

    /**
     * 获取 短信线程池大小
     *
     * @return smsPoolSize 短信线程池大小
     */
    public Integer getSmsPoolSize() {
        return this.smsPoolSize;
    }

    /**
     * 设置 短信线程池大小
     *
     * @param smsPoolSize 短信线程池大小
     */
    public void setSmsPoolSize(Integer smsPoolSize) {
        this.smsPoolSize = smsPoolSize;
    }

    /**
     * 获取 邮件线程池大小
     *
     * @return emailPoolSize 邮件线程池大小
     */
    public Integer getEmailPoolSize() {
        return this.emailPoolSize;
    }

    /**
     * 设置 邮件线程池大小
     *
     * @param emailPoolSize 邮件线程池大小
     */
    public void setEmailPoolSize(Integer emailPoolSize) {
        this.emailPoolSize = emailPoolSize;
    }

    /**
     * 获取 是否启用极光推送
     *
     * @return jpushEnabled 是否启用极光推送
     */
    public boolean isJpushEnabled() {
        return this.jpushEnabled;
    }

    /**
     * 设置 是否启用极光推送
     *
     * @param jpushEnabled 是否启用极光推送
     */
    public void setJpushEnabled(boolean jpushEnabled) {
        this.jpushEnabled = jpushEnabled;
    }

    /**
     * 获取 极光推送的appKey
     *
     * @return jpushAppKey 极光推送的appKey
     */
    public String getJpushAppKey() {
        return this.jpushAppKey;
    }

    /**
     * 设置 极光推送的appKey
     *
     * @param jpushAppKey 极光推送的appKey
     */
    public void setJpushAppKey(String jpushAppKey) {
        this.jpushAppKey = jpushAppKey;
    }

    /**
     * 获取 极光推送的密钥
     *
     * @return jpushMasterSecret 极光推送的密钥
     */
    public String getJpushMasterSecret() {
        return this.jpushMasterSecret;
    }

    /**
     * 设置 极光推送的密钥
     *
     * @param jpushMasterSecret 极光推送的密钥
     */
    public void setJpushMasterSecret(String jpushMasterSecret) {
        this.jpushMasterSecret = jpushMasterSecret;
    }

    /**
     * 获取 是否启用个推
     *
     * @return getuiEnabled 是否启用个推
     */
    public boolean isGetuiEnabled() {
        return this.getuiEnabled;
    }

    /**
     * 设置 是否启用个推
     *
     * @param getuiEnabled 是否启用个推
     */
    public void setGetuiEnabled(boolean getuiEnabled) {
        this.getuiEnabled = getuiEnabled;
    }

    /**
     * 获取 个推的appId
     *
     * @return getuiAppId 个推的appId
     */
    public String getGetuiAppId() {
        return this.getuiAppId;
    }

    /**
     * 设置 个推的appId
     *
     * @param getuiAppId 个推的appId
     */
    public void setGetuiAppId(String getuiAppId) {
        this.getuiAppId = getuiAppId;
    }

    /**
     * 获取 个推的url
     *
     * @return getuiUrl 个推的url
     */
    public String getGetuiUrl() {
        return this.getuiUrl;
    }

    /**
     * 设置 个推的url
     *
     * @param getuiUrl 个推的url
     */
    public void setGetuiUrl(String getuiUrl) {
        this.getuiUrl = getuiUrl;
    }

    /**
     * 获取 个推的密钥
     *
     * @return getuiMasterSecret 个推的密钥
     */
    public String getGetuiMasterSecret() {
        return this.getuiMasterSecret;
    }

    /**
     * 设置 个推的密钥
     *
     * @param getuiMasterSecret 个推的密钥
     */
    public void setGetuiMasterSecret(String getuiMasterSecret) {
        this.getuiMasterSecret = getuiMasterSecret;
    }

    /**
     * 获取 个推的appKey
     *
     * @return getuiAppKey 个推的appKey
     */
    public String getGetuiAppKey() {
        return this.getuiAppKey;
    }

    /**
     * 设置 个推的appKey
     *
     * @param getuiAppKey 个推的appKey
     */
    public void setGetuiAppKey(String getuiAppKey) {
        this.getuiAppKey = getuiAppKey;
    }

    /**
     * 获取 是否启用腾讯云短信
     *
     * @return qcloudSmsEnabled 是否启用腾讯云短信
     */
    public boolean isQcloudSmsEnabled() {
        return this.qcloudSmsEnabled;
    }

    /**
     * 设置 是否启用腾讯云短信
     *
     * @param qcloudSmsEnabled 是否启用腾讯云短信
     */
    public void setQcloudSmsEnabled(boolean qcloudSmsEnabled) {
        this.qcloudSmsEnabled = qcloudSmsEnabled;
    }

    /**
     * 获取 腾讯云短信appId
     *
     * @return qcloudSmsAppId 腾讯云短信appId
     */
    public String getQcloudSmsAppId() {
        return this.qcloudSmsAppId;
    }

    /**
     * 设置 腾讯云短信appId
     *
     * @param qcloudSmsAppId 腾讯云短信appId
     */
    public void setQcloudSmsAppId(String qcloudSmsAppId) {
        this.qcloudSmsAppId = qcloudSmsAppId;
    }

    /**
     * 获取 腾讯云短信appKey
     *
     * @return qcloudSmsAppKey 腾讯云短信appKey
     */
    public String getQcloudSmsAppKey() {
        return this.qcloudSmsAppKey;
    }

    /**
     * 设置 腾讯云短信appKey
     *
     * @param qcloudSmsAppKey 腾讯云短信appKey
     */
    public void setQcloudSmsAppKey(String qcloudSmsAppKey) {
        this.qcloudSmsAppKey = qcloudSmsAppKey;
    }

    /**
     * 获取 阿里云短信区域
     *
     * @return aliyunSmsRegion 阿里云短信区域
     */
    public String getAliyunSmsRegion() {
        return this.aliyunSmsRegion;
    }

    /**
     * 设置 阿里云短信区域
     *
     * @param aliyunSmsRegion 阿里云短信区域
     */
    public void setAliyunSmsRegion(String aliyunSmsRegion) {
        this.aliyunSmsRegion = aliyunSmsRegion;
    }

    /**
     * 获取 阿里云短信accessKey
     *
     * @return aliyunSmsAccessKey 阿里云短信accessKey
     */
    public String getAliyunSmsAccessKey() {
        return this.aliyunSmsAccessKey;
    }

    /**
     * 设置 阿里云短信accessKey
     *
     * @param aliyunSmsAccessKey 阿里云短信accessKey
     */
    public void setAliyunSmsAccessKey(String aliyunSmsAccessKey) {
        this.aliyunSmsAccessKey = aliyunSmsAccessKey;
    }

    /**
     * 获取 阿里云短信accessSecret
     *
     * @return aliyunSmsAccessSecret 阿里云短信accessSecret
     */
    public String getAliyunSmsAccessSecret() {
        return this.aliyunSmsAccessSecret;
    }

    /**
     * 设置 阿里云短信accessSecret
     *
     * @param aliyunSmsAccessSecret 阿里云短信accessSecret
     */
    public void setAliyunSmsAccessSecret(String aliyunSmsAccessSecret) {
        this.aliyunSmsAccessSecret = aliyunSmsAccessSecret;
    }

    /**
     * 获取 是否启用阿里云短信
     *
     * @return aliyunSmsEnabled 是否启用阿里云短信
     */
    public boolean isAliyunSmsEnabled() {
        return this.aliyunSmsEnabled;
    }

    /**
     * 设置 是否启用阿里云短信
     *
     * @param aliyunSmsEnabled 是否启用阿里云短信
     */
    public void setAliyunSmsEnabled(boolean aliyunSmsEnabled) {
        this.aliyunSmsEnabled = aliyunSmsEnabled;
    }

    /**
     * 获取 是否启用云片短信
     *
     * @return yunpianSmsEnabled 是否启用云片短信
     */
    public boolean isYunpianSmsEnabled() {
        return this.yunpianSmsEnabled;
    }

    /**
     * 设置 是否启用云片短信
     *
     * @param yunpianSmsEnabled 是否启用云片短信
     */
    public void setYunpianSmsEnabled(boolean yunpianSmsEnabled) {
        this.yunpianSmsEnabled = yunpianSmsEnabled;
    }

    /**
     * 获取 云片apiKey
     *
     * @return yunpianSmsApiKey 云片apiKey
     */
    public String getYunpianSmsApiKey() {
        return this.yunpianSmsApiKey;
    }

    /**
     * 设置 云片apiKey
     *
     * @param yunpianSmsApiKey 云片apiKey
     */
    public void setYunpianSmsApiKey(String yunpianSmsApiKey) {
        this.yunpianSmsApiKey = yunpianSmsApiKey;
    }

    /**
     * 获取 每秒钟最多发送几条短信
     *
     * @return maxSmsPerSecond 每秒钟最多发送几条短信
     */
    public Integer getMaxSmsPerSecond() {
        return this.maxSmsPerSecond;
    }

    /**
     * 设置 每秒钟最多发送几条短信
     *
     * @param maxSmsPerSecond 每秒钟最多发送几条短信
     */
    public void setMaxSmsPerSecond(Integer maxSmsPerSecond) {
        this.maxSmsPerSecond = maxSmsPerSecond;
    }
}
