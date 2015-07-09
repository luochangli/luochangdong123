package com.weidi.chat;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.common.CommonAdapter;
import com.weidi.common.ViewHolder;
import com.weidi.common.base.BaseActivity;
import com.weidi.util.Const;
import com.weidi.view.ExpandGridView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 下午2:36:29
 * @Description 1.0
 */
public class GroupChatSettingActi extends BaseActivity implements OnClickListener {
	private TextView tv_groupname;

	// 成员总数
	int m_total = 0;
	// 成员列表
	private ExpandGridView gvMenberHead;
	// 修改群名称、置顶、、、、
	private RelativeLayout re_change_groupname;
	private RelativeLayout rl_switch_chattotop;
	private RelativeLayout rl_switch_block_groupmsg;
	private RelativeLayout re_clear;

	// 状态变化
	private ImageView iv_switch_chattotop, iv_switch_unchattotop,iv_switch_block_groupmsg,iv_switch_unblock_groupmsg;
	private ImageView ivBack;

	// 删除并退出

	private Button exitBtn;
	private String I;
	// 群名称
	private String group_name;
	private String groupId;
	// 是否是管理员
	boolean is_admin = false;
	List<GroupMenber> menbers;
	String longClickUsername = null;
	private CommonAdapter<GroupMenber> adapter;
	private ProgressDialog progressDialog;
	private boolean isInDeleteMode = false;

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.social_groupchatsetting_activity);
		I = Const.USER_NAME;
        menbers = new ArrayList<GroupMenber>();
        
		initView();
		// initData();
		// updateGroup();
	}

	@Override
	protected void setListener() {
		re_change_groupname.setOnClickListener(this);
		rl_switch_chattotop.setOnClickListener(this);
		rl_switch_block_groupmsg.setOnClickListener(this);

		re_clear.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
		ivBack.setOnClickListener(new OnClickListener() {
			
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

	@Override
	protected void handleMsg(Message msg) {
		// TODO Auto-generated method stub

	}

	private void initView() {
		progressDialog = new ProgressDialog(this);
		tv_groupname = (TextView) findViewById(R.id.tv_groupname);
		gvMenberHead = (ExpandGridView) findViewById(R.id.gridview);

		re_change_groupname = (RelativeLayout) findViewById(R.id.re_change_groupname);
		rl_switch_chattotop = (RelativeLayout) findViewById(R.id.rl_switch_chattotop);
		rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
		re_clear = (RelativeLayout) findViewById(R.id.re_clear);

		iv_switch_chattotop = (ImageView) findViewById(R.id.iv_switch_chattotop);
		iv_switch_unchattotop = (ImageView) findViewById(R.id.iv_switch_unchattotop);
		iv_switch_block_groupmsg = (ImageView) findViewById(R.id.iv_switch_block_groupmsg);
		iv_switch_unblock_groupmsg = (ImageView) findViewById(R.id.iv_switch_unblock_groupmsg);
		exitBtn = (Button) findViewById(R.id.btn_exit_grp);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		initAdapter();
       // gvMenberHead.setAdapter(adapter);
	}

	private void initAdapter() {
		adapter = new CommonAdapter<GroupMenber>(this, menbers,
				R.layout.social_chatsetting_gridview_item) {

			public int getCount() {
				if (is_admin) {
					return mDatas.size() + 2;
				} else {

					return mDatas.size() + 1;

				}
			}

			@Override
			public void convert(ViewHolder helper, GroupMenber item) {
				ImageView iv_avatar = (ImageView) helper
						.getView(R.id.iv_avatar);
				TextView tv_username = helper.getView(R.id.tv_username);
				ImageView badge_delete = (ImageView) helper
						.getView(R.id.badge_delete);

				int position = helper.getPosition();
				View convertView = helper.getConvertView();
				
				// 最后一个item，减人按钮
				if (position == getCount() - 1 && is_admin) {
					tv_username.setText("");
					badge_delete.setVisibility(View.GONE);
					iv_avatar
							.setImageResource(R.drawable.icon_btn_deleteperson);

					if (isInDeleteMode) {
						// 正处于删除模式下，隐藏删除按钮
						convertView.setVisibility(View.GONE);
					} else {

						convertView.setVisibility(View.VISIBLE);
					}

					iv_avatar.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							isInDeleteMode = true;
							notifyDataSetChanged();
						}

					});

				} else if ((is_admin && position == getCount() - 2)
						|| (!is_admin && position == getCount() - 1)) { // 添加群组成员按钮
					tv_username.setText("");
					badge_delete.setVisibility(View.GONE);
					iv_avatar
							.setImageResource(R.drawable.icon_btn_addperson);
					// 正处于删除模式下,隐藏添加按钮
					if (isInDeleteMode) {
						convertView.setVisibility(View.GONE);
					} else {
						convertView.setVisibility(View.VISIBLE);
					}
					iv_avatar.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							// 进入选人页面

						}
					});
				}
				else { // 普通item，显示群组成员

				}
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_switch_block_groupmsg: // 屏蔽群组
			if (iv_switch_block_groupmsg.getVisibility() == View.VISIBLE) {
				iv_switch_block_groupmsg.setVisibility(View.INVISIBLE);
				iv_switch_unblock_groupmsg.setVisibility(View.VISIBLE);

			} else {

				iv_switch_block_groupmsg.setVisibility(View.VISIBLE);
				iv_switch_unblock_groupmsg.setVisibility(View.INVISIBLE);

			}
			break;

		case R.id.re_clear: // 清空聊天记录
			progressDialog.setMessage("正在清空群消息...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
			// 按照你们要求必须有个提示，防止记录太少，删得太快，不提示

			break;
		case R.id.re_change_groupname:

			break;
		case R.id.rl_switch_chattotop:
			// 当前状态是已经置顶,点击后取消置顶
			if (iv_switch_chattotop.getVisibility() == View.VISIBLE) {
				iv_switch_chattotop.setVisibility(View.INVISIBLE);
				iv_switch_unchattotop.setVisibility(View.VISIBLE);
			} else {
				// 当前状态是未置顶点击后置顶
				iv_switch_chattotop.setVisibility(View.VISIBLE);
				iv_switch_unchattotop.setVisibility(View.INVISIBLE);
			}
			break;

		case R.id.btn_exit_grp:

			break;

		default:
			break;
		}
	}
}
