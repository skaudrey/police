package com.policecom.imageactivity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.policecom.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

public class NewTestPicActivity extends Activity {
	// ArrayList<Entity> dataList;//用来装载数据源的列表
	List<NewImageBucket> dataList;
	GridView gridView;
	NewImageBucketAdapter adapter;// 自定义的适配器
	NewAlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	private ImageView launch_photostore_break;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_activity_image_bucket);
		launch_photostore_break = (ImageView) findViewById(R.id.launch_photostore_break);
		launch_photostore_break.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NewTestPicActivity.this.finish();
			}
		});

		helper = NewAlbumHelper.getHelper();
		helper.init(getApplicationContext());

		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		dataList = helper.getImagesBucketList(false);

		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.new_icon_addpic_unfocused);
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		// 对详细相册进行排序
		for (int i = 0; i < dataList.size(); i++) {
			Collections.sort(dataList.get(i).imageList, new FileComparator());
		}
		for (int i = 0; i < dataList.size(); i++) {
			dataList.get(i).setmName(dataList.get(i).bucketName.toString());
		}
		SortList<NewImageBucket> sortList = new SortList<NewImageBucket>();
		sortList.Sort(dataList, "getmName", null);
		adapter = new NewImageBucketAdapter(NewTestPicActivity.this, dataList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (isFastDoubleClick()) {
					Intent intent = new Intent(NewTestPicActivity.this, NewImageGridActivity.class);
					intent.putExtra(NewTestPicActivity.EXTRA_IMAGE_LIST, (Serializable) dataList.get(position).imageList);
					intent.putExtra("mPhotName", dataList.get(position).bucketName.toString());
					startActivityForResult(intent, 0);

				}

				// finish();
			}

		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		setResult(RESULT_OK, data);
		finish();
	};

	private long lastClickTime;

	public boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 2500) {
			return false;
		}
		lastClickTime = time;
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			NewBimp.drr.clear();
			NewTestPicActivity.this.finish();
			break;

		}
		return super.onKeyDown(keyCode, event);
	}
}
