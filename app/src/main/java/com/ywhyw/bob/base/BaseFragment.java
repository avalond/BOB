package com.ywhyw.bob.base;




import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;

import com.ywhyw.bob.util.Constants;
import com.ywhyw.bob.util.LogUtil;
import com.ywhyw.bob.util.ToastUtil;
import com.ywhyw.bob.util.XUtil;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.IOException;

public class BaseFragment extends Fragment implements Constants {
	
	protected Context mContext;
	protected DbManager dbUtils;
	protected BluetoothAdapter btAdapter;
	protected ProgressDialog pd;
	protected BaseApplication app;
	protected SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mContext =  getActivity();
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		pd = new ProgressDialog(mContext);
		app = (BaseApplication) mContext.getApplicationContext();
		dbUtils = x.getDb(XUtil.getDaoConfig());
		sp = mContext.getSharedPreferences(SP_NAME, mContext.MODE_PRIVATE);
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
	public void onDestroy() {
		super.onDestroy();
	}
}
