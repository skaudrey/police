package com.policecom.activity;

import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.policecom.toserver.ServiceToServer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WelcomeActivity extends Activity{
	private static final String LTAG = WelcomeActivity.class.getSimpleName();
	
	private Button wel_btn_police;
	private Button wel_btn_sheriff;
	private Button wel_btn_case;
	
	MyApplication myapp;
	
	//地图相关
	private boolean mIsEngineInitSuccess = false;  
	private NaviEngineInitListener mNaviEngineInitListener =
			new NaviEngineInitListener() {  
	        public void engineInitSuccess() {  
	            //导航初始化是异步的，需要一小段时间，以这个标志来识别引擎是否初始化成功，为true时候才能发起导航  
	            mIsEngineInitSuccess = true;  
	        }  
	 
	        public void engineInitStart() {  
	        }  
	 
	        public void engineInitFail() {  
	        }  
	    };  
	private String getSdcardDir() {  
	        if (Environment.getExternalStorageState().equalsIgnoreCase(  
	                Environment.MEDIA_MOUNTED)) {  
	            return Environment.getExternalStorageDirectory().toString();  
	        }  
	        return null;  
	    }           

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 *//*
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			Log.d(LTAG, "action: " + s);
			TextView text = (TextView) findViewById(R.id.text_Info);
			text.setTextColor(Color.RED);
			if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				text.setText("网络出错");
			}
		}
	}*/

	/*private SDKReceiver mReceiver;*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_welcome);	
		
		myapp=(MyApplication)getApplication();
		
		// 关闭服务  
		Intent intent = new Intent(this, ServiceToServer.class);
		stopService(intent);
		//初始化数据
		wel_btn_police=(Button) findViewById(R.id.wel_btn_police);//警员登陆界面
		wel_btn_sheriff=(Button) findViewById(R.id.wel_btn_sheriff);//警长登陆界面		
		wel_btn_case=(Button) findViewById(R.id.wel_btn_case);//一键登陆界面
		
		//需要加入入口界面中		
		//初始化导航引擎  
        BaiduNaviManager.getInstance().  
            initEngine(this, getSdcardDir(), 
            		mNaviEngineInitListener, 
            		"RQIYHTaqs6iIHQUO9il8sWoG",
            		null);  	
		
		//警员登陆页面跳转
		wel_btn_police.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				myapp.set_USER_TYPE(1);
				
				Intent intent=new Intent(WelcomeActivity.this, PoliceLoginActivity.class);
				/*Intent intent=new Intent(WelcomeActivity.this, PoliceCaseMapActivity.class);*/
				startActivity(intent);
			}
		});
		
		//警长登陆页面跳转
		wel_btn_sheriff.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				myapp.set_USER_TYPE(2);
				
				Intent intent=new Intent(WelcomeActivity.this, SheriffLoginActivity.class);
				
				/*Intent intent=new Intent(WelcomeActivity.this, SheriffPoliceMapActivity.class);*/
				startActivity(intent);
				}
			});
		
		//一键报案页面跳转
		wel_btn_case.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				myapp.set_USER_TYPE(0);
				
				Intent intent=new Intent(WelcomeActivity.this, CaseActivity.class);
				startActivity(intent);
			}
		});
		}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}

}
