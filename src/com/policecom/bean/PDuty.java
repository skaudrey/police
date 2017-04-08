package com.policecom.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.baidu.mapapi.model.LatLng;

public class PDuty {
	private String dutyId; //数据库中w_id
	private String pNum;  //id
	private String sPt;   //路径起点
    private String ePt;	 //案件经纬
    private String curPt;//当前经纬
    private List<LatLng> pathList=new ArrayList<LatLng>();
    private String pathStr="";
    
    public String getDutyId(){  
        return dutyId;
    }  
    public void setDutyId(String dutyId){  
        this.dutyId = dutyId;  
    }
    
    public String getPNum(){  
        return pNum;
    }  
    public void setPNum(String pNum){  
        this.pNum = pNum;  
    }
    
    public LatLng getSPt(){  
        return new LatLng(
        		Double.parseDouble(this.sPt.split("-")[0]),
        		Double.parseDouble(this.sPt.split("-")[1]));
    }  
    public void setSPt(String sPt){  
        this.sPt = sPt;  
    }  
    
    public LatLng getEPt(){  
        return new LatLng(
        		Double.parseDouble(this.ePt.split("-")[0]),
        		Double.parseDouble(this.ePt.split("-")[1]));  
    }  
    public void setEPt(String ePt){  
        this.ePt = ePt;  
    }   
    
    public LatLng getCurPt(){  
        return new LatLng(
        		Double.parseDouble(this.curPt.split("-")[0]),
        		Double.parseDouble(this.curPt.split("-")[1]));  
    }   
    public void setCurPt(String curPt){  
        this.curPt = curPt;  
    }
    
    public String getPathStr(){
    	return this.pathStr;
    }
    public void setPathStr(String pathStr){
    	this.pathStr=pathStr;
    }
    
    public List<LatLng> getPathList(){  
    	String[]  aa = this.pathStr.split(";"); 
    	   for(int i=0; i < aa.length; i++) { 
    	    this.pathList.add(new LatLng(
    	    		Double.parseDouble(aa[i].split("-")[0]),
    	    		Double.parseDouble(aa[i].split("-")[1])));
    	   
    	     }
    	   
        return pathList; 
        
    }   
    /*public void addPathList(LatLng pt){  
        this.pathList.add(pt);  
    }*/
}
