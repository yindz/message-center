package com.apifan.standalone.messagecenter.vo;

import java.util.List;
import java.util.Map;

/**
 * APP消息
 *
 * @author yindz
 */
public class AppMessage extends Message {
    private static final long serialVersionUID = 7539543631361705983L;

    /**
     * 接收者列表
     */
    private List<String> receiverList;

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
     * 获取 接收者
     *
     * @return receiverList 接收者
     */
    public List<String> getReceiverList() {
        return this.receiverList;
    }

    /**
     * 设置 接收者
     *
     * @param receiverList 接收者
     */
    public void setReceiverList(List<String> receiverList) {
        this.receiverList = receiverList;
    }
}
