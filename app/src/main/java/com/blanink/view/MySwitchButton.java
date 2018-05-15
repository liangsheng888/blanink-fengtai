package com.blanink.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blanink.R;

/**
 * 作者：Created by liangshen on 2018/3/16 0016 11:15
 * 邮箱：liangshen6868@163.com
 */
public class MySwitchButton extends FrameLayout {
    private ImageView openImage;
    private ImageView closeImage;

    public MySwitchButton(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public MySwitchButton(Context context) {
        this(context, null);
    }

    public MySwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MySwitchButton);
        Drawable openDrawable = ta.getDrawable(R.styleable.MySwitchButton_mySwitchOpenImage);
        Drawable closeDrawable = ta.getDrawable(R.styleable.MySwitchButton_mySwitchCloseImage);
        int switchStatus = ta.getInt(R.styleable.MySwitchButton_mySwitchStatus, 0);
        ta.recycle();

        LayoutInflater.from(context).inflate(R.layout.ease_widget_switch_button, this);
        openImage = (ImageView) findViewById(R.id.iv_switch_open);
        closeImage = (ImageView) findViewById(R.id.iv_switch_close);
        if(openDrawable != null){
            openImage.setImageDrawable(openDrawable);
        }
        if(closeDrawable != null){
            closeImage.setImageDrawable(closeDrawable);
        }
        if(switchStatus == 1){
            closeSwitch();
        }

    }

    /**
     * is switch open
     */
    public boolean isSwitchOpen(){
        return openImage.getVisibility() == View.VISIBLE;
    }

    public void openSwitch(){
        openImage.setVisibility(View.VISIBLE);
        closeImage.setVisibility(View.INVISIBLE);
    }

    public void closeSwitch(){
        openImage.setVisibility(View.INVISIBLE);
        closeImage.setVisibility(View.VISIBLE);
    }

}
