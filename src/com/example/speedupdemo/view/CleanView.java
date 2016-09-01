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
	boolean isRunning = false;// ���isRunningΪtrue��˵���������ж���Ч��

	/**
	 * 
	 * @param context
	 * @param attrs �Զ���ؼ�������ʾ
	 */
	public CleanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);// �����
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawArc(oval, -90, sweepAngle, true, paint);
	}

	/**
	 * �����ؼ��Ŀ��
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
	 * ��ת�ĽǶ�
	 * 
	 * @param sweepAngle
	 */
	public void setSweepAngle(float sweepAngle) {
		this.sweepAngle = sweepAngle;
		postInvalidate();// ˢ��
	}

	/**
	 * ���ζ���Ч��
	 * 
	 * @param sweepAngle
	 */
	public void setAnim(final float angle) {
		/**
		 * ֻ����һ�μ�ʱ��ִ����β����ٴ�ִ����һ�μ�ʱ��
		 */
		if (!isRunning) {
			isRunning = true;
			// 1.������ʱ������
			final Timer timer = new Timer();
			// 2.�½�����
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					// 3.��д����
					sweepAngle += temp;
					if (sweepAngle <= 0) {
						temp = -temp;
						sweepAngle = 0;
					}
					if (temp > 0) {
						if (sweepAngle >= angle) {
							sweepAngle = angle;
							// ȡ��ǰҪ��Ϊ��������Ϊ��һ�ε��û���Ҫ�ȼ����
							temp = -temp;
							isRunning = false;
							timer.cancel();// ������ʱ��
						}
					}
					// ˢ��
					postInvalidate();
				}
			};
			// timer.schedule(task, when);��ָ��������ִ��һ������
			// timer.schedule(task, delay);�ڼ��һ��ʱ��ִ��һ������
			// ��������������ÿ�����ִ��һ��
			timer.schedule(task, 0/* ��һ��ִ�еļ�� */, 20/* ��һ��ִ�о��뱾��ִ�е�ʱ�� */);
		}
	}

}
