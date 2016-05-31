package com.wuhk.devworklib.bitmap.listeners;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.wuhk.devworklib.bitmap.BitmapDisplayConfig;

/**
 * 加载完图片后的显示回调接口
 *
 * Created by wuhk on 2016/5/31.
 */
public interface DisplayImageListener {
    /**图片加载成功回调方法
     *
     * @param imageView
     * @param bitmap
     * @param config
     */
    void loadCompleted(ImageView imageView , Bitmap bitmap , BitmapDisplayConfig config);


    /**图片加载失败回调方法
     *
     * @param imageView
     * @param config
     */
    void loadFailed(ImageView imageView , BitmapDisplayConfig config);
}
