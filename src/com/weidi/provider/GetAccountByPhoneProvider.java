package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.weidi.util.Logger;

public class GetAccountByPhoneProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		GetAccountByPhoneIQ iq = new GetAccountByPhoneIQ();
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {

				if (parser.getName().equals("account")) {
					String account = parser.nextText();
					iq.setAccount(account);

				}
				if (parser.getName().equals("error")) {
				    String code = parser.getAttributeValue(0);
				    Logger.e("error", code);
					iq.setErrorCode(code);
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
