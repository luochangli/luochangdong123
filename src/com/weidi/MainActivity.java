package com.weidi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
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
import android.view.animation.TranslateAnimation;
import android.view.Window;
import android.view.WindowManager;
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
import com.weidi.common.scanner.BarcodeActivity;
import com.weidi.db.ChatDao;
import com.weidi.db.ChatMsgDao;
import com.weidi.db.NewFriendDao;
import com.weidi.db.NewsNotice;
import com.weidi.fragment.FindFragment;
import com.weidi.fragment.NewConstactFragment;
import com.weidi.fragment.NewsFragment;
import com.weidi.fragment.PersonalFrag;
import com.weidi.service.MsfService;
import com.weidi.util.Const;
import com.weidi.util.XmppUtil;

public class MainActivity extends BaseActivity implements OnClickListener {

	protected static final String TAG = "MainActivity";
	private Context mContext;
	private LinearLayout tabMsg, tabConstant, tabMe;
	private View currentButton;
	private ImageView ivTabMsg, ivTabConstant, ivTabMe;
	private TextView tv_newmsg, notiNewFriend, tvTabMsg, tvTabConstant,
			tvTabMe;
	NewsFragment newsFatherFragment;
	NewConstactFragment constactFatherFragment;
	// SettingFragment settingFragment;
	FindFragment findFragment;
	PersonalFrag settingFragment;
	boolean isStartService = false;
	private ChatDao chatDao;
	private int msgCount;

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

	}

	public void moreDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.activity_alert);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setBackgroundDrawableResource(R.drawable.xlbj7);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
		lp.x = 30;
		lp.y = 140;
		lp.width = 400; // 宽度
		lp.height = 940; // 高度
		lp.alpha = 1f; // 透明度

		dialogWindow.setAttributes(lp);
		dialog.show();

		RelativeLayout re_crechatroom = (RelativeLayout) dialogWindow
				.findViewById(R.id.re_crechatroom);
		RelativeLayout re_addfriends = (RelativeLayout) dialogWindow
				.findViewById(R.id.re_addfriends);
		RelativeLayout re_trade = (RelativeLayout) dialogWindow
				.findViewById(R.id.re_trade);
		RelativeLayout re_nearpoeple = (RelativeLayout) dialogWindow
				.findViewById(R.id.re_nearpoeple);
		RelativeLayout re2DimenCode = (RelativeLayout) dialogWindow
				.findViewById(R.id.re2DimenCode);
		RelativeLayout re_setting = (RelativeLayout) dialogWindow
				.findViewById(R.id.re_setting);

		re2DimenCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(new Intent(mContext,
						BarcodeActivity.class)));
			}
		});
		re_crechatroom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this,
						CreatChatRoomActivity.class));
				dialog.dismiss();
			}
		});

		re_addfriends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this,
						SearchActivity.class));
				dialog.dismiss();
			}
		});

		re_trade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, TradeActivity.class));
				dialog.dismiss();
			}
		});

		re_nearpoeple.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, FujinActivity.class));
				dialog.dismiss();
			}
		});
		re_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,
						SettingActivity.class));
				dialog.dismiss();
			}
		});

	}

	private void initMsgCount() {
		msgCount = chatDao.queryAllNotReadCount();
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
			tvTabMsg.setTextColor(this.getResources()
					.getColor(R.color.green_bg));
			ivTabMsg.setImageResource(R.drawable.icon_msg_press);
			getSupportFragmentManager().beginTransaction()
					.hide(constactFatherFragment).hide(settingFragment)
					.hide(findFragment).show(newsFatherFragment).commit();
			setButton(v);
			break;
		case R.id.buttom_constact:// 联系人
			setTabNomal();

			ivTabConstant.setImageResource(R.drawable.icon_contast_press);
			tvTabConstant.setTextColor(this.getResources().getColor(
					R.color.green_bg));
			getSupportFragmentManager().beginTransaction()
					.hide(newsFatherFragment).hide(settingFragment)
					.hide(findFragment).show(constactFatherFragment).commit();

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

			tvTabMe.setTextColor(this.getResources().getColor(R.color.green_bg));
			getSupportFragmentManager().beginTransaction()
					.hide(constactFatherFragment).hide(newsFatherFragment)
					.hide(findFragment).show(settingFragment).commit();

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
			case 100:

				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	    mLocalBroadcastManager.unregisterReceiver(mReceiver);
	}

	@Override
	protected void initView(Bundle savedInstanceState) {

		setContentView(R.layout.activity_main);
		mContext = this;
		chatDao = ChatDao.getInstance();
		
		isStartService = getIntent().getBooleanExtra("isStartService", false);
		if (isStartService) {
			Intent intent = new Intent(mContext, MsfService.class);
			startService(intent);
		}

		initView();

		findView();
		init();
		initMsgCount();
		initBroadcast();
		// 获取群列表
		XmppUtil.getMUCList();
		send_NewsIQ();
		QApp.getInstance().addActivity(MainActivity.this);
	}

	private void initBroadcast() {
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Const.ACTION_NEW_FRIEND_MSG)) {
					updateConstant();
				} else {
					initMsgCount();
					sendBroadcast(new Intent(Const.ACTION_ADDFRIEND));
				}
			}
		};
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				Const.ACTION_NEW_MSG));
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				Const.ACTION_NEW_FRIEND_MSG));
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

	public void send_NewsIQ() {
		NewsNotice news = NewsNotice.getInstance();
		List<Map<String, Object>> news_list = news.query();
		Map<String, Object> map = new HashMap<String, Object>();
		if (news_list.size() != 0) {
			map = news_list.get(0);
			String createdatetime = (String) map.get("createdatetime");
			IQOrder.getInstance().getNews2(createdatetime);

		} else {
			IQOrder.getInstance().getNews();

		}
		mContext.sendBroadcast(new Intent(Const.NEWSNOTICE));
	}

}
