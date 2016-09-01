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
		//��ȡ�ؼ�
		allApp = findViewById(R.id.manager_allapp);
		systemApp = findViewById(R.id.manager_systemapp);
		userApp = findViewById(R.id.manager_userapp);
		sv = (SoftwareView) findViewById(R.id.software_view);
		pb1 = (ProgressBar) findViewById(R.id.software_progress);
		pb2 = (ProgressBar) findViewById(R.id.software_progress2);
		tv1 = (TextView) findViewById(R.id.software_tv);
		tv2 = (TextView) findViewById(R.id.software_tv2);
		TitleLayout tl = (TitleLayout) findViewById(R.id.software_title);
		tl.setTitle("�������");
		//�����ؼ�
		allApp.setOnTouchListener(this);
		systemApp.setOnTouchListener(this);
		userApp.setOnTouchListener(this);
		//���ö���
		long outSDSize = MemoryManager.getPhoneSDCardSize(true, false);//����SD����ȫ���ڴ�
		if (outSDSize==0) {
			defaultToast();
		}
		long inSDSize = MemoryManager.getPhoneSDCardSize(false, false);//����SD����ȫ���ڴ�
		long inUnUseSDSize = MemoryManager.getPhoneSDCardSize(false, true);//����SD���Ŀ����ڴ�
		long outUnUseSDSize = MemoryManager.getPhoneSDCardSize(true, true);//����SD���Ŀ����ڴ�
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
		//���ý�����
		pb1.setProgress((int)Math.ceil(percent));
		tv1.setText("����:"+inUnUseSDSizes+"/"+inSDSizes);
		pb2.setProgress((int)Math.ceil(percent2));
		tv2.setText("����:"+outUnUseSDSizes+"/"+outSDSizes);
		
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
	 * ��ת����
	 * @param context
	 * @param data
	 */
	public static void startAction(Context context, String data) {
		Intent intent = new Intent(context, AppActivity.class);
		intent.putExtra("sortApp", data);
		context.startActivity(intent);
	}
	
	/**
	 * �������SD���ڴ�Ϊ0��ʾToast
	 */
	public void defaultToast() {
		Toast.makeText(this, "���ô洢���쳣", Toast.LENGTH_SHORT).show();
	}
}
