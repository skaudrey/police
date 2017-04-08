package com.policecom.activity;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption.DrivingPolicy;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class SheriffCarMapActivity extends Activity  
				implements OnGetRoutePlanResultListener{
	
	RouteLine route = null;
    OverlayManager routeOverlay = null;
    
    List<LatLng> points=new ArrayList<LatLng>();
    // 地图
    MapView mMapView = null;    
    BaiduMap mBaidumap = null;
    //搜索相关
    RoutePlanSearch mSearch = null;// 搜索模块，也可去掉地图模块独立使用   
    //搜索policy--以时间最短为默认搜索模式
    DrivingPolicy policy=DrivingPolicy.ECAR_TIME_FIRST;
	
	MyApplication myapp;
	
	//
	Button btDetail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_sheriffcarmap);	
		
		myapp=(MyApplication)getApplication();
		//UI
		btDetail=(Button)findViewById(R.id.spm_bt_details);
		//初始化地图
        mMapView = (MapView) findViewById(R.id.map);
        mBaidumap = mMapView.getMap();
        
       // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        
        System.out.println("单个警车————————————");
        System.out.println("起点"+myapp.s_pcPath.getSPt());
        System.out.println("终点"+myapp.s_pcPath.getEPt());
        System.out.println("单个警车起点"+myapp.s_pcPath.getPathStr());
        
        search(myapp.s_pcPath.getSPt(), 
				myapp.s_pcPath.getEPt(), 
						mSearch);
        
        btDetail.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent=new Intent(SheriffCarMapActivity.this,
						SheriffCarInfoActivity.class);
	        	
				startActivity(intent);
				finish();
			}
		});
	}
	
	@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onGetWalkingRouteResult(WalkingRouteResult result) {}

    
    public void onGetTransitRouteResult(TransitRouteResult result) {}

    public void onGetDrivingRouteResult(DrivingRouteResult result) {
    	System.out.println("Driving");
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(SheriffCarMapActivity.this,
            		"抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            route = result.getRouteLines().get(0);            
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
            routeOverlay = overlay;
            mBaidumap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            System.out.println("pathlist------------");
            
            OverlayOptions ooPolyline = new PolylineOptions().
            		width(10).color(0xFFFF00FF).points(
            				myapp.s_pcPath.getPathList());
            mBaidumap.addOverlay(ooPolyline);
            
        }
    }
    
    //定制RouteOverly
    private class MyDrivingRouteOverlay extends 
    	DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        
        public BitmapDescriptor getStartMarker() {
           
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            
            return null;
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        mMapView.onDestroy();
        super.onDestroy();
    }
    
     public void search(LatLng stLatLng,LatLng enLatLng,
    		RoutePlanSearch mSearch) {
        //获取起终点节点
        PlanNode stNode = PlanNode.withLocation(stLatLng);
        PlanNode enNode = PlanNode.withLocation(enLatLng);
        //发起搜索
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
    }   
}
