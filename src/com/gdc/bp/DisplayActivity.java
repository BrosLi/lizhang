package com.gdc.bp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayActivity extends Activity {  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.display_activity); 
        Intent intent = getIntent();
        String message = intent.getStringExtra(MenuFragment.EXTRA_MESSAGE);
     // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        // Set the text view as the activity layout
        setContentView(textView);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }  
  
} 