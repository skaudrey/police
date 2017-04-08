package com.policecom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.policecom.view.SetPtView;

public class PoliceCasePickPtActivity extends Activity implements
	OnGetGeoCoderResultListener{

	@SuppressWarnings("unused")
	private static final String LTAG = CaseMapActivity.class.getSimpleName();
	
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	MapStatus mMapStatus;
	private InfoWindow mInfoWindow;
	
	//UI
	Button btBack;
	
	//地理编码搜索
	GeoCoder mSearch = null;
	
	//记录地址
	String ptInfo;
	String ptCity;
	String ptStreet;
	String ptDis;
	
	LatLng pt=null;			  //设置案件点经纬度
		
	//用户拾取的点
	private LatLng pickedP=null;
	
	MyApplication myapp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_casemap);
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		btBack=(Button)findViewById(R.id.cm_btn_back);
		
		myapp=(MyApplication)getApplication();
		ptCity=myapp.finalCaseInfo.get_FINALCASE_CITY();
		ptStreet=myapp.finalCaseInfo.get_FINALCASE_Street();
		ptDis=myapp.finalCaseInfo.get_FINALCASE_DISTRICT();
		
		//btBack注册监听
		btBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				myapp.finalCaseInfo.set_FINALCASE_LOCATION(
						pt.latitude+"-"+
							pt.longitude);
				myapp.finalCaseInfo.set_FINALCASE_CITY(ptCity);
				myapp.finalCaseInfo.set_FINALCASE_DISTRICT(ptDis);
				myapp.finalCaseInfo.set_FINALCASE_Street(ptStreet);
				
				Intent intent=new Intent(PoliceCasePickPtActivity.this, PoliceInvestActivity.class);
				startActivity(intent);	
				finish();
			}
		});
		
		
		//搜索引擎初始化
		mSearch = GeoCoder.newInstance();
		//注册搜索监听
		mSearch.setOnGetGeoCodeResultListener(this);
		
		if(ptCity.equals("")||ptStreet.equals("")) {	
			//设置地图以current为中心点
			pt=myapp.getCurPt();
			mMapStatus = new MapStatus.Builder().target(pt).
					zoom(12).build();
			
			} else {
				// Geo搜索
				mSearch.geocode(new GeoCodeOption().
						city(ptCity)
						.address(ptStreet));
				//设置地图以目标点为中心点
				mMapStatus = new MapStatus.Builder().target(pt).
						zoom(12).build();
				}
		
		//地图
		MapStatusUpdate mMapStatusUpdate = 
				MapStatusUpdateFactory.newMapStatus(mMapStatus);
		//改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
		//注册地图单击监听
		mBaiduMap.setOnMapClickListener(new OnMapClickListener(){
			
			public void onMapClick(LatLng point){
				
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(point));
				pickedP=point;
				
				SetPtView view=new SetPtView(
						getApplicationContext());
				view.tvPtInfo.setText(ptInfo);
				view.btSet.setOnClickListener(
						new OnClickListener() {
							public void onClick(View v) {
								pt=pickedP;
								mBaiduMap.hideInfoWindow();
								/*myapp.finalCaseInfo.set_FINALCASE_LOCATION(
										pt.latitude+"-"+
											pt.longitude);
								myapp.finalCaseInfo.set_FINALCASE_CITY(ptCity);
								myapp.finalCaseInfo.set_FINALCASE_DISTRICT(ptDis);
								myapp.finalCaseInfo.set_FINALCASE_Street(ptStreet);*/
								
								/*Intent intent =new Intent(PoliceCasePickPtActivity.this, PoliceInvestActivity.class);
								startActivity(intent);*/					
				}});
				mInfoWindow =new InfoWindow(view, pickedP, -20);
				mBaiduMap.showInfoWindow(mInfoWindow);
				}
			public boolean onMapPoiClick(MapPoi poi) {
				return false;}});
		}
	
	
	//监听--地理编码
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(PoliceCasePickPtActivity.this, 
					"抱歉，编码未能找到结果",
					Toast.LENGTH_LONG).show();
			return;
			}
		mBaiduMap.clear();
		pt=new LatLng(result.getLocation().latitude, 
				result.getLocation().longitude);		
		mBaiduMap.addOverlay(new MarkerOptions().position(
				result.getLocation()).icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_gcoding)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(
				result.getLocation()));
		}
	//监听--反向地理编码
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(PoliceCasePickPtActivity.this, "抱歉，反编码未能找到结果",
					Toast.LENGTH_LONG).show();
			return;
			}
		ptInfo=result.getAddressDetail().district+
				result.getAddressDetail().street+
				result.getAddressDetail().streetNumber;
		ptCity=result.getAddressDetail().city;
		if (ptCity.contains("市")) {
			ptCity=ptCity.substring(0, ptCity.length()-1);
			System.out.println("ptCity"+ptCity);
		}
		ptDis=result.getAddressDetail().district;
		if (ptDis.contains("区")) {
			ptDis.replace("区", "");
			ptDis=ptDis.substring(0, ptDis.length()-1);
			System.out.println("ptDis"+ptDis);
		}
		ptStreet=result.getAddressDetail().street+
				result.getAddressDetail().streetNumber;
		}
	
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	protected void onDestroy() {
		super.onDestroy();
		mSearch.destroy();
		mMapView.onDestroy();
	}
}
