package com.wuhk.devworklib.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * 屏幕相关显示工具类
 * Created by wuhk on 2016/5/27.
 */
public abstract class ScreenDisplayUtils {

    /**返回屏幕参数
     *
     * density:在分辨率是320*480的手机上该值是：1.0<br>
     * scaledDensity：在分辨率是320*480的手机上该值是：1.0（针对字体）<br>
     * densityDpi：在分辨率是320*480的手机上该值是：160（表示每英寸px像素点）<br>
     * heightPixels和widthPixels：分别表示屏幕的高和宽，单位px<br>
     * xdpi和ydpi：分别表示屏幕的x方向和y方向的dp值<br>
     *
     * @param activity
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * 获取屏幕的宽，单位Px
     *
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        return getDisplayMetrics(activity).widthPixels;
    }

    /**
     * 获取屏幕的高，单位Px
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        return getDisplayMetrics(activity).heightPixels;
    }

    /**
     * dp转成px
     *
     * @param activity
     * @param dpValue
     *            dp单位的值
     * @return
     */
    public static float getPxByDp(Activity activity, float dpValue) {
        return dpValue * getDisplayMetrics(activity).density;
    }

    /**
     * px转成dp
     *
     * @param activity
     * @param pxValue
     *            px单位的值
     * @return
     */
    public static float getDpByPx(Activity activity, float pxValue) {
        return pxValue / getDisplayMetrics(activity).density;
    }

    /**
     * sp转成px
     *
     * @param activity
     * @param spValue
     *            sp为单位的值
     * @return
     */
    public static float getPxBySp(Activity activity, float spValue) {
        return spValue * getDisplayMetrics(activity).scaledDensity;
    }

    /**
     * px转成sp
     *
     * @param activity
     * @param pxValue
     *            px为单位的值
     * @return
     */
    public static float getSpByPx(Activity activity, float pxValue) {
        return pxValue / getDisplayMetrics(activity).scaledDensity;
    }
}
