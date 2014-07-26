package com.gdc.bp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbDao {

    SqliteHelper sqliteHelper;



	public void dbDao(Context applicationContext) {
        this.sqliteHelper = new SqliteHelper(applicationContext);
    }

    public String exeQuery(String sql) {
        SQLiteDatabase sqLiteDatabase = sqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            buffer.append(cursor.getInt(0)).append(" ")
                                 .append(cursor.getString(1)).append(" ")
                               .append(cursor.getString(2)).append(" ")
                               .append(cursor.getString(3)).append(" ");
        }
        cursor.close();
        sqLiteDatabase.close();
        sqliteHelper.close();

        return buffer.toString();
    }

    public void exeDO(String sql) {
        SQLiteDatabase sqLiteDatabase = sqliteHelper.getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
        sqliteHelper.close();
    }
}