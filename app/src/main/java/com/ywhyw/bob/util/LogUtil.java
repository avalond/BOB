package com.ywhyw.bob.util;

import android.util.Log;

public class LogUtil implements Constants {

	public static void showLog(String msg){
		if(!isShowLog){
			return;
		}
		Log.d("TAG", msg);
	}
}
