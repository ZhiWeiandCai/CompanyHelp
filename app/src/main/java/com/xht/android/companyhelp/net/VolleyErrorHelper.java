package com.xht.android.companyhelp.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;

public class VolleyErrorHelper {
	
	/**
	 * @param error
	 * @return 
	 * 0：未知错误
	 * 1：超时
	 * 2：服务器问题
	 * 3：网络问题
	 */
	public static int getErrType(Object error) {
		int type = 0;
		if(error instanceof TimeoutError){
			System.out.println("time out");
			type = 1;
		}else if(isServerProbllem(error)){
			System.out.println("server problem");
			type = 2;
		}else if(isNetworkProblem(error)){
			System.out.println("net problem");
			type = 3;
		}
		System.out.println("net error type = " + type);
		
		return type;
	}
	
	private static boolean isNetworkProblem(Object error) {
		return (error instanceof NetworkError) || (error instanceof NoConnectionError);
	}
	
	private static boolean isServerProbllem(Object error) {
		return (error instanceof ServerError) || (error instanceof AuthFailureError);
	}

}
