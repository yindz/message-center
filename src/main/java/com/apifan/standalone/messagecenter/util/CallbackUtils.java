package com.apifan.standalone.messagecenter.util;

import com.apifan.standalone.messagecenter.constant.CommonConstant;
import com.apifan.standalone.messagecenter.util.http.HttpResponse;
import com.apifan.standalone.messagecenter.util.http.HttpUtils;
import com.apifan.standalone.messagecenter.vo.Message;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 回调工具类
 *
 * @author yindz
 */
public class CallbackUtils {
    private static final Logger logger = LoggerFactory.getLogger(CallbackUtils.class);

    private static final int CALLBACK_THREADS = 8;

    private static Map<String, String> headersMap = Maps.newHashMap();

    static {
        headersMap.put("User-Agent", "message-center-callback");
    }

    //使用固定的线程池去执行回调
    private static ExecutorService callbackPool = new ThreadPoolExecutor(CALLBACK_THREADS, CALLBACK_THREADS,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(CALLBACK_THREADS * 2048), new ThreadFactoryBuilder().setNameFormat("callback-thread-%d").build(), new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 执行回调
     *
     * @param message  消息
     * @param success  消息是否发送成功
     * @param errorMsg 消息发送失败时的错误信息
     */
    public static void executeCallback(Message message, boolean success, String errorMsg) {
        if (message == null) {
            return;
        }
        if (StringUtils.isEmpty(message.getCallbackUrl())) {
            return;
        }
        if (!message.getCallbackUrl().startsWith("http://") && !message.getCallbackUrl().startsWith("https://")) {
            return;
        }
        callbackPool.submit(() -> {
            Map<String, Object> paramsMap = Maps.newHashMap();
            paramsMap.put("messageId", message.getMessageId());
            paramsMap.put("success", success);
            paramsMap.put("errorMsg", errorMsg);
            logger.info("准备开始回调! URL: {}, messageId={}, success={}, errorMsg={}", message.getCallbackUrl(), message.getMessageId(), success, errorMsg);
            try {
                HttpResponse res = HttpUtils.postJson(message.getCallbackUrl(), headersMap, JsonUtils.toJson(paramsMap));
                if (res == null) {
                    logger.error("回调失败! 返回值为空");
                    return;
                }
                if (res.getCode() != CommonConstant.HTTP_OK) {
                    logger.error("回调失败! 返回的HTTP状态码为: {}", res.getCode());
                    return;
                }
                logger.info("回调成功！");
            } catch (IOException e) {
                logger.error("回调时出现异常", e);
            }
        });
    }
}
