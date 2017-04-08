package com.policecom.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SetPtView extends LinearLayout implements OnClickListener{
	public Button btSet;
	public TextView tvPtInfo;

	public SetPtView(Context context){
			super(context);
			setupUI(context);}

	public SetPtView(Context context, AttributeSet attrSet){
			super(context, attrSet);
			setupUI(context);}

	private void setupUI(Context context){
		if (btSet != null || this.isInEditMode()) 
			return;
		//UI初始化
		setOrientation(LinearLayout.VERTICAL); 
		tvPtInfo = new TextView(context);
		tvPtInfo.setText("");
		tvPtInfo.setGravity(1);
		tvPtInfo.setTextColor(Color.rgb(206, 94, 44));
		
		btSet = new Button(context);
		LinearLayout.LayoutParams ll_params =
				new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, 
						LayoutParams.WRAP_CONTENT);
		ll_params.width =300;
		ll_params.height=68;
		btSet.setLayoutParams(ll_params);		
		btSet.setText("确认");
		
		addView(tvPtInfo);
		addView(btSet);	
	}
	public void onClick(View v){}
}
