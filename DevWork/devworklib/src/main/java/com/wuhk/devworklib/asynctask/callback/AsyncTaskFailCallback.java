package com.wuhk.devworklib.asynctask.callback;

import com.wuhk.devworklib.asynctask.Result;

/**
 * 耗时任务执行失败回调接口
 *
 * Created by wuhk on 2016/5/30.
 */
public interface AsyncTaskFailCallback<T> {

    /**执行方法
     *
     * @param result
     */
    void failCallback(Result<T> result);
}
