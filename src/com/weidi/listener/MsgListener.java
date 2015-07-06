package com.weidi.listener;

import java.io.File;
import java.util.Date;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.activity.ChatActivity;
import com.weidi.bean.Msg;
import com.weidi.bean.Session;
import com.weidi.common.DateUtil;
import com.weidi.common.UploadUtil;
import com.weidi.db.ChatMsgDao;
import com.weidi.db.SessionDao;
import com.weidi.service.MsfService;
import com.weidi.util.Const;
import com.weidi.util.DebugOut;
import com.weidi.util.FileUtil;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-15
 * @Description 1.0
 */
@SuppressWarnings("static-access")
public class MsgListener implements MessageListener {
	private static String TAG = "MsgListener";
	private MsfService context;
	private NotificationManager mNotificationManager;

	private Notification mNotification;
	private KeyguardManager mKeyguardManager = null;

	private boolean isShowNotice = false;

	private ChatMsgDao msgDao;
	private SessionDao sessionDao;

	private String fileUrl;

	public MsgListener(MsfService context,
			NotificationManager mNotificationManager) {
		DebugOut.PrintlnOut(TAG + "-");
		this.context = context;
		this.mNotificationManager = mNotificationManager;
		mKeyguardManager = (KeyguardManager) context
				.getSystemService(context.KEYGUARD_SERVICE);
		sessionDao = new SessionDao(context);
		msgDao = new ChatMsgDao(context);
	}

	@Override
	public void processMessage(Chat arg0, Message message) {
		if (ChatActivity.activityInstance != null) {
			msgProcess(message,Const.MSG_READED);
		} else if (ChatActivity.activityInstance == null) {
			msgProcess(message,Const.MSG_UNREAD);
		}
	}

	/*private void msgYesProcess(Message message) {
		try {
			String msgBody = message.getBody();
			Logger.i(TAG, message.toXML());
			String to1 = StringUtils.parseName(message.getTo());
			String from1 = StringUtils.parseName(message.getFrom());
			if (TextUtils.isEmpty(msgBody))
				return;
			// 接收者卍发送者卍消息类型卍消息内容卍发送时间
			String[] msgs = msgBody.split(Const.SPLIT);
			String to = to1;// 接收者,当然是自己
			String from = from1;// 发送者，谁给你发的消息
			String msgtype = msgs[2];// 消息类型
			String msgcontent = msgs[3];// 消息内容
			String msgtime = msgs[4];// 消息时间

			Session session = setSession(to, from, msgtime);

			if (msgtype.equals(Const.MSG_TYPE_ADD_FRIEND)) {// 添加好友的请求
				session.setType(msgtype);
				session.setContent(msgcontent);
				session.setIsdispose("0");
				sessionDao.insertSession(session);
			} else if (msgtype.equals(Const.MSG_TYPE_ADD_FRIEND_SUCCESS)) {// 对方同意添加好友的请求
				session.setType(Const.MSG_TYPE_TEXT);
				session.setContent("我们已经是好友了，快来和我聊天吧！");
				sessionDao.insertSession(session);
				// 发送广播更新好友列表
				Intent intent = new Intent(
						Const.ACTION_FRIENDS_ONLINE_STATUS_CHANGE);
				context.sendBroadcast(intent);
			} else if (msgtype.equals(Const.MSG_TYPE_TEXT)) {// 文本类型
				Msg msg = new Msg();
				msg.setToUser(to);
				msg.setFromUser(from);
				msg.setIsComing(0);
				msg.setContent(msgcontent);
				msg.setDate(msgtime);
				msg.setIsReaded("1");
				msg.setType(msgtype);
				msgDao.insert(msg);
				sendNewMsg(msg);

				session.setType(Const.MSG_TYPE_TEXT);
				session.setContent(msgcontent);
				if (sessionDao.isContent(from, to)) {// 判断最近联系人列表是否已存在记录
					sessionDao.updateSession(session);
				} else {
					sessionDao.insertSession(session);
				}
			} else if (msgtype.equals(Const.MSG_TYPE_IMG)) {// 图片
				String downUrl = UploadUtil.downLoadUrl(
						QApp.xmppConnection.getHost(), from, msgcontent);
				saveNoticeYesMsg(to, from, msgtype, downUrl, msgtime, session,
						"[图片]");
			} else if (msgtype.equals(Const.MSG_TYPE_LOCATION)) {// 位置
				Msg msg = new Msg();
				msg.setToUser(to);
				msg.setFromUser(from);
				msg.setIsComing(0);
				msg.setContent(msgcontent);
				msg.setDate(msgtime);
				msg.setIsReaded("1");
				msg.setType(msgtype);
				msgDao.insert(msg);
				sendNewMsg(msg);

				session.setType(Const.MSG_TYPE_TEXT);
				session.setContent("[位置]");
				if (sessionDao.isContent(from, to)) {
					sessionDao.updateSession(session);
				} else {
					sessionDao.insertSession(session);
				}
			} else if (msgtype.equals(Const.MSG_TYPE_VOICE)) {// 语音
				String downUrl = UploadUtil.downLoadUrl(
						QApp.xmppConnection.getHost(), from, msgcontent);
				String voicePath = FileUtil.getRecentChatPath() + msgcontent;
				Logger.i(TAG, voicePath);
				HttpUtils http = new HttpUtils();
				HttpHandler handler = http.download(downUrl, voicePath, false,
						false, new RequestCallBack<File>() {

							@Override
							public void onSuccess(ResponseInfo<File> arg0) {
								voiceUrl = arg0.result.getAbsolutePath();
								Logger.e(TAG, "下载成功:" + voiceUrl);
							}

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								Logger.e(TAG, "下载失败:" + arg1);

							}
						});
				saveNoticeYesMsg(to, from, msgtype, voiceUrl, msgtime, session,
						"[语音]");
			}

			Intent intent = new Intent(Const.ACTION_ADDFRIEND);// 发送广播，通知消息界面更新
			context.sendBroadcast(intent);

			showNotice(session.getFrom() + ":" + session.getContent());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

/*	private void saveNoticeYesMsg(final String to, final String from,
			final String msgtype, String msgcontent, final String msgtime,
			final Session session, final String sessionContent) {
		String downUrl = UploadUtil.downLoadUrl(QApp.xmppConnection.getHost(),
				from, msgcontent);
		final Msg msg = new Msg();
		if (msgtype.equals(Const.MSG_TYPE_VOICE)) {
			String voicePath = FileUtil.getRecentChatPath() + msgcontent;
			HttpUtils http = new HttpUtils();
			HttpHandler handler = http.download(downUrl, voicePath, false,
					false, new RequestCallBack<File>() {

						@Override
						public void onSuccess(ResponseInfo<File> arg0) {
							voiceUrl = arg0.result.getAbsolutePath();
							Logger.i(TAG, arg0.result.getAbsolutePath());
							msg.setToUser(to);
							msg.setFromUser(from);
							msg.setIsComing(0);
							msg.setContent(arg0.result.getAbsolutePath());
							msg.setDate(msgtime);
							msg.setIsReaded("0");
							msg.setType(msgtype);
							msgDao.insert(msg);
							sendNewMsg(msg);

							session.setType(Const.MSG_TYPE_TEXT);
							session.setContent(sessionContent);
							if (sessionDao.isContent(from, to)) {
								sessionDao.updateSession(session);
							} else {
								sessionDao.insertSession(session);
							}
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Logger.e(TAG, "下载失败:" + arg1);

						}
					});
		} else {
			msg.setToUser(to);
			msg.setFromUser(from);
			msg.setIsComing(0);
			Logger.i(TAG, downUrl);
			msg.setContent(downUrl);
			msg.setDate(msgtime);
			msg.setIsReaded("0");
			msg.setType(msgtype);
			msgDao.insert(msg);
			sendNewMsg(msg);

			session.setType(Const.MSG_TYPE_TEXT);
			session.setContent(sessionContent);
			if (sessionDao.isContent(from, to)) {
				sessionDao.updateSession(session);
			} else {
				sessionDao.insertSession(session);
			}
		}
		msg.setToUser(to);
		msg.setFromUser(from);
		msg.setIsComing(0);

		Logger.i(TAG, downUrl);
		msg.setContent(downUrl);
		msg.setDate(msgtime);
		msg.setIsReaded("1");
		msg.setType(msgtype);
		msgDao.insert(msg);
		sendNewMsg(msg);

		session.setType(Const.MSG_TYPE_TEXT);
		session.setContent(sessionContent);
		if (sessionDao.isContent(from, to)) {
			sessionDao.updateSession(session);
		} else {
			sessionDao.insertSession(session);
		}
	}*/

	private void msgProcess(Message message, final String isRead) {
		try {

			String msgBody = message.getBody();
			final Session session;
			Logger.i(TAG, message.toXML());
			final String to = StringUtils.parseName(message.getTo());// 接收者,当然是自己
			final String from = StringUtils.parseName(message.getFrom());// 发送者，谁给你发的消息
			final String msgtime = DateUtil.date2Str(new Date(), "MM-dd HH:mm");// 消息时间

		    // 文件信息处理
				String xml = message.toXML();
				if (!xml.isEmpty() && xml != null) {

					if (xml.contains("<img>") && xml.contains("</img>")) {// 图片文件处理
						String imgName = xml.replace("</img>", "<img>").split(
								"<img>", 3)[1];
						if (!imgName.isEmpty() && imgName != null) {
							String msgtype = Const.MSG_TYPE_IMG;// 消息类型
							String fileName = imgName;// 消息内容

							session = setSession(to, from, msgtime);

							String downUrl = UploadUtil.downLoadUrl(
									QApp.xmppConnection.getHost(), from,
									fileName);
							saveNoticeNoMsg(to, from, msgtype, downUrl,
									msgtime, isRead, session, "[图片]");
							showNotice(session.getFrom() + ":"
									+ session.getContent());
						}

					} else if (xml.contains("<voice>")
							&& xml.contains("</voice>")) {// 声音文件处理
						String voiceName = xml.replace("</voice>", "<voice>")
								.split("<voice>", 3)[1];
						if (!voiceName.isEmpty() && voiceName != null) {
							final String msgtype = Const.MSG_TYPE_VOICE;// 消息类型
							String fileName = voiceName;// 消息内容

							session = setSession(to, from, msgtime);

							String downUrl = UploadUtil.downLoadUrl(
									QApp.xmppConnection.getHost(), from,
									fileName);
							String voicePath = FileUtil.getRecentChatPath()
									+ fileName;
							Logger.i(TAG, voicePath);
							HttpUtils http = new HttpUtils();
							HttpHandler handler = http.download(downUrl,
									voicePath, false, false,
									new RequestCallBack<File>() {

										@Override
										public void onSuccess(
												ResponseInfo<File> arg0) {
											fileUrl = arg0.result
													.getAbsolutePath();
											if(fileUrl.isEmpty()||fileUrl ==null)
												return;
											saveNoticeNoMsg(to, from, msgtype, fileUrl,
													msgtime, isRead, session, "[语音]");
											showNotice(session.getFrom() + ":"
													+ session.getContent());
											Logger.e(TAG, "下载成功:" + fileUrl);
										}

										@Override
										public void onFailure(
												HttpException arg0, String arg1) {
											Logger.e(TAG, "下载失败:" + arg1);

										}
									});
						
						}

					} else if (xml.contains("<video>")
							&& xml.contains("</video>")) {// 视频文件处理
						String videoName = xml.replace("</video>", "<video>")
								.split("<video>", 3)[1];
						if (!videoName.isEmpty() && videoName != null) {
							final String msgtype = Const.MSG_TYPE_VIDEO;// 视频类型
							String fileName = videoName;// 消息内容

							session = setSession(to, from, msgtime);

							String downUrl = UploadUtil.downLoadUrl(
									QApp.xmppConnection.getHost(), from,
									fileName);
							String videoPath = FileUtil.getRecentChatPath()
									+ fileName;
							Logger.i(TAG, videoPath);
							HttpUtils http = new HttpUtils();
							HttpHandler handler = http.download(downUrl,
									videoPath, false, false,
									new RequestCallBack<File>() {

										@Override
										public void onSuccess(
												ResponseInfo<File> arg0) {
											fileUrl = arg0.result
													.getAbsolutePath();
											if(fileUrl.isEmpty()||fileUrl ==null)
												return;
											saveNoticeNoMsg(to, from, msgtype, fileUrl,
													msgtime, isRead, session, "[视频]");
											showNotice(session.getFrom() + ":"
													+ session.getContent());
											Logger.e(TAG, "下载成功:" + fileUrl);
										}

										@Override
										public void onFailure(
												HttpException arg0, String arg1) {
											Logger.e(TAG, "下载失败:" + arg1);

										}
									});

					}

				} else if (xml.contains("<file>") && xml.contains("</file>")) { // 文件处理

				}else{
					if(msgBody == "")
						return;
					String msgtype = Const.MSG_TYPE_TEXT;// 消息类型
					String msgcontent = msgBody;// 消息内容

					session = setSession(to, from, msgtime);
	                Logger.e(TAG, msgBody);
					saveNoticeText(to, from, msgtype, msgcontent, msgtime, isRead,
							session);
					showNotice(session.getFrom() + ":" + session.getContent());
				}

			}
			Intent intent = new Intent(Const.ACTION_ADDFRIEND);// 发送广播，通知消息界面更新
			context.sendBroadcast(intent);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Session setSession(String to, String from, String msgtime) {
		Session session = new Session();
		session.setFrom(from);
		session.setTo(to);
		session.setNotReadCount("");// 未读消息数量
		session.setTime(msgtime);
		return session;
	}

	private void saveNoticeText(final String to, final String from,
			final String msgtype, String msgcontent, final String msgtime,
			String isRead, final Session session) {
		Msg msg = new Msg();
		msg.setToUser(to);
		msg.setFromUser(from);
		msg.setIsComing(0);
		msg.setContent(msgcontent);
		msg.setDate(msgtime);
		msg.setIsReaded(isRead);
		msg.setType(msgtype);
		msgDao.insert(msg);
		sendNewMsg(msg);

		session.setType(Const.MSG_TYPE_TEXT);
		session.setContent(msgcontent);
		if (sessionDao.isContain(from, to)) {// 判断最近联系人列表是否已存在记录
			sessionDao.updateSession(session);
		} else {
			sessionDao.insertSession(session);
		}
	}

	private void saveNoticeNoMsg(String to, String from, String msgtype,
			String msgcontent, String msgtime, String isRead, Session session,
			String sessionContent) {

		Msg msg = new Msg();
		msg.setToUser(to);
		msg.setFromUser(from);
		msg.setIsComing(0);
		Logger.i(TAG, msgcontent);
		msg.setContent(msgcontent);
		msg.setDate(msgtime);
		msg.setIsReaded(isRead);
		msg.setType(msgtype);
		msgDao.insert(msg);
		sendNewMsg(msg);

		session.setType(Const.MSG_TYPE_TEXT);
		session.setContent(sessionContent);
		if (sessionDao.isContain(from, to)) {
			sessionDao.updateSession(session);
		} else {
			sessionDao.insertSession(session);
		}
	}

	void sendNewMsg(Msg msg) {
		Intent intent = new Intent(Const.ACTION_NEW_MSG);// 发送广播到聊天界面
		Bundle b = new Bundle();
		b.putSerializable("msg", msg);
		intent.putExtra("msg", b);
		context.sendBroadcast(intent);
	}

	@SuppressWarnings("deprecation")
	public void showNotice(String content) {
		// 更新通知栏
		CharSequence tickerText = content;
		mNotification = new Notification(R.drawable.ic_notice, tickerText,
				System.currentTimeMillis());
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		if (PreferencesUtils.getSharePreBoolean( Const.MSG_IS_VOICE)) {
			// 设置默认声音
			mNotification.defaults |= Notification.DEFAULT_SOUND;
		}
		if (PreferencesUtils.getSharePreBoolean(Const.MSG_IS_VIBRATE)) {
			// 设定震动(需加VIBRATE权限)
			mNotification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		// LED灯
		mNotification.defaults |= Notification.DEFAULT_LIGHTS;
		mNotification.ledARGB = 0xff00ff00;
		mNotification.ledOnMS = 500;
		mNotification.ledOffMS = 1000;
		mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
		mNotification.setLatestEventInfo(context, "新消息", tickerText, null);
		mNotificationManager.notify(Const.NOTIFY_ID, mNotification);// 通知
	}

}