package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import android.util.Log;

public class Frined_save_Provider implements IQProvider{

	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		// TODO Auto-generated method stub
		Log.i("TAG", "这里是Frined_save_Provider");
		Friend_save friendsave=new Friend_save();
		int eventType = parser.getEventType();
		boolean done=false;
		while (!done) {
			if(eventType == XmlPullParser.START_TAG) {  
					if(parser.getName().equals("id")&&parser.getName()!=null){
						Log.i("TAG", parser.getName()+parser.getText());
						String id=parser.nextText();
						friendsave.setid(id);
						Log.i("TAG", id+"Frined_save_Provider");
					}
				     if(parser.getName().equals("createdatetime")&&parser.getName()!=null){
				    	 
				    	 String createdatetime=parser.nextText();
						friendsave.setcreatedatetime(createdatetime);
						Log.i("TAG", createdatetime+"Frined_save_Provider");
					}
				     if(parser.getEventType()==XmlPullParser.END_TAG){
				    	 if(parser.getName().equals("query"))
				    	 {
				    		 done=true;
				    	 }
				     }
				     parser.nextTag();				
			}		
		}
		Log.i("TAG", friendsave.toXML());
		Log.i("TAG", "能过来吗");
		return friendsave;
		
	}
	
}
