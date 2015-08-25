package com.weidi.provider.sign;

import org.jivesoftware.smack.packet.IQ;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-23 下午4:39:21
 * @Description 1.0
 */
public class SignInIQ extends IQ {

	private String errorCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String getChildElementXML() {
		if (getType().equals(IQ.Type.SET)) {
			return send();
		}
		if (getType().equals(IQ.Type.RESULT)) {
			return result();
		}
		if (getType().equals(IQ.Type.ERROR)) {
			return getErrorCode();
		}

		return null;
	}

	private String result() {

		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:user\" event=\"sign\">");
		sb.append("</query>");
		return sb.toString();

	}

	private String send() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:user\" event=\"sign\">");
		sb.append("</query>");
		return sb.toString();
	}

}
