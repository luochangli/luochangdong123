package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;

public class BindPhoneIQ  extends IQ{
	private String phone;
	private Type type;
	private String xmlns;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

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
		if (getType() == IQ.Type.SET){
			buf.append("<phone>").append(phone).append("</phone>");
			buf.append(getExtensionsXML());
		}
		if (getType().equals(IQ.Type.RESULT)) {
			buf.append(getExtensionsXML());
		}
		buf.append("</query>");
		return buf.toString();
		
//		if (this.type.equals(IQ.Type.RESULT)) 
//			return this.getChildElementXMLForResult();
//		else
//			return this.getChildElementXMLForGet();
	}
	
	private String getChildElementXMLForGet() {
		StringBuffer buf = new StringBuffer();
		buf.append("<query" +  " xmlns=\"" + xmlns + "\">");
		if (getType() == IQ.Type.SET){
			buf.append("<phone>").append(phone).append("</phone>");
			buf.append(getExtensionsXML());
		}
		if (getType().equals(IQ.Type.RESULT)) {
			buf.append(getExtensionsXML());
		}
		buf.append("</query>");
		return buf.toString();
	}
	
	private String getChildElementXMLForResult() {
		
		return this.getExtensionsXML();
		
	}
}
