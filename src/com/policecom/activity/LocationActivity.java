package com.policecom.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.policecom.toserver.PoliceToServer;

public class LocationActivity  extends Activity{
	//提示
	private NotificationManager manager;  
    private Notification notification;  
    private PendingIntent pi;  
	
    private boolean b_note=true;
	// 定位相关
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
		
		public String latlng;
		public MyApplication myapp;
		
		 private Handler handler = new Handler() {
		        public void handleMessage(android.os.Message msg) {
		            // 第一次没有显示dialog的时候显示dialog
		        	if(msg.what==200 ){
		        		 System.out.println("轮询！！！！！！！！！！！！！联网成功");
		        		 if(msg.obj.equals("success")){
		        			 if(b_note){
		        				 notification("您的身边发生了一起案件！点进去查看", "警务通", "通知 ");  
		        				 b_note=false;
		        			 }else{
		        				 
		        			 }
		        		 }
		            }else{
		            	System.out.println("失败");
		            }
		            super.handleMessage(msg); 
		        }
		    };
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//setContentView(R.layout.activity_location);
			System.out.println("定位——————————————");
			myapp=(MyApplication)getApplication();
			// 定位初始化
			mLocClient = new LocationClient(this);
			mLocClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setScanSpan(1000);
			mLocClient.setLocOption(option);
			mLocClient.start();
			
			LocationActivity.this.finish();
		}

		/**
		 * 定位SDK监听函数
		 */
		public class MyLocationListenner implements BDLocationListener {

			
			public void onReceiveLocation(BDLocation location) {
				if (location == null)
	 	            return ;
				latlng=location.getLatitude()+"-"+location.getLongitude();
				System.out.println("经纬度————————————————"+latlng);
				PoliceToServer.addPath(myapp.get_URL(),myapp.P_INFO.get_USER_NUMBER(), myapp.pDuty.getDutyId(),latlng,handler);
				}

			public void onReceivePoi(BDLocation poiLocation) {
			}
		}

		@Override
		protected void onPause() {
			super.onPause();
		}

		@Override
		protected void onResume() {
			super.onResume();
		}

		@Override
		protected void onDestroy() {
			// 退出时销毁定位
			mLocClient.stop();
			super.onDestroy();
		}

		@SuppressWarnings("deprecation")
		private void notification(String content, String number, String date) {  
	        // 获取系统的通知管理器  
	        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
	        notification = new Notification(R.drawable.ic_launcher, content,  
	                System.currentTimeMillis());  
	        notification.defaults = Notification.DEFAULT_VIBRATE;//.DEFAULT_ALL; // 使用默认设置，比如铃声、震动、闪灯  
	        notification.flags = Notification.FLAG_AUTO_CANCEL; // 但用户点击消息后，消息自动在通知栏自动消失  
	        //notification.flags |= Notification.FLAG_NO_CLEAR;// 点击通知栏的删除，消息不会依然不会被删除  
	  
	        Intent intent = new Intent(getApplicationContext(),  
	                PoliceCaseActivity.class);  
	        intent.putExtra("content", content);  
	        intent.putExtra("number", number);  
	        intent.putExtra("date", date);  
	        
	        
	        pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);  
	          
	        notification.setLatestEventInfo(getApplicationContext(), number , content, pi);  
	  
	        // 将消息推送到状态栏  
	        manager.notify(0, notification);  
	  
	    }  
}
