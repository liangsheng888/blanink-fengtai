package com.blanink.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.bean.LoginResult;
import com.blanink.db.DemoDBManager;
import com.blanink.http.RetrofitFactory;
import com.blanink.ui.activity.EaseChat.modle.DemoHelper;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.MyActivityManager;
import com.blanink.utils.RePsdUtils;
import com.hyphenate.EMCallBack;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/6.
 */

public class FlashActivity extends Activity {
    MyActivityManager myActivityManager;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.pb_downLoad)
    ProgressBar pbDownLoad;
    @BindView(R.id.step)
    TextView step;
    private SharedPreferences sp;
    VerCodeTimer verCodeTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        ButterKnife.bind(this);
        myActivityManager = (MyActivityManager) MyActivityManager.getInstance();
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        myActivityManager.pushOneActivity(this);
        initData();
    }

    private void initData() {

            verCodeTimer = new VerCodeTimer(5000, 1000);
            verCodeTimer.start();
            tvVersionName.setText(CommonUtil.getVersion(FlashActivity.this));

        step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getString("LOGIN_NAME", null) != null) {
                    Intent intent = new Intent(FlashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(FlashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myActivityManager.popOneActivity(this);
        verCodeTimer = null;
    }

    private class VerCodeTimer extends CountDownTimer {
        public VerCodeTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //每过你规定的时间间隔做的操作
        @Override
        public void onTick(long millisUntilFinished) {
            step.setText(millisUntilFinished / 1000 + " 跳过");
            step.setGravity(Gravity.CENTER);
            step.setTextSize(14);

        }

        //倒计时结束时做的操作↓↓
        @Override
        public void onFinish() {
            if (CommonUtil.isConnected(FlashActivity.this)) {
                if (sp.getString("LOGIN_NAME", null) != null) {
                    Intent intent = new Intent(FlashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(FlashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(FlashActivity.this, "请检查你的网络", Toast.LENGTH_SHORT).show();
                if (sp.getString("LOGIN_NAME", null) != null) {
                    Intent intent = new Intent(FlashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(FlashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

}



