package com.blanink.ui.activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blanink.R;
import com.blanink.utils.MyActivityManager;
import com.blanink.utils.StatusBarCompat;

import rx.subscriptions.CompositeSubscription;

/**
 * 作者：Created by liangshen on 2017/12/15 0015 14:06
 * 邮箱：liangshen6868@163.com
 */
public class BaseActivity extends AppCompatActivity {

    public MyActivityManager activityManager;
    public CompositeSubscription msubscription;//管理所有的订阅

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorTheme));
        activityManager = MyActivityManager.getInstance();
        activityManager.pushOneActivity(this);
        msubscription=new CompositeSubscription();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityManager.popOneActivity(this);
        if(msubscription != null){
            this.msubscription.unsubscribe();
        }
    }
}
