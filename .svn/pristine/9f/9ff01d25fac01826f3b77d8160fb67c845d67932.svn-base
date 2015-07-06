package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class GetPhoneProvider implements IQProvider{

	public GetPhoneProvider() {

	}
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		GetPhone getPhone = new GetPhone();

		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) { 
				if (parser.getName().equals("phone")) {
					String phone = parser.getAttributeValue(0);
					getPhone.setPhone(phone);
				}
			}else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
					done = true;
				}
			}
		}
		return getPhone;
	}
}
