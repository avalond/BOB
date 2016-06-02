package com.ywhyw.bob.base;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

import com.ywhyw.bob.util.Constants;
import com.ywhyw.bob.util.LogUtil;
import com.ywhyw.bob.util.ToastUtil;

import org.xutils.DbManager;
import org.xutils.x;

import java.util.Locale;


public class BaseActivity extends Activity implements Constants {

	protected Context mContext;
	protected SharedPreferences sp;
	protected DbManager dbUtil;
	protected BaseApplication app;
	protected BluetoothAdapter btAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mContext = this;
		sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
		dbUtil = x.getDb(BaseApplication.daoConfig);
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		app = (BaseApplication) mContext.getApplicationContext();

		String language = sp.getString("language", "zh");
		changeLanguage(language);
		int size = sp.getInt("txtSize", 0);
		changeSize(size);
	}

	protected void intent2Activity(Class<?> cls){
		Intent intent = new Intent(mContext, cls);
		mContext.startActivity(intent);
	}

	protected void showToast(CharSequence text){
		ToastUtil.showToast(mContext, text);
	}

	protected void showLog(String msg){
		LogUtil.showLog(msg);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	protected void changeLanguage(String language) {
		Resources resources = getResources();
		Configuration configuration = resources.getConfiguration();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		if(language.equals("en")){
			configuration.locale = Locale.ENGLISH;
			Log.d("TAG", "切换为英文");
		}else{
			configuration.locale = Locale.SIMPLIFIED_CHINESE;
			Log.d("TAG", "切换为中文");
		}
		resources.updateConfiguration(configuration, metrics);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("language", language);
		editor.commit();
	}

	public void changeSize(int size){
		Resources resources = getResources();
		Configuration configuration = resources.getConfiguration();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		if(size == -1){
			configuration.fontScale = 0.85f;
		}else if(size == 1){
			configuration.fontScale = 1.15f;
		}else{
			configuration.fontScale = 1.0f;
		}
		configuration.fontScale = 1.0f;
		resources.updateConfiguration(configuration, metrics);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("txtSize", size);
		editor.commit();
	}

}
