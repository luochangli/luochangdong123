package com.weidi.chat.groupchat;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.R;
import com.weidi.chat.ChatGroupOrder;
import com.weidi.chat.GroupChatSettingActi;
import com.weidi.common.CircularImage;
import com.weidi.common.CommonAdapter;
import com.weidi.common.SortModel;
import com.weidi.common.ViewHolder;
import com.weidi.common.base.BaseActivity;
import com.weidi.common.image.ImgConfig;
import com.weidi.fragment.NewConstactFragment;
import com.weidi.provider.CreateMUCIQ;
import com.weidi.provider.CreateMUCProvider;
import com.weidi.provider.NearTime;
import com.weidi.provider.ObtainMUCInfoIQ;
import com.weidi.provider.ObtainMUCListIQ;
import com.weidi.util.Const;
import com.weidi.util.Logger;
import com.weidi.view.CircleImageView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-6 下午4:29:45
 * @Description 1.0
 */
public class CreatChatRoomActivity extends BaseActivity {

	private static String TAG = "CreatChatRoomActivity";
	private ImageView iv_search, ivBack;
	private TextView tvSubmit;
	private ListView listView;
	private EditText etSearch;
	/** 是否为一个新建的群组 */
	protected boolean isCreatingNewGroup;
	/** 是否为单选 */
	private boolean isSignleChecked;

	private CommonAdapter<SortModel> adapter;
	/** group中一开始就有的成员 */
	private List<String> exitingMembers = new ArrayList<String>();
	// 新添加的列表
	private List<String> addList = new ArrayList<String>();
	/**
	 * 可滑动的显示选中用户的View 头像
	 */
	private LinearLayout menuLinerLayout;

	// 选中用户总数,右上角显示
	int total = 0;
	private String userId = null;
	private String groupId = null;
	private ProgressDialog progressDialog;
	private String groupname;

	/**
	 * 所有成员
	 */
	private List<SortModel> alluserList;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_chatroom);
		progressDialog = new ProgressDialog(this);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		iv_search = (ImageView) findViewById(R.id.iv_search);
		tvSubmit = (TextView) findViewById(R.id.tv_checked);
		listView = (ListView) findViewById(R.id.lvCreGroupChat);
		etSearch = (EditText) findViewById(R.id.et_search);
		menuLinerLayout = (LinearLayout) this
				.findViewById(R.id.linearLayoutMenu);

		alluserList = NewConstactFragment.sourceDateList;
		setLVAdapter();
	}

	private void setLVAdapter() {

		adapter = new CommonAdapter<SortModel>(this, alluserList,
				R.layout.item_contactlist_listview_checkbox) {

			@Override
			public void convert(ViewHolder helper, final SortModel item) {

				CircleImageView iv_avatar = (CircleImageView) helper
						.getView(R.id.iv_avatar);
				final CheckBox checkBox = (CheckBox) helper
						.getView(R.id.checkbox);

				helper.setText(
						R.id.tv_name,
						item.getName() == null ? item.getValue() : item
								.getName());
				helper.setText(R.id.header, item.getSortLetters());
				ImgConfig.showHeadImg(item.getValue(), iv_avatar);

				// 群组中原来的成员一直设为选中状态
				if (exitingMembers != null
						&& exitingMembers.contains(item.getValue())) {
					checkBox.setButtonDrawable(R.drawable.btn_check);
					checkBox.setChecked(true);
				}

				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// 群组中原来的成员一直设为选中状态
						if (exitingMembers.contains(item.getValue())) {
							isChecked = true;
							checkBox.setChecked(false);
						}

						if (isChecked) {
							setChecked(item, checkBox);
						} else {
							total--;
							deleteImage(item.getValue());
							addList.remove(item.getValue());
						}
					}

					private void setChecked(final SortModel item,
							final CheckBox checkBox) {
						// 选中用户显示在滑动栏显示
						addList.add(item.getValue());
						total++;
						tvSubmit.setText("确定(" + total + ")");
						if (total > 0) {
							if (iv_search.getVisibility() == View.VISIBLE) {
								iv_search.setVisibility(View.GONE);
							}
						}
						View view = LayoutInflater.from(mContext).inflate(
								R.layout.item_chatroom_header_item, null);
						CircularImage friendHead = (CircularImage) view
								.findViewById(R.id.iv_avatar);
						friendHead.setOnClickListener(new OnClickListener() {

							@SuppressLint("NewApi")
							@Override
							public void onClick(View v) {
								checkBox.setChecked(false);
							}
						});
						// 设置id，方便后面删除
						view.setTag(item.getValue());
						ImgConfig.showHeadImg(item.getValue(), friendHead);
						menuLinerLayout.addView(view);
					}
				});

			}
		};
		listView.setAdapter(adapter);
	}

	private void deleteImage(String friend) {
		View view = (View) menuLinerLayout.findViewWithTag(friend);

		menuLinerLayout.removeView(view);
		tvSubmit.setText("确定(" + total + ")");
		addList.remove(friend);
		if (total < 1) {
			if (iv_search.getVisibility() == View.GONE) {
				iv_search.setVisibility(View.VISIBLE);
			}
		}

	}

	@Override
	protected void setListener() {

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				checkBox.toggle();

			}
		});
		tvSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChatGroupOrder.getInstance().createMUCRoom("测试群昵称", "测试群描述",
						Const.loginUser.getNickname());
                    startActivity(new Intent(mContext, GroupChatSettingActi.class));
                    finish();
			}

		});

		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					String str_s = etSearch.getText().toString().trim();
					List<SortModel> users_temp = new ArrayList<SortModel>();
					for (SortModel user : alluserList) {
						String usernick = user.getName() == null ? user
								.getValue() : user.getName();

						if (usernick.contains(str_s)) {

							users_temp.add(user);
						}
					}
					adapter.clear();
					adapter.addAll(users_temp);

				} else {
					adapter.clear();
					adapter.addAll(alluserList);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

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

}
