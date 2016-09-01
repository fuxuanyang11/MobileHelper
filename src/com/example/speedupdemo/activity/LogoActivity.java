package com.example.speedupdemo.activity;

import com.example.speedupdemo.NotificationUtil;
import com.example.speedupdemo.SharedUtil;
import com.example.speedupdemo.adapter.SetAdapter;
import com.example.speedupdemo.view.LogoView;

import android.os.Bundle;

public class LogoActivity extends BaseActivity {

	// boolean isShow = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogoView lv = new LogoView(this);
		setContentView(lv);
		SharedUtil util = new SharedUtil(this, "data");
		String value = util.getValue("in", true + "");
		boolean isShow = Boolean.parseBoolean(value);
		String value2 = util.getValue(SetAdapter.strs[1], false + "");
		boolean isCheck = Boolean.parseBoolean(value2);
		// û��Ч��:��show()Ӧ���Ǹ�Ϊfalse��cancle()��Ϊtrue�Ŷԡ�����Ҳ���У����checkBoxÿ�ζ����ó�false
		// Ҫ�ٶ�һ���жϣ��Ƿ��֪ͨ��
		if (isShow && isCheck) {
			NotificationUtil.show(this);
		}
	}
}
