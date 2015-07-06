package com.weidi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.weidi.R;

public class NickActivity extends Activity {

	ImageView iv_back;
	TextView tv_save;
	EditText et_nick;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_nick);
		init();
	}

	private void init() {
		iv_back=(ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		
		tv_save=(TextView)findViewById(R.id.tv_save);
		tv_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String nickname=et_nick.getText().toString();
				Log.i("TAG", nickname+"回到这里来吗");
				Intent intent=new Intent("updatename");
				Bundle bundle=new Bundle();
				bundle.putString("nickname", nickname);
				intent.putExtras(bundle);
				sendBroadcast(intent);
				Log.i("TAG", "广播发出去了");
				finish();

			}
		});
		//Toast.makeText(this, "已经修改成功了", 2000).show();
		
		et_nick=(EditText)findViewById(R.id.et_nick);
		
	}
}
