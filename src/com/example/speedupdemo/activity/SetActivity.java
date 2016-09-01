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
		tl.setTitle("设置");
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
					builder.setMessage("是否退出手机助手");
					builder.setCancelable(false);
					builder.setPositiveButton("否", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					builder.setNegativeButton("是", new OnClickListener() {
						
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
	 * 初始化
	 */
	public void init() {
		SharedUtil util = new SharedUtil(this, "data");
		SetParameter sp1 = new SetParameter(R.drawable.set_selector, "开机启动",
				Boolean.parseBoolean(util.getValue(strs[0], false + "")));
		sets.add(sp1);
		SetParameter sp2 = new SetParameter(R.drawable.set_selector, "通知图标",
				Boolean.parseBoolean(util.getValue(strs[1], false + "")));
		sets.add(sp2);
		SetParameter sp3 = new SetParameter(R.drawable.set_selector, "消息推送",
				Boolean.parseBoolean(util.getValue(strs[2], false + "")));
		sets.add(sp3);
		SetParameter sp4 = new SetParameter(R.drawable.ic_arrows_right, "帮助说明",
				false);
		sets.add(sp4);
		SetParameter sp5 = new SetParameter(R.drawable.ic_arrows_right, "意见反馈",
				false);
		sets.add(sp5);
		SetParameter sp6 = new SetParameter(R.drawable.ic_arrows_right, "好友分享",
				false);
		sets.add(sp6);
		SetParameter sp7 = new SetParameter(R.drawable.ic_arrows_right, "版本更新",
				false);
		sets.add(sp7);
		SetParameter sp8 = new SetParameter(R.drawable.ic_arrows_right, "关于我们",
				false);
		sets.add(sp8);
		SetParameter sp9 = new SetParameter(R.drawable.ic_arrows_right, "退出助手",
				false);
		sets.add(sp9);
	}
	
	/**
	 * 好友分享的方法
	 */
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle("手机助手");
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("恕我直言，在座的各位都是垃圾");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		 oks.show(this);
		 }
}
