package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-7-17 下午4:40:13
 *@Description 1.0 意见反馈
 */
public class SuggestIQ extends IQ{

	private String suggest;
	
	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	@Override
	public String getChildElementXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:suggest\" event= \"suggest\">");
		sb.append("<suggest>").append(getSuggest()).append("</suggest>");
		sb.append("</query>");
		return sb.toString();
	}

}
