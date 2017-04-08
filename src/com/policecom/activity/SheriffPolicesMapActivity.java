package com.policecom.activity;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.model.LatLng;
import com.policecom.toserver.SheriffToServer;
import com.policecom.view.LocView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SheriffPolicesMapActivity extends Activity{
	
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private InfoWindow mInfoWindow;
	
	private List<Marker> markers=new ArrayList<Marker>();
	private List<String> pcInfo=new ArrayList<String>();
	
	BitmapDescriptor bd = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);
	
	MyApplication myapp;
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			System.out.println("ghjkk");
			if(msg.what!=200){
				Toast.makeText(SheriffPolicesMapActivity.this, "联网失败！请检查网络", Toast.LENGTH_SHORT).show();
			}
			else {
				if(msg.obj.equals("0")){
					Toast.makeText(SheriffPolicesMapActivity.this, "最近无案件！", Toast.LENGTH_SHORT).show();
					return;
				}else{
					System.out.println("结果"+msg.obj);
					String string=msg.obj.toString();
					String sts[]=string.split("/");
					myapp.s_pcPath.setDutyId(sts[0]);
					myapp.s_pcPath.setPathStr(sts[1]);
					
					String stss[]=sts[1].split(";");
					System.out.println(stss[0]);
					myapp.s_pcPath.setSPt(sts[2]);
					myapp.s_pcPath.setEPt(sts[3]);
					myapp.s_pcPath.setCurPt(sts[4]);

					Intent intent = new Intent(SheriffPolicesMapActivity.this,
							SheriffPoliceMapActivity.class);
					startActivity(intent);
					finish();
				}
			}
			super.handleMessage(msg);
		}	
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_sheriffpolicesmap);	
		
		myapp=(MyApplication)getApplication();
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		/*mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(
				14.0f);
		mBaiduMap.setMapStatus(msu);
		initOverlay();
		mBaiduMap.setOnMarkerClickListener(new 
				OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				TextView tvInfo=new TextView(
						getApplicationContext());
				OnInfoWindowClickListener listener = null;
				if (markers.contains(marker)){
					final int i=markers.indexOf(marker);
					
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(
							tvInfo, ll, -47);
					LocView view=new LocView(
							getApplicationContext());
					
					view.tvInfo.setText(pcInfo.get(i));
					view.btComplex.setOnClickListener(
							new OnClickListener() {
								public void onClick(View v) {
									//进入详细信息页面
									int j=i-1;
									System.out.println("click"+j);
									SheriffToServer.PathToServer(myapp.get_URL(), 
											myapp.addressInfos.get(j).get_USER_ID(),
											handler);
									}
								});
					mInfoWindow =new InfoWindow(view, ll, -47);
					mBaiduMap.showInfoWindow(mInfoWindow);
					}
				return true;
				}
			});*/
		
		
		System.out.println("ggvwryey");
		
		initview();
//		//连接服务器
//		SheriffToServer.AddressToServer(myapp.get_URL(), sphandler);
	}
	void initview(){
		mMapView = new MapView(SheriffPolicesMapActivity.this,
				new BaiduMapOptions().mapStatus(
						new MapStatus.Builder().
						target(new LatLng(
								Double.parseDouble(myapp.addressInfos.
										get(0).get_USER_LAT())
								, Double.parseDouble(myapp.addressInfos
										.get(0).get_USER_LON()))).
						build()));
		System.out.println("设置地图中心点完成");
		
		setContentView(mMapView);
		
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(
				14.0f);
		mBaiduMap.setMapStatus(msu);
		initOverlay();
		mBaiduMap.setOnMarkerClickListener(new 
				OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				TextView tvInfo=new TextView(
						getApplicationContext());
				OnInfoWindowClickListener listener = null;
				if (markers.contains(marker)){
					final int i=markers.indexOf(marker);
					System.out.println("marker"+i);
					LatLng ll = marker.getPosition();
					mInfoWindow = new InfoWindow(
							tvInfo, ll, -47);
					LocView view=new LocView(
							getApplicationContext());
					
					view.tvInfo.setText(pcInfo.get(i));
					view.btComplex.setOnClickListener(
							new OnClickListener() {
								public void onClick(View v) {
									//进入详细信息页面
									System.out.println(i+"click"+myapp.addressInfos.get(i).get_USER_ID());
									myapp.S_PoliceNum=myapp.addressInfos.get(i).get_USER_ID();
									SheriffToServer.PolicePathToServer(
											myapp.get_URL(), myapp.addressInfos.get(i).get_USER_ID(), handler);
									}
								});
					mInfoWindow =new InfoWindow(view, ll, -47);
					mBaiduMap.showInfoWindow(mInfoWindow);
					}
				return true;
				}
			});
	}
	public void initOverlay() {	
		System.out.println("初始化地图数据");
		int num=myapp.addressInfos.size();
		List<LatLng> locs=new ArrayList<LatLng>();
		for(int i=0;i<num;i++){
			locs.add(new LatLng(Double.parseDouble(myapp.addressInfos.get(i).get_USER_LAT())
					, Double.parseDouble(myapp.addressInfos.get(i).get_USER_LON())));
						
		}
		
		for (int i = 0; i < locs.size(); i++) {
			markers.add((Marker)(mBaiduMap.addOverlay(
					new MarkerOptions().position(locs.get(i)).
					icon(bd).zIndex(i+5))));
			}
		//设置简略信息值
		for(int i=0;i<num;i++){
			pcInfo.add("警员："+myapp.addressInfos.get(i).get_USER_NAME());
		}	
		
	}

	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	protected void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();		
		bd.recycle();
	}
}
