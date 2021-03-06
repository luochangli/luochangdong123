package com.weidi.provider;

import java.util.List;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

import com.weidi.provider.Near.NearBean;

public class NearProvider implements IQProvider{

	private String count;
	private List<NearBean> items = null;
	public NearProvider() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		Near near = new Near();
		
		boolean done = false;
		while (!done) {

			int eventType = parser.next();
			String name = parser.getName();
			if (eventType == XmlPullParser.START_TAG) { 
				if (name.equals("error")) {
					String errorcode = parser.getAttributeValue(0);
					near.setErrorCode(errorcode);
					String text = parser.nextText();
					near.setErrorText(text);
				}
				if (name.equals("items")) {
					count = parser.getAttributeValue(0);
					Log.i("TAG", "COUNT = "+count);
					near.setCounttext(count);
				}
				if (name.equals("item")) {
					int cou = Integer.valueOf(count);
					for (int i = cou; i > 0; i--) {
						String username = null;
						Double lat = null;
						Double lon = null;
						for (int j = 0; j < 3; j++) {
							parser.nextTag();
							name = parser.getName();
							if (name.equals("username")) {
								username = parser.nextText();
								near.setUsername(username);
								Log.i("TAG",username);
							}
							if (name.equals("lat")) {
								lat = Double.valueOf(parser.nextText());
								near.setLat(lat);
								//Log.i("TAG",parser.getName()+"="+ parser.nextText());
							}
							if (name.equals("lon")) {
								lon = Double.valueOf(parser.nextText());
								near.setLon(lon);
								//Log.i("TAG",parser.getName()+"="+ parser.nextText());
							}
						}
						if (username !=null && lat != null && lon != null) {
							Near.NearBean item = new NearBean(username, lat, lon);
							near.addNearItem(item);
						}
						parser.nextTag();
						parser.nextTag();
					}
				}
			}else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
					done = true;
				}
			}
		}
		Log.i("TAG", "++"+near.toXML());
		return near;
	}
}
