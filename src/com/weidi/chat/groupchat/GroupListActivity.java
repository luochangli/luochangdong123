package com.weidi.chat.groupchat;


import java.util.ArrayList;
import com.weidi.R;
import com.weidi.adapter.Group_list_adapter;
import com.weidi.bean.Groupmember;
import com.weidi.view.AddPopWindow;
import com.weidi.view.TitleBarView;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class GroupListActivity extends Activity {

	ListView group_list;
	Group_list_adapter grouplistadapter;
	TitleBarView mTitleBarView;
	private ImageView mImageView;
	
	ArrayList<Groupmember> datasourcelist=new ArrayList<Groupmember>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_grouplist);
	    initView();
		initTitleView();	
	}
	
	
	private void initView() {
		
		// TODO Auto-generated method stub
		datasourcelist.add(new Groupmember("yy"));
		mTitleBarView=(TitleBarView)findViewById(R.id.title_bar);
		group_list=(ListView)findViewById(R.id.group_list);
		Log.i("tag", "GroupListActivity"+"前面");
		grouplistadapter=new Group_list_adapter(this,datasourcelist);
		group_list.setAdapter(grouplistadapter);
		Log.i("tag", "GroupListActivity"+"后面");
	}
	
	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE,View.GONE,View.VISIBLE, View.VISIBLE);
		mTitleBarView.setBtnLeft(R.drawable.btn_back, R.string.groupchat);
		mTitleBarView.setBtnRight(R.drawable.btn_msg_menu);
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AddPopWindow addPopWindow = new AddPopWindow(GroupListActivity.this);
				addPopWindow.showPopupWindow(mTitleBarView.getTvRight());
			}
		});
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		
	}
	
}
