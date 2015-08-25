package com.weidi.chat.repository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.util.StringUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.weidi.chat.ErrorHandle;
import com.weidi.chat.GroupChatSettingActi;
import com.weidi.chat.IQOrder;
import com.weidi.chat.bean.GroupInfo;
import com.weidi.chat.bean.GroupInfoDao;
import com.weidi.chat.bean.Menbers;
import com.weidi.chat.bean.MenbersDao;
import com.weidi.provider.ObtainMUCInfoIQ;
import com.weidi.provider.ObtainMUCmemberIQ;
import com.weidi.util.ToastUtil;
import com.weidi.util.XmppLoadThread;
import com.weidi.util.XmppUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-11 上午10:33:59
 * @Description 1.0
 */
public class MucChatRepo {
	// 参数错误
	public static final String Error_Param = "00001";
	public static final String Error_Param_S = "参数错误。";
	// 会议室不存在
	public static final String Error_Muc_Not_Exist = "00002";
	public static final String Error_Muc_Not_Exist_S = "会议室不存在。";
	// 权限不够
	public static final String Error_Not_Power = "00003";
	public static final String Error_Not_Power_S = "权限不够。";
	// 没有此人
	public static final String Error_Not_FoundUser = "00004";
	public static final String Error_Not_FoundUser_S = "没有此用户。";
	// 没有此人
	public static final String Error_DB = "00005";
	public static final String Error_DB_S = "服务器DB错。";
	// 房间已存在
	public static final String Error_Room_Existed = "00006";
	public static final String Error_Room_Existed_S = "房间已存在。";

	private static String TAG = MucChatRepo.class.getSimpleName();
	private MenbersDao menbersDao;
	private GroupInfoDao infoDao;
	private Context mContext;
	private Handler mHandler;

	public MucChatRepo(Context context, Handler handler) {
		menbersDao = MenbersDao.getInstance();
		infoDao = GroupInfoDao.getInstance();
		this.mContext = context;
		this.mHandler = handler;
	}

	public void getInfoFromDb(String muc) {
		GroupInfo item = infoDao.queryByMuc(muc);
		if (item == null) {
			getinfoFromIQ(muc);
		} else {
			Message msg = new Message();
			msg.what = GroupChatSettingActi.INFO;
			msg.obj = item;
			mHandler.sendMessage(msg);
		}

	}

	public void getinfoFromIQ(final String muc) {

		ObtainMUCInfoIQ iq = IQOrder.getInstance().obtainMUCInfo(
				XmppUtil.getFullMUC(muc));
		try {
			if (iq == null) {
				ToastUtil.showShortToast(mContext, "服务器异常，请稍后再试！");
				return;
			}
			if (iq.getErrorCode() != null) {
				ErrorHandle.mucChatError(iq.getErrorCode());
				return;
			}
			GroupInfo item = new GroupInfo();
			item.setCreatedatetime(StringUtils
					.parseDate(iq.getCrieatdatetime()).toString());
			item.setDescription(iq.getDescription());
			item.setMuc(StringUtils.parseName(iq.getMuc()));
			item.setName(iq.getName());
			item.setMe(StringUtils.parseName(iq.getTo()));
			int id = (int) infoDao.insert(item);
			item.set_id(id);
			Message msg = new Message();
			msg.what = GroupChatSettingActi.INFO;
			msg.obj = item;
			mHandler.sendMessage(msg);

		} catch (ParseException e) {
			e.printStackTrace();

		}
	}

	public void getMenbByIQ(final String muc) {

		ObtainMUCmemberIQ iq = IQOrder.getInstance().obtainMUCGetmember(
				XmppUtil.getFullMUC(muc));
		if (iq == null) {
			ToastUtil.showShortToast(mContext, "服务器异常，请稍后再试！");
			return;
		}
		if (iq.getErrorCode() != null) {
			ErrorHandle.mucChatError(iq.getErrorCode());
			return;
		}
		List<Menbers> items = new ArrayList<Menbers>();
		Menbers temp;
		for (ObtainMUCmemberIQ.Item item : iq.getItems()) {
			temp = new Menbers();
			temp.setAffiliation(item.getAffiliation());
			temp.setJid(StringUtils.parseName(item.getJid()));
			temp.setMuc(StringUtils.parseName(iq.getMuc()));
			temp.setNick(item.getNick());
			temp.setMe(StringUtils.parseName(iq.getTo()));
			int id = (int) menbersDao.insert(temp);
			temp.set_id(id);
			items.add(temp);
		}
		Message msg = new Message();
		msg.what = GroupChatSettingActi.MUC_MENBERS;
		msg.obj = items;
		mHandler.sendMessage(msg);
	}

	public void getMenbByDb(String muc) {
		List<Menbers> items = menbersDao.queryAllByMuc(muc);
		if (items.size() == 0) {
			getMenbByIQ(muc);
		} else {
			Message msg = new Message();
			msg.what = GroupChatSettingActi.MUC_MENBERS;
			msg.obj = items;
			mHandler.sendMessage(msg);
		}

	}

}
