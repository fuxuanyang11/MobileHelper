package com.example.speedupdemo.info;

public class FileInfo {
	
	/**文件图标所对应的字符串*/
	private String icon;
	
	/**文件的名称*/
	private String name;
	
	/**文件的日期*/
	private String date;
	
	/**文件的大小（字符串形式）*/
	private String sizeStr;
	
	/**文件的大小(long值)*/
	private long size;
	
	/**文件的路径*/
	private String path;
	
	/**checkBox是否被点击*/
	private boolean isChecked;
	
	/**删除的文件的类型*/
	private String type;
	
	/**文件打开方式*/
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
