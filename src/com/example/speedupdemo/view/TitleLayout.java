package com.example.speedupdemo.view;

import com.example.speedupdemo.activity.BaseActivity;
import com.example.speedupdemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

//Ϊʲô����ʵ��onTouchListener�ӿ� 
public class TitleLayout extends LinearLayout {

	TextView tv;
	View back;

	public TitleLayout(final Context context, AttributeSet attrs) {
		super(context, attrs);
		/**
		 * �ڶ�������Ϊ�����غõĲ��������һ��������,ָ��TitleLayout���Դ���this
		 */
		// LayoutInflater.from(context).inflate(R.layout.title, this);
		inflate(context, R.layout.title, this);
		back = findViewById(R.id.title_back);
		tv = (TextView) findViewById(R.id.title_text);
		back.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (v.getId() == R.id.title_back) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						back.setBackgroundResource(R.drawable.home_entry_item_pressed);
					}
					if (event.getAction() == MotionEvent.ACTION_UP) {
						back.setBackgroundResource(R.drawable.home_entry_item_normal);
						BaseActivity activity = (BaseActivity) context;
						activity.finish();
						activity.overridePendingTransition(R.anim.tomenu_in, R.anim.tomenu_out);
					}
				}
				return true;
			}
		});
	}

	/**
	 * ����TextView����
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		tv.setText(title);
	}

}
