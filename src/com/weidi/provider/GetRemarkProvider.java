package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.weidi.util.Logger;

import android.util.Log;

public class GetRemarkProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		GetRemarkIQ iq = new GetRemarkIQ();

		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("username")) {
					String username = parser.nextText();
					iq.setUsername(username);
				}
				if (parser.getName().equals("nickname")) {
					iq.setNickname(parser.nextText());
				}
				if (parser.getName().equals("error")) {
				
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
