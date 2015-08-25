package com.weidi.chat.repository;

import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.workgroup.util.ModelUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff.Mode;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.bean.User;
import com.weidi.chat.IQOrder;
import com.weidi.common.CharacterParser;
import com.weidi.common.SortModel;
import com.weidi.db.VCardDao;
import com.weidi.provider.GetRemarkIQ;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.XmppUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-11 下午3:50:47
 * @Description 1.0
 */
public class RemarkRepo {
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private static RemarkRepo instance;
	private Context mContext;

	public static RemarkRepo getInstance(Context context) {
		if (instance == null) {
			instance = new RemarkRepo(context);
		}
		return instance;
	}

	public RemarkRepo(Context context) {
		mContext = context;
		characterParser = CharacterParser.getInstance();
	}

	public static String getRemark(String to) {
		if (to.isEmpty() || to == null)
			return null;

		GetRemarkIQ iq = IQOrder.getInstance().getRemark(to);
		if (iq == null)
			return null;

		return iq.getNickname();

	}

	public static void setRemark(Context context, String to, TextView tvRemark) {

//		if (to.length() == 6) {
//			tvRemark.setTextColor(context.getResources().getColor(R.color.vip));
//		}
		User friend = VCardDao.getInstance().getUser(to);
		String toNick = getRemark(to);
		if (toNick != null) {
			tvRemark.setText(toNick);
		} else if (friend != null) {
			tvRemark.setText(friend.getNickname() == null ? to : friend
					.getNickname());

		} else {
			tvRemark.setText(to);
		}
	}

	public SortModel getSortModel(String to) {
		SortModel model = new SortModel();

		User item = VCardDao.getInstance().getUser(to);

		if (item == null) {
			item = new User(XmppUtil.getUserInfo(to));
			String remark = getRemark(to);
			String nickname = item.getNickname() == null ? to : item
					.getNickname();
			if (remark != null) {
				model.setName(remark);
				item.setNickname(remark);
			} else {
				model.setName(nickname);
				item.setNickname(nickname);
			}
			if (item.getBitmap() == null) {
				Bitmap bitmap = BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.icon_male);
				item.setBitmap(bitmap);
			}

			VCardDao.getInstance().insertUser(item);
		} else {
			model.setName(item.getNickname());
		}

		model.setPhoto(item.getBitmap());
		model.setValue(item.getUsername());

		// 汉字转换成拼音
		String pinyin = characterParser.getSelling(model.getName());
		String sortString = pinyin.substring(0, 1).toUpperCase();

		// 正则表达式，判断首字母是否是英文字母
		if (sortString.matches("[A-Z]")) {
			model.setSortLetters(sortString.toUpperCase());
		} else {
			model.setSortLetters("#");
		}
		return model;
	}

	public static SortModel getNick(String to) {

		SortModel model = new SortModel();

		User item = VCardDao.getInstance().getUser(to);

		if (item == null) {
			item = new User(XmppUtil.getUserInfo(to));
			String remark = getRemark(to);
			String nickname = item.getNickname() == null ? to : item
					.getNickname();
			if (remark != null) {
				model.setName(remark);
				item.setNickname(remark);
			} else {
				model.setName(nickname);
			}
			item.setNickname(nickname);

			VCardDao.getInstance().insertUser(item);
		} else {
			model.setName(item.getNickname());
		}
		model.setPhoto(item.getBitmap());
		model.setValue(item.getUsername());

		return model;

	}
}
