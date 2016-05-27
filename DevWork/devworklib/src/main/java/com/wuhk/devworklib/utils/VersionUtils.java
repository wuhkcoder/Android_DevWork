package com.wuhk.devworklib.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.wuhk.devworklib.DevWork;

/**
 * 获取一些版本信息的工具类
 * Created by wuhk on 2016/5/27.
 */
public abstract class VersionUtils {


    /**得到版本代码versionCode，主版本号
     *
     * @return
     */
    public static int getVersionCode(){
        int versionCode = -1;

        try {
            PackageInfo packageInfo = DevWork.getApplication()
                    .getPackageManager().getPackageInfo(DevWork.getApplication()
                    .getPackageName() , 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**获取versionName
     *
     * @return
     */
    public static String getVersionName(){
        String versionName = "";
        try {
            PackageInfo packageInfo = DevWork.getApplication()
                    .getPackageManager().getPackageInfo(DevWork.getApplication()
                    .getPackageName() , 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    /**获取应用程序图标
     *
     * @return
     */
    public static int getApplicationIcon(){
        try {
            PackageInfo packageInfo = DevWork
                    .getApplication()
                    .getPackageManager()
                    .getPackageInfo(
                            DevWork.getApplication().getPackageName(),
                            0);
            return packageInfo.applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
