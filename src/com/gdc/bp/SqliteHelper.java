package com.gdc.bp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="xiangqiao_db"; 
    private static final String TABLE_NAME="xiangqiao_table"; 
    private static final int VERSION=1;
    
    public SqliteHelper(Context context, String name, CursorFactory factory,
            int version) {
        super(context, name, factory, version);
    }

    public SqliteHelper(Context context) {
        this(context, DATABASE_NAME, null, VERSION);
        
    }

    
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据表
        String sql="create table "+TABLE_NAME+" (id integer primary key autoincrement,name varchar(20),content text,time date)";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}