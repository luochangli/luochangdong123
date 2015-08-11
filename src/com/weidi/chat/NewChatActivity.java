package com.weidi.chat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.activity.ImageGridActivity;
import com.weidi.adapter.FaceVPAdapter;
import com.weidi.adapter.NewChatAdapter;
import com.weidi.bean.ChatItem;
import com.weidi.bean.User;
import com.weidi.common.base.BaseActivity;
import com.weidi.common.function.recoding.AudioRecorder;
import com.weidi.common.function.recoding.RecordButton;
import com.weidi.common.function.recoding.RecordButton.RecordListener;
import com.weidi.common.image.ImgConfig;
import com.weidi.db.ChatDao;
import com.weidi.db.SessionDao;
import com.weidi.db.VCardDao;
import com.weidi.util.Const;
import com.weidi.util.ExpressionUtil;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;
import com.weidi.view.CircleImageView;
import com.weidi.view.DropdownListView;
import com.weidi.view.DropdownListView.OnRefreshListenerHeader;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-16 下午3:07:25
 * @Description 1.0 新聊天界面
 */
public class NewChatActivity extends BaseActivity implements OnClickListener,
		OnRefreshListenerHeader {

	public static final String TAG = NewChatActivity.class.getSimpleName();
	public static NewChatActivity instance = null;
	ImageView tvChatLeft;

	TextView tvChatTitle;
	private EditText input;
	CircleImageView tvChatRight;
	public RecordButton tvSpeek;
	ImageView btnFace, btnAdd;
	LinearLayout btnVoice, btnSendSms, btnChatKeyboard;

	LinearLayout chat_face_container, chat_add_container;
	private Boolean MUC_CHAT = false;
	DropdownListView chatListView;
	private NewChatAdapter adapter;
	private ViewPager mViewPager;
	private LinearLayout mDotsLayout;
	private LayoutInflater inflater;

	// 表情图标每页6列4行
	private int columns = 6;
	private int rows = 4;
	// 每页显示的表情view
	private List<View> views = new ArrayList<View>();
	// 表情列表
	private List<String> staticFacesList;
	/**
	 * 聊天多功能选项 图片、拍照、视频、位置
	 */
	private TextView tv_pic,// 图片
			tv_camera,// 拍照
			tv_video;// 视频

	private VCardDao userDao;
	private ChatDao chatDao;
	private SessionDao sessionDao;
	private List<ChatItem> listChat;
	private User friend;
	private SimpleDateFormat sd;
	private int offset;
	public static String YOU;
	private String I;

	// 打开相机用到的参数
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	private static final int UPDATE_ADAPTER = 2;
	private static final int RECV_MSG = 6;

	private static String picFileFullName;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.main_chat);
		YOU = getIntent().getStringExtra("from");
		initBroadcast();
		instance = this;
		initView();
		initData();

		// 更新与该用户的聊天记录全部为已读
		updateMsgToReaded();
	}

	private void initView() {
		tvChatLeft = (ImageView) findViewById(R.id.tvChatLeft);
		tvChatTitle = (TextView) findViewById(R.id.tvChatTitle);
		tvChatRight = (CircleImageView) findViewById(R.id.tvChatRight);
		btnFace = (ImageView) findViewById(R.id.image_face);
		btnAdd = (ImageView) findViewById(R.id.image_add);
		btnVoice = (LinearLayout) findViewById(R.id.tvVoice);
		btnSendSms = (LinearLayout) findViewById(R.id.send_sms);
		btnChatKeyboard = (LinearLayout) findViewById(R.id.btnChatKeyboard);
		chat_face_container = (LinearLayout) findViewById(R.id.chat_face_container);
		chat_add_container = (LinearLayout) findViewById(R.id.chat_add_container);
		chatListView = (DropdownListView) findViewById(R.id.message_chat_listview);
		input = (EditText) findViewById(R.id.input_sms);
		tvSpeek = (RecordButton) findViewById(R.id.tvSpeek);
		mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
		mViewPager.setOnPageChangeListener(new PageChange());
		// 表情下小圆点
		mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		tv_pic = (TextView) findViewById(R.id.tv_pic);
		tv_camera = (TextView) findViewById(R.id.tv_camera);
		tv_video = (TextView) findViewById(R.id.tv_video);

		initRightHead();
		initChatListView();

	}

	private void initChatListView() {
		chatListView.setOnRefreshListenerHead(this);
		chatListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					if (chat_face_container.getVisibility() == View.VISIBLE) {
						chat_face_container.setVisibility(View.GONE);
					}
					if (chat_add_container.getVisibility() == View.VISIBLE) {
						chat_add_container.setVisibility(View.GONE);
					}
					hideSoftInputView();
				}
				return false;
			}
		});
	}

	private void initRightHead() {
		if (YOU.contains("g")) {
			MUC_CHAT = true;
			tvChatRight.setImageResource(R.drawable.icon_groupchatinfo);
			tvChatRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(NewChatActivity.this,
							GroupChatSettingActi.class);
					intent.putExtra(Const.YOU, YOU);
					startActivity(intent);
				}
			});
		} else {
			tvChatRight.setImageResource(R.drawable.btn_from);
			ImgConfig.showHeadImg(YOU, tvChatRight);
			tvChatRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(NewChatActivity.this,
							ChatInfoActi.class);
					intent.putExtra(Const.YOU, YOU);
					startActivity(intent);
				}
			});

		}
	}

	private void initBroadcast() {
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Const.ACTION_NEW_MSG)) {
					ChatItem item = (ChatItem) intent
							.getSerializableExtra(ChatItem.TABLE_NAME);
					if (item.getTo().equals(YOU)) {
						item.setIsRead(ChatItem.STATUS_1);
						chatDao.updateReaded(item);
						Message msg = Message.obtain();
						msg.what = RECV_MSG;
						msg.obj = item;
						mHandler.sendMessage(msg);
					}

				}

			}
		};
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				Const.ACTION_NEW_MSG));
		mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(
				Const.ACTION_MSG_OPER));
	}

	private void updateMsgToReaded() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				chatDao.updateAllMsgToRead(YOU);
			}
		}).start();
	}

	private void initData() {
		userDao = VCardDao.getInstance();
		chatDao = ChatDao.getInstance();
		sessionDao = SessionDao.getInstance();
		sd = new SimpleDateFormat("MM-dd HH:mm");
		offset = 0;
		I = PreferencesUtils.getSharePreStr("weidi");
		friend = userDao.getUser(YOU);

		if (MUC_CHAT) {
			tvChatTitle.setText(YOU);
		} else {
			if (friend != null) {
				tvChatTitle.setText(friend.getNickname() == null ? YOU : friend
						.getNickname());
			}
		}

		listChat = chatDao.queryMsg(I, YOU, offset);
		offset = listChat.size();
		staticFacesList = ExpressionUtil.initStaticFaces(this);
		adapter = new NewChatAdapter(listChat, this);
		chatListView.setAdapter(adapter);
		chatListView.setSelection(listChat.size());
		initViewPager();// 初始化表情
	}

	/**
	 * 初始化表情
	 */
	private void initViewPager() {
		int pagesize = ExpressionUtil.getPagerCount(staticFacesList.size(),
				columns, rows);
		// 获取页数
		for (int i = 0; i < pagesize; i++) {
			views.add(ExpressionUtil.viewPagerItem(this, i, staticFacesList,
					columns, rows, input));
			LayoutParams params = new LayoutParams(16, 16);
			mDotsLayout.addView(dotsItem(i), params);
		}
		FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
		mViewPager.setAdapter(mVpAdapter);
		mDotsLayout.getChildAt(0).setSelected(true);
	}

	/**
	 * 表情页切换时，底部小圆点
	 * 
	 * @param position
	 * @return
	 */
	private ImageView dotsItem(int position) {
		View layout = inflater.inflate(R.layout.dot_image, null);
		ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
		iv.setId(position);
		return iv;
	}

	@Override
	protected void setListener() {

		tvChatLeft.setOnClickListener(this);
		btnVoice.setOnClickListener(this);
		btnSendSms.setOnClickListener(this);
		btnChatKeyboard.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
		btnFace.setOnClickListener(this);
		tv_camera.setOnClickListener(this);
		tv_pic.setOnClickListener(this);
		tv_video.setOnClickListener(this);
		inputChange();
		tvSpeek.setAudioRecord(new AudioRecorder());
		tvSpeek.setRecordListener(new RecordListener() {

			@Override
			public void recordEnd(String filePath) {
				ChatItem item = setChatItem(filePath,
						NewChatAdapter.MESSAGE_TYPE_SENT_VOICE);
				item.setChatType(Const.MSG_TYPE_VOICE);
				toHandle(item);
			}
		});
	}

	private void inputChange() {
		input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					btnSendSms.setVisibility(View.VISIBLE);
					btnVoice.setVisibility(View.GONE);
					if (tvSpeek.getVisibility() == View.VISIBLE) {
						btnChatKeyboard.setVisibility(View.GONE);
						tvSpeek.setVisibility(View.GONE);
						input.setVisibility(View.VISIBLE);
					}
				} else {
					if (btnVoice.getVisibility() != View.VISIBLE) {
						btnVoice.setVisibility(View.VISIBLE);
						btnSendSms.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void handleMsg(Message msg) {
		switch (msg.what) {
		case UPDATE_ADAPTER:
			sendMsg(msg);
			break;
		case RECV_MSG:
			ChatItem item = (ChatItem) msg.obj;
			listChat.add(item);
			offset = listChat.size();
			chatListView.setSelection(chatListView.getBottom());
			adapter.notifyDataSetChanged();
			break;
		}
	}

	private void sendMsg(Message msg) {
		try {
			ChatItem item = (ChatItem) msg.obj;
			int row = (int) chatDao.insert(item);
			Logger.i(TAG, "数据保存：" + row);
			item.set_id(row);
			listChat.add(item);
			offset = listChat.size();
			chatListView.setSelection(chatListView.getBottom());
			adapter.notifyDataSetChanged();

		} catch (Exception e) {

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvChatLeft:
			finish();
			break;
		case R.id.tvVoice:
			btnVoice();
			break;
		case R.id.send_sms:
			String content = input.getText().toString();
			if (TextUtils.isEmpty(content)) {
				return;
			}

			ChatItem item = setChatItem(content,
					NewChatAdapter.MESSAGE_TYPE_SENT_TXT);
			item.setChatType(Const.MSG_TYPE_TEXT);
			toHandle(item);
			input.setText("");
			break;
		case R.id.btnChatKeyboard:
			showEditState();
			break;

		case R.id.image_face:
			showFace();

			break;
		case R.id.image_add:
			showAdd();

			break;
		case R.id.tv_pic:
			selectImageFromLocal();
			break;

		case R.id.tv_camera:
			takePicture();
			break;
		case R.id.tv_video:
			// 点击摄像图标
			showVideo();
			break;

		default:
			break;
		}

	}

	private void toHandle(ChatItem item) {
		Message msg = Message.obtain();
		msg.what = UPDATE_ADAPTER;
		msg.obj = item;
		mHandler.sendMessage(msg);

	}

	private ChatItem setChatItem(String content, int viewType) {
		ChatItem item = new ChatItem();
		item.setMe(I);
		item.setTo(YOU);
		if (MUC_CHAT) {
			item.setIsGroup(ChatItem.STATUS_1);
		}
		String time = sd.format(new Date());
		item.setViewTyep(viewType);
		item.setIsRead(ChatItem.STATUS_1);
		item.setDate(time);
		item.setContent(content);

		return item;
	}

	private void showVideo() {
		Intent intent = new Intent(NewChatActivity.this,
				ImageGridActivity.class);
		startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
	}

	private void takePicture() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File outDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			if (!outDir.exists()) {
				outDir.mkdirs();
			}
			File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
			picFileFullName = outFile.getAbsolutePath();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		} else {
			Log.e(TAG, "请确认已经插入SD卡");
		}
	}

	public void selectImageFromLocal() {
		Intent intent = new Intent();
		/* 开启Pictures画面Type设定为image */
		intent.setType("image/*");
		/* 使用Intent.ACTION_GET_CONTENT这个Action */
		intent.setAction(Intent.ACTION_GET_CONTENT);
		/* 取得相片后返回本画面 */
		startActivityForResult(intent, 1);
	}

	private void showAdd() {
		hideSoftInputView();// 隐藏软键盘
		if (chat_face_container.getVisibility() == View.VISIBLE) {
			chat_face_container.setVisibility(View.GONE);
		}
		if (chat_add_container.getVisibility() == View.GONE) {
			chat_add_container.setVisibility(View.VISIBLE);
		} else {
			chat_add_container.setVisibility(View.GONE);
		}
	}

	private void showFace() {
		hideSoftInputView();// 隐藏软键盘
		if (chat_add_container.getVisibility() == View.VISIBLE) {
			chat_add_container.setVisibility(View.GONE);
		}
		if (chat_face_container.getVisibility() == View.GONE) {
			chat_face_container.setVisibility(View.VISIBLE);
		} else {
			chat_face_container.setVisibility(View.GONE);
		}
	}

	private void showEditState() {
		hideContainer();
		input.setVisibility(View.VISIBLE);
		btnChatKeyboard.setVisibility(View.GONE);
		btnVoice.setVisibility(View.VISIBLE);
		tvSpeek.setVisibility(View.GONE);
		btnSendSms.setVisibility(View.GONE);
		input.requestFocus();
		showSoftInputView(input);
	}

	/**
	 * 弹出输入法窗口
	 */
	private void showSoftInputView(final View v) {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				((InputMethodManager) v.getContext().getSystemService(
						Service.INPUT_METHOD_SERVICE)).toggleSoftInput(0,
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 100);
	}

	/**
	 * 录音键
	 */
	private void btnVoice() {
		input.setVisibility(View.GONE);
		btnVoice.setVisibility(View.GONE);
		btnChatKeyboard.setVisibility(View.VISIBLE);
		tvSpeek.setVisibility(View.VISIBLE);
		hideSoftInputView();// 隐藏软键盘
		hideContainer();
	}

	private void hideContainer() {
		chat_add_container.setVisibility(View.GONE);
		chat_face_container.setVisibility(View.GONE);
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) this
				.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				getLocalPic(data);
				break;
			case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
				if (resultCode == RESULT_OK) {
					Logger.e(TAG, "获取图片成功，path=" + picFileFullName);
					ChatItem item = setChatItem(picFileFullName,
							NewChatAdapter.MESSAGE_TYPE_SENT_IMAGE);
					item.setChatType(Const.MSG_TYPE_IMG);
					toHandle(item);
				} else if (resultCode == RESULT_CANCELED) {
					// 用户取消了图像捕获
				} else {
					// 图像捕获失败，提示用户
					Logger.e(TAG, "拍照失败");
				}
				break;
			case REQUEST_CODE_SELECT_VIDEO:// 发送本地选择的视频
				String videoPath = data.getStringExtra("path");
				// sendMsgFile(videoPath);
				ChatItem item = setChatItem(videoPath,
						NewChatAdapter.MESSAGE_TYPE_SENT_VIDEO);
				item.setChatType(Const.MSG_TYPE_VIDEO);
				toHandle(item);

			default:
				break;
			}

			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void getLocalPic(Intent data) {
		if (data == null)
			return;
		Uri uri = data.getData();
		if (uri != null) {
			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex("_data");
			String localSelectPath = cursor.getString(columnIndex);
			Logger.i(TAG, "获取本地图片：" + localSelectPath);
			cursor.close();
			if (localSelectPath == null || localSelectPath.equals("")) {
				ToastUtil.showLongToast(this, "找不到您想要的图片");
				return;
			} else {
				ChatItem item = setChatItem(localSelectPath,
						NewChatAdapter.MESSAGE_TYPE_SENT_IMAGE);
				item.setChatType(Const.MSG_TYPE_IMG);
				toHandle(item);
			}
		}
	}

	@Override
	protected void onDestroy() {
		mLocalBroadcastManager.unregisterReceiver(mReceiver);
		instance = null;
		YOU = null;
		super.onDestroy();
	}

	/**
	 * 表情页改变时，dots效果也要跟着改变
	 * */
	class PageChange implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
				mDotsLayout.getChildAt(i).setSelected(false);
			}
			mDotsLayout.getChildAt(arg0).setSelected(true);
		}
	}

	@Override
	public void onRefresh() {
		List<ChatItem> list = chatDao.queryMsg(I, YOU, offset);

		if (list.size() <= 0) {
			chatListView.setSelection(0);
			chatListView.onRefreshCompleteHeader();
			return;
		}
		listChat.addAll(0, list);
		offset = listChat.size();
		chatListView.onRefreshCompleteHeader();
		adapter.notifyDataSetChanged();
		chatListView.setSelection(list.size());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			hideSoftInputView();
			if (chat_face_container.getVisibility() == View.VISIBLE) {
				chat_face_container.setVisibility(View.GONE);
			} else if (chat_add_container.getVisibility() == View.VISIBLE) {
				chat_add_container.setVisibility(View.GONE);
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
