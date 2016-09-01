package com.example.speedupdemo.info;

public class TableInfo {
	/**tableµÄ_id*/
	private int id;
	
	/**tableµÄname*/
	private String name;
	
	/**tableµÄnumber*/
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
