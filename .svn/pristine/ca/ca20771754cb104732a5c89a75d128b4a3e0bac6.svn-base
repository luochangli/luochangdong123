package com.weidi.activity;

import java.util.List;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.adapter.AddFriendAdapter;
import com.weidi.bean.Session;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppUtil;
import com.weidi.view.LoadingDialog;

/**
 * 
 * @author 白玉梁
 */
public class AddFriendActivity extends Activity implements OnClickListener {
	private static String TAG = "AddFriendActivity";
	private ImageView go_back;
	private EditText search_key;
	private Button btn_search;
	private ListView search_list;

	private String search_content;
	List<Session> listUser;
	AddFriendAdapter addFriendAdapter;
	private LoadingDialog loadingDialog;

	private String I;

	private String sendInviteUser = "";// 被邀请人

	PopupWindow popWindow;
	EditText edit;
	Button btn_ok, btn_cancel;
	private LayoutInflater layoutInflater;

	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			switch (msg.what) {
			case 1:
				addFriendAdapter = new AddFriendAdapter(AddFriendActivity.this,
						listUser);
				search_list.setAdapter(addFriendAdapter);
				break;
			case -1:
				ToastUtil.showLongToast(AddFriendActivity.this, "未查询到信息");
				break;
			case 2:
				ToastUtil.showLongToast(AddFriendActivity.this, "已经发出添加好友请求");
				finish();
				break;

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		loadingDialog = new LoadingDialog(this);
		loadingDialog.setTitle("正在查询...");
		String user = PreferencesUtils.getSharePreStr("username");
		if (user.length() == 11) {
			I = PreferencesUtils.getSharePreStr( "weidi");
		} else {
			I = user;
		}
		initView();
		QApp.getInstance().addActivity(AddFriendActivity.this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		go_back = (ImageView) findViewById(R.id.img_back);// 返回
		search_key = (EditText) findViewById(R.id.search_key);
		btn_search = (Button) findViewById(R.id.btn_search);
		// listview
		search_list = (ListView) findViewById(R.id.search_list);
		search_list.setOverScrollMode(View.OVER_SCROLL_NEVER);
		search_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				final String YOU = listUser.get(arg2).getFrom();
				if (YOU.equals(I)) {
					ToastUtil.showLongToast(getApplicationContext(),
							"不能添加自己为好友");
					return;
				}
				// 此处是判断是否重复邀请了某人，当然这个判断很简单，也是不科学的，至于怎么判断此人是否被你邀请过，大家自己可以想办法
				// 还有一点是，已经成为好友了，再去点邀请，这个也是需要判断的
				if (sendInviteUser.equals(YOU)) {
					ToastUtil.showShortToast(getApplicationContext(), "你已经邀请过"
							+ YOU + "了");
					return;
				}
				showPopupWindow(search_list, YOU);
				popupInputMethodWindow();
			}
		});

		go_back.setOnClickListener(this);
		btn_search.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {// 返回
		case R.id.img_back:
			this.finish();
			break;
		case R.id.btn_search:
			search_content = search_key.getText().toString();
			if (TextUtils.isEmpty(search_content)) {
				return;
			}
			if (search_content.length() == 11) {
				String url = "http://" + QApp.xmppConnection.getHost()
						+ ":9090/plugins/jsmuser/getaccountbyphone?phone="
						+ search_content + "&type=get";
				Logger.i(TAG, url);
				HttpUtils http = new HttpUtils();
				RequestParams params = new RequestParams();
				http.send(HttpMethod.GET, url, params,
						new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								Logger.e(TAG, "获取微迪号失败：" + arg1);

							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								Logger.e(TAG, "获取微迪号成功：" + arg0.result);
								search_content = arg0.result;
								searchUser();
							}
						});
			} else {
				searchUser();
			}
			break;
		}
	}

	private void searchUser() {
		loadingDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				listUser = XmppUtil.searchUsers(QApp.xmppConnection,
						search_content);
				if (listUser.size() > 0) {
					mHandler.sendEmptyMessage(1);
				} else {
					mHandler.sendEmptyMessage(-1);
				}
			}
		}).start();
	}

	@SuppressWarnings("deprecation")
	private void showPopupWindow(View parent, final String toUser) {
		if (popWindow == null) {
			View view = layoutInflater.inflate(R.layout.pop_edit, null);
			popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT, true);
			initPop(view, toUser);
		}
		popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	public void initPop(View view, final String toUser) {
		edit = (EditText) view.findViewById(R.id.edit);
		btn_ok = (Button) view.findViewById(R.id.btn_ok);// 确定
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);// 取消
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(edit.getText().toString())) {
					return;
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
		
						
							XMPPConnection con = QApp.xmppConnection;
							String toJid = toUser + "@"
									+ con.getServiceName();
							Logger.i(TAG, "请求添加好友1："+toJid);
							
//							XmppUtil.sendAddFriendRequest(con,toJid ,Presence.Type.subscribe);
	                   
							sendInviteUser = toUser;
							mHandler.sendEmptyMessage(2);
					}
				}).start();
				popWindow.dismiss();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();
			}
		});
	}

	/**
	 * 弹出输入法窗口
	 */
	private void popupInputMethodWindow() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				((InputMethodManager) edit.getContext().getSystemService(
						Service.INPUT_METHOD_SERVICE)).toggleSoftInput(0,
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 0);
	}

}
