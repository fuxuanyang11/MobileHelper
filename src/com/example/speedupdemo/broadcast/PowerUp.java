package com.example.speedupdemo.broadcast;

import com.example.speedupdemo.NotificationUtil;
import com.example.speedupdemo.SharedUtil;
import com.example.speedupdemo.adapter.SetAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * ���������Ĺ㲥
 * @author ������
 *
 */
public class PowerUp extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedUtil util = new SharedUtil(context, "data");
		String value = util.getValue(SetAdapter.strs[0], false+"");
		boolean isOpen = Boolean.parseBoolean(value);
		if (isOpen) {
			NotificationUtil.show(context);
		}
	}

}
