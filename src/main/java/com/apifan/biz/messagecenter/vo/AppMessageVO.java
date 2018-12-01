package com.apifan.biz.messagecenter.vo;

import java.util.Map;

/**
 * APP消息
 *
 * @author yinzl
 */
public class AppMessageVO extends MessageVO{
    private static final long serialVersionUID = 7539543631361705983L;

    /**
     * 接收者
     */
    private String receiver;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 附加业务参数
     */
    private Map<String, String> extras;

    /**
     * 推送服务提供者
     */
    private String provider;

    /**
     * 获取 接收者
     *
     * @return receiver 接收者
     */
    public String getReceiver() {
        return this.receiver;
    }

    /**
     * 设置 接收者
     *
     * @param receiver 接收者
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * 获取 内容
     *
     * @return content 内容
     */
    public String getContent() {
        return this.content;
    }

    /**
     * 设置 内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取 标题
     *
     * @return title 标题
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * 设置 标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取 附加业务参数
     *
     * @return extras 附加业务参数
     */
    public Map<String, String> getExtras() {
        return this.extras;
    }

    /**
     * 设置 附加业务参数
     *
     * @param extras 附加业务参数
     */
    public void setExtras(Map<String, String> extras) {
        this.extras = extras;
    }

    /**
     * 获取 推送服务提供者
     *
     * @return provider 推送服务提供者
     */
    public String getProvider() {
        return this.provider;
    }

    /**
     * 设置 推送服务提供者
     *
     * @param provider 推送服务提供者
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }
}
