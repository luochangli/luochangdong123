package com.weidi.activity;

import org.jivesoftware.smack.packet.IQ.Type;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.common.base.BaseActivity;
import com.weidi.provider.SuggestIQ;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-17 下午3:00:31
 * @Description 1.0
 */
public class SuggestForMe extends BaseActivity implements OnClickListener {

	private ImageView topLeft;
	private TextView topTitle, topRight;
	private Button btnSubmit;
	private EditText etMsg;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_suggest);
		initView();

	}

	private void initView() {
		topLeft = (ImageView) findViewById(R.id.topLeft);
		topTitle = (TextView) findViewById(R.id.topTitle);
		topRight = (TextView) findViewById(R.id.topRight);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		etMsg = (EditText) findViewById(R.id.etSuggestMsg);
	}

	@Override
	protected void setListener() {
		btnSubmit.setOnClickListener(this);
		topLeft.setOnClickListener(this);
		topRight.setOnClickListener(this);
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

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
			sendMsg();
			break;
		case R.id.btnSubmit:
			sendMsg();
			break;
		default:
			break;
		}

	}

	private void sendMsg() {
		SuggestIQ iq = new SuggestIQ();
		iq.setType(Type.SET);
		iq.setSuggest(etMsg.getText().toString());
		QApp.getXmppConnection().sendPacket(iq);
	}

}
