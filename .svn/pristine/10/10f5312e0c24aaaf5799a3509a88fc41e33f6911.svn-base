package com.weidi.common.image;

import java.io.File;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.util.FileUtil;

@SuppressWarnings("deprecation")
public class ImageLoadCache {

	
	// 超时时间
	private int TIMEOUT = 20000;

	// 图片加载类
	private static ImageLoader mImageLoader;
	private static DisplayImageOptions binner_options;
	private static DisplayImageOptions icon_options;
	private static DisplayImageOptions user_icon_options;
	private static DisplayImageOptions girl_options;
	// Imageload缓存目录
	private static File cacheDir = new File(FileUtil.getRecentChatPath() + "/Imageload");



	static {
		ActivityManager am = (ActivityManager) QApp.getInstance()
				.getSystemService(Context.ACTIVITY_SERVICE);
		int memClass = am.getMemoryClass();
		int cacheSize = 1024 * 1024 * memClass / 8; // 硬引用缓存容量，为系统可用内存的1/4

		binner_options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_picture) // resource
				// or
				.showImageForEmptyUri(R.drawable.default_picture) // resource
				.showImageOnFail(R.drawable.default_picture) // resource or
				.cacheInMemory(false) // default
				.cacheOnDisc(true) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default
				.displayer(new SimpleBitmapDisplayer()) // default
				.build();
		icon_options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.head) // resource
														// or
				.showImageForEmptyUri(R.drawable.head) // resource
				.showImageOnFail(R.drawable.head) // resource or
				.cacheInMemory(true) // default
				.cacheOnDisc(true) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default
				.displayer(new RoundedBitmapDisplayer(50)) // default
				.build();
		user_icon_options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_female) // resource
				.showImageForEmptyUri(R.drawable.icon_female) // resource
				.showImageOnFail(R.drawable.icon_female) // resource
				.cacheInMemory(true) // default
				.cacheOnDisc(true) // default
				.imageScaleType(ImageScaleType.NONE) // default
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default
				.displayer(new SimpleBitmapDisplayer()) // default
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				QApp.getInstance()).threadPoolSize(10)
				.threadPriority(Thread.NORM_PRIORITY + 1)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(cacheSize))
				.memoryCacheSize(cacheSize)
				.discCacheSize(30 * 1024 * 1024)
				.discCacheFileCount(500)
				.discCache(new UnlimitedDiskCache(cacheDir))
				.writeDebugLogs().build();
		
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(config);
	}

	/**
	 * 根据url获取小图片 并自动设置到imageview中 获取的图片保存到内存(切勿加载大图)
	 * 
	 * @param img
	 * @param url
	 */
	public static void getIconBitmap(ImageView view, String url) {
		mImageLoader.displayImage(url, view, icon_options);
	}

	/**
	 * 根据url获取大图片 并自动设置到imageview中 获取的图片不保存到内存
	 * 
	 * @param img
	 * @param url
	 */
	public static void getBinnerBitmap(ImageView imageView, String url) {
		mImageLoader.displayImage(url, imageView, binner_options);
	}

	public static void getGirlBitmap(ImageView imageView, String url) {
		mImageLoader.displayImage(url, imageView, girl_options);
	}

	public static void getBinnerBitmap(String url, ImageLoadingListener listener) {
		mImageLoader.loadImage(url, listener);
	}

	/**
	 * 根据url获取大图片 并自动设置到imageview中 获取的图片不保存到内存
	 * 
	 * @param img
	 * @param url
	 */
	public static void getHalfHeightBitmap(ImageView imageView, String uri,
			ImageLoadingListener lister) {
		mImageLoader.displayImage(uri, imageView, binner_options, lister);
	}


}
