package com.example.speedupdemo.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;

public class ActivityCollects {
	
	public static List<Activity> activities = new ArrayList<Activity>();
	
	/**
	 * 添加活动
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		activities.add(activity);
	}
	
	/**
	 * 移除活动
	 * @param activity
	 */
	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}
	
	/**
	 * 结束所有活动
	 */
	public static void finishAllActivity() {
//		for (Activity activity : activities) {
//			if (!activity.isFinishing()) {//记得要加这个判断
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
