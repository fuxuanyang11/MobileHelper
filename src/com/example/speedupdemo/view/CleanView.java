package com.example.speedupdemo.view;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CleanView extends View {

	RectF oval;
	Paint paint;
	private float sweepAngle;
	int temp = -10;
	boolean isRunning = false;// 如果isRunning为true则说明正在运行动画效果

	/**
	 * 
	 * @param context
	 * @param attrs 自定义控件可以显示
	 */
	public CleanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);// 抗锯齿
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawArc(oval, -90, sweepAngle, true, paint);
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
	 * 
	 * @param sweepAngle
	 */
	public void setSweepAngle(float sweepAngle) {
		this.sweepAngle = sweepAngle;
		postInvalidate();// 刷新
	}

	/**
	 * 扇形动画效果
	 * 
	 * @param sweepAngle
	 */
	public void setAnim(final float angle) {
		/**
		 * 只有上一次计时器执行完次才能再次执行下一次计时器
		 */
		if (!isRunning) {
			isRunning = true;
			// 1.建立计时器对象
			final Timer timer = new Timer();
			// 2.新建任务
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					// 3.编写任务
					sweepAngle += temp;
					if (sweepAngle <= 0) {
						temp = -temp;
						sweepAngle = 0;
					}
					if (temp > 0) {
						if (sweepAngle >= angle) {
							sweepAngle = angle;
							// 取消前要改为负数，因为下一次调用还是要先减后加
							temp = -temp;
							isRunning = false;
							timer.cancel();// 结束计时器
						}
					}
					// 刷新
					postInvalidate();
				}
			};
			// timer.schedule(task, when);在指定的日期执行一次任务
			// timer.schedule(task, delay);在间隔一段时间执行一次任务
			// 第三个参数都是每隔多久执行一次
			timer.schedule(task, 0/* 第一次执行的间隔 */, 20/* 下一次执行距离本次执行的时间 */);
		}
	}

}
