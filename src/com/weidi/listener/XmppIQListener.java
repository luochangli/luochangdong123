package com.weidi.listener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import com.weidi.util.Logger;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-7-8 下午6:36:20
 *@Description 1.0
 */
public class XmppIQListener implements PacketListener {

	private static String TAG = "XmppIQListener";
	@Override
	public void processPacket(Packet packet) {
		Logger.i(TAG, packet.toXML());
		
		
	}

}
