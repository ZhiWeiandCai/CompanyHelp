package com.xht.android.companyhelp;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.entity.UMessage;
import com.umeng.message.tag.TagManager;
import com.xht.android.companyhelp.ceche.LruCacheManager;
import com.xht.android.companyhelp.model.Constants;
import com.xht.android.companyhelp.model.MessageDetail;
import com.xht.android.companyhelp.util.LogHelper;
import com.xht.android.companyhelp.util.Utils;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class App extends Application {
	
	private static final String TAG = App.class.getSimpleName();
	private static App sAppInstance;
	
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private LruCacheManager mLruCacheManager;
	public static ArrayList<MessageDetail> messageList;
	public static PushAgent mPushAgent;


	public static App getInstance() {
		return sAppInstance;
	}

	public static ArrayList<MessageDetail> getMessageList() {
		return messageList;
	}

	public static PushAgent getmPushAgent() {
		return mPushAgent;
	}
	public static  ArrayList<Map<String,String>> getArrayList() {
		return arrayList;
	}

	private static ArrayList<Map<String,String>>arrayList;

	@Override
	public void onCreate() {
		super.onCreate();
		sAppInstance = this;
		mLruCacheManager = LruCacheManager.getInstance(getApplicationContext());
		init();
		messageList = new ArrayList<MessageDetail>();

/**
 *注册公司 1；
 记账报税 2；
 社保服务 3；
 发票服务 4；
 注册商标 5；
 雇主保险 6；
 变更服务 7；
 注册资金 8；
 注销服务 9；
 劳务派遣 10
 */


		//友盟推送初始化
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		mPushAgent.getInstance(this).onAppStart();
		//mPushAgent.setDebugMode(false);

		//处理通知和消息
		UmengMessageHandler handlerMess=new UmengMessageHandler(){

			//dealWithNotificationMessage()方法负责处理通知消息
			@Override
			public void dealWithNotificationMessage(Context context, UMessage uMessage) {
				super.dealWithNotificationMessage(context, uMessage);
				Toast.makeText(context, uMessage.title, Toast.LENGTH_LONG).show();
				LogHelper.i(TAG,"----------"+uMessage.title);
				String title=uMessage.title;
				String content=uMessage.text;
				String url=uMessage.url;
				//String url="http://shehui.firefox.163.com/16/0908/06/CXFNU21MPBLIDY3A.html";
				//String iconurl="http://a1.peoplecdn.cn/60e66fb39bcff8e28c831eed027b4844.jpg@1l";
				String iconurl="";
				final MessageDetail itemMess = new MessageDetail();
				//把通知的数据添加到消息界面中的消息中心
				itemMess.setmTime(Utils.getTimeUtils(System.currentTimeMillis()));
				itemMess.setmTitle(title);
				itemMess.setmContent(content);
				//itemMess.setmUrl(url);

				LogHelper.i(TAG,"----tttttttttttttt------");
				LogHelper.i(TAG,"---"+title+"--"+content+"--"+url);
				//messageList.add(itemMess);
				LogHelper.i(TAG,"??????????-------------------------??????");

				ImageRequest request = new ImageRequest(iconurl, new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap arg0) {
						itemMess.setmBitmap(arg0);
					}
				}, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
						itemMess.setmBitmap(bitmap);


					}
				});
				addToRequestQueue(request, TAG);

				//额外信息extras
				if (uMessage.extra.entrySet()!=null) {
					for (Map.Entry<String, String> entry : uMessage.extra.entrySet()) {
						String key = entry.getKey();
						String value = entry.getValue();

						if (key.equals("url")){//健值----打开指定url
							itemMess.setmUrl(value);
							LogHelper.i(TAG, "1111111111111111111111111");
						}
						if (key.equals("iconurl")){//健值----打开指定图片url
							itemMess.setmUrlIcom(value);
							LogHelper.i(TAG, "1111111111111111111111111");
						}
						if (key.equals("uid")){//健值----获取传过来的推送id
							itemMess.setmMessUid(value);
							LogHelper.i(TAG, "1-------------------------`11---");
						}
						LogHelper.i(TAG, "--111---" + key.toString() + "---11--" + value.toString());
					}
					
				}
				LogHelper.i(TAG, "---------------------------");
				messageList.add(itemMess);
			}
			//dealWithCustomMessage()方法负责处理自定义消息，需由用户处理。 若开发者需要处理自定义消息
			@Override
			public void dealWithCustomMessage(final Context context, final UMessage msg) {
				new Handler(getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// 对自定义消息的处理方式，点击或者忽略

						LogHelper.i(TAG,"-----------"+msg.custom.toString());

						boolean isClickOrDismissed = true;
						if(isClickOrDismissed) {
							//自定义消息的点击统计
							UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
						} else {
							//自定义消息的忽略统计
							UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
						}
						Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
					}
				});
			}

		};
		mPushAgent.setMessageHandler(handlerMess);

		//自定义通知的打开行为
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				Toast.makeText(context, msg.title, Toast.LENGTH_LONG).show();
				String content=msg.getRaw().toString();

				String title=msg.title;
				String text=msg.text;
				String url=msg.url;

				LogHelper.i(TAG,"--33---"+title+"-"+text+"=77===");
				LogHelper.i(TAG,"-----"+content+"------=");
				//额外信息extras
				for (Map.Entry<String, String> entry : msg.extra.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					LogHelper.i(TAG, "-----" + key.toString() + "-----" + value.toString());
				}
			}
		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
	}

	private void init() {
		Utils.mkDir(Constants.ROOT_PATH_EXT);
		//获取屏幕分辨率（可用作填充内容的区域）		
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		Constants.DENSITY = displayMetrics.density;
		Constants.DESIRED_WIDTH = displayMetrics.widthPixels;
		Constants.DESIRED_HEIGHT = displayMetrics.heightPixels;
		TypedValue tv = new TypedValue();
		if (this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv,
				true)) {
			Constants.ACTIONBAR_HEIGHT = TypedValue.complexToDimensionPixelSize(
					tv.data, getResources().getDisplayMetrics());
		}		
		Constants.STATUSBAR_HEIGHT = getStatusBarHeight();
		LogHelper.d(TAG, "width:" + Constants.DESIRED_WIDTH + ",height:" + Constants.DESIRED_HEIGHT
				+ ",actionBarHeight:" + Constants.ACTIONBAR_HEIGHT +
				",statusBarheight:" + Constants.STATUSBAR_HEIGHT
				+ ",density:" + Constants.DENSITY +
				",navigationBarHeight:" + getNavigationBarHeight());
		
	}
	
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return mRequestQueue;
	}
	
	public void addToRequestQueue(Request<?> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}
	
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
	
	public ImageLoader getImageLoader() {
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(getRequestQueue(), mLruCacheManager.getLruImageCache());
		}
		return mImageLoader;
	}

	public LruCacheManager getLruCacheManager() {
		return mLruCacheManager;
	}
	
	private int getStatusBarHeight(){
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			int sbar = getResources().getDimensionPixelSize(x);
			return sbar;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	private int getNavigationBarHeight() {
		Resources resources = getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height",
				"dimen", "android");
		int height = resources.getDimensionPixelSize(resourceId);
		return height;
	}
	
	/**
	 * 判断网络是否连接
	 * @return 0未连接， 1移动网络， 2wifi
	 */
	public int getNetworkConnectionType(){
		final ConnectivityManager connMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);  
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null/* || !connectivityManager.getBackgroundDataSetting()*/) {
			return 0;
		}
  
		final NetworkInfo wifi =connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final NetworkInfo mobile =connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(wifi.isAvailable()){
			return 2;
		}else if(mobile.isAvailable()) {
			return 1;  
		} else{
    	   return 0;
		}
	}
	
	private Toast mToast;
	
	public void showToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
	
	public void showToast(int resId){
		try{
			if(mToast == null){
				mToast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
			}else{
				mToast.setText(resId);
				mToast.setDuration(Toast.LENGTH_SHORT);
			}
			mToast.show();
		}catch(Exception e){
			e.printStackTrace();
			try{
				if(mToast != null){
					mToast.cancel();
					mToast = null;
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}

}
