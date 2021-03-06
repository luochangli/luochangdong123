package com.weidi.common;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.weidi.R;
import com.weidi.chat.repository.RemarkRepo;
import com.weidi.common.image.ImgConfig;
import com.weidi.view.CircleImageView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-8 下午5:19:17
 * @Description 1.0
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;

	public SortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;

	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<SortModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup arg2) {
		final ViewHolder hodler;
		final SortModel item = list.get(position);

		if (convertView == null) {

			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_sort_listview, null);
			hodler = new ViewHolder();
            hodler.vip = (TextView) convertView.findViewById(R.id.vip);
			hodler.tvTitle = (TextView) convertView.findViewById(R.id.title);
			hodler.ivHeader = (CircleImageView) convertView
					.findViewById(R.id.iv_head_img);
			hodler.tvLetter = (TextView) convertView.findViewById(R.id.catalog);

			convertView.setTag(hodler);

		} else {
			hodler = (ViewHolder) convertView.getTag();
		}
		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			hodler.tvLetter.setVisibility(View.VISIBLE);
			hodler.tvLetter.setText(item.getSortLetters());
		} else {
			hodler.tvLetter.setVisibility(View.GONE);
		}
		
		hodler.ivHeader.setTag(item.getValue());
		if (hodler.ivHeader.getTag().equals(item.getValue())) {
			hodler.ivHeader.setImageBitmap(item.getPhoto());
		}
		hodler.tvTitle.setTag(item.getValue());
		if (hodler.tvTitle.getTag().equals(item.getValue())) {
			if (item.getVip()) {
				hodler.tvTitle.setTextColor(mContext.getResources().getColor(
						R.color.vip));
				hodler.tvTitle.setText(item.getName());
				hodler.vip.setVisibility(View.VISIBLE);
			}else{
				hodler.tvTitle.setTextColor(mContext.getResources().getColor(
						R.color.text_color_deep));
				hodler.tvTitle.setText(item.getName());
				hodler.vip.setVisibility(View.GONE);
			}
		
			
		}

		return convertView;

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		CircleImageView ivHeader;
		TextView vip;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}