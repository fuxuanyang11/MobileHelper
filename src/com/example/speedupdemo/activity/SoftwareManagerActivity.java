package com.example.speedupdemo.activity;

import com.example.speedupdemo.manager.MemoryManager;
import com.example.speedupdemo.view.SoftwareView;
import com.example.speedupdemo.view.TitleLayout;
import com.example.speedupdemo.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SoftwareManagerActivity extends BaseActivity implements
		OnTouchListener {
	View allApp;
	View systemApp;
	View userApp;
	SoftwareView sv;
	MemoryManager manager;
	ProgressBar pb1;
	ProgressBar pb2;
	TextView tv1;
	TextView tv2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.software_manager);
		manager = new MemoryManager();
		//获取控件
		allApp = findViewById(R.id.manager_allapp);
		systemApp = findViewById(R.id.manager_systemapp);
		userApp = findViewById(R.id.manager_userapp);
		sv = (SoftwareView) findViewById(R.id.software_view);
		pb1 = (ProgressBar) findViewById(R.id.software_progress);
		pb2 = (ProgressBar) findViewById(R.id.software_progress2);
		tv1 = (TextView) findViewById(R.id.software_tv);
		tv2 = (TextView) findViewById(R.id.software_tv2);
		TitleLayout tl = (TitleLayout) findViewById(R.id.software_title);
		tl.setTitle("软件管理");
		//关联控件
		allApp.setOnTouchListener(this);
		systemApp.setOnTouchListener(this);
		userApp.setOnTouchListener(this);
		//设置动画
		long outSDSize = MemoryManager.getPhoneSDCardSize(true, false);//外置SD卡的全部内存
		if (outSDSize==0) {
			defaultToast();
		}
		long inSDSize = MemoryManager.getPhoneSDCardSize(false, false);//内置SD卡的全部内存
		long inUnUseSDSize = MemoryManager.getPhoneSDCardSize(false, true);//内置SD卡的空余内存
		long outUnUseSDSize = MemoryManager.getPhoneSDCardSize(true, true);//外置SD卡的空余内存
		long inUseSDSize = inSDSize - inUnUseSDSize;
		long outUseSDSize = outSDSize - outUnUseSDSize;
		long allSize = outSDSize+inSDSize;
		float pecent = 1.0f*inSDSize/allSize*100;
		float angle = pecent*3.6f;
		sv.setSweepAngle(0);
		sv.setAnim(angle);
		float percent = 1.0f*inUseSDSize/inSDSize*100;
		float percent2 = 1.0f*outUseSDSize/outSDSize*100;
//		System.out.println(percent);
		String inUnUseSDSizes = Formatter.formatFileSize(this, inUnUseSDSize);
		String inSDSizes =  Formatter.formatFileSize(this, inSDSize);
		String outUnUseSDSizes = Formatter.formatFileSize(this, outUnUseSDSize);
		String outSDSizes =  Formatter.formatFileSize(this, outSDSize);
		//设置进度条
		pb1.setProgress((int)Math.ceil(percent));
		tv1.setText("可用:"+inUnUseSDSizes+"/"+inSDSizes);
		pb2.setProgress((int)Math.ceil(percent2));
		tv2.setText("可用:"+outUnUseSDSizes+"/"+outSDSizes);
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.manager_allapp) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				allApp.setBackgroundResource(R.drawable.home_entry_item_pressed);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				allApp.setBackgroundResource(R.drawable.home_entry_item_normal);
				startAction(this, "all");
			}
		}
		if (v.getId() == R.id.manager_systemapp) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				systemApp.setBackgroundResource(R.drawable.home_entry_item_pressed);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				systemApp.setBackgroundResource(R.drawable.home_entry_item_normal);
				startAction(this, "system");
			}
		}
		if (v.getId() == R.id.manager_userapp) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				userApp.setBackgroundResource(R.drawable.home_entry_item_pressed);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				userApp.setBackgroundResource(R.drawable.home_entry_item_normal);
				startAction(this, "user");
			}
		}
		return true;
	}

	/**
	 * 跳转界面
	 * @param context
	 * @param data
	 */
	public static void startAction(Context context, String data) {
		Intent intent = new Intent(context, AppActivity.class);
		intent.putExtra("sortApp", data);
		context.startActivity(intent);
	}
	
	/**
	 * 如果外置SD卡内存为0显示Toast
	 */
	public void defaultToast() {
		Toast.makeText(this, "外置存储卡异常", Toast.LENGTH_SHORT).show();
	}
}
