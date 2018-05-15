package com.blanink.ui.activity;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
/*import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;*/

import com.blanink.ui.activity.EaseChat.modle.DemoHelper;
//import com.videogo.openapi.EZOpenSDK;


import org.xutils.BuildConfig;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatformConfig;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/12/26.
 */
public class MyApplication extends Application {
    //MultiDexApplication
    public static Context applicationContext;
    private static MyApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";
    public static String currentUserNick = "";
    public static String ImagePath;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        DemoHelper.getInstance().init(applicationContext);
        //初始化xutils
        x.Ext.init(this);
       // x.Ext.setDebug(BuildConfig.DEBUG);
       // JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        PlatformConfig platformConfig = new PlatformConfig()
                .setQQ("1106704713", "uQmbxgnfUItm58mp")
                .setWechat("wx3ce0445736360261", "92ceaee5c84811650cda7dbdeec45aa0")
                .setSinaWeibo("377076020", "a0047bb649e9e2e0cbcbb2cc499ded80", "http://www.blanink.com");
        /**
         * since 1.5.0，1.5.0版本后增加API，支持在代码中设置第三方appKey等信息，当PlatformConfig为null时，或者使用JShareInterface.init(Context)时需要配置assets目录下的JGShareSDK.xml
         **/
        //*
        JShareInterface.setDebugMode(true);
        JShareInterface.init(this, platformConfig);

        new Thread(){
            @Override
            public void run() {
                File imageFile =  copyResurces( "yun.png", "yun.png", 0);
                if(imageFile != null){
                    ImagePath = imageFile.getAbsolutePath();
                    Log.e("MyApplication",ImagePath);
                }
                super.run();
            }
        }.start();

    }
    public static MyApplication getInstance() {
        return instance;
    }

    private  File copyResurces(String src, String dest, int flag){
        File filesDir = null;
        try {
            if(flag == 0) {//copy to sdcard
                filesDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/jiguang/" + dest);
                File parentDir = filesDir.getParentFile();
                if(!parentDir.exists()){
                    parentDir.mkdirs();
                }
            }else{//copy to data
                filesDir = new File(getFilesDir(), dest);
            }
            if(!filesDir.exists()) {
                filesDir.createNewFile();
                InputStream open = getAssets().open(src);
                FileOutputStream fileOutputStream = new FileOutputStream(filesDir);
                byte[] buffer = new byte[4 * 1024];
                int len = 0;
                while ((len = open.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                }
                open.close();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if(flag == 0){
                filesDir = copyResurces( src,  dest, 1);
            }
        }
        return filesDir;
    }
}
