package com.policecom.toserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import android.os.Handler;
import android.os.Message;
 
//采用HttpClient上传文件,支持多文件上传
public class UploadImage {
	public UploadImage() {
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
					if (uploadFiles(url,files,handler)) {
						System.out.println("上传成功");//通知主线程数据发送成功
					}else {
						//将数据发送给服务器失败
						System.out.println("上传失败");
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
	private static boolean uploadFiles(String url,ArrayList<File>files,Handler handler) throws ClientProtocolException, IOException {
		try {  	  
			    HttpClient client=new DefaultHttpClient();// 开启一个客户端 HTTP 请求   
			    /*连接超时*/
		        HttpConnectionParams.setConnectionTimeout(client.getParams(), 3000);
		        /*请求超时*/
		        HttpConnectionParams.setSoTimeout(client.getParams(), 5000);
			    
			    HttpPost post = new HttpPost(url+"ImageUploadServlet");//创建 HTTP POST 请求    
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();  
		       // builder.setCharset(Charset.forName("utf-8"));//设置请求的编码格式  
			    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式  
			    
				for (File file:files) {
					System.out.println(file.getName());
					builder.addBinaryBody(file.getName(), file, ContentType.create("application/jpg"),file.getName());
					//builder.addPart("img",new FileBody(file, ContentType.MULTIPART_FORM_DATA));
				}
				builder.addTextBody("method", "upload");//设置请求参数  
				HttpEntity entity = builder.build();// 生成 HTTP POST 实体
				post.setEntity(entity);//设置请求参数  
				HttpResponse response = client.execute(post);// 发起请求 并返回请求的响应  
				int code=response.getStatusLine().getStatusCode();
				System.out.println("上传图片返回报文码："+code); 
				System.out.println(code);
				handler.sendEmptyMessage(200);
				return true;
	         } catch (Exception e) {  
	             e.printStackTrace();  
	        }
		return false;  
	
	}
}
