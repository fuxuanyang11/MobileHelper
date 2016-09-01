package com.example.speedupdemo.adapter;

import java.util.ArrayList;


import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<E> extends BaseAdapter{
	
	ArrayList<E> content;
	
	public MyBaseAdapter(ArrayList<E> content){
		this.content = content;
	}
	@Override
	public int getCount() {
		return content.size();
	}

	@Override
	public Object getItem(int position) {
	
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}


}
