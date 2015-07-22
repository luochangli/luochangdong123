package com.weidi.provider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

import android.util.Log;

import com.weidi.provider.ObtainMUCmemberIQ.Item;
import com.weidi.util.Const;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-8 上午10:23:23
 * @Description 1.0
 */
public class NewsIQ extends IQ {

	private String datetime;
	private String id;
	private String title;
	private String link;
	private String imglink;
	private String createdatetime;
	
	
	
	
	public NewsIQ() {
		super();
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImglink() {
		return imglink;
	}

	public void setImglink(String imglink) {
		this.imglink = imglink;
	}

	public String getCreatedatetime() {
		return createdatetime;
	}

	public void setCreatedatetime(String createdatetime) {
		this.createdatetime = createdatetime;
	}

	private List<NewsIQ.Item> items = new ArrayList<NewsIQ.Item>();

	public List<Item> getItems() {
		synchronized (items) {
			return items;
		}
	}

	public void addItem(Item item) {
		synchronized (items) {
			items.add(item);
		}
	}

	
	private String getResult() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("query xmlns=\"com:jsm:news\" event=\"news\">");
		sb.append("<items>");
		synchronized (items) {
			for (int i = 0; i < items.size(); i++) {
				Item item = items.get(i);
				sb.append(item.toXML());
			}
		}
		sb.append("</items>");
		sb.append("</query>");
		Log.i("uuuuuu", ""+Const.notice_data.size());
		return sb.toString();
	}

	private String getSet() {
		StringBuilder sb = new StringBuilder();
		sb.append("<query xmlns=\"com:jsm:news\" event=\"news\">");
		/*sb.append("<datetime>").append(StringUtils.escapeForXML(getDatetime()))
				.append("</datetime>");*/
		sb.append("</query>");
		return sb.toString();
	}

	@Override
	public String getChildElementXML() {
		if (this.getType() == IQ.Type.RESULT) {
			return getResult();
		} else if (this.getType() == IQ.Type.GET) {
			return getSet();
		} else {
			return "";
		}
	}
	
	
	public static class Item implements Serializable {
		
		private static final long serialVersionUID = -400318560012436985L;
		private String id;
		private String title;
		private String link;
		private String imglink;
		private String cratedatetime;

		

		public String getId() {
			return id;
		}



		public void setId(String id) {
			this.id = id;
		}



		public String getTitle() {
			return title;
		}



		public void setTitle(String title) {
			this.title = title;
		}



		public String getLink() {
			return link;
		}



		public void setLink(String link) {
			this.link = link;
		}



		public String getImglink() {
			return imglink;
		}



		public void setImglink(String imglink) {
			this.imglink = imglink;
		}



		public String getCratedatetime() {
			return cratedatetime;
		}



		public void setCratedatetime(String cratedatetime) {
			this.cratedatetime = cratedatetime;
		}



		public String toXML() {
			StringBuilder sb = new StringBuilder();

			sb.append("<item>").append("<id>")
					.append(StringUtils.escapeForXML(getId()))
					.append("</id>");
			sb.append("<title>").append(StringUtils.escapeForXML(getTitle()))
					.append("</title>");
			sb.append("<link>")
					.append(StringUtils.escapeForXML(getLink()))
					.append("</link>");
			sb.append("<imglink>").append(StringUtils.escapeForXML(getLink()))
					.append("</imglink>");
			sb.append("<createdatetime>").append(StringUtils.escapeForXML(getCratedatetime()))
			.append("</createdatetime>").append("</item>");
			return sb.toString();

		}
	}

}
