package com.weidi.common;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.view.CircleImageView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-14 下午2:55:36
 * @Description 1.0 通用对话框
 */
public class CommonDialog extends DialogFragment {
	private TextView popSendMsg, popSendVideo, popFriendWeidi, popFriendAddr,
			popFriendSign, popFriendNick;
	private CircleImageView civHeadImg;
	private ImageView popFriendGender, popFriendClose, popFriendMore;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.pop_friend_info, container);
		initView(view);
		initListener();
		return view;
	}

	private void initListener() {
		popFriendClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}

		});
		popFriendMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		popSendMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
		
			}
		});
		popSendVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});
	}

	private void initView(View view) {
		popFriendClose = (ImageView) view.findViewById(R.id.ivPopClose);// 退出
		popFriendMore = (ImageView) view.findViewById(R.id.ivFriendMore);
		popSendMsg = (TextView) view.findViewById(R.id.tvSendMsg);
		popSendVideo = (TextView) view.findViewById(R.id.tvSendVideo);
		civHeadImg = (CircleImageView) view.findViewById(R.id.civPopHeadImg);
		popFriendAddr = (TextView) view.findViewById(R.id.tvFriendAddr);
		popFriendGender = (ImageView) view.findViewById(R.id.ivFriendGender);
		popFriendSign = (TextView) view.findViewById(R.id.tvFriendSign);
		popFriendWeidi = (TextView) view.findViewById(R.id.tvFriendWeidi);
		popFriendNick = (TextView) view.findViewById(R.id.tvFriendName);
	}

}
