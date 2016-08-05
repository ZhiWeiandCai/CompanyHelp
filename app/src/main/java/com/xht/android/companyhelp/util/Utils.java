package com.xht.android.companyhelp.util;

import android.annotation.SuppressLint;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 工具类，里面有一些工具函数
 * @author czw
 * <br>2016-04-23
 */
public class Utils {
	private static final String TAG = "Utils";
	
	/**
	 * 创建一个目录
	 * @param dirString
	 */
	public static void mkDir(String dirString) {
		File file = new File(dirString);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	/**
	 * 初始化屏幕宽高
	 */
	/*public static void initScreenWAndH(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point point = new Point();
		display.getRealSize(point);

		Constants.SCREEN_WIDTH = point.x;
		Constants.SCREEN_HEIGHT = point.y;
	}*/
	
	/**
	 * 删除文件及其子文件
	 * @param file
	 */
	public static void deleteFiles(File file) {
		File[] files = file.listFiles();
		//递归删除所有文件
		if (files != null) {
			for (File file2 : files) {
				deleteFiles(file2);
			}
		}
		file.delete();
	}
	
	public static byte[] chartobyte(char[] charArray) {
		byte[] byteArray = new byte[charArray.length * 2];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[2 * i] = (byte) (charArray[i] & 0xff00 >> 8);
			byteArray[2*i + 1] = (byte) (charArray[i] & 0xff);
		}
		return byteArray;
	}
	
	public static char[] bytetochar(byte[] byteArray) {
		char[] charArray = new char[byteArray.length / 2];
		for (int i = 0; i < charArray.length; i++) {
			charArray[i] = (char) (byteArray[i*2] & 0xff << 8 | byteArray[i*2 + 1] & 0xff);
		}
		return charArray;
	}
	
	public static int px2dip(Context context, int px) {
		final float density = context.getResources().getDisplayMetrics().density;
		return (int) (px / density + 0.5f);
	}
	
	public static int dip2px(Context context, int dip) {
		final float density = context.getResources().getDisplayMetrics().density;
		return (int) (dip * density + 0.5f);
	}
	
	@SuppressLint("NewApi")
	public static boolean saveJSONObjectToPath(boolean bEnCode, String path,
			JSONObject json) {
		try (FileOutputStream os = new FileOutputStream(path))
		{			
			byte[] source = null;
			source = json.toString().getBytes("utf-8");
			if (source != null) {
				os.write(source, 0, source.length);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return true;
	}	
	
	@SuppressLint("NewApi")
	public static JSONObject getJSONObjectFromPath(boolean bEnCode, String path) {
		String rawJson = null;
		byte[] buffer = new byte[1024];
		int length = 0;
		JSONObject jsonObject = null;
		StringBuilder stringBuilder = new StringBuilder();		
		try (FileInputStream is = new FileInputStream(path))
		{
			while ((length = is.read(buffer)) != -1) {
				stringBuilder.append(new String(buffer));
			}
			rawJson = stringBuilder.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			jsonObject = new JSONObject(rawJson);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return jsonObject;		
	}

	public static byte[] httpGet(String url) {
		try {
			URL urlT = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlT.openConnection();
			connection.connect();
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				LogHelper.e(TAG, "httpGet fail, status code = " + connection.getResponseCode());
				return null;
			}
			InputStream is = connection.getInputStream();
			int count = 0;
			while (count == 0) {
				count = is.available();
			}
			byte[] bA = new byte[count];
			is.read(bA);
			return bA;
		} catch (MalformedURLException e) {
			LogHelper.e(TAG, "httpGet exception, e = " + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			LogHelper.e(TAG, "httpGet exception, e = " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
