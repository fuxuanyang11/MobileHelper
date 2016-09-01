package com.example.speedupdemo.broadcast;

import com.example.speedupdemo.NotificationUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CancleBroadcast extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationUtil.cancle(context);
	}

}
