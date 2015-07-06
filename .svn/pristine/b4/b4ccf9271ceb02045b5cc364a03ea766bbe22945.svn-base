package com.weidi.util;

import android.util.Log;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-6-8 下午7:11:17
 *@Description 1.0
 */
public class Logger {
	private Logger() {
	}
	private static final String TAG = "EIM";
	// 日志记录级别，开发阶段根据需求设置成大于0的数，项目正式发布后设置成0
	private static int LOGLEVEL = 6;
	private static int ERROR = 1;
	private static int WARN = 2;
	private static int INFO = 3;
	private static int DEBUG = 4;
	private static int VERBOSE = 5;

	public static void v(String tag, String msg) {
		if (LOGLEVEL > VERBOSE) {
			Log.v(TAG+tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (LOGLEVEL > DEBUG) {
			Log.d(TAG+tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (LOGLEVEL > INFO) {
			Log.i(TAG+tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (LOGLEVEL > WARN) {
			Log.w(TAG+tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (LOGLEVEL > ERROR) {
			Log.e(TAG+tag, msg);
		}
	}
}
