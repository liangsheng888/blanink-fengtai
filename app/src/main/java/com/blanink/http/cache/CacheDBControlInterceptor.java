package com.blanink.http.cache;

import android.util.Log;

import com.blanink.ui.activity.MyApplication;
import com.blanink.utils.CommonUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * 作者：Created by liangshen on 2018/1/24 0024 13:03
 * 邮箱：liangshen6868@163.com
 */
public class CacheDBControlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString(); //获取请求URL
        Buffer buffer = new Buffer();
        request.body().writeTo(buffer);
        Log.e("CacheDBInterceptor",buffer.toString());
        String params = buffer.readString(Charset.forName("UTF-8")); //获取请求参数
        Response response;
        if (CommonUtil.isConnected(MyApplication.applicationContext)) {
            int maxAge = 60 * 60*24;
            //如果网络正常，执行请求。
            Response originalResponse = chain.proceed(request);
            //获取MediaType，用于重新构建ResponseBody
            MediaType type = originalResponse.body().contentType();
            //获取body字节即响应，用于存入数据库和重新构建ResponseBody
            byte[] bs = originalResponse.body().bytes();
            Log.e("CacheDBInterceptor",bs.length+"");
            response = originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    //重新构建body，原因在于body只能调用一次，之后就关闭了。
                    .body(ResponseBody.create(type, bs))
                    .build();
            //将响应插入数据库
            CacheDao.getInstance(MyApplication.applicationContext).insertResponse(url, params, new String(bs, "GB2312"));
        } else {
            //没有网络的时候，由于Okhttp没有缓存post请求，所以不要调用chain.proceed(request)，会导致连接不上服务器而抛出异常（504）
            String b = CacheDao.getInstance(MyApplication.applicationContext).queryResponse(url, params); //读出响应
            Log.d("OkHttp", "request:" + url);
            Log.d("OkHttp", "request method:" + request.method());
            Log.d("OkHttp", "response body:" + b);
            int maxStale = 60 * 60 * 24 * 28;
            //构建一个新的response响应结果
            response = new Response.Builder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .body(ResponseBody.create(MediaType.parse("application/json"), b.getBytes()))
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .build();
        }
        return response;
    }
    }

