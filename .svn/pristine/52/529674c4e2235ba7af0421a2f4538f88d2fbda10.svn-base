package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

import com.weidi.util.Logger;

public class ObtainMUCCreateGroupProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		ObtainMUCCreateGroupIQ creategroup=new ObtainMUCCreateGroupIQ();
		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if(eventType==XmlPullParser.START_TAG){
				if(parser.getName().equals("muc")){
					String muc = parser.nextText();
					Logger.e("MUC", muc);
					creategroup.setMuc(muc);
				}
			}else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
					done = true;
				}
			}
		}
		Log.i("tahg", creategroup.toXML());
		return creategroup;
	}

}
