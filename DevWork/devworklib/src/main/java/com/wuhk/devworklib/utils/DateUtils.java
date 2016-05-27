package com.wuhk.devworklib.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 处理时间的工具类
 * Created by wuhk on 2016/5/27.
 */
public abstract class DateUtils {
    /**按照指定格式把时间转换成字符串，格式写法：yyyy-MM-dd HH:mm:ss.SSS
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String date2String(Date date , String pattern){
        if (date == null){
            return null;
        }
        return (new SimpleDateFormat(pattern).format(date));
    }

    /**
     * 把时间转换成字符串，格式为2006-01-10 20:56:30.756
     *
     * @param date
     *            时间
     * @return 时间字符串
     */
    public static String date2String(Date date) {
        return date2String(date, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * 把时间转换成字符串，格式为2006-01-10
     *
     * @param date
     *            时间
     * @return 时间字符串
     */
    public static String date2StringByDay(Date date) {
        return date2String(date, "yyyy-MM-dd");
    }

    /**
     * 把时间转换成字符串，格式为2006-01-10 20:56
     *
     * @param date
     *            时间
     * @return 时间字符串
     */
    public static String date2StringByMinute(Date date) {
        return date2String(date, "yyyy-MM-dd HH:mm");
    }

    /**
     * 把时间转换成字符串，格式为2006-01-10 20:56:30
     *
     * @param date
     *            时间
     * @return 时间字符串
     */
    public static String date2StringBySecond(Date date) {
        return date2String(date, "yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 获取星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }
}
