package com.example.speedupdemo.activity;

import java.io.File;
import java.util.ArrayList;

import com.example.speedupdemo.adapter.FilemgrAdapter;
import com.example.speedupdemo.manager.FileManager;
import com.example.speedupdemo.manager.FileManager.OnTextChangeListener;
import com.example.speedupdemo.manager.MemoryManager;
import com.example.speedupdemo.view.TitleLayout;
import com.example.speedupdemo.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class FilemgrActivity extends BaseActivity implements OnTextChangeListener,Runnable{
	
	ListView listView;
	
	/**���������ݼ���*/
	ArrayList<String> content = new ArrayList<String>();
	
	/**�ļ��������*/
	FileManager fileManager;
	
	/**����������*/
	FilemgrAdapter adapter;
	
	TextView size;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filemgr_layout);
//		fileManager = new FileManager(this);
		fileManager = FileManager.getInstance(this);
		
		//��ȡ�ؼ�
		listView = (ListView) findViewById(R.id.filemgr_listview);
		TitleLayout tl = (TitleLayout) findViewById(R.id.filemgr_title);
		size = (TextView) findViewById(R.id.filemgr_tv_text1);
		TextView text = (TextView) findViewById(R.id.filemgr_tv_text2);
		text.setText("�ѷ���");
		
		tl.setTitle("�ļ�����");
		
		adapter = new FilemgrAdapter(content, this);
		listView.setAdapter(adapter);
		
		fileManager.setOnTextChangeListener(this);
		
		Thread thread =new Thread(this);
		thread.start();
//		//����ÿһ�еĸ߶�:��ʱû�з�����
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, SCREEN_HEIGHT);
//		listView.setLayoutParams(params);
		
		//��ʼ��content����
		for (int i = 0; i < FileManager.keys.length; i++) {
			content.add("0 B");
		}
		
		
	}
	
	/**
	 * Hander�������ڲ��࣬����ȡ�����
	 * ��ȡ��������handleMessage()�÷�����
	 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				//ˢ��������
				adapter.notifyDataSetChanged();//��ȡ���̴߳��ݵ���Ϣ��ˢ��һ�����������ı�����
				//���������û����һֱ�ı����ݡ�
				size.setText(content.get(0));
				break;

			default:
				break;
			}
		};
	};
	
	/**
	 * �������д�ķ����л������߳�
	 */
	@Override
	public void onTextChange(String type) {
		//��ȡMessage����
		Message message = handler.obtainMessage(0, type);
		//ȫ���ĵ�������
		content.set(0, Formatter.formatFileSize(this,fileManager.sizeMap.get(FileManager.TYPE_ANY)));
		//�����ﲻ����UI������
//		size.setText(Formatter.formatFileSize(this,fileManager.sizeMap.get(FileManager.TYPE_ANY))+"");
		for (int i = 1; i < FileManager.keys.length; i++) {
			if (FileManager.keys[i].equals(type)) {
				content.set(i, Formatter.formatFileSize(this, fileManager.sizeMap.get(FileManager.keys[i])));
				break;
			}
			//�Ƿ�״̬�쳣���Ѵ�����Ϣд����ѭ���С�
//			handler.sendMessage(message);
		}
		handler.sendMessage(message);
	}
	
	
	@Override
	public void run() {
		fileManager.initMap();
		fileManager.searchFile(new File(MemoryManager.getPhoneInSDCardPath()));
		//��������ܵ�������������﻽�����߳�
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				adapter.findOver = true;
				//���껹Ҫˢ��������
				adapter.notifyDataSetChanged();
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
//						startAction(FileInfoActivity.class, "file", FileManager.keys[position]);
						Intent intent = new Intent(FilemgrActivity.this, FileInfoActivity.class);
						intent.putExtra("file", FileManager.keys[position]);
						startActivityForResult(intent, 0/*�����룬���ڵ���0*/);
						
					}
				});
			}
		});
	}
	
	/**
	 * ����תʱʹ��startActivityForResult
	 * �����¸����淵��ʱ������ô˷���
	 * �������ɾ���ļ����޸Ĳ�ˢ����������
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//requestCode: startActivityForResult�еĵڶ�������,�����ж��Ǵ��ĸ����淵�ص���ǰҳ��
		//resultCode:�¸�����setResult�еķ�����:����ͨ�������룬��ȡ��ͬ�����ݣ���ͬ��״̬���ò�ͬ�ķ�����
		//data:�¸�����setResult���ݵ�����
		
		//��ȡ�¸����洫�ݵ�����
		boolean change = data.getBooleanExtra("change", false);
		if (change) {
			//�ǵ��������ã���
			for (int i = 0; i < FileManager.keys.length; i++) {
					content.set(i, Formatter.formatFileSize(this, fileManager.sizeMap.get(FileManager.keys[i])));
				}
			size.setText(content.get(0));
			adapter.notifyDataSetChanged();
		}
	}
}
