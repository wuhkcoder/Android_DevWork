package com.wuhk.devworklib.bitmap.listeners;

/**
 * 图片如果不存在，就会触发下载，这个回调就是监听下载的过程
 * 如果图片从缓存中加载，不会触发该监听
 *
 * Created by wuhk on 2016/5/31.
 */
public interface DownloaderProcessListener {

    /**开始下载任务
     *
     * @param url
     */
    void onStartLoading(String url);

    /**下载中回调，随时回调下载情况
     *
     * @param total
     * @param current
     */
    void onLoading(int total , int current);

    /**下载完成
     *
     */
    void onEndLoading();
}
