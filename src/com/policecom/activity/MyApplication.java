package com.policecom.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.policecom.bean.AddressInfo;
import com.policecom.bean.FinalCaseInfo;
import com.policecom.bean.PDuty;
import com.policecom.bean.PoliceInfo;
import com.policecom.bean.SheriffInfo;
import com.policecom.bean.UpCaseInfo;
import com.policecom.util.CurTimeUtil;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

public class MyApplication extends Application {
	//public BMapManager mapManager = null;
	public String URL="http://192.168.1.107:8080/PoliceServer/";
	public String get_URL(){
		return URL;
	}
	//用户：0
	//警员：1
	//警长：2
	public int USER_TYPE;
	public void set_USER_TYPE(int i){
		USER_TYPE=i;
	}
	public int get_USER_TYPE(){
		return USER_TYPE;
	}
	//警员基本信息
	public PoliceInfo P_INFO=new PoliceInfo();
	public FinalCaseInfo finalCaseInfo=new FinalCaseInfo();
	//警长基本信息
	public SheriffInfo S_INFO=new SheriffInfo();
	public ArrayList<FinalCaseInfo>finalCaseInfos=
			new ArrayList<FinalCaseInfo>();
	//用户上传案件信息
	public UpCaseInfo U_INFO=new UpCaseInfo();
	//警长查看的所有警员信息
	public List<PDuty> S_Police=new ArrayList<PDuty>();
	public String S_PoliceNum;
	public String S_CarNum;
	//登陆成功与否
	public int LOGIN_ID;
	public void set_LOGIN_ID(int i){
		LOGIN_ID=i;
	}
	public int get_LOGIN_ID(){
		return LOGIN_ID;
	}
	//图片路径 供上传使用
	public ArrayList<String>imagePathList=new ArrayList<String>();
	
	private File cacheDir;
	private ImageLoaderConfiguration config;

	public ArrayList<String>availCars=new ArrayList<String>();
	//所有警察位置
	public ArrayList<AddressInfo>addressInfos=new ArrayList<AddressInfo>();
	//所有警车位置
	public ArrayList<AddressInfo>caraddressInfos=new ArrayList<AddressInfo>();
	//地图相关
	//定位相关
	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	
	public Vibrator mVibrator;
	//时间相关
	CurTimeUtil aCurTimeUtil=new CurTimeUtil();
	
	public PDuty pDuty=new PDuty();//任务轨迹
	public PDuty s_pcPath=new PDuty();
	//public PoliceInfo policeInfo=new PoliceInfo();
	private Boolean start=false;//警员开始任务标志
	private LatLng curPt;//user当前位置
	public LatLng getCurPt(){
		return this.curPt;
		}
	//返回设置后的start值
	public void setStart(boolean start){
		this.start=start;
		}
	public boolean getStart(){
		return this.start;
		}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//SDK初始化
		SDKInitializer.initialize(this);
		
		cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), "kmq/ImgCache");
		config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.memoryCacheExtraOptions(320, 480)
				// max width, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				// .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 *
				// 1024)) // You can pass your own memory cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024).diskCacheSize(50 * 1024 * 1024).tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCacheFileCount(100)
				// 缓存的文件数量
				.diskCache(new UnlimitedDiscCache(cacheDir))
				// 自定义缓存路径
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout
				// 启用软引用缓存
				.memoryCache(new WeakMemoryCache())
				// 是否打印日志用于检查错误
				.writeDebugLogs().build();// 开始构建
		ImageLoader.getInstance().init(config);
		
		//地图相关
		mLocationClient = new LocationClient(this.
				getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(
				mMyLocationListener);
		mVibrator =(Vibrator)getApplicationContext().
				getSystemService(Service.VIBRATOR_SERVICE);
		InitLocation();
		initPInfo();//读取police表信息
		InitDuty();
		mLocationClient.start();
	}
	
	//定位监听初始化
	private void InitLocation(){
		LocationClientOption option = 
				new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//定位结果:百度经纬度
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		mLocationClient.setLocOption(option);
		}
	//初始化policeInfo
	public void initPInfo() {
		//测试
//		P_INFO.set_USER_NUMBER("19820102");
		}
	//初始化pDuty
	public void InitDuty(){
		//警员id
		/*pDuty.setPNum(policeInfo.get_USER_NUMBER());
		  try {
		  		String date=aCurTimeUtil.getDate();
				//添加cases
				pDuty.setDutyId(Integer.toString(
						aGeoDao.addWork(pDuty.getPNum(),
								0,
								date)));
				} catch (SQLException e) {
					e.printStackTrace();
					}*/
		//police测试
//		pDuty.setPNum(P_INFO.get_USER_NUMBER());
//		String date=aCurTimeUtil.getDate();
//		//添加cases
//		pDuty.setDutyId(Integer.toString(1));
		
		//sheriff测试
//		pDuty.setDutyId(Integer.toString(1));
//		pDuty.setPNum("19820103");
//		pDuty.setPathStr("30.541251849209,114.38382876351;"+
//				"30.541071681739,114.38384897538;" +
//				"30.540791515598,114.38386936692;" +
//				"30.540731640963,114.38386945675;" +
//				"30.54066126878,114.38387951777;" +
//				"30.539653035796,114.38378016518;" +
//				"30.538985151438,114.38365036003;" +
//				"30.538915400062,114.38364047867;" +
//				"30.538865944206,114.38361047527;" +
//				"30.538716876639,114.38355046846;" +
//				"30.538577840013,114.38349046165;" +
//				"30.53840878738,114.38343045485;" +
//				"30.538239889973,114.38336047685;" +
//				"30.538080945733,114.38329049885;" +
//				"30.537951783904,114.38324055306;" +
//				"30.537822621902,114.38319060727;" +
//				"30.537703413229,114.38314066149;" +
//				"30.537574328655,114.38308065468;" +
//				"30.537435134854,114.38303070889;" +
//				"30.537296096375,114.38297070209;" +
//				"30.537176887049,114.3829207563");
//		pDuty.setCurPt("30.532207,114.3666");//当前：武大
//		pDuty.setSPt("30.541251849209,114.38382876351");//起点：武大
//		pDuty.setEPt("30.511403,114.406424");//终点：光谷
		}
	
	//初始化pDuty
		public void InitSheriffpDuty(String policeLocs){
			//设置
			/*pDuty.setPNum(policeInfo.get_USER_NUMBER());
			  try {
			  		String date=aCurTimeUtil.getDate();
					//添加cases
					pDuty.setDutyId(Integer.toString(
							aGeoDao.addWork(pDuty.getPNum(),
									0,
									date)));
					} catch (SQLException e) {
						e.printStackTrace();
						}*/
			//policeLocs测试
			String[] aa=policeLocs.split(";");
			
			int length=aa.length;
			for (int i = 0; i <length; i++) {
				
			}
			
	}
	
	/**
	 * 实现实位回调监听
	 * BDLocation location 为得到的定位信息
	 */
	public class MyLocationListener implements BDLocationListener {
		public void onReceiveLocation(BDLocation location) {
			//软件用户当前位置
			curPt=new LatLng(location.getLatitude(),
						location.getLongitude());
			//警员位置记录
			if(USER_TYPE==1){
				pDuty.setCurPt(location.getLatitude()+
						"-"+location.getLongitude());
				pDuty.setSPt(location.getLatitude()+
						"-"+location.getLongitude());}
			}
		
		public void onReceivePoi(BDLocation arg0) {}
		}
}
