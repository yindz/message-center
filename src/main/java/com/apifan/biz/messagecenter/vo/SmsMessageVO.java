package com.apifan.biz.messagecenter.vo;

import java.util.Map;

/**
 * 短信消息
 *
 * @author yinzl
 */
public class SmsMessageVO extends MessageVO{
    private static final long serialVersionUID = -4732866581871201935L;

    /**
     * 接收者
     */
    private String receiver;

    /**
     * 内容
     */
    private String content;

    /**
     * 附加业务参数
     */
    private Map<String, String> extras;

    /**
     * 短信发送提供商名称
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
     * 获取 短信发送提供商名称
     *
     * @return provider 短信发送提供商名称
     */
    public String getProvider() {
        return this.provider;
    }

    /**
     * 设置 短信发送提供商名称
     *
     * @param provider 短信发送提供商名称
     */
    public void setProvider(String provider) {
        this.provider = provider;
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
}
