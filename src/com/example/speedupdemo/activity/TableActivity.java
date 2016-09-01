package com.example.speedupdemo.activity;

import java.util.ArrayList;

import com.example.speedupdemo.R;
import com.example.speedupdemo.adapter.TableAdapter;
import com.example.speedupdemo.info.TableInfo;
import com.example.speedupdemo.manager.CommonManager;
import com.example.speedupdemo.view.TitleLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TableActivity extends BaseActivity implements Runnable {

	int id;

	ArrayList<TableInfo> infos;

	TableAdapter tableAdapter;

	ListView listView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_layout);

		TitleLayout title = (TitleLayout) findViewById(R.id.table_title);
		listView = (ListView) findViewById(R.id.table_listview);
		/**
		 * 获取上个界面的数据
		 */
		Intent intent = getIntent();
		String idStr = intent.getStringExtra("id");
		id = Integer.parseInt(idStr);
		if (id == 1) {
			title.setTitle("订餐电话");
		} else if (id == 2) {
			title.setTitle("公共服务");
		} else if (id == 3) {
			title.setTitle("运营商");
		} else if (id == 4) {
			title.setTitle("快递服务");
		} else if (id == 5) {
			title.setTitle("机票酒店");
		} else if (id == 6) {
			title.setTitle("银行证券");
		} else if (id == 7) {
			title.setTitle("保险服务");
		} else if (id == 8) {
			title.setTitle("品牌售后");
		}

		Thread thread = new Thread(this);
		thread.start();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				call(infos.get(position).getNumber());
			}
		});
	}

	@Override
	public void run() {
		CommonManager commonManager = new CommonManager(this);
		infos = commonManager.getTable(id);

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				tableAdapter = new TableAdapter(infos, TableActivity.this);
				listView.setAdapter(tableAdapter);
			}
		});
	}
	
	
	/**
	 * 拨号的方法
	 */
	public void call(String telNumber) {
		Intent intent = new Intent(Intent.ACTION_CALL);
		Uri data = Uri.parse("tel:"+telNumber);
		intent.setData(data);
		startActivity(intent);
	}
}
