package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

public class ObtainMUCDestroyProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		ObtainMUCDestroyIQ destroy=new ObtainMUCDestroyIQ();
		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			Log.i("TAG", parser.getName()+"这里是destroy"+parser.getText()+parser.getAttributeValue(0));
			if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
					done = true;
				}
			}
		}
		return destroy;
	}

}
