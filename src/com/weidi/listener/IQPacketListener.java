package com.weidi.listener;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;

import com.weidi.provider.BindPhoneIQ;
import com.weidi.service.MsfService;
import com.weidi.util.Logger;
import com.weidi.util.XmppConnectionManager;

public class IQPacketListener implements PacketListener{
	private static String TAG = "IQPacketListener";
	MsfService context;
	public IQPacketListener (MsfService context){
		this.context = context;
	}
	@Override
	public void processPacket(Packet packet) {
		BindPhoneIQ phoneIQ = new BindPhoneIQ();
		if (packet == null) {
			return;
		}
		Logger.i(TAG, packet.toXML());
		PacketFilter filter = new AndFilter(new PacketIDFilter(phoneIQ.getPacketID()), 
        		new PacketTypeFilter(IQ.class));
		PacketCollector collector = XmppConnectionManager.getInstance().init().createPacketCollector(filter);
		IQ result = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
		Logger.i(TAG, result.toXML());
		
		collector.cancel();
		if (result.getType().equals(Type.RESULT)) {
			Logger.i(TAG, result.toXML());
			//发送广播
		}
	}
}
