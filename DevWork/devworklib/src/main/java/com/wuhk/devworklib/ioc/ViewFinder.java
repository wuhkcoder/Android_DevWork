package com.wuhk.devworklib.ioc;

import android.app.Activity;
import android.view.View;

import com.wuhk.devworklib.R;

/**
 * 注入View的寻找器
 * Created by wuhk on 2016/5/27.
 */
class ViewFinder {
    private View view;
    private Activity activity;

    public ViewFinder(View view) {
        this.view = view;
    }

    public ViewFinder(Activity activity) {
        this.activity = activity;
    }

    /**从寻找其中找到指定的view
     *
     * @param id
     * @return
     */
    public View findViewById(int id){
        if (null != view){
            return view.findViewById(id);
        }
        if (null != activity){
            return activity.findViewById(id);
        }

        return null;
    }
}
