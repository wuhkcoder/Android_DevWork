package com.wuhk.devworklib.bitmap.listeners;

import java.io.OutputStream;

/**
 * 加载图片接口
 *
 * Created by wuhk on 2016/5/31.
 */
public interface DownloaderListener {

    /**把网络或本地图片下载到文件的 outputStream
     *
     * @param urlString
     *          加载图片地址
     * @param outputStream
     *          加载输出流
     * @param callback
     *          下载过程的回调
     * @return   返回过期时长
     */
    long downloadToStream(String urlString , OutputStream outputStream ,
                          DownloaderProcessListener callback);


    /**设置图片过期时长
     *
     * @param expiry
     *          表示过期的时间点，而非过期时间间隔
     */
    void setDefaultExpiry(long expiry);

    /**获取图片过期时长
     *
     * @return
     */
    long getDefaultExpiry();
}
