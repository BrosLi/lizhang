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
        // ����PreferenceActivity��������ʹ�õ�XML�ļ������� 
        getPreferenceManager().setSharedPreferencesName("userInfo"); 
        // ����XML��Դ�ļ����˴��Ͳ���ʹ��Activity��setContentView() 
        addPreferencesFromResource(R.xml.preferences); 

 
    } 
  
    @Override 
    public boolean onPreferenceChange(Preference preference, Object newValue) { 
        preference.setSummary(String.valueOf(newValue)); 
        return true; 
    } 
}