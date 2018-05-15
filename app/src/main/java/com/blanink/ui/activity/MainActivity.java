package com.blanink.ui.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blanink.R;
import com.blanink.ui.activity.EaseChat.ChatFragmentParent;
import com.blanink.ui.activity.EaseChat.GroupsActivity;
import com.blanink.ui.activity.EaseChat.modle.DemoHelper;
import com.blanink.ui.fragment.HomeFragment;
import com.blanink.ui.fragment.MineFragment;
import com.blanink.ui.fragment.NotifyFragment;
import com.blanink.http.RetrofitFactory;
import com.blanink.bean.DataSynRefreshEvent;
import com.blanink.bean.UnReadNotify;
import com.blanink.utils.Constant;
import com.blanink.utils.RePsdUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private static final int NOTIFY_CODE =9 ;
    @BindView(R.id.main_fl)
    FrameLayout mainFl;
    @BindView(R.id.main_rb_task)
    RadioButton mainRbTask;
    @BindView(R.id.unread_msg_number)
    TextView unreadMsgNumber;
    @BindView(R.id.main_rb_talk)
    RadioButton mainRbTalk;
    @BindView(R.id.main_rb_notify)
    RadioButton mainRbNotify;
    @BindView(R.id.main_rb_mine)
    RadioButton mainRbMine;
    @BindView(R.id.main_rg)
    LinearLayout mainRg;
    @BindView(R.id.unread_notify_number)
    TextView unreadNotifyNumber;
    private Fragment[] fragments = new Fragment[4];
    private LinearLayout main_rg;
    private RadioButton[] buttons = new RadioButton[4];
    private int newIndex;//下一个即将可见的
    private int oldIndex;//当前可见的碎片
    private SharedPreferences sp;
    private Boolean update = false;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;
    private ChatFragmentParent conversationListFragment;

    // user logged into another device
    public boolean isConflict = false;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;


    /**
     * check if current user account was remove
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        EventBus.getDefault().register(this);

        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                try {
                    //some device doesn't has activity to handle this intent
                    //so add try catch
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        }

        //make sure activity will not in background if user is logged into another device or removed
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            DemoHelper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        registerBroadcastReceiver();
        initView();
        initData();

    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    //找到相关控件
    private void initView() {

        buttons[0] = (RadioButton) this.findViewById(R.id.main_rb_task);
        //buttons[1] = (RadioButton) this.findViewById(R.id.main_rb_seek);
        buttons[1] = (RadioButton) this.findViewById(R.id.main_rb_talk);
        buttons[2] = (RadioButton) this.findViewById(R.id.main_rb_notify);
        buttons[3] = (RadioButton) this.findViewById(R.id.main_rb_mine);
        main_rg = (LinearLayout) findViewById(R.id.main_rg);
        //初始化，为fragments添加数据
        fragments[0] = new HomeFragment();
        // fragments[1] = new OrderFragment();
        fragments[1] = conversationListFragment = new ChatFragmentParent();
        fragments[2] = new NotifyFragment();
        fragments[3] = new MineFragment();
        //默认选中界面  （任务）
        getSupportFragmentManager().beginTransaction().add(R.id.main_fl, fragments[0]).commit();
        buttons[0].setChecked(true);
    }

    private void initData() {
        getUnReadNotify();//显示未读通知个数

    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void notifyRefresh(DataSynRefreshEvent event){
        if(event.getCode()==NOTIFY_CODE){
            Log.e(TAG,event.getReason());
            getUnReadNotify();
        }
    }
    //切换界面
    private void changeFragments(int newIndex) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (newIndex != oldIndex) {
            ft.hide(fragments[oldIndex]);//隐藏当前的界面
            if (!fragments[newIndex].isAdded()) {//如果没有添加则添加
                ft.add(R.id.main_fl, fragments[newIndex]);
            }
            ft.show(fragments[newIndex]).commit();//显示

        }
        buttons[oldIndex].setChecked(false);
        buttons[newIndex].setChecked(true);
        //改变当前的选中项
        oldIndex = newIndex;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            // updateUnreadAddressLable();
        }

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setUpdate(intent.getBooleanExtra("UPDATE", false));
        Log.e("MainActivity", "是否更新:" + intent.getBooleanExtra("UPDATE", false));
        Log.e("MainActivity", "onNewIntent");
        int code = intent.getIntExtra("DIRECT", -1);
        //  接收返回的状态吗
        if (code != -1) {
            changeFragments(code);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterBroadcastReceiver();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    //监听菜单 防止用户不小心退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDialogExit();
        }

        return false;

    }

    private void showDialogExit() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setContentView(R.layout.dialog_custom_exit);
        final Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.dialogAnimation);
        window.setAttributes(lp);
        window.findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        window.findViewById(R.id.tv_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
    }


    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        Log.e(TAG, "未读消息" + count);
        if (count > 0) {
            unreadMsgNumber.setText(String.valueOf(count));
            unreadMsgNumber.setVisibility(View.VISIBLE);
        } else {
            unreadMsgNumber.setVisibility(View.INVISIBLE);
        }
    }

    //得到维度消息总数
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }

    //注册本地广播 更新 未读消息个数
    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "收到消息通知");
                updateUnreadLabel();

                if (oldIndex == 1) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                }
            /*    updateUnreadAddressLable();
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                } else if (currentTabIndex == 1) {
                    if(contactListFragment != null) {
                        contactListFragment.refresh();
                    }
                }*/
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }


    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @OnClick({R.id.main_rb_task, R.id.main_rb_talk, R.id.main_rb_notify, R.id.main_rb_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_rb_task:
                newIndex = 0;
                break;
            case R.id.main_rb_talk:
                newIndex = 1;
                break;
            case R.id.main_rb_notify:
                newIndex = 2;
                break;
            case R.id.main_rb_mine:
                newIndex = 3;
                break;
        }
        changeFragments(newIndex);
    }

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (oldIndex == 1) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                }
            }
        });
    }

    //消息监听
    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
         /*   //red packet code : 处理红包回执透传消息
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
                    RedPacketUtil.receiveRedPacketAckMessage(message);
                }
            }
            //end of red packet code
            refreshUIWithMessage();*/
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };


    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            //已连接到服务器
            Log.e(TAG, "已连接到服务器");
            getUnReadNotify();//更新未读通知
        }

        @Override
        public void onDisconnected(final int error) {
            Log.e(TAG, "error:" + error);

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        Log.e(TAG, "显示帐号已经被移除");


                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登陆
                        Log.e(TAG, "显示帐号在其他设备登陆");
                        CreateLoginAlert();
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                            Log.e(TAG, "当前网络可用");

                        } else {

                            //当前网络不可用，请检查网络设置
                            Log.e(TAG, "当前网络不可用，请检查网络设置");

                        }
                    }

                }
            });
        }
    }

    //显示对话框

    private void CreateLoginAlert() {
        final AlertDialog ad = new AlertDialog.Builder(this).create();
        final View adi = View.inflate(this, R.layout.dialog_login, null);
        final EditText password = (EditText) adi.findViewById(R.id.txt_password);
        final EditText account = (EditText) adi.findViewById(R.id.txt_username);
        TextView tv_cancel = (TextView) adi.findViewById(R.id.tv_cancel);
        TextView tv_ok = (TextView) adi.findViewById(R.id.tv_ok);

        String PassWord = null;
        String Account = null;

        //显示密码
        Set<Map.Entry<String, String>> entrySet = RePsdUtils.getLoginPsd(MainActivity.this).entrySet();
        for (Map.Entry<String, String> st : entrySet) {
            Account = st.getKey();
            PassWord = st.getValue();
        }
        account.setText(Account);
        password.setText(PassWord);


        ad.setView(adi);
    /*
        */

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = password.getText().toString();
                String userName = account.getText().toString();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(MainActivity.this, "请输入账户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressDialog m_Dialog = ProgressDialog.show(MainActivity.this, "请等待...", "正在为你登陆...", true);
                login(userName, pwd, ad, m_Dialog);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ad.setCancelable(false);
        ad.setCanceledOnTouchOutside(false);
        ad.show();
        //设置注册点击事件

    }


    private void login(String username, String psd, final AlertDialog ad, final ProgressDialog m_Dialog) {
        EMClient.getInstance().login(username, psd, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "再次登陆环信账号成功");
                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        m_Dialog.dismiss();
                        ad.dismiss();
                    }
                });

            }

            @Override
            public void onError(int code, String error) {
                Log.e(TAG, "再次登陆环信账号失败" + code + "---" + error);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        m_Dialog.dismiss();
                    }
                });

            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    public void getUnReadNotify(){
        String userId=sp.getString("USER_ID",null);
        RetrofitFactory.getApiService(this)
                .getUnReadNotify(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UnReadNotify>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG,"加载未读通知完成");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,e.toString());

                    }

                    @Override
                    public void onNext(UnReadNotify unReadNotify) {
                        Log.e(TAG,unReadNotify.toString());

                        if(unReadNotify!=null&&unReadNotify.getResult()>0){
                            unreadNotifyNumber.setVisibility(View.VISIBLE);
                            unreadNotifyNumber.setText(unReadNotify.getResult()+"");
                        }else {
                            unreadNotifyNumber.setVisibility(View.GONE);

                        }
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if(result != null) {
            if(result.getContents() == null) {

            } else {
               startActivity(ScanLoadingActivity.getIntent(MainActivity.this,result.getContents()));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }

    }
}


