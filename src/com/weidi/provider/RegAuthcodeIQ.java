package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-18 下午5:10:05
 * @Description 1.0 注册验证码
 */
public class RegAuthcodeIQ extends IQ {

	private String phone;
    private String errorCode;
    
    
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String getChildElementXML() {
		if (getType().equals(IQ.Type.GET)) {
			return send();
		}
		if (getType().equals(IQ.Type.RESULT)) {
			return getResult();
		}
		if(getType().equals(IQ.Type.ERROR)){
			return getErrorCode();
		}
		return null;
	}

	private String getResult() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:user\" event=\"authcode\">");
		return sb.toString();
	}

	private String send() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:user\" event=\"authcode\">");
		sb.append("<phone>").append(getPhone()).append("</phone>");
		sb.append("</query>");

		return sb.toString();
	}

}
