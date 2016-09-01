package com.example.speedupdemo.adapter;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.speedupdemo.R;
import com.example.speedupdemo.activity.BaseActivity;
import com.example.speedupdemo.info.ClearInfo;
import com.example.speedupdemo.manager.ClearPathManager;

public class ClearAdapter extends MyBaseAdapter<ClearInfo> {

	Context context;

	LayoutInflater li;

	public ClearAdapter(ArrayList<ClearInfo> content, Context context) {
		super(content);
		this.context = context;
		li = LayoutInflater.from(context);
	}

	/**
	 * 勾选所有CheckBox
	 */
	public void allChecked() {
		for (ClearInfo info : content) {
			info.setCheck(true);
		}
		notifyDataSetChanged();
	}

	/**
	 * 全部不勾选
	 */
	public void noChecked() {
		for (ClearInfo info : content) {
			info.setCheck(false);
		}
		notifyDataSetChanged();
	}

	/**
	 * 一键清理的方法
	 */
	public void clear() {
		for (ClearInfo info : content) {
			if (info.isCheck()) {
				long del = delete(info.getFile());// 获取删除的文件的大小
				ClearPathManager.allSize-=del;//所有的垃圾减去删除的垃圾
				info.setSize(info.getSize() - del);//设置垃圾的大小等于获取的垃圾减去删除的垃圾
				info.setFileSize(Formatter.formatFileSize(context,
						info.getSize()));//重新获取一次

			}
		}
		// notifyDataSetChanged();//记得刷新，因为是耗时操作，换到子线程，子线程不能刷新，但是可以重新获取一次数据
	}

	// 08-13 14:30:56.677: W/OpenGLRenderer(28865): Failed to choose config with
	// EGL_SWAP_BEHAVIOR_PRESERVED, retrying without...

	long delSize;

	public long delete(File file) {
		delSize = 0;//每次获取删除的文件要重新置0
		return deleteFile(file);
	}

	/**
	 * 删除文件夹的方法
	 */
	public long deleteFile(File file) {
		if (file.isFile()) {
			file.delete();
		} else {
			File[] listFiles = file.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				if (listFiles[i].isFile()) {
					delSize += listFiles[i].length();
//					System.out.println(listFiles[i].getAbsolutePath()
//							+ "---------" + delSize);
					listFiles[i].delete();
				} else {
					deleteFile(listFiles[i]);
					listFiles[i].delete();
				}
			}
		}
		return delSize;
	}

	/**
	 * 获取所有垃圾文件大小
	 */
	public String getAllSize() {
		return Formatter.formatFileSize(context, ClearPathManager.allSize);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = li.inflate(R.layout.clear_listview_item, null);
			// 设置每行高
			LayoutParams params = new LayoutParams(BaseActivity.SCREEN_WIDTH,
					BaseActivity.SCREEN_HEIGHT / 10);
			convertView.setLayoutParams(params);
			vh.check = (CheckBox) convertView
					.findViewById(R.id.clear_item_check);
			vh.icon = (ImageView) convertView
					.findViewById(R.id.clear_item_icon);
			vh.name = (TextView) convertView.findViewById(R.id.clear_item_name);
			vh.size = (TextView) convertView.findViewById(R.id.clear_item_size);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final ClearInfo clearInfo = content.get(position);
		vh.icon.setImageDrawable(clearInfo.getIcon());
		vh.name.setText(clearInfo.getSoftChinesename());
		vh.size.setText(clearInfo.getFileSize() + "");
		// 设置checkBox的状态
		vh.check.setChecked(clearInfo.isCheck());
		vh.check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.clear_item_check) {
					if (clearInfo.isCheck()) {
						clearInfo.setCheck(false);
					} else {
						clearInfo.setCheck(true);
					}
					notifyDataSetChanged();
				}
			}
		});

		return convertView;
	}

	class ViewHolder {
		CheckBox check;
		ImageView icon;
		TextView name;
		TextView size;
	}

}
