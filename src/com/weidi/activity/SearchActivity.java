package com.weidi.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.weidi.QApp;
import com.weidi.R;
import com.weidi.adapter.SearchAdapter;
import com.weidi.common.base.LuoBaseActivity;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-25 上午10:00:08
 * @Description 1.0
 */
public class SearchActivity extends LuoBaseActivity {
	private static String TAG = "SearchActivity";
	private ListView listView;
	private EditText searchText;
	private Button searchBtn;
	private SearchAdapter adapter;
	ImageView leftBtn;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_search);
		initView();

		adapter = new SearchAdapter(this);
		listView.setAdapter(adapter);

	}

	private void initView() {
		listView = (ListView) findViewById(R.id.lvFriend);
		searchText = (EditText) findViewById(R.id.searchText);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		leftBtn = (ImageView) findViewById(R.id.leftBtn);
	}

	public void search(View v) {
		String acount = searchText.getText().toString();
		if (acount.length() == 11) {
			getWeidi(acount);
		}else{
			searchFriend(acount);
		}
	
	}

	private void searchFriend(final String name) {
		if (TextUtils.isEmpty(name)) {
			ToastUtil.showShortToast(SearchActivity.this,
					getString(R.string.hint_search_name));
		} else {
			new XmppLoadThread(this) {

				@Override
				protected void result(Object object) {
					@SuppressWarnings("unchecked")
					List<String> userList = (ArrayList<String>) object;
					adapter.clear();
					adapter.addAll(userList);
					if (adapter.getCount() == 0) {
						listView.setVisibility(View.GONE);
					} else {
						listView.setVisibility(View.VISIBLE);
					}
				}

				@Override
				protected Object load() {
					return XmppUtil.searchUser(QApp.xmppConnection, name);
				}
			};
		}
	}

	@Override
	protected void setListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						FriendActivity.class);
				intent.putExtra("username", adapter.getItem(position));
				startActivity(intent);
			}
		});
		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	@Override
	protected void afterViews(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	private void getWeidi(String phone) {
		String url = "http://" + QApp.xmppConnection.getHost()
				+ ":9090/plugins/jsmuser/getaccountbyphone?phone=" + phone
				+ "&type=get";
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Logger.e(TAG, "获取微迪号失败：" + arg1);

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Logger.e(TAG, "获取微迪号成功：" + arg0.result);
				android.os.Message msg = new android.os.Message();
				msg.what = Const.HANDLER_PHONE_TO_WEIDI;
				msg.obj = arg0.result;
				mHandler.sendMessage(msg);
			}
		});
	}

	@Override
	protected void handleMsg(Message msg) {
		switch (msg.what) {
		case Const.HANDLER_PHONE_TO_WEIDI:
			String name = (String) msg.obj;
			searchFriend(name);
			break;
		}

	}

}
