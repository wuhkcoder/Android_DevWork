package com.wuhk.devworklib.asynctask.callback;

import com.wuhk.devworklib.asynctask.Result;

/**
 * 耗时任务成功回调接口
 * Created by wuhk on 2016/5/30.
 */
public interface AsyncTaskSuccessCallback<T> {

    /**执行方法
     *
     * @param result
     */
    void successCallback(Result<T> result);
}
