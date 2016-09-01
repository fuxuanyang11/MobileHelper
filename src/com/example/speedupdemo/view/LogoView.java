package com.example.speedupdemo.view;

import com.example.speedupdemo.R;
import com.example.speedupdemo.activity.BaseActivity;
import com.example.speedupdemo.activity.LogoActivity;
import com.example.speedupdemo.activity.MenuActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class LogoView extends View implements Runnable{
	Bitmap logo;
	Bitmap bg;
	LogoActivity la;

	public LogoView(Context context) {
		super(context);
		//新建logoActivity对象
		la = (LogoActivity) context;
		logo = BitmapFactory
				.decodeResource(getResources(), R.drawable.androidy);
		bg = BitmapFactory.decodeResource(getResources(), R.drawable.home_entry_item_normal);
		//拉伸图片
		bg = Bitmap.createScaledBitmap(bg, BaseActivity.SCREEN_WIDTH, BaseActivity.SCREEN_HEIGHT, true);
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bg, 0, 0, null);
		canvas.drawBitmap(logo, BaseActivity.SCREEN_WIDTH / 2 - logo.getWidth()
				/ 2, BaseActivity.SCREEN_HEIGHT / 2 - logo.getHeight() / 2,
				null);
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//跳转
		Intent toMenuActivity = new Intent(la,MenuActivity.class);
		la.startActivity(toMenuActivity);
		la.finish();
	}

}
