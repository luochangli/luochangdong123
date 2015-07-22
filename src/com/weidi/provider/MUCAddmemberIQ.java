package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

import com.weidi.util.Const;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 上午11:27:05
 * @Description 1.0 获取指定群信息
 */
public class MUCAddmemberIQ extends IQ {

	private String muc;
	private String jid;
	private String nick;

	public String getMuc() {
		return muc;
	}

	public void setMuc(String muc) {
		this.muc = muc;
	}

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
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
		sb.append("<query xmlns=\"com:jsm:group\" event=\"addmember\">");
		sb.append("</query>");
		return sb.toString();
	}

	private String send() {
		if (getMuc() == null || getJid() == null || getNick() == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:group\" event=\"addmember\">");
		sb.append("<muc>").append(StringUtils.escapeForXML(getMuc()))
				.append("</muc>");
		sb.append("<jid>").append(StringUtils.escapeForXML(getJid()))
				.append("</jid>");
		sb.append("<nick>").append(StringUtils.escapeForXML(getNick()))
				.append("</nick>");
		sb.append("</query>");
		return sb.toString();
	}

}
