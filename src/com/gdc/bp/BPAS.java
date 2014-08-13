package com.gdc.bp;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.gdc.bp.socket.IOAcknowledge;
import com.gdc.bp.socket.IOCallback;
import com.gdc.bp.socket.SocketIO;
import com.gdc.bp.socket.SocketIOException;
import com.koushikdutta.async.http.socketio.SocketIOClient;

public class BPAS extends Service {
    String TAG = "BPAS";
    SocketIOClient mClient;
	ArrayList<Message> mMessages = new ArrayList<Message>();
	private SharedPreferences sp; 
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
    @Override
    public void onStart(Intent intent, int startId) {
    	Toast.makeText(this, "Play Service onStart", Toast.LENGTH_LONG).show();
    	String sessionid=intent.getStringExtra("sessionid");
    	try {
			socketOn(sessionid);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
    	mBuilder.setDefaults(Notification.DEFAULT_SOUND);
    	mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
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
    
    public void socketOn(String sessionid) throws MalformedURLException{
    	String host = "http://10.0.2.2:3000";
    	//String host = "http://bp.stage1.mybluemix.net";
    	//String cookie = CookieManager.getInstance().getCookie(host);
    	String cookie=sessionid;
    	if(cookie.length()<1){
    		cookie=sp.getString("session_sid", "");
    	}
    	SocketIO socket = new SocketIO(host);
    	socket.addHeader("cookie","connect.sid="+ cookie);
    	//socket.addHeader("host","127.0.0.1:3000");
    	socket.addHeader("authorization","connect.sid="+ cookie);
    	//socket.addHeader("connect.sid", cookie);
        socket.connect(new IOCallback() {
            @Override
            public void onMessage(JSONObject json, IOAcknowledge ack) {
                try {

                    System.out.println("Server said:" + json.toString(2));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String data, IOAcknowledge ack) {
                System.out.println("Server said: " + data);
            }

            @Override
            public void onError(SocketIOException socketIOException) {
                System.out.println("an Error occured");
                socketIOException.printStackTrace();
                showNotify("BluePush error","Authorization failed, please click to login");
            }

            @Override
            public void onDisconnect() {
                System.out.println("Connection terminated.");
            }

            @Override
            public void onConnect() {
                System.out.println("Connection established");
            }

            @Override
            public void on(String event, IOAcknowledge ack, Object... args) {
                System.out.println("Server triggered event '" + event + "'");
            	//message example
            	//{"name":"push-message","args":[{"subject":"subject","message":"message","sendto":"lijie"}]}
                System.out.println("~~~~~");
                if(event.equals("push-message")){
	                JSONObject obj = null;
				
						obj = (JSONObject) args[0];
	                try {
						System.out.println(obj.getString("subject"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                try {
						showNotify(obj.getString("subject"),obj.getString("message"));
						InsertDB(obj.getString("subject"),obj.getString("message"),"sender","2014/07/27");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        });

        // This line is cached until the connection is establisched.
        socket.send("Hello Server!");
    	
    	
   /* 	
    	//SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), "http://ppdppd-125721.euw1.nitrousbox.com:8000", mConnectCallback);
    	SocketIORequest request = new SocketIORequest("http://10.0.2.2:3000", "");
    	AsyncHttpRequestBody body;
    	//body.
    	//request.setBody(body);

    	request.addHeader("username", "aaaaaaa");
    	request.addHeader("password", "BBBB");
    	System.out.println("^^^^^^^^^^^^^^^^^^^");
    	AsyncHttpClient.getDefaultInstance().executeString(request);
    	
    	
    	SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), "http://10.0.2.2:3000", mConnectCallback);
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
		*/
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