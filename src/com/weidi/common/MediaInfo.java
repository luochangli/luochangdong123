package com.weidi.common;

import java.io.UnsupportedEncodingException;

import android.graphics.Bitmap;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-3 下午5:37:03
 * @Description 1.0 媒体文件信息的业务类
 */
public class MediaInfo {

	/**
	 * play total time
	 */
	private int playDuration = 0;

	/**
	 * song name
	 */
	private String mediaName = "";

	/**
	 * album name
	 */
	private String mediaAlbum = "";

	/**
	 * artist name
	 */
	private String mediaArtist = "";

	/**
	 * mYear
	 */
	private String mediaYear = "";

	/**
	 * fileName
	 */
	private String mFileName = "";

	/**
	 * mFileType
	 */
	private String mFileType = "";

	/**
	 * mFileSize
	 */
	private String mFileSize = "";

	/**
	 * mFilePath
	 */
	private String mFilePath = "";

	public Bitmap getmBitmap() {
		return mBitmap;
	}

	public void setmBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}

	private Bitmap mBitmap = null;

	/**
	 * getPlayDuration
	 * 
	 * @return
	 */
	public int getPlayDuration() {
		return playDuration;
	}

	/**
	 * setPlayDuration
	 * 
	 * @param playDuration
	 */
	public void setPlayDuration(int playDuration) {
		this.playDuration = playDuration;
	}

	/**
	 * getMediaName
	 * 
	 * @param playDuration
	 */
	public String getMediaName() {
		return mediaName;
	}

	/**
	 * setMediaName
	 * 
	 * @param playDuration
	 */
	public void setMediaName(String mediaName) {
		try {
			mediaName = new String(mediaName.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.mediaName = mediaName;
	}

	/**
	 * getMediaAlbum
	 * 
	 * @param playDuration
	 */
	public String getMediaAlbum() {
		return mediaAlbum;
	}

	/**
	 * setMediaAlbum
	 * 
	 * @param playDuration
	 */
	public void setMediaAlbum(String mediaAlbum) {
		try {
			mediaAlbum = new String(mediaAlbum.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.mediaAlbum = mediaAlbum;
	}

	/**
	 * getMediaArtist
	 * 
	 * @param playDuration
	 */
	public String getMediaArtist() {
		return mediaArtist;
	}

	/**
	 * setMediaArtist
	 * 
	 * @param playDuration
	 */
	public void setMediaArtist(String mediaArtist) {
		try {
			mediaArtist = new String(mediaArtist.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.mediaArtist = mediaArtist;
	}

	/**
	 * getMediaYear
	 * 
	 * @param playDuration
	 */
	public String getMediaYear() {
		return mediaYear;
	}

	/**
	 * setMediaYear
	 * 
	 * @param playDuration
	 */
	public void setMediaYear(String mediaYear) {
		this.mediaYear = mediaYear;
	}

	/**
	 * getmFileName
	 * 
	 * @param playDuration
	 */
	public String getmFileName() {
		return mFileName;
	}

	/**
	 * setmFileName
	 * 
	 * @param playDuration
	 */
	public void setmFileName(String mFileName) {
		this.mFileName = mFileName;
	}

	/**
	 * getmFileType
	 * 
	 * @param playDuration
	 */
	public String getmFileType() {
		return mFileType;
	}

	/**
	 * setmFileType
	 * 
	 * @param playDuration
	 */
	public void setmFileType(String mFileType) {
		this.mFileType = mFileType;
	}

	/**
	 * getmFileSize
	 * 
	 * @param playDuration
	 */
	public String getmFileSize() {
		return mFileSize;
	}

	/**
	 * setmFileSize
	 * 
	 * @param playDuration
	 */
	public void setmFileSize(String mFileSize) {
		this.mFileSize = mFileSize;
	}

	/**
	 * getmFilePath
	 * 
	 * @param playDuration
	 */
	public String getmFilePath() {
		return mFilePath;
	}

	/**
	 * setmFilePath
	 * 
	 * @param playDuration
	 */
	public void setmFilePath(String mFilePath) {
		this.mFilePath = mFilePath;
	}

}
