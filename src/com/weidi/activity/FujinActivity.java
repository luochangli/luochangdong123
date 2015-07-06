package com.weidi.activity;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.adapter.NearAdapter;
import com.weidi.provider.Near;
import com.weidi.provider.Near.Item;
import com.weidi.provider.NearPeopleIQ;
import com.weidi.provider.NearTime;
import com.weidi.util.NearUser;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;


public class FujinActivity extends BaseActivity{

	private static String TAG = "FujinActivity";
	private ListView mListView;
	private ImageView tv_add,btn_back;
	private Context mContext;
	private double latitude;
	private double longitude;
	private List<Item> nearUsers = null;
	private NearUser nearUser = null;
	private LocationManager locationManager;
	//定时循环
	private Handler handler = new Handler();
	private Runnable task = new Runnable() {
		public void run() {
			handler.postDelayed(this, 6000*1000);
			//向服务器报告位置
//			sendPosition();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearpeople);
		mContext = this;
		initView();
		initData();
		QApp.getInstance().addActivity(FujinActivity.this);
	}
	private void locationLoad() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
			getLocation();
		}else{
			toggleGPS();
		}
	}
	private void getLocation() {
		Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
		if (location != null) {
			latitude = location.getLatitude();  
			longitude = location.getLongitude();
			Log.i(TAG, latitude+"-1");
			Log.i(TAG, longitude+"-1");
			QApp.latitude =latitude;
			QApp.longitude = longitude;
			
		}else {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
		}
	}
	
	private void toggleGPS() {
		Intent  gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(this, 0, gpsIntent, 0).send();
		} catch (Exception e) {
			e.printStackTrace();
			locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
			Location location1 = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
			if (location1 != null) {
				latitude = location1.getLatitude();  
				longitude = location1.getLongitude();
				Log.i(TAG, latitude+"-1");
				Log.i(TAG, longitude+"-1");
				QApp.latitude =latitude;
				QApp.longitude = longitude;
			}
		}
	}
	protected void sendPosition() {
		NearPeopleIQ nearPeopleIQ = new NearPeopleIQ();
		nearPeopleIQ.setType(IQ.Type.SET);
		nearPeopleIQ.setXmlns("com:jsm:latandlon:set");
		nearPeopleIQ.setLat(QApp.latitude);
		nearPeopleIQ.setLon(QApp.longitude);
		nearPeopleIQ.setFrom("");
		nearPeopleIQ.setTo("");
		Log.i(TAG, nearPeopleIQ.toXML());
		PacketFilter filter = new AndFilter(new PacketIDFilter(nearPeopleIQ.getPacketID()),
				new PacketTypeFilter(NearTime.class));
		PacketCollector collector = QApp.xmppConnection.createPacketCollector(filter);
		QApp.xmppConnection.sendPacket(nearPeopleIQ);
		NearTime result = (NearTime) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
		collector.cancel();
		Log.i(TAG, result.toXML());
	}
	LocationListener locationListener = new LocationListener() {  

		// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数  
		@Override  
		public void onStatusChanged(String provider, int status, Bundle extras) {  

		}  

		// Provider被enable时触发此函数，比如GPS被打开  
		@Override  
		public void onProviderEnabled(String provider) {  

		}  

		// Provider被disable时触发此函数，比如GPS被关闭   
		@Override  
		public void onProviderDisabled(String provider) {  

		}  

		//当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发   
		@Override  
		public void onLocationChanged(Location location) {  
			if (location != null) {     
				Log.i("Map", "Location changed : Lat: "    
						+ location.getLatitude() + " Lng: "    
						+ location.getLongitude()); 
				latitude = location.getLatitude();  
				longitude = location.getLongitude();
				Log.i(TAG, latitude+"-1");
				Log.i(TAG, longitude+"-1");
				QApp.latitude =latitude;
				QApp.longitude = longitude;
			}  
		}  
	};
	private void initData() {
		locationLoad();
//		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);  
//		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
//			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  
//			if(location != null){  
//				latitude = location.getLatitude();  
//				longitude = location.getLongitude(); 
//				Log.i(TAG, latitude+"");
//				Log.i(TAG, longitude+"");
//				latitude = QApp.latitude;
//				longitude = QApp.longitude;
//			}  
//		}else{  
//			LocationListener locationListener = new LocationListener() {  
//
//				// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数  
//				@Override  
//				public void onStatusChanged(String provider, int status, Bundle extras) {  
//
//				}  
//
//				// Provider被enable时触发此函数，比如GPS被打开  
//				@Override  
//				public void onProviderEnabled(String provider) {  
//
//				}  
//
//				// Provider被disable时触发此函数，比如GPS被关闭   
//				@Override  
//				public void onProviderDisabled(String provider) {  
//
//				}  
//
//				//当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发   
//				@Override  
//				public void onLocationChanged(Location location) {  
//					if (location != null) {     
//						Log.i("Map", "Location changed : Lat: "    
//								+ location.getLatitude() + " Lng: "    
//								+ location.getLongitude());     
//					}  
//				}  
//			};  
//			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);     
//			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);     
//			if(location != null){     
//				latitude = location.getLatitude(); //经度     
//				longitude = location.getLongitude(); //纬度  
//				latitude = QApp.latitude;
//				longitude = QApp.longitude;
//				Log.i(TAG, latitude+"");
//				Log.i(TAG, longitude+"");
//				//发送IQ请求
//			}     
//		}  
		QApp.sendPosition();
		Near nearPeople = sendNearIQ();
		nearUsers = (List<Item>) nearPeople.getNearItems();
		List<Item> items = new ArrayList<Near.Item>();
		String weidi = PreferencesUtils.getSharePreStr("weidi");
		for(Item item : nearUsers){
			String itemName = null;
			Double itemLat = null;
			Double itemLon = null;
			if (!(item.getUsername().equals(weidi))) {
				itemName = item.getUsername();
				itemLat = item.getLat();
				itemLon = item.getLon();
				Item item2 = new Item(itemName, itemLat, itemLon);
				items.add(item2);
				item2 = null;
				Log.i(TAG, item.getUsername());
				Log.i(TAG, item.getLat()+"");
				Log.i(TAG, item.getLon()+"");
			}
		}
		if (items.size() > 0) {
			NearAdapter nearAdapter = new NearAdapter(mContext, items);
			mListView.setAdapter(nearAdapter);
		}else {
			ToastUtil.showShortToast(this, "没有数据");
	
		}
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ListView listView = (ListView) parent;
				Item item = (Item) listView.getItemAtPosition(position);
				String weidi = item.getUsername();
				
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	

	private Near sendNearIQ(){

		NearPeopleIQ nearPeopleIQ = new NearPeopleIQ();
		nearPeopleIQ.setType(IQ.Type.GET);
		nearPeopleIQ.setLat(QApp.latitude);
		nearPeopleIQ.setLon(QApp.longitude);
		nearPeopleIQ.setDist(1);
		nearPeopleIQ.setXmlns("com:jsm:latandlon:query");
		nearPeopleIQ.setFrom("");
		nearPeopleIQ.setTo("");
		Log.i(TAG, nearPeopleIQ.toXML());
		PacketFilter filter = new AndFilter(new PacketIDFilter(nearPeopleIQ.getPacketID()),
				new PacketTypeFilter(Near.class));
		PacketCollector collector = QApp.xmppConnection.createPacketCollector(  
				filter);
		QApp.xmppConnection.sendPacket(nearPeopleIQ);
		Near result = (Near) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
		collector.cancel();
		nearUsers = (List<Item>) result.getNearItems();
		Log.i(TAG, result.toXML());
		Log.i(TAG, nearUsers.size()+"");
		return result;
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.near_listview);
		tv_add = (ImageView) findViewById(R.id.tv_add);
		btn_back = (ImageView) findViewById(R.id.near_iv_back);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
