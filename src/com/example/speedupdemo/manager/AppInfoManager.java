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
 * 应用信息管理
 * 
 * @author 傅炫阳
 * 
 */
public class AppInfoManager {

	AppInfo aInfo;
	// 框架层的ActivityManagers
	ActivityManager activityManager;
	PackageManager packageManager;
	Context context;
	boolean isSystem;

	public AppInfoManager(Context context) {
		this.context = context;
		// 框架层是客观存在的，不能new出来,
		// 需要用到context.getSystemService获取的是object，需要强转
		activityManager = (ActivityManager) context
				.getSystemService/* 获取系统服务 */(Context.ACTIVITY_SERVICE/* 确定服务为ActivityManager */);
		// 获取packageManager对象
		packageManager = context.getPackageManager();
	}

	/**
	 * 获取后台进程
	 * 
	 * @throws NameNotFoundException
	 */
	public Map<String, ArrayList<AppInfo>> getBackProcess() {
		// 如果是全局每次都要清空一次这个集合。
		ArrayList<AppInfo> infos = new ArrayList<AppInfo>();
		// 系统应用的集合
		ArrayList<AppInfo> systemApp = new ArrayList<AppInfo>();
		// 第三方应用的集合
		ArrayList<AppInfo> userApp = new ArrayList<AppInfo>();
		// 所以应用的集合
		Map<String, ArrayList<AppInfo>> map = new HashMap<String, ArrayList<AppInfo>>();
		// 获取进程图标和标题的步骤
		// 1.获取正在运行的运用进程
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (int i = 0; i < appProcesses.size(); i++) {
			// 2.获取运行的进程其中一条信息
			RunningAppProcessInfo info = appProcesses.get(i);
			// 3.获取该进程的名字：获取的是该进程的包名（gen文件下的包名）
			String processName = info.processName;
//			System.out.println(i + "进程名称：" + processName);
			// 4.通过包名获取应用图标
			try {
				Drawable icon = packageManager.getApplicationIcon(processName);// 如果名字写错会出现异常
				// System.out.println(i + "图标" + icon);
				// 5.获取应用的信息(参数1为包名，参数2为获取的软件的类型)
				ApplicationInfo appInfo = packageManager.getApplicationInfo(
						processName,
						// 没有第一个就要写后面2个
						PackageManager.GET_UNINSTALLED_PACKAGES/* 已经安装过可卸载的软件 */
								| PackageManager.GET_META_DATA/* 获取应用的数据 */
								| PackageManager.GET_SHARED_LIBRARY_FILES);/* 共享的文件和包 */
				// 6.通过软件的信息获取应用的标题（应用名称）。(获取的是字符序列可转成字符串)
				String label = packageManager.getApplicationLabel(appInfo)
						.toString();
				// System.out.println(i + "标题:" + label);
				// 判断是否是系统进程（只有系统的值才为1，其他都不为1）
				// 相等不一定一样，应用可能会升级，但是系统应用的最后一位肯定是1.不是系统应用为0
				// FLAG_SYSTEM=00000001，所以可以用当前应用&系统应用，不为0就是系统应用
				int x = appInfo.flags/* 当前应用的flag */
						& ApplicationInfo.FLAG_SYSTEM/* 系统应用的flag */;
				if (x != 0) {// 为系统应用
					isSystem = true;
				} else {// 第三方应用
					isSystem = false;
				}

				// 获取进程所占内存步骤
				// 1.获取进程的id
				int pid = info.pid;
				// 2.获取进程内存信息（电脑软件有多个进程，每个进程对应一个pid）
				MemoryInfo[] memoryInfos = activityManager
						.getProcessMemoryInfo(new int[] { pid });
				// 3.获取debug的MemoryInfo的对象,即内存信息的对象
				MemoryInfo memoryInfo = memoryInfos[0];// 获取对应内存信息的对象
				// 4.当前进程正在用的全部内存的大小
				int size = memoryInfo.getTotalPrivateDirty();// dirty指已经使用过的，单位是kb
				// 5.根据大小自动转换单位(调用的方法short表示没有小数)
				String sizeText = Formatter/* android的 */.formatFileSize(
						context, size * 1024/* 单位为b才可以转换 */);
				// System.out.println(i + "应用所占内存:" + sizeText);
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
	 * 杀死后台进程
	 * 
	 * @throws NameNotFoundException
	 */
	public void killAllProcess() throws NameNotFoundException {
		// 1.获取所有进程
		List<RunningAppProcessInfo> infos = activityManager
				.getRunningAppProcesses();
		for (int i = 0; i < infos.size(); i++) {
			RunningAppProcessInfo info = infos.get(i);
			// 2.获取进程的级别（importance）
			int imp = info.importance;
			// 如果小于服务级别，就是支撑程序的运行，不应该杀死。超过的才杀死
			// 系统的进程也不要杀死，判断是否是系统进程如上
			if (imp >= RunningAppProcessInfo.IMPORTANCE_SERVICE/* 服务级别 */) {
				// 判断是否是系统进程
				ApplicationInfo appInfo = packageManager.getApplicationInfo(
						info.processName,
						PackageManager.GET_UNINSTALLED_PACKAGES
								| PackageManager.GET_META_DATA
								| PackageManager.GET_SHARED_LIBRARY_FILES);
				if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
					// 3.杀死后台程序，需要提供权限
					// 在清单中的permissions中添加uses
					// permission选择android.permission.KILL_BACKGROUND_PROCESSES"
					activityManager.killBackgroundProcesses(info.processName);
				}

			}
		}
	}

	/**
	 * 获取软件
	 * 
	 * 如果为system，说明要系统的程序
	 * 如果为user，说明要用户的程序
	 * 如果string为其他,说明要全部的程序
	 */
	public ArrayList<AppInfo> getApp(String string) {
		ArrayList<AppInfo> allApp = new ArrayList<AppInfo>();
		ArrayList<AppInfo> systemApp = new ArrayList<AppInfo>();
		ArrayList<AppInfo> userApp = new ArrayList<AppInfo>();
		List<PackageInfo> packages = packageManager
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo packageInfo : packages) {
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//			//获取图标
//			int icon = applicationInfo.icon;
			//获取图片图标
			Drawable loadIcon = applicationInfo.loadIcon(packageManager);
			//获取标题
			String loadLabel = applicationInfo.loadLabel(packageManager).toString();
			//获取包名
			String packageName = packageInfo.packageName;
			//获取版本
			String versionName = packageInfo.versionName;
			aInfo = new AppInfo(loadIcon, loadLabel, packageName, versionName);
			allApp.add(aInfo);
			if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)!=0) {
				systemApp.add(aInfo);
			}else {
				userApp.add(aInfo);
			}
		}
		if (string.equals("system") ) {//要用equals!!!!!
			return systemApp;
		}else if (string.equals("user")) {
			return userApp;
		}
		return allApp;
	}
	
//	/**
//	 * 杀死选中的进程
//	 */
//	public void killSelectProcess(ArrayList<AppInfo> content) {
//		for (AppInfo appInfo : content) {
//			if (appInfo.isCheck()) {//该进程被选中
//				activityManager.killBackgroundProcesses(appInfo.getPackageName()); 
//			}
//		}
//	}
	

}
