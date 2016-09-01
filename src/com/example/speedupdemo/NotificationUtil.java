package com.example.speedupdemo;

import com.example.speedupdemo.activity.MenuActivity;
import com.example.speedupdemo.broadcast.CancleBroadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

public class NotificationUtil {
	
	/**
	 * 发送通知
	 * @param context
	 */
	public static void show(Context context) {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.defaults = Notification.DEFAULT_ALL;
		notification.ledARGB = Color.RED;
		notification.ledOnMS = 1000;
		notification.ledOffMS = 1000;
		notification.flags = Notification.FLAG_NO_CLEAR;

		// 点击通知跳转
		Intent intent = new Intent(context, MenuActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// notification.contentIntent = PendingIntent.getActivity(context, 0,
		// new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

		// 设置视图
		notification.icon = R.drawable.azy;// 图标
		notification.tickerText = "通知";
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.notification_layout);
		//布局中的点击事件
		remoteViews
				.setOnClickPendingIntent(R.id.notification_in, pendingIntent);
		Intent toBroadcast = new Intent(context, CancleBroadcast.class);
		PendingIntent out = PendingIntent.getBroadcast(context, 0, toBroadcast,
				PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.notification_out, out);
		//记得将消息的视图赋值
		notification.contentView = remoteViews;
		//发送
		manager.notify(0, notification);
		SharedUtil util = new SharedUtil(context, "data");
//		String value = util.getValue(SetAdapter.strs[1], false+"");
//		boolean isCheck = Boolean.parseBoolean(value);
//		isCheck = true;
//		util.putValue(SetAdapter.strs[1], false+"");
		//要再多一条共享参数
		util.putValue("in", false+"");
	}
	
	/**
	 * 取消通知
	 */
	public static void cancle(Context context) {
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(0);
		SharedUtil util = new SharedUtil(context, "data");
//		util.putValue(SetAdapter.strs[1], true+"");
		util.putValue("in", true+"");
	}
}
