package com.policecom.activity;

import com.policecom.bean.SheriffInfo;
import com.policecom.toserver.SheriffToServer;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SheriffLoginActivity extends ActionBarActivity {
	private EditText sl_username;
	private EditText sl_password;
	private Button sl_clearUsername;
	private Button sl_clearPassword;
	private Button sl_loginButton;
	private CheckBox sl_cb_remenber;
	private CheckBox sl_cb_autologin;

	ProgressDialog dialog;
	MyApplication myapp;
	String login_result;
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			dialog.dismiss();
			if(msg.what==200){
				SheriffInfo.loginResultAnalysis(msg.obj.toString(),myapp);
				if(myapp.get_LOGIN_ID()==1){
				      Toast.makeText(SheriffLoginActivity.this, "用户不存在！", Toast.LENGTH_SHORT).show();
			    }
			    else if(myapp.get_LOGIN_ID()==2){
				      Toast.makeText(SheriffLoginActivity.this, "账号或密码错误！", Toast.LENGTH_SHORT).show();
			    }
			    else{
				Intent intent=new Intent(SheriffLoginActivity.this, SheriffMainActivity.class);
				startActivity(intent);
				finish();
				String st=myapp.S_INFO.get_USER_NAME();
				Toast.makeText(SheriffLoginActivity.this, "欢迎警长"+st+"登陆！", Toast.LENGTH_SHORT).show();
			    }
			}else{
				Toast.makeText(SheriffLoginActivity.this, "联网失败！请检查网络", Toast.LENGTH_SHORT).show();
		  }	
			super.handleMessage(msg);
		}
	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_sherifflogin);		
		initView();		
		
		myapp=(MyApplication)getApplication();
		//登录
		sl_loginButton.setOnClickListener(new LoginOnClick());
		}
	//登陆点击事件
		private class LoginOnClick implements OnClickListener  
	    {  
	        public void onClick(View arg0) {  
	            String usename=sl_username.getText().toString();
	            String password=sl_password.getText().toString();
	            if(password==null||password.length()<=0)   
	            {         
	            	sl_password.requestFocus();  
	            	sl_password.setError("对不起，密码不能为空");  
	            }else{  
	                password=sl_password.getText().toString().trim();  
	            }  
	            
	            if (usename==null||usename.length()<=0)   
	            {         
	            	sl_username.requestFocus();  
	            	sl_password.setError("对不起，用户名不能为空");
	            	return; 
	            } else{  
	            	usename=sl_username.getText().toString().trim();  
	            }  
	            
	            dialog=new ProgressDialog(SheriffLoginActivity.this);  
	            dialog.setTitle("登录中");  
	            dialog.setMessage("登录中，请稍后");  
	            //开始登陆
	            dialog.show();
	            String url=myapp.get_URL();
	            SheriffToServer.LoginToServer(url,usename, password,handler);
	        }  
	    }
	private void initView(){
		//初始化数据
		sl_username=(EditText) findViewById(R.id.sl_username);//用户名
		sl_password=(EditText) findViewById(R.id.sl_password);//密码
				
		sl_clearUsername=(Button) findViewById(R.id.sl_clear_username);//清除用户名
		sl_clearPassword=(Button) findViewById(R.id.sl_clear_password);//清除密码	
				
		sl_loginButton=(Button) findViewById(R.id.sl_btn_login);//登陆button
				
		sl_cb_remenber=(CheckBox) findViewById(R.id.sl_remenberPassword);//记录密码
		sl_cb_autologin=(CheckBox) findViewById(R.id.sl_autoLogin);//自动登陆
				
				//清除用户名
				sl_username.addTextChangedListener(mTextWatcher);
				sl_clearUsername.setOnClickListener(new OnClickListener(){					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						sl_username.setText("");
						
					}
				});	
				
				//清除密码
				sl_password.addTextChangedListener(nTextWatcher);
				sl_clearPassword.setOnClickListener(new OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						sl_password.setText("");
						
					}
				});	
								
	}
	
	TextWatcher mTextWatcher  = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		}
		@Override
		public void afterTextChanged(Editable s) {
		if(sl_username.getText().toString()!=null&&!sl_username.getText().toString().equals("")){
			sl_clearUsername.setVisibility(View.VISIBLE);
		}else{
			sl_clearUsername.setVisibility(View.INVISIBLE);
		}
		}
	};
	
	TextWatcher nTextWatcher  = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		}
		@Override
		public void afterTextChanged(Editable s) {
		if(sl_password.getText().toString()!=null&&!sl_password.getText().toString().equals("")){
			sl_clearPassword.setVisibility(View.VISIBLE);
		}else{
			sl_clearPassword.setVisibility(View.INVISIBLE);
		}
		}
	};
	
}

