package com.blanink.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.BaseResponse;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 获取验证码
 */
public class CodeActivity extends BaseActivity {

    private static final String TAG ="CodeActivity" ;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.cord)
    EditText cord;
    @BindView(R.id.getcord)
    TextView getcord;
    @BindView(R.id.bt_next)
    Button btNext;
    private Integer msgCode;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private VerCodeTimer verCodeTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        editor = sp.edit();
        initData();
    }
    public static Intent  getIntent(Context context, String userId, String phone){
       Intent intent= new Intent(context,CodeActivity.class);
        intent.putExtra("USER_ID",userId);
        intent.putExtra("phone",phone);
        return intent;
    }
    private void initData() {
        phone.setText(getIntent().getStringExtra("phone"));
        long oldTime = sp.getLong("currentTime", 0);
        if (oldTime == 0) {
            verCodeTimer = new VerCodeTimer(120000, 1000);
            cord.setEnabled(true);
        } else {
            long sub = System.currentTimeMillis() - oldTime;
            if (60000 - sub > 0) {
                cord.setEnabled(true);
                verCodeTimer = new CodeActivity.VerCodeTimer(120000 - sub, 1000);
                getcord.setText(120000 - sub / 1000 + "秒后重新获取");
                getcord.setGravity(Gravity.CENTER);
                getcord.setTextSize(14);
                getcord.setClickable(true);
                getcord.setBackgroundResource(R.drawable.login_edit_passed_bg);
                verCodeTimer.start();

            } else {
                verCodeTimer = new CodeActivity.VerCodeTimer(120000, 1000);
                cord.setEnabled(true);
            }
        }

        //短信验证
        getcord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    final String mobile = phone.getText().toString().trim();
                    if (TextUtils.isEmpty(mobile)) {
                        Toast.makeText(CodeActivity.this, "请输入你绑定的手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!TextUtils.isEmpty(mobile) && mobile.length() != 11) {
                        Toast.makeText(CodeActivity.this, "请输入有效的11位手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    editor.putLong("currentTime", System.currentTimeMillis());
                    editor.commit();
                    Map<String, Object> mapParams = new HashMap<String, Object>();
                    mapParams.put("phone", mobile);
                    Subscription sub= RetrofitFactory.getApiService(CodeActivity.this)
                            .phoneCheck(mapParams)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<BaseResponse<Integer>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, e.getMessage());
                                }

                                @Override
                                public void onNext(BaseResponse<Integer> integerBaseResponse) {
                                    if ("00000".equals(integerBaseResponse.getErrorCode())) {

                                        //msgCode=integerBaseResponse.getResult();
                                        editor.putInt("msgCode", integerBaseResponse.getResult());
                                        editor.commit();

                                    }
                                }
                            });
                    msubscription.add(sub);
                    getcord.setClickable(false);
                    getcord.setBackgroundResource(R.drawable.login_edit_passed_bg);
                    verCodeTimer.start();
                    cord.setEnabled(true);


                }
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkCode = cord.getText().toString().trim();
                if (TextUtils.isEmpty(checkCode)) {
                    Toast.makeText(CodeActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                long sub = System.currentTimeMillis() - sp.getLong("currentTime", 0);

                if (sub > 15 * 60 * 1000 && Integer.parseInt(checkCode) == sp.getInt("msgCode", 0)) {
                    Toast.makeText(CodeActivity.this, "验证码已失效，请重新获取", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.parseInt(checkCode) != sp.getInt("msgCode", 0)) {
                    Toast.makeText(CodeActivity.this, "你输入的验证码有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(ModifyPsdByPhoneActivity.getIntent(CodeActivity.this,getIntent().getStringExtra("USER_ID")));
            }
        });

    }

    private class VerCodeTimer extends CountDownTimer {
        public VerCodeTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //每过你规定的时间间隔做的操作
        @Override
        public void onTick(long millisUntilFinished) {
            getcord.setText(millisUntilFinished / 1000 + "秒后重新获取");
            getcord.setGravity(Gravity.CENTER);
            getcord.setTextSize(14);
        }

        //倒计时结束时做的操作↓↓
        @Override
        public void onFinish() {
            getcord.setText("重新获取验证码");
            getcord.setTextSize(14);
            getcord.setClickable(true);
            getcord.setGravity(Gravity.CENTER);
            getcord.setBackgroundResource(R.drawable.login_edit_normal_bg);
        }
    }

    @Override
    public void onBackPressed() {
        if (verCodeTimer != null)
            verCodeTimer.cancel();
        super.onBackPressed();
    }
}
