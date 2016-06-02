package com.ywhyw.bob.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	private static Toast  toast;
	private static int  duration = Toast.LENGTH_SHORT;
	
	public static void showToast(Context context,CharSequence text){
		if(toast == null){
			toast = Toast.makeText(context, text, duration);
		}else{
			toast.setDuration(duration);
			toast.setText(text);
		}
		toast.show();
	}
}
