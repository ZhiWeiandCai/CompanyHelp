package com.android.volley.cache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
/**
 * Basic LRU Memory cache.
 * 
 */
public class BitmapLruImageCache extends LruCache<String, Bitmap> implements ImageCache{
	
	private final static String tag = BitmapLruImageCache.class.getSimpleName();

	public static int getDefaultLruCacheSize() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;

		Log.v(tag, "" + cacheSize);
		return cacheSize;
	}

	public BitmapLruImageCache() {
		this(getDefaultLruCacheSize());
	}
	
	public BitmapLruImageCache(int maxSize) {
		super(maxSize);
	}
	
	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight() / 1024;
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}
 
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}
	

	public void clearCache() {
		if (size() > 0) {
			Log.i(tag, "clear cache");
			evictAll();
		}
	}
}
