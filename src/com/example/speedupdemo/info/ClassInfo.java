package com.example.speedupdemo.info;

/**
 * ���ݿ�classlist�ķ�װ��
 * @author ������
 *
 */
public class ClassInfo {
	
	/**classlist��idx*/
	private int id;
	
	/**classlist��name*/
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
