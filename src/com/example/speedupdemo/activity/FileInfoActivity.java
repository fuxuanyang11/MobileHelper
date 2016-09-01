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

	/** �ļ��Ƿ�ɾ�� */
	boolean change;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fileinfo_layout);
		/**
		 * ��ȡ��һ���������Ϣ
		 */
		Intent intent = getIntent();
		type = intent.getStringExtra("file");
		// ��ָ�룺���ϻ�û�г�ʼ������Ӧ���������ֵ,���������̻߳������߳�
		// infos = fileManager.infoMap.get(type);
		// ��ȡ�ؼ�
		listView = (ListView) findViewById(R.id.fileinfo_listview);
		TitleLayout tl = (TitleLayout) findViewById(R.id.fileinfo_title);
		TextView text1 = (TextView) findViewById(R.id.fileinfo_text1);
		TextView text2 = (TextView) findViewById(R.id.fileinfo_text2);
		conunt = (TextView) findViewById(R.id.fileinfo_count);
		size = (TextView) findViewById(R.id.fileinfo_size);
		check = (ImageView) findViewById(R.id.fileinfo_iv_check);
		clear = (TextView) findViewById(R.id.fileinfo_clear);
		// �ؼ������¼�
		check.setOnTouchListener(this);
		clear.setOnTouchListener(this);

		tl.setTitle(type);
		text1.setText("�ļ�����:");
		text2.setText("ռ�ÿռ�:");
		clear.setText("ɾ����ѡ�ļ�");
		
		listView.setFastScrollEnabled(true);
		listView.setScrollBarFadeDuration(500);//���������500ms�����ʧ,�ٴε���ֿ�ʼ��ʱ

		// fileManager = new FileManager(this);
		fileManager = FileManager.getInstance(this);

		// fileManager.setOnTextChangeListener(this);

		infos = fileManager.infoMap.get(type);
		adapter = new FileInFoAdapter(infos, FileInfoActivity.this);
		listView.setAdapter(adapter);
		// ResourcesNotFoundException?:infos.size()���ص���intֵ����ȥR�ļ�������Ӧ��id
		conunt.setText(infos.size() + "��");
		size.setText(Formatter.formatFileSize(FileInfoActivity.this,
				fileManager.sizeMap.get(type)));

		// Thread thread = new Thread(this);
		// thread.start();
		
		/**
		 * listView�ĵ���¼�
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
	 * ��дfinish()����
	 */
	@Override
	public void finish() {
		Intent intent = new Intent();// ����Я������
		intent.putExtra("change", change);
		setResult(/* ������,һ����ڵ���0 */100, intent);
		super.finish();
	}

	/**
	 * ɾ���ļ��ķ���
	 */
	public void deleteFile() {
		for (int i = 0; i < infos.size(); i++) {
			if (infos.get(i).isChecked()) {
				change = true;
				if (change) {
					FileInfo info = infos.get(i);
					File file = new File(infos.get(i).getPath());
					file.delete();
					// �ļ��Ĵ�СҲҪ�ı�
					// ȫ����
					Long any = fileManager.sizeMap.get(FileManager.TYPE_ANY);
					any = any - infos.get(i).getSize();
					fileManager.sizeMap.put(FileManager.TYPE_ANY, any);
					// �����ļ�
					if (fileManager.sizeMap.containsKey(infos.get(i).getType())) {
						Long size = fileManager.sizeMap.get(infos.get(i)
								.getType());
						size = size - infos.get(i).getSize();
						fileManager.sizeMap.put(infos.get(i).getType(), size);
						// ��Ҫ�ڶ�Ӧ��infoMap�������Ƴ�
						fileManager.infoMap.get(infos.get(i).getType()).remove(
								infos.get(i));
					}
					// �ǵôӼ������Ƴ�������Ҫi--��������Ƴ��Ļ���ȫ�����ļ���û���Ƴ�
					// content.remove(i);
					// i--;
					// ��ȫ���ļ������Ƴ����Ƴ���Ӧ������һ���Ѿ�ȷ���Ķ���
					fileManager.infoMap.get(FileManager.TYPE_ANY).remove(info);
					i--;
				}
			}
		}
		conunt.setText(infos.size() + "��");
		size.setText(Formatter.formatFileSize(FileInfoActivity.this,
				fileManager.sizeMap.get(type)));
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * ���ļ�
	 */
	public void openFile(FileInfo info/*ѡ�񵽵��ļ���Ϣ*/) {
		String mime = info.getMime();//�򿪵��ļ���ʽ
		String path = info.getPath();//�򿪵�·��
		
		try {//���ܻ�����쳣�����ļ�û�д򿪷�ʽ��
			/**
			 * ʹ��Intent���ļ�
			 */
			//Intent.ACTION_VIEW���ҵ�����չʾ�ļ����ݵĽ���(���)
			Intent intent = new Intent(Intent.ACTION_VIEW);
			//�ļ���ʹ��fromFile��ȡҪ�򿪵��ļ�   ����һ��ʹ�ã�Uri.parse
			Uri data = Uri.fromFile(new File(path));
			//data:�򿪵��ļ�  mime:�򿪵ķ�ʽ
			intent.setDataAndType(data, mime);
			startActivity(intent);//��ת��������ʾ�ļ��Ľ���
		} catch (Exception e) {
//			e.printStackTrace();
			Toast.makeText(this, "�޷����ļ�", Toast.LENGTH_SHORT).show();
		}
	
	}
	/**
	 * Ϊ���ܹ��������²��ң�����ʹ�õ������ģʽ��ʹ������ͬһ����
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
	// //ResourcesNotFoundException?:infos.size()���ص���intֵ����ȥR�ļ�������Ӧ��id
	// conunt.setText(infos.size()+"��");
	// size.setText(Formatter.formatFileSize(FileInfoActivity.this,
	// fileManager.sizeMap.get(type)));
	// }
	// });
	//
	// }

	// ����ӿ�û���ã���������Ҫʵ�֡�����������дһ�������������ò����ж��Ƿ�ִ��
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
