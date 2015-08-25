package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-13 下午12:42:37
 * @Description 1.0
 */
public class UpdateAkpIQ extends IQ {
	private String name;
	private String url;
	private String code;
	private int oldcode;
	private String ver;
	private String errorCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getOldcode() {
		return oldcode;
	}

	public void setOldcode(int oldcode) {
		this.oldcode = oldcode;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String getChildElementXML() {

		if (this.getType().equals(IQ.Type.GET)) {
			return send();
		}
		if (this.getType().equals(IQ.Type.RESULT)) {
			return result();
		}
		if (getType().equals(IQ.Type.ERROR)) {
			return getErrorCode();
		}
		return null;
	}

	private String send() {
		if (getOldcode() < 0)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:system\" event=\"newversion\">");
		sb.append("<client>android</client>");
		sb.append("<code>").append(getOldcode()).append("</code>");
		sb.append("</query>");
		return sb.toString();

	}

	private String result() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:system\" event=\"newversion\">");
		sb.append("<name>").append(StringUtils.escapeForXML(getName()))
				.append("</name>");
		sb.append("<url>").append(StringUtils.escapeForXML(getUrl()))
				.append("</url>");
		sb.append("<ver>").append(StringUtils.escapeForXML(getVer()))
				.append("</ver>");
		sb.append("<code>").append(StringUtils.escapeForXML(getCode()))
				.append("</code>");
		sb.append("</query>");

		return sb.toString();

	}

}
