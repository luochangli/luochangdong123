package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class BindPhoneProvider implements IQProvider {
	
	public BindPhoneProvider() {
		
	}
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		BindPhone bindphone = new BindPhone();
		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) { 
				if (parser.getName().equals("error")) {
					String errorcode = parser.getAttributeValue(0);
					bindphone.setErrorCode(errorcode);
					String text = parser.nextText();
					bindphone.setErrorText(text);
				}
			}else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
                    done = true;
                }
			}
		}
		return bindphone;
	}

}
