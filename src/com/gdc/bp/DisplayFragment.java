package com.gdc.bp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayFragment extends Fragment {  
  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	String content="";
    	if(getArguments()!=null)
    	{
    		content = getArguments().getString("content");
    	}
        View view = inflater.inflate(R.layout.display_fragment, container, false); 

    	
    	TextView article = (TextView) view.findViewById(R.id.content);
        article.setText(content);
        return view; 
        
    }  
}  