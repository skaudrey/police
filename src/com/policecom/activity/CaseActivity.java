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
import com.policecom.toserver.CaseToServer;
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
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class CaseActivity extends Activity implements OnGetGeoCoderResultListener{
	
	
	HorizontalListView imageListView;
	ImageAdapter imageAdapter;
	
	String nowdate,nowtime;//当前日期，时间
	String month2bytes,day2bytes,hour2bytes,minute2bytes;//将单个9月改称09
	
	//提交界面时内容	
	String showdate,showtime,showyear,showmonth,showday,showhour,showminute;//提交时界面中按钮的显示日期和时间
	String showtel;//提交时界面中按钮的电话
	String showdescribe;
	String showTypeitem;//提交界面时类型Spinner选中的item

	//UI
	Button c_pictureButton,c_submitButton,c_pickButton;
	EditText c_et_date,c_et_time;//日期和时间
	EditText case_et_ptCity,case_et_ptDis,case_tv_ptStreet;//地点
	EditText  case_et_tel;//电话	
	EditText case_et_describe;//描述
	Spinner case_sp_type;//类型

	Calendar calendar;//当前时间
	int c_year,c_month,c_day,c_hour,c_minute;
	MyApplication myapp;
	
	private static final int PHOTOTAKE_GRAPH = 0;// 拍照
	private static final int PHOTOALBUM_GRAPH = 1;// 拍照
	ProgressDialog dialog;
	Dialog endDialog;
	final ArrayList<File>files=new ArrayList<File>();
	//地理编码搜索
	GeoCoder mSearch = null;
	
	//案件点经纬度
	String location="";
	//案件地址
	String ptDis="";
	String ptCity="";
	String ptStreet="";
	
	 private Handler handler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            // 第一次没有显示dialog的时候显示dialog
	        	if(msg.what!=200 ){
	        		System.out.println("报案失败");
	            }else{
	            	//上传图片
	            	if (myapp.imagePathList.size() == 0) {
	                    System.out.println("无图片！");
	                } else {
	                    UploadImage.uploadFileToServer(myapp.get_URL(),files,imagehandler);
	                }
	            }
	            super.handleMessage(msg); 
	        }
	    };
	    private Handler imagehandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            // 第一次没有显示dialog的时候显示dialog
	        	if(msg.what!=200){
	        		System.out.println("上传图片失败");
	            }else{
	            	dialog.dismiss();
	            	endDialog.show();
	            }
	            super.handleMessage(msg); 
	        }
	    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_case);	
		
		//搜索引擎初始化
		mSearch = GeoCoder.newInstance();
		//注册搜索监听
		mSearch.setOnGetGeoCodeResultListener(this);
				
		myapp=(MyApplication)getApplication();
		
		ptCity=myapp.U_INFO.getPtCity();
		System.out.println("city-----------"+ptCity);
		ptStreet=myapp.U_INFO.getPtStreet();
		ptDis=myapp.U_INFO.getPtDis();
		location=myapp.U_INFO.getCasePt();
		
		initView();		
		//日期点击修改
		c_et_date.setOnClickListener(new DateOnClicker());
		//时间点击修改
		c_et_time.setOnClickListener(new TimeOnClicker());
		//提交
		c_submitButton.setOnClickListener(new SubmitOnClick());
		c_pictureButton.setOnClickListener(new PictureOnClick());
		//删除图片imageListView.setOnItemClickListener(new ImageOnItemClickListener());
		
		//地图点选
		c_pickButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//地点
	        	ptCity=case_et_ptCity.getText().toString();         //城市
	        	ptDis=case_et_ptDis.getText().toString();           //区
	        	ptStreet=case_tv_ptStreet.getText().toString();     //街道
				
	        	myapp.U_INFO.setPtCity(ptCity);
	        	myapp.U_INFO.setPtStreet(ptStreet);
	        	myapp.U_INFO.setPtStreet(ptStreet);
	        	
	        	Intent intent=new Intent(CaseActivity.this, CaseMapActivity.class);
	        	
				startActivity(intent);
				finish();
			}
		});
	
		
		}
	
	private void initView(){
		//初始化数据
		calendar=Calendar.getInstance();//当前时间
		c_year=calendar.get(Calendar.YEAR);//当前年
		c_month=calendar.get(Calendar.MONTH)+1;//当前月
		c_day=calendar.get(Calendar.DAY_OF_MONTH);//当前天
		c_hour=calendar.get(Calendar.HOUR_OF_DAY);//当前小时
		c_minute=calendar.get(Calendar.MINUTE);//当前分
		//当月份为8月时，显示例如“08”，日、小时、分钟同样
		if(c_month<10){
			month2bytes=0+Integer.toString(c_month);}
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
			minute2bytes=0+Integer.toString(c_minute);}
		else {
			minute2bytes=Integer.toString(c_minute);
			}
		c_et_date=(EditText) findViewById(R.id.case_et_date);//日期edittext
		c_et_time=(EditText) findViewById(R.id.case_et_time);//时间edittext
				
		c_et_date.setInputType(InputType.TYPE_NULL); //始终不弹出软键盘
		c_et_time.setInputType(InputType.TYPE_NULL); 
		
		//将默认时间设为当前系统时间
		nowdate=Integer.toString(c_year)+"-"+month2bytes+"-"+day2bytes;
		nowtime=hour2bytes+":"+minute2bytes;
		c_et_date.setText(nowdate);
		c_et_time.setText(nowtime);
		
		//地点获取
		case_et_ptCity=(EditText) findViewById(R.id.case_et_ptCity);
		case_et_ptDis=(EditText) findViewById(R.id.case_et_ptDis);
		case_tv_ptStreet=(EditText) findViewById(R.id.case_et_ptStreet);
		System.out.println("设置"+myapp.U_INFO.getPtCity());
		
		
		
		//电话
		case_et_tel=(EditText) findViewById(R.id.case_et_tel);
		
		//描述
		case_et_describe=(EditText) findViewById(R.id.case_et_describe);
		
		//类型
		case_sp_type=(Spinner) findViewById(R.id.case_sp_type);

		
		c_pickButton=(Button)findViewById(R.id.case_bt_location);
		c_submitButton=(Button) findViewById(R.id.c_btn_submit);//提交button
		c_pictureButton=(Button) findViewById(R.id.c_btn_picture);//上传button
		imageListView=(HorizontalListView)findViewById(R.id.c_image_list);//照片list
		myapp.imagePathList.clear();
		myapp.imagePathList=null;
		myapp.imagePathList=new ArrayList<String>();
		imageAdapter=new ImageAdapter(this, myapp.imagePathList);
        imageListView.setAdapter(imageAdapter);
        
        endDialog = new AlertDialog.Builder(this)
        .setTitle("感谢")
        .setMessage("提交完成！感谢您的报案，我们将及时处理。")
        .setNegativeButton("确定并退出", new DialogInterface.OnClickListener()  //设置一个“取消”button
        {
          public void onClick(DialogInterface d, int which)
          {
       	   d.dismiss();   //关闭对话窗口
       	   finish();
          }
        }).create();
        
      //若地图已点选，直接初始化
        
      		if (myapp.U_INFO.getPtCity().equals("")||
      				myapp.U_INFO.getPtDis().equals("")||
      				myapp.U_INFO.getPtStreet().equals("")) {	
      			System.out.println("设置"+myapp.U_INFO.getPtCity());
      	        System.out.println("设置"+myapp.U_INFO.getPtDis());
      	        System.out.println("设置"+myapp.U_INFO.getPtStreet());
      		}
      		else {
      			case_et_ptCity.setText(myapp.U_INFO.getPtCity());
      			System.out.println("设置"+myapp.U_INFO.getPtCity());
      			case_et_ptDis.setText(myapp.U_INFO.getPtDis());
      			case_tv_ptStreet.setText(myapp.U_INFO.getPtStreet());
      		}
	}
	//日期点击事件
	private class DateOnClicker implements OnClickListener{
		public void onClick(View arg0) {
			new DatePickerDialog(CaseActivity.this,
					new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker view, int year,
						int monthOfYear,int dayOfMonth) {
					if(monthOfYear<9){
						month2bytes=0+Integer.toString(
								monthOfYear+1);}
					else {
						month2bytes=Integer.toString(
								monthOfYear+1);
						}
					if (dayOfMonth<10) {
						day2bytes=0+Integer.toString(dayOfMonth);}
					else {
						day2bytes=Integer.toString(dayOfMonth);
						}
					c_et_date.setText(year+"-"+month2bytes+"-"+
						day2bytes);
					}
				}, c_year, c_month-1, c_day).show();
			} 
		}
	//时间点击事件
		private class TimeOnClicker implements OnClickListener  
	    {  
	       public void onClick(View arg0) {
	    	   new TimePickerDialog(CaseActivity.this, 
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
							
							c_et_time.setText(hour2bytes+":"+minute2bytes);
						}
					}, c_hour, c_minute, true).show();
	    	   
	       }
	    }
		
	//提交点击事件
	private class SubmitOnClick implements OnClickListener  
    {  
        public void onClick(View arg0) { 

        	//时间及日期
        	showdate=c_et_date.getText().toString();      //日期字符串  	形式2015-08-09
        	showyear=showdate.substring(0,4);             //年字符串  形式2015
        	showmonth=showdate.substring(5,7);            //月字符串  形式08
        	showday=showdate.substring(8,10);             //日字符串  形式09
        	
        	showtime=c_et_time.getText().toString();      //时间字符串  形式08:10
        	showhour=showtime.substring(0,2);             //小时字符串  形式08
        	showminute=showtime.substring(3,5);           //分钟字符串  形式10
        	
        	if(showdate==null||showdate.length()<=0){
        		c_et_date.requestFocus();
        		c_et_date.setError("请输入日期！");  
        		return; 
        	}
        	if(showtime==null||showtime.length()<=0){
        		c_et_time.requestFocus();
        		c_et_time.setError("请输入时间！"); 
        		return; 
        	}
        	
        	//地点
        	ptCity=case_et_ptCity.getText().toString();         //城市
        	ptDis=case_et_ptDis.getText().toString();           //区
        	ptStreet=case_tv_ptStreet.getText().toString();     //街道
        	
        	
        	
        	
        	if(ptCity==null||ptCity.length()<=0){
        		case_et_ptCity.requestFocus();
        		case_et_ptCity.setError("请输入城市！");
        		return; 
        	}
        	if(ptDis==null||ptDis.length()<=0){
        		case_et_ptDis.requestFocus();
        		case_et_ptDis.setError("请输入区！");
        		return; 
        	}
        	
        	if(ptStreet==null||ptStreet.length()<=0){
        		case_tv_ptStreet.requestFocus();
        		case_tv_ptStreet.setError("请输入街道！");  
        		return; 
        	}
        	
        	myapp.U_INFO.setPtCity(ptCity);
        	myapp.U_INFO.setPtStreet(ptStreet);
        	myapp.U_INFO.setPtDis(ptDis);
        	
        	
        	//电话
        	showtel=case_et_tel.getText().toString();     //电话
        	
        	//描述
        	showdescribe=case_et_describe.getText().toString();   //描述*/
        	
        	if(showtel==null||showtel.length()<=0){
        		case_et_tel.requestFocus();
        		case_et_tel.setError("请输入电话！");  
        		return;
        	}
        	
        	//类型
        	showTypeitem=case_sp_type.getSelectedItem().toString();


        	dialog=new ProgressDialog(CaseActivity.this);  
            dialog.setTitle("提交");  
            dialog.setMessage("正在上传，请稍后！");  
            //开始登陆
            dialog.show();
         
            for(int i=0;i<myapp.imagePathList.size();i++){
    			File file = new File(myapp.imagePathList.get(i));
    			System.out.println(myapp.imagePathList.get(i).toString());
    			files.add(file);
    		}
            System.out.println(files.size());
        	//报案
        	        	
			System.out.print("================"+location);
			
            String URL=myapp.get_URL();
            String time=showyear+showmonth+showday+showhour+showminute;
            
            String showTypeitem;//提交界面时类型Spinner选中的item
            showTypeitem=case_sp_type.getSelectedItem().toString();
            String type=showTypeitem;
            String phone=showtel;
            String paths="";
            int num=myapp.imagePathList.size();
            for(int i=0;i<num;i++){
            	paths=paths+files.get(i).getName()+"-";
            }
            String description=showdescribe;
            String city=ptCity;
            String district=ptDis;
            
            mSearch.geocode(new GeoCodeOption().
					city(ptCity)
					.address(ptStreet));
            
            /*Toast.makeText(CaseActivity.this, pt,
    				Toast.LENGTH_LONG).show();*/
            //String location="";
            //location=pt;////经纬度
            /*Toast.makeText(CaseActivity.this, location,
    				Toast.LENGTH_LONG).show();*/
            CaseToServer.SubmitCaseToServer
            (URL,time,location, type, phone, description,paths,city, district,handler);
        }   
    }
	//添加图片点击事件
	private class PictureOnClick implements OnClickListener{
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
						public void onClick(DialogInterface dialog,int which) {
							if(which==PHOTOTAKE_GRAPH){//拍照
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
	                        	 //启动activity   
	                        	startActivityForResult(intent, PHOTOTAKE_GRAPH);
	                            dialog.dismiss();
	                            }
							else{//相册
	                        	Intent intent = new Intent(CaseActivity.this, NewTestPicActivity.class);
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
	    	System.out.println("拍照"+requestCode);
			switch (requestCode) {
			case PHOTOTAKE_GRAPH:
				System.out.println("拍照"+data);
				System.out.println("拍照"+data.getData().toString());
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
	            System.out.println("拍照路径"+path);
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
			Toast.makeText(CaseActivity.this, "抱歉，未能找到结果",
					Toast.LENGTH_LONG).show();
			return;
			}		
		location=result.getLocation().latitude+"-"+
			result.getLocation().longitude;	
		/*Toast.makeText(CaseActivity.this, pt,
				Toast.LENGTH_LONG).show();*/
		myapp.U_INFO.setCasePt(location);
		//System.out.print("==========="+pt);
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