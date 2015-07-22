package com.weidi.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;

import com.weidi.QApp;
import com.weidi.activity.LoginActivity;
import com.weidi.bean.Friend;
import com.weidi.chat.GroupChatSettingActi;
import com.weidi.chat.groupchat.CreatChatRoomActivity;
import com.weidi.fragment.NewConstactFragment;
import com.weidi.provider.CreateMUCIQ;
import com.weidi.provider.GetAccountByPhoneIQ;
import com.weidi.provider.ObtainMUCInfoIQ;
import com.weidi.provider.ObtainMUCmemberIQ;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.XmppUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-8 下午6:36:20
 * @Description 1.0
 */
public class XmppIQListener implements PacketListener {

	private static String TAG = "XmppIQListener";
	public static final String MUC = "muc";

	@Override
	public void processPacket(Packet packet) {
		Logger.i(TAG, packet.toXML());
		String xml = packet.toXML();
		if (xml.contains("event=\"getgroupinfo\"")) {
			
		}
		if (xml.contains("event=\"getmember\"")) {
//			ObtainMUCmemberIQ iq = (ObtainMUCmemberIQ) packet;
//			Intent intent = new Intent(GroupChatSettingActi.GROUP_MENBER_LIST);
//			Bundle bundle = new Bundle();
//			bundle.putSerializable(GroupChatSettingActi.GROUP_MENBER_LIST, iq);
//			intent.putExtras(bundle);
//			QApp.mLocalBroadcastManager.sendBroadcast(intent);
		}
		if (xml.contains("event=\"groupinfo\"")) {
			ObtainMUCInfoIQ iq = (ObtainMUCInfoIQ) packet;
			Intent intent = new Intent(GroupChatSettingActi.MUC_INFO);
			Bundle bundle = new Bundle();
			bundle.putSerializable(GroupChatSettingActi.MUC_INFO, iq);
			intent.putExtras(bundle);
			QApp.mLocalBroadcastManager.sendBroadcast(intent);
		}
		if (xml.contains("jabber:iq:roster")
				&& xml.contains(" subscription=\"both\"")) {
			QApp.mLocalBroadcastManager.sendBroadcast(new Intent(
					NewConstactFragment.REFRESH_CONSTACT));
		}
		if (xml.contains("getaccountbyphone")) {
			GetAccountByPhoneIQ iq = (GetAccountByPhoneIQ) packet;
			
		}
	}

}
