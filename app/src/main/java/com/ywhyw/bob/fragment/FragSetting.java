package com.ywhyw.bob.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ywhyw.bob.R;
import com.ywhyw.bob.activity.AboutActivity;
import com.ywhyw.bob.activity.DeviceDetailActivity;
import com.ywhyw.bob.activity.MainActivity;
import com.ywhyw.bob.base.BaseFragment;
import com.ywhyw.bob.entity.SettingStatus;
import com.ywhyw.bob.entity.SettingStatusUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.qqtheme.framework.picker.OptionPicker;

public class FragSetting extends BaseFragment implements OnClickListener{

	private View view;
	private MainActivity mainActivity;
	
	private SettingStatus status;
	private SettingStatusUtil statusUtil;
	
	private SettingReceiver mReceiver;
	
	private ImageButton ibBTStatus;
	private ProgressBar pbBtStatus;
	private ImageButton ibUSBStatus;
	private TextView tvStyle;
	private TextView tvLanguage;
	private TextView tvTxtSize;
	private TextView tvConnect;
	private TextView tvVersionCode;
	
	private RelativeLayout rlStyle;
	private RelativeLayout rlLanguage;
	private RelativeLayout rlTxtSize;
	private RelativeLayout rlConnect;
	private RelativeLayout rlVersionCode;
	private RelativeLayout rlAbout;
	

	private String lan ;
	private int size ;
	private boolean lanHasChanged = false;
	private boolean sizeHasChanged = false;
	private String [] languages = new String[]{"繁体中文","简体中文","英文"};
	private String [] textSizes = new String[]{"小","中","大"};
	private String [] connections = new String[]{"自动","手动"};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.frag_setting, null);
		mainActivity = (MainActivity) getActivity();
		statusUtil = SettingStatusUtil.getInstance();
		status = statusUtil.getSettingStatus(mContext);
		
		initView();
		initEvent();
		initReceiver();

		return view;
	}
	



	private void initView() {
		
		ibBTStatus = (ImageButton) view.findViewById(R.id.ib_bt_status);
		ibUSBStatus = (ImageButton) view.findViewById(R.id.ib_device_status);
		pbBtStatus = (ProgressBar) view.findViewById(R.id.pb_changing_bt_status);
		tvStyle = (TextView) view.findViewById(R.id.tv_style);
		tvLanguage = (TextView) view.findViewById(R.id.tv_language);
		tvTxtSize = (TextView) view.findViewById(R.id.tv_txt_size);
		tvConnect = (TextView) view.findViewById(R.id.tv_connect);
		tvVersionCode = (TextView) view.findViewById(R.id.tv_version_code);
		
		rlStyle = (RelativeLayout) view.findViewById(R.id.rl_style);
		rlLanguage = (RelativeLayout) view.findViewById(R.id.rl_language);
		rlTxtSize = (RelativeLayout) view.findViewById(R.id.rl_txt_size);
		rlConnect = (RelativeLayout) view.findViewById(R.id.rl_connect);
		rlVersionCode = (RelativeLayout) view.findViewById(R.id.rl_update);
		rlAbout = (RelativeLayout) view.findViewById(R.id.rl_about);

		lan = sp.getString("language", "zh");
		size = sp.getInt("txtSize", 0);
		showLog("当前语音为：" + lan + "  字体大小为：" + size);
		setLayout();
	}
	private void setLayout() {
		if(btAdapter.isEnabled()){
			ibBTStatus.setImageResource(R.mipmap.tb_on);
		}else{
			ibBTStatus.setImageResource(R.mipmap.tb_off);
		}
		
		if(status.isUSBAutoConnect()){
			ibUSBStatus.setImageResource(R.mipmap.tb_on);
		}else{
			ibUSBStatus.setImageResource(R.mipmap.tb_off);
		}
		tvStyle.setText(status.getStyle());
		tvLanguage.setText(status.getLanguage().equals("zh") ? "简体中文" : "英文");
		String txtSize;
		if (status.getTxtSize() == -1){
			txtSize = "小号";
		}else if(status.getTxtSize() == 1){
			txtSize = "大号";
		}else{
			txtSize = "中号";
		}
		tvTxtSize.setText(txtSize);
		tvConnect.setText(status.isAutoConnect() ? 
				mContext.getResources().getString(R.string.auto_on) : 
					mContext.getResources().getString(R.string.auto_off));
		tvVersionCode.setText(status.getVersionCode());
	}
	
	private void initEvent() {
		ibBTStatus.setOnClickListener(this);
		ibUSBStatus.setOnClickListener(this);
		rlStyle.setOnClickListener(this);
		rlLanguage.setOnClickListener(this);
		rlTxtSize.setOnClickListener(this);
		rlConnect.setOnClickListener(this);
		rlVersionCode.setOnClickListener(this);
		rlAbout.setOnClickListener(this);
	}
	
	private void initReceiver() {
		mReceiver = new SettingReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(STARTED_BT);
		filter.addAction(STOPED_BT);
		mContext.registerReceiver(mReceiver, filter);
	}
	
	class SettingReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(STARTED_BT.equals(action)){
				pbBtStatus.setVisibility(View.GONE);
				ibBTStatus.setVisibility(View.VISIBLE);
				ibBTStatus.setImageResource(R.mipmap.tb_on);
			}
			if(STOPED_BT.equals(action)){
				pbBtStatus.setVisibility(View.GONE);
				ibBTStatus.setVisibility(View.VISIBLE);
				ibBTStatus.setImageResource(R.mipmap.tb_off);
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_bt_status:
			pbBtStatus.setVisibility(View.VISIBLE);
			ibBTStatus.setVisibility(View.GONE);
			if(btAdapter.isEnabled()){
				mContext.sendBroadcast(new Intent(STOP_BT));
			}else{
				mContext.sendBroadcast(new Intent(START_BT));
			}
			break;
			
		case R.id.ib_device_status:
			if(status.isUSBAutoConnect()){
				status.setUSBAutoConnect(false);
				ibUSBStatus.setImageResource(R.mipmap.tb_off);
				showToast("当前USB连接状态为：手动");
			}else{
				status.setUSBAutoConnect(true);
				ibUSBStatus.setImageResource(R.mipmap.tb_on);
				showToast("当前USB连接状态为：自动");
			}
			statusUtil.setSettingStatus(mContext, status);
			break;
		case R.id.rl_style:
			showToast("当前风格为-->" + status.getStyle());
			break;
		case R.id.rl_language:
			showPicker(languages);
			break;
		case R.id.rl_txt_size:
			showPicker(textSizes);
			break;
			case R.id.rl_connect:
				showPicker(connections);
			break;
		case R.id.rl_update:
			intent2Activity(DeviceDetailActivity.class);
			break;
		case R.id.rl_about:
			intent2Activity(AboutActivity.class);
			break;
		default:
			break;
		}
	}

	private void showPicker(String[] strs) {
		OptionPicker picker = new OptionPicker(getActivity(), strs);
		picker.setOffset(2);
		picker.setSelectedIndex(1);
		picker.setTextSize(16);
		picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
			@Override
			public void onOptionPicked(String option) {
				showToast(option);
				if ("繁体中文".equals(option)){
					if(!lan.equals("zh")){
						lan = "zh";
						lanHasChanged = true;
					}
				}else if ("简体中文".equals(option)){
					if(!lan.equals("zh")){
						lan = "zh";
						lanHasChanged = true;
					}
				}else if ("英文".equals(option)){
					if(!lan.equals("en")){
						lan = "en";
						lanHasChanged = true;
					}
				}else if ("小".equals(option)){
					if(size != -1){
						size = -1;
						sizeHasChanged = true;
					}
				}else if ("中".equals(option)){
					if(size != 0){
						size = 0;
						sizeHasChanged = true;
					}
				}else if ("大".equals(option)){
					if(size != 1){
						size = 1;
						sizeHasChanged = true;
					}
				}else if("自动".equals(option)){
					status.setAutoConnect(true);
					showToast("当前连接为自动");
					status.setAutoConnect(true);
					tvConnect.setText("自动");
					statusUtil.setSettingStatus(mContext,status);
				}else if("手动".equals(option)) {
					status.setAutoConnect(false);
					showToast("当前连接为手动");
					status.setAutoConnect(false);
					tvConnect.setText("手动");
					statusUtil.setSettingStatus(mContext,status);
				}

				if(lanHasChanged){
					showLog("语言改变为：" + lan);
					mainActivity.changeLanguage1(lan);
				}else{
					showLog("语言未改变");
				}

				if(sizeHasChanged){
					//TODO 改变字体
					showLog("字体大小改变为：" + size);
					mainActivity.changeSize1(size);
				}else{
					showLog("字体大小未改变");
				}

			}
		});
		picker.show();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		mContext.unregisterReceiver(mReceiver);
	}
	
}
