package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

import android.util.Log;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 上午11:27:05
 * @Description 1.0 获取指定群信息
 */
public class GetFriend_ReMarkIQ extends IQ {

	private String username;
	private String nickname;
	private String myerror;
	private String attr_error;

	

	public String getmyError() {
		return myerror;
	}

	public void setmyError(String myerror) {
		this.myerror = myerror;
	}

	public String getAttr_error() {
		return attr_error;
	}

	public void setAttr_error(String attr_error) {
		this.attr_error = attr_error;
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
		if (this.getType().equals(IQ.Type.GET)) {
			return send();
		}
		if (this.getType().equals(IQ.Type.RESULT)) {
			return getresult();
		}
		if(this.getType().equals(IQ.Type.ERROR)){
			Log.i("TAG", "HUILAI ERROR");
			return error();
		}
		return null;
	}

	private String getresult() {
		if(getUsername()==null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:remark\" event=\"get\">");
		sb.append("<username>").append(StringUtils.escapeForXML(getUsername())).append("</username>");
		sb.append("<vCard>");
		sb.append("<nickname>").append(StringUtils.escapeForXML(getNickname())).append("</nickname>");
		sb.append("</vCard>");
		sb.append("</query>");
		return sb.toString();
	}

	private String send() {
		if (getUsername()==null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:remark\" event=\"get\">");
		sb.append("<username>").append(StringUtils.escapeForXML(getUsername())).append("</username>");
		sb.append("</query>");
		return sb.toString();
	}
	
	private String error() {
		if (getmyError()==null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:remark\" event=\"get\">");
		sb.append("<error code=").append(StringUtils.escapeForXML(getAttr_error())).append(">").append(StringUtils.escapeForXML(getmyError())).append("</error>");
		sb.append("</query>");
		return sb.toString();
	}

}
