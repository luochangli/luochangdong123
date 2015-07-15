package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 上午11:27:05
 * @Description 1.0 获取指定群信息
 */
public class GetPasswordHintIQ extends IQ {

	private String passwordhint;
	private String account;
	private String errorCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getPasswordhint() {
		return passwordhint;
	}

	public void setPasswordhint(String passwordhint) {
		this.passwordhint = passwordhint;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public String getChildElementXML() {
		if (this.getType().equals(IQ.Type.GET)) {
			return send();
		}
		if (this.getType().equals(IQ.Type.RESULT)) {
			return getresult();
		}
		if(this.getType().equals(IQ.Type.ERROR)){
			return getErrorCode();
		}
		return null;
	}

	private String getresult() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:user\" event=\"getpasswordhint\">");
		if (getPasswordhint() != null) {
			sb.append("<passwordhint>")
					.append(StringUtils.escapeForXML(getPasswordhint()))
					.append("</passwordhint>");
		}
		sb.append("</query>");
		return sb.toString();
	}

	private String send() {
		if (getAccount() == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:user\" event=\"getpasswordhint\">");
		sb.append("<account>").append(StringUtils.escapeForXML(getAccount()))
				.append("</account>");
		sb.append("</query>");
		return sb.toString();
	}

}
