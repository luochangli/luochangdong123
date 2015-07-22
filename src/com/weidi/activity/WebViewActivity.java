package com.weidi.activity;

import com.weidi.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity  extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		WebView web=(WebView)findViewById(R.id.webView1);
		Intent intent = this.getIntent();
		Bundle bundle=intent.getExtras();
		String pathurl=(String)bundle.get("httpurl");
		web.reload();
		web.setWebViewClient(new WebViewClient());
		web.loadUrl(pathurl);
	}
}