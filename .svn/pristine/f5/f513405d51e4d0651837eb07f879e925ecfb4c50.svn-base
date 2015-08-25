package com.weidi.common.media;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.weidi.common.function.video.VideoEntity;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-6 下午2:53:05
 * @Description 1.0
 */
public class GetMediaInfo {

	public static VideoEntity getVideoFile(String filePath,Context mContext) {

		String where = MediaStore.Video.Media.DATA + "=?";
		String[] selectionArgs = new String[] { filePath };
		VideoEntity entty = null;
		ContentResolver mContentResolver = mContext.getContentResolver();
		Cursor cursor = mContentResolver.query(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, where,
				selectionArgs, MediaStore.Video.DEFAULT_SORT_ORDER);

		if (cursor.moveToFirst()) {
			do {

				// ID:MediaStore.Audio.Media._ID
				int id = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media._ID));

				// 名称：MediaStore.Audio.Media.TITLE
				String title = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
				// 路径：MediaStore.Audio.Media.DATA
				String url = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

				// 总播放时长：MediaStore.Audio.Media.DURATION
				int duration = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));

				// 大小：MediaStore.Audio.Media.SIZE
				int size = (int) cursor.getLong(cursor
						.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

				entty = new VideoEntity();
				entty.ID = id;
				entty.title = title;
				entty.filePath = url;
				entty.duration = duration;
				entty.size = size;

			} while (cursor.moveToNext());

		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		return entty;

	}
}
