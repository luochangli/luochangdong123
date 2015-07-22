package com.weidi.chat;

import org.jivesoftware.smack.XMPPException;

import com.weidi.QApp;
import com.weidi.activity.LoginActivity;
import com.weidi.provider.GetAccountByPhoneIQ;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.ToastUtil;
import com.weidi.util.Util;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-17 下午2:29:47
 * @Description 1.0
 */
public class ReLoginTask {

	private static String TAG = "ReLoginTask";

	public static Boolean loginTask(String username, String pwd) {
		if (username.length() < 8) {
			return loginWeidi(username, pwd);
		}
		if (Util.getInstance().isMobileNumber(username)) {
			GetAccountByPhoneIQ iq = IQOrder.getInstance()
					.getAccountByPhone(username);
			if (iq.getAccount() != null) {
				Logger.e(TAG, username + "手机转微迪号：" + iq.getAccount());
				return loginWeidi(iq.getAccount(), pwd);
			}
			if (iq.getErrorCode() != null) {
				if (iq.getErrorCode().equals(LoginActivity.USER_NOT_EXIST)) {
					ToastUtil.showShortToast(QApp.getInstance(), LoginActivity.USER_NOT_EXIST_STRING);
				}
				return false;
			}
		}
		return false;

	}

	private static Boolean loginWeidi(String username, String pwd) {
		try {
			QApp.getXmppConnection().login(username, pwd, Const.RESOUCE_ID);
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
			ToastUtil.showShortToast(QApp.getInstance(), "您好！连接失败了。");
			return false;
		}

	}

}
