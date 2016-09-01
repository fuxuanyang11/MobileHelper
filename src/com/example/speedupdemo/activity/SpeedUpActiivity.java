package com.example.speedupdemo.activity;

import java.util.ArrayList;
import java.util.Map;

import com.example.speedupdemo.adapter.SpeedUpAdapter;
import com.example.speedupdemo.info.AppInfo;
import com.example.speedupdemo.manager.AppInfoManager;
import com.example.speedupdemo.manager.MemoryManager;
import com.example.speedupdemo.view.TitleLayout;
import com.example.speedupdemo.R;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ��Ҫ��onCreate��ȡϵͳ���̣�Ҫ�½�һ�����̻߳�ȡ����ȡϵͳ�������ں�ʱ����
 * 
 */
public class SpeedUpActiivity extends BaseActivity implements OnTouchListener,
		Runnable {
	// ArrayList<AppInfo> infos;
	// ���̼���
	Map<String, ArrayList<AppInfo>> map;
	// ϵͳ���̼���
	ArrayList<AppInfo> systemApp;
	// ���������̼���
	ArrayList<AppInfo> userApp;
	SpeedUpAdapter myAdapter;
	// �Ƿ���ʾϵͳ����
	boolean showSystem;
	// �Ƿ�ȫ����ѡ
	boolean allCheck;
	TextView tv;
	TextView tv_percent;
	TextView tv_clean;
	ImageView iv;
	AppInfoManager aManager;
	ListView listView;
	ProgressBar pBar2;
	TextView version;

	// boolean showList;

	// ȫ�ֱ�����û�й��췽���ͻ��ȴ�������ʱ��ʹ��this��û�е�ǰ��Ķ��������½�������÷��ڹ��췽����
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speed_up);
		Thread thread = new Thread(this);
		thread.start();
		aManager = new AppInfoManager(this);
		// �ǵû�ȡArrayList<AppInfo>�ļ���
		// ��ȡ�ؼ�
		listView = (ListView) findViewById(R.id.speedup_list_view);
		tv = (TextView) findViewById(R.id.tv_showsystem);
		tv_percent = (TextView) findViewById(R.id.speedup_tv_percent);
		iv = (ImageView) findViewById(R.id.speedup_iv_check);
		ProgressBar pBar = (ProgressBar) findViewById(R.id.speedup_progress);
		pBar2 = (ProgressBar) findViewById(R.id.speedup_progress_nomal);
		tv_clean = (TextView) findViewById(R.id.speedup_tv_clean);
		version = (TextView) findViewById(R.id.speedup_version_text);
		TitleLayout tl = (TitleLayout) findViewById(R.id.speedup_title);
		tl.setTitle("�ֻ�����");
		// ������ռ�ڱ���
		long used = MemoryManager.getMemorySize(true, this)
				- MemoryManager.getMemorySize(false, this);
		double percentf = 1.0 * used / MemoryManager.getMemorySize(true, this);
		int percent = (int) Math.round(percentf * 100);
		pBar.setProgress(percent);
		// �����ؼ�
		tv.setOnTouchListener(this);
		iv.setOnTouchListener(this);
		tv_clean.setOnTouchListener(this);
		// ���ÿؼ�
		tv.setText("��ʾϵͳ����");
		// ʹ��Formatter�����ڴ�
		String usedSize = Formatter.formatFileSize(this, used);
		String allSize = Formatter.formatFileSize(this,
				MemoryManager.getMemorySize(true, this));
		// float used2 = (float) (1.0*used/1000000);
		// float all = (float) (1.0*manager.getMemorySize(true, this)/1000000);
		tv_percent.setText("�����ڴ�" + usedSize + "/" + allSize);
		tv_percent.setTextColor(Color.BLUE);
		version.setText(Build.MODEL + " Android" + Build.VERSION.RELEASE);

		// ����listView�����ļ����¼�
		listView.setOnScrollListener(new OnScrollListener() {

			/**
			 * ����������״̬�ĸı�ͻ���ô˷���
			 * 
			 * @parm view-->���ڼ�����listView
			 * @parm scrollState-->״̬
			 */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
					// ���ٻ���
					myAdapter.fast = true;
				} else if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
					// ���ٻ���,�����ͻ����getView�ķ��������Բ���ˢ��
					myAdapter.fast = false;
				} else if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// ��ֹ�����������Ҫˢ��
					if (myAdapter.fast) {
						myAdapter.fast = false;
						// ˢ��
						myAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

		// showList = true;//���߳��к�ʱ���������߳̿϶�������߳���ִ��
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// ����ı���ʾ�Ľ���
		if (v.getId() == R.id.tv_showsystem) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				tv.setBackgroundResource(R.drawable.speedup_shape_pressed);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				tv.setBackgroundResource(R.drawable.speedup_shape_normal);
				showSystem = !showSystem;
				if (showSystem) {
					myAdapter.showSystem(systemApp);
					tv.setText("ֻ��ʾ�û�����");

				} else {
					myAdapter.removeSystem(systemApp);
					tv.setText("��ʾϵͳ����");
				}
			}
			// ��ѡ
		}
		if (v.getId() == R.id.speedup_iv_check) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				allCheck = !allCheck;
				if (allCheck) {
					iv.setImageResource(R.drawable.check_rect_correct_checked);
					if (!showSystem) {
						myAdapter.allTrue(userApp);
					} else {
						myAdapter.allTrue(systemApp);
						myAdapter.allTrue(userApp);
					}
				} else {
					iv.setImageResource(R.drawable.check_rect_correct_default);
					if (!showSystem) {
						myAdapter.allFalse(userApp);
					} else {
						myAdapter.allFalse(systemApp);
						myAdapter.allFalse(userApp);
					}
				}
			}
		}
		if (v.getId() == R.id.speedup_tv_clean) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				tv_clean.setBackgroundResource(R.drawable.speedup_shape_pressed);
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				tv_clean.setBackgroundResource(R.drawable.speedup_shape_normal);
				myAdapter.removeCheck(userApp);
				myAdapter.removeCheck(systemApp);
				listView.setVisibility(View.INVISIBLE);
				pBar2.setVisibility(View.VISIBLE);
				// �½�һ���̣߳�����ִ��run����
				Thread thread2 = new Thread(this);
				thread2.start();
			}
		}

		return true;
	}

	/**
	 * ���߳�
	 */
	@Override
	public void run() {
		map = aManager.getBackProcess();
		systemApp = map.get("system");
		userApp = map.get("user");
		myAdapter = new SpeedUpAdapter(userApp, this);
		myAdapter.allTrue(userApp);
		// listView.setAdapter(myAdapter);//ֻҪ��ı����Ĳ�����UI������ֻ�������߳���ִ�С�
		// �ܹ������߳��л������߳�
		runOnUiThread(new Runnable() {// �����ڲ���
			// ���������̵߳�run����
			@Override
			public void run() {
				// �Ȱ�listview���أ���ʾ��������listview������ɣ��Ͱѽ���������
				listView.setAdapter(myAdapter);
				listView.setVisibility(View.INVISIBLE);
				pBar2.setVisibility(View.INVISIBLE);
				listView.setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * ���ð��·��ؼ��Ķ���Ч��
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.tomenu_in, R.anim.tomenu_out);
	}
}
