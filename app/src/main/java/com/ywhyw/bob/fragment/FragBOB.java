package com.ywhyw.bob.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ywhyw.bob.R;
import com.ywhyw.bob.activity.DeviceDetailActivity;
import com.ywhyw.bob.activity.MainActivity;
import com.ywhyw.bob.adapter.BOBAdapter;
import com.ywhyw.bob.base.BaseFragment;
import com.ywhyw.bob.entity.MyDevice;
import com.ywhyw.bob.util.LogUtil;
import com.ywhyw.bob.util.XUtil;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.xutils.ex.DbException;
import org.xutils.x;

public class FragBOB extends BaseFragment{

	private View view;
	private MainActivity mainActivity;

	private List<MyDevice> devices;
	private ListView mListView;
	private BOBAdapter mAdapter;

	private LinearLayout noDevice;

	private DeviceReceiver deviceReceiver;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_bob, null);
		mainActivity = (MainActivity) getActivity();
		dbUtils = x.getDb(XUtil.getDaoConfig());
		
		registerReceiver();
		initViews();
		initList();
		for (MyDevice d : devices){
			if (d.isConnected()){
				showLog("发现有正在连接的设备，跳转至设备详情页");
				intent2Activity(DeviceDetailActivity.class);
			}
		}
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if(hidden){
			onPause();
		}else{
			onResume();
		}
	}
	
	@Override
	public void onPause() {
		reSetData();
		super.onPause();
	}



	private void registerReceiver() {
		deviceReceiver = new DeviceReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BOB_DEVICE_CONNECT_SUCCESS);
		filter.addAction(BOB_DEVICE_CONNECT_FAILED);
		filter.addAction(BOB_DEVICE_CONNECT_STATUS_CHANGED);
		filter.addAction(BOB_DEVICE_CONNECT_DELETE);
		filter.addAction(DELETE_DEVICE);

		filter.addAction(DEVICE_STATUS_CHANGED);
		mContext.registerReceiver(deviceReceiver, filter);
	}

	private void initViews() {
		mListView = (ListView) view.findViewById(R.id.lv_device);
		noDevice = (LinearLayout) view.findViewById(R.id.ll_no_device);
	}

	private void initList() {
		devices = new ArrayList<MyDevice>();
		mAdapter = new BOBAdapter(mContext, devices);
		mListView.setAdapter(mAdapter);
		reSetData();

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MyDevice device = devices.get(position);
				pd.setTitle(device.getDeviceName()+"...");
				pd.setMessage(mContext.getResources().getString(R.string.connecting));
				pd.show();
				if(device.isConnected()){
					//已连接，直接跳转到设备详情
					if(pd.isShowing()){
						pd.cancel();
					}
					Log.d("TAG", "设备已连接，直接跳转到设备详情");
					intent2Activity(DeviceDetailActivity.class);
				}else{
					showToast("正在连接设备，请稍后...");
					for (MyDevice myDevice : devices) {
						myDevice.setConnected(false);
					}
					try {
						dbUtils.saveOrUpdate(devices);
					} catch (DbException e) {
						e.printStackTrace();
					}
					Intent intent = new Intent(BOB_START_CONNECT);
					intent.putExtra("device", btAdapter.getRemoteDevice(device.getDeviceAddress()));
					mContext.sendBroadcast(intent);
					Log.d("TAG", "BOB 发送广播连接设备：" + device.getDeviceName());
				}
			}
		});
	}


	class DeviceReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(BOB_DEVICE_CONNECT_SUCCESS.contains(action)){
				if(pd.isShowing()){
					pd.dismiss();
				}
				showLog("BOB 接收到设备连接成功的广播");
				BluetoothDevice btd = intent.getParcelableExtra("device");
				showToast(btd.getName() + mContext.getResources().getString(R.string.is_connect_success));
				reSetData();
				//跳转到设备详情页
				Intent it = new Intent(mContext,DeviceDetailActivity.class);
				it.putExtra("device",btd);
				mContext.startActivity(it);
//				intent2Activity(DeviceDetailActivity.class);
			}
			if(BOB_DEVICE_CONNECT_FAILED.contains(action)){
				if(pd.isShowing()){
					pd.dismiss();
				}
				Log.d("TAG", "BOB 接收到设备连接失败，刷新界面");
				BluetoothDevice btd = intent.getParcelableExtra("device");
				showToast(btd.getName() + mContext.getResources().getString(R.string.is_connect_failed));
				reSetData();
			}
			if(BOB_DEVICE_CONNECT_STATUS_CHANGED.equals(action)){
				//search连接设备后通知bob刷新界面
				reSetData();
				//跳转到设备详情页
				intent2Activity(DeviceDetailActivity.class);
			}
			if(BOB_DEVICE_CONNECT_DELETE.equals(action)){
				reSetData();
			}
			if(DEVICE_STATUS_CHANGED.equals(action)){
				LogUtil.showLog("bob  收到main发送的设备状态改变的广播，刷新界面");
				reSetData();
			}
			if (DELETE_DEVICE.equals(action)){
				LogUtil.showLog("bob  收到adapter发送广播，删除设备");
				int id = intent.getIntExtra("id",-1);
				try {
					dbUtils.deleteById(MyDevice.class,id);
					reSetData();
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	private void reSetData(){
		devices.clear();
		Set<BluetoothDevice> bondedDevices = btAdapter.getBondedDevices();
		if(bondedDevices.size() > 0){
			for (BluetoothDevice bluetoothDevice : bondedDevices) {
//				showLog("当前设备有"+bluetoothDevice.getName());
				MyDevice data;
				try {
					data = dbUtils.selector(MyDevice.class).where("deviceAddress","=",bluetoothDevice.getAddress()).findFirst();
					if(data != null){
						devices.add(data);
//						showLog("显示数据-->" + data.toString());
					}else{
//						showLog("数据库中没有"+bluetoothDevice.getAddress()+"的数据");
					}
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		}
		mAdapter.notifyDataSetChanged();
		if (devices.size() > 0){
//			showLog("有设备devices.size ==> " + devices.size());
			mListView.setVisibility(View.VISIBLE);
			noDevice.setVisibility(View.GONE);
		}else{
//			showLog("无设备devices.size ==> " + devices.size());
			mListView.setVisibility(View.GONE);
			noDevice.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mainActivity.unregisterReceiver(deviceReceiver);
	}



}
