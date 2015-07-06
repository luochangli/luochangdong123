package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;

public class NearTime extends IQ{

	public static final String ELEMENT_NAME = "query";
	public static final String NAMESPACE = "com:jsm:latandlon:set";

	private String phone;
	private String errorcode;
	private String errortext;
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String value) {
		phone = value;
	}
	
	public String getErrorCode() {
		return errorcode;
	}
	public void setErrorCode(String value) {
		errorcode = value;
	}
	public String getErrorText() {
		return errortext;
	}
	
	public void setErrorText(String value) {
		errortext = value;
	}
	@Override
	public String getChildElementXML() {
		if (this.getType() == IQ.Type.RESULT) {
			return getChildElementXMLForResult();
		}
		else if (this.getType() == IQ.Type.SET) {
			return getChildElementXMLForSet();
		}
		else {
			return "";
		}
	}
	private String getChildElementXMLForResult() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"com:jsm:latandlon:set\">");
        if (errorcode != null) {
        	buf.append("<").append("error code=\"").append(errorcode).append("\">")
        	.append(errortext).append("</").append("error>");
        }
        buf.append("</query>");
        return buf.toString();
	}
	
	private String getChildElementXMLForSet() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"com:jsm:latandlon:set\">");
        buf.append("<phone>").append(phone).append("</phone>");
        buf.append("</query>");
        return buf.toString();
	}
}
