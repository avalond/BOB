package com.ywhyw.bob.util;

public interface Constants {

	String SP_NAME = "sp_bob";
	boolean isShowLog = true;
	/**
	 * 音乐播放状态改变
	 */
	String MUSIC_HAS_CHANGED = "musicHasChanged";


	/**
	 * Log TAG
	 */
	public static final String TAG = "cn.com.tarena.music_player";
	/**
	 * 播放模式：顺序播放
	 */
	int PLAY_MODE_ORDERED = 0;
	/**
	 * 播放模式：随机播放
	 */
	int PLAY_MODE_RANDOM = 1;
	/**
	 * 播放模式：随机播放
	 */
	int PLAY_MODE_ONLY = 2;
	//
	//
	// 以下是Activity发出的广播
	//
	//
	/**
	 * Activity发出的，播放按钮被点击
	 */
	String ACTIVITY_PLAY_BUTTON_CLICK = "cn.com.tarena.music_player.intent.action.PLAY_BUTTON_CLICK";
	/**
	 * Activity发出的，当前歌曲被点击时未播放
	 */
	String ACTIVITY_CURRENT_MUSIC_CLICK = "currentMusicClick";
	/**
	 * Activity发出的，当前歌曲播放是被删除
	 */
	String ACTIVITY_CURRENT_MUSIC_DELETE = "currentMusicDelete";
	/**
	 * Activity发出的，上一首按钮被点击
	 */
	String ACTIVITY_PREVIOUS_BUTTON_CLICK = "cn.com.tarena.music_player.intent.action.PREVIOUS_BUTTON_CLICK";
	/**
	 * Activity发出的，下一首按钮被点击
	 */
	String ACTIVITY_NEXT_BUTTON_CLICK = "cn.com.tarena.music_player.intent.action.NEXT_BUTTON_CLICK";
	/**
	 * Activity发出的，播放的歌曲发生变化
	 */
	String ACTIVITY_MUSIC_INDEX_CHANGED = "cn.com.tarena.music_player.intent.action.MUSIC_INDEX_CHANGED";
	/**
	 * Activity发出的，进度条被拖拽
	 */
	String ACTIVITY_SEEKBAR_CHANGED = "cn.com.tarena.music_player.intent.action.SEEKBAR_CHANGED";
	//
	//
	// 以下是play music Service发出的广播
	//
	//
	/**
	 * Service发出的，播放歌曲
	 */
	String SERVICE_PLAYER_PLAY = "cn.com.tarena.music_player.intent.action.PLAYER_PLAY";
	/**
	 * Service发出的，暂停播放歌曲
	 */
	String SERVICE_PLAYER_PAUSE = "cn.com.tarena.music_player.intent.action.PLYAER_PAUSE";
	/**
	 * Service发出的，更新进度播放歌曲
	 */
	String SERVICE_UPDATE_PROGRESS = "cn.com.tarena.music_player.intent.action.UPDATE_PROGRESS";




	//
	//
	//一下是BTService发出的广播
	//
	//
	String START_BT = "startBt";
	String STOP_BT = "stopBt";

	String STOPED_BT = "stopedBt";
	String STARTED_BT = "startedBt";

	String START_DISCOVERY = "startDiscovery";

	//search界面的action
	String SEARCH_START_CONNECT = "searchStartConnect";
	String SEARCH_DEVICE_CONNECT_SUCCESS = "searchDeviceConnectSuccess";
	String SEARCH_DEVICE_CONNECT_FAILED = "searchDeviceConnectFailed";
	//删除当前连接
	String SEARCH_DEVICE_CONNECT_DELETE = "searchDeviceConnectDelete";
	String SEARCH_DEVICE_CONNECT_STATUS_CHANGED = "searchDeviceConnectStatusChanged";
	

	//bob界面的action
	String BOB_START_CONNECT = "bobDtartConnect";
	String BOB_DEVICE_CONNECT_SUCCESS = "bobDeviceConnectSuccess";
	String BOB_DEVICE_CONNECT_FAILED = "bobDeviceConnectFailed";
	//删除当前连接
	String BOB_DEVICE_CONNECT_DELETE = "bobDeviceConnectDelete";
	String BOB_DEVICE_CONNECT_STATUS_CHANGED = "bobDeviceConnectStatusChanged";
	



	String BTSOCKET_SENG_MSG = "btSocketSendMsg";

	String DEVICE_STATUS_CHANGED = "deviceStatusChanged";

	String DELETE_DEVICE = "deleteDevice";


}
