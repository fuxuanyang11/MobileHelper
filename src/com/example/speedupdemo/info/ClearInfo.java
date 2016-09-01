package com.example.speedupdemo.info;

import java.io.File;

import android.graphics.drawable.Drawable;

public class ClearInfo {

	/** 软件名称 */
	private String softChinesename;

	/** 文件大小 */
	private String fileSize;

	/** 软件图标 */
	private Drawable icon;

	/** CheckBox是否勾选 */
	private boolean isCheck;

	/** 垃圾文件路径 */
	private File file;

	private long size;

	public ClearInfo(String softChinesename, String fileSize, Drawable icon,
			File file, long size) {
		super();
		this.softChinesename = softChinesename;
		this.fileSize = fileSize;
		this.icon = icon;
		this.file = file;
		this.size = size;
	}

	public String getSoftChinesename() {
		return softChinesename;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public Drawable getIcon() {
		return icon;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public File getFile() {
		return file;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
