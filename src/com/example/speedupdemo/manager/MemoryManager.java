package com.example.speedupdemo.manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

/**
 * 内存管理
 * 
 * @author 傅炫阳
 * 
 */
public class MemoryManager {

	/**
	 * 获取运行内存
	 * 
	 * isTotal-->为true则获取全部运行内存，否则获取未用的运行内存
	 * 
	 * @throws IOException
	 */
	@SuppressLint("NewApi")
	public static long getMemorySize(boolean isTotal, Context context) {
		// 获取activityManager对象
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// adctvity下的MemoryInfo为整个运行内存的运行，debug为每个进程的内存信息
		MemoryInfo memoryInfo = new MemoryInfo();
		// 把当前手机的内存信息写入到对象memoryInfo中
		activityManager.getMemoryInfo(memoryInfo);
		if (!isTotal) {// 未用的运行内存
			return memoryInfo.availMem;// 空闲的，即未用的内存
		} else {// 全部的运行内存
				// 最低版本必须到16以上，不然会报错。可以修改版本或者添加SuppressLint("NewApi")
			if (Build.VERSION.SDK_INT >= 16/* 当前手机的版本的int值 */) {
				return memoryInfo.totalMem;
			} else {// 安卓版本小于16时读取总运行内存
					// 通过字符流读取手机/proc/meminfo的文件

				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader("/proc/meminfo");
					br = new BufferedReader(fr);
					String line = br.readLine();
					System.out.println("内存信息数据:" + line);// line为内存信息数据，中间用空格隔开
					// 空格隔开的第二个内容就是内存大小
					String[] strs = line.split("\\s+");// 空白符\s,根据空格拆
					return Long.parseLong(strs[1]);
				} catch (IOException e) {
					return 0;
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * 获取内置SD卡路径，若SD卡未安装，则返回null
	 * 
	 * @return
	 */
	public static String getPhoneInSDCardPath() {
		// Environment.getDownloadCacheDirectory()：缓冲文件所占内存
		// Environment.getRootDirectory():系统文件所占内存
		// Environment.getDataDirectory():三部分总和
		// 获取内置SD卡的状态
		String state = Environment.getExternalStorageState();
		// 已安装的状态
		String stateMounted = Environment.MEDIA_MOUNTED;
		if (state.equals(stateMounted)) {// 字符串的比较用equals
			// getExternalStorageDirectory获取文件夹，并获取该文件夹的绝对路径
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			return path;
		} else {// 若SD卡未安装，则返回null
			return null;
		}
	}

	/**
	 * 获取外置SD卡 6.0之后不用这个方法
	 * 
	 * @return
	 */
	public static String getPhoneOutSDCardPath() {
		// 获取运行环境数据
		Map<String, String> map = System.getenv();
		String key = "SECONDARY_STORAGE";// 二级存储设备
		// 当没有外置SD卡时，环境数据中没有此键值对（即没有二级存储设备）
		if (map.containsKey(key)) {// 判断是否包含此键
			String str = map.get(key);
			// 有2串内容--> ....():path,没有安装外置SD卡也可能会有字符串
			// 第一个冒号后面肯定为路径，路径中肯定没有冒号，所以拆分出的第二个字符串为路径
			String[] path = str.split(":");
			if (path == null || path.length == 0/**/) {// 没有外置SD卡
				return null;
			} else {
				return path[1];// 路径
			}
		}
		return null;
	}

	/**
	 * 根据路径获取存储卡大小,单位为b
	 * 
	 * @param isOut
	 *            -->为ture获取外置SD卡大小，为false获取内置SD卡的大小
	 * @param isFree
	 *            -->若为true则获取空余格子数，否则获取全部格子数
	 */
	@SuppressLint("NewApi")
	public static long getPhoneSDCardSize(boolean isOut, boolean isFree) {
		String path = null;
		if (isOut) {
			path = getPhoneOutSDCardPath();
		} else {
			path = getPhoneInSDCardPath();
		}
		// SD卡存储是将内存等比例分割为各个小格子
		long blockSize = 0;// 格子大小
		long blockCount = 0;// 格子个数
		if (path != null) {
			// File file = new File(path);
			// 将路径所对应的文件夹划分格子
			StatFs sFs = new StatFs(path);
			// 获取每个格子内存大小，已用内存大小乘以格子大小，就是内存的大小
			blockSize = sFs.getBlockSizeLong();
			//三元表达式
			blockCount = isFree ? sFs.getAvailableBlocksLong()/* 空闲的格子数 */: sFs
					.getBlockCountLong()/* 全部格子数 */;
		}
		long size = blockSize * blockCount;//若path为空，则返回0
		return size;
	}

}
