
package com.gdc.bp;  
  
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
  
public class LoginActivity extends Activity {  
      
    private EditText userName, password;  
    private CheckBox rem_pw, auto_login;  
    private Button btn_login;   
    private String userNameValue,passwordValue;  
    private SharedPreferences sp;  
    DefaultHttpClient httpClient;
  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
          
        //ȥ������  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.login);  
          
        //���ʵ������  
        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);  
        userName = (EditText) findViewById(R.id.login_edit_account);  
        password = (EditText) findViewById(R.id.login_edit_pwd);  
        rem_pw = (CheckBox) findViewById(R.id.remember_user);  
        auto_login = (CheckBox) findViewById(R.id.auto_login);  
        btn_login = (Button) findViewById(R.id.btn_login);   

        httpClient = new DefaultHttpClient();
          
          
        //�жϼ�ס�����ѡ���״̬  
      if(sp.getBoolean("ISCHECK", true))  
        {  
          //����Ĭ���Ǽ�¼����״̬  
         rem_pw.setChecked(true);  
         userName.setText(sp.getString("USER_NAME", ""));  
         password.setText(sp.getString("PASSWORD", ""));  
          /*
          //�ж��Զ���½��ѡ��״̬  
          if(sp.getBoolean("AUTO_ISCHECK", false))  
          {  
                 //����Ĭ�����Զ���¼״̬  
                 auto_login.setChecked(true);  
                //��ת����  
                Intent intent = new Intent(LoginActivity.this,LogoActivity.class);  
                LoginActivity.this.startActivity(intent);  
                  
          }  
          */
        }  
          
        // ��¼�����¼�  ����Ĭ��Ϊ�û���Ϊ��li ���룺123  
        btn_login.setOnClickListener(new OnClickListener() {  
  
            public void onClick(View v) { 
                if(rem_pw.isChecked())  
                {  
                 //��ס�û��������롢  
                  Editor editor = sp.edit();  
                  editor.putString("USER_NAME", userNameValue);  
                  editor.putString("PASSWORD",passwordValue);  
                  editor.commit();  
                } 
            	System.out.println("-------1-------");
                userNameValue = userName.getText().toString();  
                passwordValue = password.getText().toString(); 
                System.out.println("------3-------");
                //final String JSESSIONID=""; //����һ����̬���ֶΣ�����sessionID 
                final String sid=sp.getString("session_sid", "");
                new Thread(new Runnable(){
            		@Override
            		public void run(){    

                    	System.out.println("-----Post start-----");
		                //HttpPost post = new HttpPost("http://bp.stage1.mybluemix.net/demo/login");
		                HttpPost post = new HttpPost("http://10.0.2.2:3000/demo/login");
		                //�ж��Զ���½��ѡ��״̬  
		                if(sp.getBoolean("AUTO_ISCHECK", false))  
			                {  
			                       //����Ĭ�����Զ���¼״̬  
			                       auto_login.setChecked(true);  
			                      //��ת����  
			                      if(null != sid){  
			                          post.setHeader("Cookie", "connect.sid="+sid);  
			                      }  
			                        
			                }  
                
                

		                // ������ݲ��������Ƚ϶�Ļ����ԶԴ��ݵĲ������з�װ
		                List params = new ArrayList();
		                params
		                    .add(new BasicNameValuePair("username", userNameValue));
		                params
		                    .add(new BasicNameValuePair("password", passwordValue));
		                try
		                {
		                	 
		                    // �����������
		                    post.setEntity(new UrlEncodedFormEntity(
		                        params, HTTP.UTF_8));
		                    // ����POST����
		                    HttpResponse response = httpClient.execute(post);
		                    // ����������ɹ��ط�����Ӧ
		                    if (response.getStatusLine()
		                        .getStatusCode() == 200)
		                    {
		                        String msg = EntityUtils
		                            .toString(response.getEntity());
		                        System.out.println(msg);
		                        System.out.println(response.getHeaders("Cookie").toString());
		                        CookieStore cookieStore = httpClient.getCookieStore();  
		                        List<Cookie> cookies = cookieStore.getCookies();  
		                        for(int i=0;i<cookies.size();i++){  
		                        	System.out.println("~~~~~"+cookies.get(i).getValue());
		                        	System.out.println(">>>>>"+cookies.get(i).getName());
		                            if("connect.sid".equals(cookies.get(i).getName())){  
		                                System.out.println("-----"+cookies.get(i).getValue());
		                                Intent regIntent = new Intent(LoginActivity.this,BPAS.class);
		                                sp.edit().putString("connect_sid", cookies.get(i).getValue()).commit(); 
		                               //regIntent.
		                                regIntent.putExtra("sessionid", cookies.get(i).getValue());
		                                //��ת����  
		                                Intent intent = new Intent(LoginActivity.this,LogoActivity.class);  
		                                LoginActivity.this.startActivity(intent);  
		                                startService(regIntent);
		                                break;  
		                            }  
		                        }  
		
		                        // ��ʾ��¼�ɹ�
		                        Toast.makeText(LoginActivity.this,
		                            msg, 5000).show();
		                    }
		                }
		                catch (Exception e)
		                {
		                    e.printStackTrace();
		                }
		            		}
		            	}).start();
                
                
                
                
                
                
                /*
                  
                if(userNameValue.equals("li")&&passwordValue.equals("123"))  
                {  
                    Toast.makeText(LoginActivity.this,"��¼�ɹ�", Toast.LENGTH_SHORT).show();  
                    //��¼�ɹ��ͼ�ס�����Ϊѡ��״̬�ű����û���Ϣ  
                    if(rem_pw.isChecked())  
                    {  
                     //��ס�û��������롢  
                      Editor editor = sp.edit();  
                      editor.putString("USER_NAME", userNameValue);  
                      editor.putString("PASSWORD",passwordValue);  
                      editor.commit();  
                    }  
                    //��ת����  
                    Intent intent = new Intent(LoginActivity.this,LogoActivity.class);  
                    LoginActivity.this.startActivity(intent);  
                    //finish();  
                      
                }else{  
                      
                    Toast.makeText(LoginActivity.this,"�û�����������������µ�¼", Toast.LENGTH_LONG).show();  
                } 
                */
                  
            }  
        });  
  
        //������ס�����ѡ��ť�¼�  
        rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
                if (rem_pw.isChecked()) {  
                      
                    System.out.println("��ס������ѡ��");  
                    sp.edit().putBoolean("ISCHECK", true).commit();  
                      
                }else {  
                      
                    System.out.println("��ס����û��ѡ��");  
                    sp.edit().putBoolean("ISCHECK", false).commit();  
                      
                }  
  
            }  
        });  
          /*
        //�����Զ���¼��ѡ���¼�  
        auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
                if (auto_login.isChecked()) {  
                    System.out.println("�Զ���¼��ѡ��");  
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();  
  
                } else {  
                    System.out.println("�Զ���¼û��ѡ��");  
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();  
                }  
            }  
        });          
 */
  
    }  
}

