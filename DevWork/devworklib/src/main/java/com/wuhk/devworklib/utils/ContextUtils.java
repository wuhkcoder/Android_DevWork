package com.wuhk.devworklib.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.wuhk.devworklib.DevWork;

import java.util.List;

/**
 * 判断网络或者SD卡等之类的工具类
 * Created by wuhk on 2016/5/27.
 */
public class ContextUtils {

    /**判断是否存在网络连接
     *
     * @return
     */
    public static boolean hasNetWork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) DevWork.getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return null != networkInfo && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    /**判断GPS是否打开
     *
     * @return
     */
    public static boolean isGpsEnable(){
        LocationManager locationManager = (LocationManager)DevWork.getApplication()
                .getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**SD卡是否可用
     *
     * @return
     */
    public static boolean hasSdCard(){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**获取SD卡的根目录
     *
     * @return
     */
    public static String getSdCardPath  (){
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**获取手机本身的的内置存储，一般SD卡不存在的时候使用
     *
     * @return
     */
    public static String  getCacheDirPath(){
        return DevWork.getApplication().getCacheDir().getPath();
    }

    /**获取手机本身的内置存储 （与上述getCacheDir()的区别待查）
     *
     * @return
     */
    public static String getFileDirPath(){
        return DevWork.getApplication().getFilesDir().getPath();
    }

    /**获取SD卡默认缓存路劲
     *
     * @return
     */
    public static String getExternaCacheDirPath(){
        return DevWork.getApplication().getExternalCacheDir().getPath();
    }

    /**是否有sim卡
     *
     * @return
     */
    public static boolean hasSim(){
        TelephonyManager telephonyManager = (TelephonyManager) DevWork.getApplication()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /**显示或者隐藏键盘
     *
     * @param view
     * @param isShow
     */
    public static void showSoftInput(View view , boolean isShow){
        InputMethodManager inputMethodManager = (InputMethodManager)DevWork.getApplication()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow){
            inputMethodManager.showSoftInput(view , InputMethodManager.SHOW_FORCED);
        }else{
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken() , 0);
        }
    }

    /**
     * 显示软键盘
     *
     * @param editText
     */
    public static void showSoftInput(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        showSoftInput(editText, true);
    }

    /**
     * 隐藏软键盘
     *
     * @param editText
     */
    public static void hideSoftInput(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        showSoftInput(editText, false);
    }

    /**判断是否处于后台
     *
     * @return
     */
    public static boolean isBackground(){
        ActivityManager activityManager = (ActivityManager)DevWork.getApplication()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos
                = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos){
            if (appProcessInfo.processName.equals(DevWork.getApplication().getPackageName())){
                LogUtils.i("此appimportance = " + appProcessInfo.importance
                + ", context.getClass().getName()=" + DevWork.getApplication().getClass().getName());
            }

            if (appProcessInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }


}
