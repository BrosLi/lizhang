package com.gdc.bp;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.JSONCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;
import com.koushikdutta.async.http.socketio.StringCallback;

public class BPAS extends Service {
    String TAG = "BPAS";
    SocketIOClient mClient;
	ArrayList<Message> mMessages = new ArrayList<Message>();
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
    @Override
    public void onStart(Intent intent, int startId) {
    	Toast.makeText(this, "Play Service onStart", Toast.LENGTH_LONG).show();
    	socketOn();
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Toast.makeText(this, "Play Service Stopped", Toast.LENGTH_LONG).show();
        Log.v(TAG, "ServiconDestroy");
    }
    
    public void showNotify(String title,String content){
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(this)
    	        .setSmallIcon(R.drawable.ic_launcher)
    	        .setContentTitle(title)
    	        .setContentText(content);
    	Intent resultIntent = new Intent(this, Notify.class);
    	 
    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    	stackBuilder.addParentStack(Notify.class);
    	stackBuilder.addNextIntent(resultIntent);
    	PendingIntent resultPendingIntent =
    	        stackBuilder.getPendingIntent(
    	            0,
    	            PendingIntent.FLAG_UPDATE_CURRENT
    	        );
    	mBuilder.setContentIntent(resultPendingIntent);
    	NotificationManager mNotificationManager =
    	    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	mNotificationManager.notify(0, mBuilder.build());

        Log.d(TAG, "onCreate() executed");  
    	
    }
    
    public void socketOn(){
    	SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), "http://10.0.2.2:8000", mConnectCallback);
    }
    ConnectCallback mConnectCallback = new ConnectCallback() {

		@Override
		public void onConnectCompleted(Exception ex, SocketIOClient client) {
			if (ex != null) {
				ex.printStackTrace();
				return;
			}

			client.setStringCallback(new StringCallback() {
				@Override
				public void onString(String string, Acknowledge acknowledge) {
					Log.d("SOCKET", string);
				}
			});

			client.setJSONCallback(new JSONCallback() {
				@Override
				public void onJSON(JSONObject jsonObject, Acknowledge acknowledge) {
					Log.d("SOCKET", jsonObject.toString());
				}
			});

			client.on("event", new EventCallback() {
				@Override
				public void onEvent(JSONArray jsonArray, Acknowledge acknowledge) {

					System.out.println("received a event");
					Log.d("SOCKET", jsonArray.toString());
					Type listType = new TypeToken<ArrayList<Message>>() {
					}.getType();
					Gson gson = new Gson();
					//ArrayList<Message> messages = gson.fromJson(jsonArray.toString(), listType);
					//mMessages.addAll(messages);
					//scrollMyListViewToBottom();
					showNotify("BP",jsonArray.toString());
					updateDB("Jerry",jsonArray.toString());
					DoDatabase();
					
				}
			});
			mClient = client;
		}
	};
	private void updateDB(String sender,String message){
		//实例化对象  
        DBHelper DBhelp=new DBHelper(this, "BP_db", null, 1);  
        //获取写入数据对象  
        SQLiteDatabase db=DBhelp.getWritableDatabase();  
            //实例化放置数据对象  
    ContentValues values = new ContentValues();  
    //数据准备  
    values.put(DBHelper.MESSAGE, message);  
    //插入数据  
    db.insert(DBHelper.TB_NAME, DBHelper.ID, values);
	}

	 public void DoDatabase(){
	        DbDao dbDao=new DbDao();
	                //增
	        dbDao.exeDO("insert into xiangqiao_table values(null,'xiangqiao','xiangqiao 的内容','2011-11-10 12:10:11')");
	                //改
	        dbDao.exeDO("update xiangqiao_table set name='123' where id=1");
	                //查
	        String result=dbDao.exeQuery("select * from xiangqiao_table order by id desc");
	        System.out.println(result);
	                //删
	        //dbDao.exeDO("delete from xiangqiao_table where id=2");
	}
}