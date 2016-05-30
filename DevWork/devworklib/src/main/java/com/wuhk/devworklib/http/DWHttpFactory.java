package com.wuhk.devworklib.http;

import com.wuhk.devworklib.http.urlconnect.DWHttpUrlConnectionClient;

/**
 * 实例工厂类
 * Created by wuhk on 2016/5/30.
 */
public abstract class DWHttpFactory {

    /**用URLConnection方式实现
     *
     * @return
     */
    public static DWHttpClient getURLConnectionHttpClien(){
        return new DWHttpUrlConnectionClient();
    }
}
