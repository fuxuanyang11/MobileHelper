package com.example.speedupdemo.broadcast;

import com.example.speedupdemo.activity.AppActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * �㲥�����ߣ����Զ�����onReceive����
 * @author ������
 *
 */
public class AppBroadcast extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		AppActivity app = (AppActivity) context;
		app.update();
	}

}
