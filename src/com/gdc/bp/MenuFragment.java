package com.gdc.bp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuFragment extends Fragment implements OnItemClickListener {  
	 public final static String EXTRA_MESSAGE = "com.gdc.bp.MESSAGE";
    /** 
     * 菜单界面中只包含了一个ListView。 
     */  
    private ListView menuList;  
  
    /** 
     * ListView的适配器。 
     */  
    private ArrayAdapter<String> adapter;  



  
    /** 
     * 用于填充ListView的数据，这里就简单只用了两条数据。 
     */  
    //private String[] menuItems = { "Sound", "Display" };  
  
    /** 
     * 是否是双页模式。如果一个Activity中包含了两个Fragment，就是双页模式。 
     */  
    private boolean isTwoPane;  
  
    /** 
     * 当Activity和Fragment建立关联时，初始化适配器中的数据。 
     */  
    @Override  
    public void onAttach(Activity activity) {  
        super.onAttach(activity);  
        DbAdapter db = new DbAdapter(this.getActivity());
        //---获取所有标题---


        //int i=2;
        List<String> list = new ArrayList<String>(); 
        List<String> Contents = new ArrayList<String>();  
        /*
        for (int i=0; i<menuItems.length; i++) {  
            list.add(menuItems[i]);  
        } 
        */ 


        db.open();
        Cursor c = db.getAllTitles();
        if (c.moveToFirst())
        {
        do {
        	 //menuItems[i] = c.getString(2);
        	list.add(c.getString(3)); //list.add("ruby")   
        	//Contents.add(c.getString(1));
        	 //i=i+1;
       // DisplayTitle(c);
        } while (c.moveToNext());
        }
        int size=list.size();
        String[] newStr =  list.toArray(new String[size]); //返回一个包含所有对象的指定类型的数组   
        //String[] newContent =  Contents.toArray(new String[size]); //返回一个包含所有对象的指定类型的数组          
        //menuItems[2]="test";
        db.close();
        adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, newStr);  
    }  
  
    /** 
     * 加载menu_fragment布局文件，为ListView绑定了适配器，并设置了监听事件。 
     */  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.menu_fragment, container, false);  
        menuList = (ListView) view.findViewById(R.id.menu_list);  
        menuList.setAdapter(adapter);  
        menuList.setOnItemClickListener(this);  
        return view;  
    }  
  
    /** 
     * 当Activity创建完毕后，尝试获取一下布局文件中是否有details_layout这个元素，如果有说明当前 
     * 是双页模式，如果没有说明当前是单页模式。 
     */  
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        if (getActivity().findViewById(R.id.details_layout) != null) {  
            isTwoPane = true;  
        } else {  
            isTwoPane = false;  
        }  
    }  
  
    /** 
     * 处理ListView的点击事件，会根据当前是否是双页模式进行判断。如果是双页模式，则会动态添加Fragment。 
     * 如果不是双页模式，则会打开新的Activity。 
     */  
    @Override  
    public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
    	DbAdapter db = new DbAdapter(this.getActivity());
    	db.open();
    	System.out.println("index = "+index+1);
    	Cursor c = db.getTitle((long)index+1);
    	String content = c.getString(2); 
    	String time = c.getString(4);
    	db.close();
        if (isTwoPane) {  
            Fragment fragment = null;  
           // if (index == 0) {  
               // fragment = new SoundFragment();  
           // } else if (index == 1) {  
                fragment = new DisplayFragment();  
            //} 
               
                Bundle bundle = new Bundle();
                bundle.putString("content", content);
                fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.details_layout, fragment).commit();  
        } else {  
            Intent intent = null;  
            //if (index == 0) {  
           //     intent = new Intent(getActivity(), SoundActivity.class);  
          //  } else if (index == 1) {  
            intent = new Intent(getActivity(), DisplayActivity.class);  
            intent.putExtra(EXTRA_MESSAGE,content);
          //  }  
            startActivity(intent);  
        }  
    }  
  
}  