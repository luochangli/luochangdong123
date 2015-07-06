package com.weidi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weidi.util.Const;


/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-6-29 下午2:08:51
 *@Description 1.0 新消息的条数
 */
public class NewMsgCountDao {
	private static NewMsgCountDao instance = null;
	
	private DBHelper helper;
	private SQLiteDatabase db;

	public NewMsgCountDao(Context context) {
		helper = new DBHelper(context);
	}

	public NewMsgCountDao(Context context, int version) {
		helper = new DBHelper(context, version);
	}
	
	public static NewMsgCountDao getInstance(Context context) {
		if (instance == null) {
			instance = new NewMsgCountDao(context);
		}
		return instance;
	}
	

	
	/**
	 * @param msgId  聊天的话代表userid
	 * 0代表好友添加
	 */
	public void saveNewMsg(String msgId){    //如果存在，则+1，不存在则插入
		db = helper.getWritableDatabase();
		int nowCount = getMsgCount(msgId);
		ContentValues values = new ContentValues();
		if(nowCount==0){
			values.put("msgId", msgId);
			values.put("msgCount", 1);
			values.put("whosMsg", Const.USER_NAME);
			db.insert(DBcolumns.TABLE_NEW_MSG_COUNT, null, values);   //第二，第三个参数其中一个不能为null
		}else{
			values.put("msgCount",nowCount+1 );
			db.update(DBcolumns.TABLE_NEW_MSG_COUNT, values, " msgId=? and whosMsg=?", 
					new String[]{msgId,Const.USER_NAME});
		}
	}
	
	/**
	 * @param msgId
	 */
	public void delNewMsg(String msgId){
		db.delete(DBcolumns.TABLE_NEW_MSG_COUNT, " msgId=? and whosMsg=?", new String[]{msgId,Const.USER_NAME}); 
	}

	//某个人
	public int getMsgCount(String msgId){   
		int count = 0 ;
		String sql ="select "+DBcolumns.TABLE_NEW_MSG_COUNT+" from newMsg where msgId=? and whosMsg=?";
		Cursor cursor = db.rawQuery(sql, new String[]{msgId,Const.USER_NAME});
		while(cursor.moveToNext()){
			count = cursor.getInt(0);
		}
		cursor.close();
		return count;
	}
	
	//所有
	public int getMsgCount(){   
		int count = 0 ;
		String sql ="select sum(table_new_msg_count) from table_new_msg_count where whosMsg=? and msgId != 0";
		Cursor cursor = db.rawQuery(sql, new String[]{Const.USER_NAME});
		while(cursor.moveToNext()){
			count = cursor.getInt(0);
		}
		cursor.close();
		return count;
	}

	public void clear(){
		db.delete(DBcolumns.TABLE_NEW_MSG_COUNT, "id>?", new String[]{"0"}); 
	}
}
