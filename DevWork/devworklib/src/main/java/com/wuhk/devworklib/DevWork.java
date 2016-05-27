package com.wuhk.devworklib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.wuhk.devworklib.utils.LogUtils;

/**需要使用DevWork的要在Application实例初始化的时候调用DevWork.init();
 * Created by wuhk on 2016/5/24.
 */
public abstract class DevWork {
    public static Context application;

    /**初始化
     *
     * @param context
     */
    public static void init(Context context){
        if (null == context){
            LogUtils.e("DevWork can not init. Cause context is null");
            return;
        }

        if (application instanceof Activity){
            LogUtils.d("DevWork init by Activity");
            application = context.getApplicationContext();
        }else if (context instanceof Application){
            LogUtils.d("DevWork init by Application");
            application = context;
        }else{
            LogUtils.e("DevWork can not init. Cause context is wrong type");
        }
    }

    /**获取当前程序的实例
     *
     * @return
     */
    public static Context getApplication() {
        return application;
    }
}
