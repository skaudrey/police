package com.policecom.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.policecom.adapter.HorizontalListView;
import com.policecom.adapter.ImageAdapter;
import com.policecom.imageactivity.NewTestPicActivity;
import com.policecom.toserver.PoliceToServer;
import com.policecom.toserver.UploadImage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PoliceInvestActivity extends Activity  implements OnGetGeoCoderResultListener{
	
	String location="";//经纬度用-隔开
	//获取控件
	EditText pcinvest_et_date,pcinvest_et_time;//日期和时间
	EditText pcinvest_et_city,pcinvest_et_dis,pcinvest_et_street;//地址
	Button pcinvest_bt_location;//地址按钮
	EditText pcinvest_et_criminal;//犯罪嫌疑人
	EditText pcinvest_et_client;//当事人
	Button pcinvest_bt_picture;//照片按钮
	EditText pcinvest_et_describe;//描述
	Button pcinvest_bt_submit;//提交按钮
	Spinner pcinvest_sp_type;//类型

	//地理编码搜索
	GeoCoder mSearch = null;
	
	//提交界面时内容	
	String showdate,showtime,showyear,showmonth,showday,showhour,showminute;//提交时界面中按钮的显示日期和时间
	String ptCity,ptDis,ptStreet;//提交时界面中按钮的地点		
	String showcriminal;//提交界面中犯罪嫌疑人
	String showclient;//提交界面中当事人
	String showdescribe;//提交界面中的描述
	String showTypeitem;//提交界面中类型


	Calendar calendar;//获得当前日期
	int c_year,c_month,c_day,c_hour,c_minute;//int型当前日期及时间
	
	String nowdate,nowtime;//字符串型当前日期和时间
	String month2bytes,day2bytes,hour2bytes,minute2bytes;//将单个9月改称09
	
	MyApplication myapp;
	//添加图片
	HorizontalListView imageListView;
	ImageAdapter imageAdapter;
	
	private static final int PHOTOTAKE_GRAPH = 0;// 拍照
	private static final int PHOTOALBUM_GRAPH = 1;// 拍照
	ProgressDialog dialog;
	Dialog endDialog;
	
	ArrayList<File>files=new ArrayList<File>();
	
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
        	if(msg.what==200){
        		if (myapp.imagePathList.size() == 0) {
                    System.out.println("无图片！");
                } else {
                    UploadImage.uploadFileToServer(myapp.get_URL(),files,imagehandler);
                }
            }else{
            	System.out.println("报案 失败");
            	Toast.makeText(PoliceInvestActivity.this, "提交失败！请检查网络", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg); 
        }
    };
    //处理上传和下载
    private Handler imagehandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
        	if(msg.what==200){
        		dialog.dismiss();
        		endDialog.show();
            }else{
            	Toast.makeText(PoliceInvestActivity.this, "上传图片失败！", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg); 
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_policeinvest);			
		myapp=(MyApplication)getApplication();
		System.out.println("开始查案！！！！！！！！！！！！");
		
		ptCity=myapp.finalCaseInfo.get_FINALCASE_CITY();
		ptStreet=myapp.finalCaseInfo.get_FINALCASE_Street();
		ptDis=myapp.finalCaseInfo.get_FINALCASE_DISTRICT();
		location=myapp.finalCaseInfo.get_FINALCASE_LOCATION();
		
		initView();	
		
		//日期点击修改
		pcinvest_et_date.setOnClickListener(new DateOnClicker());
		//时间点击修改
		pcinvest_et_time.setOnClickListener(new TimeOnClicker());
		
		//提交
		pcinvest_bt_submit.setOnClickListener(new SubmitOnClick());
		
		//搜索引擎初始化
		mSearch = GeoCoder.newInstance();
		//注册搜索监听
		mSearch.setOnGetGeoCodeResultListener(this);
	}
	
	private void initView(){
		
		
		
		//初始化数据
		//日期获取
		pcinvest_et_date=(EditText) findViewById(R.id.pcinvest_et_date);
		pcinvest_et_time=(EditText) findViewById(R.id.pcinvest_et_time);
		
		//地址获取
		pcinvest_et_city=(EditText) findViewById(R.id.pcinvest_et_city);
		pcinvest_et_dis=(EditText) findViewById(R.id.pcinvest_et_dis);
		pcinvest_et_street=(EditText) findViewById(R.id.pcinvest_et_street);
		
		//设置editText值
		if (myapp.finalCaseInfo.get_FINALCASE_CITY().equals("")||
  				myapp.finalCaseInfo.get_FINALCASE_DISTRICT().equals("")||
  				myapp.finalCaseInfo.get_FINALCASE_Street().equals("")) {	
  			
  		}
  		else {
  			pcinvest_et_city.setText(myapp.finalCaseInfo.get_FINALCASE_CITY());
  			System.out.println("设置"+myapp.finalCaseInfo.get_FINALCASE_CITY());
  			pcinvest_et_dis.setText(myapp.finalCaseInfo.get_FINALCASE_DISTRICT());
  			pcinvest_et_street.setText(myapp.finalCaseInfo.get_FINALCASE_Street());
  		}
				
		//地址按钮
		pcinvest_bt_location=(Button) findViewById(R.id.pcinvest_bt_location);
		
		pcinvest_bt_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ptCity=pcinvest_et_city.getText().toString();         //城市
	        	ptDis=pcinvest_et_dis.getText().toString();           //区
	        	ptStreet=pcinvest_et_street.getText().toString(); //街道
	        	
	        	myapp.finalCaseInfo.set_FINALCASE_CITY(ptCity);
	        	myapp.finalCaseInfo.set_FINALCASE_DISTRICT(ptDis);
	        	myapp.finalCaseInfo.set_FINALCASE_Street(ptStreet);
				Intent intent=new Intent(PoliceInvestActivity.this,PoliceCasePickPtActivity.class);
				startActivity(intent);
				finish();				
			}
		});
		
		//犯罪嫌疑人获取
		pcinvest_et_criminal=(EditText) findViewById(R.id.pcinvest_et_criminal);
		
		//当事人获取
		pcinvest_et_client=(EditText) findViewById(R.id.pcinvest_et_client);
		
		//照片按钮获取
		pcinvest_bt_picture=(Button) findViewById(R.id.pcinvest_bt_picture);
		pcinvest_bt_picture.setOnClickListener(new PictureOnClick());
		//描述获取
		pcinvest_et_describe=(EditText) findViewById(R.id.pcinvest_et_describe);
		//类型Spinner获取
		pcinvest_sp_type=(Spinner) findViewById(R.id.pcinvest_sp_type);

		//提交Button获取
		pcinvest_bt_submit=(Button) findViewById(R.id.pcinvest_bt_submit);
		
		
		calendar=Calendar.getInstance();//当前时间
		c_year=calendar.get(Calendar.YEAR);//当前年
		c_month=calendar.get(Calendar.MONTH)+1;//当前月
		c_day=calendar.get(Calendar.DAY_OF_MONTH);//当前天
		c_hour=calendar.get(Calendar.HOUR_OF_DAY);//当前小时
		c_minute=calendar.get(Calendar.MINUTE);//当前分
		
		
		//当月份为8月时，显示例如“08”，日、小时、分钟同样
        if(c_month<10){
	          month2bytes=0+Integer.toString(c_month);			
		}
        else {
	          month2bytes=Integer.toString(c_month);
        }
        
        if (c_day<10) {
        	day2bytes=0+Integer.toString(c_day);			
		}
        else {
        	day2bytes=Integer.toString(c_day);
		}
        
        if(c_hour<10){
        	
	          hour2bytes=0+Integer.toString(c_hour);			
		}
      else {
    	  hour2bytes=Integer.toString(c_hour);
      }
      
      if (c_minute<10) {
      	minute2bytes=0+Integer.toString(c_minute);			
		}
      else {
    	  minute2bytes=Integer.toString(c_minute);
		}
		
		
		
		pcinvest_et_date.setInputType(InputType.TYPE_NULL); //始终不弹出软键盘
		pcinvest_et_time.setInputType(InputType.TYPE_NULL); 
		
		//将默认时间设为当前系统时间
		nowdate=Integer.toString(c_year)+"-"+month2bytes+"-"+day2bytes;
		nowtime=hour2bytes+":"+minute2bytes;
		pcinvest_et_date.setText(nowdate);
		pcinvest_et_time.setText(nowtime);
		
		imageListView=(HorizontalListView)findViewById(R.id.pcinvest_image_list);//照片list
		myapp.imagePathList.clear();
		myapp.imagePathList=null;
		myapp.imagePathList=new ArrayList<String>();
		imageAdapter=new ImageAdapter(this, myapp.imagePathList);
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
			    intent.setClass(PoliceInvestActivity.this,ImageActivity.class);   
			    //绑定数据   
			    intent.putExtra("PATH",myapp.imagePathList.get(position));
			    startActivity(intent);
			}
        	
        });
        endDialog = new AlertDialog.Builder(this)
        .setTitle("感谢")
        .setMessage("提交完成！感谢您的立案。")
        .setNegativeButton("确定并退出", new DialogInterface.OnClickListener()  //设置一个“取消”button
        {
          public void onClick(DialogInterface d, int which)
          {
       	   d.dismiss();   //关闭对话窗口
       	   finish();
          }
        }).create();
		
	}
	
	//日期点击事件
		private class DateOnClicker implements OnClickListener  
	    {  
	       public void onClick(View arg0) { 
	    	   new DatePickerDialog(PoliceInvestActivity.this, 
						new DatePickerDialog.OnDateSetListener() {
							
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear,
									int dayOfMonth) {
								// TODO Auto-generated method stub
								 if(monthOfYear<9){
							          month2bytes=0+Integer.toString(monthOfYear+1);			
								}
						        else {
							          month2bytes=Integer.toString(monthOfYear+1);
						        }
						        
						        if (dayOfMonth<10) {
						        	day2bytes=0+Integer.toString(dayOfMonth);			
								}
						        else {
						        	day2bytes=Integer.toString(dayOfMonth);
								}
								
						        pcinvest_et_date.setText(year+"-"+month2bytes+"-"+day2bytes);
							}
						}, c_year, c_month-1, c_day).show();

	        }  
	    }
		
		//时间点击事件
		private class TimeOnClicker implements OnClickListener  
	    {  
	       public void onClick(View arg0) {
	    	   new TimePickerDialog(PoliceInvestActivity.this, 
	    			   new TimePickerDialog.OnTimeSetListener() {
						
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							// TODO Auto-generated method stub
							
							 if(hourOfDay<10){
						          hour2bytes=0+Integer.toString(hourOfDay);			
							}
					      else {
					    	  hour2bytes=Integer.toString(hourOfDay);
					      }
					      
					      if (minute<10) {
					      	minute2bytes=0+Integer.toString(minute);			
							}
					      else {
					    	  minute2bytes=Integer.toString(minute);
							}
							
					      pcinvest_et_time.setText(hour2bytes+":"+minute2bytes);
						}
					}, c_hour, c_minute, true).show();
	    	   
	       }
	    }
		
		private class SubmitOnClick implements OnClickListener  
	    {  
	        public void onClick(View arg0) {
	        	//时间及日期
	        	showdate=pcinvest_et_date.getText().toString();      //日期字符串  	形式2015-08-09
	        	showyear=showdate.substring(0,4);             //年字符串  形式2015
	        	showmonth=showdate.substring(5,7);            //月字符串  形式08
	        	showday=showdate.substring(8,10);             //日字符串  形式09
	        	
	        	showtime=pcinvest_et_time.getText().toString();      //时间字符串  形式08:10
	        	showhour=showtime.substring(0,2);             //小时字符串  形式08
	        	showminute=showtime.substring(3,5);           //分钟字符串  形式10
	        	//时间或日期为空时错误提醒
	        	if(showdate==null||showdate.length()<=0){
	        		pcinvest_et_date.requestFocus();
	        		pcinvest_et_date.setError("请输入日期！");       		
	        	}
	        	if(showtime==null||showtime.length()<=0){
	        		pcinvest_et_time.requestFocus();
	        		pcinvest_et_time.setError("请输入时间！");  		
	        	}
	        	
	        	//地点
	        	ptCity=pcinvest_et_city.getText().toString();         //城市
	        	ptDis=pcinvest_et_dis.getText().toString();           //区
	        	ptStreet=pcinvest_et_street.getText().toString();     //街道
	        	
	        	/*if (location.equals("")) {
	        		mSearch.geocode(new GeoCodeOption().city(
							ptCity).address(ptStreet));
				}*/
	        	        	
	        	//地点为空时错误提醒
	        	if(ptCity==null||ptCity.length()<=0){
	        		pcinvest_et_city.requestFocus();
	        		pcinvest_et_city.setError("请输入城市！");  		
	        	}
	        	if(ptDis==null||ptDis.length()<=0){
	        		pcinvest_et_dis.requestFocus();
	        		pcinvest_et_dis.setError("请输入区！");  		        		
	        	}	        	
	        	if(ptStreet==null||ptStreet.length()<=0){
	        		pcinvest_et_street.requestFocus();
	        		pcinvest_et_street.setError("请输入街道！");  		        		
	        	}
	        	
	        	//犯罪嫌疑人
	        	showcriminal=pcinvest_et_criminal.getText().toString(); 
	        	
	        	//当事人
	        	showclient=pcinvest_et_client.getText().toString(); 
	        	//类型
	        	showTypeitem=pcinvest_sp_type.getSelectedItem().toString();

	        	//描述
	        	showdescribe=pcinvest_et_describe.getText().toString(); 
	        	
	        	System.out.println(showdate);
	        	System.out.println(showtime);
	        	System.out.println(ptCity);
	        	System.out.println(ptDis);
	        	System.out.println(ptStreet);
	        	System.out.println(showcriminal);
	        	System.out.println(showclient);
	        	System.out.println(showdescribe);
	        	
	        	dialog=new ProgressDialog(PoliceInvestActivity.this);  
	            dialog.setTitle("提交");  
	            dialog.setMessage("正在提交，请稍后！");  
	            //开始登陆
	            dialog.show();
	            for(int i=0;i<myapp.imagePathList.size();i++){
	    			File file = new File(myapp.imagePathList.get(i));
	    			System.out.println(myapp.imagePathList.get(i).toString());
	    			files.add(file);
	    		}
	            System.out.println(files.size());
	            String paths="";
                int num=myapp.imagePathList.size();
                for(int i=0;i<num;i++){
                	paths=paths+files.get(i).getName()+"-";
                }
	            
                String URL=myapp.get_URL();
                
	        	
	        	if(location.equals("")||location==null){
	        		mSearch.geocode(new GeoCodeOption().
							city(ptCity)
							.address(ptStreet));
	    		}
	        	
	        	showdate=showdate.replace("-", "");
	        	showtime=showtime.replace(":", "");
	        	System.out.println("showdate!!"+showdate);
	        	System.out.println("showTime!!"+showtime);
	        	
	            String st=myapp.finalCaseInfo.get_FINALCASE_CASEID()+","
	        	        + myapp.finalCaseInfo.get_FINALCASE_CAR()+","
	        		    +showdate+showtime+","
	        			+showTypeitem+","
	        			+location+","
	        			+ptCity+","
	        			+ptDis+","
	        			+myapp.P_INFO.get_USER_ID()+","
	        			+showcriminal+","
	        			+showclient+","
	        			+showdescribe+","
	        			+paths;
	           PoliceToServer.FinalCaseToServer(URL, st, handler);
	        }
	    }
		
		//添加图片点击事件
		private class PictureOnClick implements OnClickListener  
		 {  
		     public void onClick(View arg0) { 
		    	showPictureDialog();
		     }
		 }
		
		//弹出拍照相册框
		public void showPictureDialog(){
			 final String[] names = { "拍照", "相册"};
			 Dialog alertDialog = new AlertDialog.Builder(this)
	        .setTitle("添加照片")
	        .setSingleChoiceItems(names, -1,new DialogInterface.OnClickListener() {
	                   @Override
	                   public void onClick(DialogInterface dialog,int which) {
	                        if(which==PHOTOTAKE_GRAPH){//拍照
	                        	 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
	                        	 //启动activity   
	                        	 startActivityForResult(intent, PHOTOTAKE_GRAPH);
	                             dialog.dismiss();
	                        }else{//相册
	                        	Intent intent = new Intent(PoliceInvestActivity.this, NewTestPicActivity.class);
	            				startActivityForResult(intent, PHOTOALBUM_GRAPH);
	                        	dialog.dismiss();
	                        }
	                               
	              }
	         })
	        .setNegativeButton("取消", new DialogInterface.OnClickListener()  //设置一个“取消”button
	        {
	          public void onClick(DialogInterface d, int which)
	          {
	       	   d.dismiss();   //关闭对话窗口
	          }
	        }).create();
	        alertDialog.show();
		 }
		
	    @Override  
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        // TODO Auto-generated method stub  
	    	super.onActivityResult(requestCode, resultCode, data);
			switch (requestCode) {
			case PHOTOTAKE_GRAPH:
				System.out.println(data.getData().toString());
				Uri uri=data.getData();
				//获取图片的路径：
	            String[] proj = {MediaStore.Images.Media.DATA};
	            //好像是android多媒体数据库的封装接口，具体的看Android文档
	            Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
	            //按我个人理解 这个是获得用户选择的图片的索引值
	            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	            //将光标移至开头 ，这个很重要，不小心很容易引起越界
	            cursor.moveToFirst();
	            //最后根据索引值获取图片路径
	            String path = cursor.getString(column_index);
	            myapp.imagePathList.add(path);
				imageAdapter.notifyDataSetChanged();
	            System.out.println(path);
				break;
			case PHOTOALBUM_GRAPH:
				if (data != null) {
					Bundle bundle = new Bundle();
					bundle = data.getExtras();
					ArrayList<String> mArrayList = bundle.getStringArrayList("imagelists");
					System.out.println(mArrayList.get(0));
					for (int i = 0; i < mArrayList.size(); i++) {
						Log.v("Thunder", mArrayList.get(i)+"");
					}
					for(int i=0;i<mArrayList.size();i++){
						myapp.imagePathList.add(mArrayList.get(i));
					}
					imageAdapter.notifyDataSetChanged();
				}
	        	break;
			}
	    }//end
	    //地理解析
	    public void onGetGeoCodeResult(GeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(PoliceInvestActivity.this, "抱歉，未能找到结果",
						Toast.LENGTH_LONG).show();
				return;
				}		
			location=result.getLocation().latitude+"-"+
				result.getLocation().longitude;	
			myapp.finalCaseInfo.set_FINALCASE_LOCATION(location);
			
			}
		//监听--反向地理编码
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			
			}
		protected void onPause() {
	  		super.onPause();
	  		
	  	}

	  	protected void onResume() {
	  		super.onResume();
	  		
	  	}

	  	protected void onDestroy() {
	  		super.onDestroy();
	  		mSearch.destroy();
	  		
	  	}
}
