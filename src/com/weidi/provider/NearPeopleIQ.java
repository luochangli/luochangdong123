package com.weidi.provider;

import org.jivesoftware.smack.packet.IQ;

public class NearPeopleIQ extends IQ {

	private double lat;
	private double lon;
	private int dist;
	private String xmlns;

	public String getXmlns() {
		return xmlns;
	}



	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}



	public double getLat() {
		return lat;
	}



	public void setLat(double lat) {
		this.lat = lat;
	}



	public double getLon() {
		return lon;
	}



	public void setLon(double lon) {
		this.lon = lon;
	}



	public int getDist() {
		return dist;
	}



	public void setDist(int dist) {
		this.dist = dist;
	}



	@Override
	public String getChildElementXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<query" +  " xmlns=\"" + xmlns + "\">");
		buf.append("<lat>").append(lat).append("</lat>");
		buf.append("<lon>").append(lon).append("</lon>");
		buf.append("<dist>").append(dist).append("</dist>");
		buf.append(getExtensionsXML());
		buf.append("</query>");
		return buf.toString();
	}

}
