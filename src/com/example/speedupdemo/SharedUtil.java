package com.example.speedupdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * �����������
 * @author ������
 *
 */
public class SharedUtil {
	SharedPreferences sp;
	
	public  SharedUtil(Context context,String name) {
		sp =  context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
	
	/**
	 * ��ȡ��������
	 * �������������Ͷ���String����
	 */
	public String getValue(String key,String defValue) {
		return sp.getString(key, defValue);
	}
	
	/**
	 * д�빲������
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putValue(String key,String value) {
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();//�������߳��ύ
	//API9֮�ϲ���,��commitһ��������û�з���ֵ���޷��ж��Ƿ��ύ�ɹ����ұ��������߳����ύ����
//		editor.apply();
		return editor.commit();
	}
	
}
