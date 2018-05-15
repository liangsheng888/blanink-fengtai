package com.blanink.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.jpush.android.service.PushService;

/**
 * 作者：Created by liangshen on 2017/11/2 0002 17:19
 * 邮箱：liangshen6868@163.com
 * 解决程序被杀死后不能收到通知
 */
public class JpushBroadcastReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent pushintent=new Intent(context,PushService.class);//启动极光推送的服务
        Log.e("JpushBroadcastReceive","开启激光推送服务");
        context.startService(pushintent);
    }
}
