package com.example.speedupdemo.broadcast;

import com.example.speedupdemo.activity.DetectionActivity;
import com.example.speedupdemo.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BatteryBroadcast extends BroadcastReceiver {

	ProgressBar pb;
	TextView tv;
	public int level;
	public int temperature;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		level = extras.getInt(BatteryManager.EXTRA_LEVEL);// 当前电量
		int scale = extras.getInt(BatteryManager.EXTRA_SCALE);// 总电量
		temperature = extras.getInt(BatteryManager.EXTRA_TEMPERATURE/* 电池温度 */);
		DetectionActivity dActivity = (DetectionActivity) context;
		pb = (ProgressBar) dActivity.findViewById(R.id.detection_progress);
		tv = (TextView) dActivity.findViewById(R.id.detection_text);
		pb.setMax(scale);
		pb.setProgress(level);
		int percent = (int) (1.0 * level / scale * 100);
		tv.setText(percent + "%");
	}

}
