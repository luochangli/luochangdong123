package com.weidi.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;

import android.util.Log;

import com.weidi.provider.Near.NearBean;

public class Friend_get extends IQ{
	public static final String ELEMENT_NAME = "query";
	public static final String NAMESPACE = "com:jsm:moment";
	private final List<NearBean> friendItems = new ArrayList<NearBean>();
	private String id;
	private String createdatetime;
	private String content;
	private String attachment;
	private String username;
	private double datetime;

	public void addFriendItem(NearBean item) {
		synchronized (friendItems) {
			friendItems.add(item);
		}
	}

	public Collection<NearBean> getFriendItems() {
		synchronized (friendItems) {
			return Collections.unmodifiableList(new ArrayList<NearBean>(friendItems));
		}
	}
	public String getcreatedatetime() {
		return createdatetime;
	}

	public void setcreatedatetime(String createdatetime) {
		this.createdatetime = createdatetime;
	}
	
	public String getid() {
		return id;
	}

	public void setid(String id) {
		this.id = id;
	}
	
	
	public String getattachment() {
		return attachment;
	}

	public void setattachment(String attachment) {
		this.attachment = attachment;
	}
	
	
	public String getcontent() {
		return content;
	}

	public void setcontent(String content) {
		this.content = content;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public double getDatetime() {
		return datetime;
	}

	public void setDatetime(double datetime) {
		this.datetime = datetime;
	}
	
	@Override
	public String getChildElementXML() {
		// TODO Auto-generated method stub
		if (this.getType() == IQ.Type.RESULT) {
			//return getChildElementXMLForResult();
			Log.i("TAG", "已经有结果了");
			return "";
		}
		else if (this.getType() == IQ.Type.GET) {
			return getChildElementXMLForSet();
		}else{
			Log.i("TAG", "看看不会不会来着");
			return "";
		}		
	}
	
	/*<iq id=”xxx” type=”get”>
	  <query xmlns=” com:jsm:moment” event=”get”>
	<username>xxx</username>
	<datetime>xxx</datetime>
	  </query>
	</iq>*/
	private String getChildElementXMLForSet(){
		StringBuilder buf = new StringBuilder();
		buf.append("<query xmlns=\"com:jsm:moment\" event=\"get\">");
		buf.append("<").append("username").append(">")
		.append(username)
		.append("<").append("/username").append(">")
		.append("<").append("datetime").append(">")
		.append("2015-07-03T05:48:36.473Z")
		.append("<").append("/datetime").append(">");
		buf.append("</query>");
		return buf.toString();
		
	}

}
