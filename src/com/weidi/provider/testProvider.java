package com.weidi.provider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.weidi.util.Const;

import android.util.Log;

public class testProvider implements IQProvider{

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		NewsIQ qq=new NewsIQ();
		boolean done = false;
		
		Const.notice_data=new ArrayList<Map<String, Object>>();
		while (!done) {
			int eventType = parser.next();
			Log.i("TAG", parser.getName());
			if (eventType == XmlPullParser.START_TAG) {
				if(parser.getName().equals("item"))
				{
					Const.map=new HashMap<String, Object>();
				}else if (parser.getName().equals("id")) {
					String id = parser.nextText();
					qq.setId(id);
				}else if(parser.getName().equals("title")){
					String title = parser.nextText();
					qq.setTitle(title);
					Const.map.put("title", title);
				}else if(parser.getName().equals("link")){
					String link = parser.nextText();
					qq.setLink(link);
					Const.map.put("link", link);
				}else if(parser.getName().equals("imglink")){
					String imglink = parser.nextText();
					qq.setImglink(imglink);
					Const.map.put("imglink", imglink);
				}else if(parser.getName().equals("createdatetime")){
					
					String createdatetime = parser.nextText();
					createdatetime=createdatetime.substring(0, createdatetime.indexOf("."));
					createdatetime=createdatetime.replace("T", "   ");
					qq.setCreatedatetime(createdatetime);
					Const.map.put("createdatetime", createdatetime);
					Const.notice_data.add(Const.map);
					
				}
			}else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("query")) {
                    done = true;
                }
			}
		}
		
		return qq;
	}

}
