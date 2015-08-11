package com.weidi.chat;

import android.content.Context;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.util.Const;
import com.weidi.util.ToastUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-19 上午11:29:52
 * @Description 1.0
 */
public class ErrorHandle {
	private static Context mContext = QApp.getInstance();

	public static void mucChatError(String errorCode) {
		if (errorCode.equals(MucChatRepo.Error_DB)) {
			ToastUtil.showShortLuo(MucChatRepo.Error_DB_S);
		}
		if (errorCode.equals(MucChatRepo.Error_Muc_Not_Exist)) {
			ToastUtil.showShortLuo(MucChatRepo.Error_Muc_Not_Exist_S);
		}
		if (errorCode.equals(MucChatRepo.Error_Not_FoundUser)) {
			ToastUtil.showShortLuo(MucChatRepo.Error_Not_FoundUser_S);
		}
		if (errorCode.equals(MucChatRepo.Error_Not_Power)) {
			ToastUtil.showShortLuo(MucChatRepo.Error_Not_Power_S);
		}
		if (errorCode.equals(MucChatRepo.Error_Param)) {
			ToastUtil.showShortLuo(MucChatRepo.Error_Param_S);
		}
		if (errorCode.equals(MucChatRepo.Error_Room_Existed)) {
			ToastUtil.showShortLuo(MucChatRepo.Error_Room_Existed_S);
		}

	}

	public static void errorCodeHint(String errorCode) {

		if (errorCode.equals(Const.CODE_1)) {
			ToastUtil.showShortLuo(mContext.getString(R.string.code1));
		}
		if (errorCode.equals(Const.CODE_2)) {
			ToastUtil.showShortLuo(mContext.getString(R.string.code2));
		}
		if (errorCode.equals(Const.CODE_3)) {
			ToastUtil.showShortLuo(mContext.getString(R.string.code3));
		}
		if (errorCode.equals(Const.CODE_4)) {
			ToastUtil.showShortLuo(mContext.getString(R.string.code4));
		}
		if (errorCode.equals(Const.CODE_5)) {
			ToastUtil.showShortLuo(mContext.getString(R.string.code5));
		}
		if (errorCode.equals(Const.CODE_6)) {
			ToastUtil.showShortLuo(mContext.getString(R.string.code6));
		}
		if (errorCode.equals(Const.CODE_7)) {
			ToastUtil.showShortLuo(mContext.getString(R.string.code7));
		}
		if (errorCode.equals(Const.CODE_8)) {
			ToastUtil.showShortLuo(mContext.getString(R.string.code8));
		}
		if (errorCode.equals(Const.CODE_9)) {
			ToastUtil.showShortLuo(mContext.getString(R.string.code9));
		}
		if (errorCode.equals(Const.CODE_10)) {
			ToastUtil.showShortLuo(mContext.getString(R.string.code10));
		}
		if (Integer.parseInt(errorCode) > 10000) {
			ToastUtil.showShortLuo("操作次数过多，请一个小时后在试。");
		}

	}
}
