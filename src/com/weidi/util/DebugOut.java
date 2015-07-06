package com.weidi.util;

public class DebugOut {

	public static Boolean IS_DEBUG = true;
	public static void PrintlnOut(String debugString){
		if(IS_DEBUG){
			System.out.println(debugString);
		}
	}
}
