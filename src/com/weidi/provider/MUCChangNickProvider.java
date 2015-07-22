package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

public class MUCChangNickProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		MUCChangNickIQ changnick=new MUCChangNickIQ();
		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			Log.i("TAG", parser.nextText());
			if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
					done = true;
				}
			}
		}
		return changnick;
	}

}
