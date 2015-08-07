package com.weidi.bean;

import org.jivesoftware.smackx.packet.VCard;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.weidi.db.VCardDao;
import com.weidi.util.Logger;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-30 下午6:49:55
 * @Description 1.0
 */
public class UserInfo {

	private static final String TAG = UserInfo.class.getSimpleName();
	public static final int USER_INFO_TAG = 10023;

	

	public static void getUserInfo(final String weidi, final Handler handler,Context context) {
		new XmppLoadThread(context) {

			@Override
			protected void result(Object object) {
				Logger.e(TAG, "连锁酒店方");
				Message msg = new Message();
				msg.obj = object;
				msg.what = USER_INFO_TAG;
				handler.sendMessage(msg);

			}

			@Override
			protected Object load() {
				User friend = VCardDao.getInstance().getUser(weidi);
				if (friend == null) {
					VCard vCard = XmppUtil.getUserInfo(weidi);
					User user = new User(vCard);
					VCardDao.getInstance().insertUser(user);
					return user;
				} else {
					return friend;
				}
			}
		};

	}

}
