package com.wuhk.devworklib.asynctask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.wuhk.devworklib.asynctask.callback.AsyncTaskFailCallback;
import com.wuhk.devworklib.asynctask.callback.AsyncTaskResuleNullCallback;
import com.wuhk.devworklib.asynctask.callback.AsyncTaskSuccessCallback;
import com.wuhk.devworklib.utils.ToastUtils;

/**
 * 耗时任务类的基类
 * Created by wuhk on 2016/5/30.
 */
public abstract class AbstractTask<T> extends AsyncTask<Object , Integer , Result<T>>{
    protected Context context;
    /**是否显示加载中，默认显示
     *
     */
    private boolean isShowProgressDialog = true;

    /**正在加载中提示控件
     *
     */
    private Dialog progressDialog;

    /**默认提示消息
     *
     */
    private String progressTitle = "请稍后...";

    /**加载中是否可以取消，默认可以取消
     *
     */
    private boolean isCancel = true;

    /**成功回调
     *
     */
    private AsyncTaskFailCallback<T> asyncTaskFailCallback;

    /**失败回调
     *
     */
    private AsyncTaskSuccessCallback<T> asyncTaskSuccessCallback;

    /**result返回空是回调
     *
     */
    private AsyncTaskResuleNullCallback asyncTaskResuleNullCallback;

    /**构造方法
     *
     * @param context
     */
    public AbstractTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        //显示加载中对话框
        if (isShowProgressDialog){
            if (null == getProgressDialog()){
                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle(getProgressTitle());
                progressDialog.setCancelable(isCancel);
            }
            showDialog(getProgressDialog());
        }
    }

    @Override
    protected Result<T> doInBackground(Object... params) {
        Result<T> result = null;
        try {
            result= doHttpRequest(params);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result<T>(false , "DoHttpRequest exception. Cause:" + e.getMessage());
        }
        return result;
    }

    @Override
    protected void onPostExecute(Result<T> result) {
        if (isShowProgressDialog){
            //先隐藏提示框
            dismissDialog(getProgressDialog());
        }

        if (null == result){
            if (null != asyncTaskResuleNullCallback){
                asyncTaskResuleNullCallback.resultNullCallback();
            }
            return;
        }

        if (result.isSuccess()){
            if (null != asyncTaskSuccessCallback){
                asyncTaskSuccessCallback.successCallback(result);
            }else{
                if (!TextUtils.isEmpty(result.getMessage())){
                    ToastUtils.toastShort(result.getMessage());
                }
            }
        }else{
            if (null != asyncTaskFailCallback){
                asyncTaskFailCallback.failCallback(result);
            }else{
                if (TextUtils.isEmpty(result.getMessage())){
                    ToastUtils.toastShort(result.getMessage());
                }
            }
        }
    }


    /**设置失败监听
     *
     * @param asyncTaskFailCallback
     */
    public void setAsyncTaskFailCallback(AsyncTaskFailCallback<T> asyncTaskFailCallback) {
        this.asyncTaskFailCallback = asyncTaskFailCallback;
    }

    /**设置成功监听
     *
     * @param asyncTaskSuccessCallback
     */
    public void setAsyncTaskSuccessCallback(AsyncTaskSuccessCallback<T> asyncTaskSuccessCallback) {
        this.asyncTaskSuccessCallback = asyncTaskSuccessCallback;
    }

    /**设置result返回空的监听
     *
     * @param asyncTaskResuleNullCallback
     */
    public void setAsyncTaskResuleNullCallback(AsyncTaskResuleNullCallback asyncTaskResuleNullCallback) {
        this.asyncTaskResuleNullCallback = asyncTaskResuleNullCallback;
    }

    public boolean isShowProgressDialog() {
        return isShowProgressDialog;
    }

    public void setShowProgressDialog(boolean showProgressDialog) {
        isShowProgressDialog = showProgressDialog;
    }

    public Dialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(Dialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public String getProgressTitle() {
        return progressTitle;
    }

    public void setProgressTitle(String progressTitle) {
        this.progressTitle = progressTitle;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    /**Http请求，子类自己实现
     *
     * @param objects
     * @return
     */
    protected  abstract Result<T> doHttpRequest(Object...objects);

    private void showDialog(Dialog dialog){
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissDialog(Dialog dialog){
        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
