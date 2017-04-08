package com.policecom.bean;

import java.util.Calendar;
import java.util.Date;

//初始任务分配表，在系统做推送时初始化，
public class Assign {
	private static boolean start=false;
	private static Date time;//系统发送任务或用户终端确认执行任务的时间
	private static String caseId ;
	private static String pNumber;
	private static double curX;
	private static double curY;
	private static double caseX;
	private static double caseY;
	
	public Assign(String pNumber,double curX,
			double curY,double caseX,double caseY,String caseId){
		
		this.caseId=caseId;
		this.pNumber=pNumber;
		this.curX=curX;
		this.curY=curY;
		this.caseX=caseX;
		this.caseY=caseY;
		setTime();
	}
	
	public void setStart(){
		this.start=!start;
	}	
	public boolean getStart(){
		return this.start;
	}
	
	public void setTime(){
		this.time=Calendar.getInstance().getTime();
	}	
	public Date getTime(){
		return this.time;
	}
	
	/*public void setCarNumber(String carNumber){
		this.carNumber=carNumber;
	}	
	public String getCarNumber(){
		return this.carNumber;
	}*/
	
	public void setCaseId(String caseId){
		this.caseId=caseId;
	}	
	public String getCaseId(){
		return this.caseId;
	}
	
	public void setPNumber(String pNumber){
		this.pNumber=pNumber;
	}	
	public String getPNumber(){
		return this.pNumber;
	}
	
	public void setCurX(double curX){
		this.curX=curX;
	}
	public double getCurX(){
		return this.curX;
	}
	
	public void setCurY(double curY){
		this.curY=curY;
	}
	public double getCurY(){
		return this.curY;
	}
	
	public void setCaseX(double caseX){
		this.caseX=caseX;
	}
	public double getCaseX(){
		return this.caseX;
	}
	
	public void setCaseY(double caseY){
		this.caseY=caseY;
	}
	public double getCaseY(){
		return this.caseY;
	}
}
