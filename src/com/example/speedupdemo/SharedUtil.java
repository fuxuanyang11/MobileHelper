package com.example.speedupdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 共享参数的类
 * @author 傅炫阳
 *
 */
public class SharedUtil {
	SharedPreferences sp;
	
	public  SharedUtil(Context context,String name) {
		sp =  context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}
	
	/**
	 * 获取共享数据
	 * 将所有数据类型都用String保存
	 */
	public String getValue(String key,String defValue) {
		return sp.getString(key, defValue);
	}
	
	/**
	 * 写入共享数据
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putValue(String key,String value) {
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();//可在子线程提交
	//API9之上才行,与commit一样，但是没有返回值，无法判断是否提交成功，且必须在主线程中提交数据
//		editor.apply();
		return editor.commit();
	}
	
}
