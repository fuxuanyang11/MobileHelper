package com.example.speedupdemo;

public class SetParameter {
	
	private int resourceId;
	
	private String text;
	
	private boolean isCheck;

	public SetParameter(int resourceId, String text,boolean isCheck) {
		super();
		this.resourceId = resourceId;
		this.text = text;
		this.isCheck = isCheck;
	}
	
	
	
	public int getResourceId() {
		return resourceId;
	}

	public String getText() {
		return text;
	}



	public boolean isCheck() {
		return isCheck;
	}



	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
}
