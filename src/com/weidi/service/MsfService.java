package com.weidi.service;

import java.net.DatagramSocket;
import java.net.SocketException;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.weidi.QApp;
import com.weidi.bean.User;
import com.weidi.listener.CheckConnectionListener;
import com.weidi.listener.FriendsPacketListener;
import com.weidi.listener.IQPacketListener;
import com.weidi.listener.MsgListener;
import com.weidi.listener.XmppPresenceInterceptor;
import com.weidi.listener.XmppRosterListener;
import com.weidi.provider.BindPhone;
import com.weidi.provider.BindPhoneProvider;
import com.weidi.provider.GetPhone;
import com.weidi.provider.GetPhoneProvider;
import com.weidi.provider.Near;
import com.weidi.provider.NearProvider;
import com.weidi.provider.NearTime;
import com.weidi.provider.NearTimeProvider;
import com.weidi.util.Const;
import com.weidi.util.DebugOut;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppConnectionManager;
import com.weidi.util.XmppUtil;

public class MsfService extends Service {

	private static String TAG = "MsfService";
	private static MsfService mInstance = null;
	public static DatagramSocket ds = null;

	private NotificationManager mNotificationManager;

	private String mUserName, mPassword;
	private XmppConnectionManager mXmppConnectionManager;
	private XMPPConnection mXMPPConnection;

	private CheckConnectionListener checkConnectionListener;
	private FriendsPacketListener friendsPacketListener;
	private IQPacketListener iqPacketListener;

	private final IBinder binder = new MyBinder();

	public class MyBinder extends Binder {
		public MsfService getService() {
			return MsfService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		DebugOut.PrintlnOut("MsfServiceOnCreate");
		mInstance = this;
		mUserName = PreferencesUtils.getSharePreStr("username");
		mPassword = PreferencesUtils.getSharePreStr("pwd");
		try {

			ds = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE); // 通知

		mXmppConnectionManager = XmppConnectionManager.getInstance();

		initXMPPTask();

	}

	public static MsfService getInstance() {
		return mInstance;
	}

	/**
	 * 初始化xmpp和完成后台登录
	 */
	private void initXMPPTask() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					initXMPP();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 初始化XMPP
	 */
	void initXMPP() {
		DebugOut.PrintlnOut(TAG + "-initXMPP");
		mXMPPConnection = mXmppConnectionManager.init();// 初始化XMPPConnection
		// 用于手机登录手机绑定等发IQ包的功能
		ProviderManager.getInstance().addIQProvider(GetPhone.ELEMENT_NAME,
				GetPhone.NAMESPACE, new GetPhoneProvider());

		ProviderManager.getInstance().addIQProvider(BindPhone.ELEMENT_NAME,
				BindPhone.NAMESPACE, new BindPhoneProvider());

		ProviderManager.getInstance().addIQProvider(NearTime.ELEMENT_NAME,
				NearTime.NAMESPACE, new NearTimeProvider());

		ProviderManager.getInstance().addIQProvider(Near.ELEMENT_NAME,
				Near.NAMESPACE, new NearProvider());
		// 登录判断
		initXMPPIm();
	}

	Registration registration = new Registration();

	void initXMPPIm() {
		try {
			mPassword = PreferencesUtils.getSharePreStr("pwd");
			mXMPPConnection.connect();
			QApp.xmppConnection = mXMPPConnection;
			try {
				if (checkConnectionListener != null) {
					mXMPPConnection
							.removeConnectionListener(checkConnectionListener);
					checkConnectionListener = null;
				}
			} catch (Exception e) {
			}
			if (mUserName.length() == 7) {
				loginForWeidi();
			} else if (mUserName.length() == 11) {
				loginForPhone();
			}
			// 登录成功
			if (mXMPPConnection.isAuthenticated()) {
				Log.i("TAG", mXMPPConnection.isAuthenticated() + "");
			
				sendLoginBroadcast(true);

				// packet监听
				iqPacketListener = new IQPacketListener(this);
				PacketFilter iqPacketFilter = new AndFilter(new PacketIDFilter(
						registration.getPacketID()), new PacketTypeFilter(
						IQ.class));
				mXMPPConnection.addPacketListener(iqPacketListener,
						iqPacketFilter);
				// 添加xmpp连接监听
				checkConnectionListener = new CheckConnectionListener(this);
				mXMPPConnection.addConnectionListener(checkConnectionListener);
				// 注册好友状态更新监听
				friendsPacketListener = new FriendsPacketListener(this);
				PacketFilter filter = new AndFilter(new PacketTypeFilter(
						Presence.class));
				mXMPPConnection
						.addPacketListener(friendsPacketListener, filter);
				// 注册好友拦截器
				mXMPPConnection.addPacketInterceptor(
						new XmppPresenceInterceptor(), new PacketTypeFilter(
								Presence.class));
				XmppUtil.setPresence(this, mXMPPConnection,
						PreferencesUtils.getSharePreInt("online_status"));// 设置在线状态
				// 注册通许录监听器
				final Roster roster = QApp.xmppConnection.getRoster();
				roster.addRosterListener(new XmppRosterListener());


			} else {
				sendLoginBroadcast(false);
				stopSelf(); // 如果登录失败，自动销毁Service
			}
		} catch (Exception e) {
			e.printStackTrace();
			sendLoginBroadcast(false);
			stopSelf();
		}
		ChatManager chatmanager = mXMPPConnection.getChatManager();
		chatmanager.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat arg0, boolean arg1) {
				arg0.addMessageListener(new MsgListener(MsfService.this,
						mNotificationManager));
			}
		});
	}

	/**
	 * 登录XMPP
	 */
	void loginForCheckConnection() {
	}

	void loginForWeidi() {
		try {
			mPassword = PreferencesUtils.getSharePreStr("pwd");
			Log.i("TAG", mUserName);
			Log.i("TAG", mPassword);
			PreferencesUtils.putSharePre("weidi", mUserName);
			mXMPPConnection.login(mUserName, mPassword, Const.RESOUCE_ID);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	void loginForPhone() {
		String result = null;
		try {
			String phone = PreferencesUtils.getSharePreStr("username");
			Log.i("TAG", phone);
			result = QApp.initPhoneToWei(phone);// 手机号转成微迪号
			PreferencesUtils.putSharePre("weidi", result);
			Log.i("TAG", mXMPPConnection.getHost());
			Log.i("TAG", result);
			if (result.length() <= 5) {
				ToastUtil.showLongToast(mInstance, "连接失败");
			} else {
				Log.i("TAG", "登录动作");
				mXMPPConnection.login(result, mPassword, Const.RESOUCE_ID);// 登录动作
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 发送登录状态广播
	 * 
	 * @param isLoginSuccess
	 */
	void sendLoginBroadcast(boolean isLoginSuccess) {
		Intent intent = new Intent(Const.ACTION_IS_LOGIN_SUCCESS);
		intent.putExtra("isLoginSuccess", isLoginSuccess);
		sendBroadcast(intent);
	}

	@Override
	public void onDestroy() {
		if (mNotificationManager != null) {

		}
		try {
			if (mXMPPConnection != null) {
				mXMPPConnection.disconnect();
				mXMPPConnection = null;
			}
			if (mXmppConnectionManager != null) {
				mXmppConnectionManager = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
}