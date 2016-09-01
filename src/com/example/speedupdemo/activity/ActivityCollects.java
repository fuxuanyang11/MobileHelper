package com.example.speedupdemo.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;

public class ActivityCollects {
	
	public static List<Activity> activities = new ArrayList<Activity>();
	
	/**
	 * ��ӻ
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		activities.add(activity);
	}
	
	/**
	 * �Ƴ��
	 * @param activity
	 */
	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}
	
	/**
	 * �������л
	 */
	public static void finishAllActivity() {
//		for (Activity activity : activities) {
//			if (!activity.isFinishing()) {//�ǵ�Ҫ������ж�
//				activity.finish();
//			}
//		}
		
		Iterator<Activity> iterator = activities.iterator();
		while (iterator.hasNext()) {
			Activity next = iterator.next();
			next.finish();
		}
	}
}
