package com.example.speedupdemo.adapter;

import java.util.ArrayList;

import com.example.speedupdemo.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class InstructionAdapter extends PagerAdapter {

	ArrayList<View> views;
	Context context;
	LayoutInflater li;
	String[] strs = { "applist", "banner", "creditswall", "force",
			"notification" };

	public InstructionAdapter(ArrayList<View> views, Context context) {
		this.views = views;
		this.context = context;
		li = LayoutInflater.from(context);
	}

	public void setContent(Context context) {
		for (int i = 0; i < strs.length; i++) {
			View view = li.inflate(R.layout.instruction_viewpager_item, null);
			ImageView iv = (ImageView) view.findViewById(R.id.item_viewpager);
			String name = "adware_style_"+strs[i];
			int id = view.getResources().getIdentifier(name, "drawable",context.getPackageName());
			iv.setImageResource(id);
			views.add(view);
		}
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// return super.instantiateItem(container, position);
		container.addView(views.get(position));
		return views.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

}
