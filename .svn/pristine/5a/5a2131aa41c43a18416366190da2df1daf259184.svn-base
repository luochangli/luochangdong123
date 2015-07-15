package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-7-13 下午3:21:29
 *@Description 1.0 	通过手机号获取微迪号
 */
public class GetWeidiIQ extends IQ{

	private String phone;
	private String account;
	
	@Override
	public String getChildElementXML() {
		if (this.getType().equals(IQ.Type.GET)) {
			return send();
		}
		if (this.getType().equals(IQ.Type.RESULT)) {
			return getresult();
		}
		return null;
	}

	private String getresult() {
		// TODO Auto-generated method stub
		return null;
	}

	private String send() {
	   StringBuilder suf = new StringBuilder();
	   suf.append("  <query xmlns=\"com:jsm:user\" event=\"getaccountbyphone\">");
		return null;
	}

	
}
