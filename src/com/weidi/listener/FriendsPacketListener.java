package com.weidi.listener;

import java.util.Date;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smack.util.StringUtils;

import android.content.Intent;
import android.util.Log;

import com.weidi.QApp;
import com.weidi.bean.Friend;
import com.weidi.bean.Session;
import com.weidi.common.DateUtil;
import com.weidi.db.NewFriendDao;
import com.weidi.db.SessionDao;
import com.weidi.service.MsfService;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppUtil;

public class FriendsPacketListener implements PacketListener {
	private static String TAG = "FriendsPacketListener";
	private XMPPConnection con;
	MsfService context;
	private SessionDao sessionDao;

	public FriendsPacketListener(MsfService context) {
		this.context = context;
		sessionDao = new SessionDao(context);
	}

	private Session setSession(String to, String from, String msgtime) {
		Session session = new Session();
		session.setFrom(from);
		session.setTo(to);
		session.setNotReadCount("");// 未读消息数量
		session.setTime(msgtime);
		return session;
	}

	@Override
	public void processPacket(Packet packet) {
		Logger.i(TAG, packet.toXML());
		
		if (packet instanceof Presence) {
			Presence presence = (Presence) packet;
			// Presence还有很多方法，可查看API

			final String fromJid = presence.getFrom();// 发送方
			final String toJid = presence.getTo();// 接收方

			String to = StringUtils.parseName(toJid);
			String from = StringUtils.parseName(fromJid);
			String msgtime = DateUtil.date2Str(new Date(), "MM-dd HH:mm");// 消息时间

			if (presence.getType().equals(Presence.Type.subscribe)) {// 好友申请 本地缓存好友信息
				if (!XmppUtil.getInstance().getFriendList().contains(new Friend(XmppUtil.getUsername(fromJid)))) {
					Friend friend  = new Friend(XmppUtil.getUsername(fromJid));
					friend.type = ItemType.from;
					XmppUtil.getInstance().getFriendList().add(friend);
				}
	        	
				for (Friend friend : XmppUtil.getInstance().getFriendList()) {
					System.out.println("我好友"+friend.username+"和我的关系"+friend.type);
					if (friend.equals(new Friend(XmppUtil.getUsername(fromJid))) && friend.type == ItemType.to) {
						//需添加提示
						XmppUtil.getInstance().changeFriend(friend, ItemType.both);
				        Log.e("friend", to+"我收到好友请求toBoth");
					}
					else if (friend.username.equals(XmppUtil.getUsername(fromJid))) {
						XmppUtil.getInstance().changeFriend(friend, ItemType.from);
				        Log.e("friend", to+"我收到好友请求toFrom");
						NewFriendDao.getInstance(context).saveNewFriend(from);//把好友消息放到新朋友里
						QApp.getInstance().sendBroadcast(new Intent(Const.ACTION_NEW_FRIEND_MSG));
					}
				}

				updateConstant();

			} else if (presence.getType().equals(Presence.Type.subscribed)) {// 同意添加好友，不知道为什么会自动同意
				Logger.e(TAG, "添加好友subscribed:" + fromJid);


			} else if (presence.getType().equals(Presence.Type.unsubscribe)) {// 拒绝添加好友和删除好友

				Log.e(TAG, "拒绝添加好友");
			} else if (presence.getType().equals(Presence.Type.unsubscribed)) {
				for (Friend friend : XmppUtil.getInstance().getFriendList()) {
					if (friend.equals(new Friend(from))) {
						XmppUtil.getInstance().changeFriend(friend,
								ItemType.remove);
					}
				}
				updateConstant();

			} else {
				Log.e(TAG, "error");
			}
		}
	}

	private void updateConstant() {
		Intent intent = new Intent(
				Const.ACTION_FRIENDS_ONLINE_STATUS_CHANGE);
		context.sendBroadcast(intent);
	};

}
