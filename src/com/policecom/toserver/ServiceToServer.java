package com.policecom.toserver;

import com.policecom.activity.LocationActivity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ServiceToServer extends Service {

	private MyThread myThread; 
    
    private boolean flag = true;  
    @Override  
    public IBinder onBind(Intent intent) {  
        // TODO Auto-generated method stub
        return null;  
    } 
    
    @Override  
    public void onCreate() {  
        System.out.println("oncreate()");  
        this.myThread = new MyThread();  
        this.myThread.start();  
        super.onCreate();  
    }  
  
    @Override  
    public void onDestroy() {  
        this.flag = false;  
        System.out.println("ondestroy()");  
        super.onDestroy();  
    }  
  
    
  
    private class MyThread extends Thread {  
        @Override  
        public void run() {  
            while (flag) {  
                System.out.println("发送请求");  
                try {  
                    // 每个10秒向服务器发送一次请求  
                    Thread.sleep(5000);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                } 
             System.out.println("Service---------addPath");
       		 Intent intent=new Intent(ServiceToServer.this,LocationActivity.class);
       		 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   			 startActivity(intent);
//    	 try{
//    		   		 
//    		 
//    		 
//   		      HttpPost request = new HttpPost(url);
//    		  /*连接超时*/
//              HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
//              /*请求超时*/
//              HttpConnectionParams.setSoTimeout(httpClient.getParams(), 5000);
//    		  
//    		  request.addHeader("Content-Type", "text/html");    
//    		  request.addHeader("charset", HTTP.UTF_8);
//    		  
//    		  String data="service|"+19820102+"";
//    		  StringEntity se = new StringEntity(data,"utf-8");
//    		  System.out.println(data);
//    		  request.setEntity(se);//发送
//    		  
//    		  HttpResponse httpResponse = httpClient.execute(request);//
//    		  int code = httpResponse.getStatusLine().getStatusCode();
//    		  System.out.println("报案返回报文码："+code); 
//    		  if(code==200){
//    			  //得到案件信息
//    			  String st=EntityUtils.toString(httpResponse.getEntity());
//    			  if(st.equals("success")){
//        			  
//    			  }
//    		  }
//            } catch(Exception e){
//            	e.printStackTrace();  
//            }
         }  
       }  
    }
}
