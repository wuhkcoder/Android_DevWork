package com.wuhk.devworklib.http.callback;

/**
 * 下载监听
 *
 * Created by wuhk on 2016/5/28.
 */
public interface DWHttpDownloadListener {

    /**处理结果时回调
     *
     * @param count
     * @param current
     * @param isFinish
     */
    void callback(long count , long current , boolean isFinish);
}
