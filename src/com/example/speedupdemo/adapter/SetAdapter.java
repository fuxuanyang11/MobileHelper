package com.example.speedupdemo.adapter;

import java.util.ArrayList;


import com.example.speedupdemo.NotificationUtil;
import com.example.speedupdemo.SetParameter;
import com.example.speedupdemo.SharedUtil;
import com.example.speedupdemo.activity.BaseActivity;
import com.example.speedupdemo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class SetAdapter extends MyBaseAdapter<SetParameter>{

	Context context;
	
	boolean isOn;
	
	ViewHolder vh;
	
	LayoutInflater li;
	
	public static String[] strs = {"open","notification","message"};
	public SetAdapter(ArrayList<SetParameter> content,Context context) {
		super(content);
		this.context = context;
		li = LayoutInflater.from(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		vh = null;
		if (convertView==null) {
			vh = new ViewHolder();
			convertView = li.inflate(R.layout.set_listview_item, null);
			//设置ListView每行的高,如果是在getView()中（即控件），LayoutParams应该使用的是AbsListView包下的，
			//如果是在布局中，就使用ViewGroup包下的
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, BaseActivity.SCREEN_HEIGHT/12);
//			params.height= BaseActivity.SCREEN_HEIGHT/12;
			convertView.setLayoutParams(params);
			//获取控件
			vh.tv = (TextView) convertView.findViewById(R.id.set_item_text);
			vh.cb = (CheckBox) convertView.findViewById(R.id.set_item_cb);
			vh.iv = (ImageView) convertView.findViewById(R.id.set_item_iv);
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		final SetParameter sp = content.get(position);
		vh.tv.setText(sp.getText());
		if (position<=2) {
			vh.cb.setBackgroundResource(sp.getResourceId());
			vh.cb.setChecked(sp.isCheck());
			vh.cb.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					sp.setCheck(!sp.isCheck());
					SharedUtil util = new SharedUtil(context, "data");
					util.putValue(strs[position], sp.isCheck()+"");
					if (position==1) {
						if (sp.isCheck()) {
							NotificationUtil.show(context);
						}else {
							NotificationUtil.cancle(context);
						}
					}
				}
			});
		}else {
			vh.iv.setBackgroundResource(sp.getResourceId());
			vh.cb.setVisibility(View.INVISIBLE);
		}
		
//		if (position<=2) {
//			iv.setOnClickListener(new OnClickListener() {
//				//优化适配器的话，点击事件会出现错位，怎么解决？使用图片选择器！
//				@Override
//				public void onClick(View v) {
//					isOn = !isOn;
//					if (isOn) {
//						iv.setBackgroundResource(R.drawable.check_switch_on);
//					}else {
//						iv.setBackgroundResource(R.drawable.check_switch_off);
//					}
//				}
//			});
//		}
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tv;
		ImageView iv;
		CheckBox cb;
	}
	
}
