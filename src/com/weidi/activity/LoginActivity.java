package com.weidi.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weidi.MainActivity;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.chat.IQOrder;
import com.weidi.chat.ReLoginTask;
import com.weidi.fragment.PersonalFrag;
import com.weidi.service.MsfService;
import com.weidi.util.Const;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;
import com.weidi.view.LoadingDialog;
import com.weidi.view.TextURLView;
import com.weidi.view.TitleBarView;

public class LoginActivity extends Activity {

	public static final String USER_NOT_EXIST = "00007";
	public static final String USER_NOT_EXIST_STRING = "用户不存在";
	private Context mContext;
	private RelativeLayout rl_user;
	private Button mLogin;
	private TextView mTextViewURL;
	private TextView btnLeft, btnRight;
	private EditText account, password;
	private BroadcastReceiver receiver;
	private LoadingDialog loadDialog;
	private LocationManager locationManager;
	private double latitude;
	private double longitude;

	String username;// 用户名
	String pwd;// 密码
	private LocationListener locationListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		loadDialog = new LoadingDialog(this);
		loadDialog.setTitle("正在登录...");
		findView();
		initTvUrl();
		init();
		initReceiver();
		// testLogin();
		locationListener = new LocationListener() {

			// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

			// Provider被enable时触发此函数，比如GPS被打开
			@Override
			public void onProviderEnabled(String provider) {

			}

			// Provider被disable时触发此函数，比如GPS被关闭
			@Override
			public void onProviderDisabled(String provider) {

			}

			// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					Log.i("Map",
							"Location changed : Lat: " + location.getLatitude()
									+ " Lng: " + location.getLongitude());
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					Log.i("TAG", latitude + "");
					Log.i("TAG", longitude + "");
					latitude = QApp.latitude;
					longitude = QApp.longitude;
				}
			}
		};
		QApp.getInstance().addActivity(LoginActivity.this);
	}

	private void testLogin() {
		IQOrder.getInstance().getPwdHint("14708641234");
		IQOrder.getInstance().getAccountByPhone("14708641234");
	}

	private void initReceiver() {
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Const.ACTION_IS_LOGIN_SUCCESS)) {
					if (loadDialog.isShowing()) {
						loadDialog.dismiss();
					}
					boolean isLoginSuccess = intent.getBooleanExtra(
							"isLoginSuccess", false);
					if (isLoginSuccess) {// 登录成功
<<<<<<< .mine

=======
						
						
						
>>>>>>> .r46
						
						// 默认开启声音和震动提醒
						PreferencesUtils.putSharePre(Const.MSG_IS_VOICE, true);
						PreferencesUtils
								.putSharePre(Const.MSG_IS_VIBRATE, true);

						// boolean gpsEnabled =
						// Settings.Secure.isLocationProviderEnabled(getContentResolver(),
						// locationManager.GPS_PROVIDER );
						// if (!gpsEnabled) {
						// Settings.Secure.setLocationProviderEnabled(getContentResolver(),locationManager.GPS_PROVIDER,
						// true);
						// }
						// //获取经纬度
						// loadLocation();
						// //向服务器发送自己的位置
						// QApp.sendPosition();
						// //获取经纬度
						// loadLocation();
						// //向服务器发送自己的位置
						// QApp.sendPosition();
						// Settings.Secure.setLocationProviderEnabled(
						// getContentResolver(),LocationManager.GPS_PROVIDER,
						// false);
						Intent fristintent = new Intent();
						// if
						// (PreferencesUtils.getFirst(PreferencesUtils.first)) {
						// fristintent.setClass(context,
						// GuideViewActivity.class);
						// PreferencesUtils.putFirst(PreferencesUtils.first,
						// false);
						// } else {
						fristintent.setClass(context, MainActivity.class);
						// }
						startActivity(fristintent);
						/*
						 * Intent intent2=new
						 * Intent(mContext,MainActivity.class);
						 * startActivity(intent2); finish();
						 */
					} else {
						ToastUtil.showShortToast(mContext,
								"登录失败，请检您的网络是否正常以及用户名和密码是否正确");

					}
				}
			}
		};
		// 注册广播接收者
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(Const.ACTION_IS_LOGIN_SUCCESS);
		registerReceiver(receiver, mFilter);
	}

	protected void loadLocation() {
		if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
			getLocation();
		} else {
			toggleGPS();
		}
	}

	private void findView() {
		rl_user = (RelativeLayout) findViewById(R.id.rl_user);
		mLogin = (Button) findViewById(R.id.login);
		btnLeft = (TextView) findViewById(R.id.tvTopLeft);

		btnRight = (TextView) findViewById(R.id.tvTopRight);
		mTextViewURL = (TextView) findViewById(R.id.tv_forget_password);
		btnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, RegisterActi.class);
				startActivity(intent);
			}
		});
		account = (EditText) findViewById(R.id.account);
		password = (EditText) findViewById(R.id.password);
	}

	private void init() {
		Animation anim = AnimationUtils.loadAnimation(mContext,
				R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);

		mLogin.setOnClickListener(loginOnClickListener);
		/*
		 * btnLeft.setText("微迪"); loginTile.setText("登录");
		 * btnRight.setText("注册");
		 * btnLeft.setTextColor(this.getResources().getColorStateList
		 * (R.color.white));
		 * btnRight.setTextColor(this.getResources().getColorStateList
		 * (R.color.white));
		 * btnRight.setOnClickListener(registerOnClickListener);
		 */

	}

	private void initTvUrl() {
		mTextViewURL.setText(R.string.forget_password);
		mTextViewURL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, ForgetPwdActi.class));
			}
		});
	}

	/**
	 * 登录
	 */
	private OnClickListener loginOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			doLogin();
		}
	};

	protected void onStart() {
		super.onStart();
		String username = PreferencesUtils.getSharePreStr("username");// 用户名
		String pwd = PreferencesUtils.getSharePreStr("pwd");// 密码
		if (!TextUtils.isEmpty(username)) {
			account.setText(username);
		}
		if (!TextUtils.isEmpty(pwd)) {
			password.setText(pwd);
		}
	};

	void doLogin() {
		username = account.getText().toString();// 用户名
		pwd = password.getText().toString();// 密码
		if (TextUtils.isEmpty(username)) {
			ToastUtil.showShortToast(mContext, "请输入您的账号");
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			ToastUtil.showShortToast(mContext, "请输入您的密码");
			return;
		}
		PreferencesUtils.putSharePre("username", username);
		PreferencesUtils.putSharePre("pwd", pwd);
		loadDialog.show();
		// 启动核心Service
		Intent intent = new Intent(this, MsfService.class);
		startService(intent);

	}

	private void getLocation() {
		Location location = locationManager
				.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
		while (location == null) {
			locationManager
					.requestLocationUpdates(LocationManager.GPS_PROVIDER,
							60000, 1000, locationListener);
			Log.i("TAG", "location = null");
		}
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		Log.i("TAG", latitude + "-1");
		Log.i("TAG", longitude + "-1");
		QApp.latitude = latitude;
		QApp.longitude = longitude;
	}

	private void toggleGPS() {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(this, 0, gpsIntent, 0).send();
		} catch (Exception e) {
			e.printStackTrace();
			locationManager.requestLocationUpdates(
					locationManager.NETWORK_PROVIDER, 60000, 1000,
					locationListener);
			Location location1 = locationManager
					.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
			if (location1 != null) {
				latitude = location1.getLatitude();
				longitude = location1.getLongitude();
				Log.i("TAG", latitude + "-1");
				Log.i("TAG", longitude + "-1");
				QApp.latitude = latitude;
				QApp.longitude = longitude;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

}
