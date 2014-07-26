package com.gdc.bp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class refresh extends Activity {
    /** Called when the activity is first created. */
    private int[] images ;
    private ListView listview;
    private MyAdapter adapter;
    List<Map<String,Integer>> al;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh);
        images =  new int[]{android.R.drawable.ic_btn_speak_now,
                android.R.drawable.alert_light_frame,
                android.R.drawable.arrow_down_float,
                android.R.drawable.arrow_up_float,
                android.R.drawable.btn_star_big_off,
                android.R.drawable.btn_star_big_on,
                android.R.drawable.button_onoff_indicator_off,
                android.R.drawable.button_onoff_indicator_on,
                android.R.drawable.checkbox_off_background,
                android.R.drawable.checkbox_on_background,
                android.R.drawable.ic_btn_speak_now,
                android.R.drawable.ic_delete};
        listview = (ListView)findViewById(R.id.listview);
        al = new ArrayList<Map<String,Integer>>();
        for(int i=0; i<12; i++){
            HashMap<String,Integer > map = new HashMap<String,Integer>();
            map.put(""+i, images[i]);
            al.add(map);
        }

        adapter = new MyAdapter(this, al, R.layout.list_entry, new String[]{"imageview", "tv"}, 
                new int[]{R.id.imageview, R.id.tv});
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int count,
                    long arg3) {
                if(adapter.getCount()==count+1){
                    HashMap<String ,Integer> map = new HashMap<String, Integer>();
                    map.put(""+(adapter.mItemList.size()), android.R.drawable.ic_dialog_email);
                    al.add(map);
                    adapter.mItemList = al;
                    adapter.notifyDataSetChanged();
                    Toast.makeText(refresh.this, "����ˢ��", Toast.LENGTH_SHORT).show();
                }                
            }
        });
        
    }
    
    
    private class MyAdapter extends SimpleAdapter{
        int count = 0;
        private List<Map<String, Integer>> mItemList;
        public MyAdapter(Context context, List<? extends Map<String, Integer>> data,
                int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            mItemList = (List<Map<String, Integer>>) data;
            if(data == null){
                count = 0;
            }else{
                count = data.size();
            }
        }
        public int getCount() {
            return mItemList.size();
        }

        public Object getItem(int pos) {
            return pos;
        }

        public long getItemId(int pos) {
            return pos;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Map<String ,Integer> map = mItemList.get(position);
            int image  =  map.get(""+position);
            View view = super.getView(position, convertView, parent);
            ImageView imageview = (ImageView)view.findViewById(R.id.imageview);
            TextView tv = (TextView)view.findViewById(R.id.tv);
            imageview.setBackgroundResource(image);
            tv.setText(""+position);
            return view;
        }
    }
}