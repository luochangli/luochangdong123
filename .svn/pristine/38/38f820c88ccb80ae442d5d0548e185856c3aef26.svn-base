package com.weidi.util;





import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.weidi.QApp;

public class PreferencesUtils {
	/**
	 * 普通字段存放地址
	 */
	public static String  WEIDI="xmpp";
	public static Boolean isfirst;
	public static String  first="isfirst";
	static SharedPreferences sp;
	
	public static String getSharePreStr(String field){
		sp= QApp.sharedPreferences;
		String s=sp.getString(field,"");//如果该字段没对应值，则取出字符串0
		return s;
	}
	//取出whichSp中field字段对应的int类型的值
	public static int getSharePreInt(String field){
		sp= QApp.sharedPreferences;
		int i=sp.getInt(field,0);//如果该字段没对应值，则取出0
		return i;
	}
	
	//取出whichSp中field字段对应的boolean类型的值
	public static boolean getSharePreBoolean(String field){
		sp= QApp.sharedPreferences;
		boolean i=sp.getBoolean(field, false);//如果该字段没对应值，则取出0
		return i;
	}
	
	public static boolean getFirst(String field){
		sp= QApp.sharedPreferences;
		boolean i=sp.getBoolean(field, false);//如果该字段没对应值，则取出0
		return i;
	}
	/**
	 * 修改缓存
	 * @param name     一般都name+   actid、或者userId
	 * @param true or fasle        要保存的
	 */
	public static void editXml(String name,boolean is) {
		Editor editor = QApp.sharedPreferences.edit();
		editor.putBoolean(name, is);
		editor.commit();
	}
	/**
	 * 修改缓存
	 * @param name     一般都name+   actid、或者userId
	 * @param result        要保存的
	 */
	public static void editXmlByString(String name,String result) {
		Editor editor = QApp.sharedPreferences.edit();
		if (QApp.sharedPreferences.getString(name, null) != null) {
			editor.remove(name);
		}
		editor.putString(name, result);
		editor.commit();
	}
	//保存string类型的value到whichSp中的field字段
	public static void putSharePre(String field,String value){
		sp= QApp.sharedPreferences;
		sp.edit().putString(field, value).commit();
	}
	//保存int类型的value到whichSp中的field字段
	public static void putSharePre(String field,int value){
		sp= QApp.sharedPreferences;
		sp.edit().putInt(field, value).commit();
	}
	
	//保存boolean类型的value到whichSp中的field字段
	public static void putSharePre(String field,Boolean value){
		sp= QApp.sharedPreferences;
		sp.edit().putBoolean(field, value).commit();
	}
	
	//保存用户是不是第一次登陆
	public static void putFirst(String field,Boolean value){
		sp= QApp.sharedPreferences;
		sp.edit().putBoolean(field, value).commit();
	}
	
	//清空保存的数据
		public static void clearSharePre(){
			try {
				sp= QApp.sharedPreferences;
				sp.edit().clear().commit();
			} catch (Exception e) {
			}
		}
		
		
}
