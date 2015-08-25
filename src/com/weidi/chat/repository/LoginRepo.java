package com.weidi.chat.repository;

import android.content.Context;

import com.weidi.QApp;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-23 上午10:28:19
 * @Description 1.0
 */
public class LoginRepo {
	private static LoginRepo instance;
	private Context mContext;

	public static LoginRepo getInstance() {
		if (instance == null) {
			instance = new LoginRepo();
		}
		return instance;
	}

	public LoginRepo() {
		mContext = QApp.getInstance();
	}

	

}
