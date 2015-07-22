package com.weidi.activity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.stream.JsonReader;
import com.weidi.R;
import com.weidi.adapter.NoticeAdapter;
import com.weidi.provider.NewsIQ;
import com.weidi.util.Const;
import com.weidi.view.TitleBarView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NoticeActivity extends Activity {
	ListView list_notice;
	List<Map<String, Object>> notice_data;
	String tmpeStr=null;
	String json="http://weidi.xincvip.com/Mall/Home/Article";
	Handler mHandler;
	Map<String, Object> map;
	TitleBarView mTitleBarView;
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_notice);
		notice_data=new ArrayList<Map<String, Object>>();
		initData();
		initTitleView();
		
		initView();
		/* mHandler = new Handler() { 	
			public void handleMessage(Message msg) {
				Log.i("YYYY", "接受到message");
				switch (msg.what) {
				case 1:
					initView();
					break;
				default:
					break;
				}
			};
		};*/
		
	}
	private void initData() {	 
		notice_data=Const.notice_data;
		int GG=Const.notice_data.size();
		Log.i("YYYY", ""+GG);
		/*DataTaste data=new DataTaste();
		data.execute();*/
	}
	
	private void initTitleView() {
		mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE, View.GONE);
		mTitleBarView.setTitleText(R.string.notice);
		mTitleBarView.setBtnLeftIcon(R.drawable.btn_back);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});

	}
	private void initView() {
		
	    list_notice=(ListView)findViewById(R.id.list_notice);
		NoticeAdapter adapter=new NoticeAdapter(NoticeActivity.this,notice_data);
		list_notice.setAdapter(adapter);
		list_notice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(NoticeActivity.this,WebViewActivity.class);
				String pathurl=(String)notice_data.get(arg2).get("link");
				Bundle bundle=new Bundle();
				bundle.putString("httpurl", pathurl);
				intent.putExtras(bundle);
				startActivity(intent);
				Log.i("URL", pathurl);	 
				TextView text=(TextView)view.findViewById(R.id.title);
				ImageView img=(ImageView)view.findViewById(R.id.img);
				img.setBackgroundResource(R.drawable.huise);
				text.setBackgroundColor(Color.parseColor("#b3b3b3"));
				RelativeLayout re_content=(RelativeLayout)view.findViewById(R.id.re_content);
				re_content.setBackgroundResource(R.drawable.hui);
			}
		}); 
	}
	
	public  String sendUrlRequest(String urlStr) throws Exception {	
		HttpURLConnection url_con=null;
		try{
		URL url=new URL(urlStr);
		StringBuffer bankXmlBuffer=new StringBuffer();
		//创建URL连接，提交到数据，获取返回结果
		HttpURLConnection connection=(HttpURLConnection)url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setRequestProperty(" Content-Type ","application/x-www-form-urlencoded");
		PrintWriter out=new PrintWriter(new OutputStreamWriter(connection.getOutputStream(),"utf-8"));
		out.close();
		BufferedReader in=new BufferedReader(new InputStreamReader(connection
		.getInputStream(),"utf-8"));

		String inputLine;

		while((inputLine=in.readLine())!=null){
		bankXmlBuffer.append(inputLine);
		}
		in.close();
		tmpeStr=bankXmlBuffer.toString();
		}
		catch(Exception e)
		{
		System.out.println("发送请求出现异常！"+e);
		e.printStackTrace();

		}finally{
		if(url_con!=null)
		url_con.disconnect();
		}
		return tmpeStr;
		}
	
	public void parserjson(String json){
		Log.i("TAG", "KAISHI LE ");
		
		
		json=json.replace("\\", "");
		json=json.substring(1, json.length()-1);
		 try {
			    JSONObject jo = new JSONObject(json);   
			    JSONArray jsonArray = (JSONArray) jo.get("NewsList");
			    for (int i = 0; i < jsonArray.length(); ++i) {
			        JSONObject object = (JSONObject) jsonArray.get(i);
			        map=new HashMap<String, Object>();
			        String n_id=object.getString("n_id");
			        map.put("n_id", n_id);
			        String sort=object.getString("sort");
			        map.put("sort", sort);
			        String title=object.getString("title"); 
			        map.put("title", title);
			        String miao=object.getString("miao");
			        map.put("miao", miao);
			        String create_time=object.getString("create_time");
			        map.put("create_time", create_time);
			        String filePath=object.getString("filePath");
			        map.put("filePath", filePath);
			        notice_data.add(map);
			        Log.i("TAG", ""+notice_data.size());
			       Log.i("RRRR", sort+title+miao+create_time+filePath);
			        
			    }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	class DataTaste extends AsyncTask<String, String, String>{

		
		@Override
		protected String doInBackground(String... arg0) {
			String datasource=null;
			try {
				datasource=sendUrlRequest(json);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return datasource;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//在线程和异步任务里面最好不要解析json
			parserjson(result);
			Message message=new Message();
			message.what=1;
			mHandler.sendMessage(message);
			Log.i("TTT", "发送message");
		}
	}
	
}
