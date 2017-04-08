package com.policecom.bean;

import android.R.integer;

import com.policecom.activity.MyApplication;

public class AddressInfo {
	    //警员ID及基本信息
		public String USER_ID;
		public void set_USER_ID(String i){
			USER_ID=i;
		}
		public String get_USER_ID(){
			return USER_ID;
		}
		public String USER_NAME;
		public void set_USER_NAME(String s){
			USER_NAME=s;
		}
		public String get_USER_NAME(){
			return USER_NAME;
		}
		public String USER_LON;
		public void set_USER_LON(String s){
			USER_LON=s;
		}
		public String get_USER_LON(){
			return USER_LON;
		}
		public String USER_LAT;
		public void set_USER_LAT(String s){
			USER_LAT=s;
		}
		public String get_USER_LAT(){
			return USER_LAT;
		}
		//警车信息
		public String CAR_ID;
		public void set_CAR_ID(String i){
			CAR_ID=i;
		}
		public String get_CAR_ID(){
			return CAR_ID;
		}
		public static void addressResultAnalysis(String st,MyApplication myapp){
			    System.out.println("警员位置—————————"+st);
				String sts[]=st.split(";");
				int num=sts.length;
				for(int i=0;i<num;i++){
					AddressInfo a=new AddressInfo();
					String stss[]=sts[i].split(",");
					a.set_USER_ID(stss[0]);
					a.set_USER_NAME(stss[1]);
					String stsss[]=stss[2].split("-");
					a.set_USER_LAT(stsss[0]);
					a.set_USER_LON(stsss[1]);
					myapp.addressInfos.add(a);
				}
				System.out.println("警员个数——————"+myapp.addressInfos.size());
		}
		
		public static void caraddressResultAnalysis(String st,MyApplication myapp){
		    System.out.println("警车位置—————————"+st);
			String sts[]=st.split(";");
			int num=sts.length;
			for(int i=0;i<num;i++){
				AddressInfo a=new AddressInfo();
				String stss[]=sts[i].split(",");
				a.set_CAR_ID(stss[0]);
				String stsss[]=stss[1].split("-");
				a.set_USER_LAT(stsss[0]);
				a.set_USER_LON(stsss[1]);
				myapp.caraddressInfos.add(a);
			}
			System.out.println("警车个数——————"+myapp.caraddressInfos.size());
	}
}

