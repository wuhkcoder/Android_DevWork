package com.wuhk.devwork.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wuhk.devwork.R;
import com.wuhk.devworklib.ioc.InjectView;
import com.wuhk.devworklib.ioc.app.DwActivity;
import com.wuhk.devworklib.utils.ToastUtils;

public class MainActivity extends DwActivity {

    @InjectView(R.id.helloTv)
    private TextView helloTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helloTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toastShort(helloTv.getText().toString());
            }
        });
    }
}
