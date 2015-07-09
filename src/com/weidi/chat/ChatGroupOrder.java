package com.weidi.chat;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;

import com.weidi.QApp;
import com.weidi.provider.CreateMUCIQ;
import com.weidi.provider.CreateMUCProvider;
import com.weidi.provider.ObtainMUCInfoIQ;
import com.weidi.provider.ObtainMUCInfoProvider;
import com.weidi.provider.ObtainMUCListIQ;
import com.weidi.provider.ObtainMUCListProvider;
import com.weidi.util.Const;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-7-9 下午1:54:50
 *@Description 1.0
 */
public class ChatGroupOrder {

	private static ChatGroupOrder instance;
	private XMPPConnection conn = QApp.xmppConnection;
	private ProviderManager pm = ProviderManager.getInstance();
	
	
	public static ChatGroupOrder getInstance(){
		if(instance == null){
			instance = new ChatGroupOrder();
		}
		return instance;
	}
	
	/**
	 * 创建群
	 * @param mucName
	 * @param description
	 * @param usernick
	 */
	public void createMUCRoom(String mucName,String description,String usernick){
		CreateMUCIQ iq = new CreateMUCIQ(mucName,
				description, usernick);
		iq.setType(IQ.Type.SET);
		pm.addIQProvider(Const.QUERY, Const.XMLNS, new CreateMUCProvider());
		conn.sendPacket(iq);
	}
	
	/**获取群列表
	 * @param muc
	 */
	public void obtainMUCList(){
		pm.addIQProvider(Const.QUERY, Const.XMLNS, new ObtainMUCListProvider());
		ObtainMUCListIQ iq = new ObtainMUCListIQ();
		iq.setType(IQ.Type.GET);
		conn.sendPacket(iq);
		
	}
	
	/**
	 * 获取指定群信息
	 * @param muc
	 */
	public void obtainMUCInfo(String muc){
        pm.addIQProvider(Const.QUERY, Const.XMLNS, new ObtainMUCInfoProvider());
		ObtainMUCInfoIQ iq = new ObtainMUCInfoIQ();
		iq.setMuc(muc);
		iq.setType(IQ.Type.GET);
		QApp.xmppConnection.sendPacket(iq);
	}
}
