package com.gdc.bp;
import android.content.SharedPreferences; 
import android.os.Bundle; 
import android.preference.Preference; 
import android.preference.PreferenceActivity; 
import android.preference.PreferenceScreen; 
import android.preference.Preference.OnPreferenceChangeListener; 
  
public class Setup extends PreferenceActivity implements 
        OnPreferenceChangeListener { 
    /** Called when the activity is first created. */ 
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        // 设置PreferenceActivity保存数据使用的XML文件的名称 
        getPreferenceManager().setSharedPreferencesName("userInfo"); 
        // 加载XML资源文件：此处就不能使用Activity的setContentView() 
        addPreferencesFromResource(R.xml.preferences); 

 
    } 
  
    @Override 
    public boolean onPreferenceChange(Preference preference, Object newValue) { 
        preference.setSummary(String.valueOf(newValue)); 
        return true; 
    } 
}