package com.weidi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.thinkland.sdk.util.CommonFun;
import com.weidi.chat.ChatGroupOrder;
import com.weidi.provider.BindPhone;
import com.weidi.provider.BindPhoneIQ;
import com.weidi.provider.Friend_save;
import com.weidi.provider.NearPeopleIQ;
import com.weidi.provider.NearTime;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.XmppConnectionManager;

public class QApp extends Application {

	public static double latitude;
	public static double longitude;
	private static QApp mInstance;
	public static SharedPreferences sharedPreferences;
	private static LocationManager locationManager;
	private static List activityList = new LinkedList();
	public static LocalBroadcastManager mLocalBroadcastManager;

	@Override
	public void onCreate() {
		super.onCreate();
		sharedPreferences = getSharedPreferences(PreferencesUtils.WEIDI,
				Context.MODE_PRIVATE);
		getXmppConnection();
		// 初始化图片加载器相关配置
		initImageLoader(getApplicationContext());
		CommonFun.initialize(getApplicationContext(), false);
		mInstance = this;
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
	}

	public static QApp getInstance() {
		return mInstance;
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程的优先级
				.denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// 设置缓存文件的名字
				.discCacheFileCount(60)// 缓存文件的最大个数
				.tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
				.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 微迪号转成手机号
	 */
	public static String initWeidiToPhone(String Weidi) {

		return null;
	}

	public static XMPPConnection getXmppConnection() {
		return XmppConnectionManager.getInstance().getConnection();
	}

	/**
	 * 绑定手机
	 * 
	 * @param phone
	 * @return
	 */
	public static BindPhone initBindPhone(String phone) {

		// 发送绑定手机IQ包
		BindPhoneIQ bindPhoneIQ = new BindPhoneIQ();
		bindPhoneIQ.setType(IQ.Type.SET);
		bindPhoneIQ.setXmlns("com:jsm:bindphone");
		bindPhoneIQ.setPhone(phone);

		// PacketCollector collector =
		// QApp.xmppConnection.createPacketCollector(new PacketFilter() {
		// @Override
		// public boolean accept(Packet p) {
		// System.out.println("accept packet xml = " + p.toXML());
		// if (p instanceof BindPhone) {
		// BindPhone m = (BindPhone) p;
		// System.out.println(p.toXML());
		// System.out.println(m.toXML());
		// }
		// return false;
		// }
		// });
		// 手机绑定过滤器
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				bindPhoneIQ.getPacketID()), new PacketTypeFilter(
				BindPhone.class));
		PacketCollector collector = XmppConnectionManager.getInstance()
				.getConnection().createPacketCollector(filter);
		Log.i("TAG", bindPhoneIQ.toXML());
		getXmppConnection().sendPacket(bindPhoneIQ);
		BindPhone result = (BindPhone) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		collector.cancel();
		return result;
	}

	/**
	 * 向服务器报告自己的位置
	 */
	public static void sendPosition() {
		NearPeopleIQ nearPeopleIQ = new NearPeopleIQ();
		nearPeopleIQ.setType(IQ.Type.SET);
		nearPeopleIQ.setXmlns("com:jsm:latandlon:set");
		nearPeopleIQ.setLat(QApp.latitude);
		nearPeopleIQ.setLon(QApp.longitude);
		nearPeopleIQ.setFrom("");
		nearPeopleIQ.setTo("");
		Log.i("TAG", nearPeopleIQ.toXML());
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				nearPeopleIQ.getPacketID()), new PacketTypeFilter(
				NearTime.class));
		PacketCollector collector = getXmppConnection().createPacketCollector(
				filter);
		getXmppConnection().sendPacket(nearPeopleIQ);
		NearTime result = (NearTime) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		collector.cancel();
		Log.i("TAG", result.toXML());
	}

	/*
	 * public static void getFriend(){
	 * 
	 * Friend_get friendIQ = new Friend_get(); friendIQ.setType(IQ.Type.GET);
	 * friendIQ.setUsername("1000261@jsmny"); Log.i("TAG", friendIQ.toXML());
	 * PacketFilter filter = new AndFilter(new
	 * PacketIDFilter(friendIQ.getPacketID()), new
	 * PacketTypeFilter(Friend_get.class)); PacketCollector collector =
	 * QApp.xmppConnection.createPacketCollector(filter);
	 * QApp.xmppConnection.sendPacket(friendIQ); Log.i("TAG", "这里是result前面");
	 * //Friend_get result = (Friend_get)
	 * collector.nextResult(SmackConfiguration.getPacketReplyTimeout()); //
	 * Log.i("TAG", "这里是result后面"); //collector.cancel(); //Log.i("TAG",
	 * result.toXML()+"这是朋友圈的"); }
	 */
	public static void saveFriendMessage() {
		Friend_save friendsavemessage = new Friend_save();
		friendsavemessage.setType(IQ.Type.SET);
		Log.i("TAG", friendsavemessage.toXML());
		getXmppConnection().sendPacket(friendsavemessage);
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {

		for (Object activity : activityList) {
			Log.i("AA", "" + activityList.size());
			((Activity) activity).finish();
		}

	}

}
