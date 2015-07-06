package com.weidi.adapter;


import java.util.List;

import org.jivesoftware.smackx.packet.VCard;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.bean.Session;
import com.weidi.common.image.ImgConfig;
import com.weidi.util.Const;
import com.weidi.util.ExpressionUtil;
import com.weidi.view.CircleImageView;

public class SessionAdapter extends BaseAdapter {
	private Context mContext;
	private List<Session> lists;
	VCard vcard=new VCard();
	Bitmap bm;

	public SessionAdapter(Context context, List<Session> lists) {
		this.mContext = context;
		this.lists = lists;
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
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.fragment_news_item,null);
			holder = new Holder();
			holder.iv = (CircleImageView) convertView.findViewById(R.id.user_head);
			holder.tv_name = (TextView) convertView.findViewById(R.id.user_name);
			holder.tv_tips = (TextView) convertView.findViewById(R.id.tips);
			holder.tv_content = (TextView) convertView.findViewById(R.id.content);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_newmsg= (TextView) convertView.findViewById(R.id.tv_newmsg);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		Session session = lists.get(position);
		if(session.getType().equals(Const.MSG_TYPE_ADD_FRIEND)){
			holder.tv_tips .setVisibility(View.VISIBLE);
			holder.iv.setImageResource(R.drawable.ibl);
		}else{
			holder.tv_tips.setVisibility(View.GONE);
			ImgConfig.showHeadImg(session.getFrom(), holder.iv);
			
		}
		
		holder.tv_name.setText(session.getFrom());
		holder.tv_content.setText(ExpressionUtil.prase(mContext, holder.tv_content, session.getContent()==null?"":session.getContent()));
		holder.tv_time.setText(session.getTime());
		if(!TextUtils.isEmpty(session.getNotReadCount())&&Integer.parseInt(session.getNotReadCount())>0){
			holder.tv_newmsg.setVisibility(View.VISIBLE);
			holder.tv_newmsg.setText(session.getNotReadCount());
		}else{
			holder.tv_newmsg.setVisibility(View.GONE);
			holder.tv_newmsg.setText("");
		}
		return convertView;
	}

	class Holder {
		CircleImageView iv;
		TextView tv_name,tv_tips;
		TextView tv_content;
		TextView tv_time,tv_newmsg;
	}

}