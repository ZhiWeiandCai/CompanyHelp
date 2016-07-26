package com.xht.android.companyhelp.ceche;

import android.content.Context;

import com.android.volley.cache.LruImageCache;

public class LruCacheManager {
	private static LruCacheManager sLruCacheManager;
	
	public static LruCacheManager getInstance(Context context) {
		if (sLruCacheManager == null) {
			sLruCacheManager = new LruCacheManager(context);
		}
		return sLruCacheManager;
	}
	
	private LruImageCache mLruImageCache;
	
	private LruCacheManager(Context context) {
		mLruImageCache = new LruImageCache(context);
	};
	
	public LruImageCache getLruImageCache() {
		return mLruImageCache;
	}

}
