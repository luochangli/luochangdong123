package com.weidi.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.common.base.BaseActivity;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-7-1 下午3:47:32
 *@Description 1.0
 */
public class ChangePwdActivity extends BaseActivity{

	EditText oldPwdView,pwdView,pwdView1;
	 Button subBtn;
	 
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_change_pwd);
	    oldPwdView = (EditText) findViewById(R.id.oldPwdView);
	    pwdView = (EditText) findViewById(R.id.pwdView);
	    pwdView1 = (EditText) findViewById(R.id.pwdView1);
	    subBtn = (Button) findViewById(R.id.subBtn);
	    
	}

	@Override
	protected void setListener() {
	   subBtn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (oldPwdView.getText().toString().equals("")) {
				ToastUtil.showShortToast(getApplicationContext(), "请输入旧密码");
			}
			else if(pwdView.getText().toString().equals("")){

				ToastUtil.showShortToast(getApplicationContext(), "请输入新密码");
			}
			else if(pwdView1.getText().toString().equals("")){
				ToastUtil.showShortToast(getApplicationContext(), "请确认新密码");
			}
			else if(!pwdView.getText().toString().equals(pwdView1.getText().toString())){
				ToastUtil.showShortToast(getApplicationContext(), "两次密码不一致");
			}
			else if (!oldPwdView.getText().toString().equals(QApp.sharedPreferences.getString("pwd", null))) {
				ToastUtil.showShortToast(getApplicationContext(),  "原密码错误");
			}
			else
			{
				new XmppLoadThread(ChangePwdActivity.this) {
					
					@Override
					protected void result(Object object) {
						if ((Boolean) object) {
							ToastUtil.showShortToast(getApplicationContext(), "修改成功");
							PreferencesUtils.editXmlByString("pwd", pwdView.getEditableText().toString());
							finish();
						}
						else {
							ToastUtil.showShortToast(getApplicationContext(), "修改失败");
						}
					}
					
					@Override
					protected Object load() {
						return XmppUtil.getInstance().changPwd(pwdView.getEditableText().toString());
					}
				};
			}
		}
	});
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub
		
	}
	

}
