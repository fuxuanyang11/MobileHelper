package com.example.speedupdemo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;

import com.example.speedupdemo.R;
import com.example.speedupdemo.activity.BaseActivity;
import com.example.speedupdemo.info.TableInfo;

public class TableAdapter extends MyBaseAdapter<TableInfo>{
	
	Context context;
	
	LayoutInflater li;
	
	public TableAdapter(ArrayList<TableInfo> content,Context context) {
		super(content);
		this.context = context;
		li = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView==null) {
			vh = new ViewHolder();
			convertView = li.inflate(R.layout.table_listview_item, null);
			//设置每行的高度
			LayoutParams params = new LayoutParams(BaseActivity.SCREEN_WIDTH, BaseActivity.SCREEN_HEIGHT/20);
			convertView.setLayoutParams(params);
			vh.name = (TextView) convertView.findViewById(R.id.table_name);
			vh.number = (TextView) convertView.findViewById(R.id.table_number);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		TableInfo info = content.get(position);
		vh.name.setText(info.getName());
		vh.number.setText(info.getNumber());
		
		return convertView;
	}
	
	class ViewHolder{
		TextView name;
		TextView number;
	}
	
	
}
