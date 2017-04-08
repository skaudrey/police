package com.policecom.toserver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import com.policecom.bean.FinalCaseInfo;

import android.os.Handler;
import android.os.Message;

public class SheriffToServer {
	
	/**
	 * @登录
	 * @param name 姓名
	 * @param pwd  密码
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
		 * @查询最近发生案件
		 */
	static public void GetCasesFromServer(final String URL,final String time,final Handler handler) {
			new Thread(new Runnable() {			
				@Override
				public void run() {
					try {
						String data="cases"+"|"+time;
						getResponse(URL, data, handler);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}
			}).start();
	}
		
	/**
	 * @查看警员位置
	 */
	 static public void PoliceAddressToServer(final String URL,final Handler handler) {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
					String data="police_address"+"|"+"位置";
					getResponse(URL, data, handler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}).start();
	}
	 /**
		 * @查看警车位置
		 */
		 static public void CarAddressToServer(final String URL,final Handler handler) {
			new Thread(new Runnable() {			
				@Override
				public void run() {
					try {
						String data="car_address"+"|"+"位置";
						getResponse(URL, data, handler);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}
			}).start();
		}
	 /**
		 * @查看警员路径
		 */
		 static public void PolicePathToServer(final String URL,final String p_Number,final Handler handler) {
			new Thread(new Runnable() {			
				@Override
				public void run() {
					try {
						String data="police_path"+"|"+p_Number;
						getResponse(URL, data, handler);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}
			}).start();
		}
		 /**
			 * @根据警员号查案件
			 */
			 static public void GetCaseByPoliceNum(final String URL,final String p_Number,final Handler handler) {
				new Thread(new Runnable() {			
					@Override
					public void run() {
						try {
							String data="police_case"+"|"+p_Number;
							getResponse(URL, data, handler);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}				
					}
				}).start();
			}
		 /**
			 * @查看警车路径
			 */
			 static public void CarPathToServer(final String URL,final String car_num,final Handler handler) {
				new Thread(new Runnable() {			
					@Override
					public void run() {
						try {
							String data="car_path"+"|"+car_num;
							getResponse(URL, data, handler);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}				
					}
				}).start();
			}
			 /**
				 * @根据警车号查案件
				 */
				 static public void GetCaseByCarNum(final String URL,final String c_Number,final Handler handler) {
					new Thread(new Runnable() {			
						@Override
						public void run() {
							try {
								String data="car_case"+"|"+c_Number;
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
				  url=url+"SheriffServlet";
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
				  
				  HttpResponse httpResponse;
				  httpResponse = httpClient.execute(request);
				  int code = httpResponse.getStatusLine().getStatusCode();
				  System.out.println(code);
				  Message msg=new Message();
				  msg.what=code;
				  if(code==200){
					  String result = EntityUtils.toString(httpResponse.getEntity());
					  System.out.println(result);
					  msg.obj=result;
					  System.out.println(handler.sendMessage(msg)); 
			      }
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