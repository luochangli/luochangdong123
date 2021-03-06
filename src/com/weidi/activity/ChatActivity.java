package com.weidi.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.ClipboardManager;
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
import android.widget.Toast;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.adapter.ChatAdapter;
import com.weidi.adapter.FaceVPAdapter;
import com.weidi.bean.Msg;
import com.weidi.bean.Session;
import com.weidi.chat.ChatInfoActi;
import com.weidi.chat.GroupChatSettingActi;
import com.weidi.common.MediaFile;
import com.weidi.common.UploadUtil;
import com.weidi.common.UploadUtil.UpCallback;
import com.weidi.common.function.recoding.AudioRecorder;
import com.weidi.common.function.recoding.RecordButton;
import com.weidi.common.function.recoding.RecordButton.RecordListener;
import com.weidi.common.image.ImgConfig;
import com.weidi.db.ChatMsgDao;
import com.weidi.db.SessionDao;
import com.weidi.util.Const;
import com.weidi.util.ExpressionUtil;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ShowPopWindow;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;
import com.weidi.view.DropdownListView;
import com.weidi.view.DropdownListView.OnRefreshListenerHeader;

/**
 * 聊天界面
 * 
 * @author 白玉梁
 * @blog http://blog.csdn.net/baiyuliang2013
 * @weibo http://weibo.com/274433520
 * 
 * */
@SuppressLint("SimpleDateFormat")
public class ChatActivity extends Activity implements OnClickListener,
		OnRefreshListenerHeader {
	private static String TAG = "ChatActivity";
	private ViewPager mViewPager;
	private LinearLayout mDotsLayout;
	private EditText input;
	private LinearLayout send,btnChatKeyboard,tvVoice;
	private ImageView tvBack;
	public static Boolean MUC_CHAT = false;
	CircleImageView tvRightHead;
	/**
	 * 语音按钮
	 */
	public RecordButton tvSpeek;
	private DropdownListView mListView;
	private ChatAdapter mLvAdapter;
	private ChatMsgDao msgDao;
	private SessionDao sessionDao;
	private LinearLayout chat_face_container, chat_add_container;
	private ImageView image_face;// 表情图标
	private ImageView image_add;// 更多图标

	/**
	 * 聊天多功能选项 图片、拍照、视频、位置
	 */
	private TextView tvChatTitle, tv_pic,// 图片
			tv_camera,// 拍照
			tv_video,// 视频
			tv_loc;// 位置

	// 表情图标每页6列4行
	private int columns = 6;
	private int rows = 4;
	// 每页显示的表情view
	private List<View> views = new ArrayList<View>();
	// 表情列表
	private List<String> staticFacesList;
	// 消息
	private List<Msg> listMsg;
	private SimpleDateFormat sd;
	private NewMsgReciver newMsgReciver;
	private MsgOperReciver msgOperReciver;
	private LayoutInflater inflater;
	private int offset;
	public static String I, YOU;// 为了好区分，I就是自己，YOU就是对方

	private MessageListener msgListener;
	// 打开相机用到的参数
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	private static String picFileFullName;

	public Boolean isSpeek = false;
	View view;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mLvAdapter.notifyDataSetChanged();
				break;
			}
		}
	};

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_chat);
		I = PreferencesUtils.getSharePreStr("username");
		if (I.length() == 11) {
			I = PreferencesUtils.getSharePreStr("weidi");
		}
		YOU = getIntent().getStringExtra("from");
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		tvChatTitle = (TextView) findViewById(R.id.tvChatTitle);
		tvChatTitle.setText(YOU);

		sd = new SimpleDateFormat("MM-dd HH:mm");
		msgDao = new ChatMsgDao(this);
		sessionDao = new SessionDao(this);
		msgOperReciver = new MsgOperReciver();
		newMsgReciver = new NewMsgReciver();
		IntentFilter intentFilter = new IntentFilter(Const.ACTION_MSG_OPER);
		registerReceiver(msgOperReciver, intentFilter);
		intentFilter = new IntentFilter(Const.ACTION_NEW_MSG);
		registerReceiver(newMsgReciver, intentFilter);

		staticFacesList = ExpressionUtil.initStaticFaces(this);
		// 初始化控件
		initViews();
		// 初始化表情
		initViewPager();
		// 初始化更多选项（即表情图标右侧"+"号内容）
		initAdd();
		// 初始化数据
		initData();
		// 更新与该用户的聊天记录全部为已读
		updateMsgToReaded();

	}

	private void updateMsgToReaded() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				msgDao.updateAllMsgToRead(YOU, I);
			}
		}).start();
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
	
		mListView = (DropdownListView) findViewById(R.id.message_chat_listview);
		// 表情图标
		image_face = (ImageView) findViewById(R.id.image_face);
		// 更多图标
		image_add = (ImageView) findViewById(R.id.image_add);
		// 表情布局
		chat_face_container = (LinearLayout) findViewById(R.id.chat_face_container);
		// 更多
		chat_add_container = (LinearLayout) findViewById(R.id.chat_add_container);

		mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
		mViewPager.setOnPageChangeListener(new PageChange());
		// 表情下小圆点
		mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
		input = (EditText) findViewById(R.id.input_sms);
		tvRightHead = (CircleImageView) findViewById(R.id.tvChatRight);
		tvBack = (ImageView) findViewById(R.id.tvChatLeft);

		send = (LinearLayout) findViewById(R.id.send_sms);
		input.setOnClickListener(this);

		// 表情按钮
		image_face.setOnClickListener(this);
		// 更多按钮
		image_add.setOnClickListener(this);
		// 发送
		send.setOnClickListener(this);
		// 语音对讲
		tvVoice = (LinearLayout) findViewById(R.id.tvVoice);
		tvSpeek = (RecordButton) findViewById(R.id.tvSpeek);
		btnChatKeyboard = (LinearLayout) findViewById(R.id.btnChatKeyboard);

		tvBack.setOnClickListener(this);
		tvVoice.setOnClickListener(this);
		btnChatKeyboard.setOnClickListener(this);
		tvSpeek.setAudioRecord(new AudioRecorder());
		tvSpeek.setRecordListener(new RecordListener() {

			@Override
			public void recordEnd(String filePath) {
				sendMsgFile(filePath);

			}
		});
		input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					send.setVisibility(View.VISIBLE);
					tvVoice.setVisibility(View.GONE);
					if (tvSpeek.getVisibility() == View.VISIBLE) {
						btnChatKeyboard.setVisibility(View.GONE);
						tvSpeek.setVisibility(View.GONE);
						input.setVisibility(View.VISIBLE);
					}
				} else {
					if (tvVoice.getVisibility() != View.VISIBLE) {
						tvVoice.setVisibility(View.VISIBLE);
						send.setVisibility(View.GONE);
					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		mListView.setOnRefreshListenerHead(this);
		mListView.setOnTouchListener(new OnTouchListener() {
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

	private void initAdd() {
		tv_pic = (TextView) findViewById(R.id.tv_pic);
		tv_camera = (TextView) findViewById(R.id.tv_camera);
		tv_loc = (TextView) findViewById(R.id.tv_loc);
		tv_loc.setVisibility(View.GONE);
		tv_video = (TextView) findViewById(R.id.tv_video);
		tv_pic.setOnClickListener(this);
		tv_camera.setOnClickListener(this);
		tv_loc.setOnClickListener(this);
		tv_video.setOnClickListener(this);

		if (YOU.contains("g")) {
			MUC_CHAT = true;
			tvRightHead.setImageResource(R.drawable.icon_groupchatinfo);
			tvRightHead.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ChatActivity.this,
							GroupChatSettingActi.class);
					intent.putExtra(Const.YOU, YOU);
					startActivity(intent);
				}
			});
		} else {
			tvRightHead.setImageResource(R.drawable.btn_from);
//			ImgConfig.showHeadImg(YOU, tvRightHead); 
			tvRightHead.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent =  new Intent(ChatActivity.this, ChatInfoActi.class);
					intent.putExtra(Const.YOU, YOU);
					startActivity(intent);
					
				}
			});
		}
	}

	public void initData() {
		offset = 0;
		listMsg = msgDao.queryMsg(YOU, I, offset);
		offset = listMsg.size();
		mLvAdapter = new ChatAdapter(this, listMsg);
		mListView.setAdapter(mLvAdapter);
		mListView.setSelection(listMsg.size());
	}

	/**
	 * 打开相机
	 */
	public void takePicture() {
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

	/**
	 */
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tvChatLeft:
			finish();
			break;
		case R.id.send_sms:
			String content = input.getText().toString();
			if (TextUtils.isEmpty(content)) {
				return;
			}
			sendMsgText(content);
			break;
		case R.id.input_sms:
			if (chat_face_container.getVisibility() == View.VISIBLE) {
				chat_face_container.setVisibility(View.GONE);
			}
			if (chat_add_container.getVisibility() == View.VISIBLE) {
				chat_add_container.setVisibility(View.GONE);
			}
			break;
		case R.id.btnChatKeyboard:
			showEditState(false);
			break;
		case R.id.image_face:
			hideSoftInputView();// 隐藏软键盘
			if (chat_add_container.getVisibility() == View.VISIBLE) {
				chat_add_container.setVisibility(View.GONE);
			}
			if (chat_face_container.getVisibility() == View.GONE) {
				chat_face_container.setVisibility(View.VISIBLE);
			} else {
				chat_face_container.setVisibility(View.GONE);
			}
			break;
		case R.id.image_add:
			hideSoftInputView();// 隐藏软键盘
			if (chat_face_container.getVisibility() == View.VISIBLE) {
				chat_face_container.setVisibility(View.GONE);
			}
			if (chat_add_container.getVisibility() == View.GONE) {
				chat_add_container.setVisibility(View.VISIBLE);
			} else {
				chat_add_container.setVisibility(View.GONE);
			}
			break;
		case R.id.tvVoice:
			input.setVisibility(View.GONE);
			tvVoice.setVisibility(View.GONE);
			btnChatKeyboard.setVisibility(View.VISIBLE);
			tvSpeek.setVisibility(View.VISIBLE);
			hideSoftInputView();// 隐藏软键盘
			break;
		case R.id.tv_pic:// 模拟一张图片路径
			selectImageFromLocal();

			// sendMsgImg("http://my.csdn.net/uploads/avatar/3/B/9/1_baiyuliang2013.jpg");
			break;
		case R.id.tv_camera:// 拍照，换个美女图片吧
			takePicture();
			// sendMsgImg("http://b.hiphotos.baidu.com/image/pic/item/55e736d12f2eb93872b0d889d6628535e4dd6fe8.jpg");
			break;
		case R.id.tv_loc:// 位置，正常情况下是需要定位的，可以用百度或者高德地图，现设置为北京坐标
			sendMsgLocation("116.404,39.915");
			break;
		case R.id.tv_video:
			// 点击摄像图标
			Intent intent = new Intent(ChatActivity.this,
					ImageGridActivity.class);
			startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
			break;
		}
	}

	private void showEditState(boolean isEmo) {
		input.setVisibility(View.VISIBLE);
		btnChatKeyboard.setVisibility(View.GONE);
		tvVoice.setVisibility(View.VISIBLE);
		tvSpeek.setVisibility(View.GONE);
		send.setVisibility(View.GONE);
		input.requestFocus();
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
					sendMsgFile(picFileFullName);
				} else if (resultCode == RESULT_CANCELED) {
					// 用户取消了图像捕获
				} else {
					// 图像捕获失败，提示用户
					Logger.e(TAG, "拍照失败");
				}
				break;
			case REQUEST_CODE_SELECT_VIDEO:// 发送本地选择的视频

				int duration = data.getIntExtra("dur", 0);
				String videoPath = data.getStringExtra("path");
				sendMsgFile(videoPath);
				Logger.e(TAG, videoPath);

			default:
				break;
			}

			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * This method is used to get real path of file from from uri<br/>
	 * http://stackoverflow.com/questions/11591825/how-to-get-image-path-just-
	 * captured-from-camera
	 * 
	 * @param contentUri
	 * @return String
	 */
	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			// Do not call Cursor.close() on a cursor obtained using this
			// method,
			// because the activity will do that for you at the appropriate time
			Cursor cursor = this.managedQuery(contentUri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

	/**
	 * 获取本地图片
	 * 
	 * @param data
	 */
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
				sendMsgFile(localSelectPath);
			}
		}
	}

	/**
	 * 上传路径URL
	 * 
	 * @param host
	 * @param userName
	 * @param fileName
	 * @return
	 */
	public String upLoadUrl(String host, String userName, String fileName) {
		StringBuilder sb = new StringBuilder();
		sb.append("http://");
		sb.append(host + ":9090");
		sb.append("/plugins/jsmfiles/fileupload?");
		sb.append("username=");
		sb.append(userName);
		sb.append("&filename=");
		sb.append(fileName);
		Logger.i(TAG, sb.toString());
		return sb.toString();
	}

	/**
	 * 执行发送消息 图片类型
	 * 
	 * @param content
	 */
	void sendMsgFile(final String filePath) {

		final File file = new File(filePath);
		final String host = QApp.getXmppConnection().getHost();
		String date = com.weidi.common.DateUtil.getCurDateStr("yyyyMMddHHmmss");
		final String ext = UploadUtil.getExtensionName(file.getName());
		final StringBuilder fileName = new StringBuilder();
		if (YOU.contains("g")) {
			fileName.append(YOU);
		} else {
			fileName.append(I);
		}

		fileName.append(date);
		fileName.append(".");
		fileName.append(ext);
		final String upUrl;
		if (YOU.contains("g")) {
			upUrl = upLoadUrl(host, YOU, fileName.toString());
		} else {
			upUrl = upLoadUrl(host, I, fileName.toString());
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				UploadUtil.uploadFile(file, upUrl, new UpCallback() {

					@Override
					public void upSendMsg() {
						if (MediaFile.isImageFileType(filePath)) {
							sendMsg(fileName.toString(), Const.MSG_TYPE_IMG);
						}
						if (ext.equals("amr")) {
							sendMsg(fileName.toString(), Const.MSG_TYPE_VOICE);
						}
						if (MediaFile.isVideoFileType(filePath)) {
							sendMsg(fileName.toString(), Const.MSG_TYPE_VIDEO);
						}
					}

					@Override
					public void onUploadProcess(int current, int total) {
						if (current != 0 && current <= total) {

						}

					}

					@Override
					public void onLoadingFailed() {
						ToastUtil.showLongToast(ChatActivity.this, "文件上传失败！");

					}
				});
			}
		}).start();
		if (ext.equals("amr")) {
			noticeUpdate(filePath, Const.MSG_TYPE_VOICE, fileName);

		}
		if (MediaFile.isImageFileType(filePath)) {
			noticeImg(filePath, host, fileName);
		}
		if (MediaFile.isVideoFileType(filePath)) {
			noticeUpdate(filePath, Const.MSG_TYPE_VIDEO, fileName);
		}
		// xutilsUpload(file, upUrl);
		// 在本地显示的

	}

	private void sendMsg(String fileName, String msgType) {

		try {

			XmppUtil.sendFileMsg(fileName, msgType, YOU);

		} catch (XMPPException e) {
			e.printStackTrace();
			Looper.prepare();
			ToastUtil.showShortToast(ChatActivity.this, "发送失败");
			Looper.loop();
		}
	}

	private void noticeUpdate(final String filePath, String fileType,
			final StringBuilder fileName) {

		Msg msg = getChatInfoTo(filePath, fileType);
		msg.setMsgId(msgDao.insert(msg));

		listMsg.add(msg);
		offset = listMsg.size();
		if (fileType.equals(Const.MSG_TYPE_VOICE)) {
			updateSession(Const.MSG_TYPE_TEXT, "[语音]");
		}
		if (fileType.equals(Const.MSG_TYPE_VIDEO)) {
			updateSession(Const.MSG_TYPE_TEXT, "[视频]");
		}
		mHandler.sendEmptyMessage(1);
	}

	private void noticeImg(final String filePath, final String host,
			final StringBuilder fileName) {

		Msg msg = getChatInfoTo("file:///" + filePath, Const.MSG_TYPE_IMG);
		msg.setMsgId(msgDao.insert(msg));
		msg.setBak1(Const.UPLOAD_START);
		listMsg.add(msg);
		offset = listMsg.size();
		updateSession(Const.MSG_TYPE_TEXT, "[图片]");
		mHandler.sendEmptyMessage(1);
	}

	/**
	 * 执行发送消息 文本类型
	 * 
	 * @param content
	 */
	void sendMsgText(final String content) {
		Msg msg = getChatInfoTo(content, Const.MSG_TYPE_TEXT);
		msg.setMsgId(msgDao.insert(msg));
		listMsg.add(msg);
		offset = listMsg.size();
		mLvAdapter.notifyDataSetChanged();
		input.setText("");
		try {

			XmppUtil.sendTextMsg(content, YOU);

		} catch (XMPPException e) {
			e.printStackTrace();
			Looper.prepare();
			ToastUtil.showShortToast(ChatActivity.this, "发送失败");
			Looper.loop();
		}
		updateSession(Const.MSG_TYPE_TEXT, content);
	}

	/**
	 * 执行发送消息 文本类型
	 * 
	 * @param content
	 */
	void sendMsgLocation(String content) {
		Msg msg = getChatInfoTo(content, Const.MSG_TYPE_LOCATION);
		msg.setMsgId(msgDao.insert(msg));
		listMsg.add(msg);
		offset = listMsg.size();
		mLvAdapter.notifyDataSetChanged();
		final String message = YOU + Const.SPLIT + I + Const.SPLIT
				+ Const.MSG_TYPE_LOCATION + Const.SPLIT + content + Const.SPLIT
				+ sd.format(new Date());
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					XmppUtil.sendMessage(QApp.getXmppConnection(), message,
							YOU, msgListener);
				} catch (XMPPException e) {
					e.printStackTrace();
					Looper.prepare();
					ToastUtil.showShortToast(ChatActivity.this, "发送失败");
					Looper.loop();
				}
			}
		}).start();
		updateSession(Const.MSG_TYPE_TEXT, "[位置]");
	}

	/**
	 * 发送的信息 from为收到的消息，to为自己发送的消息
	 * 
	 * @param message
	 *            => 接收者卍发送者卍消息类型卍消息内容卍发送时间
	 * @return
	 */
	private Msg getChatInfoTo(String message, String msgtype) {
		String time = sd.format(new Date());
		Msg msg = new Msg();
		msg.setFromUser(YOU);
		msg.setToUser(I);
		msg.setType(msgtype);
		msg.setIsComing(1);
		msg.setContent(message);
		msg.setDate(time);
		return msg;
	}

	void updateSession(String type, String content) {
		Session session = new Session();
		session.setFrom(YOU);
		session.setTo(I);
		session.setNotReadCount("");// 未读消息数量
		session.setContent(content);
		session.setTime(sd.format(new Date()));
		session.setType(type);
		if (sessionDao.isContain(YOU, I)) {
			sessionDao.updateSession(session);
		} else {
			sessionDao.insertSession(session);
		}
		Intent intent = new Intent(Const.ACTION_ADDFRIEND);// 发送广播，通知消息界面更新
		sendBroadcast(intent);
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

	/**
	 * 下拉加载更多
	 */
	@Override
	public void onRefresh() {
		List<Msg> list = msgDao.queryMsg(YOU, I, offset);
		if (list.size() <= 0) {
			mListView.setSelection(0);
			mListView.onRefreshCompleteHeader();
			return;
		}
		listMsg.addAll(0, list);
		offset = listMsg.size();
		mListView.onRefreshCompleteHeader();
		mLvAdapter.notifyDataSetChanged();
		mListView.setSelection(list.size());
	}

	/**
	 * 弹出输入法窗口
	 */
	private void showSoftInputView(final View v) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				((InputMethodManager) v.getContext().getSystemService(
						Service.INPUT_METHOD_SERVICE)).toggleSoftInput(0,
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 0);
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

	/**
	 * 接收消息记录操作广播：删除复制
	 * 
	 * @author baiyuliang
	 */
	private class MsgOperReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int type = intent.getIntExtra("type", 0);
			final int position = intent.getIntExtra("position", 0);
			if (listMsg.size() <= 0) {
				return;
			}
			final Msg msg = listMsg.get(position);
			switch (type) {
			case 1:// 聊天记录操作
				Builder bd = new AlertDialog.Builder(ChatActivity.this);
				String[] items = null;
				if (msg.getType().equals(Const.MSG_TYPE_TEXT)) {
					items = new String[] { "删除记录", "删除全部记录", "复制文字" };
				} else {
					items = new String[] { "删除记录", "删除全部记录" };
				}
				bd.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						switch (arg1) {
						case 0:// 删除
							listMsg.remove(position);
							offset = listMsg.size();
							mLvAdapter.notifyDataSetChanged();
							msgDao.deleteMsgById(msg.getMsgId());
							break;
						case 1:// 删除全部
							listMsg.removeAll(listMsg);
							offset = listMsg.size();
							mLvAdapter.notifyDataSetChanged();
							msgDao.deleteAllMsg(YOU, I);
							break;
						case 2:// 复制
							ClipboardManager cmb = (ClipboardManager) ChatActivity.this
									.getSystemService(ChatActivity.CLIPBOARD_SERVICE);
							cmb.setText(msg.getContent());
							Toast.makeText(getApplicationContext(), "已复制到剪切板",
									Toast.LENGTH_SHORT).show();
							break;
						}
					}
				});
				bd.show();
				break;
			}

		}
	}

	/**
	 * 接收消息记录操作广播：删除复制
	 * 
	 * @author baiyuliang
	 */
	private class NewMsgReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle b = intent.getBundleExtra("msg");
			Msg msg = (Msg) b.getSerializable("msg");
			if (msg.getFromUser().equals(YOU)) {
				msg.setIsReaded(Const.MSG_READED);
				listMsg.add(msg);
				offset = listMsg.size();
				mLvAdapter.notifyDataSetChanged();
			}

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		YOU = null;
		unregisterReceiver(msgOperReciver);
		unregisterReceiver(newMsgReciver);
		ChatAdapter.stopVoicePlay();
	}

	@Override
	protected void onResume() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// 让输入框获取焦点
				input.requestFocus();
			}
		}, 100);
		super.onResume();
	};

	/**
	 * 选择图片
	 */
	public void selectImageFromLocal() {
		Intent intent = new Intent();
		/* 开启Pictures画面Type设定为image */
		intent.setType("image/*");
		/* 使用Intent.ACTION_GET_CONTENT这个Action */
		intent.setAction(Intent.ACTION_GET_CONTENT);
		/* 取得相片后返回本画面 */
		startActivityForResult(intent, 1);
	}

	/**
	 * 监听返回键
	 */
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
