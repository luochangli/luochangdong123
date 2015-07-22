package com.weidi.service;

import java.net.DatagramSocket;
import java.net.SocketException;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.weidi.QApp;
import com.weidi.activity.LoginActivity;
import com.weidi.chat.IQOrder;
import com.weidi.listener.CheckConnectionListener;
import com.weidi.listener.FriendsPacketListener;
import com.weidi.listener.IQPacketListener;
import com.weidi.listener.MsgListener;
import com.weidi.provider.BindPhone;
import com.weidi.provider.BindPhoneProvider;
import com.weidi.provider.GetAccountByPhoneIQ;
import com.weidi.provider.Near;
import com.weidi.provider.NearProvider;
import com.weidi.provider.NearTime;
import com.weidi.provider.NearTimeProvider;
import com.weidi.util.Const;
import com.weidi.util.DebugOut;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppConnectionManager;
import com.weidi.util.XmppUtil;

public class MsfService extends Service {

	private static String TAG = "MsfService";
	private static MsfService mInstance = null;
	public static DatagramSocket ds = null;



	private String mUserName, mPassword;
	private XMPPConnection mXMPPConnection;
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
		mXMPPConnection = QApp.getXmppConnection();
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
		ProviderManager pm = ProviderManager.getInstance();
		setProviderRegister(pm);
		// 登录判断
		initXMPPIm();
	}

	private void setProviderRegister(ProviderManager pm) {

		pm.addIQProvider(BindPhone.ELEMENT_NAME, BindPhone.NAMESPACE,
				new BindPhoneProvider());

		pm.addIQProvider(NearTime.ELEMENT_NAME, NearTime.NAMESPACE,
				new NearTimeProvider());

		pm.addIQProvider(Near.ELEMENT_NAME, Near.NAMESPACE, new NearProvider());

		/*
		 * pm.addIQProvider(Friend_get.ELEMENT_NAME, Friend_get.NAMESPACE, new
		 * FrinedProvider());
		 */

	}

	Registration registration = new Registration();

	void initXMPPIm() {
		try {
			mPassword = PreferencesUtils.getSharePreStr("pwd");
			if (mUserName.length() == 7) {
				loginForWeidi();
			} else if (mUserName.length() == 11) {
				loginForPhone();
			}
			// 登录成功
			if (mXMPPConnection.isAuthenticated()) {
				Log.i("TAG", mXMPPConnection.isAuthenticated() + "");
				Const.USER_NAME = PreferencesUtils.getSharePreStr("weidi");
				sendLoginBroadcast(true);
				// packet监听
				iqPacketListener = new IQPacketListener();
				PacketFilter iqPacketFilter = new AndFilter(new PacketIDFilter(
						registration.getPacketID()), new PacketTypeFilter(
						IQ.class));
				mXMPPConnection.addPacketListener(iqPacketListener,
						iqPacketFilter);
			
			} else {
				 // 如果登录失败，自动销毁Service
				sendLoginBroadcast(false);
				stopSelf();
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
				arg0.addMessageListener(new MsgListener());
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
			GetAccountByPhoneIQ iq = IQOrder.getInstance()
					.getAccountByPhone(phone);
			if (iq.getAccount() != null) {
				result = iq.getAccount();
				Logger.e(TAG, phone + "手机转微迪号：" + result);
			}
			if (iq.getErrorCode() != null) {
				if (iq.getErrorCode().equals(LoginActivity.USER_NOT_EXIST)) {
					ToastUtil.showShortToast(QApp.getInstance(), "用户不存在");
				}

			}
			// result = QApp.initPhoneToWei(phone);// 手机号转成微迪号
			PreferencesUtils.putSharePre("weidi", result);

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

		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
}
