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

	/** ��ͼƬ���뻺�棬�ӻ����л�ȡ�Ƚ����� */
	// ��-->ͨ��ʲô��ʽ��ȡ�����е����ݣ�����ͨ��·����ȡ����һ��ͨ����·����ȡ����һ�ξͲ���Ҫ���»�ȡ
	LruCache<String, Bitmap> lruCache;

	public FileInFoAdapter(ArrayList<FileInfo> content, Context context) {
		super(content);
		this.context = context;
		li = LayoutInflater.from(context);
		fileManager = FileManager.getInstance(context);

		if (Build.VERSION.SDK_INT > 12) {// Ҫ�汾12����
			// �����ڲ���
			lruCache = new LruCache<String, Bitmap>(/* ÿ����������ֵ */1024 * 1024) {
				/**
				 * ����ÿ���ҵ���ͼƬ���û���Ĵ�С �����һ������ʱ���ͻ�ִ�д˷��������ݷ����еķ���ֵ���û���Ĵ�С
				 */
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getByteCount();// ���ظ�ͼƬ�Ĵ�С�����ֽ�Ϊ��λ��
				}
			};
		} else {
			lruCache = new LruCache<String, Bitmap>(/* ÿ����������ֵ */1024 * 1024);
		}
	}

	/**
	 * ��ѡ����CheckBox
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
		// notifyDataSetChanged();����дҲ���ԣ�
		return convertView;
	}

	/**
	 * ����ͼƬ��Ԥ��ͼ
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
		if (info.getType().equals(FileManager.TYPE_IMAGE)) {// ֻ��ͼƬ����Ԥ��ͼ
			Options options = new Options();
			options.inJustDecodeBounds = true;// ֻ���ڲ����߽��С
			BitmapFactory.decodeFile(info.getPath(), options);// ������ͼƬ
			// ��ȡͼƬ�Ŀ��
			int width = options.outWidth;
			int height = options.outHeight;
			int max = (width > height) ? width : height;
			if (max > BaseActivity.SCREEN_HEIGHT / 12) {
				options.inSampleSize = max / BaseActivity.SCREEN_HEIGHT / 12;
			} else {
				options.inSampleSize = 1;// С�ڵ���1��1һ��
			}
			options.inJustDecodeBounds = false;// ����Ϊfalse������ѹ��ͼƬ�Ĺ���
			icon = BitmapFactory.decodeFile(info.getPath(), options);
			Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_app);
			icon=Bitmap.createScaledBitmap(icon, bitmap.getWidth(), bitmap.getHeight(), true);
		}
		if (icon == null) {
			// �������Ϊ�գ��͸�ֵԭ�ȵ�ͼƬ
			String iconStr = info.getIcon();
			int resId = context.getResources().getIdentifier(iconStr,
					"drawable", context.getPackageName());
			icon = BitmapFactory.decodeResource(context.getResources(), resId);
		}
		
		// ������������ͼ����뻺�棬��һ�ξͲ���Ҫ���»�ȡ
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
