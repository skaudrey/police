package com.policecom.activity;

import com.policecom.toserver.PoliceToServer;
import com.policecom.toserver.ServiceToServer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class PoliceMainActivity extends Activity{
	
	MyApplication myapp;
	
	private Handler carhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
        	if(msg.what==200){
        		String sts[]=msg.obj.toString().split(",");
        		for(int i=0;i<sts.length;i++){
        			System.out.println("警车车牌号："+sts[i]);
            		myapp.availCars.add(sts[i]);
        		}
				Intent intent=new Intent(PoliceMainActivity.this, PoliceCaseActivity.class);
				startActivity(intent);
            }else{
            	Toast.makeText(PoliceMainActivity.this, "获取警车失败！", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg); 
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_policemain);	
		
		myapp=(MyApplication)getApplication();
		
		Intent intent=new Intent(PoliceMainActivity.this,ServiceToServer.class);
		intent.putExtra("NUMBER", myapp.P_INFO.get_USER_NUMBER());
		startService(intent);
		//进入待查案件
		((Button)findViewById(R.id.pm_btn_case)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PoliceToServer.GetAvailCar(myapp.get_URL(), carhandler);
				
			}
		});
		//进入个人信息
		((Button)findViewById(R.id.pm_btn_info)).setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(PoliceMainActivity.this, PoliceInfoActivity.class);
				startActivity(intent);
			}
		});
	}
}
