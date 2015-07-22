package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-7-18 下午5:24:06
 *@Description 1.0
 */
public class GetPwdAuthcodeIQ extends IQ{
	private String account;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	@Override
	public String getChildElementXML() {
		if (getType().equals(IQ.Type.GET)) {
			return send();
		}
		if (getType().equals(IQ.Type.RESULT)) {
			return getResult();
		}
		return null;
	}
	private String getResult() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:user\" event=\"passwordauthcode\">");
		return sb.toString();
	}

	private String send() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:user\" event=\"passwordauthcode\">");
		sb.append("<account>").append(getAccount()).append("</account>");
		sb.append("</query>");

		return sb.toString();
	}
}
