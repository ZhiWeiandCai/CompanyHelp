package com.xht.android.companyhelp.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.xht.android.companyhelp.App;
import com.xht.android.companyhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
	
	public void getArticleItems(final int witch, final APIListener apiListener) {
		String urlS = MakeURL(ARTICLE_URL, new LinkedHashMap<String, Object>() {
			{put("witchLeiBiew", witch);}
		});
		LogHelper.i(TAG, urlS);
		JsonObjectRequest req = new JsonObjectRequest(urlS, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {
					JSONArray jsonObject = response.optJSONArray("entity");

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
				apiListener.onError("错误");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
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

	public void getVerCodeReset (final String pNum, final APIListener apiListener) {
		String urlString = MakeURL(VERCODE_URL_RESET, new LinkedHashMap<String, Object>() {{
			put("telephone", pNum);
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

	public void postResetPassword(String pNum, String mimaString, String yanzheng, final APIListener apiListener) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("telephone", pNum);
			jsonObject.put("newPass", mimaString);
			jsonObject.put("verify", yanzheng);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, RESET_PASSWORD_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("提交订单出错");
			}
		});
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

	/**
	 * 根据业务类型与区域获取某业务界面的价格
	 * @param yuqu 区域
	 * @param apiListener 回调监听器
     */
	public void getJifGeofYeFu(int witch, final int yuqu, final APIListener apiListener) {
		String urlString = MakeURL(JiaGeofYeWu[witch], new LinkedHashMap<String, Object>() {{
			put("countyId", yuqu);
		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("获取价格出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 注册公司-提交订单
	 */
	public void postDingDanZhuCeCompany(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, SaveRegistOrderInfo_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("提交订单出错");
			}
		}) {

		};
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 记账报税-提交订单
	 */
	public void postDingDanJiZhang(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, JiZhangBS_SROrderI_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("提交订单出错");
			}
		}) {

		};
		App.getInstance().addToRequestQueue(req, TAG);

	}


	/**
	 * 检查版本更新 getCheckVersion
	 * @param apiListener 回调监听器
	 */
	public void getCheckVersion(final APIListener apiListener) {
		String urlString = MakeURL(CHECK_VERSION_URL, new LinkedHashMap<String, Object>() {{

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
				apiListener.onError("服务器繁忙，请稍后再试");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 根据用户id获取公司列表和社保价格
	 * @param uid 用户id
	 * @param apiListener 回调监听器
	 */
	public void getComListAndJiaGeofYeWu(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(SHEBAO_GET_JIAGE_URL, new LinkedHashMap<String, Object>() {{
			put("ordContactId", uid);
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
					if (jsonObject.optJSONArray("companyName") == null) {
						apiListener.onError("对不起，该业务暂时只对小后台客户开放");
						return;
					}
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
				apiListener.onError("获取价格出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 社保服务-提交订单
	 */
	public void postDingDanSheBao(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, SHEBAO_BOOKLIST_POST_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("提交订单出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}
	
	/**
	 * 根据用户id获取公司列表和变更资金费用
	 * @param uid 用户id
	 * @param apiListener 回调监听器
	 */
	public void getComListYeWu(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(ZHUCE_GET_BIAN_GENG_URL, new LinkedHashMap<String, Object>() {{
			put("ordContactId", uid);

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
					if (jsonObject.optJSONArray("companyName") == null) {
						apiListener.onError("对不起，该业务暂时只对小后台客户开放");
						return;
					}
					
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
				apiListener.onError("获取价格出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 根据用户id获取公司列表和发票价格RegiTrademask_Get_Url
	 * @param uid 用户id
	 * @param apiListener 回调监听器
	 */
	public void getComListAndJiaGeofFP(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(FAPIAO_GET_JIAGE_URL, new LinkedHashMap<String, Object>() {{
			put("ordContactId", uid);
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
				apiListener.onError("获取价格出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 根据用户id获取公司列表和发票价格-注册商标
	 * @param uid 用户id
	 * @param apiListener 回调监听器
	 */
	public void getComListAndJGOfRegiT(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(RegiTrademask_Get_Url, new LinkedHashMap<String, Object>() {{
			put("ordContactId", uid);
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
				apiListener.onError("获取价格出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}
	
	/**
	 * 根据用户id获取公司列表和注销公司费用
	 * @param uid 用户id
	 * @param apiListener 回调监听器
	 */
	public void getZhuXiaoComListYeWu(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(ZHUXIAO_GET_BIAN_GENG_URL, new LinkedHashMap<String, Object>() {{
			put("ordContactId", uid);

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
					if (jsonObject.optJSONArray("companyName") == null) {
						apiListener.onError("对不起，该业务暂时只对小后台客户开放");
						return;
					}
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
				apiListener.onError("获取价格出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}


	/**
	 * 根据用户id获取未支付的订单
	 * @param uid 用户id
	 * @param apiListener 回调监听器
	 */
	public void getNoPayYeWu(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(ZHUXIAO_GET_NO_PAY_URL, new LinkedHashMap<String, Object>() {{
			put("userid", uid);

		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {


					apiListener.onResult(response);
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
				apiListener.onError("获取订单出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 根据用户id获取支付的订单
	 * @param uid 用户id
	 * @param apiListener 回调监听器
	 */
	public void getYesPayYeWu(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(ZHUXIAO_GET_YES_PAY_URL, new LinkedHashMap<String, Object>() {{
			put("userid", uid);

		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {


					apiListener.onResult(response);
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
				apiListener.onError("获取订单出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 根据用户id获取公司列表和——变更服务费用
	 * @param uid 用户id
	 * @param apiListener 回调监听器
	 */
	public void getBianGengComListYeWu(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(FUWU_GET_BIAN_GENG_URL, new LinkedHashMap<String, Object>() {{
			put("ordContactId", uid);

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
					if (jsonObject.optJSONArray("companyName") == null) {
						apiListener.onError("对不起，该业务暂时只对小后台客户开放");
						return;
					}
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
				apiListener.onError("获取价格出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 根据获取公司类型
	 * @param uid
	 * @param apiListener 回调监听器
	 */
	public void getCompanyType( int uid, final APIListener apiListener) {
		String urlString = MakeURL(FUWU_GET_COMP_TYPE_URL, new LinkedHashMap<String, Object>() {
		});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);

				} else {
					JSONArray jsonObject = response.optJSONArray("entity");
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
				apiListener.onError("获取公司类型出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 根据获取公司职位
	 * @param uid
	 * @param apiListener 回调监听器
	 */
	public void getCompanyWork( int uid, final APIListener apiListener) {
		String urlString = MakeURL(FUWU_GET_COMP_WORK_URL, new LinkedHashMap<String, Object>() {
		});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);

				} else {
					JSONArray jsonObject = response.optJSONArray("entity");
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
				apiListener.onError("获取公司类型出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 根据用户id获取-----账户信息
	 * @param uid 用户id
	 * @param apiListener 回调监听器
	 */
	public void getZhangHuGuanLiYeWu11(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(FUWU_GET_ZHANGHU_URL, new LinkedHashMap<String, Object>() {{
			put("userid", uid);


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
				apiListener.onError("获取信息出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}


	/**
	 * 注册变更服务资金--提交订单
	 */
	public void postDingDanBianGengService(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BIANGENG_SERVICE_LIST_POST_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("提交订单出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);

	}
	
	/**
	 * 提交----修改账户信息
	 */
	public void postXiuGaiZhangHu(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, FUWU_POST_ZHANGHU_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("提交订单出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);

	}

	public void postChangMiMa(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, FUWU_POST_MiMa_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("提交订单出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);

	}

	public void  getDingDanMessage12(final String shangPin,final String dingdanHao,final String jinE,final int YeWuStyle,final APIListener apiListener) {
		String urlString = MakeURL(ZHIFUBAO_URL, new LinkedHashMap<String, Object>() {{
			put("shangPin",shangPin);
			put("dingdanHao",dingdanHao);
			put("jinE",jinE);
			put("YeWuStyle",YeWuStyle);
		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				String sign=response.optString("sign");
				LogHelper.i(TAG,"---666"+response.toString());
				apiListener.onResult(response);
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
				apiListener.onError("获取出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 完善信息--提交
	 */
	public void postCompleteMessage(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, FUWU_POST_COMPLETE_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("完善信息出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);

	}
	
	/**
	 * 报税请求是否通过-提交
	 */
	public void postYouMengMessage(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, FUWU_POST_YOUMENG_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("报税请求出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);

	}

	/**
	 * 注册变更资金--提交订单
	 */
	public void postDingDanBianGeng(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, BIANGENG_LIST_POST_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("提交订单出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);

	}

	/**
	 * 注销公司--提交订单
	 */
	public void postDingDanDelete(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, ZHUXIAO_LIST_POST_URL, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("提交订单出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);

	}

	/**
	 * 发票-业务开票-提交订单
	 */
	public void postDingDanFP2(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, FAPIAO_POST_LEIXING2, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("提交订单出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 发票-税局开票申请-提交订单
	 */
	public void postDingDanFP1(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, FAPIAO_POST_LEIXING1, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("提交订单出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 发票-发票认证-提交订单
	 */
	public void postDingDanFP3(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, FAPIAO_POST_LEIXING3, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("提交订单出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 注册商标-提交订单
	 */
	public void postDDRegiTm(int userId, JSONObject jsonObject, final APIListener apiListener) {
		JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, RegiTrademask_Post_Url, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {

					apiListener.onResult(response);
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
				apiListener.onError("提交订单出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 根据用户id获取初始数据-服务看板
	 * @param uid 用户id
	 * @param apiListener 回调监听器
	 */
	public void getInitDataSLB(final int uid, final int year, final int month,final APIListener apiListener) {
		String urlString = MakeURL(FWKB_DF_Get_Url, new LinkedHashMap<String, Object>() {{
			put("openId", uid);
			put("year", year);
			put("yue", month);
		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {
					apiListener.onResult(response);
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
				apiListener.onError("初始化数据出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 根据id,年月获取初始数据-服务看板
	 * @param cid 公司id
	 * @param apiListener 回调监听器
	 */
	public void getDataSLB(final int cid, final int year, final int month,final APIListener apiListener) {
		String urlString = MakeURL(FWKB_D_Get_Url, new LinkedHashMap<String, Object>() {{
			put("companyId", cid);
			put("year", year);
			put("yue", month);
		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {
					apiListener.onResult(response);
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
				apiListener.onError("初始化数据出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}
	
	/**
	 * 根据消息id获取具体信息
	 * @param uid 消息idid
	 * @param apiListener 回调监听器
	 */
	public void getYouMengYeWu(final int uid, final APIListener apiListener) {
		String urlString = MakeURL(FUWU_GET_YOUMENG_URL, new LinkedHashMap<String, Object>() {{
			put("taxId", uid);

		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				}
				else {
					JSONObject jsonObject = response.optJSONObject("entity");

					/*if (jsonObject.optJSONArray("companyName") == null) {
						apiListener.onError("您还没有在我们这里注册公司！");
						return;
					}*/
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
				apiListener.onError("获取价格出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 根据公司id,年月获取社保人员
	 *
	 */
	public void getSBPeople(final int cid, final int year, final int month,final APIListener apiListener) {
		String urlString = MakeURL(GET_SB_PEOPLE, new LinkedHashMap<String, Object>() {{
			put("year", year);
			put("month", month);
			put("companyId", cid);
		}});
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {
					apiListener.onResult(response);
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
				apiListener.onError("初始化数据出错");
			}
		});
		App.getInstance().addToRequestQueue(req, TAG);
	}

	/**
	 * 根据公司id,年月获取报税明细
	 *
	 */
	public void getBSMX(final int cid, final int year, final int month,final APIListener apiListener) {
		String urlString = MakeURL(GET_BS_MXXS, new LinkedHashMap<String, Object>() {{
			put("year", year);
			put("yue", month);
			put("companyId", cid);
		}});
		LogHelper.i(TAG, "报税明细=" + urlString);
		JsonObjectRequest req = new JsonObjectRequest(urlString, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogHelper.i(TAG, response.toString());
				if (isResponseError(response)) {
					String errMsg = response.optString("message");
					apiListener.onError(errMsg);
				} else {
					apiListener.onResult(response);
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
				apiListener.onError("初始化数据出错");
			}
		});
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
