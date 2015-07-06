package com.weidi.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weidi.QApp;
import com.weidi.bean.Session;
import com.weidi.bean.User;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-30 下午6:22:55
 * @Description 1.0
 */
public class VCardDao {
	private SQLiteDatabase db;
	private static VCardDao instance = null;

	public static VCardDao getInstance() {
		if (instance == null) {
			instance = new VCardDao(QApp.getInstance());
		}
		return instance;
	}

	public VCardDao(Context context) {
		db = DBHelper.getInstance(context).getWritableDatabase();
	}

	public VCardDao(Context context, int version) {
		db = DBHelper.getInstance(context).getWritableDatabase();
	}

	// 判断是否包含
	public User isContain(String username) {

		Cursor cursor = db.query(DBcolumns.TABLE_VCARD, new String[] { "*" },
				DBcolumns.USER_NAME + " = ? ", new String[] { username }, null,
				null, null);
		User user = null;
		while (cursor.moveToNext()) {
			user = new User();
			String name = cursor.getString(cursor
					.getColumnIndex(DBcolumns.USER_NAME));
			String nickname = cursor.getString(cursor
					.getColumnIndex(DBcolumns.USER_NICK_NAME));
			String truename = cursor.getString(cursor
					.getColumnIndex(DBcolumns.USER_TRUE_NAME));
			String email = cursor.getString(cursor
					.getColumnIndex(DBcolumns.USER_EMAIL));
			String intro = cursor.getString(cursor
					.getColumnIndex(DBcolumns.USER_INTRO));
			String mobile = cursor.getString(cursor
					.getColumnIndex(DBcolumns.USER_MOBILE));
			String sex = cursor.getString(cursor
					.getColumnIndex(DBcolumns.USER_SEX));
			String adr = cursor.getString(cursor
					.getColumnIndex(DBcolumns.USER_ADR));
			if (adr != null)
				user.setAdr(adr);
			if (email != null)
				user.setEmail(email);
			if (intro != null)
				user.setIntro(intro);
			if (mobile != null)
				user.setMobile(mobile);
			if (nickname != null)
				user.setNickname(nickname);
			if (sex != null)
				user.setSex(sex);
			if (truename != null)
				user.setTruename(truename);
			if (username != null)
				user.setUsername(username);
		}
		cursor.close();
		return user;
	}

	// 添加一个会话
	public long insertUser(User user) {

		ContentValues values = new ContentValues();
		values.put(DBcolumns.USER_NAME, user.getUsername());
		values.put(DBcolumns.USER_ADR, user.getAdr());
		values.put(DBcolumns.USER_EMAIL, user.getEmail());
		values.put(DBcolumns.USER_INTRO, user.getIntro());
		values.put(DBcolumns.USER_MOBILE, user.getMobile());
		values.put(DBcolumns.USER_NICK_NAME, user.getNickname());
		values.put(DBcolumns.USER_SEX, user.getSex());
		values.put(DBcolumns.USER_TRUE_NAME, user.getTruename());
		long row = db.insert(DBcolumns.TABLE_VCARD, null, values);
		return row;
	}

	// 修改一个回话
	public long updateSession(User user) {
		ContentValues values = new ContentValues();
		values.put(DBcolumns.USER_NAME, user.getUsername());
		values.put(DBcolumns.USER_ADR, user.getAdr());
		values.put(DBcolumns.USER_EMAIL, user.getEmail());
		values.put(DBcolumns.USER_INTRO, user.getIntro());
		values.put(DBcolumns.USER_MOBILE, user.getMobile());
		values.put(DBcolumns.USER_NICK_NAME, user.getNickname());
		values.put(DBcolumns.USER_SEX, user.getSex());
		values.put(DBcolumns.USER_TRUE_NAME, user.getTruename());
		long row = db.update(DBcolumns.TABLE_VCARD, values, DBcolumns.USER_NAME
				+ " = ? ", new String[] { user.getUsername() });
		return row;
	}

	// 删除
	public long deleteSession(String username) {
		long row = db.delete(DBcolumns.TABLE_VCARD,
				DBcolumns.USER_NAME + "=? ", new String[] { username });
		return row;
	}

	public void deleteTableData() {
		db.delete(DBcolumns.TABLE_VCARD, null, null);
	}
}
