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
		// ��ȡ���ֹ�����
		li = LayoutInflater.from(context);
	}

	/**
	 * ��ѡCheckBox
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
			// ��ȡ�ؼ�
			vh.iv = (ImageView) convertView.findViewById(R.id.imageview);
			vh.tv1 = (TextView) convertView.findViewById(R.id.textview1);
			vh.tv2 = (TextView) convertView.findViewById(R.id.textview2);
			vh.tv3 = (TextView) convertView.findViewById(R.id.textview3);
			vh.cb = (CheckBox) convertView.findViewById(R.id.list_checkBox);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		// ��ȡ����
		final AppInfo info = content.get(position);

		if (fast) {
			vh.iv.setImageResource(R.drawable.icon_bmp);
		} else {
			// ����ͼƬ
			BitmapDrawable bd = (BitmapDrawable) info.getLoadIcon();
			Bitmap bitmap = bd.getBitmap();
			sx = 1.0f * BaseActivity.SCREEN_HEIGHT / 12 / bitmap.getHeight();
			sy = 1.0f * BaseActivity.SCREEN_HEIGHT / 12 / bitmap.getHeight();
			matrix.setScale(sx, sy);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			vh.iv.setImageBitmap(bitmap);
		}

		// ���ÿؼ�
		// iv.setImageDrawable(info.getLoadIcon());
		vh.tv1.setText(info.getLoadLabel());
		vh.tv2.setText(info.getPackageName());
		vh.tv3.setText(info.getVersionName());
		vh.tv3.setTextSize(10);
		// �ǵ�����check��״̬������û�����þ͵�����ˣ�����������������������������������������
//		vh.cb.setChecked(info.isCheck());
		System.out.println("----------" + info.isCheck());
		// ��ֹ��������
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
					// �ǵ�ˢ��
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
		// // �ǵ�ˢ��
		// notifyDataSetChanged();
		// }
		// });
		return convertView;
	}
}
