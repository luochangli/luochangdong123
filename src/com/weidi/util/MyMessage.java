package com.weidi.util;

import java.util.ArrayList;

import com.weidi.bean.MyBean;

public class MyMessage {

	public int code;
	public String msg;
	
	public ArrayList<MyBean> list;

	@Override
	public String toString() {
		return "MyMessage [code=" + code + ", msg=" + msg + ", list=" + list + "]";
	}

	
	
}
