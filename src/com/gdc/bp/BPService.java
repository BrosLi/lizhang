package com.gdc.bp;

import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.gdc.bp.socket.IOAcknowledge;
import com.gdc.bp.socket.IOCallback;
import com.gdc.bp.socket.SocketIO;
import com.gdc.bp.socket.SocketIOException;

public class BPService extends Service {
    String TAG = "BPService";
    private boolean running;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Toast.makeText(this, "Play Service Created", Toast.LENGTH_LONG).show();
        Log.v(TAG, "ServiceonCreate");
        running=false;

    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        /*
         * //可以在OnCreate里面创建与音乐的链接，也可以在OnStart里面创建 mediaPlayer =
         * MediaPlayer.create(this, R.raw.test);
         */
        Toast.makeText(this, "Play Service onStart", Toast.LENGTH_LONG).show();
        Log.v(TAG, "ServiceonStart");
        try {
			socketOn();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(!running){
        	//socketT.start(); // 启动线程        
        }
        //mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Toast.makeText(this, "Play Service Stopped", Toast.LENGTH_LONG).show();
        Log.v(TAG, "ServiconDestroy");
        if(!running){
        	//socketT.start(); // 启动线程        
        }
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

    
    public void socketOn() throws MalformedURLException{
	        running=true;
			//final SocketIO socket = new SocketIO("http://ppdppd-125721.euw1.nitrousbox.com:3001/");
	        final SocketIO socket = new SocketIO("http://10.0.2.2:3001/");
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
	    	        //socket.reconnect();
	    	        //Reconnect by restart socketT thread
	    	       socket.reconnect();
	    	        System.out.println("reconnect start OnError");
	    	    }

	    	    @Override
	    	    public void onDisconnect() {
	    	        System.out.println("Connection terminated.");
	    	        //socket.reconnect();
	    	        //Reconnect by restart socketT thread
	    	        socket.reconnect();
	    	        System.out.println("reconnect start onDisconnect");
	    	    }

	    	    @Override
	    	    public void onConnect() {    	    	    
	    	        System.out.println("Connection established");
	    	        socket.send("Im an onConnect");
	    	    }

	    	    @Override
	    	    public void on(String event, IOAcknowledge ack, Object... args) {
	    	        System.out.println("Server triggered event '" + event + "'");
	    	        if(event.equals("Info")){
    	    	        JSONObject obj = (JSONObject)args[0];                        	
                        try {
    						showNotify("BP Notify",obj.getString("title"));
    					} catch (JSONException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
	    	        }
	    	    }
	    	});

	    	// This line is cached until the connection is established.
	    	//socket.send("Hello Server!");	
    }
}

