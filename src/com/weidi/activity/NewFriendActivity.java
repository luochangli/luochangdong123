/**
 * 
 */
package com.weidi.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.adapter.SearchAdapter;
import com.weidi.common.base.LuoBaseActivity;
import com.weidi.db.NewFriendDao;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-25 下午4:50:41
 * @Description 1.0
 */
public class NewFriendActivity extends LuoBaseActivity {
	private TextView rightBtn;
	private ImageView leftBtn;
	private TextView titleView;
	private ListView listView;
	private List<String> friends = new ArrayList<String>();
	private SearchAdapter adapter;

	public void initData() {
		friends = NewFriendDao.getInstance(getApplicationContext())
				.getNewFriend();
		adapter.addAll(friends);
		if (adapter.getCount() == 0) {
			listView.setVisibility(View.GONE);
		}
	}

	// public void onClick(View v){
	// switch (v.getId()) {
	// case R.id.leftBtn:
	// finish();
	// break;
	//
	// case R.id.rightBtn:
	// new AlertDialog.Builder(this)
	// .setTitle("提示")
	// .setMessage("确认清空信息?清空后不可恢复？")
	// .setPositiveButton("是", new OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// NewFriendDao.getInstance(getApplicationContext()).clear();
	// adapter.clear();
	// adapter.notifyDataSetChanged();
	// }
	// })
	// .setNegativeButton("否", null)
	// .show();
	//
	// break;
	// }
	// }

	@Override
	protected void onDestroy() {
		// NewMsgDbHelper.getInstance(MyApplication.getInstance()).delNewMsg(""+0);
		// MyApplication.getInstance().sendBroadcast(new
		// Intent("FriendNewMsg"));
		super.onDestroy();
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_new_friend);
		listView = (ListView) findViewById(R.id.lvFriendInvator);
		rightBtn = (TextView) findViewById(R.id.rightBtn);
		rightBtn.setVisibility(View.GONE);
		leftBtn = (ImageView) findViewById(R.id.leftBtn);
		titleView = (TextView) findViewById(R.id.titleView);
		adapter = new SearchAdapter(this);
		adapter.isNewFriend = true;
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						FriendActivity.class);
				intent.putExtra("username", adapter.getItem(position));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		initData();

	}

	@Override
	protected void setListener() {
		leftBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}

}
