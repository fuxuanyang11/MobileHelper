package com.example.speedupdemo.info;

public class TableInfo {
	/**table��_id*/
	private int id;
	
	/**table��name*/
	private String name;
	
	/**table��number*/
	private String number;
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public TableInfo(String name,String number) {
		super();
		this.name = name;
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

}
