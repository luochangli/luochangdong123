package com.weidi.listener;

import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import com.weidi.util.Logger;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-7-11 下午6:27:47
 *@Description 1.0
 */
public class XmppMessageIntercepter implements PacketInterceptor{

	private static String TAG = "XmppMessageIntercepter";
	
	@Override
	public void interceptPacket(Packet packet) {
		Message nowMessage = (Message) packet;
		Logger.i(TAG, nowMessage.toXML());
		
	}

}
