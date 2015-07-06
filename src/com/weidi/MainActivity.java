package com.weidi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weidi.activity.LoginActivity;
import com.weidi.activity.PicSrcPickerActivity;
import com.weidi.activity.SettingActivity;
import com.weidi.db.ChatMsgDao;
import com.weidi.db.NewFriendDao;
import com.weidi.fragment.FindFragment;
import com.weidi.fragment.NewConstactFragment;
import com.weidi.fragment.NewsFragment;
import com.weidi.fragment.SettingFragment;
import com.weidi.service.MsfService;
import com.weidi.util.Const;
import com.weidi.view.AddPopWindow;
import com.weidi.view.TitleBarView;

public class MainActivity extends FragmentActivity implements OnClickListener {

	protected static final String TAG = "MainActivity";
	private Context mContext;
	private LinearLayout tabMsg, tabConstant, tabMe;
	private View mPopView;
	private View currentButton;
    private ImageView ivTabMsg,ivTabConstant,ivTabMe;
	private TextView tv_newmsg, notiNewFriend,tvTabMsg,tvTabConstant,tvTabMe;
	private TextView app_cancle;
	private TextView app_exit;
	private TextView app_change;
	private TitleBarView mTitleBarView;
	private PopupWindow mPopupWindow;
	NewsFragment newsFatherFragment;
	NewConstactFragment constactFatherFragment;
	SettingFragment settingFragment;
	FindFragment findFragment;
	boolean isStartService = false;
	private ChatMsgDao chatMsgDao;
	private int msgCount;

	private NewMsgReciver newMsgReciver;

	private LinearLayout windowMore;
	private ImageView image;
	private RelativeLayout fmpan;
	private PopupWindow popup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		
		chatMsgDao = new ChatMsgDao(mContext);
		newMsgReciver = new NewMsgReciver();
		IntentFilter intf = new IntentFilter();
		intf.addAction(Const.ACTION_NEW_MSG);
		registerReceiver(newMsgReciver, intf);
		registerReceiver(newMsgReciver, new IntentFilter(
				Const.ACTION_NEW_FRIEND_MSG));

		isStartService = getIntent().getBooleanExtra("isStartService", false);
		if (isStartService) {
			Intent intent = new Intent(mContext, MsfService.class);
			startService(intent);
		}
		newsFatherFragment = (NewsFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_news);// 消息

		constactFatherFragment = (NewConstactFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_constact);// 联系人
		findFragment = (FindFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_find);// 发现
		settingFragment = (SettingFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_setting);// 我
		tv_newmsg = (TextView) findViewById(R.id.tv_newmsg);// 消息数目提醒
		windowMore = (LinearLayout) findViewById(R.id.window_more);
		image = (ImageView) findViewById(R.id.image_window_more);
		fmpan = (RelativeLayout) findViewById(R.id.tab1);
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.VISIBLE,View.VISIBLE);
		mTitleBarView.setTitleText(R.string.msgs);
//		mTitleBarView.setBtnLeftIcon(R.drawable.msg_back);
		mTitleBarView.setBtnRight(R.drawable.btn_msg_menu);
		
		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AddPopWindow addPopWindow = new AddPopWindow(MainActivity.this);
				addPopWindow.showPopupWindow(mTitleBarView.getTvRight());
			}
		});
	
		findView();
		init();
		initMsgCount();
		
		QApp.getInstance().addActivity(MainActivity.this);

	}

	private void initMsgCount() {
		msgCount = chatMsgDao.queryAllNotReadCount();
		if (msgCount > 0) {
			tv_newmsg.setText("" + msgCount);
			tv_newmsg.setVisibility(View.VISIBLE);
		} else {
			tv_newmsg.setText("");
			tv_newmsg.setVisibility(View.GONE);
		}
	}

	private void findView() {
		mPopView = LayoutInflater.from(mContext).inflate(R.layout.app_exit,
				null);// pop，手机菜单键弹出
		notiNewFriend = (TextView) findViewById(R.id.notiConstact);
		tabMsg = (LinearLayout) findViewById(R.id.buttom_news);// 消息
		tabConstant = (LinearLayout) findViewById(R.id.buttom_constact);// 联系人
		tabMe = (LinearLayout) findViewById(R.id.buttom_setting);// 我

		app_cancle = (TextView) mPopView.findViewById(R.id.app_cancle);// 取消
		app_change = (TextView) mPopView.findViewById(R.id.app_change_user);// 注销用户
		app_exit = (TextView) mPopView.findViewById(R.id.app_exit);// 退出
		
		tvTabConstant = (TextView) findViewById(R.id.tvTabConstant);
		tvTabMe = (TextView) findViewById(R.id.tvTabMe);
		tvTabMsg = (TextView) findViewById(R.id.tvTabMsg);
        ivTabConstant = (ImageView) findViewById(R.id.ivTabConstant);
        ivTabMe = (ImageView) findViewById(R.id.ivTabMe);
        ivTabMsg = (ImageView) findViewById(R.id.ivTabMsg);
	}

	private void init() {
		tabMsg.setOnClickListener(this);
		tabConstant.setOnClickListener(this);
		tabMe.setOnClickListener(this);
		windowMore.setOnClickListener(this);
		tabMsg.performClick();
		initPop();
	}

	@Override
	protected void onStart() {
		super.onStart();
		initMsgCount();
		updateConstant();
	}

	private void initPop() {
		mPopupWindow = new PopupWindow(mPopView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		app_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		app_change.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);
				((Activity) mContext).overridePendingTransition(
						R.anim.activity_up, R.anim.fade_out);
				try {
					MsfService.getInstance().stopSelf();
				} catch (Exception e) {

				}
				finish();
			}
		});
		app_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					MsfService.getInstance().stopSelf();
				} catch (Exception e) {

				}
				finish();
			}
		});
	}

	private void setButton(View v) {
		if (currentButton != null && currentButton.getId() != v.getId()) {
			currentButton.setEnabled(true);
		}
		v.setEnabled(false);
		currentButton = v;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}

private void setTabNomal(){
	ivTabConstant.setImageResource(R.drawable.tab_icon_contact_normal);
	ivTabMe.setImageResource(R.drawable.tab_icon_me_normal);
	ivTabMsg.setImageResource(R.drawable.tab_icon_news_normal);
	
	tvTabConstant.setTextColor(this.getResources().getColor(R.color.black));
	tvTabMe.setTextColor(this.getResources().getColor(R.color.black));
	tvTabMsg.setTextColor(this.getResources().getColor(R.color.black));
}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttom_news:// 消息
			 setTabNomal();
			 tvTabMsg.setTextColor(this.getResources().getColor(R.color.green));
			 ivTabMsg.setImageResource(R.drawable.tab_icon_news_press);
			getSupportFragmentManager().beginTransaction()
					.hide(constactFatherFragment).hide(settingFragment)
					.hide(findFragment).show(newsFatherFragment).commit();
			mTitleBarView.setTitleText(R.string.msgs);
			setButton(v);
			break;
		case R.id.buttom_constact:// 联系人
			 setTabNomal();
			 ivTabConstant.setImageResource(R.drawable.tab_icon_contact_selected);
			 tvTabConstant.setTextColor(this.getResources().getColor(R.color.green));
			getSupportFragmentManager().beginTransaction()
					.hide(newsFatherFragment).hide(settingFragment)
					.hide(findFragment).show(constactFatherFragment).commit();
			mTitleBarView.setTitleText(R.string.constacts);
			setButton(v);
			break;
		// case R.id.buttom_find:// 发现
		// getSupportFragmentManager().beginTransaction()
		// .hide(constactFatherFragment).hide(newsFatherFragment)
		// .hide(settingFragment).show(findFragment).commit();
		// setButton(v);
		// break;
		case R.id.buttom_setting:// 设置
			 setTabNomal();
				ivTabMe.setImageResource(R.drawable.tab_icon_me_press);
				tvTabMe.setTextColor(this.getResources().getColor(R.color.green));
			getSupportFragmentManager().beginTransaction()
					.hide(constactFatherFragment).hide(newsFatherFragment)
					.hide(findFragment).show(settingFragment).commit();
			mTitleBarView.setTitleText(R.string.mime);
			setButton(v);
			break;
		case R.id.window_more:// 中间按钮
			image.setImageResource(R.drawable.window_cancel_new);
			showWindow(fmpan);
			break;
		}
	}

	@SuppressWarnings("deprecation")
	private void showWindow(View parent) {
		if (popup == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.write_tab, null);
			// 创建一个PopuWidow对象
			popup = new PopupWindow(view,
					LinearLayout.LayoutParams.FILL_PARENT, 620);
			// 设置焦点在弹窗上
		}
		popup.setFocusable(true);
		// 设置允许在外点击消失
		popup.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popup.setBackgroundDrawable(new BitmapDrawable());
		// 设置菜单显示的位置
		popup.showAsDropDown(parent, Gravity.CENTER, 0);
		// popup.setAnimationStyle(R.style.popwin_anim_style);
		// 设置弹窗消失事件监听
		popup.setOnDismissListener(new OnDismissListener() {
			public void onDismiss() {
				// TODO Auto-generated method stub
				image.setImageResource(R.drawable.window_more_new);
			}
		});
		popup.setTouchInterceptor(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popup.dismiss();
					image.setImageResource(R.drawable.window_more_new);
					return true;
				}
				return false;
			}
		});
		// if(!popup.isShowing()) {
		// popup.showAsDropDown(parent, Gravity.CENTER, 0);
		// }
	}

	/**
	 * 新消息广播接收
	 * 
	 * @author 白玉梁
	 */
	private class NewMsgReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Const.ACTION_NEW_FRIEND_MSG)) {
				updateConstant();
			} else {
				initMsgCount();
			}

		}

	}

	public void updateConstant() {
		// 更新界面
		int count = NewFriendDao.getInstance(mContext).getUnDealCount();
		if (count > 0) {
			notiNewFriend.setVisibility(View.VISIBLE);
			notiNewFriend.setText("" + count);
		} else {
			notiNewFriend.setVisibility(View.GONE);
		}
		sendBroadcast(new Intent(Const.ACTION_FRIENDS_ONLINE_STATUS_CHANGE));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RESULT_OK == resultCode) {
			switch (requestCode) {
			case PicSrcPickerActivity.CROP:
				if (tabMe.getVisibility() == View.VISIBLE) {
//					String imgName = data.getStringExtra("imgName");
					settingFragment.changeHead(data.getStringExtra("imgPath"));
				}
				break;
				
			default:
				break;
			}
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(newMsgReciver);
	}

}