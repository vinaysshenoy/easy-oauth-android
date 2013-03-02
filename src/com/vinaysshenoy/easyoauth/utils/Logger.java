/*Copyright 2012 Vinay S Shenoy

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, 
software distributed under the License is distributed on 
an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
either express or implied. See the License for the specific language 
governing permissions and limitations under the License.
 */
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
