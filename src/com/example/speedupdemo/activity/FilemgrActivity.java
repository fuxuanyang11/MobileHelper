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
	
	/**适配器内容集合*/
	ArrayList<String> content = new ArrayList<String>();
	
	/**文件管理对象*/
	FileManager fileManager;
	
	/**适配器对象*/
	FilemgrAdapter adapter;
	
	TextView size;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filemgr_layout);
//		fileManager = new FileManager(this);
		fileManager = FileManager.getInstance(this);
		
		//获取控件
		listView = (ListView) findViewById(R.id.filemgr_listview);
		TitleLayout tl = (TitleLayout) findViewById(R.id.filemgr_title);
		size = (TextView) findViewById(R.id.filemgr_tv_text1);
		TextView text = (TextView) findViewById(R.id.filemgr_tv_text2);
		text.setText("已发现");
		
		tl.setTitle("文件管理");
		
		adapter = new FilemgrAdapter(content, this);
		listView.setAdapter(adapter);
		
		fileManager.setOnTextChangeListener(this);
		
		Thread thread =new Thread(this);
		thread.start();
//		//设置每一行的高度:暂时没有方法。
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, SCREEN_HEIGHT);
//		listView.setLayoutParams(params);
		
		//初始化content集合
		for (int i = 0; i < FileManager.keys.length; i++) {
			content.add("0 B");
		}
		
		
	}
	
	/**
	 * Hander的匿名内部类，并获取其对象
	 * 获取数据是在handleMessage()该方法中
	 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				//刷新适配器
				adapter.notifyDataSetChanged();//获取子线程传递的消息就刷新一次适配器，改变数据
				//在这里设置会跟着一直改变数据。
				size.setText(content.get(0));
				break;

			default:
				break;
			}
		};
	};
	
	/**
	 * 在这个重写的方法中唤醒主线程
	 */
	@Override
	public void onTextChange(String type) {
		//获取Message对象
		Message message = handler.obtainMessage(0, type);
		//全部的单独设置
		content.set(0, Formatter.formatFileSize(this,fileManager.sizeMap.get(FileManager.TYPE_ANY)));
		//在这里不能做UI操作？
//		size.setText(Formatter.formatFileSize(this,fileManager.sizeMap.get(FileManager.TYPE_ANY))+"");
		for (int i = 1; i < FileManager.keys.length; i++) {
			if (FileManager.keys[i].equals(type)) {
				content.set(i, Formatter.formatFileSize(this, fileManager.sizeMap.get(FileManager.keys[i])));
				break;
			}
			//非法状态异常？把传递信息写到了循环中。
//			handler.sendMessage(message);
		}
		handler.sendMessage(message);
	}
	
	
	@Override
	public void run() {
		fileManager.initMap();
		fileManager.searchFile(new File(MemoryManager.getPhoneInSDCardPath()));
		//加载完才能点击，所以在这里唤醒主线程
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				adapter.findOver = true;
				//改完还要刷新适配器
				adapter.notifyDataSetChanged();
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
//						startAction(FileInfoActivity.class, "file", FileManager.keys[position]);
						Intent intent = new Intent(FilemgrActivity.this, FileInfoActivity.class);
						intent.putExtra("file", FileManager.keys[position]);
						startActivityForResult(intent, 0/*请求码，大于等于0*/);
						
					}
				});
			}
		});
	}
	
	/**
	 * 若跳转时使用startActivityForResult
	 * 当从下个界面返回时，会调用此方法
	 * 这里完成删除文件有修改才刷新适配器。
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//requestCode: startActivityForResult中的第二个参数,可以判断是从哪个界面返回到当前页面
		//resultCode:下个界面setResult中的返回码:可以通过返回码，获取不同的数据，不同的状态设置不同的返回码
		//data:下个界面setResult传递的数据
		
		//获取下个界面传递的数据
		boolean change = data.getBooleanExtra("change", false);
		if (change) {
			//记得重新设置！！
			for (int i = 0; i < FileManager.keys.length; i++) {
					content.set(i, Formatter.formatFileSize(this, fileManager.sizeMap.get(FileManager.keys[i])));
				}
			size.setText(content.get(0));
			adapter.notifyDataSetChanged();
		}
	}
}
