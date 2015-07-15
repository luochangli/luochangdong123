package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 上午11:27:05
 * @Description 1.0 获取指定群信息
 */
public class ForgetPasswordIQ extends IQ {

	private String phone;
	private String password;
	private String answer;
	

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}


	@Override
	public String getChildElementXML() {
		if (this.getType().equals(IQ.Type.SET)) {
			return send();
		}
		if (this.getType().equals(IQ.Type.RESULT)) {
			return getresult();
		}
		return null;
	}

	private String getresult() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:user\" event=\"forgetpassword\">");
		sb.append("</query>");
		return sb.toString();
	}

	private String send() {
		if (getPhone()==null||getPassword()==null||getAnswer()==null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:user\" event=\"forgetpassword\">");
		sb.append("<phone>").append(StringUtils.escapeForXML(getPhone())).append("</phone>");
		sb.append("<password>").append(StringUtils.escapeForXML(getPassword())).append("</password>");
		sb.append("<answer>").append(StringUtils.escapeForXML(getAnswer())).append("</answer>");
		sb.append("</query>");
		return sb.toString();
	}

}
