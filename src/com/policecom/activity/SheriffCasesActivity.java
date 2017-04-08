package com.policecom.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.policecom.activity.R;
import com.policecom.bean.FinalCaseInfo;
import com.policecom.toserver.SheriffToServer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SheriffCasesActivity extends Activity{
	
    ListView scases_lv;
    ArrayList<String> idString=new ArrayList<String>();
    ArrayList<String> typeStrings=new ArrayList<String>();
    ArrayList<String> descirbeString=new ArrayList<String>();
    ArrayList<Integer> imageIds=new ArrayList<Integer>();
    
	MyApplication myapp;
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what!=200){
				Toast.makeText(SheriffCasesActivity.this, "联网失败！请检查网络", Toast.LENGTH_SHORT).show();
			}
			else {
				if(msg.obj.equals("0")){
					Toast.makeText(SheriffCasesActivity.this, "最近无案件！", Toast.LENGTH_SHORT).show();
					return;
				}else{
					FinalCaseInfo.caseResultAnalysis(msg.obj.toString(),myapp);
					
					//Toast.makeText(SheriffCasesActivity.this, "共有案件"+myapp.finalCaseInfos.size()+"件", Toast.LENGTH_SHORT).show();
					initview();
				}
				
			}
			super.handleMessage(msg);
		}	
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_sheriffcases);	
		myapp=(MyApplication)getApplication();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmm");
		String time="20150203";//format.format(new Date());
		System.out.println(time);
		SheriffToServer.GetCasesFromServer(myapp.get_URL(), time,handler);
	}
	void initview(){
		// 初始化
		scases_lv = (ListView) findViewById(R.id.scases_lv);

		int num=myapp.finalCaseInfos.size();
		for(int i=0;i<num;i++){
			idString.add(myapp.finalCaseInfos.get(i).get_FINALCASE_ID()+"");
			typeStrings.add(myapp.finalCaseInfos.get(i).get_FINALCASE_TYPE());
			descirbeString.add(myapp.finalCaseInfos.get(i).get_FINALCASE_DESCRIPTION());
			imageIds.add(R.id.scaseslvitem_im_next);
		}
		// 存储数据的数组列表
		List<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();

		for (int i = 0; i < num; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("scaseslvitem_tv_id", idString.get(i));
			map.put("scaseslvitem_tv_type", typeStrings.get(i));

			map.put("scaseslvitem_tv_describe", descirbeString.get(i));
			map.put("scaseslvitem_im_next", imageIds.get(i));
			// 添加数据
			listData.add(map);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listData,
				R.layout.scaseslvitem, new String[] { "scaseslvitem_tv_id",
						"scaseslvitem_tv_type", "scaseslvitem_tv_describe",
						"scaseslvitem_im_next" }, new int[] {
						R.id.scaseslvitem_tv_id, R.id.scaseslvitem_tv_type,
						R.id.scaseslvitem_tv_describe,
						R.id.scaseslvitem_im_next });
		scases_lv.setAdapter(simpleAdapter);

		// 点击进入详细介绍
		scases_lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("点击案件"+position);
				Intent intent = new Intent();
				intent.setClass(SheriffCasesActivity.this,
						SheriffCaseActivity.class);
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
	}
	
}
