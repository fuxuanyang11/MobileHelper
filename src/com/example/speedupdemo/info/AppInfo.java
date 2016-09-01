package com.example.speedupdemo.info;

import android.graphics.drawable.Drawable;

public class AppInfo {
	private Drawable icon;
	private String label;
	private String sizeText;
	// �Ƿ���ϵͳ����
	private boolean isSystem;
	// checkBox�Ƿ���
	private boolean isCheck;
	// ������
	private String processName;
	
	
//	private int softwareIcon;
	//ͼ��
	private Drawable loadIcon;
	//����
	private String loadLabel;
	//����
	private String packageName;
	//�汾
	private String versionName;
	
	
	/**
	 * ��ȡ����Ĺ��췽��
	 * @param loadIcon
	 * @param loadLabel
	 * @param packageName
	 * @param versionName
	 */
	public AppInfo(Drawable loadIcon, String loadLabel, String packageName,
			String versionName) {
		super();
		this.loadIcon = loadIcon;
		this.loadLabel = loadLabel;
		this.packageName = packageName;
		this.versionName = versionName;
	}
	
	/**
	 * ��ȡ�ڴ�Ĺ��췽��
	 * @param icon
	 * @param label
	 * @param sizeText
	 * @param isSystem
	 * @param processName
	 */
	public AppInfo(Drawable icon, String label, String sizeText,
			boolean isSystem, String processName) {
		super();
		this.icon = icon;
		this.label = label;
		this.sizeText = sizeText;
		this.isSystem = isSystem;
		this.processName = processName;
	}
	
	public Drawable getLoadIcon() {
		return loadIcon;
	}

	public String getLoadLabel() {
		return loadLabel;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getVersionName() {
		return versionName;
	}

	public String getProcessName() {
		return processName;
	}

	public boolean isSystem() {
		return isSystem;
	}

	public Drawable getIcon() {
		return icon;
	}

	public String getLabel() {
		return label;
	}

	public String getSizeText() {
		return sizeText;
	}

	

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	
}
