package com.blanink.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.BaseResponse;
import com.blanink.bean.User;
import com.blanink.utils.DialogLoadUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
public class AccountCheckActivity extends BaseActivity {

    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.bt_next)
    Button btNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_check);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        //返回
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //账号验证
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ac=account.getText().toString().trim();
                if(TextUtils.isEmpty(ac)){
                    Toast.makeText(AccountCheckActivity.this, "请输入你的账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogLoadUtils.getInstance(AccountCheckActivity.this);
                DialogLoadUtils.showDialogLoad("检查账号中...");
                Map<String, Object> mapParams = new HashMap<String, Object>();
                mapParams.put("loginName", ac);
                Subscription sub=RetrofitFactory.getApiService(AccountCheckActivity.this)
                        .accountCheck(mapParams)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<BaseResponse<User>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                DialogLoadUtils.dismissDialog();
                                Toast.makeText(AccountCheckActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(BaseResponse<User> userBaseResponse) {
                                DialogLoadUtils.dismissDialog();
                                if("00000".equals(userBaseResponse.getErrorCode())){
                                    if(userBaseResponse.getResult()!=null&&userBaseResponse.getResult().getPhone()!=null&&!userBaseResponse.getResult().getPhone().equals("")){
                                        startActivity(ForgetActivity.getIntent(AccountCheckActivity.this,userBaseResponse.getResult().getPhone(),userBaseResponse.getResult().getId()));

                                    }else{
                                        Toast.makeText(AccountCheckActivity.this, "该账号尚未绑定手机号，不能找回密码", Toast.LENGTH_SHORT).show();
                                    }
                             }else {
                                    Toast.makeText(AccountCheckActivity.this, userBaseResponse.getReason(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                msubscription.add(sub);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
