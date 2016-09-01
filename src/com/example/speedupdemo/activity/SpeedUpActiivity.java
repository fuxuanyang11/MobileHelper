package com.example.speedupdemo.activity;

import java.util.ArrayList;
import java.util.Map;

import com.example.speedupdemo.adapter.SpeedUpAdapter;
import com.example.speedupdemo.info.AppInfo;
import com.example.speedupdemo.manager.AppInfoManager;
import com.example.speedupdemo.manager.MemoryManager;
import com.example.speedupdemo.view.TitleLayout;
import com.example.speedupdemo.R;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 不要在onCreate获取系统进程，要新建一个子线程获取。获取系统进程属于耗时操作
 * 
 */
public class SpeedUpActiivity extends BaseActivity implements OnTouchListener,
		Runnable {
	// ArrayList<AppInfo> infos;
	// 进程集合
	Map<String, ArrayList<AppInfo>> map;
	// 系统进程集合
	ArrayList<AppInfo> systemApp;
	// 第三方进程集合
	ArrayList<AppInfo> userApp;
	SpeedUpAdapter myAdapter;
	// 是否显示系统进程
	boolean showSystem;
	// 是否全部勾选
	boolean allCheck;
	TextView tv;
	TextView tv_percent;
	TextView tv_clean;
	ImageView iv;
	AppInfoManager aManager;
	ListView listView;
	ProgressBar pBar2;
	TextView version;

	// boolean showList;

	// 全局变量是没有构造方法就会先创建，这时候使用this并没有当前类的对象，所以新建对象最好放在构造方法中
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speed_up);
		Thread thread = new Thread(this);
		thread.start();
		aManager = new AppInfoManager(this);
		// 记得获取ArrayList<AppInfo>的集合
		// 获取控件
		listView = (ListView) findViewById(R.id.speedup_list_view);
		tv = (TextView) findViewById(R.id.tv_showsystem);
		tv_percent = (TextView) findViewById(R.id.speedup_tv_percent);
		iv = (ImageView) findViewById(R.id.speedup_iv_check);
		ProgressBar pBar = (ProgressBar) findViewById(R.id.speedup_progress);
		pBar2 = (ProgressBar) findViewById(R.id.speedup_progress_nomal);
		tv_clean = (TextView) findViewById(R.id.speedup_tv_clean);
		version = (TextView) findViewById(R.id.speedup_version_text);
		TitleLayout tl = (TitleLayout) findViewById(R.id.speedup_title);
		tl.setTitle("手机加速");
		// 计算所占内比重
		long used = MemoryManager.getMemorySize(true, this)
				- MemoryManager.getMemorySize(false, this);
		double percentf = 1.0 * used / MemoryManager.getMemorySize(true, this);
		int percent = (int) Math.round(percentf * 100);
		pBar.setProgress(percent);
		// 关联控件
		tv.setOnTouchListener(this);
		iv.setOnTouchListener(this);
		tv_clean.setOnTouchListener(this);
		// 设置控件
		tv.setText("显示系统进程");
		// 使用Formatter换算内存
		String usedSize = Formatter.formatFileSize(this, used);
		String allSize = Formatter.formatFileSize(this,
				MemoryManager.getMemorySize(true, this));
		// float used2 = (float) (1.0*used/1000000);
		// float all = (float) (1.0*manager.getMemorySize(true, this)/1000000);
		tv_percent.setText("已用内存" + usedSize + "/" + allSize);
		tv_percent.setTextColor(Color.BLUE);
		version.setText(Build.MODEL + " Android" + Build.VERSION.RELEASE);

		// 设置listView滑动的监听事件
		listView.setOnScrollListener(new OnScrollListener() {

			/**
			 * 滑动过程中状态的改变就会调用此方法
			 * 
			 * @parm view-->正在监听的listView
			 * @parm scrollState-->状态
			 */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
					// 快速滑动
					myAdapter.fast = true;
				} else if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
					// 慢速滑动,滑动就会调用getView的方法，所以不用刷新
					myAdapter.fast = false;
				} else if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 静止，不会调用需要刷新
					if (myAdapter.fast) {
						myAdapter.fast = false;
						// 刷新
						myAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

		// showList = true;//子线程有耗时操作，主线程肯定会比子线程先执行
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 点击改变显示的进程
		if (v.getId() == R.id.tv_showsystem) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				tv.setBackgroundResource(R.drawable.speedup_shape_pressed);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				tv.setBackgroundResource(R.drawable.speedup_shape_normal);
				showSystem = !showSystem;
				if (showSystem) {
					myAdapter.showSystem(systemApp);
					tv.setText("只显示用户进程");

				} else {
					myAdapter.removeSystem(systemApp);
					tv.setText("显示系统进程");
				}
			}
			// 勾选
		}
		if (v.getId() == R.id.speedup_iv_check) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				allCheck = !allCheck;
				if (allCheck) {
					iv.setImageResource(R.drawable.check_rect_correct_checked);
					if (!showSystem) {
						myAdapter.allTrue(userApp);
					} else {
						myAdapter.allTrue(systemApp);
						myAdapter.allTrue(userApp);
					}
				} else {
					iv.setImageResource(R.drawable.check_rect_correct_default);
					if (!showSystem) {
						myAdapter.allFalse(userApp);
					} else {
						myAdapter.allFalse(systemApp);
						myAdapter.allFalse(userApp);
					}
				}
			}
		}
		if (v.getId() == R.id.speedup_tv_clean) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				tv_clean.setBackgroundResource(R.drawable.speedup_shape_pressed);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				tv_clean.setBackgroundResource(R.drawable.speedup_shape_normal);
				myAdapter.removeCheck(userApp);
				myAdapter.removeCheck(systemApp);
				listView.setVisibility(View.INVISIBLE);
				pBar2.setVisibility(View.VISIBLE);
				// 新建一个线程，重新执行run方法
				Thread thread2 = new Thread(this);
				thread2.start();
			}
		}

		return true;
	}

	/**
	 * 子线程
	 */
	@Override
	public void run() {
		map = aManager.getBackProcess();
		systemApp = map.get("system");
		userApp = map.get("user");
		myAdapter = new SpeedUpAdapter(userApp, this);
		myAdapter.allTrue(userApp);
		// listView.setAdapter(myAdapter);//只要会改变界面的操作（UI操作）只能在主线程中执行。
		// 能够在子线程中唤醒主线程
		runOnUiThread(new Runnable() {// 匿名内部类
			// 运行在主线程的run方法
			@Override
			public void run() {
				// 先把listview隐藏，显示进度条，listview生成完成，就把进度条隐藏
				listView.setAdapter(myAdapter);
				listView.setVisibility(View.INVISIBLE);
				pBar2.setVisibility(View.INVISIBLE);
				listView.setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * 设置按下返回键的动画效果
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.tomenu_in, R.anim.tomenu_out);
	}
}
