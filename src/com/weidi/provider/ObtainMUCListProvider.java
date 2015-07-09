package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.weidi.util.Logger;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-7-9 上午10:41:16
 *@Description 1.0
 */
public class ObtainMUCListProvider implements IQProvider{

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		
		ObtainMUCListIQ muclist = new ObtainMUCListIQ();
		boolean done = false;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) { 
				  if (parser.getName().equals("item")) {
					  muclist.addItem(parseItem(parser));
	                }
			}else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
                    done = true;
                }
			}
		}
		return muclist;
	}
	
	   private ObtainMUCListIQ.Item parseItem(XmlPullParser parser) throws Exception {
	        boolean done = false;
	        ObtainMUCListIQ.Item item =  new ObtainMUCListIQ.Item();
	     
	        while (!done) {
	            int eventType = parser.next();
	            if (eventType == XmlPullParser.START_TAG) {
	                if (parser.getName().equals("muc")) {
	                	String muc = parser.nextText();
	                	Logger.i("ObtainMUCList", muc);
	                    item.setMuc(muc);
	                }
	                if (parser.getName().equals("name")) {
	                	String name = parser.nextText();
	                    item.setName(name);
	                    Logger.i("ObtainMUCList", name);
	                }
	            }
	            else if (eventType == XmlPullParser.END_TAG) {
	                if (parser.getName().equals("item")) {
	                    done = true;
	                }
	            }
	        }
	        return item;
	    }

}
