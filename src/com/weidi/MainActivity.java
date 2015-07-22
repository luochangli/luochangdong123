package com.weidi;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weidi.activity.DevelopmentActi;
import com.weidi.activity.FujinActivity;
import com.weidi.activity.PicSrcPickerActivity;
import com.weidi.activity.SearchActivity;
import com.weidi.activity.SettingActivity;
import com.weidi.activity.TradeActivity;
import com.weidi.chat.IQOrder;
import com.weidi.chat.groupchat.CreatChatRoomActivity;
import com.weidi.common.base.BaseActivity;
import com.weidi.db.ChatMsgDao;
import com.weidi.db.NewFriendDao;
import com.weidi.fragment.FindFragment;
import com.weidi.fragment.NewConstactFragment;
import com.weidi.fragment.NewsFragment;
import com.weidi.fragment.PersonalFrag;
import com.weidi.service.MsfService;
import com.weidi.util.Const;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;
import com.weidi.view.TitleBarView;

public class MainActivity extends BaseActivity implements OnClickListener {

	protected static final String TAG = "MainActivity";
	private Context mContext;
	private LinearLayout tabMsg, tabConstant, tabMe;
	private View currentButton;
	private ImageView ivTabMsg, ivTabConstant, ivTabMe;
	private TextView tv_newmsg, notiNewFriend, tvTabMsg, tvTabConstant,
			tvTabMe;

	private TitleBarView mTitleBarView;
	NewsFragment newsFatherFragment;
	NewConstactFragment constactFatherFragment;
	// SettingFragment settingFragment;
	FindFragment findFragment;
	PersonalFrag settingFragment;
	boolean isStartService = false;
	private ChatMsgDao chatMsgDao;
	private int msgCount;
	private NewMsgReciver newMsgReciver;
	private ImageView tabMore;
	private PopupWindow popMore;

	RelativeLayout re_crechatroom, re_addfriends, re_trade, re_nearpoeple,
			re2DimenCode, re_setting;
	public DrawerLayout mDrawerLayout;

	private void initView() {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
				Gravity.RIGHT);

		newsFatherFragment = (NewsFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_news);// 消息

		constactFatherFragment = (NewConstactFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_constact);// 联系人
		findFragment = (FindFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_find);// 发现
		settingFragment = (PersonalFrag) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_setting);// 我
		tv_newmsg = (TextView) findViewById(R.id.tv_newmsg);// 消息数目提醒

		tabMore = (ImageView) findViewById(R.id.image_window_more);
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.VISIBLE, View.VISIBLE);
		mTitleBarView.setTitleText(R.string.msgs);
		// mTitleBarView.setBtnLeftIcon(R.drawable.msg_back);
		mTitleBarView.setBtnRight(R.drawable.btn_msg_menu);

		mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// LayoutInflater hh=getLayoutInflater();
				// View view=hh.inflate(R.layout.activity_alert, null);
				// final Builder gg=new AlertDialog.Builder(MainActivity.this);
				// gg.setView(view);
				// AlertDialog oo=gg.create();
				// oo.show();
				// Window window=oo.getWindow();
				// window.setContentView(R.layout.activity_alert);
				// gg.setCancelable(true);

				final Builder build = new AlertDialog.Builder(MainActivity.this);
				final AlertDialog dlg = build.create();
				dlg.show();
				dlg.setCanceledOnTouchOutside(true);
				Window window = dlg.getWindow();
				window.setContentView(R.layout.activity_alert);
				dlg.setCancelable(true);
				dlg.setCanceledOnTouchOutside(true);
				RelativeLayout re_crechatroom = (RelativeLayout) window
						.findViewById(R.id.re_crechatroom);
				RelativeLayout re_addfriends = (RelativeLayout) window
						.findViewById(R.id.re_addfriends);
				RelativeLayout re_trade = (RelativeLayout) window
						.findViewById(R.id.re_trade);
				RelativeLayout re_nearpoeple = (RelativeLayout) window
						.findViewById(R.id.re_nearpoeple);
				RelativeLayout re2DimenCode = (RelativeLayout) window
						.findViewById(R.id.re2DimenCode);
				RelativeLayout re_setting = (RelativeLayout) window
						.findViewById(R.id.re_setting);

				re_crechatroom.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(MainActivity.this,
								CreatChatRoomActivity.class));
						dlg.dismiss();
					}
				});

				re_addfriends.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(MainActivity.this,
								SearchActivity.class));
						dlg.dismiss();
					}
				});

				re_trade.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						startActivity(new Intent(MainActivity.this,
								TradeActivity.class));
						dlg.dismiss();
					}
				});

				re_nearpoeple.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						startActivity(new Intent(MainActivity.this,
								FujinActivity.class));
						dlg.dismiss();
					}
				});
				re_setting.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						startActivity(new Intent(MainActivity.this,
								SettingActivity.class));
						dlg.dismiss();
					}
				});
			}
		});
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

		notiNewFriend = (TextView) findViewById(R.id.notiConstact);
		tabMsg = (LinearLayout) findViewById(R.id.buttom_news);// 消息
		tabConstant = (LinearLayout) findViewById(R.id.buttom_constact);// 联系人
		tabMe = (LinearLayout) findViewById(R.id.buttom_setting);// 我

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
		tabMore.setOnClickListener(this);
		tabMsg.performClick();
	}

	@Override
	protected void onStart() {
		super.onStart();
		initMsgCount();
		updateConstant();
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

	private void setTabNomal() {
		ivTabConstant.setImageResource(R.drawable.icon_contast_nomal);
		ivTabMe.setImageResource(R.drawable.icon_me_nomal);
		ivTabMsg.setImageResource(R.drawable.icon_msg_nomal);

		tvTabConstant.setTextColor(this.getResources().getColor(
				R.color.text_tab));
		tvTabMe.setTextColor(this.getResources().getColor(R.color.text_tab));
		tvTabMsg.setTextColor(this.getResources().getColor(R.color.text_tab));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttom_news:// 消息
			setTabNomal();
			tvTabMsg.setTextColor(this.getResources().getColor(R.color.green));
			ivTabMsg.setImageResource(R.drawable.icon_msg_press);
			getSupportFragmentManager().beginTransaction()
					.hide(constactFatherFragment).hide(settingFragment)
					.hide(findFragment).show(newsFatherFragment).commit();
			mTitleBarView.setTitleText(R.string.msgs);
			setButton(v);
			break;
		case R.id.buttom_constact:// 联系人
			setTabNomal();
			ivTabConstant.setImageResource(R.drawable.icon_contast_press);
			tvTabConstant.setTextColor(this.getResources().getColor(
					R.color.green));
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
			ivTabMe.setImageResource(R.drawable.icon_me_press);
			tvTabMe.setTextColor(this.getResources().getColor(R.color.green));
			getSupportFragmentManager().beginTransaction()
					.hide(constactFatherFragment).hide(newsFatherFragment)
					.hide(findFragment).show(settingFragment).commit();
			mTitleBarView.setTitleText(R.string.mime);
			setButton(v);
			break;
		case R.id.image_window_more:

			showMoreWindow(tabMore);
			break;
		}
	}

	private void showMoreWindow(View v) {
		// DisplayMetrics metric = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(metric);
		// int height = metric.heightPixels;
		// int w = View.MeasureSpec.makeMeasureSpec(0,
		// View.MeasureSpec.UNSPECIFIED);
		// int h = View.MeasureSpec.makeMeasureSpec(0,
		// View.MeasureSpec.UNSPECIFIED);
		// v.measure(w, h);
		// int heightV = v.getMeasuredHeight();
		// int popHeight = height - heightV;
		if (popMore == null) {
			View popupView = LayoutInflater.from(this).inflate(
					R.layout.pop_winmore, null);
			popMore = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, true);
			initPop(popupView);
		}

		popMore.setAnimationStyle(R.style.PopupAnimation);
		popMore.setFocusable(true);
		popMore.setBackgroundDrawable(new BitmapDrawable());
		popMore.setOutsideTouchable(true);
		popMore.showAtLocation(v, Gravity.CENTER, 0, 0);
		// popMore.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		// int[] location = new int[2];
		// v.getLocationOnScreen(location);
		// popMore.showAtLocation(v, Gravity.NO_GRAVITY, location[0],
		// location[1]
		// - popMore.getHeight());
		// popMore.update();
		// popMore.setOnDismissListener(new OnDismissListener() {
		// public void onDismiss() {
		// // TODO Auto-generated method stub
		// tabMore.setImageResource(R.drawable.window_more_new);
		// }
		// });
		// popMore.setTouchInterceptor(new OnTouchListener() {
		// public boolean onTouch(View view, MotionEvent event) {
		// if (view.getId() == R.id.image_window_more) {
		// popMore.dismiss();
		// return true;
		// }
		// return false;
		// }
		// });
	}

	private void initPop(View view) {
		LinearLayout oa, bigHealth, finance, rich, mall, thingsNet;
		ImageView ivMore;
		oa = (LinearLayout) view.findViewById(R.id.oa);
		bigHealth = (LinearLayout) view.findViewById(R.id.bigHealth);
		finance = (LinearLayout) view.findViewById(R.id.finance);
		rich = (LinearLayout) view.findViewById(R.id.rich);
		mall = (LinearLayout) view.findViewById(R.id.mall);
		thingsNet = (LinearLayout) view.findViewById(R.id.thingNet);
		ivMore = (ImageView) view.findViewById(R.id.ivMore);
		ivMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popMore.dismiss();
			}
		});
		oa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(mContext, DevelopmentActi.class));
			}
		});
		bigHealth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, DevelopmentActi.class));
			}
		});

		finance.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, DevelopmentActi.class));
			}
		});
		rich.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, DevelopmentActi.class));
			}
		});
		mall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, DevelopmentActi.class));
			}
		});
		thingsNet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, DevelopmentActi.class));
			}
		});

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
				sendBroadcast(new Intent(Const.ACTION_ADDFRIEND));
			}

		}

	}

	public void updateConstant() {
		// 更新界面
		int count = NewFriendDao.getInstance().getUnDealCount();
		if (count > 0) {
			notiNewFriend.setVisibility(View.VISIBLE);
			notiNewFriend.setText("" + count);
		} else {
			notiNewFriend.setVisibility(View.GONE);
		}
		QApp.mLocalBroadcastManager.sendBroadcast(new Intent(
				NewConstactFragment.SHOW_NEW_FRIEND));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RESULT_OK == resultCode) {
			switch (requestCode) {
			case PicSrcPickerActivity.CROP:
				if (tabMe.getVisibility() == View.VISIBLE) {
					// String imgName = data.getStringExtra("imgName");
					// settingFragment.changeHead(data.getStringExtra("imgPath"));
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

	@Override
	protected void initView(Bundle savedInstanceState) {

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

		initView();

		findView();
		init();
		initMsgCount();
		// 获取群列表
		XmppUtil.getMUCList();

		IQOrder.getInstance().getNews();
		QApp.getInstance().addActivity(MainActivity.this);

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

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
