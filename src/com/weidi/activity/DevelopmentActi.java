package com.weidi.activity;

import android.os.Bundle;
import android.os.Message;
import android.widget.RelativeLayout;

import com.weidi.R;
import com.weidi.common.base.BaseActivity;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-7-7 下午4:00:14
 *@Description 1.0
 */
public class DevelopmentActi extends BaseActivity{
	private RelativeLayout mImageView;
	@Override
	protected void handleMsg(Message msg) {
	
		
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_welcome);
		
		mImageView = (RelativeLayout) findViewById(R.id.iv_welcome);
		mImageView.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.development));
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}

}