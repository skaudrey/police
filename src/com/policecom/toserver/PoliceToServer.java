package com.policecom.toserver;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;

public class PoliceToServer {

	
	/**
	 * 登录login
	 */
	 static public void LoginToServer(final String URL,final String name,final String pwd,final Handler handler) {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
					String data="login"+"|"+name+","+pwd;
					getResponse(URL, data, handler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}).start();
	}
	 
	 /**
		 *获得可用警车
		 */
		 static public void GetAvailCar(final String URL,final Handler handler) {
			new Thread(new Runnable() {			
				@Override
				public void run() {
					try {
						String data="availcar"+"|"+"车子";
						getResponse(URL, data, handler);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}
			}).start();
		}

	 
	 /**
		 * 开始导航
		 */
		 static public void StartCase(final String URL,final String pDuty,final Handler handler) {
			new Thread(new Runnable() {			
				@Override
				public void run() {
					try {
						String data="startcase"+"|"+pDuty;
						getResponse(URL, data, handler);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}
			}).start();
		}

	 /**
		 * 路径
		 */
		 static public void addPath(final String URL,final String pNumber,final String dutyId,final String latlng,final Handler handler) {
			new Thread(new Runnable() {			
				@Override
				public void run() {
					try {
						String data="service"+"|"+pNumber+","+dutyId+","+latlng;
						System.out.println("服务器——————————经纬度"+data);
						getResponse(URL, data, handler);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}
			}).start();
		}
	 
	 
	/**
	 * 决定立案invest
	 */
	static public void FinalCaseToServer(final String URL, final String st, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String data = "finalcase" + "|" + st;
					getResponse(URL, data, handler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	/**
	 * 查看分配的case
	 */
	static public void getCaseToServer(final String URL, final Handler handler) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String data = "getcase" + "|" + "案件！";
					getResponse(URL, data, handler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	 //发送请求
	 private static void getResponse(String url,String data,Handler handler) 
			 throws ParseException, IOException{
			try {
		  HttpClient httpClient = new DefaultHttpClient();
		  url=url+"PoliceServlet";
		  HttpPost request = new HttpPost(url);
		 
		  /*连接超时*/
          HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
          /*请求超时*/
          HttpConnectionParams.setSoTimeout(httpClient.getParams(), 5000);
		  
		  request.addHeader("Content-Type", "text/html");    
		  request.addHeader("charset", HTTP.UTF_8);
		  StringEntity se = new StringEntity(data,"utf-8");
		  System.out.println(data);
		  request.setEntity(se);//发送
		  
		  HttpResponse httpResponse = httpClient.execute(request);//
		  int code = httpResponse.getStatusLine().getStatusCode();
		  System.out.println(code);
		  Message msg=new Message();
		  msg.what=code;
		  if(code==200){
			  String result = EntityUtils.toString(httpResponse.getEntity());
			  System.out.println(result);
			  msg.obj=result;
	          }
		      handler.sendMessage(msg);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ConnectTimeoutException cte) {
				  Message msg=new Message();
				  msg.what=1000;
				  handler.sendMessage(msg);
            } catch (SocketTimeoutException ste) {
            	  Message msg=new Message();
				  msg.what=1000;
				  handler.sendMessage(msg);
            }catch (NoHttpResponseException e){
            	  Message msg=new Message();
				  msg.what=1000;
				  handler.sendMessage(msg);
            }
        }
}