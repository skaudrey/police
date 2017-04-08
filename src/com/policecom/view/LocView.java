package com.policecom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class LocView extends LinearLayout implements OnClickListener{
	public Button btComplex;
	public TextView tvInfo;

	public LocView(Context context){
			super(context);
			setupUI(context);}

	public LocView(Context context, AttributeSet attrSet){
			super(context, attrSet);
			setupUI(context);}

	private void setupUI(Context context){
		if (btComplex != null || this.isInEditMode()) 
			return;
		
		//UI初始化
		setOrientation(LinearLayout.VERTICAL); 
		tvInfo = new TextView(context);
		tvInfo.setText("");
		btComplex=new Button(context);
		LinearLayout.LayoutParams ll_params =
				new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, 
						LayoutParams.WRAP_CONTENT);
		ll_params.width =150;
		ll_params.height=68;
		btComplex.setLayoutParams(ll_params);
		btComplex.setText("详细");
		
		addView(tvInfo);
		addView(btComplex);
	}
	public void onClick(View v){}
}
