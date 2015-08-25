package com.weidi.provider.sign;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-23 下午4:48:29
 * @Description 1.0
 */
public class IntegrationIQ extends IQ {

	private String integral;
	private String errorCode;

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String getChildElementXML() {
		if (getType().equals(IQ.Type.GET)) {
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
		sb.append("<query xmlns=\"com:jsm:user\" event=\"integral\">");
		sb.append("<integral>").append(StringUtils.escapeForXML(getIntegral()))
				.append("</integral>");
		sb.append("</query>");
		return sb.toString();

	}

	private String send() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:user\" event=\"integral\">");
		sb.append("</query>");

		return sb.toString();
	}

}
