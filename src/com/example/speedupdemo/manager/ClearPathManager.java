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
	 * 新建文件
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
	 * 获取数据
	 */
	public ArrayList<ClearInfo> getData(Context context) {

		ArrayList<ClearInfo> infos = new ArrayList<ClearInfo>();

		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(file,
				null);
		Cursor cursor = database.query("softdetail", null, null, null, null,
				null, null);
		allSize = 0;
		while (cursor.moveToNext()) {
			// 获取路径
			int filePathIndex = cursor.getColumnIndex("filepath");
			String filePath = cursor.getString(filePathIndex);
			// 获取内置SD卡的路径
			String inSDCardPath = MemoryManager.getPhoneInSDCardPath();
			File file = new File(inSDCardPath + filePath);// 要加上内置SD卡的路径
			Drawable icon = null;
			long fileSize = 0;
			String fileSizeStr = "0.00B";
			if (file.exists()) {// 判断垃圾文件是否存在。
				// 获取软件名称,最后是在判断文件存在时才获取
				int nameIndex = cursor.getColumnIndex("softChinesename");
				String softChinesename = cursor.getString(nameIndex);

				// 获取包名
				String apkName = cursor.getString(cursor
						.getColumnIndex("apkname"));
				fileSize = getAllFileSize(file);
				allSize += fileSize;
				fileSizeStr = Formatter.formatFileSize(context, fileSize);
				try {
					// 如果存在就获取对应的图标
					icon = packageManager.getApplicationIcon(apkName);
				} catch (NameNotFoundException e) {
					// 如果不存在就设置为默认的图标
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
	 * 获取文件夹大小
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
