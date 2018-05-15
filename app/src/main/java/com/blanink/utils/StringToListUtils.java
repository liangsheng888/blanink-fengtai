package com.blanink.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/27.
 * 将字符串 转化成集合的工具类
 */
public class StringToListUtils {

    public static List<String> stringToList(String str,String sign){
        List<String> strList=new ArrayList<>();
        if(str!=null){
            String [] st=str.split(sign);
            for (String s:
                st ) {
                strList.add(s);
            }
        }

        return strList;
    }
}
