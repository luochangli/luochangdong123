package com.weidi.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.packet.VCard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.drm.DrmStore.RightsStatus;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView.OnChildClickListener;

import com.thinkland.sdk.sms.SMSCaptcha;
import com.thinkland.sdk.util.BaseData.ResultCallBack;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.util.PreferencesUtils;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppConnectionManager;
import com.weidi.view.Group;
import com.weidi.view.LoadingDialog;
import com.weidi.view.People;
import com.weidi.view.TitleBarView;

public class RegisterActivity extends BaseActivity 
 {
	private Context mContext;
	private Button btn_complete;
	private TitleBarView mTitleBarView;
	private EditText et_name,et_password;
	private String host,servicename;
	private String account,password;
	private LoadingDialog loadDialog;
	private SMSCaptcha captcha;
	private XmppConnectionManager xmppConnectionManager;
	VCard vcard;
	XMPPConnection mXMPPConnection;
	Spinner  spinCity;
	TextView tv_xieyi,btnLeft;
	 private ArrayAdapter<CharSequence> adapterCity=null;  
	 
	
	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(loadDialog.isShowing()){
				loadDialog.dismiss();
			}
			switch (msg.what) {
			case 0:
				ToastUtil.showLongToast(mContext, "注册失败");
				break;
			case 1:
				ToastUtil.showLongToast(mContext, "注册成功，请牢记您的账号和密码");
				PreferencesUtils.putFirst("isfirst", true);
				PreferencesUtils.putSharePre("username", account);
				PreferencesUtils.putSharePre("pwd", password);
				finish();
				break;
			case 2:
				ToastUtil.showLongToast(mContext, "该昵称已被注册");
				break;
			case 3:
				ToastUtil.showLongToast(mContext, "注册失败");
				break;
			case 4:
				ToastUtil.showLongToast(mContext, "注册失败,请检查您的网络");
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_userinfo2);
		//启动核心Service 
		//Intent intent2=new Intent(RegisterActivity.this,MsfService.class);
		//startService(intent2);
		captcha = SMSCaptcha.getInstance();
		mContext=this;
		xmppConnectionManager=XmppConnectionManager.getInstance();
		loadDialog=new LoadingDialog(this);
		findView();
		initTitleView();
		init();
		choosecity();
		
		QApp.getInstance().addActivity(RegisterActivity.this);
		
	}
	
	private void choosecity(){
		spinCity.setOnItemSelectedListener(new CityOnItemSelectedListener());
	}
	
	private void findView(){
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		btn_complete=(Button) findViewById(R.id.register_complete);
		et_name=(EditText) findViewById(R.id.name);//账号
		et_password=(EditText) findViewById(R.id.password);//密码
		spinCity=(Spinner)super.findViewById(R.id.province);
		tv_xieyi=(TextView)findViewById(R.id.tv_xieyi);
		
		/* btnLeft = (TextView) findViewById(R.id.tvLeft);
		 btnLeft.setTextColor(this.getResources().getColorStateList(R.color.white));*/
		
	}

	private void init(){
		btn_complete.setOnClickListener(completeOnClickListener);
		 String xieyi = "<font color=" + "\"" + "#AAAAAA" + "\">" + "点击上面的"
	                + "\"" + "注册" + "\"" + "按钮,即表示你同意" + "</font>" + "<u>"
	                + "<font color=" + "\"" + "#576B95" + "\">" + "《微迪 软件许可及服务协议》"
	                + "</font>" + "</u>"; 

	     tv_xieyi.setText(Html.fromHtml(xieyi));
	    /* btnLeft.setText("取消");
	     btnLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});*/
	        
	}

	private void initTitleView(){
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.title_register_info);
		mTitleBarView.setBtnLeft(R.string.back);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 点击注册
	 */
	private OnClickListener completeOnClickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			//						account=et_name.getText().toString().trim().replaceAll("\\s*", "");
			//						String code = "+86";
			//						checkPhoneNum(account, code);
			doReg();
		}
	};
	
	

	void doReg(){
		account=et_name.getText().toString();
		password=et_password.getText().toString();
		if(TextUtils.isEmpty(account)){
			ToastUtil.showLongToast(mContext, "请填写昵称");
			return;
		}
		if(TextUtils.isEmpty(password)){
			ToastUtil.showLongToast(mContext, "请填写密码");
			return;
		}
		loadDialog.setTitle("正在注册...");
		loadDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				 mXMPPConnection = xmppConnectionManager.init();
				try {
					mXMPPConnection.connect();
					 
					//					 String wdResult = GetPostUtil
					//			                .sendGet("http://192.168.0.10:9090/plugins/jsmuser/getaccountbyphone?phone="+registname+"&type=reg");
					//			        Log.i("TAG", wdResult);
					host = mXMPPConnection.getHost();
					Log.i("TAG",host);  
					servicename = mXMPPConnection.getServiceName();
					Log.i("TAG",servicename);  
					String result1 = "1000084";
					String uriAPI ="http://"+host+":9090/plugins/jsmuser/getaccountbyphone?phone="+account+"&type=reg"; 
					if(account.length() == 11){
						Log.i("TAG",uriAPI);  
						/*建立HTTP Get对象*/  
						HttpGet httpRequest = new HttpGet(uriAPI);  
						try  
						{  
							/*发送请求并等待响应*/  
							HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);  
							/*若状态码为200 ok*/  
							if(httpResponse.getStatusLine().getStatusCode() == 200){    
								/*读*/  
								result1 = EntityUtils.toString(httpResponse.getEntity());  
								Log.i("TAG",result1);  
							}  
							else{  
								Log.i("TAG", "Error Response: "+httpResponse.getStatusLine().toString());  
							}  
						}  
						catch (Exception e)  
						{   
							e.printStackTrace();  
						} 
					}

					Log.i("TAG", result1);
					if(result1.length()<=5){
						Log.i("TAG", "************************");
						Looper.prepare();     
						Toast.makeText(RegisterActivity.this,"该用户已经存在",Toast.LENGTH_SHORT).show();  
						Looper.loop(); 
						Log.i("TAG", "************************");
					}else{
						Registration registration = new Registration();
						registration.setType(IQ.Type.SET);
						registration.setTo(servicename);
						registration.setUsername(result1);
						registration.setPassword(password);
						registration.addAttribute("android", "geolo_createUser_android");
						PacketFilter filter = new AndFilter(new PacketIDFilter(registration.getPacketID()),new PacketTypeFilter(IQ.class));
						PacketCollector collector = mXMPPConnection.createPacketCollector(filter);       
						mXMPPConnection.sendPacket(registration);
						IQ result = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
						Log.i("TAG", result.getType().toString());
						collector.cancel();
						int registerRe = registerResult(result);
						Log.i("TAG", registerRe+"");
			
						mHandler.sendEmptyMessage(registerRe);
					}
					//int result=XmppUtil.register(mXMPPConnection, account, password);
					//mHandler.sendEmptyMessage(result);
				} catch (XMPPException e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(4);
				}
			}
		}).start();
	}
	private int  registerResult(IQ result) {
		if(null==result){
			return 0;
		}else if(result.getType() == IQ.Type.RESULT){
			
			return 1;
		}else if(result.getType() == IQ.Type.ERROR){
			if(result.getError().toString().equalsIgnoreCase("conflict(409)")){
				return 2;
			}else{
				return 3;
			}
		}
		return 3;
	}
	@SuppressWarnings("unused")
	private void checkPhoneNum(String phone, String code) {

		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, R.string.smssdk_write_mobile_phone,
					Toast.LENGTH_SHORT).show();
			return;
		}

		showDialog(phone, code);

	}

	public void showDialog(final String phone, String code) {
		String phoneNum = code + " " + splitPhoneNum(phone);
		String strContent = getResources().getString(
				R.string.smssdk_make_sure_mobile_detail)
				+ phoneNum;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Captcha")
		.setIcon(R.drawable.ic_launcher)
		.setCancelable(false)
		.setMessage(strContent)
		.setPositiveButton(R.string.smssdk_ok,
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				Log.i("TAG", "ok");
				showDialog(getResources()
						.getString(
								R.string.smssdk_get_verification_code_content));
				captcha.sendCaptcha(phone,
						new ResultCallBack() {

					@Override
					public void onResult(int arg0,
							String arg1, String arg2) {
						// TODO Auto-generated method
						// stub
						closeDialog();
						if (arg0 == 0) {
							Log.i("TAG", "kaishi");
							afterCaptchaRequested();
						}

					}
				});

			}
		})
		.setNegativeButton(R.string.smssdk_cancel,
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		AlertDialog dlg = builder.create();
		dlg.show();

	}

	private void afterCaptchaRequested() {
		Log.i("TAG", "tiaozhuanqian");
		String phone = et_name.getText().toString().trim()
				.replaceAll("\\s*", "");
		String code = "+86";
		String fomatedPhone = code + " " + splitPhoneNum(phone);

		Toast.makeText(this, "成功!", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(RegisterActivity.this, CaptchaActivity.class);
		intent.putExtra("formatedPhone", fomatedPhone);
		intent.putExtra("phone", phone);
		startActivityForResult(intent, 2);
	}

	private String splitPhoneNum(String phone) {
		StringBuilder builder = new StringBuilder(phone);
		builder.reverse();
		for (int i = 4, len = builder.length(); i < len; i += 5) {
			builder.insert(i, ' ');
		}

		builder.reverse();
		return builder.toString();

	}
	@Override
	protected void onActivityResult(int requestCode, int resultcode, Intent data) {
		if (requestCode == 2 && resultcode == RESULT_OK) {
			doReg();
		}
	}
	
	 private class  CityOnItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> adapter, View arg1, int position,
				long id) {
			
			String sInfo=adapter.getItemAtPosition(position).toString();
			PreferencesUtils.putSharePre( "region", sInfo);
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		 
	 }
}
