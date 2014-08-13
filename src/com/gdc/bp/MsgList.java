package com.gdc.bp;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MsgList extends Activity {  
	  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        
        setContentView(R.layout.list);
       // DbAdapter db = new DbAdapter(this);
        //Start BluePush service
        //startBPService();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true; 

            case R.id.action_logoff:
            	logoff();
                return true;
            case R.id.action_stop:
            	stopBPService();
                return true;
            case R.id.action_restart:
            	startBPService();
                return true;    
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void openSettings(){
    	Intent intent = new Intent();
    	  intent.setClass(this, Setup.class);
    	  startActivity(intent);
    }
    
	/** start service**/
	private void startBPService(){
		Intent intent = new Intent(this, BPAS.class);  
		startService(intent);
	}
	/** stop service**/
	private void stopBPService(){
		Intent intent = new Intent(this, BPAS.class);  
		stopService(intent);
	}	

	/** test login demo **/
	private void delList(){
		//
	}
	/** test logoff **/
	private void logoff(){
		 SharedPreferences userInfo = getSharedPreferences("userInfo", 0);  
	     //userInfo.edit().putString("USER_NAME", "").commit();  
	     userInfo.edit().putString("PASSWORD", "").commit();
	     userInfo.edit().putBoolean("ISCHECK", false).commit();
	     userInfo.edit().putBoolean("AUTO_ISCHECK", false).commit();
		Intent intent = new Intent(this, LoginActivity.class);  
		startActivity(intent);
	}
  
  
}  