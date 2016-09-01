package com.example.speedupdemo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.speedupdemo.ViewHolder;
import com.example.speedupdemo.activity.BaseActivity;
import com.example.speedupdemo.info.AppInfo;
import com.example.speedupdemo.R;

public class SoftwareAdapter extends MyBaseAdapter<AppInfo> {
	Context context;
	float sx;
	float sy;
	Matrix matrix;
	LayoutInflater li;
	public boolean fast;

	public SoftwareAdapter(ArrayList<AppInfo> content, Context context) {
		super(content);
		this.context = context;
		matrix = new Matrix();
		// 获取布局过滤器
		li = LayoutInflater.from(context);
	}

	/**
	 * 勾选CheckBox
	 */
	public void isChecked(Boolean flag) {
		for (int i = 0; i < content.size(); i++) {
			content.get(i).setCheck(flag);
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = li.inflate(R.layout.software_list_item, null);
			// 获取控件
			vh.iv = (ImageView) convertView.findViewById(R.id.imageview);
			vh.tv1 = (TextView) convertView.findViewById(R.id.textview1);
			vh.tv2 = (TextView) convertView.findViewById(R.id.textview2);
			vh.tv3 = (TextView) convertView.findViewById(R.id.textview3);
			vh.cb = (CheckBox) convertView.findViewById(R.id.list_checkBox);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		// 获取对象
		final AppInfo info = content.get(position);

		if (fast) {
			vh.iv.setImageResource(R.drawable.icon_bmp);
		} else {
			// 拉伸图片
			BitmapDrawable bd = (BitmapDrawable) info.getLoadIcon();
			Bitmap bitmap = bd.getBitmap();
			sx = 1.0f * BaseActivity.SCREEN_HEIGHT / 12 / bitmap.getHeight();
			sy = 1.0f * BaseActivity.SCREEN_HEIGHT / 12 / bitmap.getHeight();
			matrix.setScale(sx, sy);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			vh.iv.setImageBitmap(bitmap);
		}

		// 设置控件
		// iv.setImageDrawable(info.getLoadIcon());
		vh.tv1.setText(info.getLoadLabel());
		vh.tv2.setText(info.getPackageName());
		vh.tv3.setText(info.getVersionName());
		vh.tv3.setTextSize(10);
		// 记得设置check的状态！！！没有设置就点击不了！！！！！！！！！！！！！！！！！！！！！
//		vh.cb.setChecked(info.isCheck());
		System.out.println("----------" + info.isCheck());
		// 防止下拉重置
		// vh.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

		vh.cb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.list_checkBox) {
					if (info.isCheck()) {
						info.setCheck(false);
					} else {
						info.setCheck(true);
					}
					// 记得刷新
					notifyDataSetChanged();
				}
			}
		});

		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		// if (info.isCheck()) {
		// info.setCheck(false);
		// } else {
		// info.setCheck(true);
		// }
		// // 记得刷新
		// notifyDataSetChanged();
		// }
		// });
		return convertView;
	}
}
