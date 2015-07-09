package com.weidi.chat;
/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-7-9 下午2:54:45
 *@Description 1.0
 */
public class GroupMenber {

	private String userjid;
	private String usernick;
	private String affiliation;//管理员 10，普通成员 30
	
	public String getUserjid() {
		return userjid;
	}
	public void setUserjid(String userjid) {
		this.userjid = userjid;
	}
	public String getUsernick() {
		return usernick;
	}
	public void setUsernick(String usernick) {
		this.usernick = usernick;
	}
	public String getAffiliation() {
		return affiliation;
	}
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	
	
}
