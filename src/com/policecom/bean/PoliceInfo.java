package com.policecom.bean;

import android.R.integer;

import com.policecom.activity.MyApplication;

public class PoliceInfo {
	    //警员ID及基本信息
		public int USER_ID;
		public void set_USER_ID(int i){
			USER_ID=i;
		}
		public int get_USER_ID(){
			return USER_ID;
		}
		public String USER_NAME;
		public void set_USER_NAME(String s){
			USER_NAME=s;
		}
		public String get_USER_NAME(){
			return USER_NAME;
		}
		public String USER_NUMBER;
		public void set_USER_NUMBER(String s){
			USER_NUMBER=s;
		}
		public String get_USER_NUMBER(){
			return USER_NUMBER;
		}
		public String USER_PWD;
		public void set_USER_PWD(String s){
			USER_PWD=s;
		}
		public String get_USER_PWD(){
			return USER_PWD;
		}
		public String USER_PHONE;
		public void set_USER_PHONE(String s){
			USER_PHONE=s;
		}
		public String get_USER_PHONE(){
			return USER_PHONE;
		}
		public String USER_OFFICENUM;
		public void set_USER_OFFICENUM(String s){
			USER_OFFICENUM=s;
		}
		public String get_USER_OFFICENUM(){
			return USER_OFFICENUM;
		}
		public String USER_ONWEEK1;
		public void set_USER_ONWEEK1(String s){
			USER_ONWEEK1=s;
		}
		public String get_USER_ONWEEK1(){
			return USER_ONWEEK1;
		}
		public String USER_ONWEEK2;
		public void set_USER_ONWEEK2(String s){
			USER_ONWEEK2=s;
		}
		public String get_USER_ONWEEK2(){
			return USER_ONWEEK2;
		}
		public String USER_ONDUTYTIME1;
		public void set_USER_ONDUTYTIME1(String s){
			USER_ONDUTYTIME1=s;
		}
		public String get_USER_ONDUTYTIME1(){
			return USER_ONDUTYTIME1;
		}
		public String USER_ONDUTYTIME2;
		public void set_USER_ONDUTYTIME2(String s){
			USER_ONDUTYTIME2=s;
		}
		public String get_USER_ONDUTYTIME2(){
			return USER_ONDUTYTIME2;
		}
		public String USER_ADDRESS;
		public void set_USER_ADDRESS(String s){
			USER_ADDRESS=s;
		}
		public String get_USER_ADDRESS(){
			return USER_ADDRESS;
		}
		public String USER_ABSENTTIME;
		public void set_USER_ABSENTTIME(String s){
			USER_ABSENTTIME=s;
		}
		public String get_USER_ABSENTTIME(){
			return USER_ABSENTTIME;
		}
		public String USER_OID;
		public void set_USER_OID(String s){
			USER_OID=s;
		}
		public String get_USER_OID(){
			return USER_OID;
		}
		public String USER_ONDUTYSIGN;
		public void set_USER_ONDUTYSIGN(String s){
			USER_ONDUTYSIGN=s;
		}
		public String get_USER_ONDUTYSIGN(){
			return USER_ONDUTYSIGN;
		}
		
		public static void loginResultAnalysis(String st,MyApplication myapp){
			System.out.println(st);
			if(st.equals("0") || st.equals("1") ||st.equals("2")){
				myapp.set_LOGIN_ID(Integer.parseInt(st));
			}
			else{
				String sts[]=st.split("\\.");
				int i=Integer.parseInt(sts[0]);
				myapp.set_LOGIN_ID(i);
				String sst[]=sts[1].split(",");
				
				myapp.P_INFO.set_USER_ID(Integer.parseInt(sst[0]));
				myapp.P_INFO.set_USER_NAME(sst[1]);
				myapp.P_INFO.set_USER_NUMBER(sst[2]);
				
				myapp.P_INFO.set_USER_PWD(sst[3]);
				myapp.P_INFO.set_USER_PHONE(sst[4]);
				myapp.P_INFO.set_USER_OFFICENUM(sst[5]);
				
				myapp.P_INFO.set_USER_ONDUTYSIGN(sst[6]);
				myapp.P_INFO.set_USER_ONWEEK1(sst[7]);
				myapp.P_INFO.set_USER_ONWEEK2(sst[8]);
				
				myapp.P_INFO.set_USER_ONDUTYTIME1(sst[9]);
				myapp.P_INFO.set_USER_ONDUTYTIME2(sst[10]);
				myapp.P_INFO.set_USER_ADDRESS(sst[11]);
				
				myapp.P_INFO.set_USER_ABSENTTIME(sst[12]);
				myapp.P_INFO.set_USER_OID(sst[13]);
				
				myapp.pDuty.setDutyId(sts[2]);
			}
		}
}

