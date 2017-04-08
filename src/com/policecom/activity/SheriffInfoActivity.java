package com.policecom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class SheriffInfoActivity extends Activity{
	
	MyApplication myapp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_sheriffinfo);	
		
		myapp=(MyApplication)getApplication();
		
		myapp=(MyApplication)getApplication();
		((TextView)findViewById(R.id.si_name)).setText(myapp.S_INFO.get_USER_NAME());
		((TextView)findViewById(R.id.si_birth)).setText(myapp.S_INFO.get_USER_NUMBER());
		((TextView)findViewById(R.id.si_address)).setText(myapp.S_INFO.get_USER_ADDRESS());
		((TextView)findViewById(R.id.si_officeNum)).setText(myapp.S_INFO.get_USER_OFFICENUM());
		((TextView)findViewById(R.id.si_phoneNum)).setText(myapp.S_INFO.get_USER_PHONE());
	}
}
