package com.ywhyw.bob.activity;

import java.util.Set;

import com.ywhyw.bob.R;
import com.ywhyw.bob.base.BaseActivity;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplshAcivity extends BaseActivity{

	private static final int WANT_TO_DEVICE_LIST = 1;
	private static final int WANT_TO_SEARCH = 2;
	private static final long SPLSH_DUR_TIME = 2000;
	private Handler splshHandler;
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splsh);
		
		splshHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Intent i = new Intent();
				switch (msg.what) {
				case WANT_TO_SEARCH:
					i.setClass(mContext, MainActivity.class);
					i.putExtra("openType", MainActivity.OPEN_FRAG_SEARCH);
					break;
				case WANT_TO_DEVICE_LIST:
					i.setClass(mContext, MainActivity.class);
					i.putExtra("openType", MainActivity.OPEN_FRAG_BOB);
					break;
				default:
					break;
				}
				startActivity(i);
				finish();
			}
		};
		queryDevice();
	}
	private void queryDevice() {
		Set<BluetoothDevice> bondedDevices = btAdapter.getBondedDevices();
		if(bondedDevices.size() > 0){
			showLog("有历史连接设备");
				splshHandler.sendEmptyMessageDelayed(WANT_TO_DEVICE_LIST,SPLSH_DUR_TIME);
		}else{
			showLog("无历史连接设备");
			splshHandler.sendEmptyMessageDelayed(WANT_TO_SEARCH,SPLSH_DUR_TIME);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
