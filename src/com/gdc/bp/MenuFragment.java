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
     * �˵�������ֻ������һ��ListView�� 
     */  
    private ListView menuList;  
  
    /** 
     * ListView���������� 
     */  
    private ArrayAdapter<String> adapter;  



  
    /** 
     * �������ListView�����ݣ�����ͼ�ֻ�����������ݡ� 
     */  
    //private String[] menuItems = { "Sound", "Display" };  
  
    /** 
     * �Ƿ���˫ҳģʽ�����һ��Activity�а���������Fragment������˫ҳģʽ�� 
     */  
    private boolean isTwoPane;  
  
    /** 
     * ��Activity��Fragment��������ʱ����ʼ���������е����ݡ� 
     */  
    @Override  
    public void onAttach(Activity activity) {  
        super.onAttach(activity);  
        DbAdapter db = new DbAdapter(this.getActivity());
        //---��ȡ���б���---


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
        String[] newStr =  list.toArray(new String[size]); //����һ���������ж����ָ�����͵�����   
        //String[] newContent =  Contents.toArray(new String[size]); //����һ���������ж����ָ�����͵�����          
        //menuItems[2]="test";
        db.close();
        adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, newStr);  
    }  
  
    /** 
     * ����menu_fragment�����ļ���ΪListView�������������������˼����¼��� 
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
     * ��Activity������Ϻ󣬳��Ի�ȡһ�²����ļ����Ƿ���details_layout���Ԫ�أ������˵����ǰ 
     * ��˫ҳģʽ�����û��˵����ǰ�ǵ�ҳģʽ�� 
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
     * ����ListView�ĵ���¼�������ݵ�ǰ�Ƿ���˫ҳģʽ�����жϡ������˫ҳģʽ����ᶯ̬���Fragment�� 
     * �������˫ҳģʽ�������µ�Activity�� 
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