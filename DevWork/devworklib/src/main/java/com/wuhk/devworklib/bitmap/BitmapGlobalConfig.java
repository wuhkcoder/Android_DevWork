package com.wuhk.devworklib.bitmap;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.wuhk.devworklib.bitmap.cache.BitmapCache;
import com.wuhk.devworklib.bitmap.cache.BitmapCacheManager;
import com.wuhk.devworklib.bitmap.core.BitmapCommonUtils;
import com.wuhk.devworklib.bitmap.listeners.DownloaderListener;
import com.wuhk.devworklib.bitmap.listeners.MakeCachekayListener;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 图片加载的全局配置，包括了缓存管理等一些参数
 *
 * Created by wuhk on 2016/5/31.
 */
public class BitmapGlobalConfig {
    /**磁盘缓存地址*/
    private String diskCachePath;

    /**最小内存缓存阀值 : 2M */
    public final static int MIN_MEMORY_CACHE_SIZE = 1024 * 1024 * 2;

    /**内存缓存大小，默认：8M */
    private int memoryCacheSize = 1024 * 1024 * 8;

    /**最小磁盘缓存阀值：10M */
    public final static int MIN_DISK_CACHE_SIZE = 1024 * 1024 * 10;

    /**磁盘缓存大小， 默认：50M */
    private int diskCacheSize = 1024 * 1024 * 50;

    /**是否开启内存缓存*/
    private boolean memoryCacheEnabled = true;

    /**是否开启磁盘缓存*/
    private boolean diskCacheEnabled = true;

    /**图片缓存*/
    private BitmapCache bitmapCache;

    /**处理线程数*/
    private int threadPoolSize = 5;

    /**标记是否需要重新初始化线程池*/
    private  boolean _dirty_params_bitmapLoadExecutor = true;

    /**线程池*/
    private ExecutorService bitmapLoadExecutor;

    /**上下文*/
    private Context application;

    /**cahceKey生成监听*/
    private MakeCachekayListener makeCachekayListener;

    /**图片下载器*/
    private DownloaderListener downloaderListener;

    public BitmapGlobalConfig(Context application) {
        this.application = application;
        //初始化缓存
        initBitmapCache();
    }

    /**初始化缓存
     *
     */
    private void initBitmapCache(){

    }

    ////////////////////////////////////////////缓存磁盘路径/////////////////////////////////////////

    /**获取磁盘缓存路径
     *
     * @return
     */
    public String getDiskCachePath(){
        if (TextUtils.isEmpty(diskCachePath)){
            diskCachePath = BitmapCommonUtils.getDiskCacheDir(application , "DWBitmapCache");
        }
        return diskCachePath;
    }

    /**设置磁盘缓存路径
     *
     * @param diskCachePath
     * @return
     */
    public BitmapGlobalConfig setDiskCachePath(String diskCachePath){
        this.diskCachePath = diskCachePath;
        return this;
    }

    //////////////////////////////////////////获取缓存模块///////////////////////////////////////////////
    public BitmapCache getBitmapCache(){
        if (null == bitmapCache){
            bitmapCache = new BitmapCache(this);
        }
        return bitmapCache;
    }

    /////////////////////////////////////////设置内存缓存大小/////////////////////////////////////////

    /**获取内存缓存大小
     *
     * @return
     */
    public int getMemoryCacheSize() {
        return memoryCacheSize;
    }


    public BitmapGlobalConfig setMemoryCacheSize(int memoryCacheSize) {
        if (memoryCacheSize >= MIN_MEMORY_CACHE_SIZE){
            this.memoryCacheSize = memoryCacheSize;
            if (null != bitmapCache){
                bitmapCache.setMemoryCacheSize(this.memoryCacheSize);
            }
        }else{
            //TODO:设置默认的内存缓存大小
        }
        return this;
    }


    ///////////////////////////////////////////磁盘缓存大小//////////////////////////////////////////

    /**获取磁盘缓存大小
     *
     * @return
     */
    public int getDiskCacheSize() {
        return diskCacheSize;
    }

    /**设置磁盘缓存大小
     *
     * @param diskCacheSize
     * @return
     */
    public BitmapGlobalConfig setDiskCacheSize(int diskCacheSize) {
        if (diskCacheSize >= MIN_DISK_CACHE_SIZE){
            this.diskCacheSize = diskCacheSize;
            if (bitmapCache != null){
                bitmapCache.setDiskCacheSize(this.diskCacheSize);
            }
        }
        return this;
    }


    ///////////////////////////////////////////设置线程池数量////////////////////////////////////////

    /**获取加载线程池数
     *
     * @return
     */
    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    /**设置加载线程池数
     *
     * @param threadPoolSize
     */
    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    /////////////////////////////////////////加载图片的线程池/////////////////////////////////////////

    /**获取线程池
     *
     * @return
     */
    public ExecutorService getBitmapLoadExecutor() {
        if (_dirty_params_bitmapLoadExecutor || bitmapLoadExecutor == null){
            bitmapLoadExecutor = Executors.newFixedThreadPool(getThreadPoolSize(),
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setPriority(Thread.NORM_PRIORITY - 1);
                            return t;
                        }
                    });
            _dirty_params_bitmapLoadExecutor = false;
        }
        return bitmapLoadExecutor;
    }

    //////////////////////////////////////是否开启内存缓存///////////////////////////////////////////

    /**判断是否开启内存缓存
     *
     * @return
     */
    public boolean isMemoryCacheEnabled() {
        return memoryCacheEnabled;
    }

    /**设置是否开启内存缓存
     *
     * @param memoryCacheEnabled
     */
    public void setMemoryCacheEnabled(boolean memoryCacheEnabled) {
        this.memoryCacheEnabled = memoryCacheEnabled;
    }

    /////////////////////////////////////是否开启磁盘缓存////////////////////////////////////////////

    /**判断是否开启磁盘缓存
     *
     * @return
     */
    public boolean isDiskCacheEnabled() {
        return diskCacheEnabled;
    }

    /**设置是否开启磁盘缓存
     *
     * @param diskCacheEnabled
     */
    public void setDiskCacheEnabled(boolean diskCacheEnabled) {
        this.diskCacheEnabled = diskCacheEnabled;
    }

    ///////////////////////////////////////////内存信息查看//////////////////////////////////////////
    /** 获取可用内存信息 */
    private int getMemoryClass() {
        return ((ActivityManager) application
                .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
    }

    //TODO：接口默认实现
    public MakeCachekayListener getMakeCachekayListener() {
        return makeCachekayListener;
    }

    public void setMakeCachekayListener(MakeCachekayListener makeCachekayListener) {
        this.makeCachekayListener = makeCachekayListener;
    }

    public DownloaderListener getDownloaderListener() {
        return downloaderListener;
    }

    public void setDownloaderListener(DownloaderListener downloaderListener) {
        this.downloaderListener = downloaderListener;
    }
}
