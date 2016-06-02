package com.ywhyw.bob.activity;

import com.ywhyw.bob.R;
import com.ywhyw.bob.base.BaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class AboutActivity extends BaseActivity{
	
	private LinearLayout back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		initViews();
		initEvent();
	}

	private void initViews() {
		back = (LinearLayout) findViewById(R.id.ll_back);
	}

	private void initEvent() {
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
