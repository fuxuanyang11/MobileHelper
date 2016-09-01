package com.example.speedupdemo.adapter;

import java.util.ArrayList;

import com.example.speedupdemo.ViewHolder;
import com.example.speedupdemo.activity.BaseActivity;
import com.example.speedupdemo.info.AppInfo;
import com.example.speedupdemo.R;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

//��Ԫ������ƥ�䵽�б��Ӧ����
public class SpeedUpAdapter extends MyBaseAdapter<AppInfo> {
	// ��ɾ���͵�����������һ�����ϡ�
	// ArrayList<AppInfo> content;
	Context context;
	Matrix matrix;
	float sx;
	float sy;
	ActivityManager activityManager;
	LayoutInflater li;
	public boolean fast;

	public SpeedUpAdapter(ArrayList<AppInfo> content, Context context) {
		super(content);
		this.context = context;
		// ��ȡActivityManager����
		activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		matrix = new Matrix();
		li = LayoutInflater.from(context);
	}

	/**
	 * ��ʾϵͳ����
	 */
	public void showSystem(ArrayList<AppInfo> systemApp) {
		content.addAll(systemApp);
		// �ǵ�ˢ��!!!!!
		notifyDataSetChanged();
	}

	/**
	 * �Ƴ�ϵͳ����
	 */
	public void removeSystem(ArrayList<AppInfo> systemApp) {
		content.removeAll(systemApp);
		notifyDataSetChanged();
	}

	/**
	 * ��ѡ����checkBox
	 */
	public void allTrue(ArrayList<AppInfo> infos) {
		// �������ϰ�������������������������������
		for (int i = 0; i < infos.size(); i++) {
			infos.get(i).setCheck(true);
		}
		notifyDataSetChanged();
	}

	/**
	 * ����checkBox������ѡ
	 */
	public void allFalse(ArrayList<AppInfo> infos) {
		for (int i = 0; i < infos.size(); i++) {
			infos.get(i).setCheck(false);
		}
		notifyDataSetChanged();
	}

	/**
	 * ɾ����ѡ�Ľ���
	 */
	public void removeCheck(ArrayList<AppInfo> infos) {
		for (int i = 0; i < infos.size(); i++) {
			if (infos.get(i).isCheck()) {
				activityManager.killBackgroundProcesses(infos.get(i)
						.getProcessName());
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			// ��ȡ���ֹ�����,����ڹ��췽����
			vh = new ViewHolder();
			// ��ȡview
			convertView = li.inflate(R.layout.speed_list_item, null);

			// ��ȡ�ؼ�
			/* ImageView iv */vh.iv = (ImageView) convertView
					.findViewById(R.id.imageview);
			 vh.tv1 = (TextView) convertView.findViewById(R.id.textview1);
			 vh.tv2 = (TextView) convertView.findViewById(R.id.textview2);
			 vh.tv3 = (TextView) convertView.findViewById(R.id.textview3);
			 vh.cb = (CheckBox) convertView.findViewById(R.id.checkbox);
			//convertViewΪ��ʱset
			convertView.setTag(vh);
		} else {//convertView��Ϊ��
			vh = (ViewHolder) convertView.getTag();
		}
		// ��ȡAppInfo����
		final AppInfo info = content.get(position);
//		Bitmap bitmap = null;
		// һ����ͼƬ�ͺ������Ƿ������쳣������32λ���������õ�������ı�����
		if (fast) {
			vh.iv.setImageResource(R.drawable.icon_bmp);
		}else {
			BitmapDrawable bd = (BitmapDrawable) info.getIcon();
			Bitmap bitmap = bd.getBitmap();
			sx = 1.0f * BaseActivity.SCREEN_HEIGHT / 12 / bitmap.getHeight();
			sy = 1.0f * BaseActivity.SCREEN_HEIGHT / 12 / bitmap.getHeight();
			matrix.setScale(sx, sy);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			vh.iv.setImageBitmap(bitmap);
		}
		

		// iv.setImageDrawable(info.getIcon());
		
		vh.tv1.setText(info.getLabel());
		vh.tv2.setText(info.getSizeText());
		// �ǵ�����check��״̬������
		vh.cb.setChecked(info.isCheck());
		// ����checkBox�ļ����¼�,��������������,����ʹ��setOnCheckedChangeListener
		//��Ϊ���¼֮ǰ�����checkBox��״̬
//		vh.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		vh.cb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (v.getId()==R.id.checkbox) {
					if (info.isCheck()) {
						info.setCheck(false);
					} else {
						info.setCheck(true);
					}
					// ˢ��
					notifyDataSetChanged();
				}
			}
		});

//			@Override
//			/**
//			 * @param isChecked��ǰ��CheckBox״̬��ѡ����Ϊtrue
//			 */
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				if (info.isCheck()) {
//					info.setCheck(false);
//				} else {
//					info.setCheck(true);
//				}
//				// ˢ��
//				notifyDataSetChanged();
//			}
//		});
		if (info.isSystem()) {
			//�Ż�����Щû��ʾ���֣�ԭ���õ���֮ǰ��TextView���������صģ�������Ҫ�ٽ�����ʾ����
			vh.tv3.setVisibility(View.VISIBLE);
			vh.tv3.setText("ϵͳ����");
			vh.tv3.setTextColor(Color.RED);
		} else {
			vh.tv3.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	

}
