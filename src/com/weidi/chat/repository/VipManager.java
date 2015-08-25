package com.weidi.chat.repository;

import com.weidi.QApp;
import com.weidi.R;

import android.content.Context;
import android.widget.TextView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-24 上午10:25:46
 * @Description 1.0
 */
public class VipManager {

	private static VipManager instance;
	private Context mContext;

	public static VipManager getInstance() {
		if (instance == null) {
			instance = new VipManager();
		}
		return instance;
	}

	public VipManager() {
		mContext = QApp.getInstance();
	}

	/**
	 * 靓号处理
	 * @param to
	 * @param view
	 */
	public void isVipWeidi(String to, TextView view) {
		if (to == null || view == null) {
			return;
		}

		if (to.length() > 6) {
			view.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.vip_dear));
            view.setTextColor(mContext.getResources().getColor(R.color.white));
		}else{
			view.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.vip_bg));
            view.setTextColor(mContext.getResources().getColor(R.color.yellow));
		}

	}
}
