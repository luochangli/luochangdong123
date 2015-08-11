package com.weidi.chat;

import org.jivesoftware.smack.util.StringUtils;

import com.weidi.db.VCardDao;
import com.weidi.provider.GetRemarkIQ;
import com.weidi.util.PreferencesUtils;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-11 下午3:50:47
 * @Description 1.0
 */
public class RemarkRepo {

	public static String getRemark(String to) {
		if (to.isEmpty() || to == null)
			return null;

		String remark = PreferencesUtils.getSharePreStr(to);
		if (remark.isEmpty()) {
			GetRemarkIQ iq = IQOrder.getInstance().getRemark(to);
			if (iq == null)
				return null;
			if (iq.getNickname() != null) {
				PreferencesUtils.putSharePre(to, iq.getNickname());
			}
			return iq.getNickname();
		} else {
			return remark;
		}

	}
}
