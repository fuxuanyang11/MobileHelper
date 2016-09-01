package com.example.speedupdemo.activity;

/**
 * 怎么获取当前软件的版本？
 * listView监听事件的参数的含义
 * 用onMeasure有时候算出来的高度好像不一样？
 */



import com.example.speedupdemo.view.TitleLayout;
import com.example.speedupdemo.R;

import android.os.Bundle;
import android.widget.TextView;

public class AboutUsActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		
		TitleLayout tl = (TitleLayout) findViewById(R.id.about_title);
		TextView tv = (TextView) findViewById(R.id.about_text);
		tv.setText("1.0");//怎么获取当前软件的版本？
		tl.setTitle("关于我们");
	}
}
