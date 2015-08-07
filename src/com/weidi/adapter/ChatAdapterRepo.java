package com.weidi.adapter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.adapter.NewChatAdapter.ViewHolderRecvPicture;
import com.weidi.adapter.NewChatAdapter.ViewHolderRecvText;
import com.weidi.adapter.NewChatAdapter.ViewHolderRecvVideo;
import com.weidi.adapter.NewChatAdapter.ViewHolderRecvVoice;
import com.weidi.adapter.NewChatAdapter.ViewHolderSentPicture;
import com.weidi.adapter.NewChatAdapter.ViewHolderSentText;
import com.weidi.adapter.NewChatAdapter.ViewHolderSentVideo;
import com.weidi.adapter.NewChatAdapter.ViewHolderSentVoice;
import com.weidi.bean.ChatItem;
import com.weidi.bean.Session;
import com.weidi.common.DateUtil;
import com.weidi.common.MediaFile;
import com.weidi.common.MediaInfo;
import com.weidi.common.MediaInfoProvider;
import com.weidi.common.UploadUtil;
import com.weidi.common.UploadUtil.UpCallback;
import com.weidi.common.function.recoding.MediaManager;
import com.weidi.common.function.video.VideoEntity;
import com.weidi.common.function.video.VideoThumbnail;
import com.weidi.common.image.ImageLoadCache;
import com.weidi.common.image.ImgConfig;
import com.weidi.common.media.GetMediaInfo;
import com.weidi.db.ChatDao;
import com.weidi.db.SessionDao;
import com.weidi.util.Const;
import com.weidi.util.ExpressionUtil;
import com.weidi.util.Logger;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppUtil;
import com.weidi.view.CircleImageView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-4 上午11:01:59
 * @Description 1.0
 */
public class ChatAdapterRepo {

	private static final String TAG = ChatAdapterRepo.class.getSimpleName();
	private static ChatAdapterRepo instance;
	Handler mHandler;
	ChatDao chatDao;
	SessionDao sessionDao;
	Context mContext;

	public static ChatAdapterRepo getInstance(Handler handler, Context context) {
		if (instance == null) {
			instance = new ChatAdapterRepo(handler, context);
		}
		return instance;
	}

	public ChatAdapterRepo(Handler handler, Context context) {
		chatDao = ChatDao.getInstance();
		sessionDao = SessionDao.getInstance();
		mHandler = handler;
		mContext = context;
	}

	public void handleChatItem(ChatItem item, Object object) {
		switch (item.getViewTyep()) {
		case NewChatAdapter.MESSAGE_TYPE_RECV_TXT:
			handlRecvText(item, object);

			break;
		case NewChatAdapter.MESSAGE_TYPE_RECV_IMAGE:
			handleRecvImg(item, object);

			break;
		case NewChatAdapter.MESSAGE_TYPE_RECV_VIDEO:
			handleRecvVideo(item, object);

			break;
		case NewChatAdapter.MESSAGE_TYPE_RECV_VOICE:
			handleRecvVoice(item, object);

			break;
		case NewChatAdapter.MESSAGE_TYPE_SENT_TXT:
			handleSentText(item, object);

			break;
		case NewChatAdapter.MESSAGE_TYPE_SENT_IMAGE:
			handleSentImg(item, object);

			break;
		case NewChatAdapter.MESSAGE_TYPE_SENT_VIDEO:
			handleSendVideo(item, object);

			break;
		case NewChatAdapter.MESSAGE_TYPE_SENT_VOICE:
			handleSentVoice(item, object);

			break;

		default:
			break;
		}
	}

	private void handleRecvVoice(ChatItem item, Object object) {
		ViewHolderRecvVoice recvVoice = (ViewHolderRecvVoice) object;
		recvVoice.timestamp.setText(item.getDate());

		showRecvHead(recvVoice.userhead, recvVoice.nickname, item);
		if (item.getVoiceReaded() == 1) {  
			recvVoice.unread.setVisibility(View.GONE);
		}else{
			recvVoice.unread.setVisibility(View.VISIBLE);
		}
		MediaManager.getVoiceTime(item.getContent(), recvVoice.length);
	}

	private void handleRecvVideo(ChatItem item, Object object) {
		ViewHolderRecvVideo recvVideo = (ViewHolderRecvVideo) object;
		recvVideo.timestamp.setText(item.getDate());
		showRecvHead(recvVideo.userhead, recvVideo.nickname, item);
		Bitmap videoImg = VideoThumbnail.getVideoThumbnail(item.getContent(), 120,
				120, MediaStore.Images.Thumbnails.MICRO_KIND);
		recvVideo.video.setImageBitmap(videoImg);
		VideoEntity entty = GetMediaInfo.getVideoFile(item.getContent(),
				mContext);
		if(entty == null)
			return;
	
		recvVideo.size.setText(VideoThumbnail.bytes2kb(entty.size));
		recvVideo.lenght.setText(DateUtil.date2Str(entty.duration, "mm:ss"));
	
	}

	private void handleRecvImg(ChatItem item, Object object) {
		ViewHolderRecvPicture recvImg = (ViewHolderRecvPicture) object;
		showRecvHead(recvImg.userhead, recvImg.nickname, item);
		recvImg.timestamp.setText(item.getDate());
		ImageLoadCache.getBinnerBitmap(recvImg.picture, item.getContent());
	}

	private void handlRecvText(ChatItem item, Object object) {
		ViewHolderRecvText recvText = (ViewHolderRecvText) object;
		showRecvHead(recvText.userhead, recvText.nickname, item);
		SpannableStringBuilder sb = ExpressionUtil.prase(QApp.getInstance(),
				recvText.content, item.getContent());// 对内容做处理
		recvText.content.setText(sb);
		Linkify.addLinks(recvText.content, Linkify.ALL);// 增加文本链接类型
		recvText.timestamp.setText(item.getDate());

		recvText.timestamp.setText(item.getDate());
	}

	private void handleSentVoice(ChatItem item, Object object) {
		ViewHolderSentVoice sentVoice = (ViewHolderSentVoice) object;
		sentVoice.timestamp.setText(item.getDate());
		MediaManager.getVoiceTime(item.getContent(), sentVoice.length);

		if (item.getFileStatus() == ChatItem.STATUS_0) {
			sentVoice.progressBar.setVisibility(View.VISIBLE);
			sendVoiceMsg(sentVoice, item);

		}
		if (item.getFileStatus() == ChatItem.STATUS_2) {
			sentVoice.status.setVisibility(View.VISIBLE);
		}
	}

	private void handleSendVideo(ChatItem item, Object object) {
		ViewHolderSentVideo sentVideo = (ViewHolderSentVideo) object;
		sentVideo.timestamp.setText(item.getDate());

		VideoEntity entty = GetMediaInfo.getVideoFile(item.getContent(),
				mContext);
		Bitmap videoImg = VideoThumbnail.getVideoThumbnail(entty.filePath, 120,
				120, MediaStore.Images.Thumbnails.MICRO_KIND);
		sentVideo.size.setText(VideoThumbnail.bytes2kb(entty.size));
		sentVideo.lenght.setText(DateUtil.date2Str(entty.duration, "mm:ss"));
		sentVideo.video.setImageBitmap(videoImg);
		if (item.getFileStatus() == ChatItem.STATUS_0) {
			sentVideo.llLoad.setVisibility(View.VISIBLE);
			sentVideoMsg(sentVideo, item);
		}
		if (item.getFileStatus() == ChatItem.STATUS_2) {
			sentVideo.status.setVisibility(View.VISIBLE);
		}
	}

	private void handleSentImg(ChatItem item, Object object) {
		ViewHolderSentPicture sentPicture = (ViewHolderSentPicture) object;
		sentPicture.timestamp.setText(item.getDate());
		ImageLoadCache.getBinnerBitmap(sentPicture.picture, Const.ExtraPath
				+ item.getContent());
		if (item.getFileStatus() == ChatItem.STATUS_0) {
			sentPicture.llLoad.setVisibility(View.VISIBLE);
			sentPicMsg(sentPicture, item);
		}
		if (item.getFileStatus() == ChatItem.STATUS_2) {
			sentPicture.status.setVisibility(View.VISIBLE);
		}
	}

	private void handleSentText(ChatItem item, Object object) {
		ViewHolderSentText sentText = (ViewHolderSentText) object;

		SpannableStringBuilder sb = ExpressionUtil.prase(QApp.getInstance(),
				sentText.content, item.getContent());// 对内容做处理
		sentText.content.setText(sb);
		Linkify.addLinks(sentText.content, Linkify.ALL);// 增加文本链接类型
		sentText.timestamp.setText(item.getDate());
		sentText.timestamp.setText(item.getDate());

		if (item.getFileStatus() == ChatItem.STATUS_0) {
			sentText.progressBar.setVisibility(View.VISIBLE);
			sendTXTMsg(item, sentText);
		}
		if (item.getFileStatus() == ChatItem.STATUS_2) {
			sentText.status.setVisibility(View.VISIBLE);
		}
	}

	private void showRecvHead(CircleImageView head, TextView nick, ChatItem msg) {
		if (msg.getIsGroup() == 1) {
			head.setVisibility(View.VISIBLE);
			ImgConfig.showHeadImg(msg.getMucFrom(), head);
			nick.setText(msg.getUsernick());

		} else {
			head.setVisibility(View.GONE);
			nick.setText("");
		}
	}

	private void sendVoiceMsg(final ViewHolderSentVoice sentVoice,
			final ChatItem item) {
		final File file = new File(item.getContent());
		String date = com.weidi.common.DateUtil.getCurDateStr("yyyyMMddHHmmss");
		final String ext = UploadUtil.getExtensionName(file.getName());
		final StringBuilder fileName = new StringBuilder();
		fileName.append(item.getMe());

		fileName.append(date);
		fileName.append(".");
		fileName.append(ext);
		final String upUrl;
		if (item.getIsGroup() == 1) {
			upUrl = upLoadUrl(item.getMe(), fileName.toString());
		} else {
			upUrl = upLoadUrl(item.getMe(), fileName.toString());
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				UploadUtil.uploadFile(file, upUrl, new UpCallback() {
					@Override
					public void upSendMsg() {
						sendMsg(fileName.toString(), item);
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								sentVoice.progressBar.setVisibility(View.GONE);
							}
						});
						item.setFileStatus(ChatItem.STATUS_1);
						long row = chatDao.updateMsgStatus(item);
						Logger.i(TAG, "数据上传成功" + row);
						updateSession(item);

					}

					@Override
					public void onUploadProcess(int current, int total) {

					}

					@Override
					public void onLoadingFailed() {
						ToastUtil.showShortLuo("文件上传失败！");
						chatDao.updateMsgStatus(item);
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								sentVoice.progressBar.setVisibility(View.GONE);
								sentVoice.status.setVisibility(View.VISIBLE);
							}
						});
					}
				});
			}
		}).start();
	}

	private void sentVideoMsg(final ViewHolderSentVideo sentVideo,
			final ChatItem item) {
		final File file = new File(item.getContent());
		String date = com.weidi.common.DateUtil.getCurDateStr("yyyyMMddHHmmss");
		final String ext = UploadUtil.getExtensionName(file.getName());
		final StringBuilder fileName = new StringBuilder();
		fileName.append(item.getMe());

		fileName.append(date);
		fileName.append(".");
		fileName.append(ext);
		final String upUrl;

		upUrl = upLoadUrl(item.getMe(), fileName.toString());

		new Thread(new Runnable() {

			@Override
			public void run() {
				UploadUtil.uploadFile(file, upUrl, new UpCallback() {

					@Override
					public void upSendMsg() {
						sendMsg(fileName.toString(), item);
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								sentVideo.llLoad.setVisibility(View.GONE);
							}
						});
						item.setFileStatus(ChatItem.STATUS_1);
						long row = chatDao.updateMsgStatus(item);
						Logger.i(TAG, "数据上传成功" + row);
						updateSession(item);

					}

					@Override
					public void onUploadProcess(int current, int total) {
						if (current != 0 && current <= total) {
							final float num = (float) current / (float) total;
							final DecimalFormat df = new DecimalFormat("0.00");// 格式化小数
							final String s = df.format(num * 100);// 返回的是String类型
							Logger.e(TAG, "进度值：" + s);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									sentVideo.percentage.setText(s);
								}
							});

						}

					}

					@Override
					public void onLoadingFailed() {
						ToastUtil.showShortLuo("文件上传失败！");
						chatDao.updateMsgStatus(item);
						sentVideo.llLoad.setVisibility(View.GONE);
						sentVideo.status.setVisibility(View.VISIBLE);
					}
				});
			}
		}).start();
	}

	private void sentPicMsg(final ViewHolderSentPicture sentPicture2,
			final ChatItem item) {
		final File file = new File(item.getContent());
		String date = com.weidi.common.DateUtil.getCurDateStr("yyyyMMddHHmmss");
		final String ext = UploadUtil.getExtensionName(file.getName());
		final StringBuilder fileName = new StringBuilder();
		fileName.append(item.getMe());

		fileName.append(date);
		fileName.append(".");
		fileName.append(ext);
		final String upUrl;

		upUrl = upLoadUrl(item.getMe(), fileName.toString());

		new Thread(new Runnable() {

			@Override
			public void run() {
				UploadUtil.uploadFile(file, upUrl, new UpCallback() {

					@Override
					public void upSendMsg() {
						sendMsg(fileName.toString(), item);
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								sentPicture2.llLoad.setVisibility(View.GONE);
							}
						});
						item.setFileStatus(ChatItem.STATUS_1);
						long row = chatDao.updateMsgStatus(item);
						Logger.e(TAG, "上传成功：" + row);
						updateSession(item);

					}

					@Override
					public void onUploadProcess(int current, int total) {
						if (current != 0 && current <= total) {
							float num = (float) current / (float) total;
							DecimalFormat df = new DecimalFormat("0.00");// 格式化小数
							final String s = df.format(num * 100);// 返回的是String类型
							Logger.e(TAG, "进度值：" + s);
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									sentPicture2.percentage.setText(s);
								}
							});

						}

					}

					@Override
					public void onLoadingFailed() {
						ToastUtil.showShortLuo("文件上传失败！");
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								sentPicture2.llLoad.setVisibility(View.GONE);
								sentPicture2.status.setVisibility(View.VISIBLE);
							}
						});

						item.setFileStatus(ChatItem.STATUS_2);
						chatDao.updateMsgStatus(item);
					}
				});
			}
		}).start();

	}

	/**
	 * 上传路径URL
	 * 
	 * @param host
	 * @param userName
	 * @param fileName
	 * @return
	 */
	public String upLoadUrl(String userName, String fileName) {
		StringBuilder sb = new StringBuilder();
		sb.append("http://");
		sb.append(QApp.getXmppConnection().getHost() + ":9090");
		sb.append("/plugins/jsmfiles/fileupload?");
		sb.append("username=");
		sb.append(userName);
		sb.append("&filename=");
		sb.append(fileName);
		Logger.i(TAG, sb.toString());
		return sb.toString();
	}

	private void sendMsg(String fileName, ChatItem item) {

		try {

			XmppUtil.sendFileMsg(fileName, item.getChatType(), item.getTo());

		} catch (XMPPException e) {
			e.printStackTrace();
			Looper.prepare();
			ToastUtil.showShortLuo("发送失败");
			Looper.loop();
		}
	}


	private void sendTXTMsg(ChatItem item, ViewHolderSentText sentText) {

		try {
			XmppUtil.sendTextMsg(item.getContent(), item.getTo());
			item.setFileStatus(ChatItem.STATUS_1);
			long row = chatDao.updateMsgStatus(item);
			Logger.i(TAG, "数据上传成功" + row);
			updateSession(item);
			sentText.progressBar.setVisibility(View.GONE);

		} catch (XMPPException e) {
			e.printStackTrace();
			item.setFileStatus(ChatItem.STATUS_2);
			chatDao.updateMsgStatus(item);
			sentText.status.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 更新回话列表
	 * 
	 * @param type
	 * @param content
	 */
	private void updateSession(ChatItem item) {
		Session session = new Session();
		session.setFrom(item.getTo());
		session.setTo(item.getMe());
		session.setNotReadCount("");// 未读消息数量
		if (item.getChatType().equals(Const.MSG_TYPE_TEXT)) {
			session.setContent(item.getContent());
		}
		if (item.getChatType().equals(Const.MSG_TYPE_VIDEO)) {
			session.setContent("[视频]");
		}
		if (item.getChatType().equals(Const.MSG_TYPE_IMG)) {
			session.setContent("[图片]");
		}
		if (item.getChatType().equals(Const.MSG_TYPE_VOICE)) {
			session.setContent("[语音]");
		}

		session.setTime(item.getDate());
		session.setType(item.getChatType());
		if (sessionDao.isContain(item.getTo(), item.getMe())) {
			sessionDao.updateSession(session);
		} else {
			sessionDao.insertSession(session);
		}
		Intent intent = new Intent(Const.ACTION_ADDFRIEND);// 发送广播，通知消息界面更新
		mContext.sendBroadcast(intent);
	}

}
