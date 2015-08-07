package com.weidi.chat;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.bean.User;
import com.weidi.bean.UserInfo;
import com.weidi.common.base.BaseActivity;
import com.weidi.common.image.ImgConfig;
import com.weidi.util.Const;
import com.weidi.util.ToastUtil;
import com.weidi.view.CircleImageView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-30 下午6:41:51
 * @Description 1.0
 */
public class ChatInfoActi extends BaseActivity {

	ImageView topLeft;
	TextView topTitle, topRight, tvNick;
	CircleImageView ivUserHead;

	String YOU;

	private void initView() {
		topLeft = (ImageView) findViewById(R.id.topLeft);
		topTitle = (TextView) findViewById(R.id.topTitle);
		topRight = (TextView) findViewById(R.id.topRight);
		ivUserHead = (CircleImageView) findViewById(R.id.ivUserHead);
		tvNick = (TextView) findViewById(R.id.tvNick);
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_chat_info);

		initView();
		initData();
	}

	private void initData() {

		topTitle.setText("聊天信息");
		topRight.setVisibility(View.GONE);

		YOU = getIntent().getStringExtra(Const.YOU);
	     UserInfo.getUserInfo(YOU, mHandler,this);
	}

	@Override
	protected void setListener() {
		topLeft.setOnClickListener(new OnClickListener() {
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
		switch (msg.what) {
		case UserInfo.USER_INFO_TAG:
			User user = (User) msg.obj;
			if (user == null) {
				ToastUtil.showMine(mContext, "加载失败，请重试！");
				return;
			}
			user.showHead(ivUserHead);
			tvNick.setText(user.getNickname() == null ? user.getUsername()
					: user.getNickname());

			break;

		default:
			break;
		}

	}

}
