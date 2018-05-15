package com.blanink.http.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 作者：Created by liangshen on 2018/1/24 0024 13:06
 * 邮箱：liangshen6868@163.com
 */
public class CacheDao {
    private static volatile CacheDao cacheDao;

    private CacheDBHelper helper;
    private SQLiteDatabase database;

    private CacheDao(Context context) {
        helper = new CacheDBHelper(context.getApplicationContext());
        database = helper.getWritableDatabase();
    }

    public static CacheDao getInstance(Context context) {
        if (cacheDao == null) {
            synchronized (CacheDao.class) {
                if (cacheDao == null) {
                    cacheDao = new CacheDao(context);
                }
            }
        }
        return cacheDao;
    }

    //查
    public String queryResponse(String urlKey, String params) {

        String response = null;
        if (database.isOpen()) {
            Cursor cursor = database.rawQuery("select * from  cache  where url=? and params=?", new String[]{urlKey, params});
            Log.e("CacheDao","select * from  cache  where url="+ urlKey+"and params="+params);
            while (cursor.moveToNext()) {
                response = cursor.getString(cursor.getColumnIndex("response"));
            }
        }
        return response;
    }

    //增
    public void insertResponse(String urlKey, String params, String value) {
        ContentValues values = new ContentValues();
        values.put("url", urlKey);
        values.put("params", params);
        values.put("response", value);
        if (database.isOpen()) {
            database.replace("cache", null, values);
        }
    }

    //改
    public void updateResponse(String urlKey, String params, String value) {
    }

    //删
    public void deleteResponse(String urlKey, String params) {
    }
}
