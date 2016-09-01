package com.example.speedupdemo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build;
import android.util.LruCache;
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
import com.example.speedupdemo.info.FileInfo;
import com.example.speedupdemo.manager.FileManager;

public class FileInFoAdapter extends MyBaseAdapter<FileInfo> {

	Context context;

	LayoutInflater li;

	FileManager fileManager;

	/** 将图片放入缓存，从缓冲中获取比解析快 */
	// 键-->通过什么方式获取缓冲中的内容，比如通过路径获取，上一次通过该路径获取，下一次就不需要重新获取
	LruCache<String, Bitmap> lruCache;

	public FileInFoAdapter(ArrayList<FileInfo> content, Context context) {
		super(content);
		this.context = context;
		li = LayoutInflater.from(context);
		fileManager = FileManager.getInstance(context);

		if (Build.VERSION.SDK_INT > 12) {// 要版本12以上
			// 匿名内部类
			lruCache = new LruCache<String, Bitmap>(/* 每个缓存的最大值 */1024 * 1024) {
				/**
				 * 根据每次找到的图片设置缓存的大小 多添加一条缓存时，就会执行此方法。根据方法中的返回值设置缓存的大小
				 */
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getByteCount();// 返回该图片的大小（以字节为单位）
				}
			};
		} else {
			lruCache = new LruCache<String, Bitmap>(/* 每个缓存的最大值 */1024 * 1024);
		}
	}

	/**
	 * 勾选所有CheckBox
	 */
	public void allChecked() {
		for (int i = 0; i < content.size(); i++) {
			content.get(i).setChecked(true);
		}
		notifyDataSetChanged();
	}

	public void cancleChecked() {
		for (int i = 0; i < content.size(); i++) {
			content.get(i).setChecked(false);
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = li.inflate(R.layout.fileinfo_listview_item, null);
			LayoutParams params = new LayoutParams(BaseActivity.SCREEN_WIDTH,
					BaseActivity.SCREEN_HEIGHT / 12);
//			LayoutParams iconParms = (LayoutParams) vh.icon.getLayoutParams();
//			iconParms.width = BaseActivity.SCREEN_WIDTH/
			convertView.setLayoutParams(params);
			vh.cb = (CheckBox) convertView.findViewById(R.id.fileinfo_item_cb);
			vh.icon = (ImageView) convertView
					.findViewById(R.id.fileinfo_item_icon);
			vh.name = (TextView) convertView
					.findViewById(R.id.fileinfo_item_name);
			vh.date = (TextView) convertView
					.findViewById(R.id.fileinfo_item_date);
			vh.size = (TextView) convertView
					.findViewById(R.id.fileinfo_item_size);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final FileInfo info = content.get(position);
		// int resId = context.getResources().getIdentifier(info.getIcon(),
		// "drawable", context.getPackageName());
		// vh.icon.setImageResource(resId);
		vh.icon.setImageBitmap(setIncon(info));
		vh.name.setText(info.getName());
		vh.date.setText(info.getDate());
		vh.size.setText(info.getSizeStr());
		vh.size.setTextSize(8);
		vh.cb.setChecked(info.isChecked());
		vh.cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				info.setChecked(!info.isChecked());
			}
		});
		// notifyDataSetChanged();不用写也可以？
		return convertView;
	}

	/**
	 * 设置图片的预览图
	 * 
	 * @param info
	 * @return
	 */
	public Bitmap setIncon(FileInfo info) {
		Bitmap icon = null;
		icon = lruCache.get(info.getPath());
		if (icon != null) {
			return icon;
		}
		if (info.getType().equals(FileManager.TYPE_IMAGE)) {// 只有图片才有预览图
			Options options = new Options();
			options.inJustDecodeBounds = true;// 只用于测量边界大小
			BitmapFactory.decodeFile(info.getPath(), options);// 测量该图片
			// 获取图片的宽高
			int width = options.outWidth;
			int height = options.outHeight;
			int max = (width > height) ? width : height;
			if (max > BaseActivity.SCREEN_HEIGHT / 12) {
				options.inSampleSize = max / BaseActivity.SCREEN_HEIGHT / 12;
			} else {
				options.inSampleSize = 1;// 小于等于1跟1一样
			}
			options.inJustDecodeBounds = false;// 设置为false，才有压缩图片的功能
			icon = BitmapFactory.decodeFile(info.getPath(), options);
			Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_app);
			icon=Bitmap.createScaledBitmap(icon, bitmap.getWidth(), bitmap.getHeight(), true);
		}
		if (icon == null) {
			// 如果还是为空，就赋值原先的图片
			String iconStr = info.getIcon();
			int resId = context.getResources().getIdentifier(iconStr,
					"drawable", context.getPackageName());
			icon = BitmapFactory.decodeResource(context.getResources(), resId);
		}
		
		// 将解析出来的图标加入缓存，下一次就不需要重新获取
		lruCache.put(info.getPath(), icon);
		return icon;
	}

	class ViewHolder {
		CheckBox cb;
		ImageView icon;
		TextView name;
		TextView date;
		TextView size;
	}

}
