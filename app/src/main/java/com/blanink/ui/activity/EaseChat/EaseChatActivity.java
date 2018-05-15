package com.blanink.ui.activity.EaseChat;




import android.os.Bundle;

import com.blanink.R;
import com.blanink.ui.activity.BaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;


/**
 * Created by Administrator on 2017/1/10.
 * 聊天界面
 */

public class EaseChatActivity extends BaseActivity {
    public static EaseChatActivity activityInstance;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activityInstance = this;
        EaseChatFragment easeChatFragment=new ChatFragment();
        easeChatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.ec_layout_container,easeChatFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance=null;
    }
}
