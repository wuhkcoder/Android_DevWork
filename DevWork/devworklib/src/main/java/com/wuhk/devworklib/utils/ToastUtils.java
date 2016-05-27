package com.wuhk.devworklib.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.wuhk.devworklib.DevWork;

/**吐司工具类
 * Created by wuhk on 2016/5/27.
 */
public abstract class ToastUtils {

    /**长时间显示
     *
     * @param text
     */
    public static void toastLong(final String text){
        if (null == text){
            return;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DevWork.getApplication() , text ,Toast.LENGTH_LONG).show();
            }
        });
    }

    /**短时间显示
     *
     * @param text
     */
    public static void toastShort(final String text){
        if (null == text){
            return;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DevWork.getApplication() , text , Toast.LENGTH_SHORT).show();
            }
        });

    }
}
