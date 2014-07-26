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
     * ���췽�� 
     * @param context�����Ļ��� 
     * @param name���ݿ��� 
     * @param factory��ѡ���α깤����ͨ���� Null�� 
     * @param version���ݿ�İ汾���������ݣ� 
     */  
    public DBHelper(Context context, String name, CursorFactory factory,  
            int version) {  
        super(context, name, factory, version);  
    }  
    /** 
     * �������ݿ�󣬶����ݿ�Ĳ���  
     */  
    public void onCreate(SQLiteDatabase db) {  
        //�������  
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + " (" + ID  
                + " INTEGER PRIMARY KEY," + MESSAGE + " VARCHAR"+ ")");  
    }  
    /** 
     * ���ݿ���µķ��� 
     */  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
          
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);  
        onCreate(db);  
    }  
 
 public void onOpen(SQLiteDatabase db) {       
         super.onOpen(db);         
         //ÿ�γɹ������ݿ�����ȱ�ִ��       
     }       
} 