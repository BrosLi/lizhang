package com.gdc.bp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class DbAdapter
{
public static final String KEY_ROWID = "_id";
public static final String KEY_TITLE = "title";
public static final String KEY_CONTENT = "content";
public static final String KEY_SENDER = "sender";
public static final String KEY_TIME = "time";
private static final String TAG = "DbAdapter";
private static final String DATABASE_NAME = "BP_db";
private static final String DATABASE_TABLE = "Message";
private static final int DATABASE_VERSION = 1;
private static final String DATABASE_CREATE =
"create table Message (_id integer primary key autoincrement, "
+ "title text not null, content text not null,sender text not null, "
+ "time text not null);";
private final Context context;
private DatabaseHelper DBHelper;
private SQLiteDatabase db;
public DbAdapter(Context ctx)
{
this.context = ctx;
DBHelper = new DatabaseHelper(context);
}
private static class DatabaseHelper extends SQLiteOpenHelper
{
DatabaseHelper(Context context)
{
super(context, DATABASE_NAME, null, DATABASE_VERSION);
}
@Override
public void onCreate(SQLiteDatabase db)
{
db.execSQL(DATABASE_CREATE);
}
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion,
int newVersion)
{
Log.w(TAG, "Upgrading database from version " + oldVersion
+ " to "
+ newVersion + ", which will destroy all old data");
db.execSQL("DROP TABLE IF EXISTS Message");
onCreate(db);
}
}
//---打开数据库---

public DbAdapter open() throws SQLException
{
db = DBHelper.getWritableDatabase();
return this;
}
//---关闭数据库---

public void close()
{
DBHelper.close();
}
//---向数据库中插入一个标题---

public long insertTitle(String title, String content, String sender, String time)
{
ContentValues initialValues = new ContentValues();
initialValues.put(KEY_TITLE, title);
initialValues.put(KEY_CONTENT, content);
initialValues.put(KEY_SENDER, sender);
initialValues.put(KEY_TIME, time);
return db.insert(DATABASE_TABLE, null, initialValues);
}
//---删除一个指定标题---

public boolean deleteTitle(long rowId)
{
return db.delete(DATABASE_TABLE, KEY_ROWID +
"=" + rowId, null) > 0;
}
//---检索所有标题---

public Cursor getAllTitles()
{
return db.query(DATABASE_TABLE, new String[] {
KEY_ROWID,
KEY_TITLE,
KEY_CONTENT,
KEY_SENDER,
KEY_TIME},
null,
null,
null,
null,
null);
}
//---检索一个指定标题---

public Cursor getTitle(long rowId) throws SQLException
{
	System.out.println(DATABASE_TABLE);
	System.out.println(KEY_ROWID);
	System.out.println(KEY_TITLE);
	System.out.println(KEY_CONTENT);
	System.out.println(KEY_SENDER);
	System.out.println(KEY_TIME);
	System.out.println(rowId);
Cursor mCursor =
db.query(true, DATABASE_TABLE, new String[] {
KEY_ROWID,
KEY_TITLE,
KEY_CONTENT,
KEY_SENDER,
KEY_TIME
},
KEY_ROWID + "=" + rowId,
null,
null,
null,
null,
null);
if (mCursor != null) {
mCursor.moveToFirst();
}
return mCursor;
}
//---更新一个标题---

public boolean updateTitle(long rowId, String title,
String content, String sender, String time)
{
ContentValues args = new ContentValues();
args.put(KEY_TITLE, title);
args.put(KEY_CONTENT, content);
args.put(KEY_SENDER, sender);
args.put(KEY_TIME, time);
return db.update(DATABASE_TABLE, args,
KEY_ROWID + "=" + rowId, null) > 0;
}
}