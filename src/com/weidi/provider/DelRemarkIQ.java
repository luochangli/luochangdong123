package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 上午11:27:05
 * @Description 1.0 获取指定群信息
 */
public class DelRemarkIQ extends IQ {

	private String username;
	private String nickname;
	private String errorCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String getChildElementXML() {
		if (this.getType().equals(IQ.Type.SET)) {
			return send();
		}
		if (this.getType().equals(IQ.Type.RESULT)) {
			return getResult();
		}
		if (this.getType().equals(IQ.Type.ERROR)) {
			return getErrorCode();
		}
		return null;

	}

	private String getResult() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:remark\" event=\"del\">");
		sb.append("</query>");
		return sb.toString();
	}

	private String send() {
		if (getUsername() == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:remark\" event=\"del\">");
		sb.append("<username>").append(StringUtils.escapeForXML(getUsername()))
				.append("</username>");
		sb.append("</query>");
		return sb.toString();
	}

}
