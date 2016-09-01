package com.example.speedupdemo.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.speedupdemo.info.AppInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;
import android.text.format.Formatter;

/**
 * Ӧ����Ϣ����
 * 
 * @author ������
 * 
 */
public class AppInfoManager {

	AppInfo aInfo;
	// ��ܲ��ActivityManagers
	ActivityManager activityManager;
	PackageManager packageManager;
	Context context;
	boolean isSystem;

	public AppInfoManager(Context context) {
		this.context = context;
		// ��ܲ��ǿ͹۴��ڵģ�����new����,
		// ��Ҫ�õ�context.getSystemService��ȡ����object����Ҫǿת
		activityManager = (ActivityManager) context
				.getSystemService/* ��ȡϵͳ���� */(Context.ACTIVITY_SERVICE/* ȷ������ΪActivityManager */);
		// ��ȡpackageManager����
		packageManager = context.getPackageManager();
	}

	/**
	 * ��ȡ��̨����
	 * 
	 * @throws NameNotFoundException
	 */
	public Map<String, ArrayList<AppInfo>> getBackProcess() {
		// �����ȫ��ÿ�ζ�Ҫ���һ��������ϡ�
		ArrayList<AppInfo> infos = new ArrayList<AppInfo>();
		// ϵͳӦ�õļ���
		ArrayList<AppInfo> systemApp = new ArrayList<AppInfo>();
		// ������Ӧ�õļ���
		ArrayList<AppInfo> userApp = new ArrayList<AppInfo>();
		// ����Ӧ�õļ���
		Map<String, ArrayList<AppInfo>> map = new HashMap<String, ArrayList<AppInfo>>();
		// ��ȡ����ͼ��ͱ���Ĳ���
		// 1.��ȡ�������е����ý���
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (int i = 0; i < appProcesses.size(); i++) {
			// 2.��ȡ���еĽ�������һ����Ϣ
			RunningAppProcessInfo info = appProcesses.get(i);
			// 3.��ȡ�ý��̵����֣���ȡ���Ǹý��̵İ�����gen�ļ��µİ�����
			String processName = info.processName;
//			System.out.println(i + "�������ƣ�" + processName);
			// 4.ͨ��������ȡӦ��ͼ��
			try {
				Drawable icon = packageManager.getApplicationIcon(processName);// �������д�������쳣
				// System.out.println(i + "ͼ��" + icon);
				// 5.��ȡӦ�õ���Ϣ(����1Ϊ����������2Ϊ��ȡ�����������)
				ApplicationInfo appInfo = packageManager.getApplicationInfo(
						processName,
						// û�е�һ����Ҫд����2��
						PackageManager.GET_UNINSTALLED_PACKAGES/* �Ѿ���װ����ж�ص���� */
								| PackageManager.GET_META_DATA/* ��ȡӦ�õ����� */
								| PackageManager.GET_SHARED_LIBRARY_FILES);/* ������ļ��Ͱ� */
				// 6.ͨ���������Ϣ��ȡӦ�õı��⣨Ӧ�����ƣ���(��ȡ�����ַ����п�ת���ַ���)
				String label = packageManager.getApplicationLabel(appInfo)
						.toString();
				// System.out.println(i + "����:" + label);
				// �ж��Ƿ���ϵͳ���̣�ֻ��ϵͳ��ֵ��Ϊ1����������Ϊ1��
				// ��Ȳ�һ��һ����Ӧ�ÿ��ܻ�����������ϵͳӦ�õ����һλ�϶���1.����ϵͳӦ��Ϊ0
				// FLAG_SYSTEM=00000001�����Կ����õ�ǰӦ��&ϵͳӦ�ã���Ϊ0����ϵͳӦ��
				int x = appInfo.flags/* ��ǰӦ�õ�flag */
						& ApplicationInfo.FLAG_SYSTEM/* ϵͳӦ�õ�flag */;
				if (x != 0) {// ΪϵͳӦ��
					isSystem = true;
				} else {// ������Ӧ��
					isSystem = false;
				}

				// ��ȡ������ռ�ڴ沽��
				// 1.��ȡ���̵�id
				int pid = info.pid;
				// 2.��ȡ�����ڴ���Ϣ����������ж�����̣�ÿ�����̶�Ӧһ��pid��
				MemoryInfo[] memoryInfos = activityManager
						.getProcessMemoryInfo(new int[] { pid });
				// 3.��ȡdebug��MemoryInfo�Ķ���,���ڴ���Ϣ�Ķ���
				MemoryInfo memoryInfo = memoryInfos[0];// ��ȡ��Ӧ�ڴ���Ϣ�Ķ���
				// 4.��ǰ���������õ�ȫ���ڴ�Ĵ�С
				int size = memoryInfo.getTotalPrivateDirty();// dirtyָ�Ѿ�ʹ�ù��ģ���λ��kb
				// 5.���ݴ�С�Զ�ת����λ(���õķ���short��ʾû��С��)
				String sizeText = Formatter/* android�� */.formatFileSize(
						context, size * 1024/* ��λΪb�ſ���ת�� */);
				// System.out.println(i + "Ӧ����ռ�ڴ�:" + sizeText);
				aInfo = new AppInfo(icon, label, sizeText, isSystem,
						processName);
				infos.add(aInfo);
				if (isSystem) {
					systemApp.add(aInfo);
				} else {
					userApp.add(aInfo);
				}
			} catch (Exception e) {
			}
		}
		map.put("system", systemApp);
		map.put("user", userApp);
		return map;
	}

	/**
	 * ɱ����̨����
	 * 
	 * @throws NameNotFoundException
	 */
	public void killAllProcess() throws NameNotFoundException {
		// 1.��ȡ���н���
		List<RunningAppProcessInfo> infos = activityManager
				.getRunningAppProcesses();
		for (int i = 0; i < infos.size(); i++) {
			RunningAppProcessInfo info = infos.get(i);
			// 2.��ȡ���̵ļ���importance��
			int imp = info.importance;
			// ���С�ڷ��񼶱𣬾���֧�ų�������У���Ӧ��ɱ���������Ĳ�ɱ��
			// ϵͳ�Ľ���Ҳ��Ҫɱ�����ж��Ƿ���ϵͳ��������
			if (imp >= RunningAppProcessInfo.IMPORTANCE_SERVICE/* ���񼶱� */) {
				// �ж��Ƿ���ϵͳ����
				ApplicationInfo appInfo = packageManager.getApplicationInfo(
						info.processName,
						PackageManager.GET_UNINSTALLED_PACKAGES
								| PackageManager.GET_META_DATA
								| PackageManager.GET_SHARED_LIBRARY_FILES);
				if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
					// 3.ɱ����̨������Ҫ�ṩȨ��
					// ���嵥�е�permissions�����uses
					// permissionѡ��android.permission.KILL_BACKGROUND_PROCESSES"
					activityManager.killBackgroundProcesses(info.processName);
				}

			}
		}
	}

	/**
	 * ��ȡ���
	 * 
	 * ���Ϊsystem��˵��Ҫϵͳ�ĳ���
	 * ���Ϊuser��˵��Ҫ�û��ĳ���
	 * ���stringΪ����,˵��Ҫȫ���ĳ���
	 */
	public ArrayList<AppInfo> getApp(String string) {
		ArrayList<AppInfo> allApp = new ArrayList<AppInfo>();
		ArrayList<AppInfo> systemApp = new ArrayList<AppInfo>();
		ArrayList<AppInfo> userApp = new ArrayList<AppInfo>();
		List<PackageInfo> packages = packageManager
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo packageInfo : packages) {
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//			//��ȡͼ��
//			int icon = applicationInfo.icon;
			//��ȡͼƬͼ��
			Drawable loadIcon = applicationInfo.loadIcon(packageManager);
			//��ȡ����
			String loadLabel = applicationInfo.loadLabel(packageManager).toString();
			//��ȡ����
			String packageName = packageInfo.packageName;
			//��ȡ�汾
			String versionName = packageInfo.versionName;
			aInfo = new AppInfo(loadIcon, loadLabel, packageName, versionName);
			allApp.add(aInfo);
			if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)!=0) {
				systemApp.add(aInfo);
			}else {
				userApp.add(aInfo);
			}
		}
		if (string.equals("system") ) {//Ҫ��equals!!!!!
			return systemApp;
		}else if (string.equals("user")) {
			return userApp;
		}
		return allApp;
	}
	
//	/**
//	 * ɱ��ѡ�еĽ���
//	 */
//	public void killSelectProcess(ArrayList<AppInfo> content) {
//		for (AppInfo appInfo : content) {
//			if (appInfo.isCheck()) {//�ý��̱�ѡ��
//				activityManager.killBackgroundProcesses(appInfo.getPackageName()); 
//			}
//		}
//	}
	

}
