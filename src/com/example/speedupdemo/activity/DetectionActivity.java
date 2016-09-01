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
		// ��ȡ�ڴ�������
		manager = new MemoryManager();
		// ��ȡ�ؼ�
		pb = (ProgressBar) findViewById(R.id.detection_progress);
		layout = (TitleLayout) findViewById(R.id.detection_title);
		power = findViewById(R.id.detection_power);

		for (int i = 0; i < tvs.length; i++) {
			String name = strs[i] + "_text";
			int id = getResources().getIdentifier(name, "id", getPackageName());
			tvs[i] = (TextView) findViewById(id);
		}

		tvs[0].setText("�豸����:" + Build.MODEL);
		tvs[1].setText("ϵͳ�汾:" + Build.VERSION.RELEASE);
		String allSize = Formatter.formatFileSize(this,
				MemoryManager.getMemorySize(true, this));
		String spcaeSize = Formatter.formatFileSize(this,
				MemoryManager.getMemorySize(false, this));
		tvs[2].setText("ȫ�������ڴ�:"+ allSize);
//		Log.e("Detection", spcaeSize);
		tvs[3].setText("ʣ�������ڴ�:" + spcaeSize);//??�ᱨ��layout��idд���ˣ�
		tvs[4].setText("cpu����:"+Build.CPU_ABI);
		tvs[5].setText("cpu����:"+getCPUNumber());
		tvs[6].setText("�ֻ��ֱ���"+getResolusion());
		tvs[7].setText("����ֱ���"+getCameraResolusion());
		tvs[8].setText("�����汾:"+getBaseband());
		tvs[9].setText("�Ƿ�ROOT:"+isRoot());

		layout.setTitle("�ֻ����");
		// ���ü����¼�
		power.setOnTouchListener(this);
		// ע��
		broadcast = new BatteryBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(broadcast, filter);

	}

	/**
	 * Dialog�Ի���
	 */
	public void itemsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��ص�����Ϣ");
		String[] items = { "��ǰ����:" + broadcast.level,
				"����¶�:" + broadcast.temperature };
		builder.setItems(items, null);
		builder.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ע��ע��
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
	 * ��ȡCPU������
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
	 * ��ȡ�����汾
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
	 * �Ƿ�Root
	 */
	public String isRoot() {
//		boolean isRoot = false;
		File f1 = new File("/system/bin/su");
		File f2 = new File("/system/xbin/su");
		if (f1.exists()||f2.exists()) {
			return "��";
		}
		return "��";
	}
	
	/**
	 * ��ȡ�ֻ��ֱ���
	 */
	public String getResolusion() {
		String resolusion = "";
		DisplayMetrics dm =new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		resolusion = dm.widthPixels+"X"+dm.heightPixels;
		return resolusion;
	}
	
	/**
	 * ��ȡ����ֱ���
	 */
	public String getCameraResolusion() {
		Camera camera = Camera.open();//�������
		String sizeStr = "";
		if (camera!=null) {
			Parameters parameters = camera.getParameters();//��ȡ�������
			List<Size> sizes = parameters.getSupportedPictureSizes();//��ȡ���֧�ֵĳߴ�
			Size maxSize = sizes.get(0);
			for (Size size : sizes) {
				if (maxSize.width*maxSize.height<size.width*size.height) {
					maxSize = size;
				}
			}
			sizeStr = sizeStr+maxSize.width+"X"+maxSize.height;
			//�ͷ����
			camera.release();
		}
		return sizeStr;
	}

}
