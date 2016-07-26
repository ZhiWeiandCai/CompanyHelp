package com.xht.android.companyhelp.util;

import android.util.Log;

/**
 * 打印日志的帮助类
 * @author czw
 * <br>2016-04-20
 */
public class LogHelper {
	
	private static final String TAG = LogHelper.class.getSimpleName();
	
	public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARNING = 3;
    public static final int LEVEL_ERROR = 4;    

    private static int sLevel = LEVEL_VERBOSE;
    
    public static final int v(String classTag, String msg) {    	
		return v(classTag, "", getLogInfo(msg));    	
    }
    
    public static final int v(String classTag, String privateTag, String msg) {
    	if (sLevel > LEVEL_VERBOSE) {
			return sLevel;
		}
    	return Log.v(String.format("%s %s %s", TAG, classTag, privateTag), msg);
    }
    
    public static final int d(String classTag, String msg) {
    	return d(classTag, "", getLogInfo(msg));
    }
    
    public static final int d(String classTag, String privateTag, String msg) {
    	if (sLevel > LEVEL_DEBUG) {
			return sLevel;
		}
    	return Log.d(String.format("%s %s %s", TAG, classTag, privateTag), msg);
    }
    
    public static final int i(String classTag, String msg) {
    	return i(classTag, "", getLogInfo(msg));
    }
    
    public static final int i(String classTag, String privateTag, String msg) {
    	if (sLevel > LEVEL_INFO) {
			return sLevel;
		}
    	return Log.i(String.format("%s %s %s", TAG, classTag, privateTag), msg);
    }
    
    public static final int w(String classTag, String msg) {
    	return w(classTag, "", getLogInfo(msg));
    }
    
    public static final int w(String classTag, String privateTag, String msg) {
    	if (sLevel > LEVEL_WARNING) {
			return sLevel;
		}
    	return Log.w(String.format("%s %s %s", TAG, classTag, privateTag), msg);
    }
    
    public static final int e(String classTag, String msg) {
    	return e(classTag, "", getLogInfo(msg));
    }
    
    public static final int e(String classTag, String privateTag, String msg) {
    	if (sLevel > LEVEL_ERROR) {
			return sLevel;
		}
    	return Log.e(String.format("%s %s %s", TAG, classTag, privateTag), msg);
    }

	private static String getLogInfo(String msg) {	
		StackTraceElement stackTraceElement = new Exception().getStackTrace()[2];
		return String.format("%s, File:%s, Function:%s, Line:%d, ThreadId:%d", msg, stackTraceElement.getFileName(),
				stackTraceElement.getMethodName(), stackTraceElement.getLineNumber(), Thread.currentThread().getId());
	}

}
