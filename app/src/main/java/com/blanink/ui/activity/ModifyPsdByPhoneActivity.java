package com.blanink.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.Response;
import com.blanink.utils.DialogLoadUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 找回密码
 */
public class ModifyPsdByPhoneActivity extends BaseActivity {

    @BindView(R.id.iv_last)
    TextView ivLast;
    @BindView(R.id.tender_manager)
    RelativeLayout tenderManager;
    @BindView(R.id.et_newPsd)
    EditText etNewPsd;
    @BindView(R.id.et_nick)
    EditText etNick;
    @BindView(R.id.ll_psd)
    LinearLayout llPsd;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.activity_modify_psd)
    RelativeLayout activityModifyPsdByPhoneActivity;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psd_by_phone);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);

    }

    public static Intent  getIntent(Context context, String userId){
        Intent intent= new Intent(context,ModifyPsdByPhoneActivity.class);
        intent.putExtra("USER_ID",userId);
        return intent;
    }
    @OnClick({R.id.iv_last, R.id.btn_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_last:
                finish();
                break;
            case R.id.btn_ok:
                commitData();
                break;
        }
    }


    private void commitData() {
        //修改前先验证
        // http://localhost:8088/blanink-api/passwordUpdata?loginName=admin&newPassword=admin&id=2
        String newPsd = etNewPsd.getText().toString().trim();
        String newPsdSecond = etNick.getText().toString().trim();
        if (TextUtils.isEmpty(newPsd)) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newPsdSecond)) {
            Toast.makeText(this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPsdSecond.equals(newPsd)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        DialogLoadUtils.getInstance(ModifyPsdByPhoneActivity.this);
        DialogLoadUtils.showDialogLoad("操作进行中...");
        loadData(newPsd);
    }

    //提交密码
    private void loadData(String psd) {
       Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("newPassword", psd);
        mapParams.put("userId", getIntent().getStringExtra("USER_ID"));
        mapParams.put("loginName", sp.getString("phoneCheck",""));
        Subscription subscription=  RetrofitFactory.getApiService(this)
                .passwordUpdata(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogLoadUtils.dismissDialog();
                        Toast.makeText(ModifyPsdByPhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response response) {
                        DialogLoadUtils.dismissDialog();
                        if (response.getErrorCode().equals("00000")) {
                            Toast.makeText(ModifyPsdByPhoneActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(ModifyPsdByPhoneActivity.this,LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else {
                            Toast.makeText(ModifyPsdByPhoneActivity.this, response.getReason(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        msubscription.add(subscription);

    }}
