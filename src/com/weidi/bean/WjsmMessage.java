package com.weidi.bean;

import org.jivesoftware.smack.packet.PacketExtension;

/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-6-15 上午11:58:12
 *@Description 1.0
 */
public class WjsmMessage implements PacketExtension{

    private StringBuffer packetContent = null;  
    
    public static final String ELEMENT_NAME="jsm";  
    public static final String NAME_SPACE="com:jsm:msg";  
      
    public StringBuffer getPacketContent() {  
        return packetContent;  
    }  
      
    public void setPacketContent(StringBuffer packetContent) {  
        this.packetContent = packetContent;  
    }  
      
      
    @Override  
    public String getElementName() {  
        // TODO Auto-generated method stub  
        return ELEMENT_NAME;  
    }  
  
    @Override  
    public String getNamespace() {  
        // TODO Auto-generated method stub  
        return NAME_SPACE;  
    }  
  
    @Override  
    public String toXML() {  
        // TODO Auto-generated method stub  
        return packetContent.toString();  
    }  

}
