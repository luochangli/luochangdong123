package com.weidi.activity;

import org.jivesoftware.smackx.packet.VCard;

import com.weidi.R;
import com.weidi.bean.User;
import com.weidi.common.image.ImgConfig;
import com.weidi.db.VCardDao;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;
import com.weidi.view.TitleBarView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FriendInfoActivity extends Activity implements OnClickListener {

	TitleBarView mTitleBarView;
	RelativeLayout re_region,re_sign;
	TextView tv_sendmessage,tv_sendvideo,tvNickName,tvWeidi,tv_region,tv_sign;
	String from,nickname,sign,region;
	User friend;
	ImageView seximg;
	CircleImageView civHeadImg;
	VCard vcard;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendinfo);
		Bundle getdata=getIntent().getExtras();
		from=(String) getdata.get("from");
		Log.i("TTTT", from); 
		initView();
		initTitlebar();
		initFriendData(from);
	}
	private void initTitlebar() {
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.friendinfo);
		mTitleBarView.setBtnLeftIcon(R.drawable.btn_back_nomal);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void initView() {
		tvNickName=(TextView)findViewById(R.id.tvNickName);
		tvWeidi=(TextView)findViewById(R.id.tvWeidi);
		tv_region=(TextView)findViewById(R.id.tv_region);
		tv_sign=(TextView)findViewById(R.id.tv_sign);
		seximg=(ImageView)findViewById(R.id.seximg);
		civHeadImg = (CircleImageView)findViewById(R.id.civHeadImg);
		mTitleBarView=(TitleBarView)findViewById(R.id.title_bar);
		tv_sendmessage=(TextView)findViewById(R.id.tv_sendmessage);
		tv_sendvideo=(TextView)findViewById(R.id.tv_sendvideo);
		tv_sendmessage.setOnClickListener(this);
		
	
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_sendmessage:
			Intent intent = new Intent();
			intent.putExtra("from", from);
			intent.setClass(FriendInfoActivity.this, ChatActivity.class);
			startActivity(intent);
			break;
			
        case R.id.tv_sendvideo:
			
			break;

		default:
			break;
		}
		
	}
	/*private void fillFriendInfo() {
		if (friend.getSex() != null) {
			if (friend.getSex().equals("男")) {
				seximg.setImageResource(R.drawable.male);
			} else {
				seximg.setImageResource(R.drawable.female);
			}
		}
		if (friend.getIntro() != null) {
			tv_sign.setText(friend.getIntro());
		}
		if (friend.getNickname() != null) {
			tvNickName.setText(friend.getNickname());
		}
		if (friend.getAdr() != null) {
			tv_region.setText(friend.getAdr());
		}
		ImgConfig.showHeadImg(friend.getUsername(),civHeadImg);
	}
	
	private void initFriendData(final String friendWeidi) {
		
		gg(friendWeidi);
		if (friend != null) {
			Log.i("GGGGGG", "这不是第一次创建friend但他们的内容改变了");
			//friend = new User(XmppUtil.getUserInfo(friendWeidi));
			fillFriendInfo();
			tvWeidi.setText(friendWeidi);
			VCardDao.getInstance().insertUser(friend);
		} 
	}
	
	public User gg(String friendWeidi){
		friend = VCardDao.getInstance().isContain(friendWeidi);
		if (friend == null) {
			return friend = new User(XmppUtil.getUserInfo(friendWeidi));
		} else {
			return friend;
		}
	}*/
	
	private void initFriendData(final String friendWeidi) {
		new XmppLoadThread(FriendInfoActivity.this) {

			@Override
			protected void result(Object object) {

				if (friend != null) {
					// boolean aa=compareContent(friend);
					Log.i("GGGGGG", "这不是第一次创建friend但他们的内容改变了");
					vcard=XmppUtil.getUserInfo(friendWeidi);
					friend = new User(vcard);
					fillFriendInfo();
					tvWeidi.setText(friendWeidi);
					VCardDao.getInstance().insertUser(friend);
					/*
					 * Log.i("GGGGGG", "这不是第一次创建friend"); fillFriendInfo();
					 * popFriendWeidi.setText(friend.getUsername()); friend =
					 * null;
					 */

				} else {

					VCard vCard = (VCard) object;
					friend = new User(vCard);
					fillFriendInfo();
					tvWeidi.setText(friendWeidi);
					VCardDao.getInstance().insertUser(friend);
					Log.i("GGGGGG", "这是第一次创建friend");
				}

			}

			private void fillFriendInfo() {
				if (friend.getSex() != null) {
					if (friend.getSex().equals("男")) {
						seximg.setImageResource(R.drawable.male);
					} else {
						seximg.setImageResource(R.drawable.female);
					}
				}
				if (friend.getIntro() != null) {
					tv_sign.setText(friend.getIntro());
				}
				if (friend.getNickname() != null) {
					tvNickName.setText(friend.getNickname());
				}
				if (friend.getAdr() != null) {
					tv_region.setText(friend.getAdr());
				}
				friend.showHead(civHeadImg);
				/*if(vcard.getField("avatar")!= null){
					ImgConfig.showHeadImg(friendWeidi, civHeadImg);
				}else if(friend.getSex().equals("男")){
					civHeadImg.setImageResource(R.drawable.man2);
				}else {
					civHeadImg.setImageResource(R.drawable.woman2);
				} */
				
			}

			@Override
			protected Object load() {

				friend = VCardDao.getInstance().isContain(friendWeidi);
				if (friend == null) {
					return XmppUtil.getUserInfo(friendWeidi);
				} else {
					return friend;
				}
			}
		};
	}
}