package com.weidi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.weidi.R;
import com.weidi.activity.MyInfoActi;
import com.weidi.bean.User;
import com.weidi.common.base.BaseFragment;
import com.weidi.common.image.ImgConfig;
import com.weidi.util.Const;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-18 上午10:15:04
 * @Description 1.0
 */
public class PersonalFrag extends BaseFragment implements OnClickListener {

	@ViewInject(R.id.rlMeGallerys)
	RelativeLayout rlMeGallerys;
	@ViewInject(R.id.rlMe2Code)
	RelativeLayout rlMe2Code;
	@ViewInject(R.id.rlMeCollotions)
    RelativeLayout rlMeCollotions;
	@ViewInject(R.id.rlMeWallet)
	RelativeLayout rlMeWallet;
	
	CircleImageView civHead;
	TextView tvNickName, tvWeidi;
	public static final String UPDATE_PERSONAL = "update_personal";

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.frag_me);
		Const.loginUser = new User(XmppUtil.getUserInfo(null));// 加载用户数据
		ViewUtils.inject(this, mRootView);
		initView();
		initData();
		initBroadcast();
	}

	private void initBroadcast() {
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				initData();

			}
		};
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				UPDATE_PERSONAL));
	}

	private void initData() {
		ImgConfig.showHeadImg(Const.USER_NAME, civHead);
		tvWeidi.setText(Const.USER_NAME);
		if (Const.loginUser.getNickname() != null) {
			tvNickName.setText(Const.loginUser.getNickname());
		}
	}

	private void initView() {
		civHead = (CircleImageView) mRootView.findViewById(R.id.civHeadImg);
		tvNickName = (TextView) mRootView.findViewById(R.id.tvNickName);
		tvWeidi = (TextView) mRootView.findViewById(R.id.tvWeidi);

	}

	@Override
	protected void setListener() {
		civHead.setOnClickListener(this);
	}

	@OnClick(R.id.rlMe2Code)
	public void saoyisao(View v){
		ToastUtil.showShortLuo("扫一扫");
	}
	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}
   
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.civHeadImg:
			Intent intent = new Intent(mContext, MyInfoActi.class);
			mContext.startActivity(intent);
			break;

		default:
			break;
		}

	}
	
	@Override
	public void onDestroy() {
		mLocalBroadcastManager.unregisterReceiver(mReceiver);
		super.onDestroy();
	}

}
