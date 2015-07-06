package com.weidi.util;

import org.jivesoftware.smack.packet.IQ;

public class GetPhoneIQ extends IQ{
	private String xmlns;
	
	
	
	public String getXmlns() {
		return xmlns;
	}



	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}



	@Override
	public String getChildElementXML() {
		
		StringBuffer buf = new StringBuffer();
		buf.append("<query" +  " xmlns=\"" + xmlns + "\">");
		buf.append("</query>");
		return buf.toString();
	}

	
}
