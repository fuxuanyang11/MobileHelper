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
	 * ����֪ͨ
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

		// ���֪ͨ��ת
		Intent intent = new Intent(context, MenuActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// notification.contentIntent = PendingIntent.getActivity(context, 0,
		// new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

		// ������ͼ
		notification.icon = R.drawable.azy;// ͼ��
		notification.tickerText = "֪ͨ";
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.notification_layout);
		//�����еĵ���¼�
		remoteViews
				.setOnClickPendingIntent(R.id.notification_in, pendingIntent);
		Intent toBroadcast = new Intent(context, CancleBroadcast.class);
		PendingIntent out = PendingIntent.getBroadcast(context, 0, toBroadcast,
				PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.notification_out, out);
		//�ǵý���Ϣ����ͼ��ֵ
		notification.contentView = remoteViews;
		//����
		manager.notify(0, notification);
		SharedUtil util = new SharedUtil(context, "data");
//		String value = util.getValue(SetAdapter.strs[1], false+"");
//		boolean isCheck = Boolean.parseBoolean(value);
//		isCheck = true;
//		util.putValue(SetAdapter.strs[1], false+"");
		//Ҫ�ٶ�һ���������
		util.putValue("in", false+"");
	}
	
	/**
	 * ȡ��֪ͨ
	 */
	public static void cancle(Context context) {
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(0);
		SharedUtil util = new SharedUtil(context, "data");
//		util.putValue(SetAdapter.strs[1], true+"");
		util.putValue("in", true+"");
	}
}
