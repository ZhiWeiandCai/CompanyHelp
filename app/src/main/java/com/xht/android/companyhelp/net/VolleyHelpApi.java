package com.xht.android.companyhelp.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.StringRequest;
import com.xht.android.companyhelp.App;
import com.xht.android.companyhelp.model.Article;
import com.xht.android.companyhelp.util.LogHelper;

public class VolleyHelpApi extends BaseApi{
	private static final String TAG = "VolleyHelpApi";
	
	private static VolleyHelpApi sVolleyHelpApi;
	
	public static synchronized VolleyHelpApi getInstance() {
		if (sVolleyHelpApi == null) {
			sVolleyHelpApi = new VolleyHelpApi();
		}
		return sVolleyHelpApi;
	}
	
	private VolleyHelpApi() {}
	
	interface ICallBackListener {
		void onResponse(String string);
		void onError(int errCode, String errMessage);
	}

	private boolean isResponseError(JSONObject jb){
		String errorCode = jb.optString("code","0");

		if(errorCode.equals("1")){
			return false;
		}		
		return true;
	}
	
	public void getArticleItems(String url, ArrayList<Article> articles) {
		JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray response) {
				// TODO Auto-generated method stub
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
				case 1:
					LogHelper.i(TAG, "超时");
					break;
				case 2:
					LogHelper.i(TAG, "服务器问题");
					break;
				case 3:
					LogHelper.i(TAG, "网络问题");
					break;
				default:
					LogHelper.i(TAG, "未知错误");
				}
			}
		});
	}

	@SuppressWarnings("serial")
	public void getVerCode(final String pNum, final APIListener apiListener) {
		String urlString = MakeURL(VERCODE_URL, new LinkedHashMap<String, Object>() {{
			put("pNumber", pNum);
		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {
					JSONObject jsonObject = response.optJSONObject("entity");
					apiListener.onResult(jsonObject);
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
					case 1:
						LogHelper.i(TAG, "超时");
						break;
					case 2:
						LogHelper.i(TAG, "服务器问题");
						break;
					case 3:
						LogHelper.i(TAG, "网络问题");
						break;
					default:
						LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("错误，获取验证码");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);

	}
	
	public void postZhuCe(String pNum, String mimaString, String yanzheng, final APIListener apiListener) {
		final HashMap<String, String> mParams=new HashMap<String, String>();
				
		mParams.put("pNumber", pNum);
		mParams.put("password", mimaString);
		mParams.put("verify", yanzheng);
		
		StringRequest req = new StringRequest(Request.Method.POST, REGIST_URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				LogHelper.i(TAG, response);
				JSONObject jObject;
				try {
					jObject = new JSONObject(response);
					if (isResponseError(jObject)) {
						String errMsg = jObject.optString("message");
						apiListener.onError(errMsg);
					} else {
						JSONObject jsonObject = jObject.optJSONObject("entity");
						apiListener.onResult(jsonObject);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
				case 1:
					LogHelper.i(TAG, "超时");
					break;
				case 2:
					LogHelper.i(TAG, "服务器问题");
					break;
				case 3:
					LogHelper.i(TAG, "网络问题");
					break;
				default:
					LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("错误，postZhuCe");
			}
		}) {
			@Override 
			protected Map<String, String> getParams () throws AuthFailureError{	
				
				return mParams;
			}
		};
		App.getInstance().addToRequestQueue(req, TAG);
	}
	
	public void postLogin(String pNum, String mimaString, final APIListener apiListener) {
		final HashMap<String, String> mParams=new HashMap<String, String>();
		
		mParams.put("pNumber", pNum);
		mParams.put("password", mimaString);
		StringRequest req = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				LogHelper.i(TAG, response);
				JSONObject jObject;
				try {
					jObject = new JSONObject(response);
					if (isResponseError(jObject)) {
						String errMsg = jObject.optString("message");
						apiListener.onError(errMsg);
					} else {
						JSONObject jsonObject = jObject.optJSONObject("entity");
						apiListener.onResult(jsonObject);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				int type = VolleyErrorHelper.getErrType(error);
				switch (type) {
				case 1:
					LogHelper.i(TAG, "超时");
					break;
				case 2:
					LogHelper.i(TAG, "服务器问题");
					break;
				case 3:
					LogHelper.i(TAG, "网络问题");
					break;
				default:
					LogHelper.i(TAG, "未知错误");
				}
				apiListener.onError("错误，postLogin");
			}
		}) {
			@Override 
			protected Map<String, String> getParams () throws AuthFailureError{				    
				return mParams;
			}
		};
		App.getInstance().addToRequestQueue(req, TAG);
	}
	
	public static  String MakeURL(String p_url, LinkedHashMap<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if(url.indexOf("?")<0)
			url.append('?');

		for(String name : params.keySet()){
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
		}
		String temStr = url.toString().replace("?&", "?");
		
		return temStr;
	}

}
