package com.wuhk.devworklib.bitmap;

import android.graphics.Bitmap;
import android.view.animation.Animation;

import com.wuhk.devworklib.bitmap.listeners.DisplayImageListener;
import com.wuhk.devworklib.bitmap.listeners.DownloaderProcessListener;

/**
 * 图片展现配置
 *
 * Created by wuhk on 2016/5/31.
 */
public class BitmapDisplayConfig {
    /**图片质量：2.3之后默认参数*/
    private Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;

    /**是否显示原图，注意：图片较大较多时，千万设置false，并控制好下面的最大宽高，不然还是不能避免OOM*/
    private boolean showOrginal = false;

    /** showOrignal = false 时才有效，限制最大宽 ， 默认900*/
    private int bitmapMaxWidth = 900;

    /** showOrignal = false 时才有效，限制最大高 ， 默认900*/
    private int bitmapMaxHeight = 900;

    /**加载完成后显示动画*/
    private Animation animation;

    /**加载中图片*/
    private Bitmap loadingBitmap = Bitmap.createBitmap(50 , 50 , Bitmap.Config.ARGB_8888);

    /**加载失败图片*/
    private Bitmap loadFailedBitmap;

    /**加载完成后回调*/
    private DisplayImageListener displayImageListener;

    /**从网络下载图片是回调，只有在网络上下载时才会触发*/
    private DownloaderProcessListener downloaderCallback;

    /**图片需要进行圆角处理。默认不处理*/
    private float roundPx = 0;

    /**配置的tag值*/
    private Object tag;

    public Bitmap.Config getBitmapConfig() {
        return bitmapConfig;
    }

    public void setBitmapConfig(Bitmap.Config bitmapConfig) {
        this.bitmapConfig = bitmapConfig;
    }

    public boolean isShowOrginal() {
        return showOrginal;
    }

    public void setShowOrginal(boolean showOrginal) {
        this.showOrginal = showOrginal;
    }

    public int getBitmapMaxWidth() {
        return bitmapMaxWidth;
    }

    public void setBitmapMaxWidth(int bitmapMaxWidth) {
        this.bitmapMaxWidth = bitmapMaxWidth;
    }

    public int getBitmapMaxHeight() {
        return bitmapMaxHeight;
    }

    public void setBitmapMaxHeight(int bitmapMaxHeight) {
        this.bitmapMaxHeight = bitmapMaxHeight;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Bitmap getLoadingBitmap() {
        return loadingBitmap;
    }

    public void setLoadingBitmap(Bitmap loadingBitmap) {
        this.loadingBitmap = loadingBitmap;
    }

    public Bitmap getLoadFailedBitmap() {
        return loadFailedBitmap;
    }

    public void setLoadFailedBitmap(Bitmap loadFailedBitmap) {
        this.loadFailedBitmap = loadFailedBitmap;
    }

    public DisplayImageListener getDisplayImageListener() {
        return displayImageListener;
    }

    public void setDisplayImageListener(DisplayImageListener displayImageListener) {
        this.displayImageListener = displayImageListener;
    }

    public DownloaderProcessListener getDownloaderCallback() {
        return downloaderCallback;
    }

    public void setDownloaderCallback(DownloaderProcessListener downloaderCallback) {
        this.downloaderCallback = downloaderCallback;
    }

    public float getRoundPx() {
        return roundPx;
    }

    public void setRoundPx(float roundPx) {
        this.roundPx = roundPx;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
