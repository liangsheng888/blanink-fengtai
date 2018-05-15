package com.blanink.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.blanink.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import java.io.File;

import static com.blanink.R.id.imageView;

/**
 * Created by Administrator on 2017/6/21.
 */
public class GlideUtils {
    public static void glideImageView(Context context, ImageView iv, String url, Boolean isCycle) {
        Uri uri = Uri.parse(url);
        if(Util.isOnMainThread()) {
            if (isCycle) {
                Glide.with(context)
                        .load(uri)
                        .centerCrop()
                        .thumbnail(0.1f)
                        .transform(new GlideCircleTransform(context))
                        .placeholder(R.drawable.fail)
                        .error(R.drawable.fail)
                        .into(iv);
            } else {
                Glide.with(context)
                        .load(uri)
                        .centerCrop()
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.fail)
                        .error(R.drawable.default_load)
                        .into(iv);
            }
        }

    }

    //加载本地图p
    public static void glideLocalImageView(Context context, ImageView iv, String drawableName, Boolean isCycle) {
        if (isCycle) {
            Glide.with(context)
                    .load(new File(Environment.getExternalStorageDirectory(), drawableName))
                    .centerCrop()
                    .thumbnail(0.1f)
                    .transform(new GlideCircleTransform(context))
                    .placeholder(R.drawable.fail)
                    .error(R.drawable.fail)
                    .into(iv);
        } else {
            Glide.with(context)
                    .load(new File(Environment.getExternalStorageDirectory(), drawableName))
                    .centerCrop()
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.fail)
                    .error(R.drawable.default_load)
                    .into(iv);
        }

    }

    //加载圆角
    public static void glideCornerImageView(Context context, ImageView iv, String drawableName) {
        Uri uri = Uri.parse(drawableName);
        Glide.with(context)
                .load(uri)
                .centerCrop()
                .transform(new GlideCircleTransform(context))
                .thumbnail(0.1f)
                .placeholder(R.drawable.fail)
                .error(R.drawable.fail)
                .into(iv);
    }
}
