package com.example.speedupdemo.activity;

import java.io.File;
import java.util.ArrayList;

import com.example.speedupdemo.R;
import com.example.speedupdemo.adapter.FileInFoAdapter;
import com.example.speedupdemo.info.FileInfo;
import com.example.speedupdemo.manager.FileManager;
import com.example.speedupdemo.view.TitleLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileInfoActivity extends BaseActivity implements OnTouchListener {

	FileInFoAdapter adapter;

	ArrayList<FileInfo> infos;

	FileManager fileManager;

	ListView listView;

	String type;

	TextView conunt;

	TextView size;

	ImageView check;

	TextView clear;

	boolean isCheck;

	/** 文件是否删除 */
	boolean change;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fileinfo_layout);
		/**
		 * 获取上一个界面的信息
		 */
		Intent intent = getIntent();
		type = intent.getStringExtra("file");
		// 空指针：集合还没有初始化，不应该在这个赋值,可以在子线程唤醒主线程
		// infos = fileManager.infoMap.get(type);
		// 获取控件
		listView = (ListView) findViewById(R.id.fileinfo_listview);
		TitleLayout tl = (TitleLayout) findViewById(R.id.fileinfo_title);
		TextView text1 = (TextView) findViewById(R.id.fileinfo_text1);
		TextView text2 = (TextView) findViewById(R.id.fileinfo_text2);
		conunt = (TextView) findViewById(R.id.fileinfo_count);
		size = (TextView) findViewById(R.id.fileinfo_size);
		check = (ImageView) findViewById(R.id.fileinfo_iv_check);
		clear = (TextView) findViewById(R.id.fileinfo_clear);
		// 控件监听事件
		check.setOnTouchListener(this);
		clear.setOnTouchListener(this);

		tl.setTitle(type);
		text1.setText("文件数量:");
		text2.setText("占用空间:");
		clear.setText("删除所选文件");
		
		listView.setFastScrollEnabled(true);
		listView.setScrollBarFadeDuration(500);//点击滑动条500ms后会消失,再次点击又开始计时

		// fileManager = new FileManager(this);
		fileManager = FileManager.getInstance(this);

		// fileManager.setOnTextChangeListener(this);

		infos = fileManager.infoMap.get(type);
		adapter = new FileInFoAdapter(infos, FileInfoActivity.this);
		listView.setAdapter(adapter);
		// ResourcesNotFoundException?:infos.size()返回的是int值，会去R文件查找相应的id
		conunt.setText(infos.size() + "个");
		size.setText(Formatter.formatFileSize(FileInfoActivity.this,
				fileManager.sizeMap.get(type)));

		// Thread thread = new Thread(this);
		// thread.start();
		
		/**
		 * listView的点击事件
		 */
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				openFile(infos.get(position));
			}
		});
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.fileinfo_iv_check) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				isCheck = !isCheck;
				if (isCheck) {
					check.setImageResource(R.drawable.check_rect_correct_checked);
					adapter.allChecked();
				} else {
					check.setImageResource(R.drawable.check_rect_correct_default);
					adapter.cancleChecked();
				}
			}
		}
		if (v.getId() == R.id.fileinfo_clear) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				clear.setBackgroundResource(R.drawable.speedup_shape_pressed);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				clear.setBackgroundResource(R.drawable.speedup_shape_normal);
				deleteFile();
			}
		}

		return true;
	}

	/**
	 * 重写finish()方法
	 */
	@Override
	public void finish() {
		Intent intent = new Intent();// 用来携带数据
		intent.putExtra("change", change);
		setResult(/* 返回码,一般大于等于0 */100, intent);
		super.finish();
	}

	/**
	 * 删除文件的方法
	 */
	public void deleteFile() {
		for (int i = 0; i < infos.size(); i++) {
			if (infos.get(i).isChecked()) {
				change = true;
				if (change) {
					FileInfo info = infos.get(i);
					File file = new File(infos.get(i).getPath());
					file.delete();
					// 文件的大小也要改变
					// 全部的
					Long any = fileManager.sizeMap.get(FileManager.TYPE_ANY);
					any = any - infos.get(i).getSize();
					fileManager.sizeMap.put(FileManager.TYPE_ANY, any);
					// 其他文件
					if (fileManager.sizeMap.containsKey(infos.get(i).getType())) {
						Long size = fileManager.sizeMap.get(infos.get(i)
								.getType());
						size = size - infos.get(i).getSize();
						fileManager.sizeMap.put(infos.get(i).getType(), size);
						// 还要在对应的infoMap集合中移除
						fileManager.infoMap.get(infos.get(i).getType()).remove(
								infos.get(i));
					}
					// 记得从集合中移除，而且要i--，用这个移除的话，全部的文件会没有移除
					// content.remove(i);
					// i--;
					// 在全部的集合中移除，移除的应该是上一个已经确定的对象，
					fileManager.infoMap.get(FileManager.TYPE_ANY).remove(info);
					i--;
				}
			}
		}
		conunt.setText(infos.size() + "个");
		size.setText(Formatter.formatFileSize(FileInfoActivity.this,
				fileManager.sizeMap.get(type)));
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * 打开文件
	 */
	public void openFile(FileInfo info/*选择到的文件信息*/) {
		String mime = info.getMime();//打开的文件方式
		String path = info.getPath();//打开的路径
		
		try {//可能会出现异常，该文件没有打开方式。
			/**
			 * 使用Intent打开文件
			 */
			//Intent.ACTION_VIEW：找到可以展示文件内容的界面(软件)
			Intent intent = new Intent(Intent.ACTION_VIEW);
			//文件：使用fromFile获取要打开的文件   其他一般使用：Uri.parse
			Uri data = Uri.fromFile(new File(path));
			//data:打开的文件  mime:打开的方式
			intent.setDataAndType(data, mime);
			startActivity(intent);//跳转到可以显示文件的界面
		} catch (Exception e) {
//			e.printStackTrace();
			Toast.makeText(this, "无法打开文件", Toast.LENGTH_SHORT).show();
		}
	
	}
	/**
	 * 为了能够不再重新查找，可以使用单例设计模式。使对象都是同一个。
	 */
	// @Override
	// public void run() {
	//
	// // fileManager.initMap();
	// // fileManager.searchFile(new
	// File(MemoryManager.getPhoneInSDCardPath()));
	//
	// runOnUiThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// infos = fileManager.infoMap.get(type);
	// adapter = new FileInFoAdapter(infos, FileInfoActivity.this);
	// listView.setAdapter(adapter);
	// //ResourcesNotFoundException?:infos.size()返回的是int值，会去R文件查找相应的id
	// conunt.setText(infos.size()+"个");
	// size.setText(Formatter.formatFileSize(FileInfoActivity.this,
	// fileManager.sizeMap.get(type)));
	// }
	// });
	//
	// }

	// 这个接口没有用，但是又需要实现。还是另外再写一个方法？或者用参数判断是否执行
	// @Override
	// public void onTextChange(String type) {
	// Message message = handler.obtainMessage(0, type);
	// infos.get(0).setSize(fileManager.sizeMap.get(FileManager.TYPE_ANY));
	// for (int i = 0; i < FileManager.keys.length; i++) {
	// if (FileManager.keys[i].equals(type)) {
	// infos.get(i).setSize(
	// fileManager.sizeMap.get(FileManager.keys[i]));
	// }
	// }
	// handler.sendMessage(message);
	// }
}
