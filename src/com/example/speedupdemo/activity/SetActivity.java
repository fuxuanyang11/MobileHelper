package com.example.speedupdemo.activity;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.speedupdemo.SetParameter;
import com.example.speedupdemo.SharedUtil;
import com.example.speedupdemo.adapter.SetAdapter;
import com.example.speedupdemo.view.TitleLayout;
import com.example.speedupdemo.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SetActivity extends BaseActivity {
	ArrayList<SetParameter> sets = new ArrayList<SetParameter>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_layout);
		TitleLayout tl = (TitleLayout) findViewById(R.id.set_title);
		tl.setTitle("����");
		init();
		SetAdapter adapter = new SetAdapter(sets, this);
		ListView listView = (ListView) findViewById(R.id.set_listview);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position==3) {
					startAction(InstructionActivity.class, "help", true+"");
				}
				if (position==7) {
					startAction(AboutUsActivity.class, null, null);
				}
				if (position==8) {
					AlertDialog.Builder builder =new  AlertDialog.Builder(SetActivity.this);
					builder.setMessage("�Ƿ��˳��ֻ�����");
					builder.setCancelable(false);
					builder.setPositiveButton("��", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					builder.setNegativeButton("��", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ActivityCollects.finishAllActivity();
						}
					});
					builder.show();
				}
				if (position==5) {
					showShare();
				}
			}
		});
	}

	String[] strs = { "open", "notification", "message" };
	
	/**
	 * ��ʼ��
	 */
	public void init() {
		SharedUtil util = new SharedUtil(this, "data");
		SetParameter sp1 = new SetParameter(R.drawable.set_selector, "��������",
				Boolean.parseBoolean(util.getValue(strs[0], false + "")));
		sets.add(sp1);
		SetParameter sp2 = new SetParameter(R.drawable.set_selector, "֪ͨͼ��",
				Boolean.parseBoolean(util.getValue(strs[1], false + "")));
		sets.add(sp2);
		SetParameter sp3 = new SetParameter(R.drawable.set_selector, "��Ϣ����",
				Boolean.parseBoolean(util.getValue(strs[2], false + "")));
		sets.add(sp3);
		SetParameter sp4 = new SetParameter(R.drawable.ic_arrows_right, "����˵��",
				false);
		sets.add(sp4);
		SetParameter sp5 = new SetParameter(R.drawable.ic_arrows_right, "�������",
				false);
		sets.add(sp5);
		SetParameter sp6 = new SetParameter(R.drawable.ic_arrows_right, "���ѷ���",
				false);
		sets.add(sp6);
		SetParameter sp7 = new SetParameter(R.drawable.ic_arrows_right, "�汾����",
				false);
		sets.add(sp7);
		SetParameter sp8 = new SetParameter(R.drawable.ic_arrows_right, "��������",
				false);
		sets.add(sp8);
		SetParameter sp9 = new SetParameter(R.drawable.ic_arrows_right, "�˳�����",
				false);
		sets.add(sp9);
	}
	
	/**
	 * ���ѷ���ķ���
	 */
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //�ر�sso��Ȩ
		 oks.disableSSOWhenAuthorize(); 

		// ����ʱNotification��ͼ�������  2.5.9�Ժ�İ汾�����ô˷���
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		 oks.setTitle("�ֻ�����");
		 // titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		 oks.setText("ˡ��ֱ�ԣ������ĸ�λ��������");
		 // imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		 //oks.setImagePath("/sdcard/test.jpg");//ȷ��SDcard������ڴ���ͼƬ
		 // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		 oks.setUrl("http://sharesdk.cn");
		 // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		 oks.setComment("���ǲ��������ı�");
		 // site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		 oks.setSiteUrl("http://sharesdk.cn");

		// ��������GUI
		 oks.show(this);
		 }
}
