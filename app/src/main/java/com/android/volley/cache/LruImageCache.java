package com.android.volley.cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader.ImageCache;
/**
 * Basic LRU cache.
 * 
 */
public class LruImageCache implements ImageCache{
	private static final int MEMORY_IMAGECACHE_SIZE = 1024*1024*20;
	private static final int DISK_IMAGECACHE_SIZE = 1024*1024*15;
	private static final CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static final int DISK_IMAGECACHE_QUALITY = 100;
	
	private BitmapLruImageCache mBitmapLruImageCache;
	private DiskLruImageCache mDiskLruImageCache;
	private static ExecutorService service = null;
	
	public LruImageCache(Context context) {
		mBitmapLruImageCache = new BitmapLruImageCache(MEMORY_IMAGECACHE_SIZE);
		mDiskLruImageCache = new DiskLruImageCache(context, context.getPackageName(), 
				DISK_IMAGECACHE_SIZE, DISK_IMAGECACHE_COMPRESS_FORMAT, DISK_IMAGECACHE_QUALITY);
	}
	
	
	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = mBitmapLruImageCache.getBitmap(url);
		
		Log.d("LruImageCache", "getBitmap url key:"+url+","+(bitmap == null));
		if(bitmap == null){
			bitmap = mDiskLruImageCache.getBitmap(url);
			if(bitmap != null)
				mBitmapLruImageCache.putBitmap(url, bitmap);
		}
		
		return bitmap;
	}
 
	@Override
	public void putBitmap(final String url, final Bitmap bitmap) {
		if(service == null)
			service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {
			
			@Override
			public void run() {
				mBitmapLruImageCache.putBitmap(url, bitmap);
				mDiskLruImageCache.putBitmap(url, bitmap);
			}
		});
		
	}
}
