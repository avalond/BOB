package com.ywhyw.bob.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ywhyw.bob.R;
import com.ywhyw.bob.activity.DeviceDetailActivity;
import com.ywhyw.bob.activity.MainActivity;
import com.ywhyw.bob.adapter.SearchAdapter;
import com.ywhyw.bob.base.BaseApplication;
import com.ywhyw.bob.base.BaseFragment;
import com.ywhyw.bob.entity.MyDevice;
import com.ywhyw.bob.util.LogUtil;
import com.ywhyw.bob.util.XUtil;

import org.xutils.ex.DbException;
import org.xutils.x;

public class FragSearch extends BaseFragment{

	private View view;
	private MainActivity mainActivity;

	private ListView lvDevice;
	private SearchAdapter mAdapter;
	private List<MyDevice> devices;

	private BTReceiver receiver;

	private LinearLayout llSearch;
	private LinearLayout llSearching;
	private View headView;
	private LinearLayout llHead;

	private LinearLayout llEmpty;

	private int curPosition;


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.frag_search, null);
		mainActivity = (MainActivity) getActivity();
		dbUtils = x.getDb(XUtil.getDaoConfig());

		initViews();
		initReceiver();
		setListView();
		initEvent();

		return view;
	}



	private void initViews() {
		lvDevice = (ListView) view.findViewById(R.id.lv_search);
		llSearch = (LinearLayout) view.findViewById(R.id.ll_search);
		llSearching = (LinearLayout) view.findViewById(R.id.ll_searching);
		headView = LayoutInflater.from(mContext).inflate(R.layout.head_view_search, null);
		llHead = (LinearLayout) headView.findViewById(R.id.ll_head);
		llEmpty = (LinearLayout) view.findViewById(R.id.ll_empty);

		lvDevice.addHeaderView(headView);
		llHead.setVisibility(View.GONE);
	}

	private void initReceiver() {
		receiver = new BTReceiver();
		IntentFilter filter = new IntentFilter();
		//打开、关闭蓝牙
		filter.addAction(STARTED_BT);
		filter.addAction(STOPED_BT);
		//查找可连接的蓝牙设备
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

		filter.addAction(SEARCH_DEVICE_CONNECT_SUCCESS);
		filter.addAction(SEARCH_DEVICE_CONNECT_FAILED);

		filter.addAction(DEVICE_STATUS_CHANGED);
		mContext.registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mContext.unregisterReceiver(receiver);
	}


	private void setListView() {
		devices = new ArrayList<MyDevice>();
		mAdapter = new SearchAdapter(devices, mContext);
		lvDevice.setAdapter(mAdapter);
		if (devices.size()==0){
			llEmpty.setVisibility(View.VISIBLE);
			lvDevice.setVisibility(View.GONE);
		}else{
			llEmpty.setVisibility(View.GONE);
			lvDevice.setVisibility(View.VISIBLE);
		}
	}



	private void initEvent() {
		//搜索当前可连接的设备
		llSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!btAdapter.isEnabled()){
					showLog("蓝牙未打开，后台打开蓝牙");
					btAdapter.enable();
					showToast("打开蓝牙后请点击搜索按钮");
					return;
				}
				llEmpty.setVisibility(View.GONE);
				lvDevice.setVisibility(View.VISIBLE);
				llSearch.setVisibility(View.GONE);
				llSearching.setVisibility(View.VISIBLE);
				llHead.setVisibility(View.VISIBLE);
				//				getUsbDevice();
				//				devices.clear();
				Log.d("TAG", "发送广播,开始搜索设备");
				mContext.sendBroadcast(new Intent(START_DISCOVERY));
			}
		});

		lvDevice.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if(position == 0){
					return;
				}
				curPosition = position - 1;

				if (btAdapter.isDiscovering()) {
					btAdapter.cancelDiscovery();
					llSearch.setVisibility(View.VISIBLE);
					llSearching.setVisibility(View.GONE);
					llHead.setVisibility(View.GONE);
				}
				pd.setTitle("连接设备");
				pd.setMessage("正在连接设备，请稍后...");
				pd.show();
				Intent intent = new Intent(SEARCH_START_CONNECT);
				intent.putExtra("device", btAdapter.getRemoteDevice(devices.get(curPosition).getDeviceAddress()));
				mContext.sendBroadcast(intent);
				Log.d("TAG", "search 发送广播连接设备：" + devices.get(curPosition).getDeviceName());
			}
		});

	}




	class BTReceiver extends BroadcastReceiver{


		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(BluetoothDevice.ACTION_FOUND.equals(action)){
				showLog("搜索到蓝牙设备");
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				MyDevice myDevice = new MyDevice();
				myDevice.setDeviceAddress(device.getAddress());
				myDevice.setDeviceName(device.getName());
				if(device.getBondState() == BluetoothDevice.BOND_BONDING){
					myDevice.setConnected(true);
				}else{
					myDevice.setConnected(false);
				}
				Log.d("TAG", "判断列表中是否已显示该设备");
				if(devices.size() > 0){
					for (MyDevice d : devices) {
						if(!d.getDeviceAddress().equals(myDevice.getDeviceAddress())){
							devices.add(myDevice);
						}
					}
				}else{
					devices.add(myDevice);
				}

				mAdapter.notifyDataSetChanged();
				if (devices.size()==0){
					llEmpty.setVisibility(View.VISIBLE);
					lvDevice.setVisibility(View.GONE);
				}else{
					llEmpty.setVisibility(View.GONE);
					lvDevice.setVisibility(View.VISIBLE);
				}
				//保存数据
				try {
					MyDevice data = dbUtils.selector(MyDevice.class).where("deviceAddress", "=", myDevice.getDeviceAddress()).findFirst();
					if(data == null){
						dbUtils.saveBindingId(myDevice);
					}else{
						data.setConnected(myDevice.isConnected());
						dbUtils.saveOrUpdate(data);
					}
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
			if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
				showLog("结束搜索");
				llSearch.setVisibility(View.VISIBLE);
				llSearching.setVisibility(View.GONE);
				llHead.setVisibility(View.GONE);
				if (devices.size()==0){
					llEmpty.setVisibility(View.VISIBLE);
					lvDevice.setVisibility(View.GONE);
					showToast("未搜索到相关设备，请打开设备蓝牙，或确保设备在有效距离内");
				}else{
					llEmpty.setVisibility(View.GONE);
					lvDevice.setVisibility(View.VISIBLE);
				}
			}
			if(SEARCH_DEVICE_CONNECT_SUCCESS.contains(action)){
				BluetoothDevice btd = intent.getParcelableExtra("device");
				Log.d("TAG", "search 接收到设备连接"+btd.getName()+"成功的广播");
				mAdapter.notifyDataSetChanged();
				//保存设备数据
				try {
					MyDevice data = dbUtils.selector(MyDevice.class).where("deviceAddress", "=", btd.getAddress()).findFirst();
					if(data == null){
						MyDevice curDevice = devices.get(curPosition);

						curDevice.setConnected(true);
						dbUtils.saveBindingId(curDevice);
					}else{
						data.setConnected(true);
						dbUtils.saveOrUpdate(data);
					}
				} catch (DbException e) {
					e.printStackTrace();
				}

				if (pd.isShowing()){
					pd.dismiss();
				}

				mAdapter.notifyDataSetChanged();

				Intent it = new Intent(mContext,DeviceDetailActivity.class);
				it.putExtra("device",btd);
				mContext.startActivity(it);

//				mainActivity.openFragmentFrist(MainActivity.OPEN_FRAG_BOB);
				//设备状态改变，广播通知bob刷新界面
//				mContext.sendBroadcast(new Intent(BOB_DEVICE_CONNECT_STATUS_CHANGED));
			}
			if(SEARCH_DEVICE_CONNECT_FAILED.contains(action)){
				BluetoothDevice btd = intent.getParcelableExtra("device");
				Log.d("TAG", "search 接收到设备"+btd.getName()+"连接失败，刷新界面");
				showToast(btd.getName() + "连接失败，请保持设备在有效范围");
				reSetData();
				mAdapter.notifyDataSetChanged();
				//保存设备数据
				try {
					MyDevice data = dbUtils.selector(MyDevice.class).where("deviceAddress", "=", btd.getAddress()).findFirst();
					if(data == null){
						MyDevice curDevice = devices.get(curPosition);

						curDevice.setConnected(true);
						dbUtils.saveBindingId(curDevice);
					}else{
						data.setConnected(true);
						dbUtils.saveOrUpdate(data);
					}
				} catch (DbException e) {
					e.printStackTrace();
				}

				if (pd.isShowing()){
					pd.dismiss();
				}

				mAdapter.notifyDataSetChanged();
			}
			if(DEVICE_STATUS_CHANGED.equals(action)){
				LogUtil.showLog("search  收到main发送的设备状态改变的广播，刷新界面");
				if (pd.isShowing()){
					pd.dismiss();
				}
				reSetData();
			}
		}

	}




	public void reSetData() {
		for (MyDevice myDevice : devices) {
			try {
				MyDevice findFirst = dbUtils.selector(MyDevice.class).where("deviceAddress", "=", myDevice.getDeviceAddress()).findFirst();
				if(findFirst != null){
					myDevice.setConnected(findFirst.isConnected());
				}
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
		mAdapter.notifyDataSetChanged();

	}

	@Override
	public void onResume() {
		reSetData();
		super.onResume();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if(!hidden){
			onResume();
		}
		super.onHiddenChanged(hidden);
	}


}
