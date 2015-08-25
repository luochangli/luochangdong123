package com.weidi.provider.sign;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-23 下午4:53:31
 * @Description 1.0
 */
public class IntegrationProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		IntegrationIQ iq = new IntegrationIQ();

		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("integral")) {
					iq.setIntegral(parser.nextText());
				}
				if (parser.getName().equals("error")) {
					iq.setErrorCode(parser.getAttributeValue(0));
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
					done = true;
				}
			}
		}
		return iq;
	}

}
