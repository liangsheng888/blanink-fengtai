package com.blanink.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/***
 * 忘记密码
 */
public class ForgetActivity extends BaseActivity {

    private static final String TAG = "ForgetActivity";
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.bt_next)
    Button btNext;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        editor = sp.edit();
        initData();
    }

    private void initData() {
        String phoneHide = getIntent().getStringExtra("phone") + "";
        if (phoneHide.length() == 11) {
            phoneHide = phoneHide.substring(0, 3) + "****" + phoneHide.substring(7, 11);
        }
        tvPhone.setHint(phoneHide);
        //返回
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //返回
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //下一步
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mobile = phone.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(ForgetActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mobile.equals(getIntent().getStringExtra("phone"))) {
                    Toast.makeText(ForgetActivity.this, "手机号与该账号绑定的不匹配", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(CodeActivity.getIntent(ForgetActivity.this, getIntent().getStringExtra("userId"), getIntent().getStringExtra("phone")));

             /*   DialogLoadUtils.getInstance(ForgetActivity.this);
                DialogLoadUtils.showDialogLoad("检查账号中...");
                Map<String, Object> mapParams = new HashMap<String, Object>();
                mapParams.put("phone", mobile);
                Subscription sub = RetrofitFactory.getApiService(ForgetActivity.this)
                        .mobileCheck(mapParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<BaseResponse<User>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(ForgetActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                                DialogLoadUtils.dismissDialog();
                            }

                            @Override
                            public void onNext(BaseResponse<User> user) {
                                if ("00000".equals(user.getErrorCode())) {
                                    editor.putString("phoneCheck", mobile);
                                    editor.commit();
                                } else {
                                    Toast.makeText(ForgetActivity.this, "你输入的账号不存在", Toast.LENGTH_SHORT).show();
                                }
                                DialogLoadUtils.dismissDialog();
                            }
                        });
                msubscription.add(sub);*/
            }
        });
    }


    public static Intent getIntent(Context context, String phone, String id) {
        Intent intent = new Intent(context, ForgetActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("userId", id);
        return intent;
    }
}
