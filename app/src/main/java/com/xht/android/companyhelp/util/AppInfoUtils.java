package com.xht.android.companyhelp.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 获取版本号
 */
public class AppInfoUtils {

	/*
	 * 	��获取版本名
	 */
	public static String getAppInfoName(Context context){
		  
		try {
				PackageManager pm=context.getPackageManager();
				PackageInfo packageinfo= pm.getPackageInfo(context.getPackageName(), 0);
				String versionname=packageinfo.versionName;
				return versionname;
				
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		return "";
		
	}
	
	
	/*
	 * ��获取版本号
	 */
	public static int getAppInfoNumber(Context context){
		  
		try {
	        	//��ȡpackageManagerʵ��
				PackageManager pm=context.getPackageManager();
				//��ȡ�汾��Ϣ
				PackageInfo packageinfo= pm.getPackageInfo(context.getPackageName(), 0);
				int versionnumber=packageinfo.versionCode;
				return versionnumber;
				
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		return 0;
		
	}
}
