package com.blanink.ui.activity;

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
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DialogLoadUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 手机验证
 */
public class PhoneCheckActivity extends BaseActivity {

    private static final String TAG = "PhoneCheckActivity";


    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.cord)
    EditText cord;
    @BindView(R.id.getcord)
    TextView getcord;
    @BindView(R.id.login_psd)
    TextView loginPsd;
    @BindView(R.id.et_login_psd)
    EditText etLoginPsd;
    @BindView(R.id.second_login_psd)
    TextView secondLoginPsd;
    @BindView(R.id.et_second_login_psd)
    EditText etSecondLoginPsd;
    @BindView(R.id.savecord)
    Button savecord;
    @BindView(R.id.now)
    TextView now;
    VerCodeTimer verCodeTimer = null;
    private Integer msgCode;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_check);
        ButterKnife.bind(this);
        sp=getSharedPreferences("DATA",MODE_PRIVATE);
        editor= sp.edit();

        initData();
    }

    private void initData() {
        phone.setText(sp.getString("phoneCheck",""));
        //返回
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        long oldTime=sp.getLong("currentTime",0);
        if(oldTime==0){
            verCodeTimer = new VerCodeTimer(120000, 1000);
            cord.setEnabled(true);
        }else {
            long sub=System.currentTimeMillis()-oldTime;
            if(60000-sub>0){
                cord.setEnabled(true);
                verCodeTimer = new VerCodeTimer(120000-sub, 1000);
                getcord.setText(120000-sub / 1000 + "秒后重新获取");
                getcord.setGravity(Gravity.CENTER);
                getcord.setTextSize(14);
                getcord.setClickable(true);
                getcord.setBackgroundResource(R.drawable.login_edit_passed_bg);
                verCodeTimer.start();

            }else {
                verCodeTimer = new VerCodeTimer(120000, 1000);
                cord.setEnabled(true);
            }
        }
        //手机验证
        getcord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mobile = phone.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(PhoneCheckActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(mobile)&&mobile.length()!=11) {
                    Toast.makeText(PhoneCheckActivity.this, "请输入有效的11位手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                editor.putLong("currentTime",System.currentTimeMillis());
                editor.commit();
                Map<String, Object> mapParams = new HashMap<String, Object>();
                mapParams.put("phone",mobile);
                RetrofitFactory.getApiService(PhoneCheckActivity.this)
                        .phoneQuery(mapParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<BaseResponse<Integer>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG,e.getMessage());
                            }

                            @Override
                            public void onNext(BaseResponse<Integer> integerBaseResponse) {
                                if("00000".equals(integerBaseResponse.getErrorCode())){

                                    //msgCode=integerBaseResponse.getResult();
                                    editor.putInt("msgCode",integerBaseResponse.getResult());
                                    editor.putString("phoneCheck",mobile);
                                    editor.commit();

                                }
                            }
                        });
                getcord.setClickable(false);
                getcord.setBackgroundResource(R.drawable.login_edit_passed_bg);
                verCodeTimer.start();
                cord.setEnabled(true);


            }
        });
        //注册
        savecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = phone.getText().toString().trim();
                String code = cord.getText().toString().trim();
                String psd=etLoginPsd.getText().toString().trim();
                String secondPsd=etSecondLoginPsd.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(PhoneCheckActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(mobile)&&mobile.length()!=11) {
                    Toast.makeText(PhoneCheckActivity.this, "请输入有效的11位手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(PhoneCheckActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(psd)) {
                    Toast.makeText(PhoneCheckActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(secondPsd)) {
                    Toast.makeText(PhoneCheckActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!psd.equals(secondPsd)){
                    Toast.makeText(PhoneCheckActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                long sub=System.currentTimeMillis()-sp.getLong("currentTime",0);

            /*  if(sub>15*60*1000&&Integer.parseInt(code)==sp.getInt("msgCode",0)){
                    Toast.makeText(PhoneCheckActivity.this, "验证码已失效，请重新获取", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Integer.parseInt(code)!=sp.getInt("msgCode",0)){
                    Toast.makeText(PhoneCheckActivity.this, "你输入的验证码有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!mobile.equals(sp.getString("phoneCheck",null))){
                    Toast.makeText(PhoneCheckActivity.this, "验证码和手机号不匹配", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                DialogLoadUtils.getInstance(PhoneCheckActivity.this);
                DialogLoadUtils.showDialogLoad("正在提交...");
                save();
            }
        });

    }

    private void save() {
        if(!CommonUtil.isConnected(this)){
            Toast.makeText(this, "请检查你的网络", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("name",getIntent().getStringExtra("companyName"));
        mapParams.put("shortName",getIntent().getStringExtra("shortName"));
        mapParams.put("code",getIntent().getStringExtra("companyCode"));
        mapParams.put("address",getIntent().getStringExtra("address"));
        mapParams.put("user.password",etLoginPsd.getText().toString().trim());
        mapParams.put("user.phone",phone.getText().toString().trim());
        mapParams.put("scope",getIntent().getStringExtra("introduce")==null?"":getIntent().getStringExtra("introduce"));
        RetrofitFactory.getApiService(PhoneCheckActivity.this)
                .officeSave(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<String>>() {
                    @Override
                    public void onCompleted() {
                        DialogLoadUtils.dismissDialog();

                    }

                    @Override
                    public void onError(Throwable e) {
                          DialogLoadUtils.dismissDialog();
                          Toast.makeText(PhoneCheckActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BaseResponse<String> stringBaseResponse) {
                            if("00000".equals(stringBaseResponse.getErrorCode())){
                                editor.remove("msgCode");
                                editor.remove("phoneCheck");
                                editor.commit();
                                Toast.makeText(PhoneCheckActivity.this, "注册账号成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PhoneCheckActivity.this,LoginActivity.class));
                                finish();
                            }else {
                                Toast.makeText(PhoneCheckActivity.this, "注册账号失败,"+stringBaseResponse.getResult(), Toast.LENGTH_SHORT).show();

                            }
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

