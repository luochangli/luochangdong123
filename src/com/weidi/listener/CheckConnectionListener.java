package com.weidi.listener;

import org.jivesoftware.smack.ConnectionListener;

import android.util.Log;

import com.weidi.service.MsfService;
import com.weidi.util.ToastUtil;


/**
 * @author baiyuliang
 */
public class CheckConnectionListener implements ConnectionListener{
	
	private MsfService context;
	
	public CheckConnectionListener(MsfService context){
		this.context=context;
	}

	@Override
	public void connectionClosed() {
		Log.i("RRRRRRRRRRRRR", "手动关闭网络");
	}

	@Override
	public void connectionClosedOnError(Exception e) {  
		if (e.getMessage().equals("stream:error (conflict)")) {
			ToastUtil.showLongToast(context, "您的账号在异地登录");
		}
	}

	@Override
	public void reconnectingIn(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconnectionFailed(Exception arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub
		
	}

}