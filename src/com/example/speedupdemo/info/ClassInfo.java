package com.example.speedupdemo.info;

/**
 * 数据库classlist的封装类
 * @author 傅炫阳
 *
 */
public class ClassInfo {
	
	/**classlist的idx*/
	private int id;
	
	/**classlist的name*/
	private String name;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ClassInfo(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
}
