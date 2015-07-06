package com.weidi.common.function.recoding;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.weidi.R;
import com.weidi.activity.ChatActivity;
import com.weidi.bean.Msg;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-12 下午5:53:09
 * @Description 1.0
 */
public class VoicePlayClickListener implements View.OnClickListener {

	Msg message;
	String voiceBody;
	ImageView voiceIconView;

	private AnimationDrawable voiceAnimation = null;
	MediaPlayer mediaPlayer = null;
	ImageView iv_read_status;
	Activity activity;
	private String chatType;
	private BaseAdapter adapter;

	public static boolean isPlaying = false;
	public static VoicePlayClickListener currentPlayListener = null;

	/**
	 * 
	 * @param message
	 * @param v
	 * @param iv_read_status
	 * @param context
	 * @param activity
	 * @param user
	 * @param chatType
	 */
	public VoicePlayClickListener(Msg message, ImageView v,
			ImageView iv_read_status, BaseAdapter adapter, Activity activity,
			String username) {
		this.message = message;
		voiceBody = message.getContent();
		this.iv_read_status = iv_read_status;
		this.adapter = adapter;
		voiceIconView = v;
		this.activity = activity;
		this.chatType = message.getType();
	}

	public void stopPlayVoice() {
		voiceAnimation.stop();
		if (message.getIsComing() == 0) {
			voiceIconView.setImageResource(R.drawable.chatfrom_voice_playing);
		} else {
			voiceIconView.setImageResource(R.drawable.chatto_voice_playing);
		}
		// stop play voice
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		isPlaying = false;

		adapter.notifyDataSetChanged();
	}

	public void playVoice(String filePath) {
		if (!(new File(filePath).exists())) {
			return;
		}

		AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
		mediaPlayer = new MediaPlayer();
		if (((ChatActivity)activity).tvSpeek.recordState ==0) {
			audioManager.setMode(AudioManager.MODE_NORMAL);
			audioManager.setSpeakerphoneOn(true);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
		} else {
			audioManager.setSpeakerphoneOn(false);// 关闭扬声器
			// 把声音设定成Earpiece（听筒）出来，设定为正在通话中
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
		}
		try {
			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepare();
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mediaPlayer.release();
					mediaPlayer = null;
					stopPlayVoice(); // stop animation
				}
			});
		
			isPlaying = true;
			currentPlayListener = this;
			mediaPlayer.start();
			showAnimation();
		}catch(Exception e){
			
		}
			// 如果是接收的消息
			
	}

	// show the voice playing animation
	private void showAnimation() {
		// play voice, and start animation
		if (message.getIsComing() == 0) {
			voiceIconView.setImageResource(R.anim.voice_from_icon);
		} else {
			voiceIconView.setImageResource(R.anim.voice_to_icon);
		}
		voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
		voiceAnimation.start();
	}



	@Override
	public void onClick(View v) {
		if (isPlaying) {
			
			currentPlayListener.stopPlayVoice();
		}

		if (message.getIsComing() == 1) {
			// for sent msg, we will try to play the voice file directly
			playVoice(voiceBody);
		} else {
				File file = new File(voiceBody);
				if (file.exists() && file.isFile())
					playVoice(voiceBody);
				else
					System.err.println("file not exist");
			} 
	}
}