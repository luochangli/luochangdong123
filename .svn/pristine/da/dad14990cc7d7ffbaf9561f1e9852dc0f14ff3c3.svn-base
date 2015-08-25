package com.weidi.listener;

import java.util.TimerTask;

import com.weidi.util.Const;
import com.weidi.util.XmppUtil;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-8-24 上午11:50:56
 *@Description 1.0
 */
public class XmppTimetask extends TimerTask{

	@Override
	public void run() {
		try {  
            XmppUtil.getInstance().conXmpp();
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}

}
