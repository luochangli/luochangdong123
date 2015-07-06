package com.weidi.listener;

import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;

import android.content.Intent;
import android.util.Log;

import com.weidi.QApp;
import com.weidi.bean.Friend;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.XmppUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-29 上午11:10:46
 * @Description 1.0 添加好友发送拦截器
 */
public class XmppPresenceInterceptor implements PacketInterceptor {

	@Override
	public void interceptPacket(Packet packet) {
		Presence presence = (Presence) packet;

		Logger.e("xmppchat send", presence.toXML());

		String from = presence.getFrom();// 发送方
		String to = presence.getTo();// 接收方
		// Presence.Type有7中状态
		if (presence.getType().equals(Presence.Type.subscribe)) {// 发出好友申请
			if (!XmppUtil.getInstance().getFriendList()
					.contains(new Friend(XmppUtil.getUsername(to)))) {
				Friend friend = new Friend(XmppUtil.getUsername(to));
				friend.type = ItemType.to;
				XmppUtil.getInstance().getFriendList().add(friend);
			}
			for (Friend friend : XmppUtil.getInstance().getFriendList()) {
				System.out.println("我好友" + friend.username + "和我的关系"
						+ friend.type);
				if ((friend.username.equals(XmppUtil.getUsername(to)) && friend.type == ItemType.from)) {
					XmppUtil.getInstance().changeFriend(friend, ItemType.both);
					Log.e("friend", "我向" + to + "发出好友请求toBoth");
				} else if (friend.username.equals(XmppUtil.getUsername(to))) {
					XmppUtil.getInstance().changeFriend(friend, ItemType.to);
					Log.e("friend", "我向" + to + "发出好友请求toTo");
				}
			}

			// 发送广播，通知消息界面更新
			QApp.getInstance()
					.sendBroadcast(new Intent(Const.ACTION_FRIENDS_ONLINE_STATUS_CHANGE));
		} else if (presence.getType().equals(Presence.Type.subscribed)) {// 同意添加好友

			Logger.e("friend", to + "我同意好友添加");
			// 发送广播，通知消息界面更新
//			QApp.getInstance()
//					.sendBroadcast(new Intent(Const.ACTION_FRIENDS_ONLINE_STATUS_CHANGE));
		} else if (presence.getType().equals(Presence.Type.unsubscribe)
				|| presence.getType().equals(Presence.Type.unsubscribed)) {// 拒绝添加好友
																			// 删除好友

			Logger.e("friend", "我删除好友" + to);
			for (Friend friend : XmppUtil.getInstance().getFriendList()) {
				if (friend.username.equals(XmppUtil.getUsername(to))) {
					XmppUtil.getInstance()
							.changeFriend(friend, ItemType.remove);
				}
			}
			// 发送广播，通知消息界面更新
			QApp.getInstance()
					.sendBroadcast(new Intent(Const.ACTION_FRIENDS_ONLINE_STATUS_CHANGE));
		}

	}

}