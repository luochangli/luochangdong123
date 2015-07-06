package com.weidi.util;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.weidi.MainActivity;
import com.weidi.QApp;
import com.weidi.R;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-6-26 上午11:32:52
 *@Description 1.0
 */
public class MyAndroidUtil {
	private static Notification myNoti = new Notification();
	/**
	 * @param context
	 * @param title
	 * @param message
	 * @param icon
	 * @param okBtn
	 * 没有取消功能的了
	 */
	public static void showDialog(Context context ,String title,String message,int icon,DialogInterface.OnClickListener okBtn){
		new AlertDialog.Builder(context)
		.setTitle(title)
		.setIcon(icon)
		.setMessage(message)
		.setPositiveButton("确定",okBtn)
		.setNegativeButton("返回", null).show();
	}
	
	
	public static void clearNoti(){
		myNoti.number = 0;
		NotificationManager manger = (NotificationManager)QApp.getInstance()
				.getSystemService(Service.NOTIFICATION_SERVICE);
		manger.cancelAll();   
	}
	
	public static void showNoti(String notiMsg){
		//android推送
		if(notiMsg.contains(Const.MSG_TYPE_IMG))
			myNoti.tickerText = "[图片]";
		else if(notiMsg.contains(Const.MSG_TYPE_VOICE))
			myNoti.tickerText = "[语音]";
		else if(notiMsg.contains("[/g0"))
			myNoti.tickerText = "[动画表情]";
//		else if(notiMsg.contains("[/f0"))  //适配表情
//			myNoti.tickerText = ExpressionUtil.getText(MyApplication.getInstance(), StringUtil.Unicode2GBK(notiMsg));
		else if(notiMsg.contains("[/a0"))
			myNoti.tickerText = "[位置]";
		else{
			myNoti.tickerText = notiMsg;
		}
		
		Intent intent = new Intent();   //要跳去的界面
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(QApp.getInstance(), MainActivity.class);
		
		NotificationManager mNotificationManager = 
	    		(NotificationManager) QApp.getInstance().getSystemService(Service.NOTIFICATION_SERVICE);
		PendingIntent appIntent = PendingIntent.getActivity(QApp.getInstance(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		myNoti.icon = R.drawable.ic_launcher;
		myNoti.flags = Notification.FLAG_SHOW_LIGHTS|Notification.FLAG_AUTO_CANCEL;  //闪光灯
		myNoti.ledARGB= 0xff00ff00;           //绿色
		//myNoti.number = NewMsgDbHelper.getInstance(MyApplication.getInstance()).getMsgCount();
		
//		if (MyApplication.sharedPreferences.getBoolean("isShake", true)) {
//			myNoti.defaults = Notification.DEFAULT_VIBRATE; // 震动
//		}
//		if (MyApplication.sharedPreferences.getBoolean("isSound", true)) {
//			myNoti.defaults = Notification.DEFAULT_SOUND; // 响铃
//		}
//		myNoti.setLatestEventInfo(MyApplication.getInstance(), MyApplication.getInstance().getString(R.string.app_name), myNoti.tickerText, appIntent);
//		mNotificationManager.notify(0, myNoti);
	}
}
