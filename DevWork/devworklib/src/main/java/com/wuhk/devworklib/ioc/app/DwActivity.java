package com.wuhk.devworklib.ioc.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.wuhk.devworklib.ioc.InjectView;
import com.wuhk.devworklib.ioc.ViewUtils;
import com.wuhk.devworklib.utils.LogUtils;

import java.lang.reflect.Field;

/**
 * Created by wuhk on 2016/5/27.
 */
public class DwActivity extends Activity {
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ViewUtils.inject(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ViewUtils.inject(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ViewUtils.inject(this);
    }

//
//    /**
//     * 对各种注解进行注入
//     */
//    private void initDw() {
//        Field[] fileds = getClass().getDeclaredFields();
//        for (int i = 0; i < fileds.length; i++) {
//            initInjectView(fileds[i]);
//        }
//    }
//
//    /**
//     * 注解了InjectView的字段注入
//     */
//    private void initInjectView(Field field) {
//        InjectView injectView = field.getAnnotation(InjectView.class);
//
//        if (null != injectView) {
//            try {
//                View view = this.findViewById(injectView.value());
//                if (null != view) {
//                    field.setAccessible(true);
//                    field.set(this, view);
//                }
//            } catch (Exception e) {
//                LogUtils.e("BPInjectView exception. Cause:" + e.getMessage(), e);
//            }
//        }
//    }

}
