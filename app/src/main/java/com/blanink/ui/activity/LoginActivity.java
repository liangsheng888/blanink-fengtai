package com.blanink.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.EaseChat.modle.DemoHelper;
import com.blanink.db.DemoDBManager;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.DataSynRefreshEvent;
import com.blanink.bean.LoginResult;
import com.blanink.utils.CheckNet;
import com.blanink.utils.CommonUtil;
import com.blanink.utils.DialogLoadUtils;
import com.blanink.utils.RePsdUtils;
import com.blanink.utils.StatusBarCompat;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import com.videogo.openapi.EZOpenSDK;

/***
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity {
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private static final String TAG = "LoginActivity ";
    private static final int FRAGMENT_TASK = 0;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.rl_login_progress)
    RelativeLayout rlLoginProgress;
    @BindView(R.id.iv_user)
    ImageView ivUser;
    @BindView(R.id.et_user)
    EditText etUser;
    @BindView(R.id.iv_psd)
    ImageView ivPsd;
    @BindView(R.id.et_psd)
    EditText etPsd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.activity_login)
    RelativeLayout activityLogin;
    @BindView(R.id.rl_user_)
    RelativeLayout rlUser;
    @BindView(R.id.rl_psd_)
    RelativeLayout rlPsd;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    private EditText et_user;
    private EditText et_psd;
    private Button btn_login;
    private TextView tv_forget;
    private RelativeLayout rl_login_progress;
    private ImageView imageView;
    private SharedPreferences sp;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:

            }
        }
    };
    private String loginName;
    private String psd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorTheme));
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        initView();
        initData();
    }

    private void initView() {
        et_user = ((EditText) findViewById(R.id.et_user));//找到账户
        et_psd = ((EditText) findViewById(R.id.et_psd));//找到密码
        btn_login = ((Button) findViewById(R.id.btn_login));//登录
        tv_forget = ((TextView) findViewById(R.id.tv_forget));//忘记密码
        rl_login_progress = ((RelativeLayout) findViewById(R.id.rl_login_progress));
        imageView = (ImageView) findViewById(R.id.loading_img);


    }

    private void initData() {

        SpannableString spannableString = new SpannableString("空行互联欢迎你");

        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(1.2f);
        RelativeSizeSpan sizeSpan02 = new RelativeSizeSpan(1.4f);
        RelativeSizeSpan sizeSpan03 = new RelativeSizeSpan(1.6f);
        RelativeSizeSpan sizeSpan04 = new RelativeSizeSpan(1.8f);
        RelativeSizeSpan sizeSpan05 = new RelativeSizeSpan(1.6f);
        RelativeSizeSpan sizeSpan06 = new RelativeSizeSpan(1.4f);
        RelativeSizeSpan sizeSpan07 = new RelativeSizeSpan(1.2f);

        spannableString.setSpan(sizeSpan01, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan02, 1, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan03, 2, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan04, 3, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan05, 4, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan06, 5, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan07, 6, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvName.setText(spannableString);

        //显示密码
        Set<Map.Entry<String, String>> entrySet = RePsdUtils.getLoginPsd(this).entrySet();
        for (Map.Entry<String, String> st : entrySet) {
            loginName = st.getKey();
            psd = st.getValue();
        }
        et_user.setText(loginName);
        et_psd.setText(psd);
        //登录验证
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得输入的账户，密码
                final String userName = et_user.getText().toString();
                final String passWord = et_psd.getText().toString();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, "账户不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passWord)) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Boolean isConnect = CheckNet.isNetWorkConnected(LoginActivity.this);
                //检查网络是否连接，
                DialogLoadUtils.getInstance(LoginActivity.this);
                DialogLoadUtils.showDialogLoad("登陆中...");
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        if (isConnect) {
                            //登录状态设置为显示
                            Map<String, Object> mapParams = new HashMap<String, Object>();
                            mapParams.put("loginName", userName);
                            mapParams.put("password", passWord);
                            RetrofitFactory.getApiService(LoginActivity.this)
                                    .loginResult(mapParams)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<LoginResult>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            DialogLoadUtils.dismissDialog();
                                            Toast.makeText(LoginActivity.this, "服务器无响应！", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onNext(final LoginResult loginResult) {
                                            DialogLoadUtils.dismissDialog();
                                            if ("00000".equals(loginResult.getErrorCode())) {
                                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                //setAccessToken();
                                                //保存用户信息
                                                SharedPreferences.Editor ed = sp.edit();
                                                //清空上个账号的信息
                                                ed.clear();
                                                ed.commit();
                                                //保存当前账号信息
                                                ed.putString("COMPANY_ID", loginResult.getResult().company.id);
                                                ed.putString("USER_ID", loginResult.getResult().id);
                                                ed.putString("LOGIN_NAME", loginResult.getResult().loginName);
                                                ed.putString("NAME", loginResult.getResult().name);
                                                ed.putString("PHONE", loginResult.getResult().phone);
                                                ed.putString("MOBILE", loginResult.getResult().mobile);
                                                ed.putString("PHOTO", loginResult.getResult().photo);
                                                ed.putString("ROLE_LIST", loginResult.getResult().roleList.toString());
                                                ed.putString("PASSWORD", passWord);
                                                ed.putString("COMPANY_TYPE", loginResult.getResult().office.serviceType);
                                                ed.commit();
                                                //
                                                RePsdUtils.saveLoginPsd(LoginActivity.this, userName, passWord);//记住密码
                                                setAlias(loginResult.getResult().id);
                                                DemoDBManager.getInstance().closeDB();

                                                // reset current user name before login

                                                //退出上个环信账号
                                                DemoHelper.getInstance().logout(true, new EMCallBack() {
                                                    @Override
                                                    public void onSuccess() {
                                                        Log.e(TAG, "退出环信账号成功");
                                                        RegisterUser(loginResult.getResult().getLoginName(), passWord);
                                                        login(loginResult.getResult().getLoginName(), passWord, loginResult);//灯鹭当前账号
                                                    }

                                                    @Override
                                                    public void onError(int i, String s) {
                                                        Log.e(TAG, "退出环信账号失败" + i + "---" + s.toString());

                                                    }

                                                    @Override
                                                    public void onProgress(int i, String s) {

                                                    }
                                                });


                                                //跳转到普通用户界面
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.putExtra("DATA", loginResult);
                                                intent.putExtra("DIRECT", FRAGMENT_TASK);
                                                Log.e(TAG, "是否更新:" + getIntent().getBooleanExtra("UPDATE", false));

                                                intent.putExtra("UPDATE", getIntent().getBooleanExtra("UPDATE", false));
                                                startActivity(intent);
                                                finish();


                                            } else if ("10004".equals(loginResult.getErrorCode())) {
                                                Toast.makeText(LoginActivity.this, "您的账户已经过期，请登录网页版进行续费！", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "用户或密码错误, 请重试！", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            DialogLoadUtils.dismissDialog();
                            Toast.makeText(LoginActivity.this, "你的网络有问题，请检查网络", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1500);


            }
        });
        //忘记密码
        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,AccountCheckActivity.class));

            }
        });

        //注册用户
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }

    //注册环信账户
    private void RegisterUser(final String userId, final String psd) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 注册方法，同步，需要自己异步执行，根据执行情况判断是否注册成功

                    EMClient.getInstance().createAccount(userId, psd);

                    Log.e(TAG, "注册环信账户成功");
                } catch (HyphenateException e) {
                    int errorCode = e.getErrorCode();
                    Log.e(TAG, "errorCode:" + errorCode);

                    if (errorCode == EMError.NETWORK_ERROR) {
                        // Toast.makeText(getApplicationContext(), "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "网络异常，请检查网络");

                    } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                        //Toast.makeText(getApplicationContext(), "用户已存在！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "用户已存在");

                    } else if (errorCode == EMError.USER_PERMISSION_DENIED) {
                        //Toast.makeText(getApplicationContext(), "注册失败，无权限！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "注册失败，无权限");

                    } else {
                        // Toast.makeText(getApplicationContext(), "注册失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "注册失败，无权限");

                    }

                }
            }
        }).start();
    }

    private void login(String username, String psd, final LoginResult loginResult) {
        EMClient.getInstance().login(username, psd, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e("login", "登陆环信账号成功");
                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        MyApplication.currentUserNick.trim());
                if (updatenick) {
                    Log.e(TAG, "update current user nick success");
                }
                DemoHelper.getInstance().setCurrentUserName(loginResult.getResult().getLoginName());
                DemoHelper.getInstance().getUserProfileManager().setCurrentUserNick(loginResult.getResult().name);
                DemoHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(loginResult.getResult().photo);
                Log.e(TAG, "当前用户信息:" + DemoHelper.getInstance().getUserProfileManager().getCurrentUserInfo().toString());
                // get user's info (this should be get from App's server or 3rd party service)
                // DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                //通知刷新
                if (getIntent().getBooleanExtra("UPDATE", false)) {
                    EventBus.getDefault().post(new DataSynRefreshEvent(true, 6, "通知刷新"));
                    EventBus.getDefault().post(new DataSynRefreshEvent(true, 7, "首页刷新"));

                }
            }

            @Override
            public void onError(int code, String error) {
                Log.e("login", "登陆环信账号失败" + code + "---" + error);

            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    //激光设置
    //设置别名（Jpush）
    private void setAlias(String alias) {
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        if (!CommonUtil.isValidTagAndAlias(alias)) {
            return;
        }

        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
            // CommonUtil.showToast(logs, getApplicationContext());
        }
    };


    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (CommonUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

            // CommonUtil.showToast(logs, getApplicationContext());
        }

    };
}
