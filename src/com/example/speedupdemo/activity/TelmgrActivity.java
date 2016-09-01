package com.example.speedupdemo.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.speedupdemo.adapter.TelmsgAdapter;
import com.example.speedupdemo.info.ClassInfo;
import com.example.speedupdemo.manager.CommonManager;
import com.example.speedupdemo.view.TitleLayout;
import com.example.speedupdemo.R;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class TelmgrActivity extends BaseActivity implements Runnable {

	CommonManager commonManager;

	ArrayList<ClassInfo> infos;

	GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telmsg_layout);
		commonManager = new CommonManager(this);

		// 获取控件
		TitleLayout tl = (TitleLayout) findViewById(R.id.telmsg_title);
		gridView = (GridView) findViewById(R.id.telmsg_gridview); 
		// 设置控件
		tl.setTitle("通讯大全");

		Thread thread = new Thread(this);
		thread.start();

		// GridView点击事件
		gridView.setOnItemClickListener(new OnItemClickListener() {

			/**
			 * @parm parent-->点击的listView
			 * @parm view-->适配器getItem()方法返回的view
			 * @parm position-->点击的listView对应的角标
			 * @parm id-->适配器getItemId()方法返回的id
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startAction(TableActivity.class, "id",infos.get(position).getId()+"");
				
			}
		});
	}

	@Override
	public void run() {// 因为获取数据是耗时操作要写在子线程

		AssetManager assets = getAssets();// 获取目录下assets包
		InputStream inputStream = null;
		try {
			// 将assets包下的数据写入到data。。。。下的数据库
			inputStream = assets.open("db/commonnum.db");// 开启数据库
			commonManager.createFile(inputStream);
			infos = commonManager.getData();// 获取ClassInfo集合,要在开启数据库后才能写入
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

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TelmsgAdapter adapter = new TelmsgAdapter(infos,
						TelmgrActivity.this);
				gridView.setAdapter(adapter);
			}
		});
	}
}
