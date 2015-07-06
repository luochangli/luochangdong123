package com.weidi.util;

import org.jivesoftware.smack.packet.IQ;

public class ResultIQ extends IQ{

	private String xmlns;
	private Type type;
	private String error;
	
	
	public String getXmlns() {
		return xmlns;
	}


	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}


	public Type getType() {
		return type;
	}


	public void setType(Type type) {
		this.type = type;
	}


	@Override
	public String getChildElementXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<query" +  " xmlns=\"" + xmlns + "\">");
		buf.append("<error>").append(error).append("</phone>");
		buf.append(getExtensionsXML());
		buf.append("</query>");
		return buf.toString();
	}

}
