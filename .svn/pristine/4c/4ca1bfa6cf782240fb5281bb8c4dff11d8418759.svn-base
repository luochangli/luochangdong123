package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.weidi.R.string;

import android.util.Log;

public class FrinedProvider2 implements IQProvider{

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		Log.i("TAG", "这里是FrinedProvider");
		Friend_get friendget=new Friend_get();
		int eventType = parser.getEventType();
		boolean done=false;
		while (eventType!=XmlPullParser.END_DOCUMENT) {
			if(eventType == XmlPullParser.START_TAG){
				Log.i("TAG",parser.getName());
				if(parser.getName().equals("item")){
					String type=parser.getAttributeValue(0);
					Log.i("TAG", type+"这里item属性");
					if(type=="standard"){
						boolean done2=false;
						while(!done2){
							if(parser.getName().equals("id")){
								parser.nextText();
								Log.i("TAG", parser.nextText()+"id的值");
							}else if(parser.getName().equals("username")){
								parser.nextText();
								Log.i("TAG", parser.getText()+"username的值");
							}else if(parser.getName().equals("createdatetime")){
								parser.nextText();
								Log.i("TAG", parser.getText()+"createdatetime的值");
							}else if(parser.getName().equals("content")){
								parser.nextText();
								Log.i("TAG", parser.getText()+"content的值");
							}else if(parser.getName().equals("attachment")){
								String attr=parser.getAttributeValue(0);								
								String img=parser.nextText();
								Log.i("TAG", attr+"attachment的属性");
								Log.i("TAG", img+"attachment的值");
							}else if(parser.getName().equals("reply")){
								String attr=parser.getAttributeValue(0);
								 Log.i("TAG", attr+"reply的属性");
								 if(attr=="up"){
									 parser.nextTag();
									 if(parser.getName().equals("username"))
									 {
										 parser.nextText();
									 }
									 parser.next();
									 if(parser.getName().equals("datetime"))
									 {
										 parser.nextText();
									 }
								 }
								 if(attr=="down"){
									 parser.next();
									 if(parser.getName().equals("username"))
									 {
										 parser.nextText();
									 }
									 parser.next();
									 if(parser.getName().equals("datetime"))
									 {
										 parser.nextText();
									 }
								 }
								 if(attr=="content"){
									 parser.next();
									 if(parser.getName().equals("username"))
									 {
										 parser.nextText();
									 }
									 parser.next();
									 if(parser.getName().equals("datetime"))
									 {
										 parser.nextText();
									 }
								 }
							}else if(parser.getEventType()==XmlPullParser.END_TAG){
								 if(parser.getName().equals("item")){
									 done = true; 
								 }
							}
							parser.nextTag();
							Log.i("TAG", parser.getName());
						}
					}else if(type=="link"){
						//暂时不做
					}
			}else if(eventType == XmlPullParser.END_TAG){
				 if(parser.getName().equals("item")){
					 done = true; 
				 }
			}else{	 
				parser.nextTag();
			}
		  }else {
			  parser.next();
		  }
	   }
		return friendget;
	}
}
	


