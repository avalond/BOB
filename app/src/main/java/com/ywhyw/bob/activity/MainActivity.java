package com.ywhyw.bob.activity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.ywhyw.bob.R;
import com.ywhyw.bob.entity.MyDevice;
import com.ywhyw.bob.fragment.FragBOB;
import com.ywhyw.bob.fragment.FragSearch;
import com.ywhyw.bob.fragment.FragSetting;
import com.ywhyw.bob.service.BTService;
import com.ywhyw.bob.service.PlayMusicService;
import com.ywhyw.bob.util.Constants;
import com.ywhyw.bob.util.LogUtil;
import com.ywhyw.bob.util.ToastUtil;
import com.ywhyw.bob.util.XUtil;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;


public class MainActivity extends FragmentActivity implements OnCheckedChangeListener,OnClickListener,Constants {

	public static final int OPEN_FRAG_SEARCH = 100;
	public static final int OPEN_FRAG_BOB = 101;
	public static final int OPEN_FRAG_SETTING = 102;
	private Context mContext;
	protected SharedPreferences sp;
	private RadioGroup mRg;
	private RadioButton rbSearch;
	private RadioButton rbBob;
	private RadioButton rbSetting;
	private FragmentManager fm;
	private FragSearch fragSearch;
	private FragBOB fragBOB;
	private FragSetting fragSetting;

	private int openType;

	private DbManager dbUtils;
	private MainReceiver mainReceiver;

	protected BluetoothAdapter btAdapter;

	private static boolean isExit = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);

		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if(!btAdapter.isEnabled()){
			btAdapter.enable();
			ToastUtil.showToast(this,"使用前请打开蓝牙");
		}

		sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
		String language = sp.getString("language", "zh");
		changeLanguage(language);
		int size = sp.getInt("txtSize", 0);
		changeSize(size);

		fm = getSupportFragmentManager();

		mContext = this;
		dbUtils = x.getDb(XUtil.getDaoConfig());
		startService();

		initViews();
		openType = getIntent().getIntExtra("openType", OPEN_FRAG_BOB);

		initReceiver();

		openFragmentFrist(openType);

	}

	private void initReceiver() {
		mainReceiver = new MainReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
		filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
		registerReceiver(mainReceiver, filter);
	}

	class MainReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
				LogUtil.showLog("main  收到有蓝牙设备连接的广播");
				BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				LogUtil.showLog("连接的蓝牙设备为：" + btDevice.getName());
				try {
					MyDevice data = dbUtils.selector(MyDevice.class).where("deviceAddress", "=", btDevice.getAddress()).findFirst();
					if (data != null) {
						data.setConnected(true);
						dbUtils.saveOrUpdate(data);
						LogUtil.showLog("main  保存数据--" + data.toString());
					} else {
						LogUtil.showLog("数据库中无此设备");
					}
				} catch (DbException e) {
					e.printStackTrace();
				}
				//发广播通知设备状态改变
				LogUtil.showLog("main  保存状态后通知search及bob更新界面");
				sendBroadcast(new Intent(DEVICE_STATUS_CHANGED));
			}
			if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
				LogUtil.showLog("main  收到有蓝牙设备断开连接的广播");
				BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				try {
					MyDevice data = dbUtils.selector(MyDevice.class).where("deviceAddress", "=", btDevice.getAddress()).findFirst();
					if (data != null) {
						data.setConnected(false);
						dbUtils.saveOrUpdate(data);
						LogUtil.showLog("main  保存数据--" + data.toString());
					}
				} catch (DbException e) {
					e.printStackTrace();
				}
				//发广播通知设备状态改变
				LogUtil.showLog("main  保存状态后通知search及bob更新界面");
				sendBroadcast(new Intent(DEVICE_STATUS_CHANGED));
			}
		}

	}

	private void startService() {
		startService(new Intent(mContext, BTService.class));
		startService(new Intent(mContext, PlayMusicService.class));
	}

	public void openFragmentFrist(int type) {
		switch (type) {
			case OPEN_FRAG_SEARCH:
				rbSearch.performClick();
				break;
			case OPEN_FRAG_BOB:
				rbBob.performClick();
				break;
			case OPEN_FRAG_SETTING:
				rbSetting.performClick();
				break;

			default:
				break;
		}
	}

	private void initViews() {
		mRg = (RadioGroup) findViewById(R.id.rg);
		rbSearch = (RadioButton) findViewById(R.id.rb_search);
		rbBob = (RadioButton) findViewById(R.id.rb_bob);
		rbSetting = (RadioButton) findViewById(R.id.rb_setting);
		mRg.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.rb_search:
				showFragment(0);
				break;
			case R.id.rb_bob:
				showFragment(1);
				break;
			case R.id.rb_setting:
				showFragment(2);
				break;

			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void onDestroy() {
		//关闭音乐跟socket的service
		stopService(new Intent(mContext, BTService.class));
		stopService(new Intent(mContext, PlayMusicService.class));
		try {
			List<MyDevice> findAll = dbUtils.findAll(MyDevice.class);
			if (findAll != null && findAll.size() > 0){
				for (MyDevice device : findAll) {
					device.setConnected(false);
				}
				dbUtils.saveOrUpdate(findAll);
				sendBroadcast(new Intent(DEVICE_STATUS_CHANGED));
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbUtils.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		unregisterReceiver(mainReceiver);
		super.onDestroy();
	}

	private void showFragment(int position) {
		FragmentTransaction ft = fm.beginTransaction();
		hideFragment(ft);
		switch (position) {
			case 0:
				if (fragSearch == null) {
					fragSearch = new FragSearch();
					ft.add(R.id.fl_content, fragSearch);
				} else {
					ft.show(fragSearch);
				}
				LogUtil.showLog("search");
				break;
			case 1:
				if (fragBOB == null) {
					fragBOB = new FragBOB();
					ft.add(R.id.fl_content, fragBOB);
				} else {
					ft.show(fragBOB);
				}
				LogUtil.showLog("bob");
				break;
			case 2:
				if (fragSetting == null) {
					fragSetting = new FragSetting();
					ft.add(R.id.fl_content, fragSetting);
				} else {
					ft.show(fragSetting);
				}
				LogUtil.showLog("setting");
				break;
		}
		ft.commit();
	}

	private void hideFragment(FragmentTransaction ft) {
		if (fragSearch != null) {
			ft.hide(fragSearch);
		}
		if (fragBOB != null) {
			ft.hide(fragBOB);
		}
		if (fragSetting != null) {
			ft.hide(fragSetting);
		}
	}

	public void changeLanguage1(String language) {
		Resources resources = getResources();
		Configuration configuration = resources.getConfiguration();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		if (language.equals("en")) {
			configuration.locale = Locale.ENGLISH;
			Log.d("TAG", "切换为英文");
		} else {
			configuration.locale = Locale.SIMPLIFIED_CHINESE;
			Log.d("TAG", "切换为中文");
		}
		resources.updateConfiguration(configuration, metrics);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("language", language);
		editor.commit();
		finish();
		Intent i = new Intent(mContext, MainActivity.class);
		i.putExtra("openType", MainActivity.OPEN_FRAG_SETTING);
		mContext.startActivity(i);
	}

	public void changeLanguage(String language) {
		Resources resources = getResources();
		Configuration configuration = resources.getConfiguration();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		if (language.equals("en")) {
			configuration.locale = Locale.ENGLISH;
			Log.d("TAG", "切换为英文");
		} else {
			configuration.locale = Locale.SIMPLIFIED_CHINESE;
			Log.d("TAG", "切换为中文");
		}
		resources.updateConfiguration(configuration, metrics);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("language", language);
		editor.commit();
	}

	public void changeSize(int size) {
		Resources resources = getResources();
		Configuration configuration = resources.getConfiguration();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		if (size == -1) {
			configuration.fontScale = 0.85f;
		} else if (size == 1) {
			configuration.fontScale = 1.15f;
		} else {
			configuration.fontScale = 1.0f;
		}
		resources.updateConfiguration(configuration, metrics);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("txtSize", size);
		editor.commit();
	}

	public void changeSize1(int size) {
		Resources resources = getResources();
		Configuration configuration = resources.getConfiguration();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		if (size == -1) {
			configuration.fontScale = 0.85f;
		} else if (size == 1) {
			configuration.fontScale = 1.15f;
		} else {
			configuration.fontScale = 1.0f;
		}
		configuration.fontScale = 1.0f;
		resources.updateConfiguration(configuration, metrics);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("txtSize", size);
		editor.commit();
		finish();
		Intent i = new Intent(mContext, MainActivity.class);
		i.putExtra("openType", MainActivity.OPEN_FRAG_SETTING);
		mContext.startActivity(i);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			LogUtil.showLog("press back");
//			AlertDialog dialog = new AlertDialog.Builder(mContext)
//					.setTitle("退出")
//					.setMessage("退出并关闭蓝牙？")
//					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							btAdapter.disable();
//							finish();
//						}
//					}).create();
//			dialog.show();
//			return true;
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次关闭蓝牙并退出程序",
					Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			btAdapter.disable();

			this.finish();
		}
	}


	private static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};





}