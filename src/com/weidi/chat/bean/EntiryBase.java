package com.weidi.chat.bean;
/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-8-10 下午2:08:14
 *@Description 1.0
 */
public abstract class EntiryBase {
	
	public static final String ID = "_id";
	public static final String ME = "me";
	
	protected int _id;
	protected String me;
	
	

	public String getMe() {
		return me;
	}

	public void setMe(String me) {
		this.me = me;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}
	
	

}
