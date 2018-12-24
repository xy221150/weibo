package org.wxy.weibo.cosmos.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class SQLite extends SQLiteOpenHelper {
    private static final String DBNAME="cosmos.db";
    private static final int VERSION=1;
    public SQLite(@Nullable Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table info(_id integer primary key autoincrement," +
                "name varchar,token varchar,uid varchar,url varchar)");
        Log.d(DBNAME, "onCreate: "+"创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
