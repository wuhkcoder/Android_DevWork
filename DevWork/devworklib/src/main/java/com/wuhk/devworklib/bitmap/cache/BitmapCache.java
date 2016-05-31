package com.wuhk.devworklib.bitmap.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wuhk.devworklib.bitmap.BitmapDisplayConfig;
import com.wuhk.devworklib.bitmap.BitmapGlobalConfig;
import com.wuhk.devworklib.bitmap.core.BitmapCommonUtils;
import com.wuhk.devworklib.bitmap.core.BitmapDecoder;
import com.wuhk.devworklib.io.IOUtils;
import com.wuhk.devworklib.utils.BitmapUtils;
import com.wuhk.devworklib.utils.LogUtils;
import com.wuhk.devworklib.utils.VerifyUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 图片缓存对象，组合了内存缓存和磁盘缓存的操作
 * <p/>
 * Created by wuhk on 2016/5/31.
 */
public class BitmapCache {
    /**
     * 磁盘缓存索引
     */
    private static final int DISK_CACHE_INDEX = 0;

    /**
     * 磁盘缓存
     */
    private static LruDiskCache diskCache;

    /**
     * 内存缓存
     */
    private static LruMemoryCache<String, Bitmap> memoryCache;

    /**
     * 同一地址，不同显示配置
     */
    private static HashMap<String, ArrayList<String>> uri2keyListMap = new HashMap<String, ArrayList<String>>();//辅助内存缓存

    /**
     * 操作磁盘缓存锁
     */
    private final Object diskCacheLock = new Object();

    /**
     * 标记磁盘是否可读
     */
    private boolean isDiskCacheReadable = false;

    /**
     * 全局配置
     */
    private BitmapGlobalConfig globalConfig;

    public BitmapCache(BitmapGlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    /////////////////////////////////////////////初始化缓存//////////////////////////////////////////

    /**
     * 初始化内存缓存
     *
     * @return
     */
    public BitmapCache initMemoryCache() {
        if (!globalConfig.isMemoryCacheEnabled()) {
            LogUtils.d("MemoryCache not enabled");
            return null;
        }

        if (null != memoryCache) {
            try {
                clearMemoryCache();
            } catch (Exception e) {
                LogUtils.e("ClearMemoryCache Exception. Cause:" + e.getMessage());
            }
        }

        memoryCache = new LruMemoryCache<String, Bitmap>(globalConfig.getMemoryCacheSize()) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                if (null == value) {
                    LogUtils.d("bitmap is null");
                    return 0;
                }
                return BitmapCommonUtils.getBitmapSize(value);
            }
        };
        return this;
    }

    /**
     * 初始化磁盘缓存
     *
     * @return
     */
    public BitmapCache initDiskCache() {
        if (!globalConfig.isDiskCacheEnabled()) {
            LogUtils.d("DiskCache not enabled");
            return null;
        }

        synchronized (diskCacheLock) {
            if (null == diskCache || diskCache.isClosed()) {
                File diskCacheDir = new File(globalConfig.getDiskCachePath());
                if (!diskCacheDir.exists()) {
                    diskCacheDir.mkdirs();
                }

                long availableSpace = BitmapCommonUtils.getAvaiableSpace(diskCacheDir);
                long diskCacheSize = globalConfig.getDiskCacheSize();
                diskCacheSize = availableSpace > diskCacheSize ? diskCacheSize : availableSpace;

                try {
                    diskCache = LruDiskCache.open(diskCacheDir, 1, 1, diskCacheSize);
                } catch (IOException e) {
                    diskCache = null;
                    LogUtils.e(e.getMessage(), e);
                }
            }

            isDiskCacheReadable = true;
            diskCacheLock.notifyAll();
        }
        return this;
    }
    /////////////////////////////////////////////缓存设置调整////////////////////////////////////////

    /**设置内存缓存大小
     *
     * @param maxSize
     * @return
     */
    public BitmapCache setMemoryCacheSize(int maxSize){
        if (memoryCache != null){
            memoryCache.setMaxSize(maxSize);
        }
        return this;
    }

    /**设置磁盘缓存大小
     *
     * @param maxSize
     * @return
     */
    public BitmapCache setDiskCacheSize(int maxSize){
        if (diskCache != null){
            diskCache.setMaxSize(maxSize);
        }
        return this;
    }

    /////////////////////////////////////////////下载图片，会保存在磁盘///////////////////////////////
    public Bitmap cacheBitmapFromUri(String uri , BitmapDisplayConfig config){
        BitmapMeta bitmapMeta  = new BitmapMeta();

        OutputStream outputStream = null;
        LruDiskCache.Snapshot snapshot = null;

        try {
            //如果有开启磁盘缓存，下载到磁盘
            if (globalConfig.isDiskCacheEnabled()){
                synchronized (diskCacheLock){
                    //等到直到磁盘缓存被初始化完毕
                    while (!isDiskCacheReadable){
                        try {
                            diskCacheLock.wait();
                        } catch (InterruptedException e) {
                            //被中断可继续往下操作
                        }
                    }

                    if(null != diskCache){
                        String cacheKey = globalConfig.getMakeCachekayListener().makeCacheKey(uri);
                        snapshot = diskCache.get(cacheKey);
                        if (null == snapshot){
                            LruDiskCache.Editor editor  = diskCache.edit(cacheKey);
                            if (null != editor){
                                outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
                                bitmapMeta.expiryTimestamp = globalConfig.getDownloaderListener()
                                        .downloadToStream(uri , outputStream , config.getDownloaderCallback());
                                if (bitmapMeta.expiryTimestamp < 0){
                                    editor.abort();
                                    return null;//下载失败
                                }else{
                                    editor.setEntryExpiryTimestamp(bitmapMeta.expiryTimestamp);
                                    editor.commit();
                                }
                                snapshot = diskCache.get(cacheKey);
                            }
                        }

                        if (null != snapshot){
                            bitmapMeta.inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);
                        }
                    }
                }
            }
            //如果磁盘缓存没有开启，图片就直接下载到内存中，直接下载到内存中是很危险的
            if (!globalConfig.isDiskCacheEnabled() || diskCache == null || bitmapMeta.inputStream == null){
                outputStream = new ByteArrayOutputStream();
                bitmapMeta.expiryTimestamp = globalConfig.getDownloaderListener()
                        .downloadToStream(uri , outputStream , config.getDownloaderCallback());

                if (bitmapMeta.expiryTimestamp < 0){
                    return null;
                }else{
                    bitmapMeta.data = ((ByteArrayOutputStream)outputStream).toByteArray();
                }
            }

            String cacheKey = globalConfig.getMakeCachekayListener().makeCacheKey(uri);

            return addBitmapToMemoryCache(cacheKey , config , bitmapMeta);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(snapshot);
        }

        return null;
    }

    /**把图片缓存添加到内存缓存中
     *
     * @param cachekey
     * @param config
     * @param bitmapMeta
     * @return
     * @throws IOException
     */
    private Bitmap addBitmapToMemoryCache(String cachekey , BitmapDisplayConfig config
            , BitmapMeta bitmapMeta) throws IOException{
        if (cachekey == null || bitmapMeta == null){
            return null;
        }

        Bitmap bitmap = null;

        try {
            if (bitmapMeta.inputStream != null){
                //表示开启了磁盘缓存，然后网络上的图片直接下载到了本地磁盘，保存的是输入流
                if (config.isShowOrginal()){
                    bitmap = BitmapFactory.decodeFileDescriptor(bitmapMeta.inputStream.getFD());
                }else{
                    bitmap = BitmapDecoder.decodeSampleBitmapFromDescriptor(bitmapMeta.inputStream.getFD()
                    , config.getBitmapMaxWidth() , config.getBitmapMaxHeight() ,config.getBitmapConfig());
                }
            }else if (bitmapMeta.data != null){
                //表示没有开启了磁盘缓存，开启了内存缓存，所有图片直接下载到了data内存中
                if (config.isShowOrginal()){
                    bitmap = BitmapFactory.decodeByteArray(bitmapMeta.data , 0  , bitmapMeta.data.length);
                }else{
                    bitmap = BitmapDecoder.decodeSampledBitmapFromByteArray(bitmapMeta.data,
                            config.getBitmapMaxWidth() , config.getBitmapMaxHeight(), config.getBitmapConfig());
                }
            }else{
                //表示磁盘缓存和内存缓存都没有开启，不建议这样
                bitmap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null == bitmap){
            return null;
        }

        //把图片添加到内存缓存中
        String key = cachekey = config.toString();
        if (globalConfig.isMemoryCacheEnabled() && null != memoryCache){
            ArrayList<String> keyList = uri2keyListMap.get(cachekey);
            if (null == keyList){
                keyList = new ArrayList<String>();
                uri2keyListMap.put(cachekey , keyList);
            }

            keyList.add(key);

            memoryCache.put(key , bitmap , bitmapMeta.expiryTimestamp);
        }

        return bitmap;
    }

    /**从内存缓存中获取图片
     *
     * @param uri
     * @param config
     * @return
     */
    public Bitmap getBitmapFromMemCache(String uri , BitmapDisplayConfig config){
        String key = uri + config.toString();
        if (memoryCache != null){
            Bitmap bitmap = memoryCache.get(key);
            return null == bitmap ? null :bitmap;
        }
        return null;
    }

    public Bitmap getBitmapFromDiskCache(String uri , BitmapDisplayConfig config){
        if (!globalConfig.isDiskCacheEnabled()){
            return null;
        }

        synchronized (diskCacheLock){
            while (!isDiskCacheReadable){
                try {
                    diskCacheLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (diskCache != null){
                LruDiskCache.Snapshot snapshot = null;

                try {
                    snapshot = diskCache.get(uri);
                    if(snapshot != null){
                        Bitmap bitmap = null;

                        try {
                            if (config.isShowOrginal()){
                                bitmap = BitmapFactory.decodeFileDescriptor(snapshot.getInputStream(DISK_CACHE_INDEX).getFD());
                            }else{
                                bitmap = BitmapDecoder.decodeSampleBitmapFromDescriptor(snapshot.getInputStream(DISK_CACHE_INDEX)
                                .getFD() , config.getBitmapMaxWidth() , config.getBitmapMaxHeight() , config.getBitmapConfig());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //图片在磁盘缓存读取不到，直接返回null
                        if (null == bitmap){
                            return null;
                        }

                       //TODO:圆角处理

                        String key = uri + config.toString();
                        if (globalConfig.isMemoryCacheEnabled() && null != memoryCache){
                            memoryCache.put(key , bitmap , diskCache.getExpiryTimestamp(uri));
                        }

                        return bitmap;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    IOUtils.closeQuietly(snapshot);
                }
            }
            return null;
        }
    }
    /////////////////////////////////////////////清理缓存部分////////////////////////////////////////

    /**
     * 清理所有缓存
     */
    public void clearCache() {
        clearMemoryCache();
        clearDiskCache();
    }

    /**
     * 清理所有内存缓存
     */
    public void clearMemoryCache() {
        if (memoryCache != null) {
            memoryCache.evictAll();
            uri2keyListMap.clear();
        }
    }

    /**
     * 清理所有磁盘缓存
     */
    public void clearDiskCache() {
        synchronized (diskCacheLock) {
            if (diskCache != null && !diskCache.isClosed()) {
                try {
                    diskCache.delete();
                } catch (IOException e) {
                    LogUtils.e("清理磁盘缓存异常，原因：" + e.getMessage(), e);
                }
                diskCache = null;
                isDiskCacheReadable = false;
            }
        }
        initDiskCache();
    }

    /**
     * 清理指定的内存缓存和磁盘缓存
     *
     * @param uri 缓存的图片地址
     */
    public void clearCache(String uri) {
        clearMemoryCache(uri);
        clearDiskCache(uri);
    }

    /**
     * 清理指定的内存缓存
     *
     * @param uri 缓存的图片地址
     */
    public void clearMemoryCache(String uri) {
        ArrayList<String> keyList = uri2keyListMap.get(uri);
        if (VerifyUtils.isEmpty(keyList)) {
            return;
        }

        if (null == memoryCache) {
            return;
        }

        // 遍历删除该地址下的所有缓存
        for (String key : keyList) {
            memoryCache.remove(key);
        }
        uri2keyListMap.remove(uri);
    }

    /**
     * 清理指定的磁盘缓存
     *
     * @param uri 缓存的图片地址
     */
    public void clearDiskCache(String uri) {
        synchronized (diskCacheLock) {
            if (diskCache != null && !diskCache.isClosed()) {
                try {
                    diskCache.remove(uri);
                } catch (IOException e) {
                    LogUtils.e("清理[" + uri + "]磁盘缓存IO异常，原因：" + e.getMessage(),
                            e);
                }
            }
        }
    }

    /**
     * flush磁盘缓存
     */
    public void flush() {
        synchronized (diskCacheLock) {
            if (diskCache != null) {
                try {
                    diskCache.flush();
                } catch (IOException e) {
                    LogUtils.e("flush磁盘缓存IO异常，原因：" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 关闭内存缓存和磁盘缓存，关闭后，缓存要重新初始化，否则不可用
     */
    public void close() {
        synchronized (diskCacheLock) {
            if (diskCache != null) {
                try {
                    if (!diskCache.isClosed()) {
                        diskCache.close();
                        diskCache = null;
                    }
                } catch (IOException e) {
                    LogUtils.e("关闭缓存IO异常，原因：" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 图片内容封装<br>
     * 如果启用了磁盘缓存，那么从网络上下载图片是直接下载到磁盘上的，所有保存的是inputStream<br>
     * 如果没有启用磁盘缓存，只启用了内存缓存，那么从网络上下载图片是直接下载到内存中，所有保存的是data数据
     *
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2013-9-17 下午3:10:29 $
     */
    private class BitmapMeta {
        public FileInputStream inputStream;
        public byte[] data;
        public long expiryTimestamp;
    }
}
