package com.policecom.activity;

import java.util.ArrayList;

import com.policecom.adapter.HorizontalListView;
import com.policecom.adapter.ImageAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SheriffCaseActivity extends Activity{
	
	MyApplication myapp;
	int position;
	HorizontalListView imageListView;
	ImageAdapter imageAdapter;
	ArrayList<String> filenames=new ArrayList<String>();
	ArrayList<String> filepaths=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_sheriffcaseinfo);	
		
		myapp=(MyApplication)getApplication();
		Intent it=getIntent();
		position=it.getExtras().getInt("position");
		System.out.println("点击案件"+position);
		System.out.println(myapp.finalCaseInfos.get(position).FINALCASE_CITY);
		initview();
	}
	void initview(){
		
		((TextView)findViewById(R.id.scaseinfo_tv_showdate))
		.setText(myapp.finalCaseInfos.get(position).get_FINALCASE_TIME().substring(0, 4)+"-"+myapp.finalCaseInfos.get(position).get_FINALCASE_TIME().substring(4, 6)+"-"+
		myapp.finalCaseInfos.get(position).get_FINALCASE_TIME().substring(6, 8));
		((TextView)findViewById(R.id.scaseinfo_tv_showtime))
		.setText(myapp.finalCaseInfos.get(position).get_FINALCASE_TIME().substring(8, 10)+"-"+myapp.finalCaseInfos.get(position).get_FINALCASE_TIME().substring(10, 12));
		((TextView)findViewById(R.id.scaseinfo_tv_showlocation)).setText(myapp.finalCaseInfos.get(position).get_FINALCASE_CITY()
				+myapp.finalCaseInfos.get(position).get_FINALCASE_DISTRICT());
		((TextView)findViewById(R.id.scaseinfo_tv_showtel)).setText(
				myapp.finalCaseInfos.get(position).get_FINALCASE_PHONE());
		((TextView)findViewById(R.id.scaseinfo_tv_showtype)).setText(myapp.finalCaseInfos.get(position).get_FINALCASE_TYPE());
		((TextView)findViewById(R.id.scaseinfo_tv_showdescribe)).setText(myapp.finalCaseInfos.get(position).get_FINALCASE_DESCRIPTION());
		
		imageListView=(HorizontalListView)findViewById(R.id.scaseinfo_image_list);//照片list
		String st2[]=myapp.finalCaseInfos.get(position).get_FINALCASE_PATHS().split("-");
		for(int i=0;i<st2.length;i++){
			filenames.add(st2[i]);
			System.out.println(filenames.get(i).toString());
		}
		for(int i=0;i<filenames.size();i++){
			String st=Environment.getExternalStorageDirectory()
					+ "/Police/" + filenames.get(i);
			filepaths.add(st);
		}
		imageAdapter=new ImageAdapter(this, filepaths);
        imageListView.setAdapter(imageAdapter);
        //图片点击放大
        imageListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("你选择的图片"+position);
			    Intent intent = new Intent();  
			     //设置传递方向   
			    intent.setClass(SheriffCaseActivity.this,ImageActivity.class);   
			    //绑定数据   
			    intent.putExtra("PATH",filepaths.get(position));
			    startActivity(intent);
			}
        	
        });
		
	}
}
