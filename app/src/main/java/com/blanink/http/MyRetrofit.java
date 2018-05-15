package com.blanink.http;

import android.content.Context;

import com.blanink.http.cache.CacheDBControlInterceptor;
import com.blanink.http.cache.CacheInterceptor;
import com.blanink.utils.BaseUrlUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：Created by liangshen on 2017/10/17 0017 10:30
 * 邮箱：liangshen6868@163.com
 */
public class MyRetrofit {
    private static final String TAG = "MyRetrofit";
    private ApiService apiService;
    private OkHttpClient mOkHttpClient;
    private Context context;

    public MyRetrofit(Context context) {
        this.context = context;
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(BaseUrlUtils.NET_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        apiService = mRetrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }

    public OkHttpClient getOkHttpClient() {
        if (null == mOkHttpClient) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            mOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor(new CacheInterceptor (context))
                    .retryOnConnectionFailure(true)
                    .readTimeout(60, TimeUnit.MINUTES)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .cache(new Cache(new File(context.getCacheDir(), "cache"), 10240 * 1024))
                    .build();
        }
        return mOkHttpClient;
    }


}
