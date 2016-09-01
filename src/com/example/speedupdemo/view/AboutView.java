package com.example.speedupdemo.view;

import com.example.speedupdemo.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class AboutView extends View {
	float width;
	float height;

	Bitmap bitmap;

	public AboutView(Context context, AttributeSet attrs) {
		super(context, attrs);

		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.androidy);
		bitmap = Bitmap.createScaledBitmap(bitmap, 3 * bitmap.getWidth(),
				3 * bitmap.getHeight(), true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, width / 2 - bitmap.getWidth() / 2, height-bitmap.getHeight(), null);
	}
}
