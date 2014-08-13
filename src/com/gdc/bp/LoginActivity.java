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
          
        //去除标题  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.login);  
          
        //获得实例对象  
        sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);  
        userName = (EditText) findViewById(R.id.login_edit_account);  
        password = (EditText) findViewById(R.id.login_edit_pwd);  
        rem_pw = (CheckBox) findViewById(R.id.remember_user);  
        auto_login = (CheckBox) findViewById(R.id.auto_login);  
        btn_login = (Button) findViewById(R.id.btn_login);   

        httpClient = new DefaultHttpClient();
          
          
        //判断记住密码多选框的状态  
      if(sp.getBoolean("ISCHECK", true))  
        {  
          //设置默认是记录密码状态  
         rem_pw.setChecked(true);  
         userName.setText(sp.getString("USER_NAME", ""));  
         password.setText(sp.getString("PASSWORD", ""));  
          /*
          //判断自动登陆多选框状态  
          if(sp.getBoolean("AUTO_ISCHECK", false))  
          {  
                 //设置默认是自动登录状态  
                 auto_login.setChecked(true);  
                //跳转界面  
                Intent intent = new Intent(LoginActivity.this,LogoActivity.class);  
                LoginActivity.this.startActivity(intent);  
                  
          }  
          */
        }  
          
        // 登录监听事件  现在默认为用户名为：li 密码：123  
        btn_login.setOnClickListener(new OnClickListener() {  
  
            public void onClick(View v) { 
                if(rem_pw.isChecked())  
                {  
                 //记住用户名、密码、  
                  Editor editor = sp.edit();  
                  editor.putString("USER_NAME", userNameValue);  
                  editor.putString("PASSWORD",passwordValue);  
                  editor.commit();  
                } 
            	System.out.println("-------1-------");
                userNameValue = userName.getText().toString();  
                passwordValue = password.getText().toString(); 
                System.out.println("------3-------");
                //final String JSESSIONID=""; //定义一个静态的字段，保存sessionID 
                final String sid=sp.getString("session_sid", "");
                new Thread(new Runnable(){
            		@Override
            		public void run(){    

                    	System.out.println("-----Post start-----");
		                //HttpPost post = new HttpPost("http://bp.stage1.mybluemix.net/demo/login");
		                HttpPost post = new HttpPost("http://10.0.2.2:3000/demo/login");
		                //判断自动登陆多选框状态  
		                if(sp.getBoolean("AUTO_ISCHECK", false))  
			                {  
			                       //设置默认是自动登录状态  
			                       auto_login.setChecked(true);  
			                      //跳转界面  
			                      if(null != sid){  
			                          post.setHeader("Cookie", "connect.sid="+sid);  
			                      }  
			                        
			                }  
                
                

		                // 如果传递参数个数比较多的话可以对传递的参数进行封装
		                List params = new ArrayList();
		                params
		                    .add(new BasicNameValuePair("username", userNameValue));
		                params
		                    .add(new BasicNameValuePair("password", passwordValue));
		                try
		                {
		                	 
		                    // 设置请求参数
		                    post.setEntity(new UrlEncodedFormEntity(
		                        params, HTTP.UTF_8));
		                    // 发送POST请求
		                    HttpResponse response = httpClient.execute(post);
		                    // 如果服务器成功地返回响应
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
		                                //跳转界面  
		                                Intent intent = new Intent(LoginActivity.this,LogoActivity.class);  
		                                LoginActivity.this.startActivity(intent);  
		                                startService(regIntent);
		                                break;  
		                            }  
		                        }  
		
		                        // 提示登录成功
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
                    Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();  
                    //登录成功和记住密码框为选中状态才保存用户信息  
                    if(rem_pw.isChecked())  
                    {  
                     //记住用户名、密码、  
                      Editor editor = sp.edit();  
                      editor.putString("USER_NAME", userNameValue);  
                      editor.putString("PASSWORD",passwordValue);  
                      editor.commit();  
                    }  
                    //跳转界面  
                    Intent intent = new Intent(LoginActivity.this,LogoActivity.class);  
                    LoginActivity.this.startActivity(intent);  
                    //finish();  
                      
                }else{  
                      
                    Toast.makeText(LoginActivity.this,"用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();  
                } 
                */
                  
            }  
        });  
  
        //监听记住密码多选框按钮事件  
        rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
                if (rem_pw.isChecked()) {  
                      
                    System.out.println("记住密码已选中");  
                    sp.edit().putBoolean("ISCHECK", true).commit();  
                      
                }else {  
                      
                    System.out.println("记住密码没有选中");  
                    sp.edit().putBoolean("ISCHECK", false).commit();  
                      
                }  
  
            }  
        });  
          /*
        //监听自动登录多选框事件  
        auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
                if (auto_login.isChecked()) {  
                    System.out.println("自动登录已选中");  
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();  
  
                } else {  
                    System.out.println("自动登录没有选中");  
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();  
                }  
            }  
        });          
 */
  
    }  
}