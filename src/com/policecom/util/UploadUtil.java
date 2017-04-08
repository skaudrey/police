package com.policecom.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.os.Handler;
import android.util.Log;
 
//采用HttpClient上传文件,支持多文件上传
public class UploadUtil {
	public static final int UPLOAD_SUCCESS=0x123;
	public static final int UPLOAD_FAIL=0x124;
	public UploadUtil() {
		// TODO Auto-generated constructor stub
		
	}	
	/**
	 * @param params 请求参数，包括请求的的方法参数method如：“upload”，
	 * 请求上传的文件类型fileTypes如：“.jpg.png.docx”
	 * @param files 要上传的文件集合
	 */
	public static void uploadFileToServer(final String url, final ArrayList<File>files,final Handler handler) {
		// TODO Auto-generated method stub	
		new Thread(new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					 if (uploadFiles(url,files)) {
						handler.sendEmptyMessage(UPLOAD_SUCCESS);//通知主线程数据发送成功
					}else {
						//将数据发送给服务器失败
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}).start();
	}
	/**
	 * @param url servlet的地址
	 * @param params 要传递的参数
	 * @param files 要上传的文件
	 * @return true if upload success else false
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static boolean uploadFiles(String url,ArrayList<File>files) throws ClientProtocolException, IOException {
		HttpClient client=new DefaultHttpClient();// 开启一个客户端 HTTP 请求 
//		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,2000);//连接时间
//		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,5000);//数据传输时间
		url=url+"ImageServlet";
		System.out.println(url);
		HttpPost post = new HttpPost(url);//创建 HTTP POST 请求  
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	//	builder.setCharset(Charset.forName("uft-8"));//设置请求的编码格式
	    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
		builder.addTextBody("method", "upload");//设置请求参数
		//System.out.println(builder.toString());
		builder.addTextBody("fileTypes", ".jpg");//设置请求参数
		System.out.println(builder.toString());
		
		int count=0;
		for (File file:files) {
			builder.addBinaryBody("file"+count, file);
			count++;
		}
		HttpEntity entity = builder.build();// 生成 HTTP POST 实体  	new StringEntity("aaaaaaa","utf-8");
		System.out.println("请求"+entity.toString());
		post.setEntity(entity);//设置请求参数
		HttpResponse response = client.execute(post);// 发起请求 并返回请求的响应
		System.out.println(response.getStatusLine().getStatusCode());
		System.out.println(response.toString());
		if (response.getStatusLine().getStatusCode()==200) {
			return true;
		}
		return false;		
	}
}
