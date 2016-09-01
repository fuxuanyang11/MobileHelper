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

		// ��ȡ�ؼ�
		TitleLayout tl = (TitleLayout) findViewById(R.id.telmsg_title);
		gridView = (GridView) findViewById(R.id.telmsg_gridview); 
		// ���ÿؼ�
		tl.setTitle("ͨѶ��ȫ");

		Thread thread = new Thread(this);
		thread.start();

		// GridView����¼�
		gridView.setOnItemClickListener(new OnItemClickListener() {

			/**
			 * @parm parent-->�����listView
			 * @parm view-->������getItem()�������ص�view
			 * @parm position-->�����listView��Ӧ�ĽǱ�
			 * @parm id-->������getItemId()�������ص�id
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startAction(TableActivity.class, "id",infos.get(position).getId()+"");
				
			}
		});
	}

	@Override
	public void run() {// ��Ϊ��ȡ�����Ǻ�ʱ����Ҫд�����߳�

		AssetManager assets = getAssets();// ��ȡĿ¼��assets��
		InputStream inputStream = null;
		try {
			// ��assets���µ�����д�뵽data���������µ����ݿ�
			inputStream = assets.open("db/commonnum.db");// �������ݿ�
			commonManager.createFile(inputStream);
			infos = commonManager.getData();// ��ȡClassInfo����,Ҫ�ڿ������ݿ�����д��
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
