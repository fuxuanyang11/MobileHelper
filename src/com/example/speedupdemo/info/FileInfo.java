package com.example.speedupdemo.info;

public class FileInfo {
	
	/**�ļ�ͼ������Ӧ���ַ���*/
	private String icon;
	
	/**�ļ�������*/
	private String name;
	
	/**�ļ�������*/
	private String date;
	
	/**�ļ��Ĵ�С���ַ�����ʽ��*/
	private String sizeStr;
	
	/**�ļ��Ĵ�С(longֵ)*/
	private long size;
	
	/**�ļ���·��*/
	private String path;
	
	/**checkBox�Ƿ񱻵��*/
	private boolean isChecked;
	
	/**ɾ�����ļ�������*/
	private String type;
	
	/**�ļ��򿪷�ʽ*/
	private String mime;

	public FileInfo(String icon, String name, String date, String sizeStr,
			long size,String path,String type,String mime) {
		super();
		this.icon = icon;
		this.name = name;
		this.date = date;
		this.sizeStr = sizeStr;
		this.size = size;
		this.path = path;
		this.type = type;
		this.mime = mime;
	}
	
	public String getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}

	public String getDate() {
		return date;
	}

	public String getSizeStr() {
		return sizeStr;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getPath() {
		return path;
	}

	public String getType() {
		return type;
	}

	public String getMime() {
		return mime;
	}

}
