package com.gdc.bp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {  
  
    public static final String TB_NAME = "bp_table";  
    public static final String ID = "_id";  
    public static final String MESSAGE = "message";  
      
    /** 
     * 构造方法 
     * @param context上下文环境 
     * @param name数据库名 
     * @param factory可选的游标工厂（通常是 Null） 
     * @param version数据库的版本（整形数据） 
     */  
    public DBHelper(Context context, String name, CursorFactory factory,  
            int version) {  
        super(context, name, factory, version);  
    }  
    /** 
     * 创建数据库后，对数据库的操作  
     */  
    public void onCreate(SQLiteDatabase db) {  
        //建立表格  
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + " (" + ID  
                + " INTEGER PRIMARY KEY," + MESSAGE + " VARCHAR"+ ")");  
    }  
    /** 
     * 数据库更新的方法 
     */  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
          
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);  
        onCreate(db);  
    }  
 
 public void onOpen(SQLiteDatabase db) {       
         super.onOpen(db);         
         //每次成功打开数据库后首先被执行       
     }       
} 