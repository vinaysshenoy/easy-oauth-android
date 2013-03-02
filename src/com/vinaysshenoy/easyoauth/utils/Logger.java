package com.vinaysshenoy.easyoauth.utils;

import android.util.Log;

public class Logger {

	private static boolean debugMode = false;

	public static void e(String tag, String msg) {

		if (debugMode)
			Log.e(tag, msg);

	}

	public static void d(String tag, String msg) {

		if (debugMode)
			Log.d(tag, msg);
	}

	public static void i(String tag, String msg) {

		if (debugMode)
			Log.i(tag, msg);
	}

	public static void v(String tag, String msg) {

		if (debugMode)
			Log.v(tag, msg);
	}

	public static void w(String tag, String msg) {

		if (debugMode)
			Log.w(tag, msg);
	}

	public static void wtf(String tag, String msg) {

		if (debugMode)
			Log.wtf(tag, msg);
	}

	public static void setDebugMode(boolean debug) {

		debugMode = debug;
	}

}
