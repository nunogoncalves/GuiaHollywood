package com.numicago.guiahollywood;

import android.util.Log;

public abstract class MyLog {

	public static void p(String text) {
		Log.d("com.numicago.android.guiahollywood1", text);
	}
	
	public static void p(String context, String text) {
		Log.d(context, text);
	}
}
