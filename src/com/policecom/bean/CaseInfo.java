package com.policecom.bean;

import java.util.ArrayList;

public class CaseInfo {
	    //案件ID及基本信息
		public int CASE_ID;
		public void set_CASE_ID(int i){
			CASE_ID=i;
		}
		public int get_CASE_ID(){
			return CASE_ID;
		}
		public String CASE_TIME;
		public void set_CASE_TIME(String s){
			CASE_TIME=s;
		}
		public String get_CASE_TIME(){
			return CASE_TIME;
		}
		public String CASE_LOCATION;
		public void set_CASE_LOCATION(String s){
			CASE_LOCATION=s;
		}
		public String get_CASE_LOCATION(){
			return CASE_LOCATION;
		}
		public String CASE_TYPE;
		public void set_CASE_TYPE(String s){
			CASE_TYPE=s;
		}
		public String get_CASE_TYPE(){
			return CASE_TYPE;
		}
		public String CASE_PHONE;
		public void set_CASE_PHONE(String s){
			CASE_PHONE=s;
		}
		public String get_CASE_PHONE(){
			return CASE_PHONE;
		}
		public String CASE_DESCRIPTION;
		public void set_CASE_DESCRIPTION(String s){
			CASE_DESCRIPTION=s;
		}
		public String get_CASE_DESCRIPTION(){
			return CASE_DESCRIPTION;
		}
		public String CASE_POLICES_IDS;
		public void set_CASE_POLICES_IDS(String s){
			CASE_POLICES_IDS=s;
		}
		public String get_CASE_POLICES_IDS(){
			return CASE_POLICES_IDS;
		}
		public String CASE_POLICES_NAMES;
		public void set_CASE_POLICES_NAMES(String s){
			CASE_POLICES_NAMES=s;
		}
		public String get_CASE_POLICES_NAMES(){
			return CASE_POLICES_NAMES;
		}
		public String CASE_CITY;
		public void set_CASE_CITY(String s){
			CASE_CITY=s;
		}
		public String get_CASE_CITY(){
			return CASE_CITY;
		}
		public String CASE_DISTRICT;
		public void set_CASE_DISTRICT(String s){
			CASE_DISTRICT=s;
		}
		public String get_CASE_DISTRICT(){
			return CASE_DISTRICT;
		}
		
		public static ArrayList<CaseInfo> caseResultAnalysis(String st){
			ArrayList<CaseInfo>list=new ArrayList<CaseInfo>();
			String[] casesst=st.split("\\.");
			for(int i=0;i<casesst.length;i++){
				CaseInfo onecase=new CaseInfo();
				String[] onecasest=casesst[i].split(",");
				onecase.set_CASE_ID(Integer.parseInt(onecasest[0]));
				onecase.set_CASE_TIME(onecasest[1]);
				onecase.set_CASE_TYPE(onecasest[2]);
				onecase.set_CASE_DESCRIPTION(onecasest[3]);
				onecase.set_CASE_CITY(onecasest[4]);
				onecase.set_CASE_DISTRICT(onecasest[5]);
				onecase.set_CASE_LOCATION(onecasest[6]);
				onecase.set_CASE_PHONE(onecasest[7]);
				onecase.set_CASE_POLICES_IDS(onecasest[8]);
				onecase.set_CASE_POLICES_NAMES(onecasest[9]);
				list.add(onecase);
			}
			return list;
		}
}

