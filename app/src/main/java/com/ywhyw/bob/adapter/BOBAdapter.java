package com.ywhyw.bob.adapter;

import java.util.List;

import com.ywhyw.bob.R;
import com.ywhyw.bob.entity.MyDevice;
import com.ywhyw.bob.util.Constants;
import com.ywhyw.bob.util.LogUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BOBAdapter extends BaseAdapter{

	private Context mContext;
	private List<MyDevice> devices;
	
	
	public BOBAdapter(Context mContext, List<MyDevice> devices) {
		this.mContext = mContext;
		this.devices = devices;
	}

	@Override
	public int getCount() {
		return devices.size();
	}

	@Override
	public Object getItem(int position) {
		return devices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	class ViewHolder{
		TextView tvName;
		TextView tvId;
		TextView tvStatus;
		LinearLayout llDelete;
		ImageView ivIcon;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bob, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_device_name);
			holder.tvId = (TextView) convertView.findViewById(R.id.tv_device_id);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_device_status);
			holder.llDelete = (LinearLayout) convertView.findViewById(R.id.ll_device_delete);
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_device_icon);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final MyDevice device = devices.get(position);
		
		holder.tvName.setText(device.getDeviceName());
		holder.tvId.setText(device.getDeviceAddress());
		if(device.isConnected()){
			holder.tvStatus.setText(mContext.getResources().getString(R.string.connected));
		}else{
			holder.tvStatus.setText(mContext.getResources().getString(R.string.no_connected));
		}
		holder.ivIcon.setImageResource(R.mipmap.logo_bt_s);
		holder.llDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(mContext)
					.setTitle(mContext.getResources().getString(R.string.dialog_title_device_delete))
					.setMessage(mContext.getResources().getString(R.string.dialog_msg_device_delete))
					.setPositiveButton(mContext.getResources().getString(R.string.dialog_done_device_delete), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//删除设备
							LogUtil.showLog("删除设备 " + device.getDeviceName());
							Intent i = new Intent(Constants.DELETE_DEVICE);
							i.putExtra("id",device.getId());
							mContext.sendBroadcast(i);
						}
					})
					.setNegativeButton(mContext.getResources().getString(R.string.dialog_cancle_device_delete), null).show();
			}
		});
		
		return convertView;
	}

}
