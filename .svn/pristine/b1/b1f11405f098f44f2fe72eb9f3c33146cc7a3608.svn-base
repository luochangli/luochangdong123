package com.weidi.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.chat.NewChatActivity;
import com.weidi.common.CharacterParser;
import com.weidi.common.ClearEditText;
import com.weidi.common.SortAdapter;
import com.weidi.common.SortModel;
import com.weidi.common.base.BaseActivity;
import com.weidi.fragment.NewConstactFragment;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-31 下午1:54:23
 * @Description 1.0
 */
public class ConstactSearchActi extends BaseActivity {

	TextView tvBack;
	ListView fitlerList;
	SortAdapter adapter;
	ClearEditText mClearEditText;
	private CharacterParser characterParser;
	List<SortModel> mData;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.acti_constact_search);

		initView();
		initData();
		showInput();
	}

	private void initData() {
		characterParser = CharacterParser.getInstance();
		mData = NewConstactFragment.sourceDateList;

	}

	private void initView() {
		tvBack = (TextView) findViewById(R.id.tvBack);
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		fitlerList = (ListView) findViewById(R.id.country_lvcountry);

	}

	private void showInput() {
		mClearEditText.setFocusable(true);

		mClearEditText.setFocusableInTouchMode(true);

		mClearEditText.requestFocus();

		Timer timer = new Timer();

		timer.schedule(new TimerTask()

		{

			public void run()

			{
				InputMethodManager inputManager = (InputMethodManager) mClearEditText
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(mClearEditText, 0);
			}
		}, 200);

	}

	@Override
	protected void setListener() {
		tvBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				filterData(arg0.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		fitlerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String from = ((SortModel) adapter.getItem(position))
						.getValue();
				Intent intent = new Intent(mContext, NewChatActivity.class);
				intent.putExtra("from", from);
				startActivity(intent);
				finish();
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

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = mData;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : mData) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}
		// 根据a-z进行排序
		// Collections.sort(filterDateList, pinyinComparator);
		if (filterDateList.size() > 0) {
			fitlerList.setVisibility(View.VISIBLE);
			adapter = new SortAdapter(mContext, filterDateList);
			fitlerList.setAdapter(adapter);
		} else {
			fitlerList.setVisibility(View.GONE);
		}

	}

	@Override
	protected void onDestroy() {
		QApp.mLocalBroadcastManager.sendBroadcast(new Intent(
				NewConstactFragment.SEARCH));
		super.onDestroy();
	}
}
