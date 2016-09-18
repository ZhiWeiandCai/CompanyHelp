package com.xht.android.companyhelp.model;

import android.os.Environment;

public class Constants {
	
	public static final String APP_ID = "wx9d3b007949c52dd8";
	/*
	 * 外部存储器的根目录
	 */
	public static final String ROOT_PATH_EXT = Environment.getExternalStorageDirectory() + "/CompanyHelp/";
	/**
	 * 屏幕分辨率（可用作填充内容的区域）
	 */
	public static int DESIRED_WIDTH;
	/**
	 * 屏幕分辨率（可用作填充内容的区域）
	 */
	public static int DESIRED_HEIGHT;
	/**
	 * 设备density
	 */
	public static float DENSITY;
	/**
	 * 屏幕分辨率
	 */
	public static int SCREEN_WIDTH;
	/**
	 * 屏幕分辨率
	 */
	public static int SCREEN_HEIGHT;
	/**
	 * 状态栏高度
	 */
	public static int STATUSBAR_HEIGHT;
	/**
	 * actionBar高度
	 */
	public static int ACTIONBAR_HEIGHT;
	
	static {
		
	}

}
