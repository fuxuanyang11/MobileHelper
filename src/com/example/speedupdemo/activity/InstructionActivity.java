package com.example.speedupdemo.activity;

import java.util.ArrayList;

import com.example.speedupdemo.SharedUtil;
import com.example.speedupdemo.adapter.InstructionAdapter;
import com.example.speedupdemo.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class InstructionActivity extends BaseActivity implements
		OnPageChangeListener, OnClickListener {
	ViewPager vp;
	ArrayList<View> views = new ArrayList<View>();
	ImageView[] ivs = new ImageView[5];
	String[] strs = { "iv1", "iv2", "iv3", "iv4", "iv5" };
	TextView tv;

	boolean first = true;
	
	boolean show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedUtil util = new SharedUtil(this, "data");
		// 获取共享数据
		String firString = util.getValue("first", true + "");
		first = Boolean.parseBoolean(firString);
		
		Intent intent = getIntent();
		String showStr = intent.getStringExtra("help");
		show = Boolean.parseBoolean(showStr);
		if (!first&&show==false) {
			startAction(this);
			finish();
		} 
		if(first||show) {
			setContentView(R.layout.instruction_layout);

			// 写入共享数据
			util.putValue("first", false + "");

			InstructionAdapter adapter = new InstructionAdapter(views, this);
			// 设置内容
			adapter.setContent(this);
			vp = (ViewPager) findViewById(R.id.instruction_viewPager);
			vp.setAdapter(adapter);
			for (int i = 0; i < ivs.length; i++) {
				String name = "instruct_" + strs[i];
				int id = getResources().getIdentifier(name, "id",
						getPackageName());
				ivs[i] = (ImageView) findViewById(id);
			}
			tv = (TextView) findViewById(R.id.instruct_tv);
			tv.setVisibility(View.INVISIBLE);
			tv.setOnClickListener(this);
			vp.setOnPageChangeListener(this);
			ivs[0].setBackgroundResource(R.drawable.adware_style_selected);
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		tv.setVisibility(View.INVISIBLE);
		// 最后一张
		if (arg0 == views.size() - 1) {
			tv.setVisibility(View.VISIBLE);
		}
		for (int i = 0; i < ivs.length; i++) {
			if (i == arg0) {
				ivs[i].setBackgroundResource(R.drawable.adware_style_selected);
			} else {
				ivs[i].setBackgroundResource(R.drawable.adware_style_default);
			}
		}
	}

	/**
	 * 跳转
	 * 
	 * @param context
	 */
	public void startAction(Context context) {
		Intent intent = new Intent(context, LogoActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.instruct_tv) {
			if (show) {
				finish();
				overridePendingTransition(R.anim.tomenu_in, R.anim.tomenu_out);
			}
			if (first) {
				startAction(this);
				finish();
				overridePendingTransition(R.anim.tomenu_in, R.anim.tomenu_out);
			
			}
		
		}
	}
}
