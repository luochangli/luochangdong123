package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

public class GetFriend_ReMarkProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		GetFriend_ReMarkIQ remark=new GetFriend_ReMarkIQ();
		
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			Log.i("TAG", parser.getName());
			if(eventType==XmlPullParser.START_TAG){ 
				if(parser.getName().equals("username")){
					String username=parser.nextText();
					remark.setUsername(username);
					Log.i("TAG", "made");
					
				}else if(parser.getName().equals("nickname")){
					remark.setNickname(parser.nextText());
					Log.i("TAG", parser.nextText());
				}else if(parser.getName().equals("error")){
					remark.setAttr_error(parser.getAttributeValue(0));
					remark.setmyError(parser.nextText());
					Log.i("TAG", parser.nextText());
				}
			}else if (eventType==XmlPullParser.END_TAG) {
				if(parser.getName().equals("query")){
					done = true;
				}
				}
			
		}
		return remark;
	}

}
