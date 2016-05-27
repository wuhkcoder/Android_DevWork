package com.wuhk.devwork;

import android.app.Application;

import com.wuhk.devworklib.DevWork;

/**
 * Created by wuhk on 2016/5/27.
 */
public class App extends Application {
    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DevWork.init(this);
    }
}
