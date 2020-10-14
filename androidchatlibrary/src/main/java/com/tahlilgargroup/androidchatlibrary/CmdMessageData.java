package com.tahlilgargroup.androidchatlibrary;

import android.view.View;

//نگهدارنده اطلاعات پیام چت برای انجام عملیات
public class CmdMessageData {
    private int MessagePosition;
    private String MessageType;
    private View MessageLayout;
    private ChatAppMsgDTO chatAppMsgDTO;

  /*  public CmdMessageData(int messagePosition, String messageType, View messageLayout) {
        MessagePosition = messagePosition;
        MessageType = messageType;
        MessageLayout = messageLayout;
    }*/

    public CmdMessageData(int messagePosition, String messageType, View messageLayout, ChatAppMsgDTO chatAppMsgDTO) {
        MessagePosition = messagePosition;
        MessageType = messageType;
        MessageLayout = messageLayout;
        this.chatAppMsgDTO = chatAppMsgDTO;
    }

    public int getMessagePosition() {
        return MessagePosition;
    }

    public void setMessagePosition(int messagePosition) {
        MessagePosition = messagePosition;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public View getMessageLayout() {
        return MessageLayout;
    }

    public void setMessageLayout(View messageLayout) {
        MessageLayout = messageLayout;
    }

    public ChatAppMsgDTO getChatAppMsgDTO() {
        return chatAppMsgDTO;
    }

    public void setChatAppMsgDTO(ChatAppMsgDTO chatAppMsgDTO) {
        this.chatAppMsgDTO = chatAppMsgDTO;
    }
}
