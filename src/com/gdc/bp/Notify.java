package com.gdc.bp;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gdc.bp.base.BaseActivity;
/*
 * ֪ͨ��Ӧ��
 */
public class Notify extends BaseActivity implements OnClickListener{
	private Button btn_stop;
	private Button btn_restart;
	private Button btn_show;
	private Button btn_bigstyle_show;
	private Button btn_show_progress;
	private Button btn_show_cz;
	private Button btn_clear;
	private Button btn_clear_all;
	private Button btn_show_custom;
	/** �����ת��ָ���Ľ��� */
	private Button btn_show_intent_act;

	/** Notification������ */
	NotificationCompat.Builder mBuilder;
	/** Notification��ID */
	int notifyId = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify);
		initView();
		startBPService();
		//initNotify();
		//Solution 1: Start a thread for socketIO
		//socketT.start();
		//Solution 2: Start a service for socketIO
		//Intent intent = new Intent(Notify.this, BPService.class);  
		//Bundle bundle=new Bundle();
		//bundle.putString("param", "oper1");
		//intent.putExtras(bundle);

	}

		

	private void initView() {
		btn_stop = (Button)findViewById(R.id.btn_stop);
		btn_restart = (Button)findViewById(R.id.btn_restart);
		btn_show = (Button)findViewById(R.id.btn_show);
		btn_bigstyle_show = (Button)findViewById(R.id.btn_bigstyle_show);
		btn_show_progress = (Button)findViewById(R.id.btn_show_progress);
		btn_show_cz = (Button)findViewById(R.id.btn_show_cz);
		btn_clear = (Button)findViewById(R.id.btn_clear);
		btn_clear_all = (Button)findViewById(R.id.btn_clear_all);
		btn_show_custom = (Button)findViewById(R.id.btn_show_custom);
		btn_show_intent_act = (Button)findViewById(R.id.btn_show_intent_act);
		//btn_show_intent_apk = (Button)findViewById(R.id.btn_show_intent_apk);
		btn_stop.setOnClickListener(this);
		btn_restart.setOnClickListener(this);
		btn_show.setOnClickListener(this);
		btn_bigstyle_show.setOnClickListener(this);
		btn_show_progress.setOnClickListener(this);
		btn_show_cz.setOnClickListener(this);
		btn_clear.setOnClickListener(this);
		btn_clear_all.setOnClickListener(this);
		btn_show_custom.setOnClickListener(this);
		btn_show_intent_act.setOnClickListener(this);
		//btn_show_intent_apk.setOnClickListener(this);
	}
	/** start service**/
	private void startBPService(){
		Intent intent = new Intent(Notify.this, BPService.class);  
		startService(intent);
	}
	/** stop service**/
	private void stopBPService(){
		Intent intent = new Intent(Notify.this, BPService.class);  
		stopService(intent);
	}	
	/** ��ʼ��֪ͨ�� */
	private void initNotify(){
		mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle("���Ա���")
				.setContentText("��������")
				.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
//				.setNumber(number)//��ʾ����
				.setTicker("����֪ͨ����")//֪ͨ�״γ�����֪ͨ��������������Ч����
				.setWhen(System.currentTimeMillis())//֪ͨ������ʱ�䣬����֪ͨ��Ϣ����ʾ
				.setPriority(Notification.PRIORITY_DEFAULT)//���ø�֪ͨ���ȼ�
//				.setAutoCancel(true)//���������־���û��������Ϳ�����֪ͨ���Զ�ȡ��  
				.setOngoing(false)//ture��������Ϊһ�����ڽ��е�֪ͨ������ͨ����������ʾһ����̨����,�û���������(�粥������)����ĳ�ַ�ʽ���ڵȴ�,���ռ���豸(��һ���ļ�����,ͬ������,������������)
				.setDefaults(Notification.DEFAULT_VIBRATE)//��֪ͨ������������ƺ���Ч������򵥡���һ�µķ�ʽ��ʹ�õ�ǰ���û�Ĭ�����ã�ʹ��defaults���ԣ�������ϣ�
				//Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND ������� // requires VIBRATE permission
				.setSmallIcon(R.drawable.ic_launcher);
	}
	
	/** ��ʾ֪ͨ�� */
	public void showNotify(String Title,String Content){
		
		mBuilder.setContentTitle(Title)
				.setContentText(Content)
//				.setNumber(number)//��ʾ����
				.setTicker("New Bluepush Notification");//֪ͨ�״γ�����֪ͨ��������������Ч����
		mNotificationManager.notify(notifyId, mBuilder.build());
//		mNotification.notify(getResources().getString(R.string.app_name), notiId, mBuilder.build());
	}
	
	/** ��ʾ����ͼ���֪ͨ�� */
	public void showBigStyleNotify() {
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		String[] events = new String[5];
		// Sets a title for the Inbox style big view
		inboxStyle.setBigContentTitle("����ͼ����:");
		// Moves events into the big view
		for (int i=0; i < events.length; i++) {
		    inboxStyle.addLine(events[i]);
		}
		mBuilder.setContentTitle("���Ա���")
				.setContentText("��������")
//				.setNumber(number)//��ʾ����
				.setStyle(inboxStyle)//���÷��
				.setTicker("����֪ͨ����");// ֪ͨ�״γ�����֪ͨ��������������Ч����
		mNotificationManager.notify(notifyId, mBuilder.build());
		// mNotification.notify(getResources().getString(R.string.app_name),
		// notiId, mBuilder.build());
	}
	
	/** ��ʾ��פ֪ͨ�� */
	public void showCzNotify(){
//		Notification mNotification = new Notification();//Ϊ�˼������⣬���ø÷��������Զ�����BUILD��ʽ����
//		Notification mNotification  = new Notification.Builder(this).getNotification();//���ַ�ʽ�Ѿ���ʱ
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//		//PendingIntent ��ת����
		PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, getIntent(), 0);  
		mBuilder.setSmallIcon(R.drawable.ic_launcher)
				.setTicker("��פ֪ͨ����")
				.setContentTitle("��פ����")
				.setContentText("ʹ��cancel()�����ſ��԰���ȥ��Ŷ")
				.setContentIntent(pendingIntent);
		Notification mNotification = mBuilder.build();
		//����֪ͨ  ��Ϣ  ͼ��  
		mNotification.icon = R.drawable.ic_launcher;
		//��֪ͨ���ϵ����֪ͨ���Զ������֪ͨ
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;//FLAG_ONGOING_EVENT �ڶ�����פ�����Ե���������������ȥ��  FLAG_AUTO_CANCEL  ������������ȥ��
		//������ʾ֪ͨʱ��Ĭ�ϵķ������𶯡�LightЧ��  
		mNotification.defaults = Notification.DEFAULT_VIBRATE;
		//���÷�����Ϣ������
		mNotification.tickerText = "֪ͨ����";
		//���÷���֪ͨ��ʱ��  
		mNotification.when=System.currentTimeMillis(); 
//		mNotification.flags = Notification.FLAG_AUTO_CANCEL; //��֪ͨ���ϵ����֪ͨ���Զ������֪ͨ
//		mNotification.setLatestEventInfo(this, "��פ����", "ʹ��cancel()�����ſ��԰���ȥ��Ŷ", null); //������ϸ����Ϣ  ,������������Ѿ������� 
		mNotificationManager.notify(notifyId, mNotification);
	}
	
	/** ��ʾ֪ͨ�������ת��ָ��Activity */
	public void showIntentActivityNotify(){
		// Notification.FLAG_ONGOING_EVENT --���ó�פ Flag;Notification.FLAG_AUTO_CANCEL ֪ͨ���ϵ����֪ͨ���Զ������֪ͨ
//		notification.flags = Notification.FLAG_AUTO_CANCEL; //��֪ͨ���ϵ����֪ͨ���Զ������֪ͨ
		mBuilder.setAutoCancel(true)//�������֪ͨ����ʧ  
				.setContentTitle("���Ա���")
				.setContentText("�����ת")
				.setTicker("����");
		//�������ͼACTION����ת��Intent
		Intent resultIntent = new Intent(this, MainActivity.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(pendingIntent);
		mNotificationManager.notify(notifyId, mBuilder.build());
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_stop:
			stopBPService();
			break;
		case R.id.btn_restart:
			startBPService();
			break;
		case R.id.btn_show:
			//showNotify();
			break;
		case R.id.btn_bigstyle_show:
			showBigStyleNotify();
			break;
		case R.id.btn_show_cz:
			showCzNotify();
			break;
		case R.id.btn_clear:
			clearNotify(notifyId);
			break;
		case R.id.btn_clear_all:
			clearAllNotify();
			break;
		case R.id.btn_show_intent_act:
			showIntentActivityNotify();
			break;
		default:
			break;
		}
	}

}
