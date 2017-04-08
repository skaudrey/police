package com.policecom.bean;

import java.util.ArrayList;

import com.policecom.activity.MyApplication;

public class FinalCaseInfo {
	    //案件ID及基本信息
		public int FINALCASE_ID;
		public void set_FINALCASE_ID(int i){
			FINALCASE_ID=i;
		}
		public int get_FINALCASE_ID(){
			return FINALCASE_ID;
		}
		//案件—— 报案ID及基本信息
		public int FINALCASE_CASEID;
		public void set_FINALCASE_CASEID(int i){
			FINALCASE_CASEID=i;
		}
		public int get_FINALCASE_CASEID(){
			return FINALCASE_CASEID;
		}
		//报案时间
		public String FINALCASE_TIME0;
		public void set_FINALCASE_TIME0(String s){
			FINALCASE_TIME0=s;
		}
		public String get_FINALCASE_TIME0(){
			return FINALCASE_TIME0;
		}
		//立案时间
		public String FINALCASE_TIME;
		public void set_FINALCASE_TIME(String s){
			FINALCASE_TIME=s;
		}
		public String get_FINALCASE_TIME(){
			return FINALCASE_TIME;
		}
		public String FINALCASE_LOCATION="";
		public void set_FINALCASE_LOCATION(String s){
			FINALCASE_LOCATION=s;
		}
		public String get_FINALCASE_LOCATION(){
			return FINALCASE_LOCATION;
		}
		public String FINALCASE_TYPE;
		public void set_FINALCASE_TYPE(String s){
			FINALCASE_TYPE=s;
		}
		public String get_FINALCASE_TYPE(){
			return FINALCASE_TYPE;
		}
		public String FINALCASE_PHONE;
		public void set_FINALCASE_PHONE(String s){
			FINALCASE_PHONE=s;
		}
		public String get_FINALCASE_PHONE(){
			return FINALCASE_PHONE;
		}
		public String FINALCASE_CAR;
		public void set_FINALCASE_CAR(String s){
			FINALCASE_CAR=s;
		}
		public String get_FINALCASE_CAR(){
			return FINALCASE_CAR;
		}
		public String FINALCASE_DESCRIPTION;
		public void set_FINALCASE_DESCRIPTION(String s){
			FINALCASE_DESCRIPTION=s;
		}
		public String get_FINALCASE_DESCRIPTION(){
			return FINALCASE_DESCRIPTION;
		}
		public String FINALCASE_POLICES_IDS;
		public void set_FINALCASE_POLICES_IDS(String s){
			FINALCASE_POLICES_IDS=s;
		}
		public String get_FINALCASE_POLICES_IDS(){
			return FINALCASE_POLICES_IDS;
		}
		public String FINALCASE_POLICES_ONEID;
		public void set_FINALCASE_POLICES_ONEID(String s){
			FINALCASE_POLICES_ONEID=s;
		}
		public String get_FINALCASE_POLICES_ONEID(){
			return FINALCASE_POLICES_ONEID;
		}
		public String FINALCASE_POLICES_ONENAME;
		public void set_FINALCASE_POLICES_ONENAME(String s){
			FINALCASE_POLICES_ONENAME=s;
		}
		public String get_FINALCASE_POLICES_ONENAME(){
			return FINALCASE_POLICES_ONENAME;
		}
		public String FINALCASE_PATHS;
		public void set_FINALCASE_PATHS(String s){
			FINALCASE_PATHS=s;
		}
		public String get_FINALCASE_PATHS(){
			return FINALCASE_PATHS;
		}
		public String FINALCASE_CITY="";
		public void set_FINALCASE_CITY(String s){
			FINALCASE_CITY=s;
		}
		public String get_FINALCASE_CITY(){
			return FINALCASE_CITY;
		}
		public String FINALCASE_DISTRICT="";
		public void set_FINALCASE_DISTRICT(String s){
			FINALCASE_DISTRICT=s;
		}
		public String get_FINALCASE_DISTRICT(){
			return FINALCASE_DISTRICT;
		}
		public String FINALCASE_CRIMINALS;
		public void set_FINALCASE_CRIMINALS(String s){
			FINALCASE_CRIMINALS=s;
		}
		public String get_FINALCASE_CRIMINALS(){
			return FINALCASE_CRIMINALS;
		}
		public String FINALCASE_CLIENTS;
		public void set_FINALCASE_CLIENTS(String s){
			FINALCASE_CLIENTS=s;
		}
		public String get_FINALCASE_CLIENTS(){
			return FINALCASE_CLIENTS;
		}
		public String FINALCASE_Street="";
		public void set_FINALCASE_Street(String st){
			FINALCASE_Street=st;
		}
		public String get_FINALCASE_Street(){
			return FINALCASE_Street;
		}
		public static void caseResultAnalysis(String st,MyApplication myapp){
			String[] casesst=st.split(";");
			for(int i=0;i<casesst.length;i++){
				FinalCaseInfo onecase=new FinalCaseInfo();
				String[] onecasest=casesst[i].split(",");
				onecase.set_FINALCASE_ID(Integer.parseInt(onecasest[0]));
				onecase.set_FINALCASE_CASEID(Integer.parseInt(onecasest[1]));
				onecase.set_FINALCASE_CAR(onecasest[2]);
				onecase.set_FINALCASE_TIME(onecasest[3]);
				onecase.set_FINALCASE_TYPE(onecasest[4]);
				onecase.set_FINALCASE_LOCATION(onecasest[5]);
				onecase.set_FINALCASE_CITY(onecasest[6]);
				onecase.set_FINALCASE_DISTRICT(onecasest[7]);
				onecase.set_FINALCASE_POLICES_IDS(onecasest[8]);
				onecase.set_FINALCASE_CRIMINALS(onecasest[9]);
				onecase.set_FINALCASE_CLIENTS(onecasest[10]);
				onecase.set_FINALCASE_DESCRIPTION(onecasest[11]);
				onecase.set_FINALCASE_PATHS(onecasest[12]);
				onecase.set_FINALCASE_POLICES_ONEID(onecasest[13]);
				onecase.set_FINALCASE_POLICES_ONENAME(onecasest[14]);
				myapp.finalCaseInfos.add(onecase);
			}
		}
}

