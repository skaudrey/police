package com.policecom.activity;

import java.util.ArrayList;

import com.policecom.adapter.HorizontalListView;
import com.policecom.adapter.ImageAdapter;
import com.policecom.toserver.DownloadImage;
import com.policecom.toserver.PoliceToServer;
import com.policecom.toserver.ServiceToServer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;

public class PoliceCaseActivity extends Activity{
	
	HorizontalListView imageListView;
	ImageAdapter imageAdapter;
	
	MyApplication myapp;
	ArrayList<String>resultlist=new ArrayList<String>();
	ArrayList<String> filenames=new ArrayList<String>();
	ArrayList<String> filepaths=new ArrayList<String>();
	String whichchoice;
	private Handler endhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
        	if(msg.what==200){
        		Intent intent=new Intent(PoliceCaseActivity.this, PoliceCaseMapActivity.class);
				startActivity(intent);
				finish();
            }else{
            	Toast.makeText(PoliceCaseActivity.this, "下载失败，请重试一次！", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg); 
        }
    };
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
        	if(msg.what==200){
        		
        		String caseResult=msg.obj.toString();
        		System.out.println("得到案件联网成功！");
        		System.out.println(caseResult);
        		//Toast.makeText(PoliceCaseActivity.this, caseResult, Toast.LENGTH_SHORT).show();
        		
        		String st1[]=caseResult.split(",");
        		for(int i=0;i<st1.length;i++){
        			resultlist.add(st1[i]);
        		}
        		myapp.pDuty.setEPt(resultlist.get(1));
        		System.out.println("将案件终点存起来！！！！！！！！！！！！"+resultlist.get(1));
        		System.out.println(myapp.pDuty.getEPt().latitude);
        		System.out.println(myapp.pDuty.getEPt().longitude);
        		
        		String st2[]=resultlist.get(4).split("-");
        		for(int i=0;i<st2.length;i++){
        			filenames.add(st2[i]);
        			System.out.println(filenames.get(i).toString());
        		}
        		//下载图片
        		System.out.println("开始下载图片！！！！");
        		DownloadImage.DownloadToServer(myapp.get_URL(),filenames, imagehandler);
            }else{
            	Toast.makeText(PoliceCaseActivity.this, "下载失败，请重试一次！", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg); 
        }
    };
    private Handler imagehandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
        	if(msg.what==200){
        		String fileString = Environment.getExternalStorageDirectory()+ "/Police/" +filenames.get(0);
        		System.out.println("下载图片成功！！！！");
        		PoliceToServer.GetAvailCar(myapp.get_URL(), carhandler);
				
        		
            }else{
            	Toast.makeText(PoliceCaseActivity.this, "下载图片失败，请重试一次！", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg); 
        }
    };
    private Handler carhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
        	if(msg.what==200){
        		String sts[]=msg.obj.toString().split(",");
        		for(int i=0;i<sts.length;i++){
        			System.out.println("警车车牌号："+sts[i]);
            		myapp.availCars.add(sts[i]);
        		}
        		initview();
            }else{
            	Toast.makeText(PoliceCaseActivity.this, "获取警车失败！", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg); 
        }
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_policecase);	
		
		myapp=(MyApplication)getApplication();
		// 关闭服务  
		Intent intent = new Intent(this, ServiceToServer.class);
		stopService(intent);
		// 查看案件
		System.out.println("案件信息！！！！");
		PoliceToServer.getCaseToServer(myapp.get_URL(), handler);
				
	}
	void initview(){
		System.out.println("initview_________");
		myapp.finalCaseInfo.set_FINALCASE_CASEID(Integer.parseInt(resultlist.get(8)));
		
		
		((TextView)findViewById(R.id.pccase_tv_showdate))
		.setText(resultlist.get(0).substring(0, 4)+"-"+resultlist.get(0).substring(4, 6)+"-"+resultlist.get(0).substring(6, 8));
		((TextView)findViewById(R.id.pccase_tv_showtime))
		.setText(resultlist.get(0).substring(8, 10)+"-"+resultlist.get(0).substring(10, 12));
		((TextView)findViewById(R.id.pccase_tv_showlocation)).setText(resultlist.get(6)+resultlist.get(7));
		((TextView)findViewById(R.id.pccase_tv_showtel)).setText(resultlist.get(2));
		((TextView)findViewById(R.id.pccase_tv_showtype)).setText(resultlist.get(3));
		((TextView)findViewById(R.id.pccase_tv_showdescribe)).setText(resultlist.get(5));
		
		imageListView=(HorizontalListView)findViewById(R.id.pc_image_list);//照片list
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
			    intent.setClass(PoliceCaseActivity.this,ImageActivity.class);   
			    //绑定数据   
			    intent.putExtra("PATH",filepaths.get(position));
			    startActivity(intent);
			}
        	
        });
        
		((Button) findViewById(R.id.pccase_bt))
				.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final String[] choices=(String[])myapp.availCars.toArray(new String[myapp.availCars.size()]);
						new AlertDialog.Builder(v.getContext())
								.setTitle("决定办案!\n若乘坐警车，请选择：")
								.setIcon(android.R.drawable.ic_dialog_info)
								.setMultiChoiceItems(choices, null, new OnMultiChoiceClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which, boolean isChecked) {
										// TODO Auto-generated method stub
										whichchoice=choices[which];
									}
								})
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog,int which) {
												// TODO Auto-generated method
												myapp.setStart(true);
												System.out.println(which);
												System.out.println(whichchoice);
												//Toast.makeText(PoliceCaseActivity.this, whichchoice, Toast.LENGTH_SHORT).show();
								        		myapp.finalCaseInfo.set_FINALCASE_CAR(whichchoice);
								        		PoliceToServer.StartCase(myapp.get_URL(), myapp.pDuty.getDutyId(), endhandler);
											}
										})
							   .setNegativeButton("取消", 
									   new DialogInterface.OnClickListener() {

									          @Override
									        public void onClick(DialogInterface dialog,int which) {
										      // TODO Auto-generated method
										     dialog.dismiss();
									}
								})
							   .show();
					}
				});
		
	}
	
}
