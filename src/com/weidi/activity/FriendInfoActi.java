package com.weidi.activity;

import org.jivesoftware.smackx.packet.VCard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.bean.User;
import com.weidi.chat.NewChatActivity;
import com.weidi.chat.RemarkActi;
import com.weidi.chat.RemarkRepo;
import com.weidi.common.ViewHolder;
import com.weidi.common.base.BaseActivity;
import com.weidi.db.SessionDao;
import com.weidi.db.VCardDao;
import com.weidi.util.Const;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;
import com.weidi.view.FriendPopWindow;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-28 下午2:55:46
 * @Description 1.0 好友信息资料页
 */
public class FriendInfoActi extends BaseActivity {

	private static final int FILL_DATA = 1212;
	private static final int LOAD_DATA_ERROR = 1214;
	private LayoutInflater layoutInflater;
	FriendPopWindow addPopWindow;
	private PopupWindow popFriendInfo;// 好友信息
	public static final String REFRESH_TO = "refresh_to";

	ImageView ivFriendMore;
	private PopupWindow popupWindow;
	LinearLayout llTopRight;

	@ViewInject(R.id.civHeadImg)
	CircleImageView civHeadImg;
	@ViewInject(R.id.tvNickName)
	TextView tvNickName;
	@ViewInject(R.id.ivMeSex)
	ImageView ivMeSex;
	@ViewInject(R.id.tvWeidi)
	TextView tvWeidi;
	@ViewInject(R.id.tvInfoAdr)
	TextView tvInfoAdr;
	@ViewInject(R.id.tvInfoSign)
	TextView tvInfoSign;
	@ViewInject(R.id.btnSendMsg)
	TextView btnSendMsg;
	@ViewInject(R.id.btnSendVideo)
	TextView btnSendVideo;

	private String YOU;

	@OnClick(R.id.topLeft)
	public void back(View v) {
		finish();
	}

	@OnClick(R.id.llTopRight)
	public void right(View v) {
		Intent intent = new Intent(this, RemarkActi.class);
		intent.putExtra(Const.YOU, YOU);
		startActivity(intent);

	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_info);
		ViewUtils.inject(this);
		YOU = getIntent().getStringExtra(Const.YOU);
		initData();
		initBroadcast();
	}

	private void initBroadcast() {
		mReceiver =new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				initData();
				
			}
		};
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(REFRESH_TO));
		
	}

	@Override
	protected void setListener() {
		btnSendMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FriendInfoActi.this,
						NewChatActivity.class);
				intent.putExtra("from", YOU);
				startActivity(intent);
				finish();
			}
		});
		btnSendVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleMsg(Message msg) {
		switch (msg.what) {
		case FILL_DATA:
			User obj = (User) msg.obj;
			fillData(obj);
			break;
		case LOAD_DATA_ERROR:
			ToastUtil.showMine(mContext, "加载数据失败！");
			break;

		default:
			break;
		}

	}

	private void fillData(User item) {
		tvWeidi.setText(YOU);
		String toNick = RemarkRepo.getRemark(YOU);
		item.showHead(civHeadImg);
		if (item.getSex() != null) {
			if (item.getSex().equals("男")) {
				ivMeSex.setImageResource(R.drawable.male);
			} else {
				ivMeSex.setImageResource(R.drawable.female);
			}
		}
		if (item.getIntro() != null) {
			tvInfoSign.setText(item.getIntro());
		}
		if (toNick != null) {
			tvNickName.setText(toNick);
		} else if (item.getNickname() != null) {
			tvNickName.setText(item.getNickname());
		}else{
			tvNickName.setText(YOU);
		}

		if (item.getAdr() != null) {
			tvInfoAdr.setText(item.getAdr());
		}
	}

	private void initData() {
		new XmppLoadThread(this) {

			@Override
			protected void result(Object object) {
				User user = (User) object;
				if (user != null) {
					sendFillDataBroadcast(user);
				} else {
					mHandler.sendEmptyMessage(LOAD_DATA_ERROR);
				}

			}

			private void sendFillDataBroadcast(User obj) {
				Message msg = new Message();
				msg.what = FILL_DATA;
				msg.obj = obj;
				mHandler.sendMessage(msg);
			}

			@Override
			protected Object load() {
				User friend = VCardDao.getInstance().getUser(YOU);
				if (friend == null) {
					VCard vCard = XmppUtil.getUserInfo(YOU);
					User user = new User(vCard);
					VCardDao.getInstance().insertUser(user);
					return user;
				} else {
					return friend;
				}

			}
		};
	}

	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow() {
		// TODO Auto-generated method stub
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.pop_friend_more, null, false);
		// 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
		popupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		// 设置动画效果
		popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);

		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}

				return false;
			}

		});
	}

	/***
	 * 获取PopupWindow实例
	 */
	private void getPopupWindow() {
		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopuptWindow();
		}
	}

}
