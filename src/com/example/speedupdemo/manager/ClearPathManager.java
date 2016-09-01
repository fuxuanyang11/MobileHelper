package com.example.speedupdemo.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.speedupdemo.R;
import com.example.speedupdemo.info.ClearInfo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.format.Formatter;

public class ClearPathManager {

	String path = "";

	File file;

	PackageManager packageManager;


	public static long allSize = 0;

	public ClearPathManager(Context context) {
		path = "data" + File.separator + Environment.getDataDirectory()
				+ File.separator + context.getPackageName() + File.separator
				+ "clearpath.db";
		file = new File(path);
		packageManager = context.getPackageManager();
	}

	/**
	 * �½��ļ�
	 * 
	 * @throws IOException
	 */
	public void createFile(InputStream inputStream) throws IOException {
		if (file.exists()) {
			return;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int length = 0;
			while ((length = inputStream.read(b)) != -1) {
				fos.write(b, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}

	/**
	 * ��ȡ����
	 */
	public ArrayList<ClearInfo> getData(Context context) {

		ArrayList<ClearInfo> infos = new ArrayList<ClearInfo>();

		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(file,
				null);
		Cursor cursor = database.query("softdetail", null, null, null, null,
				null, null);
		allSize = 0;
		while (cursor.moveToNext()) {
			// ��ȡ·��
			int filePathIndex = cursor.getColumnIndex("filepath");
			String filePath = cursor.getString(filePathIndex);
			// ��ȡ����SD����·��
			String inSDCardPath = MemoryManager.getPhoneInSDCardPath();
			File file = new File(inSDCardPath + filePath);// Ҫ��������SD����·��
			Drawable icon = null;
			long fileSize = 0;
			String fileSizeStr = "0.00B";
			if (file.exists()) {// �ж������ļ��Ƿ���ڡ�
				// ��ȡ�������,��������ж��ļ�����ʱ�Ż�ȡ
				int nameIndex = cursor.getColumnIndex("softChinesename");
				String softChinesename = cursor.getString(nameIndex);

				// ��ȡ����
				String apkName = cursor.getString(cursor
						.getColumnIndex("apkname"));
				fileSize = getAllFileSize(file);
				allSize += fileSize;
				fileSizeStr = Formatter.formatFileSize(context, fileSize);
				try {
					// ������ھͻ�ȡ��Ӧ��ͼ��
					icon = packageManager.getApplicationIcon(apkName);
				} catch (NameNotFoundException e) {
					// ��������ھ�����ΪĬ�ϵ�ͼ��
					icon = context.getResources().getDrawable(
							R.drawable.icon_bmp);
					e.printStackTrace();
				}
				ClearInfo info = new ClearInfo(softChinesename, fileSizeStr,
						icon, file, fileSize);
				infos.add(info);
			}

		}
		// allSizeStr = Formatter.formatFileSize(context, allSize);
		return infos;
	}

	public long getAllFileSize(File file) {
		fileSize = 0;
		return getFileSize(file);

	}

	long fileSize = 0;

	/**
	 * ��ȡ�ļ��д�С
	 */
	public long getFileSize(File file) {
		if (file.isFile()) {
			fileSize += file.length();
		} else {
			File[] listFiles = file.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				if (listFiles[i].isFile()) {
					fileSize += listFiles[i].length();
				} else {
					getFileSize(listFiles[i]);
				}
			}
		}
		return fileSize;
	}

}
