package com.ywhyw.bob.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ywhyw.bob.R;
import com.ywhyw.bob.base.BaseFragment;
import com.ywhyw.bob.entity.PTStatus;
import com.ywhyw.bob.entity.PTStatusUtil;
import com.ywhyw.bob.util.TimeUtil;
import com.ywhyw.bob.widget.KnobView;


public class FragPt extends BaseFragment implements OnCheckedChangeListener,OnClickListener{

	private View view;

	private PTStatus ptStatus;
	private PTStatusUtil statusUtil;

	public static final int TYPE_ZHENJIU = 1;
	public static final int TYPE_ANMO = 2;
	public static final int TYPE_LILIAO = 3;
	public static final int TYPE_YUELIAO = 4;
	private int curType;
	private int curOnType = 0;

	private RadioGroup rgBottom;

	private TextView tvType;
	private TextView tvIntensity;
	private ImageButton ibTypeStatus;
	private ImageButton ibPlus;
	private ImageButton ibSubtract;

	private KnobView knobView;
	private TextView tvTime;
	private Button btnClock;
	private int time = 0;

	private ImageButton ibS1;
	private ImageButton ibS2;
	private ImageButton ibS3;
	private ImageButton ibS4;
	private ImageButton ibS5;
	private ImageButton ibS6;
	private ImageButton ibS7;
	private ImageButton ibS8;
	private ImageButton ibS9;
	private ImageButton ibS10;
	private ImageButton ibS11;
	private LinearLayout llS1;
	private LinearLayout llS2;
	private LinearLayout llS3;
	private LinearLayout llS4;
	private LinearLayout llS5;
	private LinearLayout llS6;
	private LinearLayout llS7;
	private LinearLayout llS8;
	private LinearLayout llS9;
	private LinearLayout llS10;
	private LinearLayout llS11;

	private ImageButton ibP1;
	private ImageButton ibP2;
	private ImageButton ibP3;
	private ImageButton ibP4;
	private ImageButton ibP5;
	private ImageButton ibP6;
	private ImageButton ibP7;
	private ImageButton ibP8;
	private ImageButton ibP9;
	private ImageButton ibP10;
	private ImageButton ibP11;
	private LinearLayout llP1;
	private LinearLayout llP2;
	private LinearLayout llP3;
	private LinearLayout llP4;
	private LinearLayout llP5;
	private LinearLayout llP6;
	private LinearLayout llP7;
	private LinearLayout llP8;
	private LinearLayout llP9;
	private LinearLayout llP10;
	private LinearLayout llP11;

	private LinearLayout llFrequency;
	private TextView tvFrequency;
	private ImageButton ibSubtractF;
	private ImageButton ibPlusF;
	private int curFrequency;

	private ZhenjiuThread zhenjiuThread;
	private boolean zhenjiuIsRun = false;
	private AnmoThread anmoThread;
	private boolean anmoIsRun = false;
	private LiliaoThread liliaoThread;
	private boolean liliaoIsRun = false;
	private YueliaoThread yueliaoThread;
	private boolean yueliaoIsRun = false;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_pt, container,false);
		statusUtil = PTStatusUtil.getInstance();
		ptStatus = statusUtil.getPTStatus(mContext);
		initView();
		initEvent();

		//初始化选中针灸单元
		reSetLayout(TYPE_ZHENJIU);

		zhenjiuThread = new ZhenjiuThread();
		anmoThread = new AnmoThread();
		liliaoThread = new LiliaoThread();
		yueliaoThread = new YueliaoThread();


		return view;
	}



	private void initView() {
		rgBottom = (RadioGroup) view.findViewById(R.id.rg_pt);
		tvType = (TextView) view.findViewById(R.id.tv_type);
		tvIntensity = (TextView) view.findViewById(R.id.tv_intensity);
		ibTypeStatus = (ImageButton) view.findViewById(R.id.ib_type_status);
		ibPlus = (ImageButton) view.findViewById(R.id.ib_plus);
		ibSubtract = (ImageButton) view.findViewById(R.id.ib_subtract);

		ibS1 = (ImageButton) view.findViewById(R.id.ib_s1);
		ibS2 = (ImageButton) view.findViewById(R.id.ib_s2);
		ibS3 = (ImageButton) view.findViewById(R.id.ib_s3);
		ibS4 = (ImageButton) view.findViewById(R.id.ib_s4);
		ibS5 = (ImageButton) view.findViewById(R.id.ib_s5);
		ibS6 = (ImageButton) view.findViewById(R.id.ib_s6);
		ibS7 = (ImageButton) view.findViewById(R.id.ib_s7);
		ibS8 = (ImageButton) view.findViewById(R.id.ib_s8);
		ibS9 = (ImageButton) view.findViewById(R.id.ib_s9);
		ibS10 = (ImageButton) view.findViewById(R.id.ib_s10);
		ibS11 = (ImageButton) view.findViewById(R.id.ib_s11);

		llS1 = (LinearLayout) view.findViewById(R.id.ll_s1);
		llS2 = (LinearLayout) view.findViewById(R.id.ll_s2);
		llS3 = (LinearLayout) view.findViewById(R.id.ll_s3);
		llS4 = (LinearLayout) view.findViewById(R.id.ll_s4);
		llS5 = (LinearLayout) view.findViewById(R.id.ll_s5);
		llS6 = (LinearLayout) view.findViewById(R.id.ll_s6);
		llS7 = (LinearLayout) view.findViewById(R.id.ll_s7);
		llS8 = (LinearLayout) view.findViewById(R.id.ll_s8);
		llS9 = (LinearLayout) view.findViewById(R.id.ll_s9);
		llS10 = (LinearLayout) view.findViewById(R.id.ll_s10);
		llS11 = (LinearLayout) view.findViewById(R.id.ll_s11);

		ibP1 = (ImageButton) view.findViewById(R.id.ib_p1);
		ibP2 = (ImageButton) view.findViewById(R.id.ib_p2);
		ibP3 = (ImageButton) view.findViewById(R.id.ib_p3);
		ibP4 = (ImageButton) view.findViewById(R.id.ib_p4);
		ibP5 = (ImageButton) view.findViewById(R.id.ib_p5);
		ibP6 = (ImageButton) view.findViewById(R.id.ib_p6);
		ibP7 = (ImageButton) view.findViewById(R.id.ib_p7);
		ibP8 = (ImageButton) view.findViewById(R.id.ib_p8);
		ibP9 = (ImageButton) view.findViewById(R.id.ib_p9);
		ibP10 = (ImageButton) view.findViewById(R.id.ib_p10);
		ibP11 = (ImageButton) view.findViewById(R.id.ib_p11);

		llP1 = (LinearLayout) view.findViewById(R.id.ll_p1);
		llP2 = (LinearLayout) view.findViewById(R.id.ll_p2);
		llP3 = (LinearLayout) view.findViewById(R.id.ll_p3);
		llP4 = (LinearLayout) view.findViewById(R.id.ll_p4);
		llP5 = (LinearLayout) view.findViewById(R.id.ll_p5);
		llP6 = (LinearLayout) view.findViewById(R.id.ll_p6);
		llP7 = (LinearLayout) view.findViewById(R.id.ll_p7);
		llP8 = (LinearLayout) view.findViewById(R.id.ll_p8);
		llP9 = (LinearLayout) view.findViewById(R.id.ll_p9);
		llP10 = (LinearLayout) view.findViewById(R.id.ll_p10);
		llP11 = (LinearLayout) view.findViewById(R.id.ll_p11);

		llFrequency = (LinearLayout) view.findViewById(R.id.ll_frequency);
		tvFrequency = (TextView) view.findViewById(R.id.tv_frequency);

		ibPlusF = (ImageButton) view.findViewById(R.id.ib_plus_f);
		ibSubtractF = (ImageButton) view.findViewById(R.id.ib_subtract_f);

		btnClock = (Button) view.findViewById(R.id.btn_clock);
		tvTime = (TextView) view.findViewById(R.id.tv_time);
		knobView = (KnobView) view.findViewById(R.id.knobview);
		knobView.setMax(1800);
	}

	private void initEvent() {
		rgBottom.setOnCheckedChangeListener(this);
		ibTypeStatus.setOnClickListener(this);
		ibPlus.setOnClickListener(this);
		ibSubtract.setOnClickListener(this);

		llS1.setOnClickListener(this);
		llS2.setOnClickListener(this);
		llS3.setOnClickListener(this);
		llS4.setOnClickListener(this);
		llS5.setOnClickListener(this);
		llS6.setOnClickListener(this);
		llS7.setOnClickListener(this);
		llS8.setOnClickListener(this);
		llS9.setOnClickListener(this);
		llS10.setOnClickListener(this);
		llS11.setOnClickListener(this);

		llP1.setOnClickListener(this);
		llP2.setOnClickListener(this);
		llP3.setOnClickListener(this);
		llP4.setOnClickListener(this);
		llP5.setOnClickListener(this);
		llP6.setOnClickListener(this);
		llP7.setOnClickListener(this);
		llP8.setOnClickListener(this);
		llP9.setOnClickListener(this);
		llP10.setOnClickListener(this);
		llP11.setOnClickListener(this);

		btnClock.setOnClickListener(this);

		knobView.setProgressChangeListener(new KnobView.OnProgressChangeListener() {
			@Override
			public void onProgressChanged(boolean fromUser, int progress) {
				time = progress;
				tvTime.setText(TimeUtil.getStrFromInt(time));
				switch (curType){
					case TYPE_ZHENJIU:
						ptStatus.setStatusZhenJiuClockTime(time);
						break;
					case TYPE_ANMO:
						ptStatus.setStatusAnMoClockTime(time);
						break;
					case TYPE_LILIAO:
						ptStatus.setStatusLiLiaoClockTime(time);
						break;
					case TYPE_YUELIAO:
						ptStatus.setStatusYueLiaoClockTime(time);
						break;
				}
				statusUtil.setPTStatus(mContext,ptStatus);
			}
			@Override
			public void onStartTrackingTouch(KnobView view, int progress) {
			}
			@Override
			public void onStopTrackingTouch(KnobView view, int progress) {

			}
		});

		ibPlusF.setOnClickListener(this);
		ibSubtractF.setOnClickListener(this);
	}

	/**
	 * 根据当前type刷新界面
	 * 强度
	 * 状态按钮
	 * 加减按钮
	 * @param type
	 */
	private void reSetLayout(int type){
		curType = type;
		reSetIntensity();
		switch (type) {
			case TYPE_ZHENJIU:
//				showLog("当前状态：针灸 " + (ptStatus.isStatusZhenJiuIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusZhenJiuIntensity()):"关闭"));
//				showToast("当前状态：针灸 " + (ptStatus.isStatusZhenJiuIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusZhenJiuIntensity()):"关闭"));
				tvType.setText(getString(R.string.unit_zhenjiu));
				tvIntensity.setText(ptStatus.getStatusZhenJiuIntensity()+"");
				llFrequency.setVisibility(View.GONE);
				if(ptStatus.isStatusZhenJiuIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					ibPlus.setImageResource(R.mipmap.btn_jia_checked);
					ibSubtract.setImageResource(R.mipmap.btn_jian_checked);
					knobView.setTouchable(true);
					btnClock.setClickable(true);
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ibPlus.setImageResource(R.mipmap.btn_jia_nomal);
					ibSubtract.setImageResource(R.mipmap.btn_jian_nomal);
					knobView.setTouchable(false);
					btnClock.setClickable(false);
				}
				time = ptStatus.getStatusZhenJiuClockTime();
				tvTime.setText(TimeUtil.getStrFromInt(time));
				knobView.setProgress(time);
				if(ptStatus.isStatusZhenJiuIsClock()){
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
				}else{
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
				}
				switch (ptStatus.getStatusZhenJiuIntensity()) {
					case 0:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS1.setImageResource(R.mipmap.y1);
						}else{
							ibS1.setImageResource(R.mipmap.l1);
						}
						break;
					case 10:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS2.setImageResource(R.mipmap.y2);
						}else{
							ibS2.setImageResource(R.mipmap.l2);
						}
						break;
					case 20:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS3.setImageResource(R.mipmap.y3);
						}else{
							ibS3.setImageResource(R.mipmap.l3);
						}
						break;
					case 30:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS4.setImageResource(R.mipmap.y4);
						}else{
							ibS4.setImageResource(R.mipmap.l4);
						}
						break;
					case 40:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS5.setImageResource(R.mipmap.y5);
						}else{
							ibS5.setImageResource(R.mipmap.l5);
						}
						break;
					case 50:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS6.setImageResource(R.mipmap.y6);
						}else{
							ibS6.setImageResource(R.mipmap.l6);
						}
						break;
					case 60:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS7.setImageResource(R.mipmap.y7);
						}else{
							ibS7.setImageResource(R.mipmap.l7);
						}
						break;
					case 70:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS8.setImageResource(R.mipmap.y8);
						}else{
							ibS8.setImageResource(R.mipmap.l8);
						}
						break;
					case 80:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS9.setImageResource(R.mipmap.y9);
						}else{
							ibS9.setImageResource(R.mipmap.l9);
						}
						break;
					case 90:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS10.setImageResource(R.mipmap.y10);
						}else{
							ibS10.setImageResource(R.mipmap.l10);
						}
						break;
					case 100:
						if(ptStatus.isStatusZhenJiuIsOpen()){
							ibS11.setImageResource(R.mipmap.y11);
						}else{
							ibS11.setImageResource(R.mipmap.l11);
						}
						break;

					default:
						break;
				}
				break;
			case TYPE_ANMO:
//				showToast("当前状态：按摩" + (ptStatus.isStatusAnMoIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusAnMoIntensity()):"关闭"));
//				showLog("当前状态：按摩" + (ptStatus.isStatusAnMoIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusAnMoIntensity()):"关闭"));
				tvType.setText(getString(R.string.unit_anmo));
				tvIntensity.setText(ptStatus.getStatusAnMoIntensity()+"");
				llFrequency.setVisibility(View.GONE);
				if(ptStatus.isStatusAnMoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					ibPlus.setImageResource(R.mipmap.btn_jia_checked);
					ibSubtract.setImageResource(R.mipmap.btn_jian_checked);
					knobView.setTouchable(true);
					btnClock.setClickable(true);
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ibPlus.setImageResource(R.mipmap.btn_jia_nomal);
					ibSubtract.setImageResource(R.mipmap.btn_jian_nomal);
					knobView.setTouchable(false);
					btnClock.setClickable(false);
				}
				time = ptStatus.getStatusAnMoClockTime();
				tvTime.setText(TimeUtil.getStrFromInt(time));
				knobView.setProgress(time);
				if(ptStatus.isStatusAnMoIsClock()){
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
				}else{
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
				}
				switch (ptStatus.getStatusAnMoIntensity()) {
					case 0:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS1.setImageResource(R.mipmap.y1);
						}else{
							ibS1.setImageResource(R.mipmap.l1);
						}
						break;
					case 10:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS2.setImageResource(R.mipmap.y2);
						}else{
							ibS2.setImageResource(R.mipmap.l2);
						}
						break;
					case 20:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS3.setImageResource(R.mipmap.y3);
						}else{
							ibS3.setImageResource(R.mipmap.l3);
						}
						break;
					case 30:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS4.setImageResource(R.mipmap.y4);
						}else{
							ibS4.setImageResource(R.mipmap.l4);
						}
						break;
					case 40:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS5.setImageResource(R.mipmap.y5);
						}else{
							ibS5.setImageResource(R.mipmap.l5);
						}
						break;
					case 50:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS6.setImageResource(R.mipmap.y6);
						}else{
							ibS6.setImageResource(R.mipmap.l6);
						}
						break;
					case 60:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS7.setImageResource(R.mipmap.y7);
						}else{
							ibS7.setImageResource(R.mipmap.l7);
						}
						break;
					case 70:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS8.setImageResource(R.mipmap.y8);
						}else{
							ibS8.setImageResource(R.mipmap.l8);
						}
						break;
					case 80:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS9.setImageResource(R.mipmap.y9);
						}else{
							ibS9.setImageResource(R.mipmap.l9);
						}
						break;
					case 90:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS10.setImageResource(R.mipmap.y10);
						}else{
							ibS10.setImageResource(R.mipmap.l10);
						}
						break;
					case 100:
						if(ptStatus.isStatusAnMoIsOpen()){
							ibS11.setImageResource(R.mipmap.y11);
						}else{
							ibS11.setImageResource(R.mipmap.l11);
						}
						break;

					default:
						break;
				}
				break;
			case TYPE_LILIAO:
//				showToast("当前状态：理疗" + (ptStatus.isStatusLiLiaoIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusLiLiaoIntensity()):"关闭"));
//				showLog("当前状态：理疗" + (ptStatus.isStatusLiLiaoIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusLiLiaoIntensity()):"关闭"));
				tvType.setText(getString(R.string.unit_liliao));
				tvIntensity.setText(ptStatus.getStatusLiLiaoIntensity()+"");
				llFrequency.setVisibility(View.GONE);
				if(ptStatus.isStatusLiLiaoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					ibPlus.setImageResource(R.mipmap.btn_jia_checked);
					ibSubtract.setImageResource(R.mipmap.btn_jian_checked);
					knobView.setTouchable(true);
					btnClock.setClickable(true);
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ibPlus.setImageResource(R.mipmap.btn_jia_nomal);
					ibSubtract.setImageResource(R.mipmap.btn_jian_nomal);
					knobView.setTouchable(false);
					btnClock.setClickable(false);
				}
				time = ptStatus.getStatusLiLiaoClockTime();
				tvTime.setText(TimeUtil.getStrFromInt(time));
				knobView.setProgress(time);
				if(ptStatus.isStatusLiLiaoIsClock()){
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
				}else{
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
				}
				switch (ptStatus.getStatusLiLiaoIntensity()) {
					case 0:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS1.setImageResource(R.mipmap.y1);
						}else{
							ibS1.setImageResource(R.mipmap.l1);
						}
						break;
					case 10:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS2.setImageResource(R.mipmap.y2);
						}else{
							ibS2.setImageResource(R.mipmap.l2);
						}
						break;
					case 20:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS3.setImageResource(R.mipmap.y3);
						}else{
							ibS3.setImageResource(R.mipmap.l3);
						}
						break;
					case 30:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS4.setImageResource(R.mipmap.y4);
						}else{
							ibS4.setImageResource(R.mipmap.l4);
						}
						break;
					case 40:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS5.setImageResource(R.mipmap.y5);
						}else{
							ibS5.setImageResource(R.mipmap.l5);
						}
						break;
					case 50:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS6.setImageResource(R.mipmap.y6);
						}else{
							ibS6.setImageResource(R.mipmap.l6);
						}
						break;
					case 60:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS7.setImageResource(R.mipmap.y7);
						}else{
							ibS7.setImageResource(R.mipmap.l7);
						}
						break;
					case 70:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS8.setImageResource(R.mipmap.y8);
						}else{
							ibS8.setImageResource(R.mipmap.l8);
						}
						break;
					case 80:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS9.setImageResource(R.mipmap.y9);
						}else{
							ibS9.setImageResource(R.mipmap.l9);
						}
						break;
					case 90:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS10.setImageResource(R.mipmap.y10);
						}else{
							ibS10.setImageResource(R.mipmap.l10);
						}
						break;
					case 100:
						if(ptStatus.isStatusLiLiaoIsOpen()){
							ibS11.setImageResource(R.mipmap.y11);
						}else{
							ibS11.setImageResource(R.mipmap.l11);
						}
						break;

					default:
						break;
				}
				break;
			case TYPE_YUELIAO:
//				showToast("当前状态：乐疗" + (ptStatus.isStatusYueLiaoIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusYueLiaoIntensity() + "频率为：" + ptStatus.getStatusYueLiaoFrequency()):"关闭"));
//				showLog("当前状态：乐疗" + (ptStatus.isStatusYueLiaoIsOpen() ?
//						("打开 强度为：" + ptStatus.getStatusYueLiaoIntensity() + "频率为：" + ptStatus.getStatusYueLiaoFrequency()):"关闭"));
				tvType.setText(getString(R.string.unit_yueliao));
				tvIntensity.setText(ptStatus.getStatusYueLiaoIntensity()+"");
				llFrequency.setVisibility(View.VISIBLE);
				curFrequency = ptStatus.getStatusYueLiaoFrequency();
				tvFrequency.setText(curFrequency+"HZ");
				if(ptStatus.isStatusYueLiaoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_on);

					ibPlus.setImageResource(R.mipmap.btn_jia_checked);
					ibSubtract.setImageResource(R.mipmap.btn_jian_checked);

					ibPlusF.setImageResource(R.mipmap.btn_jia_checked);
					ibSubtractF.setImageResource(R.mipmap.btn_jian_checked);

					knobView.setTouchable(true);
					btnClock.setClickable(true);
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_off);

					ibPlus.setImageResource(R.mipmap.btn_jia_nomal);
					ibSubtract.setImageResource(R.mipmap.btn_jian_nomal);

					ibPlusF.setImageResource(R.mipmap.btn_jia_nomal);
					ibSubtractF.setImageResource(R.mipmap.btn_jian_nomal);

					knobView.setTouchable(false);
					btnClock.setClickable(false);
				}
				time = ptStatus.getStatusYueLiaoClockTime();
				tvTime.setText(TimeUtil.getStrFromInt(time));
				knobView.setProgress(time);
				if(ptStatus.isStatusYueLiaoIsClock()){
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
				}else{
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
				}
				//判断强度并高亮当前频率
				switch (ptStatus.getStatusYueLiaoIntensity()) {
					case 0:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS1.setImageResource(R.mipmap.y1);
						}else{
							ibS1.setImageResource(R.mipmap.l1);
						}
						break;
					case 10:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS2.setImageResource(R.mipmap.y2);
						}else{
							ibS2.setImageResource(R.mipmap.l2);
						}
						break;
					case 20:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS3.setImageResource(R.mipmap.y3);
						}else{
							ibS3.setImageResource(R.mipmap.l3);
						}
						break;
					case 30:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS4.setImageResource(R.mipmap.y4);
						}else{
							ibS4.setImageResource(R.mipmap.l4);
						}
						break;
					case 40:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS5.setImageResource(R.mipmap.y5);
						}else{
							ibS5.setImageResource(R.mipmap.l5);
						}
						break;
					case 50:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS6.setImageResource(R.mipmap.y6);
						}else{
							ibS6.setImageResource(R.mipmap.l6);
						}
						break;
					case 60:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS7.setImageResource(R.mipmap.y7);
						}else{
							ibS7.setImageResource(R.mipmap.l7);
						}
						break;
					case 70:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS8.setImageResource(R.mipmap.y8);
						}else{
							ibS8.setImageResource(R.mipmap.l8);
						}
						break;
					case 80:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS9.setImageResource(R.mipmap.y9);
						}else{
							ibS9.setImageResource(R.mipmap.l9);
						}
						break;
					case 90:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS10.setImageResource(R.mipmap.y10);
						}else{
							ibS10.setImageResource(R.mipmap.l10);
						}
						break;
					case 100:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibS11.setImageResource(R.mipmap.y11);
						}else{
							ibS11.setImageResource(R.mipmap.l11);
						}
						break;

					default:
						break;
				}
				//判断频率并高亮当前频率
				switch (ptStatus.getStatusYueLiaoFrequency()) {
					case 0:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibP1.setImageResource(R.mipmap.y1);
						}else{
							ibP1.setImageResource(R.mipmap.l1);
						}
						break;
					case 100:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibP2.setImageResource(R.mipmap.y2);
						}else{
							ibP2.setImageResource(R.mipmap.l2);
						}
						break;
					case 200:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibP3.setImageResource(R.mipmap.y3);
						}else{
							ibP3.setImageResource(R.mipmap.l3);
						}
						break;
					case 300:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibP4.setImageResource(R.mipmap.y4);
						}else{
							ibP4.setImageResource(R.mipmap.l4);
						}
						break;
					case 400:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibP5.setImageResource(R.mipmap.y5);
						}else{
							ibP5.setImageResource(R.mipmap.l5);
						}
						break;
					case 500:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibP6.setImageResource(R.mipmap.y6);
						}else{
							ibP6.setImageResource(R.mipmap.l6);
						}
						break;
					case 600:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibP7.setImageResource(R.mipmap.y7);
						}else{
							ibP7.setImageResource(R.mipmap.l7);
						}
						break;
					case 700:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibP8.setImageResource(R.mipmap.y8);
						}else{
							ibP8.setImageResource(R.mipmap.l8);
						}
						break;
					case 800:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibP9.setImageResource(R.mipmap.y9);
						}else{
							ibP9.setImageResource(R.mipmap.l9);
						}
						break;
					case 900:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibP10.setImageResource(R.mipmap.y10);
						}else{
							ibP10.setImageResource(R.mipmap.l10);
						}
						break;
					case 1000:
						if(ptStatus.isStatusYueLiaoIsOpen()){
							ibP11.setImageResource(R.mipmap.y11);
						}else{
							ibP11.setImageResource(R.mipmap.l11);
						}
						break;

					default:
						break;
				}
				break;

			default:
				break;
		}
	}

	/**
	 * 重置强度等级背景图片
	 */
	private void reSetIntensity() {
		ibS1.setImageResource(R.mipmap.g1);
		ibS2.setImageResource(R.mipmap.g2);
		ibS3.setImageResource(R.mipmap.g3);
		ibS4.setImageResource(R.mipmap.g4);
		ibS5.setImageResource(R.mipmap.g5);
		ibS6.setImageResource(R.mipmap.g6);
		ibS7.setImageResource(R.mipmap.g7);
		ibS8.setImageResource(R.mipmap.g8);
		ibS9.setImageResource(R.mipmap.g9);
		ibS10.setImageResource(R.mipmap.g10);
		ibS11.setImageResource(R.mipmap.g11);

		ibP1.setImageResource(R.mipmap.g1);
		ibP2.setImageResource(R.mipmap.g2);
		ibP3.setImageResource(R.mipmap.g3);
		ibP4.setImageResource(R.mipmap.g4);
		ibP5.setImageResource(R.mipmap.g5);
		ibP6.setImageResource(R.mipmap.g6);
		ibP7.setImageResource(R.mipmap.g7);
		ibP8.setImageResource(R.mipmap.g8);
		ibP9.setImageResource(R.mipmap.g9);
		ibP10.setImageResource(R.mipmap.g10);
		ibP11.setImageResource(R.mipmap.g11);
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.rb_zhenjiu:
				reSetLayout(TYPE_ZHENJIU);
				break;
			case R.id.rb_anmo:
				reSetLayout(TYPE_ANMO);
				break;
			case R.id.rb_liliao:
				reSetLayout(TYPE_LILIAO);
				break;
			case R.id.rb_yueliao:
				reSetLayout(TYPE_YUELIAO);
				break;

			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ib_type_status://设置当前单元开关状态
				setTypeButton();
				break;
			case R.id.ib_plus://增加强度

				plusIntensity();
				break;
			case R.id.ib_subtract://减少强度

				subtratIntensity();
				break;

			case R.id.ib_plus_f:

				plusHz();
				break;

			case R.id.ib_subtract_f:

				subtratHz();
				break;

			case R.id.ll_s1:
				setIntensity(0);
				break;
			case R.id.ll_s2:
				setIntensity(10);
				break;
			case R.id.ll_s3:
				setIntensity(20);
				break;
			case R.id.ll_s4:
				setIntensity(30);
				break;
			case R.id.ll_s5:
				setIntensity(40);
				break;
			case R.id.ll_s6:
				setIntensity(50);
				break;
			case R.id.ll_s7:
				setIntensity(60);
				break;
			case R.id.ll_s8:
				setIntensity(70);
				break;
			case R.id.ll_s9:
				setIntensity(80);
				break;
			case R.id.ll_s10:
				setIntensity(90);
				break;
			case R.id.ll_s11:
				setIntensity(100);
				break;

			case R.id.ll_p1:
				setHz(0);
				break;
			case R.id.ll_p2:
				setHz(100);
				break;
			case R.id.ll_p3:
				setHz(200);
				break;
			case R.id.ll_p4:
				setHz(300);
				break;
			case R.id.ll_p5:
				setHz(400);
				break;
			case R.id.ll_p6:
				setHz(500);
				break;
			case R.id.ll_p7:
				setHz(600);
				break;
			case R.id.ll_p8:
				setHz(700);
				break;
			case R.id.ll_p9:
				setHz(800);
				break;
			case R.id.ll_p10:
				setHz(900);
				break;
			case R.id.ll_p11:
				setHz(1000);
				break;



			case R.id.btn_clock:
				setClock();
				break;




			default:
				break;
		}
	}

	/**
	 * 设置定时器
	 */
	private void setClock() {
		switch (curType){
			case TYPE_ZHENJIU:
				if (ptStatus.isStatusZhenJiuIsClock()){
					showToast("关闭定时");
					ptStatus.setStatusZhenJiuIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!zhenjiuThread.isInterrupted()){
						zhenjiuIsRun = false;
					}
				}else{
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					ptStatus.setStatusZhenJiuIsClock(true);
					ptStatus.setStatusZhenJiuClockTime(time);
					showToast("开始定时，针灸" + TimeUtil.getStrFromInt(time) + "后关闭");
					zhenjiuIsRun = true;
					zhenjiuThread = new ZhenjiuThread();
					zhenjiuThread.start();
				}
				break;
			case TYPE_ANMO:
				if (ptStatus.isStatusAnMoIsClock()){
					showToast("关闭定时");
					ptStatus.setStatusAnMoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!anmoThread.isInterrupted()){
						anmoIsRun = false;
					}
				}else{
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					ptStatus.setStatusAnMoIsClock(true);
					ptStatus.setStatusAnMoClockTime(time);
					showToast("开始定时，按摩" +  TimeUtil.getStrFromInt(time) + "后关闭");
					anmoIsRun = true;
					anmoThread = new AnmoThread();
					anmoThread.start();
				}
				break;
			case TYPE_LILIAO:
				if (ptStatus.isStatusLiLiaoIsClock()){
					showToast("关闭定时");
					ptStatus.setStatusLiLiaoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!liliaoThread.isInterrupted()){
						liliaoIsRun = false;
					}
				}else{
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					ptStatus.setStatusLiLiaoIsClock(true);
					ptStatus.setStatusLiLiaoClockTime(time);
					showToast("开始定时，理疗" +  TimeUtil.getStrFromInt(time) + "后关闭");
					liliaoIsRun = true;
					liliaoThread = new LiliaoThread();
					liliaoThread.start();
				}
				break;
			case TYPE_YUELIAO:
				if (ptStatus.isStatusYueLiaoIsClock()){
					showToast("关闭定时");
					ptStatus.setStatusYueLiaoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!yueliaoThread.isInterrupted()){
						yueliaoIsRun = false;
					}
				}else{
					btnClock.setText(getString(R.string.btn_close));
					btnClock.setBackgroundResource(R.drawable.btn_orange_shape);
					ptStatus.setStatusYueLiaoIsClock(true);
					ptStatus.setStatusYueLiaoClockTime(time);
					showToast("开始定时，乐疗" +  TimeUtil.getStrFromInt(time) + "后关闭");
					yueliaoIsRun = true;
					yueliaoThread = new YueliaoThread();
					yueliaoThread.start();
				}
				break;
		}
		statusUtil.setPTStatus(mContext,ptStatus);
//		reSetLayout(curType);
		return;
	}

	/**
	 * 设置强度
	 * @param intensity
	 */
	private void setIntensity(int intensity){
		switch (curType) {
			case TYPE_ZHENJIU:
				if(ptStatus.isStatusZhenJiuIsOpen()){
					ptStatus.setStatusZhenJiuIntensity(intensity);
					statusUtil.setPTStatus(mContext, ptStatus);
					sengMsgToDevice("设置当前强度为" + intensity + "    ");
					reSetLayout(TYPE_ZHENJIU);
				}
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsOpen()){
					ptStatus.setStatusAnMoIntensity(intensity);
					statusUtil.setPTStatus(mContext, ptStatus);
					sengMsgToDevice("设置当前强度为" + intensity + "    ");
					reSetLayout(TYPE_ANMO);
				}
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsOpen()){
					ptStatus.setStatusLiLiaoIntensity(intensity);
					statusUtil.setPTStatus(mContext, ptStatus);
					sengMsgToDevice("设置当前强度为" + intensity + "    ");
					reSetLayout(TYPE_LILIAO);
				}
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsOpen()){
					ptStatus.setStatusYueLiaoIntensity(intensity);
					statusUtil.setPTStatus(mContext, ptStatus);
					sengMsgToDevice("设置当前强度为" + intensity + "    ");
					reSetLayout(TYPE_YUELIAO);
				}
				break;

			default:
				break;
		}
	}

	/**
	 * 设置频率
	 * @param hz
	 */
	private void setHz(int hz){
		if(ptStatus.isStatusYueLiaoIsOpen()){
			ptStatus.setStatusYueLiaoFrequency(hz);
			statusUtil.setPTStatus(mContext, ptStatus);
			sengMsgToDevice("设置当前频率为" + hz + "    ");
			reSetLayout(TYPE_YUELIAO);
		}
	}

	/**
	 * 减少强度并刷新页面
	 */
	private void subtratIntensity() {
		int i ;
		switch (curType) {
			case TYPE_ZHENJIU:
				if(ptStatus.isStatusZhenJiuIsOpen()){
					i = ptStatus.getStatusZhenJiuIntensity();
					if(i == 0){
						showToast(mContext.getResources().getString(R.string.intensity_min));
					}else{
						i -=10;
						ptStatus.setStatusZhenJiuIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
						sengMsgToDevice("减少强度到" + i + "    ");
					}
					reSetLayout(TYPE_ZHENJIU);
				}
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsOpen()){
					i = ptStatus.getStatusAnMoIntensity();
					if(i == 0){
						showToast(mContext.getResources().getString(R.string.intensity_min));
					}else{
						i -=10;
						ptStatus.setStatusAnMoIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
						sengMsgToDevice("减少强度到" + i + "    ");
					}
					reSetLayout(TYPE_ANMO);
				}
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsOpen()){
					i = ptStatus.getStatusLiLiaoIntensity();
					if(i == 0){
						showToast(mContext.getResources().getString(R.string.intensity_min));
					}else{
						i -=10;
						ptStatus.setStatusLiLiaoIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
						sengMsgToDevice("减少强度到" + i + "    ");
					}
					reSetLayout(TYPE_LILIAO);
				}
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsOpen()){
					i = ptStatus.getStatusYueLiaoIntensity();
					if(i == 0){
						showToast(mContext.getResources().getString(R.string.intensity_min));
					}else{
						i -=10;
						ptStatus.setStatusYueLiaoIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
						sengMsgToDevice("减少强度到" + i + "    ");
					}
					reSetLayout(TYPE_YUELIAO);
				}
				break;

			default:
				break;
		}
	}

	/**
	 * 增加强度并刷新页面
	 */
	private void plusIntensity() {
		int i ;
		switch (curType) {
			case TYPE_ZHENJIU:
				if(ptStatus.isStatusZhenJiuIsOpen()){


					i = ptStatus.getStatusZhenJiuIntensity();
					if(i == 100){
						showToast(mContext.getResources().getString(R.string.intensity_max));
					}else{
						i +=10;
						ptStatus.setStatusZhenJiuIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
						sengMsgToDevice("增加强度到" + i + "    ");
					}
					reSetLayout(TYPE_ZHENJIU);
				}
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsOpen()){
					i = ptStatus.getStatusAnMoIntensity();
					if(i == 100){
						showToast(mContext.getResources().getString(R.string.intensity_max));
					}else{
						i +=10;
						ptStatus.setStatusAnMoIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
						sengMsgToDevice("增加强度到" + i + "    ");
					}
					reSetLayout(TYPE_ANMO);
				}
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsOpen()){
					i = ptStatus.getStatusLiLiaoIntensity();
					if(i == 100){
						showToast(mContext.getResources().getString(R.string.intensity_max));
					}else{
						i +=10;
						ptStatus.setStatusLiLiaoIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
						sengMsgToDevice("增加强度到" + i + "    ");
					}
					reSetLayout(TYPE_LILIAO);
				}
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsOpen()){
					i = ptStatus.getStatusYueLiaoIntensity();
					if(i == 100){
						showToast(mContext.getResources().getString(R.string.intensity_max));
					}else{
						i +=10;
						ptStatus.setStatusYueLiaoIntensity(i);
						statusUtil.setPTStatus(mContext, ptStatus);
						sengMsgToDevice("增加强度到" + i + "    ");
					}
					reSetLayout(TYPE_YUELIAO);
				}
				break;

			default:
				break;
		}
	}

	/**
	 * 增加频率并刷新页面
	 */
	private void plusHz() {
		int i ;
		if(ptStatus.isStatusYueLiaoIsOpen()){
			i = ptStatus.getStatusYueLiaoFrequency();
			if(i == 1000){
				showToast(mContext.getResources().getString(R.string.hz_max));
			}else{
				i +=100;
				ptStatus.setStatusYueLiaoFrequency(i);
				statusUtil.setPTStatus(mContext, ptStatus);
				sengMsgToDevice("增加频率到" + i + "    ");
			}
			reSetLayout(TYPE_YUELIAO);
		}

	}

	/**
	 * 减少频率并刷新页面
	 */
	private void subtratHz() {
		int i ;
		if(ptStatus.isStatusYueLiaoIsOpen()){
			i = ptStatus.getStatusYueLiaoFrequency();
			if(i == 0){
				showToast(mContext.getResources().getString(R.string.hz_min));
			}else{
				i -=100;
				ptStatus.setStatusYueLiaoFrequency(i);
				statusUtil.setPTStatus(mContext, ptStatus);
				sengMsgToDevice("减少频率到" + i + "    ");
			}
			reSetLayout(TYPE_YUELIAO);
		}

	}


	/**
	 * 根据type类型设置开关状态
	 */
	private void setTypeButton() {
		switch (curType) {
			case TYPE_ZHENJIU:
				if(ptStatus.isStatusZhenJiuIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_off);

					//关闭当前单元及定时
					ptStatus.setStatusZhenJiuIsOpen(false);
					ptStatus.setStatusZhenJiuIsClock(false);

					btnClock.setText(getString(R.string.btn_open));
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					//发送指令关闭当前单元
					sengMsgToDevice("关闭当前模式    ");
					//关闭当前定时线程
					if (!zhenjiuThread.isInterrupted()){
						zhenjiuIsRun = false;
					}
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					//选中单元非当前单元时，关闭并切换为当前单元
					if (curOnType != curType){
						sengMsgToDevice("关闭当前模式    ");
						sengMsgToDevice("切换模式为针灸    ");
						curOnType = curType;
					}
					sengMsgToDevice("打开当前模式    ");
					//设置其他单元状态为未打开
					ptStatus.setStatusZhenJiuIsOpen(true);
					ptStatus.setStatusAnMoIsOpen(false);
					ptStatus.setStatusLiLiaoIsOpen(false);
					ptStatus.setStatusYueLiaoIsOpen(false);
					//设置其他单元定时状态为关闭
					ptStatus.setStatusAnMoIsClock(false);
					ptStatus.setStatusLiLiaoIsClock(false);
					ptStatus.setStatusYueLiaoIsClock(false);
					//关闭其他单元定时器
					if (!anmoThread.isInterrupted()){
						anmoIsRun = false;
					}
					if (!liliaoThread.isInterrupted()){
						liliaoIsRun = false;
					}
					if (!yueliaoThread.isInterrupted()){
						yueliaoIsRun = false;
					}
				}
				reSetLayout(TYPE_ZHENJIU);
				statusUtil.setPTStatus(mContext, ptStatus);
				break;
			case TYPE_ANMO:
				if(ptStatus.isStatusAnMoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ptStatus.setStatusAnMoIsOpen(false);
					ptStatus.setStatusAnMoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					sengMsgToDevice("关闭当前模式    ");
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!anmoThread.isInterrupted()){
						anmoIsRun = false;
					}
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					if (curOnType != curType){
						sengMsgToDevice("关闭当前模式    ");
						sengMsgToDevice("切换模式为按摩    ");
						curOnType = curType;
					}
					sengMsgToDevice("打开当前模式    ");
					ptStatus.setStatusZhenJiuIsOpen(false);
					ptStatus.setStatusAnMoIsOpen(true);
					ptStatus.setStatusLiLiaoIsOpen(false);
					ptStatus.setStatusYueLiaoIsOpen(false);
					//设置其他单元定时状态为关闭
					ptStatus.setStatusZhenJiuIsClock(false);
					ptStatus.setStatusLiLiaoIsClock(false);
					ptStatus.setStatusYueLiaoIsClock(false);
					//关闭其他单元定时器
					if (!zhenjiuThread.isInterrupted()){
						zhenjiuIsRun = false;
					}
					if (!liliaoThread.isInterrupted()){
						liliaoIsRun = false;
					}
					if (!yueliaoThread.isInterrupted()){
						yueliaoIsRun = false;
					}
				}
				reSetLayout(TYPE_ANMO);
				statusUtil.setPTStatus(mContext, ptStatus);
				break;
			case TYPE_LILIAO:
				if(ptStatus.isStatusLiLiaoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ptStatus.setStatusLiLiaoIsOpen(false);
					ptStatus.setStatusLiLiaoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					sengMsgToDevice("关闭当前模式    ");
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!liliaoThread.isInterrupted()){
						liliaoIsRun = false;
					}
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					if (curOnType != curType){
						sengMsgToDevice("关闭当前模式    ");
						sengMsgToDevice("切换模式为理疗    ");
						curOnType = curType;
					}
					sengMsgToDevice("打开当前模式    ");
					ptStatus.setStatusZhenJiuIsOpen(false);
					ptStatus.setStatusAnMoIsOpen(false);
					ptStatus.setStatusLiLiaoIsOpen(true);
					ptStatus.setStatusYueLiaoIsOpen(false);
					//设置其他单元定时状态为关闭
					ptStatus.setStatusZhenJiuIsClock(false);
					ptStatus.setStatusAnMoIsClock(false);
					ptStatus.setStatusYueLiaoIsClock(false);
					//关闭其他单元定时器
					if (!anmoThread.isInterrupted()){
						anmoIsRun = false;
					}
					if (!zhenjiuThread.isInterrupted()){
						zhenjiuIsRun = false;
					}
					if (!yueliaoThread.isInterrupted()){
						yueliaoIsRun = false;
					}
				}
				reSetLayout(TYPE_LILIAO);
				statusUtil.setPTStatus(mContext, ptStatus);
				break;
			case TYPE_YUELIAO:
				if(ptStatus.isStatusYueLiaoIsOpen()){
					ibTypeStatus.setImageResource(R.mipmap.tb_off);
					ptStatus.setStatusYueLiaoIsOpen(false);
					ptStatus.setStatusYueLiaoIsClock(false);
					btnClock.setText(getString(R.string.btn_open));
					sengMsgToDevice("关闭当前模式    ");
					btnClock.setBackgroundResource(R.drawable.btn_gray_shape);
					if (!yueliaoThread.isInterrupted()){
						yueliaoIsRun = false;
					}
				}else{
					ibTypeStatus.setImageResource(R.mipmap.tb_on);
					if (curOnType != curType){
						sengMsgToDevice("关闭当前模式    ");
						sengMsgToDevice("切换模式为乐疗    ");
						curOnType = curType;
					}
					sengMsgToDevice("打开当前模式    ");
					ptStatus.setStatusZhenJiuIsOpen(false);
					ptStatus.setStatusAnMoIsOpen(false);
					ptStatus.setStatusLiLiaoIsOpen(false);
					ptStatus.setStatusYueLiaoIsOpen(true);
					//设置其他单元定时状态为关闭
					ptStatus.setStatusZhenJiuIsClock(false);
					ptStatus.setStatusAnMoIsClock(false);
					ptStatus.setStatusLiLiaoIsClock(false);
					//关闭其他单元定时器
					if (!anmoThread.isInterrupted()){
						anmoIsRun = false;
					}
					if (!liliaoThread.isInterrupted()){
						liliaoIsRun = false;
					}
					if (!zhenjiuThread.isInterrupted()){
						zhenjiuIsRun = false;
					}
				}
				reSetLayout(TYPE_YUELIAO);
				statusUtil.setPTStatus(mContext, ptStatus);
				break;

			default:
				break;
		}
	}




	public class ZhenjiuThread extends Thread{
		@Override
		public void run() {
			while (zhenjiuIsRun){
				try {
					sleep(1000);
					Message message = new Message();
					message.what = 1;
					zhenjiuHandler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private Handler zhenjiuHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int t = ptStatus.getStatusZhenJiuClockTime();
			if(t - 1 <= 0){
				ptStatus.setStatusZhenJiuClockTime(0);
				if (!zhenjiuThread.isInterrupted()){
					zhenjiuIsRun = false;
				}
				ptStatus.setStatusZhenJiuIsClock(false);
				ptStatus.setStatusZhenJiuIsOpen(false);
				showToast("计时结束，停止针灸");
				tvTime.setText("0秒");
				sengMsgToDevice("关闭当前模式    ");
			}else{
				ptStatus.setStatusZhenJiuClockTime(t - 1);
				statusUtil.setPTStatus(mContext,ptStatus);
				showLog("当前时间为：" + t + "，具体为：" + TimeUtil.getStrFromInt(t));
			}
			if(curType == TYPE_ZHENJIU){
				reSetLayout(TYPE_ZHENJIU);
			}
		}
	};


	public class AnmoThread extends Thread{
		@Override
		public void run() {
			while (anmoIsRun){
				try {
					sleep(1000);
					Message message = new Message();
					message.what = 1;
					anmoHandler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private Handler anmoHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int t = ptStatus.getStatusAnMoClockTime();
			if(t - 1 <= 0){
				ptStatus.setStatusAnMoClockTime(0);
				if (!anmoThread.isInterrupted()){
					anmoIsRun = false;
				}
				ptStatus.setStatusAnMoIsClock(false);
				ptStatus.setStatusAnMoIsClock(false);
				showToast("计时结束，停止按摩");
				tvTime.setText("0秒");
				sengMsgToDevice("关闭当前模式1    ");
			}else{
				ptStatus.setStatusAnMoClockTime(t - 1);
				statusUtil.setPTStatus(mContext,ptStatus);
				showLog("当前时间为：" + t + "，具体为：" + TimeUtil.getStrFromInt(t));
			}
			if(curType == TYPE_ANMO){
				reSetLayout(TYPE_ANMO);
			}
		}
	};


	public class LiliaoThread extends Thread{
		@Override
		public void run() {
			while (liliaoIsRun){
				try {
					sleep(1000);
					Message message = new Message();
					message.what = 1;
					liliaoHandler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private Handler liliaoHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int t = ptStatus.getStatusLiLiaoClockTime();
			if(t - 1 <= 0){
				ptStatus.setStatusLiLiaoClockTime(0);
				if (!liliaoThread.isInterrupted()){
					liliaoIsRun = false;
				}
				ptStatus.setStatusLiLiaoIsClock(false);
				ptStatus.setStatusLiLiaoIsOpen(false);
				showToast("计时结束，停止理疗");
				tvTime.setText("0秒");
				sengMsgToDevice("关闭当前模式    ");
			}else{
				ptStatus.setStatusLiLiaoClockTime(t - 1);
				statusUtil.setPTStatus(mContext,ptStatus);
				showLog("当前时间为：" + t + "，具体为：" + TimeUtil.getStrFromInt(t));
			}
			if(curType == TYPE_LILIAO){
				reSetLayout(TYPE_LILIAO);
			}
		}
	};


	public class YueliaoThread extends Thread{
		@Override
		public void run() {
			while (yueliaoIsRun){
				try {
					sleep(1000);
					Message message = new Message();
					message.what = 1;
					yueliaoHandler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private Handler yueliaoHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int t = ptStatus.getStatusYueLiaoClockTime();
			if(t - 1 <= 0){
				ptStatus.setStatusYueLiaoClockTime(0);
				if (!yueliaoThread.isInterrupted()){
					yueliaoIsRun = false;
				}
				ptStatus.setStatusYueLiaoIsClock(false);
				ptStatus.setStatusYueLiaoIsOpen(false);
				showToast("计时结束，停止乐疗");
				tvTime.setText("0秒");
				sengMsgToDevice("关闭当前模式    ");
			}else{
				ptStatus.setStatusYueLiaoClockTime(t - 1);
				statusUtil.setPTStatus(mContext,ptStatus);
				showLog("当前时间为：" + t + "，具体为：" + TimeUtil.getStrFromInt(t));
			}
			if(curType == TYPE_YUELIAO){
				reSetLayout(TYPE_YUELIAO);
			}
		}
	};

	private void sengMsgToDevice(String msg){
		Intent intent = new Intent(BTSOCKET_SENG_MSG);
		intent.putExtra("msg" , msg);
		mContext.sendBroadcast(intent);
	}


}
