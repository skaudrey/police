package com.policecom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
public class PoliceInfoActivity extends Activity{
	
	MyApplication myapp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_policeinfo);	
		
		myapp=(MyApplication)getApplication();
		((TextView)findViewById(R.id.pi_name)).setText(myapp.P_INFO.get_USER_NAME());
		((TextView)findViewById(R.id.pi_birth)).setText(myapp.P_INFO.get_USER_NUMBER());
		((TextView)findViewById(R.id.pi_address)).setText(myapp.P_INFO.get_USER_ADDRESS());
		((TextView)findViewById(R.id.pi_officeNum)).setText(myapp.P_INFO.get_USER_OFFICENUM());
		((TextView)findViewById(R.id.pi_phoneNum)).setText(myapp.P_INFO.get_USER_PHONE());
	}
}
