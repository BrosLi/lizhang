package com.gdc.bp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.gdc.bp.BPAS;

public class HttpClientLogin extends Activity
{
    Button get;
    Button login;
    EditText response;
    DefaultHttpClient httpClient;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http);
        // 创建DefaultHttpClient对象
        httpClient = new DefaultHttpClient();
        get = (Button) findViewById(R.id.get);
        login = (Button) findViewById(R.id.btn_login);
        response = (EditText) findViewById(R.id.response);
        get.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	new Thread(new Runnable(){
            		@Override
            		public void run(){
                        // 创建一个HttpGet对象
                        HttpGet get = new HttpGet(
                            "http://10.0.2.2:3000/demo/home");
                        try
                        {
                            // 发送GET请求
                            HttpResponse httpResponse = httpClient.execute(get);
                            HttpEntity entity = httpResponse.getEntity();
                            if (entity != null)
                            {
                                // 读取服务器响应
                                BufferedReader br = new BufferedReader(
                                    new InputStreamReader(entity.getContent()));
                                String line = null;
                                response.setText("");
                                while ((line = br.readLine()) != null)
                                {
                                    // 使用response文本框显示服务器响应
                                    response.append(line + "\n");
                                    System.out.println(line);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
            		}
            	}).start();
            }
        });
        login.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	 final String LOGON_SITE = "localhost" ;  
            	 final int     LOGON_PORT = 3000;  
                final View loginDialog = getLayoutInflater().inflate(
                    R.layout.http, null); //显示的弹出登录界面的界面文件
                new AlertDialog.Builder(HttpClientLogin.this)
                    .setTitle("登录系统")
                    .setView(loginDialog)
                    .setPositiveButton("登录",
                        new DialogInterface.OnClickListener()
                        {
                    	String JSESSIONID; //定义一个静态的字段，保存sessionID 
                            @Override
                            public void onClick(DialogInterface dialog,
                                int which)
                            {
                            	new Thread(new Runnable(){
                            		@Override
                            		public void run(){
                            			
                                String name = ((EditText) loginDialog
                                    .findViewById(R.id.et_zh)).getText()
                                    .toString();
                                String pass = ((EditText) loginDialog
                                    .findViewById(R.id.et_mima)).getText()
                                    .toString();
                                HttpPost post = new HttpPost(
                                    "http://10.0.2.2:3000/demo/login");
                                if(null != JSESSIONID){  
                                    post.setHeader("Cookie", "JSESSIONID="+JSESSIONID);  
                                }  
                                // 如果传递参数个数比较多的话可以对传递的参数进行封装
                                List params = new ArrayList();
                                params
                                    .add(new BasicNameValuePair("username", name));
                                params
                                    .add(new BasicNameValuePair("password", pass));
                                try
                                {
                                	 
                                    // 设置请求参数
                                    post.setEntity(new UrlEncodedFormEntity(
                                        params, HTTP.UTF_8));
                                    // 发送POST请求
                                    HttpResponse response = httpClient
                                        .execute(post);
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
                                                JSESSIONID = cookies.get(i).getValue();  
                                                //CookieManager.getInstance().setCookie("http://10.0.2.2:3000", JSESSIONID);
                                                System.out.println("-----"+JSESSIONID);
                                                Intent regIntent = new Intent(HttpClientLogin.this,BPAS.class);
                                                
                                               //regIntent.
                                                regIntent.putExtra("sessionid", JSESSIONID);
                                                startService(regIntent);
                                        		//Intent intent = new Intent(BPAS.class);
                                        		//startService(intent);
                                                break;  
                                            }  
                                        }  

                                        // 提示登录成功
                                        Toast.makeText(HttpClientLogin.this,
                                            msg, 5000).show();
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            		}
                            	}).start();
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
    }
}