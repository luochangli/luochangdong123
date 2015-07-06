package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;

public class MomentIQ extends IQ{

	public static final String ELEMENT_NAME = "query";
	public static final String NAMESPACE = "com:jsm:moment";

	private String username;
	private String datetime;
	private String errorcode;
	private String errortext;
	
	
	public String getuserName() {
		return username;
	}
	public void setuserName(String value) {
		username = value;
	}
	public String getdateTime() {
		return datetime;
	}
	public void setdateTime(String time) {
		datetime = time;
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
		else if (this.getType() == IQ.Type.GET) {
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
	
	 /*<query xmlns=” com:jsm:moment” event=”get”>
	 <username>xxx</username>
	 <datetime>xxx</datetime>
	   </query>*/

	
	private String getChildElementXMLForSet() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns="+"com:jsm:moment"+""+">");
       // buf.append("<phone>").append(phone).append("</phone>");
        buf.append("</query>");
        return buf.toString();
	}
	
	
}
