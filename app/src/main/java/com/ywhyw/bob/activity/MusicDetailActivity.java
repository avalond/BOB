package com.ywhyw.bob.activity;

import com.ywhyw.bob.R;
import com.ywhyw.bob.base.BaseActivity;
import com.ywhyw.bob.entity.Music;
import com.ywhyw.bob.util.TextFormatter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;




public class MusicDetailActivity extends BaseActivity implements OnClickListener{
	private int type;
	private static final int MODE_LRC = 100;
	private static final int MODE_NOLRC = 101;
	
	private ImageView ivBgMain;
	
	private ImageView musicBg;
	private LinearLayout llBack;
	private TextView tvName;
	private TextView tvArtist;
	private TextView tvLrcMode;
	private ImageButton ibPlayMode;
	private ImageButton ibMusicList;
	
	private LinearLayout layoutLrc;
	private TextView tvLrc;
	private TextView tvCurTime1;
	private TextView tvTolTime1;
	private ImageButton ibPlay1;
	private ImageButton ibPerious1;
	private ImageButton ibNext1;
	private SeekBar sbProgress1;
	
	private LinearLayout layoutNoLrc;
	private TextView tvCurTime2;
	private TextView tvTolTime2;
	private ImageButton ibPlay2;
	private ImageButton ibPerious2;
	private ImageButton ibNext2;
	private SeekBar sbProgress2;
	
	
	
	private musicReceiver musicReceiver;
	
	private Music curMusic;
	
	private int playMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_detail);
		curMusic = app.getMusicList().get(app.getCurrentMusicIndex());
		playMode = app.getPlayMode();
		type = MODE_LRC;
		
		
		initViews();
		initEvent();
		
		

		
		showLrc();
		showLog(curMusic.toString());
		tvName.setText(curMusic.getTitle());
		tvArtist.setText(curMusic.getArtist());

		String path = curMusic.getImage();
		Bitmap bitmap = null;
		if (path != null && !path.equals("")) {
			bitmap = BitmapFactory.decodeFile(path);
		} else {
			bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
		}

		musicBg.setImageBitmap(bitmap);
		ivBgMain.setImageBitmap(bitmap);
		if(playMode == PLAY_MODE_ORDERED){
			ibPlayMode.setImageResource(R.mipmap.play_mode_circle);
		}else if(playMode == PLAY_MODE_ONLY){
			ibPlayMode.setImageResource(R.mipmap.play_mode_sigal);
		}else{
			ibPlayMode.setImageResource(R.mipmap.play_model_randem);
		}
	}

	@Override
	protected void onResume() {
		// 初始化广播接收者
		initReceiver();
		super.onResume();
	}

	private void showLrc() {
		if(type == MODE_NOLRC){
			type = MODE_LRC;
			tvLrcMode.setText(getResources().getString(R.string.hide_lrc));
			layoutNoLrc.setVisibility(View.GONE);
			layoutLrc.setVisibility(View.VISIBLE);
		}else{
			type = MODE_NOLRC;
			tvLrcMode.setText(getResources().getString(R.string.show_lrc));
			layoutNoLrc.setVisibility(View.VISIBLE);
			layoutLrc.setVisibility(View.GONE);
		}
	}







	private void initViews() {
		ivBgMain = (ImageView) findViewById(R.id.iv_bg_main);
		
		musicBg = (ImageView) findViewById(R.id.iv_bg);
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		tvName = (TextView) findViewById(R.id.tv_title);
		tvArtist = (TextView) findViewById(R.id.tv_artist);
		tvLrcMode = (TextView) findViewById(R.id.tv_lrc_mode);
		ibPlayMode = (ImageButton) findViewById(R.id.ib_play_model);
		ibMusicList = (ImageButton) findViewById(R.id.ib_music_list);
		
		tvLrc = (TextView) findViewById(R.id.tv_lrc);
		
		layoutLrc = (LinearLayout) findViewById(R.id.ll_mode_lrc);
		tvCurTime1 = (TextView) findViewById(R.id.tv_cur_time1);
		tvTolTime1 = (TextView) findViewById(R.id.tv_time1);
		ibPerious1 = (ImageButton) findViewById(R.id.ib_perious1);
		ibPlay1 = (ImageButton) findViewById(R.id.ib_play1);
		ibNext1 = (ImageButton) findViewById(R.id.ib_next1);
		sbProgress1 = (SeekBar) findViewById(R.id.sb_progress1);
		
		layoutNoLrc = (LinearLayout) findViewById(R.id.ll_mode_nolrc);
		tvCurTime2 = (TextView) findViewById(R.id.tv_cur_time2);
		tvTolTime2 = (TextView) findViewById(R.id.tv_time2);
		ibPerious2 = (ImageButton) findViewById(R.id.ib_perious2);
		ibPlay2 = (ImageButton) findViewById(R.id.ib_play2);
		ibNext2 = (ImageButton) findViewById(R.id.ib_next2);
		sbProgress2 = (SeekBar) findViewById(R.id.sb_progress2);
		
	}
	
	private void initEvent() {
		llBack.setOnClickListener(this);
		ibPerious1.setOnClickListener(this);
		ibPerious2.setOnClickListener(this);
		ibPlay1.setOnClickListener(this);
		ibPlay2.setOnClickListener(this);
		ibNext1.setOnClickListener(this);
		ibNext2.setOnClickListener(this);
		tvLrcMode.setOnClickListener(this);
		ibPlayMode.setOnClickListener(this);
		
		tvLrcMode.setOnClickListener(this);
		
		sbProgress1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Intent intent = new Intent(ACTIVITY_SEEKBAR_CHANGED);
				intent.putExtra("progress", seekBar.getProgress());
				sendBroadcast(intent);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}
		});
		sbProgress2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Intent intent = new Intent(ACTIVITY_SEEKBAR_CHANGED);
				intent.putExtra("progress", seekBar.getProgress());
				sendBroadcast(intent);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}
		});
	}
	
	
	/**
	 * 初始化广播接收者
	 */
	private void initReceiver() {
		// 初始化广播接收者
		musicReceiver = new musicReceiver();
		// 初始化广播接收者的意图过滤器
		IntentFilter filter = new IntentFilter();
		// 添加接收的广播类型
		filter.addAction(SERVICE_PLAYER_PLAY);
		filter.addAction(SERVICE_PLAYER_PAUSE);
		filter.addAction(SERVICE_UPDATE_PROGRESS);
		// 注册广播接收者
		registerReceiver(musicReceiver, filter);
	}
	
	
	

	class musicReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			curMusic = app.getMusicList().get(app.getCurrentMusicIndex());
			if (SERVICE_PLAYER_PLAY.equals(action)) {
				// 当播放歌曲时
				ibPlay1.setImageResource(R.mipmap.logo_pause_s); // 切换按钮图片
				ibPlay2.setImageResource(R.mipmap.logo_pause); // 切换按钮图片
				tvName.setText(curMusic.getTitle());
				tvArtist.setText(curMusic.getArtist());

				String path = curMusic.getImage();
				Bitmap bitmap = null;
				if (path != null && !path.equals("")) {
					bitmap = BitmapFactory.decodeFile(path);
				} else {
					bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
				}

				musicBg.setImageBitmap(bitmap);
				ivBgMain.setImageBitmap(bitmap);
			} else if (SERVICE_PLAYER_PAUSE.equals(action)) {
				// 当暂停播放时
				ibPlay1.setImageResource(R.mipmap.logo_play_s);
				ibPlay2.setImageResource(R.mipmap.logo_play);
			} else if (SERVICE_UPDATE_PROGRESS.equals(action)) {
				// 实时更新播放进度
				int currentPosition = intent.getIntExtra("currentPosition", 0);
				String curTime = TextFormatter.getMusicTime(currentPosition);
				String totleTime = TextFormatter.getMusicTime(curMusic.getDuration());
				int progress = (int) (currentPosition * 100 / curMusic.getDuration());
				progress = progress > 100 ? 0 : progress;
				tvCurTime1.setText(curTime);
				tvTolTime1.setText(totleTime);
				sbProgress1.setProgress(progress);
				tvCurTime2.setText(curTime);
				tvTolTime2.setText(totleTime);
				sbProgress2.setProgress(progress);
			}
		}
		
	}


	
	
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.ib_perious1:
		case R.id.ib_perious2:
			showLog("上一曲");
			intent.setAction(ACTIVITY_PREVIOUS_BUTTON_CLICK);
			sendBroadcast(intent);
			break;
		case R.id.ib_play1:
		case R.id.ib_play2:
			showLog("播放/暂停");
			intent.setAction(ACTIVITY_PLAY_BUTTON_CLICK);
			sendBroadcast(intent);
			break;
		case R.id.ib_next1:
		case R.id.ib_next2:
			showLog("下一曲");
			intent.setAction(ACTIVITY_NEXT_BUTTON_CLICK);
			sendBroadcast(intent);
			break;
			
		case R.id.tv_lrc_mode:
			showLrc();
			break;
		case R.id.ib_play_model:
			setPlayModel();
			break;

		default:
			break;
		}
	}




	private void setPlayModel() {
		if(playMode == PLAY_MODE_ORDERED){
			playMode = PLAY_MODE_ONLY;
			ibPlayMode.setImageResource(R.mipmap.play_mode_sigal);
			app.setPlayMode(PLAY_MODE_ONLY);
			showToast(getResources().getString(R.string.play_single));
		}else if(playMode == PLAY_MODE_ONLY){
			playMode = PLAY_MODE_RANDOM;
			ibPlayMode.setImageResource(R.mipmap.play_model_randem);
			app.setPlayMode(PLAY_MODE_RANDOM);
			showToast(getResources().getString(R.string.play_random));
		}else{
			playMode = PLAY_MODE_ORDERED;
			ibPlayMode.setImageResource(R.mipmap.play_mode_circle);
			app.setPlayMode(PLAY_MODE_ORDERED);
			showToast(getResources().getString(R.string.play_ciecle));
		}
	}


	@Override
	protected void onDestroy() {
		unregisterReceiver(musicReceiver);
		super.onDestroy();
	}
}
