package com.policecom.activity;

import java.util.ArrayList;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.CommonParams.NL_Net_Mode;
import com.baidu.navisdk.CommonParams.Const.ModelName;
import com.baidu.navisdk.comapi.mapcontrol.BNMapController;
import com.baidu.navisdk.comapi.mapcontrol.MapParams.Const.LayerMode;
import com.baidu.navisdk.comapi.routeguide.RouteGuideParams.RGLocationMode;
import com.baidu.navisdk.comapi.routeplan.BNRoutePlaner;
import com.baidu.navisdk.comapi.routeplan.IRouteResultObserver;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.baidu.navisdk.comapi.setting.SettingParams;
import com.baidu.navisdk.model.NaviDataEngine;
import com.baidu.navisdk.model.RoutePlanModel;
import com.baidu.navisdk.model.datastruct.RoutePlanNode;
import com.baidu.navisdk.ui.routeguide.BNavConfig;
import com.baidu.navisdk.ui.routeguide.BNavigator;
import com.baidu.navisdk.ui.widget.RoutePlanObserver;
import com.baidu.navisdk.util.common.PreferenceHelper;
import com.baidu.navisdk.util.common.ScreenUtil;
import com.baidu.nplatform.comapi.map.MapGLSurfaceView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class PoliceCaseMapActivity extends Activity implements
	OnGetGeoCoderResultListener{
	
	MyApplication myapp;
	
	//地图相关
	private RoutePlanModel mRoutePlanModel = null;
	private MapGLSurfaceView mMapView = null;
	//起终点经纬度
	private int sX, sY, eX, eY;
	
	//当前
	//private LatLng curPt;
	//地理编码搜索
	GeoCoder mSearch1 = null;
	GeoCoder mSearch2=null;
	//UI
	private EditText startCityEditText ;
	private EditText startStrEditText ;
	private EditText endCityEditText ;
	private EditText endStrEditText ;
	
	//起始点城市、街区
	String stCity,stStr,enCity,enStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_policecasemap);	
		
		myapp=(MyApplication)getApplication();
		
		/*curPt=myapp.getCurPt();*/
		System.out.println("开始导航！！！！！！！！");
		sX=(int)(myapp.pDuty.getCurPt().latitude * 1e5);
		sY=(int)(myapp.pDuty.getCurPt().longitude * 1e5);
		eX=(int)(myapp.pDuty.getEPt().latitude * 1e5);
		eY=(int)(myapp.pDuty.getEPt().longitude * 1e5);
		
		System.out.println(sX);
		System.out.println(sY);
		System.out.println(eX);
		System.out.println(eY);
		//搜索引擎初始化
		mSearch1 = GeoCoder.newInstance();
		//注册搜索监听
		mSearch1.setOnGetGeoCodeResultListener(this);
		
		mSearch2=GeoCoder.newInstance();
		
		mSearch2.setOnGetGeoCodeResultListener(new 
				OnGetGeoCoderResultListener() {
			public void onGetReverseGeoCodeResult(
					ReverseGeoCodeResult result) {
				enCity=result.getAddressDetail().city;
				enStr=result.getAddressDetail().street+
						result.getAddressDetail().streetNumber;
				
				endCityEditText.setText(enCity);
				endStrEditText.setText(enStr);
				
			}
			public void onGetGeoCodeResult(
					GeoCodeResult result) {
				/*if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(PoliceCaseMapActivity.this, "抱歉，未能找到结果",
							Toast.LENGTH_LONG).show();
					return;
					}
				else{
					Toast.makeText(PoliceCaseMapActivity.this, 
							"黄鹤楼地理编码",
							Toast.LENGTH_LONG).show();
				}*/
				eX =(int)(result.getLocation().latitude * 1e5);
				eY =(int) (result.getLocation().longitude * 1e5);
				}});
		startCityEditText = (EditText) findViewById(
				R.id.et_start_city);
		startStrEditText = (EditText) findViewById(
				R.id.et_start_street);
		endCityEditText = (EditText) findViewById(
				R.id.et_end_city);
		endStrEditText = (EditText) findViewById(
				R.id.et_end_street);
		
		initView();
		
		findViewById(R.id.online_calc_btn).
			setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					startCalcRoute(NL_Net_Mode.NL_Net_Mode_OnLine);
					}
				});
		
		findViewById(R.id.real_btn).setOnClickListener(
				new OnClickListener() {
					
					public void onClick(View arg0) {
						PreferenceHelper.getInstance(
								getApplicationContext())
								.putBoolean(SettingParams.Key.SP_TRACK_LOCATE_GUIDE,
										false);
						startNavi();
						}
					});
		
		findViewById(R.id.ll_start_btn).setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						/*if(myapp.getCurPt().equals(
								myapp.pDuty.getEPt())){*/
							myapp.setStart(false);
							Intent intent=new Intent(
									PoliceCaseMapActivity.this,
									PoliceInvestActivity.class);
							startActivity(intent);
							finish();
						/*}
						else {
							Toast.makeText(PoliceCaseMapActivity.this, 
									"您尚未到达案发现场，请小心驾驶",
										Toast.LENGTH_LONG).show();
					}*/
											
					}
				});
		
		}
	
	@Override
	public void onDestroy() {
		mSearch1.destroy();
		mSearch2.destroy();
		super.onDestroy();		
	}

	@Override
	public void onPause() {
		super.onPause();
		BNRoutePlaner.getInstance().setRouteResultObserver(null);
		((ViewGroup) (findViewById(R.id.mapview_layout))).removeAllViews();
		BNMapController.getInstance().onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		initMapView();
		((ViewGroup) (findViewById(R.id.mapview_layout))).addView(mMapView);
		BNMapController.getInstance().onResume();
	}

    private void initMapView() {
        if (Build.VERSION.SDK_INT < 14) {
            BaiduNaviManager.getInstance().destroyNMapView();
        }
        
        mMapView = BaiduNaviManager.getInstance().createNMapView(this);
        BNMapController.getInstance().setLevel(14);
        BNMapController.getInstance().setLayerMode(
                LayerMode.MAP_LAYER_MODE_BROWSE_MAP);
        updateCompassPosition();
        
        BNMapController.getInstance().locateWithAnimation(
                (int) (myapp.getCurPt().latitude * 1e5), 
                (int) (myapp.getCurPt().longitude * 1e5));
    }
	
	/**
	 * 更新指南针位置
	 */
	private void updateCompassPosition(){
		int screenW = this.getResources().getDisplayMetrics().widthPixels;
		BNMapController.getInstance().resetCompassPosition(
				screenW - ScreenUtil.dip2px(this, 30),
					ScreenUtil.dip2px(this, 126), -1);
	}

	public void setSptEpt() {
		try {
			if ((startCityEditText.getText().toString()).
					equals(stCity)&&
					(startStrEditText.getText().toString()).
					equals(stStr)&&
					(endCityEditText.getText().toString()).
					equals(enCity)&&
					(endStrEditText.getText().toString()).
					equals(enStr)) {
				return;
			}
			/*stCity=startCityEditText.getText().toString();
			stStr=startStrEditText.getText().toString();
			enCity=endCityEditText.getText().toString();
			enStr=endStrEditText.getText().toString();*/
			
			mSearch1.geocode(new GeoCodeOption().city(
					startCityEditText.getText().toString())
					.address(
					startStrEditText.getText().toString()));
			mSearch2.geocode(new GeoCodeOption().city(
					endCityEditText.getText().toString())
					.address(endStrEditText.getText().toString()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	private void startCalcRoute(int netmode) {
		//获取输入的起终点
		setSptEpt();
		
		//起点
		RoutePlanNode startNode = new RoutePlanNode(sX, sY,
				RoutePlanNode.FROM_MAP_POINT, 
				startStrEditText.getText().toString(),
				startStrEditText.getText().toString());
		//终点
		RoutePlanNode endNode = new RoutePlanNode(eX, eY,
				RoutePlanNode.FROM_MAP_POINT, 
				endStrEditText.getText().toString(), 
				endStrEditText.getText().toString());
		//将起终点添加到nodeList
		ArrayList<RoutePlanNode> nodeList = new ArrayList<RoutePlanNode>(2);
		nodeList.add(startNode);
		nodeList.add(endNode);
		BNRoutePlaner.getInstance().setObserver(new RoutePlanObserver(this, null));
		//设置算路方式
		BNRoutePlaner.getInstance().setCalcMode(NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME);
		// 设置算路结果回调
		BNRoutePlaner.getInstance().setRouteResultObserver(
				mRouteResultObserver);
		// 设置起终点并算路
		boolean ret = BNRoutePlaner.getInstance().setPointsToCalcRoute(
				nodeList,NL_Net_Mode.NL_Net_Mode_OnLine);
		if(!ret){
			Toast.makeText(this, "规划失败", Toast.LENGTH_SHORT).show();
		}
	}

	private void startNavi() {
		if (mRoutePlanModel == null) {
			Toast.makeText(this, "请先算路！", Toast.LENGTH_LONG).show();
			return;
		}
		// 获取路线规划结果起点
		RoutePlanNode startNode = mRoutePlanModel.getStartNode();
		// 获取路线规划结果终点
		RoutePlanNode endNode = mRoutePlanModel.getEndNode();
		if (null == startNode || null == endNode) {
			return;
		}
		// 获取路线规划算路模式
		int calcMode = BNRoutePlaner.getInstance().getCalcMode();
		Bundle bundle = new Bundle();
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_VIEW_MODE,
				BNavigator.CONFIG_VIEW_MODE_INFLATE_MAP);
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_CALCROUTE_DONE,
				BNavigator.CONFIG_CLACROUTE_DONE);
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_START_X,
				startNode.getLongitudeE6());
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_START_Y,
				startNode.getLatitudeE6());
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_END_X, endNode.getLongitudeE6());
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_END_Y, endNode.getLatitudeE6());
		bundle.putString(BNavConfig.KEY_ROUTEGUIDE_START_NAME,
				mRoutePlanModel.getStartName(this, false));
		bundle.putString(BNavConfig.KEY_ROUTEGUIDE_END_NAME,
				mRoutePlanModel.getEndName(this, false));
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_CALCROUTE_MODE, calcMode);
		
		// GPS 导航
		bundle.putInt(BNavConfig.KEY_ROUTEGUIDE_LOCATE_MODE,
					RGLocationMode.NE_Locate_Mode_GPS);
		
		
		Intent intent = new Intent(PoliceCaseMapActivity.this,
				BNavigatorActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	private IRouteResultObserver mRouteResultObserver = 
			new IRouteResultObserver() {

		public void onRoutePlanYawingSuccess() {}

		public void onRoutePlanYawingFail() {}

		public void onRoutePlanSuccess() {
			BNMapController.getInstance().setLayerMode(
					LayerMode.MAP_LAYER_MODE_ROUTE_DETAIL);
			mRoutePlanModel = (RoutePlanModel) NaviDataEngine.getInstance()
					.getModel(ModelName.ROUTE_PLAN);
		}

		public void onRoutePlanFail() {}

		public void onRoutePlanCanceled() {}

		public void onRoutePlanStart() {}

	};

	public void initView() {
		mSearch1.reverseGeoCode(new ReverseGeoCodeOption()
		.location(myapp.getCurPt()));
		mSearch2.reverseGeoCode(new ReverseGeoCodeOption()
		.location(myapp.pDuty.getEPt()));
	}

	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(PoliceCaseMapActivity.this, "抱歉，未能找到结果",
					Toast.LENGTH_LONG).show();
			return;
			}
		sX = (int) (result.getLocation().latitude * 1e5);
		sY = (int) (result.getLocation().longitude * 1e5);			
	}

	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(PoliceCaseMapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		stCity=result.getAddressDetail().city;
		stStr=result.getAddressDetail().street+
				result.getAddressDetail().streetNumber;
		
		startCityEditText.setText(stCity);
		startStrEditText.setText(stStr);
	}
}