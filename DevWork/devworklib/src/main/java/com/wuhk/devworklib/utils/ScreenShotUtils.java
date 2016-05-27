package com.wuhk.devworklib.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 截屏工具
 * Created by wuhk on 2016/5/27.
 */
public abstract class ScreenShotUtils {

    /**View保存成图片
     *
     * @param view
     * @param saveFileName
     * @return
     */
    public static Bitmap shotView(View view , String saveFileName){
        view.setDrawingCacheEnabled(true);

        Bitmap bitmap = view.getDrawingCache();
        //保存到指定文件
        if(!VerifyUtils.isEmpty(saveFileName)){
            File file = new File(saveFileName);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()){
                parentFile.mkdirs();
            }

            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG , 70 ,fos);
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException ioe) {
                    // ignore
                }
            }

        }
        return bitmap;
    }

    /**
     * 截屏
     *
     * @param activity
     * @param saveFileName
     *            截屏后文件保存路劲
     * @return
     */
    public static Bitmap shotScreen(Activity activity, String saveFileName) {
        // 获取屏幕
        View decorview = activity.getWindow().getDecorView();
        return shotView(decorview, saveFileName);
    }
}
