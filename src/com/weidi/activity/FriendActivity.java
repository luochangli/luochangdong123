package com.weidi.activity;

import org.jivesoftware.smackx.packet.VCard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.bean.Friend;
import com.weidi.bean.User;
import com.weidi.common.CircularImage;
import com.weidi.common.base.LuoBaseActivity;
import com.weidi.db.NewMsgCountDao;
import com.weidi.util.Const;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-25 下午4:50:41
 * @Description 1.0
 */
public class FriendActivity extends LuoBaseActivity implements OnClickListener {
	Button operBtn;
	TextView nameView, sexView, signView, nickNameView, phoneView, emailView;
	CircularImage headView;
	ImageView leftBtn;
	private String username;
	private User friend;
	private FriendReceiver reciver;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_friend);

		initView();

		username = getIntent().getStringExtra("username");
		operBtn.setOnClickListener(this);
		nameView.setText(username);
		// 接收到新消息的事件监听
		reciver = new FriendReceiver();
		registerReceiver(reciver, new IntentFilter(Const.ACTION_FRIENDS_ONLINE_STATUS_CHANGE));

		if (username.equals(Const.USER_NAME)) {
			operBtn.setVisibility(View.GONE);
		}
		isFriend();
		initData();
		QApp.getInstance().addActivity(FriendActivity.this);

	}

	private void initView() {
		operBtn = (Button) findViewById(R.id.operBtn);
		nameView = (TextView) findViewById(R.id.nameView);
		sexView = (TextView) findViewById(R.id.sexView);
		signView = (TextView) findViewById(R.id.signView);
		nickNameView = (TextView) findViewById(R.id.nickNameView);
		phoneView = (TextView) findViewById(R.id.phoneView);
		emailView = (TextView) findViewById(R.id.emailView);
		headView = (CircularImage) findViewById(R.id.headView);
		leftBtn = (ImageView) findViewById(R.id.leftBtn);
	}

	public void initData() {
		new XmppLoadThread(this) {

			@Override
			protected void result(Object object) {
				VCard vCard = (VCard) object;
				friend = new User(vCard);
				if (friend != null) {
					if (friend.getSex() != null) {
						sexView.setText(friend.getSex());
					}
					if (friend.getIntro() != null) {
						signView.setText(friend.getIntro());
					}
					if (friend.getNickname() != null) {
						nickNameView.setText("    "+friend.getNickname());
					}
					if (friend.getEmail() != null) {
						emailView.setText("    "+friend.getEmail());
					}
					if (friend.getMobile() != null) {
						phoneView.setText("    "+friend.getMobile());
					}
					friend.showHead(headView);
				}
			}

			@Override
			protected Object load() {
				return XmppUtil.getUserInfo(username);
			}
		};
	}

	public void isFriend() {
		if (XmppUtil.getInstance().getFriendBothList()
				.contains(new Friend(username))) {
			operBtn.setText("移出通讯录");
		} else {
			operBtn.setText("添加到通讯录");
		}
	}

	@Override
	protected void setListener() {
		operBtn.setOnClickListener(this);
        leftBtn.setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}

	private class FriendReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			isFriend();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.operBtn:
			if (operBtn.getText().equals("添加到通讯录")) {
				isFriend();
				XmppUtil.addUser(QApp.xmppConnection, username);
				ToastUtil
						.showShortToast(getApplicationContext(), "添加成功，等待通过验证");
				QApp.getInstance().sendBroadcast(new Intent(Const.ACTION_FRIENDS_ONLINE_STATUS_CHANGE));
			
			} else if (operBtn.getText().equals("移出通讯录")) {
				XmppUtil.getInstance().removeUser(username);
				ToastUtil.showShortToast(getApplicationContext(), "移除成功");
				QApp.getInstance().sendBroadcast(new Intent(Const.ACTION_FRIENDS_ONLINE_STATUS_CHANGE));
				operBtn.setText("添加到通讯录");
                //删除聊天数据和其他与该好友的数据
				QApp.getInstance().sendBroadcast(new Intent(Const.ACTION_NEW_FRIEND_MSG));

			}
			break;
		case R.id.leftBtn:
			finish();
			break;

		default:
			break;
		}

	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(reciver);
		super.onDestroy();
	}
}
