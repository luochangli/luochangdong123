package com.weidi.activity;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.weidi.R;
import com.weidi.db.NewsNotice;
import com.weidi.util.Const;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmActivity extends Activity{
	TextView notice_title,notice_content;
	String title,content;
	 Window win;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suoping);
		  win = getWindow();  
		    win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED  
		    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD  
		    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON  
		    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); 
		    
		//	notice_content=(TextView)findViewById(R.id.content);
			notice_title=(TextView)findViewById(R.id.tv_title);
		    intiView();
		    getNewData();
	}
	
	private void intiView(){
	
		
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.ll);
		//获取WallpaperManager 壁纸管理器
		WallpaperManager wallpaperManager = WallpaperManager.getInstance(this); // 获取壁纸管理器
		// 获取当前壁纸
		Drawable wallpaperDrawable = wallpaperManager.getDrawable();
		//将Drawable,转成Bitmap
		Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();

		float step = 0;
		//计算出屏幕的偏移量
		step = (bm.getWidth() - 480) / (7 - 1);
		//截取相应屏幕的Bitmap
		Bitmap pbm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth()/2, bm.getHeight());
		//设置 背景
		layout.setBackgroundDrawable(new BitmapDrawable(pbm));
		
		RelativeLayout re_mm=(RelativeLayout)findViewById(R.id.re_mm);
		re_mm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(AlarmActivity.this, "请解锁后再进入",  Toast.LENGTH_LONG).show();
				
			}
		});
		
		ImageView img=(ImageView)findViewById(R.id.img);
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlarmActivity.this.finish();
				
			}
		});
	}
	
	
	public void getNewData(){
		Log.i("TTTTT", "TTTTT");
		NewsNotice news=NewsNotice.getInstance();
		List<Map<String,Object>> news_list=news.query(1);
		Map<String,Object> map=new HashMap<String, Object>();
		map=news_list.get(0);
	//	content=(String)map.get("content");
		title=(String)map.get("title");
		/*if(content!=null){
			notice_content.setText(content);
		}*/
		if(title!=null){
			notice_title.setText(title);
		}
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);  
		if (!pm.isScreenOn()) {  
		    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |  
		            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");  
		    wl.acquire();  
		    wl.release();  
		}  
		//getNewData();
		

	}
}
