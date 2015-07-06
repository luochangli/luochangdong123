package com.weidi.activity;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.service.MsfService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity  extends Activity{

	private Button bt_exit;
	RelativeLayout login_out;
	TextView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		intiView();
		QApp.getInstance().addActivity(SettingActivity.this);
	}
	private void intiView(){
		back=(TextView)findViewById(R.id.tvLeft);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		bt_exit=(Button)findViewById(R.id.bt_exit);
		bt_exit.setOnClickListener(new exitListener());
		
	}
	
	class exitListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.bt_exit:
				exit();
				break;

			default:
				break;
			}
		
		}
		
	}
	public void exit(){
		final AlertDialog dlg = new AlertDialog.Builder(SettingActivity.this).create();
        dlg.show();
        Window window = dlg.getWindow();      
        window.setContentView(R.layout.activity_exit);  
        TextView exit=(TextView)window.findViewById(R.id.tv_exit);
        TextView tv_allexit=(TextView)window.findViewById(R.id.tv_allexit);
        exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				startActivity(new Intent(SettingActivity.this,LoginActivity.class));
				MsfService.getInstance().stopSelf();
				finish();
			}
		});
        
         tv_allexit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				/*QApp.getInstance().activity_exit();
			    MsfService.getInstance().stopSelf();*/	
				startActivity(new Intent(SettingActivity.this,LoginActivity.class));
				MsfService.getInstance().stopSelf();
				finish();
			}
		});
	}
	
}
