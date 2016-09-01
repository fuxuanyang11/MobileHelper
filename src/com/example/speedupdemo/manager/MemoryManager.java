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
 * �ڴ����
 * 
 * @author ������
 * 
 */
public class MemoryManager {

	/**
	 * ��ȡ�����ڴ�
	 * 
	 * isTotal-->Ϊtrue���ȡȫ�������ڴ棬�����ȡδ�õ������ڴ�
	 * 
	 * @throws IOException
	 */
	@SuppressLint("NewApi")
	public static long getMemorySize(boolean isTotal, Context context) {
		// ��ȡactivityManager����
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// adctvity�µ�MemoryInfoΪ���������ڴ�����У�debugΪÿ�����̵��ڴ���Ϣ
		MemoryInfo memoryInfo = new MemoryInfo();
		// �ѵ�ǰ�ֻ����ڴ���Ϣд�뵽����memoryInfo��
		activityManager.getMemoryInfo(memoryInfo);
		if (!isTotal) {// δ�õ������ڴ�
			return memoryInfo.availMem;// ���еģ���δ�õ��ڴ�
		} else {// ȫ���������ڴ�
				// ��Ͱ汾���뵽16���ϣ���Ȼ�ᱨ�������޸İ汾�������SuppressLint("NewApi")
			if (Build.VERSION.SDK_INT >= 16/* ��ǰ�ֻ��İ汾��intֵ */) {
				return memoryInfo.totalMem;
			} else {// ��׿�汾С��16ʱ��ȡ�������ڴ�
					// ͨ���ַ�����ȡ�ֻ�/proc/meminfo���ļ�

				FileReader fr = null;
				BufferedReader br = null;
				try {
					fr = new FileReader("/proc/meminfo");
					br = new BufferedReader(fr);
					String line = br.readLine();
					System.out.println("�ڴ���Ϣ����:" + line);// lineΪ�ڴ���Ϣ���ݣ��м��ÿո����
					// �ո�����ĵڶ������ݾ����ڴ��С
					String[] strs = line.split("\\s+");// �հ׷�\s,���ݿո��
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
	 * ��ȡ����SD��·������SD��δ��װ���򷵻�null
	 * 
	 * @return
	 */
	public static String getPhoneInSDCardPath() {
		// Environment.getDownloadCacheDirectory()�������ļ���ռ�ڴ�
		// Environment.getRootDirectory():ϵͳ�ļ���ռ�ڴ�
		// Environment.getDataDirectory():�������ܺ�
		// ��ȡ����SD����״̬
		String state = Environment.getExternalStorageState();
		// �Ѱ�װ��״̬
		String stateMounted = Environment.MEDIA_MOUNTED;
		if (state.equals(stateMounted)) {// �ַ����ıȽ���equals
			// getExternalStorageDirectory��ȡ�ļ��У�����ȡ���ļ��еľ���·��
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			return path;
		} else {// ��SD��δ��װ���򷵻�null
			return null;
		}
	}

	/**
	 * ��ȡ����SD�� 6.0֮�����������
	 * 
	 * @return
	 */
	public static String getPhoneOutSDCardPath() {
		// ��ȡ���л�������
		Map<String, String> map = System.getenv();
		String key = "SECONDARY_STORAGE";// �����洢�豸
		// ��û������SD��ʱ������������û�д˼�ֵ�ԣ���û�ж����洢�豸��
		if (map.containsKey(key)) {// �ж��Ƿ�����˼�
			String str = map.get(key);
			// ��2������--> ....():path,û�а�װ����SD��Ҳ���ܻ����ַ���
			// ��һ��ð�ź���϶�Ϊ·����·���п϶�û��ð�ţ����Բ�ֳ��ĵڶ����ַ���Ϊ·��
			String[] path = str.split(":");
			if (path == null || path.length == 0/**/) {// û������SD��
				return null;
			} else {
				return path[1];// ·��
			}
		}
		return null;
	}

	/**
	 * ����·����ȡ�洢����С,��λΪb
	 * 
	 * @param isOut
	 *            -->Ϊture��ȡ����SD����С��Ϊfalse��ȡ����SD���Ĵ�С
	 * @param isFree
	 *            -->��Ϊtrue���ȡ����������������ȡȫ��������
	 */
	@SuppressLint("NewApi")
	public static long getPhoneSDCardSize(boolean isOut, boolean isFree) {
		String path = null;
		if (isOut) {
			path = getPhoneOutSDCardPath();
		} else {
			path = getPhoneInSDCardPath();
		}
		// SD���洢�ǽ��ڴ�ȱ����ָ�Ϊ����С����
		long blockSize = 0;// ���Ӵ�С
		long blockCount = 0;// ���Ӹ���
		if (path != null) {
			// File file = new File(path);
			// ��·������Ӧ���ļ��л��ָ���
			StatFs sFs = new StatFs(path);
			// ��ȡÿ�������ڴ��С�������ڴ��С���Ը��Ӵ�С�������ڴ�Ĵ�С
			blockSize = sFs.getBlockSizeLong();
			//��Ԫ���ʽ
			blockCount = isFree ? sFs.getAvailableBlocksLong()/* ���еĸ����� */: sFs
					.getBlockCountLong()/* ȫ�������� */;
		}
		long size = blockSize * blockCount;//��pathΪ�գ��򷵻�0
		return size;
	}

}
