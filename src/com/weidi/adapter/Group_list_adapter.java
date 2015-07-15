package com.weidi.adapter;

import java.util.ArrayList;

import com.weidi.R;
import com.weidi.bean.Groupmember;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Group_list_adapter extends BaseAdapter {
	Context context;
	ArrayList<Groupmember> datasourcelist;
	public Group_list_adapter(Context context,ArrayList<Groupmember> datasourcelist){
		this.context=context;
		this.datasourcelist=datasourcelist;	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datasourcelist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return datasourcelist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		Log.i("TAG", "Group_list_adapter");
		ViewHolder viewHolder = null;
		if(view==null){
			viewHolder = new ViewHolder();
			view=LayoutInflater.from(context).inflate(
					R.layout.grouplist_item, null);
			viewHolder.mImageView=(ImageView)view.findViewById(R.id.imageView1);
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_name.setText(datasourcelist.get(position).grouplist_tvname());
		viewHolder.mImageView.setImageResource(R.drawable.default_useravatar);
		return view;
		
	}
	
	class ViewHolder {
		TextView tv_name;
		ImageView mImageView;
	}

}
