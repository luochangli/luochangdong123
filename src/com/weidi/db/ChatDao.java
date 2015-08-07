package com.weidi.db;

import java.util.ArrayList;

import org.jivesoftware.smack.Chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weidi.QApp;
import com.weidi.bean.ChatItem;
import com.weidi.bean.Msg;
import com.weidi.util.Const;
import com.weidi.util.Logger;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-3 下午1:28:14
 * @Description 1.0
 */
public class ChatDao {

	private DBHelper helper;
	private static ChatDao instance;

	public static ChatDao getInstance() {
		if (instance == null) {
			instance = new ChatDao(QApp.getInstance());
		}
		return instance;
	}

	public ChatDao(Context context) {
		helper = new DBHelper(context);
	}

	public ChatDao(Context context, int version) {
		helper = new DBHelper(context, version);
	}

	/**
	 * 添加新信息
	 * 
	 * @param ChatItem
	 */
	public long insert(ChatItem item) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ChatItem.IS_GROUP, item.getIsGroup());
		values.put(ChatItem.USER_NICK, item.getUsernick());
		values.put(ChatItem.ME, item.getMe());
		values.put(ChatItem.TO, item.getTo());
		values.put(ChatItem.MUC, item.getMucFrom());
		values.put(ChatItem.CHAT_TYPE, item.getChatType());
		values.put(ChatItem.CONTENT, item.getContent());
		values.put(ChatItem.IS_RECV, item.getIsRecv());
		values.put(ChatItem.IS_READ, item.getIsRead());
		values.put(ChatItem.DATE, item.getDate());
		values.put(ChatItem.FILE_STATUS, item.getFileStatus());
		values.put(ChatItem.VOICE_READED, item.getVoiceReaded());
		values.put(ChatItem.VIEW_TYPE, item.getViewTyep());
		values.put(ChatItem.BAK1, item.getBak1());
		values.put(ChatItem.BAK2, item.getBak2());
		values.put(ChatItem.BAK3, item.getBak3());
		values.put(ChatItem.BAK4, item.getBak4());
		values.put(ChatItem.BAK5, item.getBak5());
		values.put(ChatItem.BAK6, item.getBak6());
		values.put(ChatItem.BAK7, item.getBak7());

		long row = db.insert(ChatItem.TABLE_NAME, null, values);

		db.close();

		return row;
	}

	/**
	 * 清空所有聊天记录
	 */
	public void deleteTableData() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(ChatItem.TABLE_NAME, null, null);
		db.close();
	}

	/**
	 * 根据from/to，删除对应聊天记录
	 * 
	 * @return
	 */
	public long deleteMsgById(String from, String to) {
		SQLiteDatabase db = helper.getWritableDatabase();
		long row = db.delete(ChatItem.TABLE_NAME, ChatItem.ME + " = ? AND "
				+ ChatItem.TO + " =? ", new String[] { from, to });
		db.close();
		return row;
	}

	/**
	 * 查询列表,每页返回15条,依据id逆序查询，将时间最早的记录添加进list的最前面
	 * 
	 * @return
	 */
	public ArrayList<ChatItem> queryMsg(String from, String to, int offset) {
		ArrayList<ChatItem> list = new ArrayList<ChatItem>();
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "select * from " + ChatItem.TABLE_NAME + " where "
				+ ChatItem.ME + "=? and " + ChatItem.TO + "=? order by "
				+ ChatItem.ID + " desc limit ?,?";
		String[] args = new String[] { from, to, String.valueOf(offset), "15" };
		Cursor cursor = db.rawQuery(sql, args);
		ChatItem item = null;
		while (cursor.moveToNext()) {
			item = new ChatItem();
			item.set_id(cursor.getInt(cursor.getColumnIndex(item.ID)));
			item.setIsGroup(cursor.getInt(cursor
					.getColumnIndex(ChatItem.IS_GROUP)));
			item.setUsernick(cursor.getString(cursor
					.getColumnIndex(ChatItem.USER_NICK)));
			item.setMe(cursor.getString(cursor.getColumnIndex(item.ME)));
			item.setTo(cursor.getString(cursor.getColumnIndex(ChatItem.TO)));
			item.setMucFrom(cursor.getString(cursor
					.getColumnIndex(ChatItem.MUC)));
			item.setChatType(cursor.getString(cursor
					.getColumnIndex(ChatItem.CHAT_TYPE)));
			item.setContent(cursor.getString(cursor
					.getColumnIndex(ChatItem.CONTENT)));
			item.setIsRecv(cursor.getInt(cursor
					.getColumnIndex(ChatItem.IS_RECV)));
			item.setDate(cursor.getString(cursor.getColumnIndex(ChatItem.DATE)));
			item.setFileStatus(cursor.getInt(cursor
					.getColumnIndex(ChatItem.FILE_STATUS)));
			item.setVoiceReaded(cursor.getInt(cursor
					.getColumnIndex(ChatItem.VOICE_READED)));
			item.setViewTyep(cursor.getInt(cursor
					.getColumnIndex(ChatItem.VIEW_TYPE)));
			item.setIsRead(cursor.getInt(cursor
					.getColumnIndex(ChatItem.IS_READ)));
			item.setBak1(cursor.getString(cursor.getColumnIndex(ChatItem.BAK1)));
			item.setBak2(cursor.getString(cursor.getColumnIndex(ChatItem.BAK2)));
			item.setBak3(cursor.getString(cursor.getColumnIndex(ChatItem.BAK3)));
			item.setBak4(cursor.getString(cursor.getColumnIndex(ChatItem.BAK4)));
			item.setBak5(cursor.getString(cursor.getColumnIndex(ChatItem.BAK5)));
			item.setBak6(cursor.getString(cursor.getColumnIndex(ChatItem.BAK6)));
			item.setBak7(cursor.getString(cursor.getColumnIndex(ChatItem.BAK7)));

			list.add(0, item);
		}
		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 查询所有信息未读数量
	 */
	public int queryAllNotReadCount() {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = 0;
		Cursor cursor = db.rawQuery("select count(*) from "
				+ ChatItem.TABLE_NAME + " where " + ChatItem.IS_READ
				+ " = 0  and " + ChatItem.ME + " = " + Const.USER_NAME, null);
		cursor.moveToFirst();
		count = cursor.getInt(0);
		cursor.close();
		return count;
	}

	/**
	 * 更新所有信息为已读
	 * 
	 * @param member
	 */
	public long updateAllMsgToRead(String to) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ChatItem.IS_READ, ChatItem.STATUS_1);
		long row = db.update(ChatItem.TABLE_NAME, values, ChatItem.ME
				+ " =? and " + ChatItem.TO + " =? and " + ChatItem.IS_READ
				+ "=0 ", new String[] { Const.USER_NAME, to });
		db.close();
		return row;
	}

	public int queryUnreadCount(String from) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = 0;
		Cursor countcursor = db.rawQuery("select count(*) from "
				+ ChatItem.TABLE_NAME + " where " + ChatItem.ME + " = ? and "
				+ ChatItem.IS_READ + " = 0" + " AND " + ChatItem.TO + " = ?",
				new String[] { Const.USER_NAME, from });

		if (countcursor.moveToFirst()) {
			count = countcursor.getInt(0);
		}
		countcursor.close();
		return count;
	}

	/**
	 * 更新语音为已读
	 * 
	 * @param from
	 * @return
	 */
	public long updateVoiceReaded(ChatItem item) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ChatItem.VOICE_READED, ChatItem.STATUS_1);
		long row = db.update(ChatItem.TABLE_NAME, values, ChatItem.ME
				+ " =? and " + ChatItem.TO + " =? and " + ChatItem.VOICE_READED
				+ "=0 and " + ChatItem.ID + " =? ", new String[] {
				item.getMe(), Const.USER_NAME, String.valueOf(item.get_id()) });
		db.close();
		return row;
	}

	/**
	 * 更新消息状态 是否成功接受或发送消息
	 * 
	 * @param from
	 * @return
	 */
	public long updateMsgStatus(ChatItem item) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ChatItem.FILE_STATUS, item.getFileStatus());
		long row = db.update(
				ChatItem.TABLE_NAME,
				values,
				ChatItem.ME + " =? and " + ChatItem.TO + " =? and "
						+ ChatItem.ID + " =? ",
				new String[] { item.getMe(), item.getTo(),
						String.valueOf(item.get_id()) });
		db.close();

		return row;
	}

	/**
	 * 删除和from的会话
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public long deleteAllMsg(String from) {
		SQLiteDatabase db = helper.getWritableDatabase();
		long row = db.delete(ChatItem.TABLE_NAME, ChatItem.ME + " = ?"
				+ " AND " + ChatItem.TO + " = ?", new String[] { from,
				Const.USER_NAME });
		db.close();
		return row;
	}

}
