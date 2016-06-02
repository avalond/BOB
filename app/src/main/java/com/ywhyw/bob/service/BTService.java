package com.ywhyw.bob.service;

import android.app.Service;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.ywhyw.bob.util.Constants;
import com.ywhyw.bob.util.LogUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class BTService extends Service implements Constants {

	static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	static final String A2DP_SRC_UUID  = "0000110B-0000-1000-8000-00805F9B34FB";

	private UUID uuid ;
	private BluetoothAdapter btAdapter;
	private BluetoothSocket btSocket;

	private btReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		LogUtil.showLog("BTService onCreat");
		btAdapter = BluetoothAdapter.getDefaultAdapter();

		uuid = UUID.fromString( SPP_UUID);

		receiver = new btReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(START_BT);
		filter.addAction(STOP_BT);
		
		filter.addAction(START_DISCOVERY);
		filter.addAction(SEARCH_START_CONNECT);
		
		filter.addAction(BOB_START_CONNECT);
		filter.addAction(BOB_DEVICE_CONNECT_DELETE);
		
		filter.addAction(BTSOCKET_SENG_MSG);
		registerReceiver(receiver, filter);


		super.onCreate();
	}


	class btReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(START_BT.equals(action)){
				btAdapter.enable();
				Log.d("TAG", "service 打开蓝牙");
				while(!btAdapter.isEnabled()){
				}
				sendBroadcast(new Intent(STARTED_BT));
				Log.d("TAG", "发送广播通知主界面蓝牙已经打开");
			}
			if(STOP_BT.equals(action)){
				btAdapter.disable();
				Log.d("TAG", "service 关闭蓝牙");
				while(btAdapter.isEnabled()){
				}
				sendBroadcast(new Intent(STOPED_BT));
				Log.d("TAG", "发送广播通知主界面蓝牙已经关闭");
			}
			
			
			if(START_DISCOVERY.equals(action)){
				btAdapter.startDiscovery();
			}
			if(SEARCH_START_CONNECT.equals(action)){
				BluetoothDevice device = intent.getParcelableExtra("device");
				Log.d("TAG", "收到SEARCH广播，开始连接设备："+device.getName());
				ConnectDevice(device,SEARCH_START_CONNECT);
			}
			if(BOB_START_CONNECT.equals(action)){
				BluetoothDevice device = intent.getParcelableExtra("device");
				Log.d("TAG", "收到BOB广播，开始连接设备："+device.getName());
				ConnectDevice(device,BOB_START_CONNECT);
			}
			if(BOB_DEVICE_CONNECT_DELETE.equals(action)){
				try {
					if(btSocket != null){
						btSocket.close(); 
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(BTSOCKET_SENG_MSG.equals(action)){
				String msg = intent.getStringExtra("msg");
				sendMsg(msg);
			}
		}

	}

	private void ConnectDevice(BluetoothDevice device,String type){
		Intent intent = new Intent();
		//断开之前连接
		try {
			if(btSocket != null){
				btSocket.close();
				Log.d("TAG", "断开之前连接");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			//开始配对的时候停止搜索
			btAdapter.cancelDiscovery();
			//			btSocket = device.createRfcommSocketToServiceRecord(uuid);
			btSocket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device, 1);
			btSocket.connect();

		} catch (IOException e) {
			e.printStackTrace();
			Log.d("TAG", "连接设备："+device.getName()+"失败 " + e.getMessage());
		}catch (IllegalAccessException e) {
			e.printStackTrace();
			Log.d("TAG", "连接设备："+device.getName()+"失败 " + e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Log.d("TAG", "连接设备："+device.getName()+"失败 " + e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			Log.d("TAG", "连接设备："+device.getName()+"失败 " + e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			Log.d("TAG", "连接设备："+device.getName()+"失败 " + e.getMessage());
		}
		if(!btSocket.isConnected()){
			if(type.equals(SEARCH_START_CONNECT)){
				intent.setAction(SEARCH_DEVICE_CONNECT_FAILED);
				Log.d("TAG", "连接设备："+device.getName()+"失败  给SEARCH发送广播");
			}else{
				intent.setAction(BOB_DEVICE_CONNECT_FAILED);
				Log.d("TAG", "连接设备："+device.getName()+"失败  给BOB发送广播");
			}
			intent.putExtra("device", device);
			sendBroadcast(intent);
		}else{
			if(type.equals(BOB_START_CONNECT)){
				intent.setAction(BOB_DEVICE_CONNECT_SUCCESS);
				Log.d("TAG", "连接设备："+device.getName() + "成功  给BOB发送广播");
			}else{
				intent.setAction(SEARCH_DEVICE_CONNECT_SUCCESS);
				Log.d("TAG", "连接设备："+device.getName() + "成功  给SEARCH发送广播");
			}
			intent.putExtra("device", device);
			sendBroadcast(intent);
		}
	}


	protected void sendMsg(String msg) {

		OutputStream outStream = null;
		try {
			outStream = btSocket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.showLog("发送失败：" + e.getMessage());
		}

		byte[] msgBuffer = null;		
		try {
			msgBuffer = msg.getBytes("GBK");//编码
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}		
		try {
			outStream.write(msgBuffer);				
			LogUtil.showLog("成功发送指令:"+msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.showLog("发送数据失败");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	public void sendBtStatus(boolean b) {
		while(btAdapter.isEnabled() == b){
			if(b){
				sendBroadcast(new Intent(STARTED_BT));
				Log.d("TAG", "发送广播通知主界面蓝牙已经打开");
			}else{
				sendBroadcast(new Intent(STOPED_BT));
				Log.d("TAG", "发送广播通知主界面蓝牙已经关闭");
			}
		}
	}




}
