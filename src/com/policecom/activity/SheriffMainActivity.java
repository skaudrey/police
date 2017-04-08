package com.policecom.activity;

import com.policecom.activity.R;
import com.policecom.bean.AddressInfo;
import com.policecom.toserver.SheriffToServer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SheriffMainActivity extends Activity{
	
	MyApplication myapp;
	
	
	private Handler sphandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			System.out.println("ghjkk");
			if(msg.what!=200){
				Toast.makeText(SheriffMainActivity.this, "联网失败！请检查网络", Toast.LENGTH_SHORT).show();
			}
			else {
				if(msg.obj.equals("0")){
					Toast.makeText(SheriffMainActivity.this, "最近无案件！", Toast.LENGTH_SHORT).show();
					return;
				}else{
					System.out.println("结果"+msg.obj);
					AddressInfo.addressResultAnalysis(msg.obj.toString(),myapp);
					System.out.println("警员个数——————"+myapp.addressInfos.size());
					//AddressInfo.addressResultAnalysis(msg.obj.toString(),myapp);
					System.out.println(myapp.addressInfos.size());
					Intent intent = new Intent(SheriffMainActivity.this,
							SheriffPolicesMapActivity.class);
					startActivity(intent);
				}
			}
			super.handleMessage(msg);
		}	
	};
	private Handler schandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			System.out.println("ghjkk");
			if(msg.what!=200){
				Toast.makeText(SheriffMainActivity.this, "联网失败！请检查网络", Toast.LENGTH_SHORT).show();
			}
			else {
				if(msg.obj.equals("0")){
					Toast.makeText(SheriffMainActivity.this, "无警车！", Toast.LENGTH_SHORT).show();
					return;
				}else{
					System.out.println("结果"+msg.obj);
					AddressInfo.caraddressResultAnalysis(msg.obj.toString(),myapp);
					System.out.println("警车个数——————"+myapp.caraddressInfos.size());
					//AddressInfo.addressResultAnalysis(msg.obj.toString(),myapp);
					System.out.println(myapp.caraddressInfos.size());
					Intent intent = new Intent(SheriffMainActivity.this,
							SheriffCarsMapActivity.class);
					startActivity(intent);
				}
			}
			super.handleMessage(msg);
		}	
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_sheriffmain);	
		
		myapp=(MyApplication)getApplication();
		// 查看最近发生案件
		((Button) findViewById(R.id.sm_btn_case))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SheriffMainActivity.this,
								SheriffCasesActivity.class);
						startActivity(intent);
					}
				});
		// 查看当前警员
		((Button) findViewById(R.id.sm_btn_police))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//连接服务器
						SheriffToServer.PoliceAddressToServer(myapp.get_URL(), sphandler);
						
					}
				});
		// 查看警车
		((Button) findViewById(R.id.sm_btn_policecar))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						SheriffToServer.CarAddressToServer(myapp.get_URL(), schandler);
						
					}
				});
		// 查看个人信息
		((Button) findViewById(R.id.sm_btn_info))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(SheriffMainActivity.this,
								SheriffInfoActivity.class);
						startActivity(intent);
					}
				});
	}
}
