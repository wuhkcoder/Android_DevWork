package com.wuhk.devworklib.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.wuhk.devworklib.DevWork;

/**获取设备信息工具类
 * Created by wuhk on 2016/5/27.
 */
public abstract class DeviceUtils {

    /**获取唯一设备号
     *
     * @return
     */
    public static String getDeviceId(){
        TelephonyManager telephonyManager = (TelephonyManager) DevWork.getApplication()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}
