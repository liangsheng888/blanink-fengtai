package com.blanink.utils;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by Administrator on 2017/6/20.
 * 打开 系统文件
 */
public class OpenFileUtils {
    public static Intent openFile(String filePath, String endName){

            /* 取得扩展名 */
            /* 依扩展名的类型决定MimeType */
        if(endName.endsWith("m4a")||endName.endsWith("mp3")||endName.endsWith("mid")||
                endName.endsWith("xmf")||endName.endsWith("ogg")||endName.endsWith("wav")){
            return getAudioFileIntent(filePath,endName);
        }else if(endName.endsWith("3gp")||endName.endsWith("mp4")){
            return getAudioFileIntent(filePath,endName);
        }else if(endName.endsWith("jpg")||endName.endsWith("gif")||endName.endsWith("png")||
                endName.endsWith("jpeg")||endName.endsWith("bmp")||endName.endsWith("pictureunlock")){
            return getImageFileIntent(filePath,endName);
        }else if(endName.endsWith("apk")){
            return getApkFileIntent(filePath,endName);
        }else if(endName.endsWith("ppt")){
            return getPptFileIntent(filePath,endName);
        }else if(endName.endsWith("xls")){
            return getExcelFileIntent(filePath,endName);
        }else if(endName.endsWith("doc")){
            return getWordFileIntent(filePath,endName);
        }else if(endName.endsWith("pdf")){
            return getPdfFileIntent(filePath,endName);
        }else if(endName.endsWith("chm")){
            return getChmFileIntent(filePath,endName);
        }else if(endName.endsWith("txt")){
            return getTextFileIntent(filePath,endName,false);
        }else{
            return getAllIntent(filePath,endName);
        }
    }

    //Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent( String filePath, String endName) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(filePath,endName  ));
        intent.setDataAndType(uri,"*/*");
        return intent;
    }
    //Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent( String filePath, String endName) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(filePath,endName  ));
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        return intent;
    }

    //Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent( String filePath, String endName) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(filePath,endName  ));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent( String filePath, String endName ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(filePath,endName ));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    //Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent( String filePath, String endName ){

        Uri uri = Uri.parse(filePath+endName ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(filePath+endName ).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent( String filePath, String endName) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath,endName ));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String filePath, String endName ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath,endName ));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent( String filePath, String endName){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath,endName ));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent( String filePath, String endName){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath,endName ));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent( String filePath, String endName ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath,endName ));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent( String filePath, String endName, boolean paramBoolean){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean){
            Uri uri1 = Uri.parse(filePath+endName);
            intent.setDataAndType(uri1, "text/plain");
        }else{
            Uri uri2 = Uri.fromFile(new File(filePath,endName  ));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }
    //Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent( String filePath, String endName ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath,endName ));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
}
