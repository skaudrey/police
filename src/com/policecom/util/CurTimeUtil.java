package com.policecom.util;

import java.util.Calendar;

//获取当前时间的字符串
public class CurTimeUtil {
	private String curDate="";
	private String curTime="";
		
	//返回：20150613
	public String getDate() {				
		int y,m,d;  
		Calendar cal=Calendar.getInstance();
		cal.getTime();
		y=cal.get(Calendar.YEAR);    
		m=cal.get(Calendar.MONTH);    
		d=cal.get(Calendar.DATE);    
		
		if(m<=8){
			if (d<=9) {
				this.curDate=y+"0"+(m+1)+"0"+d;
			}else {
				this.curDate=y+"0"+(m+1)+d;
			}
			
			}
		if(m>8){
			if (d<=9) {
				this.curDate=y+(m+1)+"0"+d;
			}else {
				this.curDate=Integer.toString(y)+
						Integer.toString(m+1)+
						Integer.toString(d);}
			}
		
		return this.curDate;
	}
	//返回：1520
	public String getTime() {				
		int h,m;    
		Calendar cal=Calendar.getInstance();
		cal.getTime();
		h=cal.get(Calendar.HOUR_OF_DAY);    
		m=cal.get(Calendar.MINUTE);    
		   
		if(h<=9){
			if (m<=9) {
				this.curDate="0"+h+"0"+m;
			}else {
				this.curDate="0"+h+m;}
			}
		if(h>9){
			if (m<=9) {
				this.curDate=h+"0"+m;
			}else {
				this.curDate=Integer.toString(h)+
						m;}
			}		
		return this.curTime;
	}
	//返回201506131520
	public String getDateAndTime() {				
		return this.curDate+this.curTime;
	}
}
