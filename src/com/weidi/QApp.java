package com.weidi;

import java.util.ArrayList;
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
import org.jivesoftware.smack.packet.Packet;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.thinkland.sdk.util.CommonFun;
import com.weidi.provider.BindPhone;
import com.weidi.provider.BindPhoneIQ;
import com.weidi.provider.GetPhone;
import com.weidi.provider.GetPhoneIQ;
import com.weidi.provider.NearPeopleIQ;
import com.weidi.provider.NearTime;
import com.weidi.util.PreferencesUtils;

public class QApp extends Application{

	public static XMPPConnection xmppConnection;
	public static double latitude;
	public static double longitude;
	private static QApp mInstance;
	public static SharedPreferences sharedPreferences;
	private static LocationManager locationManager;
	public static List activityList=new ArrayList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		sharedPreferences = getSharedPreferences(PreferencesUtils.WEIDI, Context.MODE_PRIVATE);
		//初始化图片加载器相关配置
		initImageLoader(getApplicationContext());
		CommonFun.initialize(getApplicationContext(), false);
		mInstance = this;

	}
	public static QApp getInstance(){
		return mInstance;
	}
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程的优先级
		.denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
		.discCacheFileNameGenerator(new Md5FileNameGenerator())// 设置缓存文件的名字
		.discCacheFileCount(60)// 缓存文件的最大个数
		.tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
		.build();
		ImageLoader.getInstance().init(config);
	}
	/**
	 * 手机号转成微迪号
	 * @param phone
	 * @return
	 */
	public static String initPhoneToWei(String phone){
//		String url = "http://192.168.0.10:9090/plugins/jsmuser/getaccountbyphone?phone="+phone+"&type=get";
		String url = "http://"+xmppConnection.getHost()+":9090/plugins/jsmuser/getaccountbyphone?phone="+phone+"&type=get";
		Log.i("TAG", url);
		HttpGet httpRequest = new HttpGet(url);
		String result = null;
		try{  
			/*发送请求并等待响应*/  
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);  
			/*若状态码为200 ok*/  
			if(httpResponse.getStatusLine().getStatusCode() == 200){    
				/*读*/  
				result = EntityUtils.toString(httpResponse.getEntity());  
				Log.i("TAG",result);  
			} 
			else{  
				Log.i("TAG", "Error Response: "+httpResponse.getStatusLine().toString());  
			}  
		}  
		catch (Exception e){   
			e.printStackTrace();  
		}
		return result;
	}
	/**
	 * 微迪号转成手机号
	 */
	public static String initWeidiToPhone(String Weidi){

		return null;
	}
	/**
	 * 绑定手机
	 * @param phone
	 * @return
	 */
	public static BindPhone initBindPhone(String phone){

		//发送绑定手机IQ包
		BindPhoneIQ bindPhoneIQ = new BindPhoneIQ();
		bindPhoneIQ.setType(IQ.Type.SET);
		bindPhoneIQ.setXmlns("com:jsm:bindphone");
		bindPhoneIQ.setPhone(phone);

		//		PacketCollector collector = QApp.xmppConnection.createPacketCollector(new  PacketFilter() {
		//			@Override
		//			public boolean accept(Packet p) {
		//				System.out.println("accept packet xml = " + p.toXML());
		//				if (p instanceof BindPhone) {
		//					BindPhone m = (BindPhone) p;
		//					System.out.println(p.toXML());
		//					System.out.println(m.toXML());
		//				}
		//				return false;
		//			}
		//		});
		//手机绑定过滤器
		PacketFilter filter = new AndFilter(new PacketIDFilter(bindPhoneIQ.getPacketID()),
				new PacketTypeFilter(BindPhone.class));
		PacketCollector collector = QApp.xmppConnection.createPacketCollector(  
				filter);
		Log.i("TAG", bindPhoneIQ.toXML());
		QApp.xmppConnection.sendPacket(bindPhoneIQ);
		BindPhone result = (BindPhone) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
		collector.cancel();
		return result;
	}
	/**
	 * 获取手机号
	 * @return
	 */
	public static String initGetBindPhone(){
		//发送获取手机IQ包
		GetPhoneIQ getPhoneIQ = new GetPhoneIQ();
		getPhoneIQ.setType(IQ.Type.GET);
		getPhoneIQ.setXmlns("com:jsm:getbindingphone");

		//		xmppConnection.sendPacket(getPhoneIQ);
		//		PacketCollector collector1 = xmppConnection.createPacketCollector(new PacketFilter() {
		//			@Override
		//			public boolean accept(Packet p) {
		//				System.out.println("accept packet xml = " + p.toXML());
		//				if (p instanceof GetPhone) {
		//					GetPhone m = (GetPhone) p;
		//					System.out.println(p.toXML());
		//					System.out.println(m.toXML());
		//				}
		//				return false;
		//			}
		//		});
		//
		//		return null;
		PacketFilter filter = new AndFilter(new PacketIDFilter(getPhoneIQ.getPacketID()),
				new PacketTypeFilter(GetPhone.class));
		PacketCollector collector = xmppConnection.createPacketCollector(  
				filter);
		Log.i("TAG", getPhoneIQ.toXML());
		xmppConnection.sendPacket(getPhoneIQ);
		GetPhone result = (GetPhone) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
		collector.cancel();
		if (result == null) {
			return null;
		}else {
			Log.i("TAG", result.toXML());
			String phone = result.getPhone();
			return phone;
		}
	}

	public static String getBindPhone(){
		//发送获取手机IQ包
		GetPhoneIQ getPhoneIQ = new GetPhoneIQ();
		getPhoneIQ.setType(IQ.Type.GET);
		getPhoneIQ.setXmlns("com:jsm:getbindingphone");

		PacketCollector collector = xmppConnection	.createPacketCollector(new 
				PacketFilter() {public boolean accept(Packet p) {	
					System.out.println("accept packet xml = " + p.toXML());						
					if (p instanceof GetPhone) {		
						GetPhone m = (GetPhone) p;				
						System.out.println(p.toXML());						
						System.out.println(m.toXML());							
						return true;
					}					
					return false;
				}
		});	
		xmppConnection.sendPacket(getPhoneIQ);		
		GetPhone packet = (GetPhone) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());	
		collector.cancel();		
		if (packet == null) {	

		} else {		
			System.out.println("final packet xml = " + packet.toXML());		
		}		
		return packet.getFrom();
	}
	/**
	 * 向服务器报告自己的位置
	 */
	public static void sendPosition(){
		NearPeopleIQ nearPeopleIQ = new NearPeopleIQ();
		nearPeopleIQ.setType(IQ.Type.SET);
		nearPeopleIQ.setXmlns("com:jsm:latandlon:set");
		nearPeopleIQ.setLat(QApp.latitude);
		nearPeopleIQ.setLon(QApp.longitude);
		nearPeopleIQ.setFrom("");
		nearPeopleIQ.setTo("");
		Log.i("TAG", nearPeopleIQ.toXML());

		PacketFilter filter = new AndFilter(new PacketIDFilter(nearPeopleIQ.getPacketID()),
				new PacketTypeFilter(NearTime.class));
		PacketCollector collector = QApp.xmppConnection.createPacketCollector(filter);
		QApp.xmppConnection.sendPacket(nearPeopleIQ);
		NearTime result = (NearTime) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
		collector.cancel();
		Log.i("TAG", result.toXML());
	}
	
	public void addActivity(Activity activity)
	 {
	    activityList.add(activity);
	 }

	public void activity_exit()
	 {

	 for(int i=activityList.size();i>=0;i--)
	 {
		 Activity activity=(Activity)activityList.get(i);
		 activity.finish();
		
	 }
	 System.exit(0);
	

	 }


	
}


