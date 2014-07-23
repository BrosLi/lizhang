package com.gdc.bp;

import android.app.Activity;
import android.os.Bundle;

public class DisplayActivity extends Activity {  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.display_activity); 
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }  
  
} 