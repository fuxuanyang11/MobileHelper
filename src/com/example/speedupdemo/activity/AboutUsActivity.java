package com.example.speedupdemo.activity;

/**
 * ��ô��ȡ��ǰ����İ汾��
 * listView�����¼��Ĳ����ĺ���
 * ��onMeasure��ʱ��������ĸ߶Ⱥ���һ����
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
		tv.setText("1.0");//��ô��ȡ��ǰ����İ汾��
		tl.setTitle("��������");
	}
}
