package com.ywhyw.bob.adapter;

import java.util.List;

import com.ywhyw.bob.R;
import com.ywhyw.bob.entity.MyDevice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter{

	private List<MyDevice> devices;
	private Context mContext;

	public SearchAdapter(List<MyDevice> devices, Context mContext) {
		this.devices = devices;
		this.mContext = mContext;
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
		ImageView ivIcon;
		TextView tvName;
		LinearLayout llCon;
		LinearLayout llNotCon;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_device_search, null);
			holder = new ViewHolder();
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_search_icon);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_search_name);
			holder.llCon = (LinearLayout) convertView.findViewById(R.id.ll_isConnect);
			holder.llNotCon = (LinearLayout) convertView.findViewById(R.id.ll_isNotConnect);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		MyDevice device = devices.get(position);
		if(device.isConnected()){
			holder.llCon.setVisibility(View.VISIBLE);
			holder.llNotCon.setVisibility(View.GONE);
		}else{
			holder.llCon.setVisibility(View.GONE);
			holder.llNotCon.setVisibility(View.VISIBLE);
		}
		holder.ivIcon.setImageResource(R.mipmap.logo_bt_b);
		holder.tvName.setText(device.getDeviceName());

		return convertView;
	}

}
