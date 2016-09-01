package com.example.speedupdemo.adapter;

import java.util.ArrayList;

import com.example.speedupdemo.ViewHolder;
import com.example.speedupdemo.activity.BaseActivity;
import com.example.speedupdemo.info.AppInfo;
import com.example.speedupdemo.R;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

//将元素数据匹配到列表对应行上
public class SpeedUpAdapter extends MyBaseAdapter<AppInfo> {
	// 不删掉就等于重新声明一个集合。
	// ArrayList<AppInfo> content;
	Context context;
	Matrix matrix;
	float sx;
	float sy;
	ActivityManager activityManager;
	LayoutInflater li;
	public boolean fast;

	public SpeedUpAdapter(ArrayList<AppInfo> content, Context context) {
		super(content);
		this.context = context;
		// 获取ActivityManager对象
		activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		matrix = new Matrix();
		li = LayoutInflater.from(context);
	}

	/**
	 * 显示系统进程
	 */
	public void showSystem(ArrayList<AppInfo> systemApp) {
		content.addAll(systemApp);
		// 记得刷新!!!!!
		notifyDataSetChanged();
	}

	/**
	 * 移除系统进程
	 */
	public void removeSystem(ArrayList<AppInfo> systemApp) {
		content.removeAll(systemApp);
		notifyDataSetChanged();
	}

	/**
	 * 勾选所有checkBox
	 */
	public void allTrue(ArrayList<AppInfo> infos) {
		// 遍历集合啊！！！！！！！！！！！！！！！
		for (int i = 0; i < infos.size(); i++) {
			infos.get(i).setCheck(true);
		}
		notifyDataSetChanged();
	}

	/**
	 * 所有checkBox都不勾选
	 */
	public void allFalse(ArrayList<AppInfo> infos) {
		for (int i = 0; i < infos.size(); i++) {
			infos.get(i).setCheck(false);
		}
		notifyDataSetChanged();
	}

	/**
	 * 删除勾选的进程
	 */
	public void removeCheck(ArrayList<AppInfo> infos) {
		for (int i = 0; i < infos.size(); i++) {
			if (infos.get(i).isCheck()) {
				activityManager.killBackgroundProcesses(infos.get(i)
						.getProcessName());
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			// 获取布局过滤器,最好在构造方法中
			vh = new ViewHolder();
			// 获取view
			convertView = li.inflate(R.layout.speed_list_item, null);

			// 获取控件
			/* ImageView iv */vh.iv = (ImageView) convertView
					.findViewById(R.id.imageview);
			 vh.tv1 = (TextView) convertView.findViewById(R.id.textview1);
			 vh.tv2 = (TextView) convertView.findViewById(R.id.textview2);
			 vh.tv3 = (TextView) convertView.findViewById(R.id.textview3);
			 vh.cb = (CheckBox) convertView.findViewById(R.id.checkbox);
			//convertView为空时set
			convertView.setTag(vh);
		} else {//convertView不为空
			vh = (ViewHolder) convertView.getTag();
		}
		// 获取AppInfo对象
		final AppInfo info = content.get(position);
//		Bitmap bitmap = null;
		// 一拉伸图片就很慢？非法参数异常，超过32位？矩阵设置的是拉伸的倍数！
		if (fast) {
			vh.iv.setImageResource(R.drawable.icon_bmp);
		}else {
			BitmapDrawable bd = (BitmapDrawable) info.getIcon();
			Bitmap bitmap = bd.getBitmap();
			sx = 1.0f * BaseActivity.SCREEN_HEIGHT / 12 / bitmap.getHeight();
			sy = 1.0f * BaseActivity.SCREEN_HEIGHT / 12 / bitmap.getHeight();
			matrix.setScale(sx, sy);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			vh.iv.setImageBitmap(bitmap);
		}
		

		// iv.setImageDrawable(info.getIcon());
		
		vh.tv1.setText(info.getLabel());
		vh.tv2.setText(info.getSizeText());
		// 记得设置check的状态！！！
		vh.cb.setChecked(info.isCheck());
		// 设置checkBox的监听事件,避免下拉会重置,不能使用setOnCheckedChangeListener
		//因为会记录之前点击过checkBox的状态
//		vh.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		vh.cb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (v.getId()==R.id.checkbox) {
					if (info.isCheck()) {
						info.setCheck(false);
					} else {
						info.setCheck(true);
					}
					// 刷新
					notifyDataSetChanged();
				}
			}
		});

//			@Override
//			/**
//			 * @param isChecked当前的CheckBox状态是选中则为true
//			 */
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				if (info.isCheck()) {
//					info.setCheck(false);
//				} else {
//					info.setCheck(true);
//				}
//				// 刷新
//				notifyDataSetChanged();
//			}
//		});
		if (info.isSystem()) {
			//优化后有些没显示文字，原因：用的是之前的TextView，他是隐藏的，所以需要再将他显示出来
			vh.tv3.setVisibility(View.VISIBLE);
			vh.tv3.setText("系统进程");
			vh.tv3.setTextColor(Color.RED);
		} else {
			vh.tv3.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	

}
