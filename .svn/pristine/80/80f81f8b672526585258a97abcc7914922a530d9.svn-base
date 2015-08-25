package com.weidi.provider.sign;

import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.chat.IQOrder;
import com.weidi.common.DateUtil;
import com.weidi.fragment.PersonalFrag;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-23 下午5:48:39
 * @Description 1.0
 */
public class SignManager {
	private static String TAG = SignManager.class.getSimpleName();
	public static String INTEGRAL = "integral";
	private Context mContext;
	private static SignManager instance;

	public static SignManager getInstance() {
		if (instance == null) {
			instance = new SignManager();
		}
		return instance;
	}

	public SignManager() {
		mContext = QApp.getInstance();
	}

	public Boolean isSinged() {
		String signed = PreferencesUtils.getSharePreStr(Const.USER_ACCOUNT
				+ "signIn");

		if (DateUtil.getCurDateStr("yyyy-MM-dd").equals(signed)) {
			return true;
		} else {
			return false;
		}
	}

	public void sendBroad(String messge) {
		String value = parserIntegration(messge);
		if (value == null) {
			ToastUtil.showShortLuo("签到失败！");
		}
		Intent intent = new Intent(PersonalFrag.UPDATE_INTEGRAL);
		intent.putExtra(INTEGRAL, value);
		QApp.mLocalBroadcastManager.sendBroadcast(intent);
	}

	public void getIntegral(TextView tvIntegral) {

		IntegrationIQ iq = IQOrder.getInstance().sendIntegrationIQ();
		if (iq == null) {
			return;
		}
		String value = iq.getIntegral();
		Logger.e(TAG, value);
		tvIntegral.setText(value);
	}

	private String parserIntegration(String messge) {
		XmlPullParser parser;
		String integral = null;
		try {
			parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new StringReader(messge));
			Boolean done = false;
			while (!done) {
				int eventType = parser.next();
				if (eventType == XmlPullParser.START_TAG) {
					if (parser.getName().equals("integral")) {
						integral = parser.nextText();
						Logger.e(TAG, integral);
					}

				} else if (eventType == XmlPullParser.END_TAG) {
					if (parser.getName().equals("message")) {
						done = true;
					}
				}
			}
			return integral;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
