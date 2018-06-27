package com.example.live.model;

/**
 * Created by Administrator.
 */

public class ChatMsgInfo {

    public static final int MSGTYPE_LIST = 0;
    public static final int MSGTYPE_DANMU = 1;

    private int msgType = MSGTYPE_LIST;
    private String text;
    private String senderId;
    private String avatar;
    private String senderName;


    public static ChatMsgInfo createListInfo(String text, String userId, String avatar) {
        ChatMsgInfo chatMsgInfo = new ChatMsgInfo();
        chatMsgInfo.msgType = MSGTYPE_LIST;
        chatMsgInfo.text = text;
        chatMsgInfo.senderId = userId;
        chatMsgInfo.avatar = avatar;
        chatMsgInfo.senderName = "";

        return chatMsgInfo;
    }

    public static ChatMsgInfo createDanmuInfo(String text, String userId, String avatar, String name) {
        ChatMsgInfo chatMsgInfo = new ChatMsgInfo();
        chatMsgInfo.msgType = MSGTYPE_LIST;
        chatMsgInfo.text = text;
        chatMsgInfo.senderId = userId;
        chatMsgInfo.avatar = avatar;
        chatMsgInfo.senderName = name;

        return chatMsgInfo;
    }

    public String getContent() {
        return text;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getMsgType() {
        return msgType;
    }

    public String getSenderName() {
        return senderName;
    }


}
