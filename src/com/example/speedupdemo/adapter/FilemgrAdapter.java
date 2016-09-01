package com.example.speedupdemo.adapter;

import java.util.ArrayList;

import com.example.speedupdemo.R;
import com.example.speedupdemo.activity.BaseActivity;
import com.example.speedupdemo.manager.FileManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FilemgrAdapter extends MyBaseAdapter<String>{
	
	Context context;
	
	LayoutInflater li;
	
	public boolean findOver;
	
	public FilemgrAdapter(ArrayList<String> content,Context context) {
		super(content);
		this.context = context;
		li = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView==null) {
			vh = new ViewHolder();
			convertView = li.inflate(R.layout.filemgr_listview_item, null);
			LayoutParams params = new LayoutParams(BaseActivity.SCREEN_WIDTH, BaseActivity.SCREEN_HEIGHT/12);
			convertView.setLayoutParams(params);
			vh.file = (TextView) convertView.findViewById(R.id.fielmgr_item_tv1);
			vh.size = (TextView) convertView.findViewById(R.id.fielmgr_item_tv2);
			vh.bg = (TextView) convertView.findViewById(R.id.filemgr_item_bg);
			vh.pb = (ProgressBar) convertView.findViewById(R.id.filemgr_item_progress);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.file.setText(FileManager.keys[position]);
		vh.size.setText(content.get(position));
		vh.bg.setVisibility(View.INVISIBLE);
		if (findOver) {
			vh.pb.setVisibility(View.INVISIBLE);
			vh.bg.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView file;
		TextView size;
		TextView bg;
		ProgressBar pb;
	}

}
