package com.wuhk.devworklib.bitmap.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;

import com.wuhk.devworklib.utils.LogUtils;

import java.io.File;

/**
 * 通用图片工具类
 * Created by wuhk on 2016/5/31.
 */
public class BitmapCommonUtils {

    /**获取可以使用的缓存目录，如果SD卡存在就获取缓存到SD卡，如果不存在，就使用内置内存
     *
     * @param context
     * @param dirName
     * @return
     */
    public static String getDiskCacheDir(Context context , String dirName){
        final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                ? context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();
        return cachePath + File.separator + dirName;

    }


    /**获取bitmap的字节大小
     *
     * @param bitmap
     * @return
     */
    public static int getBitmapSize(Bitmap bitmap){
        if (null == bitmap){
            LogUtils.d("bitmap is null");
            return 0;
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }


    /**获取目录指定的可用空间
     *
     * @param path
     * @return
     */
    public static long getAvaiableSpace(File path){
        if (null == path || !path.isDirectory()){
            return -1;
        }

        if (!path.exists()){
            path.mkdirs();
        }

        try {
            StatFs statFs = new StatFs(path.getPath());
            return (long)statFs.getBlockSize() * (long)statFs.getAvailableBlocks();
        } catch (Exception e) {
            LogUtils.e("获取控件异常，原因：" + e.getMessage());
        }

        return -1;
    }
}
