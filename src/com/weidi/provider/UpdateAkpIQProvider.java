package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-13 下午1:04:13
 * @Description 1.0
 */
public class UpdateAkpIQProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		UpdateAkpIQ iq = new UpdateAkpIQ();
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("name")) {
					String name = parser.nextText();
					iq.setName(name);
				}
				if (parser.getName().equals("error")) {
					iq.setErrorCode(parser.getAttributeValue(0));
				}
				if (parser.getName().equals("url")) {
					iq.setUrl(parser.nextText());
				}
				if (parser.getName().equals("code")) {
					iq.setCode(parser.nextText());
				}
				if (parser.getName().equals("ver")) {
					iq.setVer(parser.nextText());

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
