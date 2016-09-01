package com.example.speedupdemo.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class BaseActivity extends Activity{
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager manager = getWindowManager();
		Display display = manager.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		SCREEN_WIDTH = dm.widthPixels;
		SCREEN_HEIGHT = dm.heightPixels;
		ActivityCollects.addActivity(this);//方法里面的this是调用该方法的类的this
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollects.removeActivity(this);
	}
	
	/**
	 * 跳转的方法
	 */
	public void startAction(@SuppressWarnings("rawtypes") Class cls,String name,String value) {
		Intent intent = new Intent(this, cls);
		intent.putExtra(name, value);
		startActivity(intent);
	}
	
	/**
	 * 收取上个界面的的数据
	 */
	public void startResultAction(@SuppressWarnings("rawtypes") Class c,String name,String value) {
		Intent intent = new Intent(this, c);
		intent.putExtra(name,value);
		startActivityForResult(intent, 0/*请求码，大于等于0*/);
	}
}

