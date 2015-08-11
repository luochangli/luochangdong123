package com.weidi.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.activity.FriendInfoActi;
import com.weidi.common.base.BaseActivity;
import com.weidi.fragment.NewConstactFragment;
import com.weidi.provider.SaveRemarkIQ;
import com.weidi.util.Const;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-11 下午4:24:52
 * @Description 1.0
 */
public class RemarkActi extends BaseActivity implements OnClickListener {

	private ImageView topLeft;
	private TextView topTitle, topRight;
	private String to;
	private EditText etRemark;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_remark);
		to = getIntent().getStringExtra(Const.YOU);
		initView();

	}

	private void initView() {
		topLeft = (ImageView) findViewById(R.id.topLeft);
		topTitle = (TextView) findViewById(R.id.topTitle);
		topRight = (TextView) findViewById(R.id.topRight);
		topRight.setText("完成");
		topTitle.setText("备注信息");
		etRemark = (EditText) findViewById(R.id.etRemark);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		topLeft.setOnClickListener(this);
		topRight.setOnClickListener(this);

	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topLeft:
			finish();
			break;
		case R.id.topRight:
			String nickname = etRemark.getText().toString();
			if (nickname.isEmpty() || nickname == null) {
				ToastUtil.showMine(mContext, "备注信息不能为空！");
				return;
			}
			SaveRemarkIQ iq = IQOrder.getInstance().saveRemark(to, nickname);
			if (iq == null) {
				ToastUtil.showMine(mContext, "修改备注失败！");
				return;
			}
			PreferencesUtils.putSharePre(to, nickname);
			mLocalBroadcastManager.sendBroadcast(new Intent(
					NewConstactFragment.REFRESH_CONSTACT));
			mLocalBroadcastManager.sendBroadcast(new Intent(
					FriendInfoActi.REFRESH_TO));
			finish();

			break;

		default:
			break;
		}

	}

}
