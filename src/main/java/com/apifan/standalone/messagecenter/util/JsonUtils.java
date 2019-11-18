package com.apifan.standalone.messagecenter.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JSON工具
 *
 * @author yindz
 */
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 解析json字符串为对象
     *
     * @param json 待解析的json字符串
     * @param type 类型
     * @param <T>  泛型类型
     * @return 对象
     */
    public static <T> T readAsObject(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error("解析json出现异常", e);
        }
        return null;
    }

    /**
     * 解析json字节数组为对象
     *
     * @param data 待解析的json字节数组
     * @param type 类型
     * @param <T>  泛型类型
     * @return 对象
     */
    public static <T> T readAsObject(byte[] data, Class<T> type) {
        try {
            return mapper.readValue(data, type);
        } catch (IOException e) {
            logger.error("解析json出现异常", e);
        }
        return null;
    }

    /**
     * 解析json字符串为map
     *
     * @param json 待解析的json字符串
     * @return map 实例
     */
    public static Map<String, Object> readAsMap(String json) {
        try {
            return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            logger.error("解析json出现异常", e);
        }
        return null;
    }

    /**
     * 解析json字符串为map列表
     *
     * @param json 待解析的json字符串
     * @return map列表
     */
    public static List<Map<String, Object>> readAsMapList(String json) {
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, Map.class);
            return mapper.readValue(json, collectionType);
        } catch (IOException e) {
            logger.error("解析json出现异常", e);
        }
        return null;
    }

    /**
     * 解析json字符串为List
     *
     * @param json  待解析的json字符串
     * @param clazz 类
     * @param <T>   泛型
     * @return list实例
     */
    public static <T> List<T> readAsList(String json, Class<T> clazz) {
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return mapper.readValue(json, collectionType);
        } catch (IOException e) {
            logger.error("解析json出现异常", e);
        }
        return null;
    }

    /**
     * 解析json字符串为数组
     *
     * @param json  待解析的json字符串
     * @param clazz 类
     * @param <T>   泛型
     * @return 数组
     */
    public static <T> T[] readAsArray(String json, Class<T> clazz) {
        try {
            ArrayType arrayType = mapper.getTypeFactory().constructArrayType(clazz);
            return mapper.readValue(json, arrayType);
        } catch (IOException e) {
            logger.error("解析json出现异常", e);
        }
        return null;
    }

    /**
     * 对象转换为json
     *
     * @param obj      对象
     * @param beautify 是否美化格式
     * @return json字符串
     */
    public static String toJson(Object obj, boolean beautify) {
        try {
            return beautify ? mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj) : mapper.writeValueAsString(obj);
        } catch (IOException e) {
            logger.error("生成json出现异常", e);
        }
        return null;
    }

    /**
     * 对象转换为json
     * 不带美化格式
     *
     * @param obj 对象
     * @return json字符串
     */
    public static String toJson(Object obj) {
        return toJson(obj, false);
    }
}
