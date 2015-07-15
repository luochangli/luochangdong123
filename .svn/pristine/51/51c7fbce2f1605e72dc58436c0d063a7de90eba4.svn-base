package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;

public class Friend_reply extends IQ{
	private String momentid,type,content;
	

	public String getMomentid() {
		return momentid;
	}


	public void setMomentid(String momentid) {
		this.momentid = momentid;
	}


	public String getType1() {
		return type;
	}


	public void setType1(String type) {
		this.type = type;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<query xmlns=\"com:jsm:moment\" event=\"reply\">");
		buf.append("<").append("momentid").append(">")
		.append(momentid)
		.append("<").append("/momentid").append(">")
		.append("<").append("type").append(">")
		.append(type)
		.append("<").append("/type").append(">")
		.append("<").append("content").append(">")
		.append(content)
		.append("<").append("/content").append(">");
		buf.append("</query>");
		return buf.toString();
	}

}
