package com.weidi.common.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;

import com.weidi.R;
import com.weidi.activity.SearchActivity;
import com.weidi.activity.SettingActivity;
import com.weidi.activity.SuggestForMe;
import com.weidi.activity.TradeActivity;
import com.weidi.chat.groupchat.CreatChatRoomActivity;
import com.weidi.chat.repository.ContactsActivity;
import com.weidi.common.scanner.BarcodeActivity;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-12 上午10:44:16
 * @Description 1.0
 */
public class PopMenu {

	private Activity activity;
	private PopupWindow popupWindow;

	// private OnItemClickListener listener;

	public PopMenu(Activity activity) {
		// TODO Auto-generated constructor stub
		this.activity = activity;

		View view = LayoutInflater.from(activity).inflate(
				R.layout.activity_alert, null);
		initView(view);
		popupWindow = new PopupWindow(view, 100, LayoutParams.WRAP_CONTENT);
		popupWindow = new PopupWindow(view, activity.getResources()
				.getDimensionPixelSize(R.dimen.popmenu_width),
				LayoutParams.WRAP_CONTENT);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	private void initView(View view) {
		RelativeLayout re_crechatroom = (RelativeLayout) view
				.findViewById(R.id.re_crechatroom);
		RelativeLayout re_addfriends = (RelativeLayout) view
				.findViewById(R.id.re_addfriends);
		RelativeLayout re_trade = (RelativeLayout) view
				.findViewById(R.id.re_trade);
		RelativeLayout re_nearpoeple = (RelativeLayout) view
				.findViewById(R.id.re_nearpoeple);
		RelativeLayout re2DimenCode = (RelativeLayout) view
				.findViewById(R.id.re2DimenCode);
		RelativeLayout re_setting = (RelativeLayout) view
				.findViewById(R.id.re_setting);
		RelativeLayout reSuggest = (RelativeLayout) view.findViewById(R.id.reSuggest);
		reSuggest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, SuggestForMe.class);
				activity.startActivity(intent);
				dismiss();
			}
		});
	

		re2DimenCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.startActivity(new Intent(new Intent(activity,
						BarcodeActivity.class)));
				dismiss();
			}
		});
		re_crechatroom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				activity.startActivity(new Intent(activity,
						CreatChatRoomActivity.class));
				dismiss();
			}
		});

		re_addfriends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				activity.startActivity(new Intent(activity,
						SearchActivity.class));
				dismiss();
			}
		});

		re_trade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				activity.startActivity(new Intent(activity, TradeActivity.class));
				dismiss();
			}
		});

		re_nearpoeple.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				activity.startActivity(new Intent(activity, ContactsActivity.class));
//				dismiss();
			}
		});
		re_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				activity.startActivity(new Intent(activity,
						SettingActivity.class));
				dismiss();
			}
		});
	}

	// 下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		popupWindow.showAsDropDown(parent, 0, 0);
		WindowManager.LayoutParams params = activity.getWindow()
				.getAttributes();
		params.alpha = 0.7f;
		activity.getWindow().setAttributes(params);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		// 点击其他地方消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = activity.getWindow()
						.getAttributes();
				params.alpha = 1f;
				activity.getWindow().setAttributes(params);
			}
		});
		// 刷新状态
		popupWindow.update();

	}

	// 隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();

	}
}
