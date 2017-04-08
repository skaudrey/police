package com.policecom.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageActivity extends Activity{
	
	MyApplication myapp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_image);	
		
		myapp=(MyApplication)getApplication();
		Intent intent = getIntent();   
		//获取数据   
		String path = intent.getStringExtra("PATH");
		Bitmap bm=BitmapFactory.decodeFile(path);
		((ImageView)findViewById(R.id.i_image)).setImageBitmap(bm);
		((ImageView)findViewById(R.id.i_image)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
