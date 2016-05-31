package com.wuhk.devworklib.bitmap.listeners;

/**
 *  产生缓存key监听
 *
 * Created by wuhk on 2016/5/31.
 */
public interface MakeCachekayListener {

    /**产生cacheKay
     *
     * @param url
     * @return
     */
    String makeCacheKey(String url);
}
