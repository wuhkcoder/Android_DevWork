package com.wuhk.devworklib.asynctask;

import java.io.Serializable;

/**
 * 耗时操作返回的结果对象，一般可以用作Http请求结果的封装
 *
 * Created by wuhk on 2016/5/30.
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 6335628920913198509L;

    /**结果成功失败标志
     *
     */
    private boolean success;

    /**结果的提示
     *
     */
    private String message;

    /**结果数据
     *
     */
    private T value;

    public Result(){

    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result(boolean success, String message, T value) {
        this.success = success;
        this.message = message;
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
