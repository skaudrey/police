package com.policecom.toserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
 
//采用HttpClient上传文件,支持多文件上传
public class DownloadImage {
		
	static Message msg=new Message();
	 /**
	  * 下载图片
	  */
	 static public void DownloadToServer(final String URL,final ArrayList<String>filenames,final Handler handler) {
			new Thread(new Runnable() {			
				@Override
				public void run() {
						for(int i=0;i<filenames.size();i++){
							try {
								getResponse(URL, filenames.get(i));
								if(i==(filenames.size()-1)){
									handler.sendMessage(msg);
								}
								System.out.println("下载成功");//通知主线程数据发送成功
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				}
			}).start();
		}
	 
	// 发送请求
	private static void getResponse(String url, String filename)
			throws ParseException, IOException {
		try {
			String data = "download" + "|" + filename;
			HttpClient httpClient = new DefaultHttpClient();
			url = url + "ImageDownloadServlet";
			HttpPost request = new HttpPost(url);

			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					3000);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(httpClient.getParams(), 5000);

			request.addHeader("Content-Type", "text/html");
			request.addHeader("charset", HTTP.UTF_8);
			StringEntity se = new StringEntity(data, "utf-8");
			System.out.println(data);
			request.setEntity(se);// 发送

			HttpResponse httpResponse = httpClient.execute(request);//
			int code = httpResponse.getStatusLine().getStatusCode();
			System.out.println(code);
			msg.what = code;
			System.out.println(msg.what);
			if (code == 200) {
				// 输入流
				InputStream is = httpResponse.getEntity().getContent();
				// 1K的数据缓冲
				byte[] bs = new byte[1024];
				// 读取到的数据长度
				int len;
				// 输出的文件流
				String fileString = Environment.getExternalStorageDirectory()
						+ "/Police/" + filename;
				System.out.println(fileString);
				File file = new File(fileString);
				OutputStream os = new FileOutputStream(fileString);
				// 开始读取
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
