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
	// TODO ȫ�ֱ���
	AppInfoManager aManager;
	ListView listView;
	SoftwareAdapter sAdapter;
	ImageView iv;
	boolean allCheck;
	String data;// �ϸ������ȡ������
	ArrayList<AppInfo> appInfos;
	TextView unInstall;
	ProgressBar pb;
	TitleLayout tl;
	//�����㲥����
	AppBroadcast appBroadcast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.software_all);
		listView = (ListView) findViewById(R.id.software_listview);
		aManager = new AppInfoManager(this);
		// ��ȡ�ؼ�
		iv = (ImageView) findViewById(R.id.software_iv_check);
		unInstall = (TextView) findViewById(R.id.tv_uninstall);
		pb = (ProgressBar) findViewById(R.id.app_progress);
		tl = (TitleLayout) findViewById(R.id.app_title);
		// �����ؼ�
		iv.setOnTouchListener(this);
		unInstall.setOnTouchListener(this);
		//�����㲥����
		appBroadcast = new AppBroadcast();
		//������ͼ������
		IntentFilter iFilter = new IntentFilter();
		//��Ӽ����
		iFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		//��Ӷ�ȡ���ݵļƻ�
		iFilter.addDataScheme("package");
		//ע��㲥
		registerReceiver(appBroadcast, iFilter);
		
		Thread thread = new Thread(this);
		thread.start();
		/**
		 * ��ȡ�ϸ����������
		 */
		Intent intent = getIntent();
		data = intent.getStringExtra("sortApp");
		if (data.equals("all")) {
			tl.setTitle("�������");
		}else if (data.equals("system")) {
			tl.setTitle("ϵͳ���");
		}else if (data.equals("user")) {
			tl.setTitle("�û����");
		}
		// listView�ļ����¼�
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
					// ���ٻ���
					sAdapter.fast = true;
				} else if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
					// ���ٻ���
					sAdapter.fast = false;
				} else {
					if (sAdapter.fast = true) {
						sAdapter.fast = false;
						// ˢ��
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
		//ע���㲥
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
			// �����̵߳������߳�
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
	 * ж�����
	 */
	public void unInstalled() {
		for (AppInfo appInfo : appInfos) {
			if (appInfo.isCheck()) {
				// ͨ��Intent.ACTION_DELETEж�����
				Intent intent = new Intent(Intent.ACTION_DELETE);
				// Uri:Uniform resource identifier:ͳһ��Դ��ʶ��
				Uri data = Uri.parse("package:" + appInfo.getPackageName());
				intent.setData(data);// ��ǰ��Ҫж�ص����������
				startActivity(intent);
			}
		}
	}

	/**
	 * ����listView����ʾ�������������߳�
	 */
	public void update() {
		listView.setVisibility(View.INVISIBLE);
		pb.setVisibility(View.VISIBLE);
		Thread thread = new Thread(this);
		thread.start();
	}
	
}
