package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-7-18 下午5:28:22
 *@Description 1.0
 */
public class GetPwdAuthcodeProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		GetPwdAuthcodeIQ iq = new GetPwdAuthcodeIQ();

		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
					done = true;
				}
			}
		}
		return iq;
	}

}
