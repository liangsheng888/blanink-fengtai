package com.blanink.ui.activity.my;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.LoginResult;
import com.blanink.utils.GlideUtils;


import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/***
 * 我的个人资料
 */
public class MyProfile extends BaseActivity {
    private SharedPreferences sp;
    private TextView iv_last;
    private TextView tv_name;
    private ImageView iv_photo;
    private TextView tv_company;
    private TextView tv_role;
    private TextView tv_area;
    private TextView tv_phone;
    private TextView tv_modify;
    private LoginResult userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initView();
        initData();
    }

    private void initView() {
        iv_last = ((TextView) findViewById(R.id.iv_last));
        tv_name = ((TextView) findViewById(R.id.tv_name));
        iv_photo = ((ImageView) findViewById(R.id.iv_photo));
        tv_company = ((TextView) findViewById(R.id.tv_company));
        tv_role = ((TextView) findViewById(R.id.tv_role));
        tv_area = ((TextView) findViewById(R.id.tv_area));
        tv_phone = ((TextView) findViewById(R.id.tv_phone_number));
        tv_modify = ((TextView) findViewById(R.id.tv_modify));
    }

    private void initData() {
        loadUserInfoFromSever();
        iv_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfile.this, ModifyProfile.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("MyProfile", userInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUserInfoFromSever();
    }

    public void loadUserInfoFromSever() {
        // http://localhost:8088/blanink-api/customer/user?userId=fec25c7f7634448581e21876ef517c57
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id", sp.getString("USER_ID", null));
        RetrofitFactory.getApiService(this)
                .user(mapParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LoginResult loginResult) {
                        userInfo=loginResult;
                        if (loginResult.getResult().photo != null && loginResult.getResult().photo != "") {
                            GlideUtils.glideImageView(MyProfile.this, iv_photo, loginResult.getResult().photo, true);
                        }
                        tv_name.setText(loginResult.getResult().name);
                        tv_company.setText(loginResult.getResult().company.name);
                        tv_role.setText(loginResult.getResult().roleNames);
                        tv_phone.setText(loginResult.getResult().phone);
                        tv_area.setText(loginResult.getResult().company.area.name);
                    }
                });
    }
}
