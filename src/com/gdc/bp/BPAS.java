package com.gdc.bp;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
    	Intent resultIntent = new Intent(this, MsgList.class);
    	 
    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    	stackBuilder.addParentStack(MsgList.class);
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
    	SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), "http://ppdppd-125721.euw1.nitrousbox.com:8000", mConnectCallback);
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
					//ArrayList<Message> messages = gson.fromJson(jsonArray.toString(), listType);
					//mMessages.addAll(messages);
					//scrollMyListViewToBottom();
					JSONObject obj = null;
					try {
						obj = (JSONObject) jsonArray.getJSONObject(0);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					try {
						showNotify(obj.getString("sender"),obj.getString("message"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						InsertDB("bp",obj.getString("message"),obj.getString("sender"),"2014/07/27");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("InsertDB done");
					
				}
			});
			mClient = client;
		}
	};
	
	public void InsertDB(String title,String content, String sender, String time){
        DbAdapter db = new DbAdapter(this);
      //---add 2 titles---

      db.open();
      long id;
      id = db.insertTitle(
      title,
      content,
      sender,
      time);
      db.close();
	}
}