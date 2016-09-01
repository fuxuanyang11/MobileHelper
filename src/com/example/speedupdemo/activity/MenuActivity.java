package com.example.speedupdemo.activity;


import com.example.speedupdemo.manager.AppInfoManager;
import com.example.speedupdemo.manager.MemoryManager;
import com.example.speedupdemo.view.CleanView;
import com.example.speedupdemo.R;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;

public class MenuActivity extends BaseActivity implements OnTouchListener {

	AppInfoManager aManager;
	ImageView iv_bg;
	TextView tv_percent;
	TextView button_sppedUp;
	MemoryManager mManager;
	long used;
	int percent;
	CleanView cv;
	View sppedUp;
	View software;
	View detection;
	View message;
	View filemgr;
	View garbageClear;
	ImageView iv_about;
	ImageView iv_set;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		aManager = new AppInfoManager(this);
		mManager = new MemoryManager();
		// 获取控件
		iv_bg = (ImageView) findViewById(R.id.iv_bg);
		tv_percent = (TextView) findViewById(R.id.tv_scale);
		cv = (CleanView) findViewById(R.id.cv_cleanview);
		button_sppedUp = (TextView) findViewById(R.id.button_speed_up);
		sppedUp = findViewById(R.id.speed_up);
		software = findViewById(R.id.software_manager);
		detection = findViewById(R.id.detection);
		message = findViewById(R.id.message);
		filemgr = findViewById(R.id.file_manager);
		garbageClear = findViewById(R.id.garbage_clear);
		iv_about = (ImageView) findViewById(R.id.about_us);
		iv_set = (ImageView) findViewById(R.id.menu_set);
		// 关联控件
		iv_bg.setOnTouchListener(this);
		tv_percent.setOnTouchListener(this);
		cv.setOnTouchListener(this);
		button_sppedUp.setOnTouchListener(this);
		sppedUp.setOnTouchListener(this);
		software.setOnTouchListener(this);
		detection.setOnTouchListener(this);
		message.setOnTouchListener(this);
		filemgr.setOnTouchListener(this);
		garbageClear.setOnTouchListener(this);
		iv_about.setOnTouchListener(this);
		iv_set.setOnTouchListener(this);
		// 获取百分比
		getPercent();
		tv_percent.setText(percent + "");
		cv.setSweepAngle(360);
		cv.setAnim((float) (percent * 3.6));
//		cv.setSweepAngle((float) (percent * 3.6));
		

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (v.getId() == R.id.iv_bg || v.getId() == R.id.button_speed_up) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				iv_bg.setImageResource(R.drawable.home_score_pressed_bg);
				button_sppedUp.setBackgroundResource(R.drawable.home_speedup2);
				// 杀死后台进程
				try {
					aManager.killAllProcess();
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				iv_bg.setImageResource(R.drawable.home_score_normal_bg);
				button_sppedUp.setBackgroundResource(R.drawable.home_speedup1);
				// cv.setSweepAngle((float) (percent*3.6));
				getPercent();
				// 动画效果
				cv.setAnim((float) (percent * 3.6));
				tv_percent.setText(percent + "");
			}
		}
		if (v.getId() == R.id.speed_up) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				sppedUp.setBackgroundResource(R.drawable.home_entry_item_pressed);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				sppedUp.setBackgroundResource(R.drawable.home_entry_item_normal);
				Intent toSpeedup = new Intent(this, SpeedUpActiivity.class);
				startActivity(toSpeedup);
				//设置跳转的形式
				overridePendingTransition(R.anim.menu_in, R.anim.menu_out);
			}
		}if (v.getId()==R.id.software_manager) {
			if (event.getAction()==MotionEvent.ACTION_DOWN) {
				software.setBackgroundResource(R.drawable.home_entry_item_pressed);
			}if (event.getAction() == MotionEvent.ACTION_UP) {
				software.setBackgroundResource(R.drawable.home_entry_item_normal);
				Intent toSoftware = new Intent(this, SoftwareManagerActivity.class);
				startActivity(toSoftware);
				//设置跳转的形式
				overridePendingTransition(R.anim.menu_in, R.anim.menu_out);
			}
		}
		if (v.getId()==R.id.detection) {
			if (event.getAction()==MotionEvent.ACTION_DOWN) {
				detection.setBackgroundResource(R.drawable.home_entry_item_pressed);
			}
			if (event.getAction()==MotionEvent.ACTION_UP) {
				detection.setBackgroundResource(R.drawable.home_entry_item_normal);
				Intent todetection = new Intent(this,DetectionActivity.class);
				startActivity(todetection);
				//设置跳转的形式
				overridePendingTransition(R.anim.menu_in, R.anim.menu_out);
			}
		}
		if (v.getId()==R.id.message) {
			if (event.getAction()==MotionEvent.ACTION_DOWN) {
				message.setBackgroundResource(R.drawable.home_entry_item_pressed);
			}
			if (event.getAction()==MotionEvent.ACTION_UP) {
				message.setBackgroundResource(R.drawable.home_entry_item_normal);
				Intent toTelmgr = new Intent(this,TelmgrActivity.class);
				startActivity(toTelmgr);
				//设置跳转的形式
				overridePendingTransition(R.anim.menu_in, R.anim.menu_out);
			}
		}
		if (v.getId()==R.id.file_manager) {
			if (event.getAction()==MotionEvent.ACTION_DOWN) {
				filemgr.setBackgroundResource(R.drawable.home_entry_item_pressed);
			}
			if (event.getAction()==MotionEvent.ACTION_UP) {
				filemgr.setBackgroundResource(R.drawable.home_entry_item_normal);
				Intent toFilemgr = new Intent(this,FilemgrActivity.class);
				startActivity(toFilemgr);
				//设置跳转的形式
				overridePendingTransition(R.anim.menu_in, R.anim.menu_out);
			}
		}
		if (v.getId()==R.id.garbage_clear) {
			if (event.getAction()==MotionEvent.ACTION_DOWN) {
				garbageClear.setBackgroundResource(R.drawable.home_entry_item_pressed);
			}
			if (event.getAction()==MotionEvent.ACTION_UP) {
				garbageClear.setBackgroundResource(R.drawable.home_entry_item_normal);
				Intent toClear = new Intent(this,GarbageClearActivity.class);
				startActivity(toClear);
				//设置跳转的形式
				overridePendingTransition(R.anim.menu_in, R.anim.menu_out);
			}
		}
		if (v.getId()==R.id.about_us) {
			if (event.getAction()==MotionEvent.ACTION_DOWN) {
				iv_about.setBackgroundResource(R.drawable.home_entry_item_pressed);
			}
			if (event.getAction()==MotionEvent.ACTION_UP) {
				iv_about.setBackgroundResource(R.drawable.home_entry_item_normal);
				Intent toAbout = new Intent(this,AboutUsActivity.class);
				startActivity(toAbout);
				//设置跳转的形式
				overridePendingTransition(R.anim.menu_in, R.anim.menu_out);
			}
		}
		if (v.getId()==R.id.menu_set) {
			if (event.getAction()==MotionEvent.ACTION_DOWN) {
				iv_set.setBackgroundResource(R.drawable.home_entry_item_pressed);
			}
			if (event.getAction()==MotionEvent.ACTION_UP) {
				iv_set.setBackgroundResource(R.drawable.home_entry_item_normal);
				Intent toSet = new Intent(this,SetActivity.class);
				startActivity(toSet);
				//设置跳转的形式
				overridePendingTransition(R.anim.menu_in, R.anim.menu_out);
			}
		}
		return true;
	}

	/**
	 * 获取百分比
	 * 
	 * @return
	 */
	public int getPercent() {
		// 获取后台进程
		aManager.getBackProcess();
		// 使用的内存
		used = MemoryManager.getMemorySize(true, this)
				- MemoryManager.getMemorySize(false, this);
		long AllmemorySize = MemoryManager.getMemorySize(true, this);
		double percentf = 1.0 * used / AllmemorySize;
		percent = (int) Math.round(percentf * 100);
		return percent;

	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.tomenu_in, R.anim.tomenu_out);
	}

}
