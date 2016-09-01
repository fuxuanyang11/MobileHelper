package com.example.speedupdemo.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.speedupdemo.info.ClassInfo;
import com.example.speedupdemo.info.TableInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * �������ݿ���ࡢ�����ݿ���в���
 * 
 * @author ������
 * 
 */
public class CommonManager {

	String filepath = "";

	File file;

	public CommonManager(Context context) {
		filepath = "data" + File.separator + Environment.getDataDirectory()
				+ File.separator + context.getPackageName() + File.separator
				+ "commonnum.db";

		file = new File(filepath);
	}

	/**
	 * �½��ļ�
	 * 
	 * @throws IOException
	 */
	public void createFile(InputStream inputStream) throws IOException {
		if (file.exists()) {// ������ھ�ֱ�ӽ�������
			 return;
//			file.delete();
		}
		// ʹ���ֽ�����ȡ
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int length = 0;
			while ((length = inputStream.read(b)) != -1) {
				fos.write(b, 0, length);
			}
		} finally {
			if (fos != null) {
				fos.close();
			}
		}

	}

	/**
	 * ��ȡ����
	 */
	public ArrayList<ClassInfo> getData() {
		ArrayList<ClassInfo> infos = new ArrayList<ClassInfo>();
		// ����·�������ݿ�
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(file,
				null);
		// ��ѯ����
		Cursor cursor = database.rawQuery("select*from classlist", null);
		while (cursor.moveToNext()) {
			int idIndex = cursor.getColumnIndex("idx");
			int id = cursor.getInt(idIndex);
			int nameIndex = cursor.getColumnIndex("name");
			String name = cursor.getString(nameIndex);
			ClassInfo info = new ClassInfo(id, name);
			infos.add(info);
		}
		// �ǵùر��α�
		cursor.close();
		return infos;
	}
	
	/**
	 * ��ȡ��
	 */
	public ArrayList<TableInfo> getTable(int id) {
		
		ArrayList<TableInfo> infos = new ArrayList<TableInfo>();
		// ����·�������ݿ�
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(file,
				null);
		// ��ѯ����
		Cursor cursor = database.rawQuery("select*from table"+id, null);
		while (cursor.moveToNext()) {
			
			int nameIndex = cursor.getColumnIndex("name");
			String name = cursor.getString(nameIndex);
			int numberIndex = cursor.getColumnIndex("number");
			String number = cursor.getString(numberIndex);
			TableInfo info = new TableInfo(name, number);
			infos.add(info);
		}
		// �ǵùر��α�
		cursor.close();
		return infos;
		
	}
	
}
