package com.apifan.standalone.messagecenter.util;

import com.apifan.standalone.messagecenter.constant.CommonConstant;
import com.google.common.base.CaseFormat;
import com.google.common.base.Charsets;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

/**
 * 字符串工具类
 *
 * @author yindz
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

    /**
     * 判断字符串是否非空(过滤空格)
     *
     * @param src 待检测的字符串
     * @return 是否非空
     */
    public static boolean notEmpty(String src) {
        if (isEmpty(src)) {
            return false;
        }
        return isNotEmpty(src.trim());
    }

    /**
     * 生成随机字符串
     *
     * @param length     长度
     * @param digitsOnly 是否仅含数字
     * @return 随机字符串
     */
    public static String getRandomString(int length, boolean digitsOnly) {
        RandomStringGenerator.Builder builder = new RandomStringGenerator.Builder().withinRange('0', 'z');
        if (digitsOnly) {
            return builder.filteredBy(DIGITS).build().generate(length);
        }
        return builder.filteredBy(LETTERS, DIGITS).build().generate(length);
    }

    /**
     * 对字符串进行URLEncode
     *
     * @param src 原始字符串
     * @param enc 编码名称
     * @return 编码后的字符串
     */
    public static String urlEncode(String src, String enc) {
        if (isEmpty(src)) {
            return null;
        }
        try {
            return URLEncoder.encode(src, enc);
        } catch (UnsupportedEncodingException e) {
            logger.error("URLEncode出现异常", e);
        }
        return src;
    }

    /**
     * 使用UTF-8编码对字符串进行URLEncode
     *
     * @param src 原始字符串
     * @return 编码后的字符串
     */
    public static String urlEncode(String src) {
        return urlEncode(src, CommonConstant.DEFAULT_CHARSET);
    }

    /**
     * 哈希运算
     *
     * @param src  待计算的字符串
     * @param type 算法类型
     * @return 结果
     */
    private static String hash(String src, String type) {
        if (isEmpty(src)) {
            return null;
        }
        byte[] bytes = src.getBytes(Charsets.UTF_8);
        if ("md5".equalsIgnoreCase(type)) {
            return DigestUtils.md5Hex(bytes);
        } else if ("sha1".equalsIgnoreCase(type)) {
            return DigestUtils.sha1Hex(bytes);
        } else if ("sha256".equalsIgnoreCase(type)) {
            return DigestUtils.sha256Hex(bytes);
        } else if ("sha512".equalsIgnoreCase(type)) {
            return DigestUtils.sha512Hex(bytes);
        } else {
            logger.error("暂不支持的算法: {}", type);
        }
        return null;
    }

    /**
     * md5计算
     *
     * @param src 待计算的字符串
     * @return md5字符串
     */
    public static String md5(String src) {
        return hash(src, "md5");
    }

    /**
     * sha1计算
     *
     * @param src 待计算的字符串
     * @return sha1字符串
     */
    public static String sha1(String src) {
        return hash(src, "sha1");
    }

    /**
     * sha256计算
     *
     * @param src 待计算的字符串
     * @return sha256字符串
     */
    public static String sha256(String src) {
        return hash(src, "sha256");
    }

    /**
     * sha512计算
     *
     * @param src 待计算的字符串
     * @return sha512字符串
     */
    public static String sha512(String src) {
        return hash(src, "sha512");
    }

    /**
     * 下划线转驼峰
     *
     * @param underlineStr 下划线字符串
     * @return 驼峰字符串
     */
    public static String underlineToCamel(String underlineStr) {
        if (isEmpty(underlineStr)) {
            return "";
        }
        if (Character.isUpperCase(underlineStr.trim().charAt(0))) {
            //首字母为大写，则转换为大写
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, underlineStr);
        } else {
            return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, underlineStr.toLowerCase());
        }
    }

    /**
     * 驼峰转下划线
     *
     * @param camelStr 驼峰字符串
     * @return 下划线字符串
     */
    public static String camelToUnderline(String camelStr) {
        if (isEmpty(camelStr)) {
            return "";
        }
        if (Character.isUpperCase(camelStr.trim().charAt(0))) {
            //首字母为大写，则转换为大写
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, camelStr);
        } else {
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, camelStr);
        }
    }

    /**
     * HTML转义
     *
     * @param html 待转义字符串
     * @return 转义后字符串
     */
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    /**
     * HTML反转义(还原)
     *
     * @param html 转义后字符串
     * @return 还原的html字符串
     */
    public static String unescapeHtml(String html) {
        return StringEscapeUtils.unescapeHtml4(html);
    }
}
