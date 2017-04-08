package com.policecom.activity;

import com.policecom.bean.PoliceInfo;
import com.policecom.toserver.PoliceToServer;

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
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class PoliceLoginActivity extends ActionBarActivity {
	private EditText pl_username;
	private EditText pl_password;
	private Button pl_clearUsername;
	private Button pl_clearPassword;
	private Button pl_loginButton;
	private CheckBox pl_cb_remenber;
	private CheckBox pl_cb_autologin;

	
	ProgressDialog dialog;
	MyApplication myapp;
	String login_result;
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			dialog.dismiss();
			if(msg.what==200){
				PoliceInfo.loginResultAnalysis(msg.obj.toString(),myapp);
				if(myapp.get_LOGIN_ID()==1){
					Toast.makeText(PoliceLoginActivity.this, "用户不存在！", Toast.LENGTH_SHORT).show();
				}
				else if(myapp.get_LOGIN_ID()==2){
					Toast.makeText(PoliceLoginActivity.this, "账号或密码错误！", Toast.LENGTH_SHORT).show();
				}
				else{
					String st=myapp.P_INFO.get_USER_NAME();
					Toast.makeText(PoliceLoginActivity.this, "欢迎警员"+st+"登陆！", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(PoliceLoginActivity.this, PoliceMainActivity.class);
					startActivity(intent);
					finish();
				}
			}else{
				Toast.makeText(PoliceLoginActivity.this, "联网失败！请检查网络", Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}	
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_policelogin);		
		initView();		
		myapp=(MyApplication)getApplication();
		//登录
		pl_loginButton.setOnClickListener(new LoginOnClick());
		
		}
	
    //登陆点击事件
	private class LoginOnClick implements OnClickListener  
    {  
        public void onClick(View arg0) {  
            String usename=pl_username.getText().toString();
            String password=pl_password.getText().toString();
            if(password==null||password.length()<=0)   
            {         
            	pl_password.requestFocus();  
            	pl_password.setError("对不起，密码不能为空");  
            }else{  
                password=pl_password.getText().toString().trim();  
            }  
            
            if (usename==null||usename.length()<=0)   
            {         
            	pl_username.requestFocus();  
            	pl_password.setError("对不起，用户名不能为空");
            	return; 
            } else{  
            	usename=pl_username.getText().toString().trim();  
            }  
            
            dialog=new ProgressDialog(PoliceLoginActivity.this);  
            dialog.setTitle("登录中");  
            dialog.setMessage("登录中，请稍后");  
            //开始登陆
            dialog.show();
            String url=myapp.get_URL();
            PoliceToServer.LoginToServer(url,usename, password,handler);
        }  
    }
	private void initView(){
		//初始化数据
		pl_username=(EditText) findViewById(R.id.pl_username);//用户名
		pl_password=(EditText) findViewById(R.id.pl_password);//密码
				
		pl_clearUsername=(Button) findViewById(R.id.pl_clear_username);//清除用户名
		pl_clearPassword=(Button) findViewById(R.id.pl_clear_password);//清除密码	
				
		pl_loginButton=(Button) findViewById(R.id.pl_btn_login);//登陆button
			
				
		pl_cb_remenber=(CheckBox) findViewById(R.id.pl_remenberPassword);//记录密码
		pl_cb_autologin=(CheckBox) findViewById(R.id.pl_autoLogin);//自动登陆
				
				//清除用户名
				pl_username.addTextChangedListener(mTextWatcher);
				pl_clearUsername.setOnClickListener(new OnClickListener(){					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pl_username.setText("");
						
					}
				});	
				
				//清除密码
				pl_password.addTextChangedListener(nTextWatcher);
				pl_clearPassword.setOnClickListener(new OnClickListener(){
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pl_password.setText("");
						
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
		if(pl_username.getText().toString()!=null&&!pl_username.getText().toString().equals("")){
			pl_clearUsername.setVisibility(View.VISIBLE);
		}else{
			pl_clearUsername.setVisibility(View.INVISIBLE);
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
		if(pl_password.getText().toString()!=null&&!pl_password.getText().toString().equals("")){
			pl_clearPassword.setVisibility(View.VISIBLE);
		}else{
			pl_clearPassword.setVisibility(View.INVISIBLE);
		}
		}
	};
	
	
}
