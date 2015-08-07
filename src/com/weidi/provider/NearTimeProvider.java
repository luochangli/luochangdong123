package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class NearTimeProvider implements IQProvider{

	public NearTimeProvider() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		NearTime nearTime = new NearTime();
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) { 
				if (parser.getName().equals("error")) {
					String errorcode = parser.getAttributeValue(0);
					nearTime.setErrorCode(errorcode);
					String text = parser.nextText();
					nearTime.setErrorText(text);
				}
			}else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
                    done = true;
                }
			}
		}
		return nearTime;
	
	}

}
