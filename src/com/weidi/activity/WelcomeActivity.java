package com.weidi.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.weidi.QApp;
import com.weidi.R;

public class WelcomeActivity extends Activity {
	protected static final String TAG = "WelcomeActivity";
	private Context mContext;
	private RelativeLayout mImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		mContext = this;
		findView();
		init();
		QApp.getInstance().addActivity(WelcomeActivity.this);
	}

	private void findView() {
		mImageView = (RelativeLayout) findViewById(R.id.iv_welcome);
	}

	private void init() {
		mImageView.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent =null;
				intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		},2000); 

	}
}
