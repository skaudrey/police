package com.policecom.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.policecom.activity.R;
import com.policecom.imageactivity.ConvertToBitmap;

public class ImageAdapter  extends BaseAdapter {

	private Context mContext;
	private List<String> mList;
	public ImageAdapter(Context context, List<String> list) {
		this.mContext = context;
		this.mList = list;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
		
		holder = new Holder();
		convertView = View.inflate(mContext, R.layout.ada_image, null);
		holder.iv = (ImageView) convertView.findViewById(R.id.ada_iv_image);
		holder.iv.setVisibility(View.VISIBLE);
		Bitmap bm = ConvertToBitmap.GetToBitmap(mList.get(position), 100, 100);
		holder.iv.setImageBitmap(bm);
		return convertView;
	}
	class Holder {
		private ImageView iv;
	}
}
