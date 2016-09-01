package com.example.speedupdemo.activity;

import java.util.ArrayList;

import com.example.speedupdemo.adapter.SoftwareAdapter;
import com.example.speedupdemo.broadcast.AppBroadcast;
import com.example.speedupdemo.info.AppInfo;
import com.example.speedupdemo.manager.AppInfoManager;
import com.example.speedupdemo.view.TitleLayout;
import com.example.speedupdemo.R;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AppActivity extends BaseActivity implements Runnable,
		OnTouchListener {
	// TODO 全局变量
	AppInfoManager aManager;
	ListView listView;
	SoftwareAdapter sAdapter;
	ImageView iv;
	boolean allCheck;
	String data;// 上个界面获取的数据
	ArrayList<AppInfo> appInfos;
	TextView unInstall;
	ProgressBar pb;
	TitleLayout tl;
	//声明广播对象
	AppBroadcast appBroadcast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.software_all);
		listView = (ListView) findViewById(R.id.software_listview);
		aManager = new AppInfoManager(this);
		// 获取控件
		iv = (ImageView) findViewById(R.id.software_iv_check);
		unInstall = (TextView) findViewById(R.id.tv_uninstall);
		pb = (ProgressBar) findViewById(R.id.app_progress);
		tl = (TitleLayout) findViewById(R.id.app_title);
		// 关联控件
		iv.setOnTouchListener(this);
		unInstall.setOnTouchListener(this);
		//创建广播对象
		appBroadcast = new AppBroadcast();
		//创建意图过滤器
		IntentFilter iFilter = new IntentFilter();
		//添加监听活动
		iFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		//添加读取数据的计划
		iFilter.addDataScheme("package");
		//注册广播
		registerReceiver(appBroadcast, iFilter);
		
		Thread thread = new Thread(this);
		thread.start();
		/**
		 * 获取上个界面的数据
		 */
		Intent intent = getIntent();
		data = intent.getStringExtra("sortApp");
		if (data.equals("all")) {
			tl.setTitle("所有软件");
		}else if (data.equals("system")) {
			tl.setTitle("系统软件");
		}else if (data.equals("user")) {
			tl.setTitle("用户软件");
		}
		// listView的监听事件
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
					// 快速滑动
					sAdapter.fast = true;
				} else if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
					// 慢速滑动
					sAdapter.fast = false;
				} else {
					if (sAdapter.fast = true) {
						sAdapter.fast = false;
						// 刷新
						sAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//注销广播
		unregisterReceiver(appBroadcast);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.software_iv_check) {
			if (event.getAction() == MotionEvent.ACTION_UP) {

				allCheck = !allCheck;
				if (allCheck) {
					sAdapter.isChecked(true);
					iv.setBackgroundResource(R.drawable.check_rect_correct_checked);
				} else {
					sAdapter.isChecked(false);
					iv.setBackgroundResource(R.drawable.check_rect_correct_default);
				}
			}
		}
		if (v.getId() == R.id.tv_uninstall) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				unInstall.setBackgroundResource(R.drawable.speedup_shape_pressed);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				unInstall.setBackgroundResource(R.drawable.speedup_shape_normal);
				unInstalled();
				// Thread thread = new Thread(this);
				// thread.start();
			}
		}

		return true;
	}

	@Override
	public void run() {
		// Log.e("sortApp", data+"----------");
		appInfos = aManager.getApp(data);
		sAdapter = new SoftwareAdapter(appInfos, this);
		
		runOnUiThread(new Runnable() {
			// 在子线程调用主线程
			@Override
			public void run() {
				listView.setAdapter(sAdapter);
				listView.setVisibility(View.INVISIBLE);
				pb.setVisibility(View.INVISIBLE);
				listView.setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * 卸载软件
	 */
	public void unInstalled() {
		for (AppInfo appInfo : appInfos) {
			if (appInfo.isCheck()) {
				// 通过Intent.ACTION_DELETE卸载软件
				Intent intent = new Intent(Intent.ACTION_DELETE);
				// Uri:Uniform resource identifier:统一资源标识符
				Uri data = Uri.parse("package:" + appInfo.getPackageName());
				intent.setData(data);// 当前需要卸载的软件的数据
				startActivity(intent);
			}
		}
	}

	/**
	 * 隐藏listView，显示进度条，开启线程
	 */
	public void update() {
		listView.setVisibility(View.INVISIBLE);
		pb.setVisibility(View.VISIBLE);
		Thread thread = new Thread(this);
		thread.start();
	}
	
}
