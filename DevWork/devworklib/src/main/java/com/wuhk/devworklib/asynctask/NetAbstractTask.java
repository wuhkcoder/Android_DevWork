package com.wuhk.devworklib.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.wuhk.devworklib.asynctask.callback.AsyncTaskResuleNullCallback;
import com.wuhk.devworklib.utils.ContextUtils;
import com.wuhk.devworklib.utils.ToastUtils;

/**
 * 网络请求异步任务类，会先判断网络是否存在。
 *
 * Created by wuhk on 2016/5/30.
 */
public abstract class NetAbstractTask<T> extends AbstractTask<T> {
    public NetAbstractTask(Context context) {
        super(context);
        setAsyncTaskResuleNullCallback(new AsyncTaskResuleNullCallback() {
            @Override
            public void resultNullCallback() {
                ToastUtils.toastShort("无网络连接");
            }
        });
    }

    @Override
    protected Result<T> doHttpRequest(Object... objects) {
        if (!ContextUtils.hasNetWork()){
            return null;
        }

        return onHttpRequest(objects);
    }

    /**子类实现，网络请求操作
     *
     * @param params
     * @return
     */
    protected abstract Result<T> onHttpRequest(Object ... params);
}
