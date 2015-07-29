package com.weidi.activity;

import org.jivesoftware.smackx.packet.VCard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.weidi.R;
import com.weidi.bean.User;
import com.weidi.common.base.BaseActivity;
import com.weidi.common.image.ImgConfig;
import com.weidi.db.VCardDao;
import com.weidi.util.Const;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-28 下午2:55:46
 * @Description 1.0 好友信息资料页
 */
public class FriendInfoActi extends BaseActivity {

	private static final int FILL_DATA = 1212;
	private static final int LOAD_DATA_ERROR = 1214;

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

	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_info);
		ViewUtils.inject(this);
		YOU = getIntent().getStringExtra(Const.USER_NAME);
		initData(YOU);
	}

	@Override
	protected void setListener() {
		btnSendMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FriendInfoActi.this,
						ChatActivity.class);
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
		if (item.getNickname() != null) {
			tvNickName.setText(item.getNickname());
		}
		if (item.getAdr() != null) {
			tvInfoAdr.setText(item.getAdr());
		}
	}

	private void initData(final String weidi) {
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
				User friend = VCardDao.getInstance().isContain(weidi);
				if (friend == null) {
					VCard vCard = XmppUtil.getUserInfo(weidi);
					User user = new User(vCard);
					VCardDao.getInstance().insertUser(user);
					return user;
				} else {
					return friend;
				}

			}
		};
	}

}