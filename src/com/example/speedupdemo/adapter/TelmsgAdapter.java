package com.example.speedupdemo.adapter;

import java.util.ArrayList;

import com.example.speedupdemo.R;
import com.example.speedupdemo.activity.BaseActivity;
import com.example.speedupdemo.info.ClassInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;

public class TelmsgAdapter extends MyBaseAdapter<ClassInfo> {

	Context context;
	LayoutInflater li;
	int[] resId = { R.drawable.telmsg__red_selector,
			R.drawable.telmsg_yellow_selsctor,
			R.drawable.telmsg_green_selector };

	public TelmsgAdapter(ArrayList<ClassInfo> content, Context context) {
		super(content);
		this.context = context;
		li = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		ClassInfo info = content.get(position);
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = li.inflate(R.layout.gridview_item, null);
			// 设置每一行的高度
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					BaseActivity.SCREEN_HEIGHT / 8);
			convertView.setLayoutParams(params);
			vh.iv = (TextView) convertView.findViewById(R.id.gridview_iv);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.iv.setText(info.getName());
		//只需要用三个就可以
		vh.iv.setBackgroundResource(resId[position % 3]);
		return convertView;
	}

	public class ViewHolder {
		public TextView iv;
	}

}
