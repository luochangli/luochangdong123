package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

public class GetBindPhoneProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		GetBindPhoneIQ getbindphone=new GetBindPhoneIQ();
		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			Log.i("TAG", parser.getName());
			if(eventType==XmlPullParser.START_TAG){ 
				if(parser.getName().equals("phone")){
					String phone=parser.nextText();
					getbindphone.setPhone(phone);
					Log.i("TAG", "made");
					
				}
			}else if (eventType==XmlPullParser.END_TAG) {
				if(parser.getName().equals("query")){
					done = true;
				}
				}
		}
		return getbindphone;
	}

}
