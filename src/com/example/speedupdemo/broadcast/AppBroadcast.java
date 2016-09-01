package com.example.speedupdemo.broadcast;

import com.example.speedupdemo.activity.AppActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 广播接收者，会自动调用onReceive方法
 * @author 傅炫阳
 *
 */
public class AppBroadcast extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		AppActivity app = (AppActivity) context;
		app.update();
	}

}
