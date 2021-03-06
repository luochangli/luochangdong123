package com.weidi.common.function.recoding;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.widget.TextView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-12 下午6:52:04
 * @Description 1.0
 */
public class MediaManager {

	private static MediaPlayer mMediaPlayer;
	private static boolean isPause;
	private static String TAG = "MediaManager";
	public static Boolean isVoicePlay = false;

	public static int getVoiceTime(String filePath, TextView voiceTime) {
		int time = 0;

		MediaPlayer mp = new MediaPlayer();

		try {
			mp.setDataSource(filePath);
			mp.prepare();
			time = mp.getDuration() / 1000 + 1;
			voiceTime.setText(time + "\"");

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 播放音乐
	 * 
	 * @param filePath
	 * @param onCompletionListener
	 */
	public static void playSound(String filePath,
			OnCompletionListener onCompletionListener) {
		if (mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();

			// 设置一个error监听器
			mMediaPlayer.setOnErrorListener(new OnErrorListener() {

				public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
					mMediaPlayer.reset();
					return false;
				}
			});
		} else {
			mMediaPlayer.reset();
		}

		try {
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setOnCompletionListener(onCompletionListener);
			mMediaPlayer.setDataSource(filePath);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			isVoicePlay = true;
		} catch (Exception e) {

		}
	}

	/**
	 * 暂停播放
	 */
	public static void pause() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) { // 正在播放的时候
			mMediaPlayer.pause();
			isPause = true;
		}
	}

	/**
	 * 当前是isPause状态
	 */
	public static void resume() {
		if (mMediaPlayer != null && isPause) {
			mMediaPlayer.start();
			isPause = false;
		}
	}

	/**
	 * 释放资源
	 */
	public static void release() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			isVoicePlay = false;
			mMediaPlayer = null;
		}
	}
}
