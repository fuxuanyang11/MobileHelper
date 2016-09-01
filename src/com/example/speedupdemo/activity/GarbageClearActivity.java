package com.example.speedupdemo.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.speedupdemo.adapter.ClearAdapter;
import com.example.speedupdemo.info.ClearInfo;
import com.example.speedupdemo.manager.ClearPathManager;
import com.example.speedupdemo.view.TitleLayout;
import com.example.speedupdemo.R;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GarbageClearActivity extends BaseActivity implements Runnable,
		OnTouchListener {

	ListView listView;

	ClearAdapter adapter;

	ProgressBar progressBar;

	ImageView check;

	TextView clear;

	TextView allSzie;

	TextView text;

	boolean isCheckd;

	ArrayList<ClearInfo> infos = new ArrayList<ClearInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clear_layout);

		TitleLayout tl = (TitleLayout) findViewById(R.id.clear_title);
		tl.setTitle("垃圾清理");

		// 获取控件
		listView = (ListView) findViewById(R.id.clear_listview);
		progressBar = (ProgressBar) findViewById(R.id.clear_progress);
		check = (ImageView) findViewById(R.id.clear_iv_check);
		clear = (TextView) findViewById(R.id.clear_text);
		allSzie = (TextView) findViewById(R.id.clear_tv_text1);
		text = (TextView) findViewById(R.id.clera_tv_text2);
		clear.setText("一键清理");

		check.setOnTouchListener(this);
		clear.setOnTouchListener(this);
		listView.setVisibility(View.INVISIBLE);
		Thread thread = new Thread(this);
		thread.start();

	}

	@Override
	public void run() {
		if (adapter != null) {
			// 删除垃圾文件
			adapter.clear();
		} else {
			ClearPathManager clearPathManager = new ClearPathManager(this);
			AssetManager assets = getAssets();
			InputStream inputStream = null;
			try {
				// 将assets的数据写入到database中
				inputStream = assets.open("db/clearpath.db");
				clearPathManager.createFile(inputStream);
				infos = clearPathManager.getData(this);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				} else {
					adapter = new ClearAdapter(infos, GarbageClearActivity.this);
					listView.setAdapter(adapter);
				}
				listView.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.INVISIBLE);
				allSzie.setText(adapter.getAllSize());
				text.setText("已发现");
			}
		});
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.clear_iv_check) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				isCheckd = !isCheckd;
				if (isCheckd) {
					adapter.allChecked();
					check.setBackgroundResource(R.drawable.check_rect_correct_checked);
				} else {
					adapter.noChecked();
					check.setBackgroundResource(R.drawable.check_rect_correct_default);
				}
			}
		}
		if (v.getId() == R.id.clear_text) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				clear.setBackgroundResource(R.drawable.speedup_shape_pressed);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				clear.setBackgroundResource(R.drawable.speedup_shape_normal);
				boolean flag = false;
				for (int i = 0; i < infos.size(); i++) {
					flag = infos.get(i).isCheck();
					if (flag) {//只有当选中才会删除垃圾
						listView.setVisibility(View.INVISIBLE);
						progressBar.setVisibility(View.VISIBLE);
						Thread thread = new Thread(this);
						thread.start();
						break;
					}
				}
				if (!flag) {
					Toast.makeText(this, "没有选中文件", Toast.LENGTH_SHORT).show();
				}
				
			}
		}

		return true;
	}
}
