package com.weidi.activity;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.service.MsfService;
import com.weidi.view.TitleBarView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.System;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity extends Activity {

	private Button bt_exit;
	RelativeLayout login_out;
	TextView back;
	TitleBarView mTitleBarView;
	RelativeLayout re_ddm, re_histroy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		intiView();
		initTitleView();
		QApp.getInstance().addActivity(SettingActivity.this);

	}

	private void intiView() {
		bt_exit = (Button) findViewById(R.id.bt_exit);
		bt_exit.setOnClickListener(new AllListener());
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		re_ddm = (RelativeLayout) findViewById(R.id.re_ddm);
		re_ddm.setOnClickListener(new AllListener());
		re_histroy = (RelativeLayout) findViewById(R.id.re_histroy);
		re_histroy.setOnClickListener(new AllListener());

	}

	class AllListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.bt_exit:
				exit();
				break;

			case R.id.re_ddm:
				ddm();
				break;
			case R.id.re_histroy:
				histroy();
				break;
			default:
				break;
			}

		}

	}

	private void initTitleView() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.login);
		mTitleBarView.setBtnLeftIcon(R.drawable.btn_back_nomal);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});

	}

	public void exit() {
		final AlertDialog dlg = new AlertDialog.Builder(SettingActivity.this)
				.create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.activity_exit);

		TextView exit = (TextView) window.findViewById(R.id.tv_exit);
		TextView tv_allexit = (TextView) window.findViewById(R.id.tv_allexit);
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivity(new Intent(SettingActivity.this,
						LoginActivity.class));

				MsfService.getInstance().stopSelf();
				java.lang.System.exit(0);

			}

		});
		tv_allexit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				QApp.getInstance().exit();

				MsfService.getInstance().stopSelf();
				java.lang.System.exit(0);

			}

		});
	}

	private void ddm() {
		Intent intent = new Intent(SettingActivity.this, DdmActivity.class);

		startActivity(intent);
	}

	private void histroy() {
		Intent intent = new Intent(SettingActivity.this, ClearHistroyChat.class);

		startActivity(intent);
	}

}
