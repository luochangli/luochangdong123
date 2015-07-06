package com.weidi.adapter;

import java.util.List;

import org.jivesoftware.smackx.packet.VCard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.bean.User;
import com.weidi.provider.Near.Item;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;

public class NearAdapter extends BaseAdapter{
	private Context mContext;
	private List<Item> lists;
	private Double self_lat = null;
	private Double self_lon = null;
	private double s;
	public NearAdapter(Context mContext, List<Item> lists) {
		super();
		this.mContext = mContext;
		this.lists = lists;
		self_lat = QApp.latitude;
		self_lon = QApp.longitude;
	}

	@Override
	public int getCount() {
		if (lists != null) {
			return lists.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.activity_near_item, null);
			
			holder = new Holder();
			holder.iv = (CircleImageView) convertView.findViewById(R.id.near_user_head);
			holder.tv_name = (TextView) convertView.findViewById(R.id.near_user_name);
			holder.tv_dist = (TextView) convertView.findViewById(R.id.near_content);
			holder.tv_signature = (TextView) convertView.findViewById(R.id.near_tv_time);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		String name = PreferencesUtils.getSharePreStr("weidi");
		Item item = lists.get(position);
		if (item.getUsername().equals(name)) {
			self_lat = item.getLat();
			self_lon = item.getLon();
		}
		Double lat = item.getLat();
		Double lon = item.getLon();
		if (self_lat != null && self_lon != null && lat != null && lon != null) {
			s = gps2m(self_lat, self_lon, lat, lon);
		}
		
		VCard vCard = (VCard)  XmppUtil.getUserInfo(item.getUsername());
		User friend = new User(vCard);
	    friend.showHead(holder.iv);
		holder.tv_name.setText(lists.get(position).getUsername());
		holder.tv_dist.setText(s+"米");
		holder.tv_signature.setText(friend.getIntro());
		return convertView;
	}
	class Holder {
		CircleImageView iv;//头像
		TextView tv_name;
		TextView tv_dist;
		TextView tv_signature;//签名
	}
	private final double EARTH_RADIUS = 6378137.0;
	@SuppressWarnings("unused")
	private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
	       double radLat1 = (lat_a * Math.PI / 180.0);
	       double radLat2 = (lat_b * Math.PI / 180.0);
	       double a = radLat1 - radLat2;
	       double b = (lng_a - lng_b) * Math.PI / 180.0;
	       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
	              + Math.cos(radLat1) * Math.cos(radLat2)
	              * Math.pow(Math.sin(b / 2), 2)));
	       s = s * EARTH_RADIUS;
	       s = Math.round(s * 10000) / 10000;
	       return s;
	    }
}
