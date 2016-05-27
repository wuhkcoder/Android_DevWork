package com.wuhk.devworklib.utils;

import java.util.Collection;
import java.util.Objects;

/**
 * 验证工具类
 * Created by wuhk on 2016/5/27.
 */
public abstract class VerifyUtils {

    /**当数组为空或者长度为0，又或者长度为1且元素的值位空是返回true
     *
     * @param args
     * @return
     */
    public static boolean isEmpty(Objects[] args){
        return args == null || args.length == 0
                || (args.length == 1 && args[0] == null);
    }


    /**字符串为空或者空格都算是空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0;
    }


    /**判断集合是否为空，当空或者长度为0时
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(Collection<T> collection){
        return collection == null || collection.isEmpty();
    }

    /**是否为数字的字符串
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        if (isEmpty(str)){
            return false;
        }
        for (int i = 0 ; i < str.length() ; i++){
            if (str.charAt(i) > '9' || str.charAt(i) < '0'){
                return false;
            }
        }
        return true;
    }

    /**是否是固定范围内的字符串
     *
     * @param str
     * @param min
     * @param max
     * @return
     */
    public static boolean isNumber(String str , int min , int max){
        if (!isNumber(str)){
            return false;
        }

        int number = Integer.parseInt(str);

        return number >= min && max <=max;
    }


}
