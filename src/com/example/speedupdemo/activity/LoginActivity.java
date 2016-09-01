package com.example.speedupdemo.activity;

import java.util.HashMap;

import cn.sharesdk.facebook.i;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.example.speedupdemo.R;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class LoginActivity extends BaseActivity implements OnClickListener, PlatformActionListener{
	
	
	public String name;
	
	public String icon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alogin__layout);
		ShareSDK.initSDK(this);
		LinearLayout weibo = (LinearLayout) findViewById(R.id.login_weibo);
		LinearLayout qq = (LinearLayout) findViewById(R.id.login_qq);
		LinearLayout wechat = (LinearLayout) findViewById(R.id.login_wechat);
		weibo.setOnClickListener(this);
		qq.setOnClickListener(this);
		wechat.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.login_weibo) {
			Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
			authorize(weibo);
			PlatformDb db = weibo.getDb();
			name = db.getUserName();
			icon = db.getUserIcon();
//			if (weiboName!=null&&weiboIcon!=null) {
//				startAction(InstructionActivity.class, null, null);
//			}
		}
		if (v.getId()==R.id.login_qq) {
			Platform qq = ShareSDK.getPlatform(QQ.NAME);
			authorize(qq);
			PlatformDb db = qq.getDb();
			name = db.getUserName();
			icon = db.getUserIcon();
//			if (qqName!=null&&qqIcon!=null) {
//				startAction(InstructionActivity.class, null, null);
//			}
		}
		if (v.getId()==R.id.login_wechat) {
			Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
			authorize(wechat);
			PlatformDb db = wechat.getDb();
			name = db.getUserName();
			icon = db.getUserIcon();
//			if (wechatName!=null&&wechatIcon!=null) {
//				startAction(InstructionActivity.class, null, null);
//			}
		}
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		if (name!=null&&icon!=null) {
			startAction(InstructionActivity.class, null, null);
			System.out.println(name+"---------------"+icon);
			finish();
		}
	}
	
	private void authorize(Platform plat) {
		if (plat.isValid()) {
			String userId = plat.getDb().getUserId();
			if (!TextUtils.isEmpty(userId)) {
				// UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
				// login(plat.getName(), userId, null);
				return;
			}
		}
		plat.setPlatformActionListener(this);
		plat.SSOSetting(true);
		plat.showUser(null);
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
	}
}
