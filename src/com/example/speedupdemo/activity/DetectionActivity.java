package com.example.speedupdemo.activity;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.regex.Pattern;

import com.example.speedupdemo.broadcast.BatteryBroadcast;
import com.example.speedupdemo.manager.MemoryManager;
import com.example.speedupdemo.view.TitleLayout;
import com.example.speedupdemo.R;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetectionActivity extends BaseActivity implements OnTouchListener {

	ProgressBar pb;
	BatteryBroadcast broadcast;
	TitleLayout layout;
	View power;
	String[] strs = { "model", "version", "allmemory", "spacememory",
			"cpuname", "cpunumber", "mobile", "camera", "baseband", "root" };
	TextView[] tvs = new TextView[10];
	MemoryManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detection_layout);
		// 获取内存管理对象
		manager = new MemoryManager();
		// 获取控件
		pb = (ProgressBar) findViewById(R.id.detection_progress);
		layout = (TitleLayout) findViewById(R.id.detection_title);
		power = findViewById(R.id.detection_power);

		for (int i = 0; i < tvs.length; i++) {
			String name = strs[i] + "_text";
			int id = getResources().getIdentifier(name, "id", getPackageName());
			tvs[i] = (TextView) findViewById(id);
		}

		tvs[0].setText("设备名称:" + Build.MODEL);
		tvs[1].setText("系统版本:" + Build.VERSION.RELEASE);
		String allSize = Formatter.formatFileSize(this,
				MemoryManager.getMemorySize(true, this));
		String spcaeSize = Formatter.formatFileSize(this,
				MemoryManager.getMemorySize(false, this));
		tvs[2].setText("全部运行内存:"+ allSize);
//		Log.e("Detection", spcaeSize);
		tvs[3].setText("剩余运行内存:" + spcaeSize);//??会报错，layout中id写错了！
		tvs[4].setText("cpu名称:"+Build.CPU_ABI);
		tvs[5].setText("cpu数量:"+getCPUNumber());
		tvs[6].setText("手机分辨率"+getResolusion());
		tvs[7].setText("相机分辨率"+getCameraResolusion());
		tvs[8].setText("基带版本:"+getBaseband());
		tvs[9].setText("是否ROOT:"+isRoot());

		layout.setTitle("手机检测");
		// 设置监听事件
		power.setOnTouchListener(this);
		// 注册
		broadcast = new BatteryBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(broadcast, filter);

	}

	/**
	 * Dialog对话框
	 */
	public void itemsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("电池电量信息");
		String[] items = { "当前电量:" + broadcast.level,
				"电池温度:" + broadcast.temperature };
		builder.setItems(items, null);
		builder.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销注册
		unregisterReceiver(broadcast);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.detection_power) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				power.setBackgroundResource(R.drawable.home_entry_item_pressed);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				power.setBackgroundResource(R.drawable.home_entry_item_normal);
				itemsDialog();
			}
		}

		return true;
	}

	/**
	 * 获取CPU的数量
	 */
	public int getCPUNumber() {
		File file = new File("/sys/devices/system/cpu");
		File[] files = file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
					return true;
				} else {
					return false;
				}
			}
		});
		return files.length;
	}

	/**
	 * 获取基带版本
	 */
	@SuppressWarnings("deprecation")
	public String getBaseband() {
		String baseband = null;
		if (Build.VERSION.SDK_INT >= 14) {
			baseband = Build.getRadioVersion();
		} else {
			baseband = Build.RADIO;
		}
		return baseband;
	}
	
	/**
	 * 是否Root
	 */
	public String isRoot() {
//		boolean isRoot = false;
		File f1 = new File("/system/bin/su");
		File f2 = new File("/system/xbin/su");
		if (f1.exists()||f2.exists()) {
			return "是";
		}
		return "否";
	}
	
	/**
	 * 获取手机分辨率
	 */
	public String getResolusion() {
		String resolusion = "";
		DisplayMetrics dm =new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		resolusion = dm.widthPixels+"X"+dm.heightPixels;
		return resolusion;
	}
	
	/**
	 * 获取相机分辨率
	 */
	public String getCameraResolusion() {
		Camera camera = Camera.open();//开启相机
		String sizeStr = "";
		if (camera!=null) {
			Parameters parameters = camera.getParameters();//获取相机属性
			List<Size> sizes = parameters.getSupportedPictureSizes();//获取相机支持的尺寸
			Size maxSize = sizes.get(0);
			for (Size size : sizes) {
				if (maxSize.width*maxSize.height<size.width*size.height) {
					maxSize = size;
				}
			}
			sizeStr = sizeStr+maxSize.width+"X"+maxSize.height;
			//释放相机
			camera.release();
		}
		return sizeStr;
	}

}
