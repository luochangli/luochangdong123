package com.weidi.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jivesoftware.smackx.packet.VCard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.activity.ChatActivity;
import com.weidi.activity.NewFriendActivity;
import com.weidi.activity.SearchActivity;
import com.weidi.bean.Friend;
import com.weidi.bean.User;
import com.weidi.common.CharacterParser;
import com.weidi.common.ClearEditText;
import com.weidi.common.PinyinComparator;
import com.weidi.common.SideBar;
import com.weidi.common.SideBar.OnTouchingLetterChangedListener;
import com.weidi.common.SortAdapter;
import com.weidi.common.SortModel;
import com.weidi.common.ViewHolder;
import com.weidi.common.base.BaseFragment;
import com.weidi.db.NewFriendDao;
import com.weidi.db.VCardDao;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;
import com.weidi.view.FriendPopWindow;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-8 下午5:19:17
 * @Description 1.0
 */
public class NewConstactFragment extends BaseFragment {
	private static String TAG = "NewConstactFragment";
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private TextView tvTotal, newFriCount;
	private FriendsOnlineStatusReceiver friendsOnlineStatusReceiver;
	private Context mContext;
	private RelativeLayout tvNewFriend, rlAddFriend;
	private PopupWindow popFriendInfo;// 好友信息
	FriendPopWindow addPopWindow;// 好友更多
	private LayoutInflater layoutInflater;
	private TextView popSendMsg, popSendVideo, popFriendWeidi, popFriendAddr,
			popFriendSign, popFriendNick;
	private CircleImageView civHeadImg;
	private ImageView popFriendGender, popFriendClose, popFriendMore;
	private User friend;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	public static List<SortModel> sourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setRootView(R.layout.frag_constact);

		mContext = getActivity();
		friendsOnlineStatusReceiver = new FriendsOnlineStatusReceiver();
		IntentFilter intentFilter = new IntentFilter(
				Const.ACTION_FRIENDS_ONLINE_STATUS_CHANGE);
		mContext.registerReceiver(friendsOnlineStatusReceiver, intentFilter);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		pinyinComparator = new PinyinComparator();
		sourceDateList = new ArrayList<SortModel>();
		// 获取好友列表
		XmppUtil.getInstance().getFriends(QApp.xmppConnection.getRoster());
		initView();

		initData();

		tvTotal.setText(String.valueOf(sourceDateList.size()) + "位联系人");
		adapter = new SortAdapter(mApp, sourceDateList);
		sortListView.setAdapter(adapter);
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
		mClearEditText = (ClearEditText) mRootView
				.findViewById(R.id.filter_edit);
	}

	@Override
	protected void setListener() {
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
					showPopupWindow(view, from);

				}

			}
		});

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
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
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param data
	 * @return
	 */
	private List<SortModel> filledData(List<String> data) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		User user;
		for (int i = 0; i < data.size(); i++) {
			SortModel sortModel = new SortModel();
			user = VCardDao.getInstance().isContain(data.get(i));
			if (user == null) {
				user = new User(XmppUtil.getUserInfo(data.get(i)));
			}
			String nick = user.getNickname() == null ? user.getUsername()
					: user.getNickname();
			sortModel.setName(nick);
			sortModel.setValue(user.getUsername());

			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(nick);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = sourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : sourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		tvTotal.setText(String.valueOf(sourceDateList.size()) + "位联系人");
		adapter.updateListView(filterDateList);

	}

	@Override
	protected void handleMsg(Message msg) {
		switch (msg.what) {
		case 1:

			initData();
			tvTotal.setText(String.valueOf(sourceDateList.size()) + "位联系人");
			adapter.updateListView(sourceDateList);
			break;
		}
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {

	}

	private void initData() {
		// XMPPConnection conn = QApp.xmppConnection;
		// if (conn == null)
		// return;
		// Roster roster = conn.getRoster();
		// List<RosterEntry> entries = XmppUtil.getAllEntries(roster);
		//
		// for (RosterEntry entry : entries) {
		// Logger.i(TAG, entry.getUser() + "type:" + entry.getType().name());
		// if (entry.getType().name().equals("both")) {
		//
		// firendData.add(StringUtils.parseName(entry.getUser()));
		// Logger.i(TAG, entry.getUser());
		//
		// }
		// }

		List<String> friendData = new ArrayList<String>();
		List<Friend> friends = XmppUtil.getInstance().getFriendBothList();
		for (Friend friend : friends) {
			friendData.add(friend.username);
		}

		if (friendData.size() > 0) {

			sourceDateList = filledData(friendData);
			Logger.i(TAG, "好友数量=" + sourceDateList.size());
		} else {
			ToastUtil.showShortToast(mContext, "暂无好友");
		}
		// 根据a-z进行排序源数据
		Collections.sort(sourceDateList, pinyinComparator);
		updateConstant();
	}

	private void showPopupWindow(View parent, String friendWeidi) {
		if (popFriendInfo == null) {
			View view = layoutInflater.inflate(R.layout.pop_friend_info, null);
			popFriendInfo = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, true);
			initPop(view, friendWeidi);
		}
		popFriendInfo.setAnimationStyle(android.R.style.Animation_InputMethod);
		popFriendInfo.setFocusable(true);
		popFriendInfo.setOutsideTouchable(true);
		popFriendInfo.setBackgroundDrawable(new BitmapDrawable());
		popFriendInfo
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popFriendInfo.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	public void initPop(View view, final String friendWeidi) {
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

		addPopWindow = new FriendPopWindow(layoutInflater,
				R.layout.pop_friend_more) {

			@Override
			public void convert(ViewHolder helper) {
				RelativeLayout rlDelete = helper.getView(R.id.rlDelete);
				rlDelete.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						XmppUtil.getInstance().removeUser(friendWeidi);
						ToastUtil.showShortToast(mContext, "移除成功");
						QApp.getInstance()
								.sendBroadcast(
										new Intent(
												Const.ACTION_FRIENDS_ONLINE_STATUS_CHANGE));
						closePop();
					}
				});
			}
		};
		initFriendData(friendWeidi);
		setPopFriend(friendWeidi);
	}

	private void closePop() {
		popFriendInfo.dismiss();
		popFriendInfo = null;
		addPopWindow.dismiss();
		addPopWindow = null;

	}

	private void setPopFriend(final String friendWeidi) {
		popFriendClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closePop();
			}

		});
		popFriendMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				addPopWindow.showPopupWindow(tvNewFriend);
			}
		});
		popSendMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("from", friendWeidi);
				intent.setClass(mContext, ChatActivity.class);
				startActivity(intent);
				popFriendInfo.dismiss();
				popFriendInfo = null;
			}
		});
		popSendVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});
	}

	private void initFriendData(final String friendWeidi) {
		new XmppLoadThread(mContext) {

			@Override
			protected void result(Object object) {

				if (friend != null) {
					fillFriendInfo();
					popFriendWeidi.setText(friend.getUsername());
					friend = null;
				} else {

					VCard vCard = (VCard) object;
					friend = new User(vCard);
					fillFriendInfo();
					popFriendWeidi.setText(friendWeidi);
					VCardDao.getInstance().insertUser(friend);
				}
			}

			private void fillFriendInfo() {
				if (friend.getSex() != null) {
					if (friend.getSex().equals("男")) {
						popFriendGender.setImageResource(R.drawable.male);
					} else {
						popFriendGender.setImageResource(R.drawable.female);
					}
				}
				if (friend.getIntro() != null) {
					popFriendSign.setText(friend.getIntro());
				}
				if (friend.getNickname() != null) {
					popFriendNick.setText(friend.getNickname());
				}
				if (friend.getAdr() != null) {
					popFriendAddr.setText(friend.getAdr());
				}
				friend.showHead(civHeadImg);
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

	public void updateConstant() {
		// 更新界面
		int count = NewFriendDao.getInstance(mContext).getUnDealCount();
		if (count > 0) {
			newFriCount.setVisibility(View.VISIBLE);
			newFriCount.setText("" + count);
		} else {
			newFriCount.setVisibility(View.GONE);
		}
	}

	class FriendsOnlineStatusReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			/*
			 * String from = intent.getStringExtra("from"); int status =
			 * intent.getIntExtra("status", 0); if (!TextUtils.isEmpty(from)) {
			 * if (status == 1) { ToastUtil.showShortToast(mApp, from + "上线了");
			 * } else if (status == 0) { ToastUtil.showShortToast(mApp, from +
			 * "下线了"); } }
			 */
			if (intent.getAction().equals(
					Const.ACTION_FRIENDS_ONLINE_STATUS_CHANGE)) {
				mHandler.sendEmptyMessage(1);// 更新界面

			}

		}
	}

	@Override
	public void onDestroy() {
		mContext.unregisterReceiver(friendsOnlineStatusReceiver);
		super.onDestroy();
	}
}
