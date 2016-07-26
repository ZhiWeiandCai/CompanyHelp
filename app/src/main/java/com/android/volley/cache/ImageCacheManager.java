package com.android.volley.cache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;

/**
 * Implementation of volley's ImageCache interface. This manager tracks the application image loader and cache. 
 *
 * Volley recommends an L1 non-blocking cache which is the default MEMORY CacheType. 
 * @author Trey Robinson
 *
 */
public class ImageCacheManager implements ImageCache{

	private static final int DISK_IMAGECACHE_SIZE = 1024*1024*30;
	private static final CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static final int DISK_IMAGECACHE_QUALITY = 100;

	/**
	 * Volley recommends in-memory L1 cache but both a disk and memory cache are provided.
	 * Volley includes a L2 disk cache out of the box but you can technically use a disk cache as an L1 cache provided
	 * you can live with potential i/o blocking. 
	 *
	 */
	public enum CacheType {
		DISK
		, MEMORY
		, IMAGE
	}

	private static ImageCacheManager mInstance;

	/**
	 * Volley image loader 
	 */
	private ImageLoader mImageLoader;

	/**
	 * Image cache implementation
	 */
	private ImageCache mImageCache;

	/**
	 * @return
	 * 		instance of the cache manager
	 */
	public static ImageCacheManager getInstance(){
		if(mInstance == null){
			mInstance = new ImageCacheManager();
		}

		return mInstance;
	}

	public static ImageCacheManager getInstance(Context context){
		if(mInstance == null){
			mInstance = new ImageCacheManager();
			RequestManager.init(context);
			mInstance.init(context,null, 0, null, 0, CacheType.IMAGE);
		}

		return mInstance;
	}
	/**
	 * Initializer for the manager. Must be called prior to use. 
	 *
	 * @param context
	 * 			application context
	 * @param uniqueName
	 * 			name for the cache location
	 * @param cacheSize
	 * 			max size for the cache
	 * @param compressFormat
	 * 			file type compression format.
	 * @param quality
	 */
	public void init(Context context, String uniqueName, int cacheSize, CompressFormat compressFormat, int quality, CacheType type){
		switch (type) {
			case DISK:
				mImageCache= new DiskLruImageCache(context, uniqueName, cacheSize, compressFormat, quality);
				break;
			case MEMORY:
				mImageCache = new BitmapLruImageCache(cacheSize);
			default:
				mImageCache = new LruImageCache(context);
				break;
		}

		mImageLoader = new ImageLoader(RequestManager.getRequestQueue(), this);
	}

	@Override
	public Bitmap getBitmap(String url) {
		return mImageCache.getBitmap(createKey(url));
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		try {
			mImageCache.putBitmap(createKey(url), bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 	Executes and image load
	 * @param url
	 * 		location of image
	 * @param listener
	 * 		Listener for completion
	 */
	public void getImage(String url, ImageListener listener){
		mImageLoader.get(url, listener);
	}

	/**
	 * @return
	 * 		instance of the image loader
	 */
	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	/**
	 * Creates a unique cache key based on a url value
	 * @param url
	 * 		url to be used in key creation
	 * @return
	 * 		cache key value
	 */
	private String createKey(String url){
		String key = url;
		key = String.valueOf(url.hashCode());

		return key;
	}

	private String getFilenameForKey(String key) {
		int firstHalfLength = key.length() / 2;
		String localFilename = String.valueOf(key.substring(0, firstHalfLength).hashCode());
		localFilename += String.valueOf(key.substring(firstHalfLength).hashCode());
		return localFilename;
	}

	/**
	 * 根据原始键生成新键，以保证键的名称的合法性
	 * @param key 原始键，通常是url
	 * @return
	 */
	public static String generateKey(String key)
	{
		String cacheKey;
		try
		{
			MessageDigest digest = MessageDigest.getInstance("md5");
			digest.update(key.getBytes());
			cacheKey = bytesToHexString(digest.digest());
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private static String bytesToHexString(byte[] bytes)
	{
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < bytes.length; i++)
		{
			String hex = Integer.toHexString(0xff&bytes[i]);
			if(hex.length() == 1)
				builder.append('0');
			builder.append(hex);
		}
		return builder.toString();
	}
}

