package com.weidi.fragment;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.activity.ChatActivity;
import com.weidi.adapter.SessionAdapter;
import com.weidi.bean.Session;
import com.weidi.db.ChatMsgDao;
import com.weidi.db.SessionDao;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppUtil;
import com.weidi.view.CustomListView;
import com.weidi.view.CustomListView.OnRefreshListener;

public class NewsFragment extends Fragment implements OnRefreshListener {
	private Context mContext;
	private View mBaseView;
	private CustomListView mCustomListView;
	private SessionAdapter adapter;
	private List<Session> sessionList = new ArrayList<Session>();
	private SessionDao sessionDao;
	private ChatMsgDao chatMsgDao;
	private String userid;
	private static String TAG = "NewsFragment";
	private AddFriendReceiver addFriendReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_news_father, null);
		userid = PreferencesUtils.getSharePreStr("username");
		if (userid.length() == 11) {
			userid = PreferencesUtils.getSharePreStr("weidi");
		}
		sessionDao = new SessionDao(mContext);
		chatMsgDao = new ChatMsgDao(mContext);
		addFriendReceiver = new AddFriendReceiver();
		IntentFilter intentFilter = new IntentFilter(Const.ACTION_ADDFRIEND);
		mContext.registerReceiver(addFriendReceiver, new IntentFilter(
				Const.ACTION_DELETE_MSG));
		mContext.registerReceiver(addFriendReceiver, intentFilter);
		findView();
		initData();
		return mBaseView;
	}

	private void findView() {

		mCustomListView = (CustomListView) mBaseView.findViewById(R.id.lv_news);// listview
		mCustomListView.setOnRefreshListener(this);// 设置listview下拉刷新监听
		mCustomListView.setCanLoadMore(false);// 设置禁止加载更多
		mCustomListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final Session session = sessionList.get(arg2 - 1);
				if (session.getType().equals(Const.MSG_TYPE_ADD_FRIEND)) {
					if (!TextUtils.isEmpty(session.getIsdispose())) {
						if (!session.getIsdispose().equals("1")) {
							Builder bd = new AlertDialog.Builder(mContext);
							bd.setItems(new String[] { "同意" },
									new OnClickListener() {
										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											XMPPConnection con = QApp.xmppConnection;
											Roster roster = con.getRoster();

											final String toJid = session
													.getFrom()
													+ "@"
													+ QApp.xmppConnection
															.getServiceName();
											String fromJid = userid
													+ "@"
													+ QApp.xmppConnection
															.getServiceName();
											Logger.e(TAG, toJid + "and"
													+ session.getFrom());
											XmppUtil.sendAgreeAddFriend(con,
													toJid, fromJid);
											XmppUtil.addUsers(roster, toJid,
													session.getFrom(), "我的好友");
											Intent intent = new Intent(
													Const.ACTION_FRIENDS_ONLINE_STATUS_CHANGE);
											mContext.sendBroadcast(intent);

											sessionDao
													.updateSessionToDisPose(session
															.getId());// 将本条数据在数据库中改为已处理
											sessionList.remove(session);
											session.setIsdispose("1");
											sessionList.add(0, session);

											adapter.notifyDataSetChanged();
											// new Thread(new Runnable() {
											// public void run() {
											// try {
											//
											// XmppUtil.sendMessage(
											// QApp.xmppConnection,
											// "我们已经是好友了，快来和我聊天吧！",
											// session.getFrom());
											// } catch (XMPPException e) {
											// // TODO Auto-generated
											// // catch block
											// e.printStackTrace();
											// }
											// // 发送广播更新好友列表
											// }
											// }).start();

										}
									});
							bd.create().show();
						} else {
							ToastUtil.showShortToast(mContext, "已同意");
						}
					}
				} else {
					Intent intent = new Intent(mContext, ChatActivity.class);
					intent.putExtra("from", session.getFrom());
					startActivity(intent);
				}
			}
		});
		mCustomListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						final Session session = sessionList.get(arg2 - 1);
						Builder bd = new AlertDialog.Builder(mContext);
						bd.setItems(new String[] { "删除会话" },
								new OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										deleteMsg(session);
										initData();
									}

									private void deleteMsg(final Session session) {
										sessionDao.deleteSession(session);
										chatMsgDao.deleteAllMsg(
												session.getFrom(),
												session.getTo());
										mContext.sendBroadcast(new Intent(
												Const.ACTION_NEW_MSG));
									}
								}).create().show();
						return true;
					}
				});
	}

	private void initData() {
		// 注意，当数据量较多时，此处要开线程了，否则阻塞主线程
		sessionList = sessionDao.queryAllSessions(userid);
		adapter = new SessionAdapter(mContext, sessionList);
		mCustomListView.setAdapter(adapter);
	}

	@Override
	public void onStart() {
		super.onStart();
		initData();
	}

	@Override
	public void onRefresh() {
		initData();
		mCustomListView.onRefreshComplete();
	}

	class AddFriendReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			
			initData();

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mContext.unregisterReceiver(addFriendReceiver);
	}

}
