package com.example.speedupdemo.view;

import com.example.speedupdemo.activity.BaseActivity;
import com.example.speedupdemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

//为什么不能实现onTouchListener接口 
public class TitleLayout extends LinearLayout {

	TextView tv;
	View back;

	public TitleLayout(final Context context, AttributeSet attrs) {
		super(context, attrs);
		/**
		 * 第二个参数为给加载好的布局再添加一个父布局,指定TitleLayout所以传入this
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
	 * 设置TextView内容
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		tv.setText(title);
	}

}
