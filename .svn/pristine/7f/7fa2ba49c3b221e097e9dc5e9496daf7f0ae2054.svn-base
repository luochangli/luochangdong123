package com.weidi.provider;

import org.jivesoftware.smack.packet.PacketExtension;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-10 下午6:37:46
 * @Description 1.0
 */
public class EntryMUCPresence implements PacketExtension {

	@Override
	public String getElementName() {
		// TODO Auto-generated method stub
		return "x";
	}

	@Override
	public String getNamespace() {
		return "http://jabber.org/protocol/muc";
	}

	@Override
	public String toXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<").append(getElementName()).append(" xmlns=\"")
				.append(getNamespace()).append("\">");
		buf.append("</").append(getElementName()).append(">");
		return buf.toString();
	}

}
