package com.weidi.view;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.weidi.R;
import com.weidi.common.ViewHolder;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-6-30 下午3:47:47
 *@Description 1.0 好友信息功能键
 */
public abstract class FriendPopWindow extends PopupWindow{
	private View conentView;
    private RelativeLayout rlDelete;

	@SuppressLint("InflateParams")
	public FriendPopWindow(LayoutInflater inflater,int layoutId) {
		
	    final ViewHolder viewHolder = getViewHolder(inflater, conentView, layoutId);
	    conentView = viewHolder.getConvertView();
	    convert(viewHolder);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
         
 
    }

	public abstract void convert(ViewHolder helper);
	
	private ViewHolder getViewHolder(LayoutInflater inflater,View convertView,int layoutId)
	{
		return ViewHolder.get(inflater,convertView,layoutId);
	}


	/**
     * 显示popupWindow
     * 
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
