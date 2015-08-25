package com.weidi.chat.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.util.StringUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;

import com.weidi.QApp;
import com.weidi.bean.User;
import com.weidi.common.PinyinComparator;
import com.weidi.common.SortModel;
import com.weidi.util.XmppUtil;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-12 下午6:22:40
 * @Description 1.0 通讯录
 */
public class ConstactRepo {

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private Context mContext;

	public ConstactRepo(Context context) {
		mContext = context;
		pinyinComparator = new PinyinComparator();
	}

	public List<SortModel> getMyConstact() {
		List<String> list = XmppUtil.getRosterBoth(QApp.getXmppConnection()
				.getRoster());
		List<SortModel> sourceDateList = new ArrayList<SortModel>();
		if (list.size() > 0) {
			sourceDateList = getSortData(list);
		}
		// 根据a-z进行排序源数据
		Collections.sort(sourceDateList, pinyinComparator);
		return sourceDateList;
	}

	public List<SortModel> getNewConstact() {
		Collection<RosterEntry> entries = XmppUtil.getInstance().getMyRoster();
		return tempSortData(entries);

	}

	private List<SortModel> tempSortData(Collection<RosterEntry> entries) {
		List<SortModel> models = new ArrayList<SortModel>();
		SortModel sortModel;
		for (RosterEntry item : entries) {
			sortModel = new SortModel();
			String user = StringUtils.parseName(item.getUser());
			sortModel.setValue(item.getUser());
			sortModel.setName(item.getName());
			if (user.length() == 6) {
				sortModel.setVip(true);
			} else {
				sortModel.setVip(false);
			}
			models.add(sortModel);
		}
		return models;
	}

	private List<SortModel> getSortData(List<String> data) {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		SortModel sortModel;
		for (int i = 0; i < data.size(); i++) {
			try {

				String to = StringUtils.parseName(data.get(i));

				sortModel = RemarkRepo.getInstance(mContext).getSortModel(to);
				if (to.length() == 6) {
					sortModel.setVip(true);
				} else {
					sortModel.setVip(false);
				}
				mSortList.add(sortModel);
			} catch (Exception e) {

			}

		}
		return mSortList;
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param data
	 * @return
	 */
	private List<SortModel> filledData(List<String> data) {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		SortModel sortModel;
		for (int i = 0; i < data.size(); i++) {
			try {

				String to = StringUtils.parseName(data.get(i));
				sortModel = RemarkRepo.getNick(to);

				mSortList.add(sortModel);
			} catch (Exception e) {

			}

		}
		return mSortList;

	}

	public ArrayList<Map<String, Object>> getConstact() {
		ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		List<String> list = XmppUtil.getRosterBoth(QApp.getXmppConnection()
				.getRoster());
		List<SortModel> sourceDateList = new ArrayList<SortModel>();
		if (list.size() > 0) {
			sourceDateList = filledData(list);
			items = getPhoneContacts(sourceDateList);

		}
		return items;

	}

	/** 得到手机通讯录联系人信息 **/
	private ArrayList<Map<String, Object>> getPhoneContacts(
			List<SortModel> dateList) {
		ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		if (dateList != null) {
			for (SortModel item : dateList) {
				Map<String, Object> map = new HashMap<String, Object>();

				// 得到联系人名称
				String contactName = item.getName();

				String contactSort = CnToSpell.getFullSpell(contactName)
						.toUpperCase().substring(0, 1);
				if (contactSort.equals("0") || contactSort.equals("1")
						|| contactSort.equals("2") || contactSort.equals("3")
						|| contactSort.equals("4") || contactSort.equals("5")
						|| contactSort.equals("6") || contactSort.equals("7")
						|| contactSort.equals("8") || contactSort.equals("9")) {
					contactSort = "#";
				}
				item.setSortLetters(contactSort);
				// 得到手机号码
				String phoneNumber = item.getValue();
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// 得到联系人头像Bitamp
				Bitmap contactPhoto = item.getPhoto();

				map.put("Name", contactName);
				map.put("phoneNumber", phoneNumber);
				map.put("contactPhoto", contactPhoto);
				map.put("Sort", contactSort);

				items.add(map);
			}
		}
		return items;
	}

}
