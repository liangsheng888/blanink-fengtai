package com.blanink.http;

import android.content.Context;

import okhttp3.OkHttpClient;

/**
 * 作者：Created by liangshen on 2017/10/17 0017 10:30
 * 邮箱：liangshen6868@163.com
 */
public class RetrofitFactory {
    private static final Object monitor = new Object();
    private static MyRetrofit myRetrofit;
    private static ApiService apiService;
    public static ApiService getApiService(Context context) {
        synchronized (monitor) {
            if (myRetrofit == null) {
                myRetrofit = new MyRetrofit(context);
            }
            if (apiService == null) {
                apiService = myRetrofit.getApiService();
            }
            return apiService;
        }
    }

    public static OkHttpClient getOkHttpClient(Context context) {
        synchronized (monitor) {
            if (myRetrofit == null) {
                myRetrofit = new MyRetrofit(context);
            }
            return myRetrofit.getOkHttpClient();
        }
    }

}
