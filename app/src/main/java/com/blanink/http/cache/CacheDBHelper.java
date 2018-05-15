package com.blanink.http.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者：Created by liangshen on 2018/1/24 0024 13:07
 * 邮箱：liangshen6868@163.com
 */
public class CacheDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "cache.db";
    private static final int DB_VERSION = 1;
    static final String CACHE_TABLE = "cache";

    public CacheDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + CACHE_TABLE +
                " (url text, params text, response text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + CACHE_TABLE;
        db.execSQL(sql);
        onCreate(db);
    }
}
