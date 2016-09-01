package com.example.speedupdemo.view;

import java.util.Timer;
import java.util.TimerTask;

import com.example.speedupdemo.manager.MemoryManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class SoftwareView extends View{
	RectF oval;
	Paint paint;
	Paint bluePaint;
	float sweepAngle;
	int temp = 10;
	MemoryManager manager;

	public SoftwareView(Context context, AttributeSet attrs) {
		super(context, attrs);
		manager = new MemoryManager();
		paint = new Paint();
		bluePaint = new Paint();
		bluePaint.setColor(Color.BLUE);
		bluePaint.setAntiAlias(true);
		paint.setColor(Color.GREEN);
		paint.setAntiAlias(true);//抗锯齿
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawOval(oval, paint);
		canvas.drawArc(oval, -90, sweepAngle, true, bluePaint);
	}
	
	/**
	 * 测量控件的宽高
	 */
	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		float width = MeasureSpec.getSize(widthMeasureSpec);
		float height = MeasureSpec.getSize(heightMeasureSpec);
		oval = new RectF(0, 0, width, height);
	}
	
	/**
	 * 旋转的角度
	 * @param sweepAngle
	 */
	public void setSweepAngle(float sweepAngle) {
		this.sweepAngle = sweepAngle;
		postInvalidate();
	}
	
	public void setAnim(final float angle) {
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				sweepAngle +=temp;
				if (sweepAngle==angle) {
					timer.cancel();
				}
				postInvalidate();
			}
		};
		timer.schedule(task, 50, 100);
	}
	
	
	
}
