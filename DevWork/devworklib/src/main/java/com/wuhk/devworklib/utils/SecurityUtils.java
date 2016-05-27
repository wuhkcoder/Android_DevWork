package com.wuhk.devworklib.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**加解密工具类，目前只包含MD5，后续增加
 * Created by wuhk on 2016/5/27.
 */
public abstract class SecurityUtils {
    /**
     * 使用 MD5 对字符串加密。
     *
     * @param str 源字符串
     * @return 加密后字符串
     */
    public static String encodeByMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return toHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not encodeByMD5", e);
        }
    }

    /**
     * 使用 MD5 对字节数组加密。
     *
     * @param bytes 源字符 byte 数组
     * @return 加密后字符串
     */
    public static String encodeByMD5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            return toHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not encodeByMD5", e);
        }
    }

    /**
     * 把一个字节数组转换为16进制表达的字符串
     *
     * @param bytes
     * @return
     */
    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            hexString
                    .append(enoughZero(Integer.toHexString(bytes[i] & 0xff), 2));
        }
        return hexString.toString();
    }


    /**
     * 在字符串str左边补齐0直到长度等于length
     *
     * @param str
     * @param len
     * @return
     */
    private static String enoughZero(String str, int len) {
        while (str.length() < len) {
            str = "0" + str;
        }
        return str;
    }

}
