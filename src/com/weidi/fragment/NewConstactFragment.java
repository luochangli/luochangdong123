package com.weidi.fragment;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smackx.packet.VCard;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.weidi.MainActivity;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.activity.ConstactSearchActi;
import com.weidi.activity.FriendActivity;
import com.weidi.activity.FriendInfoActi;
import com.weidi.activity.NewFriendActivity;
import com.weidi.activity.SearchActivity;
import com.weidi.bean.User;
import com.weidi.chat.repository.ConstactRepo;
import com.weidi.common.CharacterParser;
import com.weidi.common.PinyinComparator;
import com.weidi.common.SideBar;
import com.weidi.common.SideBar.OnTouchingLetterChangedListener;
import com.weidi.common.SortAdapter;
import com.weidi.common.SortModel;
import com.weidi.common.base.BaseFragment;
import com.weidi.db.NewFriendDao;
import com.weidi.util.Const;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;
import com.weidi.view.FriendPopWindow;
import com.weidi.view.LoadingDialog;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-8 下午5:19:17
 * @Description 1.0
 */
public class NewConstactFragment extends BaseFragment {
	private static String TAG = "NewConstactFragment";
	public static final String REFRESH_CONSTACT = "refresh_constacts";
	public static final String SHOW_NEW_FRIEND = "show_new_friend";
	public static final String SEARCH = "search";
	private LoadingDialog loadDialog;
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private TextView tvTotal, newFriCount;

	private RelativeLayout tvNewFriend, rlAddFriend, re_chatroom;
	FriendPopWindow addPopWindow;// 好友更多

	private ConstactRepo repo;
	MainActivity activity;
	@ViewInject(R.id.llTopRight)
	LinearLayout llTopRight;
	@ViewInject(R.id.llSearch)
	RelativeLayout llSearch;
	@ViewInject(R.id.llConstact)
	public LinearLayout llConstact;
	private float y;
	public static List<SortModel> sourceDateList;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.frag_constact);
		ViewUtils.inject(this, mRootView);
		mContext = getActivity();
		// 实例化汉字转拼音类
		loadDialog = new LoadingDialog(mContext);
		loadDialog.setTitle("正在加载...");
		CharacterParser.getInstance();

		new PinyinComparator();
		sourceDateList = new ArrayList<SortModel>();
		repo = new ConstactRepo(mContext);
		initView();
		new PinyinComparator();
		sourceDateList = new ArrayList<SortModel>();
		tvTotal.setText(String.valueOf(sourceDateList.size()) + "位联系人");
		adapter = new SortAdapter(mApp, sourceDateList);
		sortListView.setAdapter(adapter);
		initBroadcast();
		initData();
	}

	private void initBroadcast() {
		mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(REFRESH_CONSTACT)) {
					mHandler.sendEmptyMessage(1);// 更新界面
					QApp.mLocalBroadcastManager.sendBroadcast(new Intent(
							FriendActivity.SHOW_IS_FRIEND));
				}
				if (intent.getAction().equals(SHOW_NEW_FRIEND)) {
					updateConstant();
				}
				if (intent.getAction().equals(SEARCH)) {
					TranslateAnimation animation = new TranslateAnimation(0, 0,
							-y, 0);
					animation.setDuration(500);
					animation.setFillAfter(true);
					llConstact.startAnimation(animation);
				}

			}
		};
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				REFRESH_CONSTACT));
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				SHOW_NEW_FRIEND));
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				SEARCH));
	}

	private void initView() {
		sortListView = (ListView) mRootView
				.findViewById(R.id.country_lvcountry);
		dialog = (TextView) mRootView.findViewById(R.id.dialog);
		sideBar = (SideBar) mRootView.findViewById(R.id.sidrbar);
		sideBar.setTextView(dialog);
		// 通讯录页眉页脚
		LayoutInflater infalter = LayoutInflater.from(mContext);
		View headView = infalter.inflate(R.layout.item_contact_list_header,
				null);
		sortListView.addHeaderView(headView);
		View footerView = infalter.inflate(R.layout.item_contact_list_footer,
				null);
		sortListView.addFooterView(footerView);
		// SourceDateList =
		// filledData(getResources().getStringArray(R.array.date));
		// 新的朋友
		tvNewFriend = (RelativeLayout) headView
				.findViewById(R.id.re_newfriends);
		rlAddFriend = (RelativeLayout) headView.findViewById(R.id.rlAddFriend);
		newFriCount = (TextView) headView.findViewById(R.id.tv_unread);
		tvTotal = (TextView) footerView.findViewById(R.id.tv_total);

		re_chatroom = (RelativeLayout) headView.findViewById(R.id.re_chatroom);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	@Override
	protected void setListener() {
		llTopRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.popMenu.showAsDropDown(v);
			}
		});
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});
		//
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				if (position != 0 && position != sourceDateList.size() + 1) {
					String from = ((SortModel) adapter.getItem(position - 1))
							.getValue();
					// ShowPopWindow mm=new ShowPopWindow(mContext, 0);
					// showPopupWindow(view, from);
					Intent in = new Intent(mContext, FriendInfoActi.class);
					Bundle bundle = new Bundle();
					bundle.putString(Const.YOU, from);
					in.putExtras(bundle);
					startActivity(in);

				}

			}
		});

		// 根据输入框输入值的改变来过滤搜索
		// mClearEditText.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		// // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
		// filterData(s.toString());
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// }
		// });
		// 新的朋友
		tvNewFriend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, NewFriendActivity.class);
				startActivity(intent);

			}
		});
		rlAddFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, SearchActivity.class));
			}
		});

		re_chatroom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// startActivity(new Intent(mContext, GroupListActivity.class));
			}
		});

		llSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				y = llSearch.getY();
				llSearch.getHeight();
				TranslateAnimation animation = new TranslateAnimation(0, 0, 0,
						-y);
				animation.setDuration(300);
				animation.setFillAfter(true);
				animation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationStart(Animation arg0) {
						Intent intent = new Intent();
						intent.setClass(mContext, ConstactSearchActi.class);
						startActivity(intent);
						activity.overridePendingTransition(R.anim.animation_2,
								R.anim.animation_1);
					}

				});

				llConstact.startAnimation(animation);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleMsg(Message msg) {
		switch (msg.what) {
		case 1:

			initData();

			break;
		case 2:
			sourceDateList = (List<SortModel>) msg.obj;
			tvTotal.setText(String.valueOf(sourceDateList.size()) + "位联系人");
			adapter.updateListView(sourceDateList);
			if (loadDialog.isShowing()) {
				loadDialog.dismiss();
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {

	}

	private void initData() {
		loadDialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {

				List<SortModel> items = new ArrayList<SortModel>();
				items = repo.getMyConstact();
				Message msg = Message.obtain();
				msg.what = 2;
				msg.obj = items;
				mHandler.sendMessage(msg);
			}
		}).start();

	}

	public void updateConstant() {
		// 更新界面
		int count = NewFriendDao.getInstance().getUnDealCount();
		if (count > 0) {
			newFriCount.setVisibility(View.VISIBLE);
			newFriCount.setText("" + count);
		} else {
			newFriCount.setVisibility(View.GONE);
		}
	}

	public boolean compareContent(User friend) {
		String old_sex = friend.getSex();
		String old_intro = friend.getIntro();
		String old_nickname = friend.getNickname();
		String old_adr = friend.getAdr();
		VCard vcard = XmppUtil.getUserInfo(friend.getUsername());
		String new_sex = vcard.getField("sex");
		String new_nickname = vcard.getField("nickName");
		String new_adr = vcard.getField("adr");
		String new_intro = vcard.getField("intro");
		if (new_sex.equals(old_sex) && new_nickname.equals(old_nickname)
				&& new_adr.equals(old_adr) && new_intro.equals(old_intro)) {

			return false;

		} else {
			return true;
		}
	}

	@Override
	public void onDestroy() {
		mLocalBroadcastManager.unregisterReceiver(mReceiver);
		super.onDestroy();
	}
}
