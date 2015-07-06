package com.weidi.activity;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.provider.BindPhone;
import com.weidi.provider.BindPhoneIQ;
import com.weidi.util.ToastUtil;

public class BindPhoneActivity extends Activity implements OnClickListener{

	private ImageView iv_back;
	private TextView et_bindphone;
	private TextView tv_bindphone_save;
	private String bindPhone;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_update_bindphone);
		initView();
		initData();
		QApp.getInstance().addActivity(BindPhoneActivity.this);
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_bindphone = (TextView) findViewById(R.id.et_phone);
		tv_bindphone_save = (TextView) findViewById(R.id.tv_bindphone_save);
	}

	private void initData() {
		iv_back.setOnClickListener(this);
		tv_bindphone_save.setOnClickListener(this);
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_bindphone_save:
			bindPhone = et_bindphone.getText().toString();
			if (bindPhone.equals("")||bindPhone == null) {
				ToastUtil.showShortToast(this, "请输入要更改的手机号");
//				String phone = QApp.getBindPhone();
				return;
			}else {
				//发送绑定手机IQ包
				BindPhoneIQ bindPhoneIQ = new BindPhoneIQ();
				bindPhoneIQ.setType(IQ.Type.SET);
				bindPhoneIQ.setXmlns("com:jsm:bindphone");
				bindPhoneIQ.setPhone(bindPhone);
				
				//手机绑定过滤器
				PacketFilter filter = new AndFilter(new PacketIDFilter(bindPhoneIQ.getPacketID()),
						new PacketTypeFilter(BindPhone.class));
				PacketCollector collector = QApp.xmppConnection.createPacketCollector(  
						filter);
				Log.i("TAG", bindPhoneIQ.toXML());
				QApp.xmppConnection.sendPacket(bindPhoneIQ);
				BindPhone result = (BindPhone) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
				collector.cancel();
				Log.i("TAG", result.toXML());
				String errorCode = result.getErrorCode();
				Log.i("TAG", errorCode+"");
				if (errorCode == null) {//绑定成功
					Intent intent = new Intent();
					intent.putExtra("BindPhone", bindPhone);
					setResult(RESULT_OK, intent);
					finish();
				}else if (errorCode.equals("00001")) {//手机已绑定
					ToastUtil.showShortToast(this, "手机已绑定");
				}else if (errorCode.equals("00002")) {//手机号码已锁定
					ToastUtil.showShortToast(this, "手机号码已锁定");
				}
			}
			break;
		}
	}
}
