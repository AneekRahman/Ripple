package com.ripple.www.Classes;

public class MessageListRow {

    public static final int TYPE_MSG_RECEIVED_NOT_SEEN = 0;
    public static final int TYPE_MSG_RECEIVED_SEEN = 1;
    public static final int TYPE_MSG_SENT_NOT_SEEN = 2;
    public static final int TYPE_MSG_SENT_SEEN = 3;

    private String msgUserName, msgUserDpUrl, msgMessage, msgSentTime;
    private Integer msgRowType, msgUserId;

    public MessageListRow(Integer msgRowType, Integer msgUserId, String msgUserName, String msgUserDpUrl, String msgMessage, String msgSentTime){

        this.msgRowType = msgRowType;
        this.msgUserId = msgUserId;
        this.msgUserName = msgUserName;
        this.msgUserDpUrl = msgUserDpUrl;
        this.msgMessage = msgMessage;
        this.msgSentTime = msgSentTime;

    }

    public Integer getMsgRowType() {
        return msgRowType;
    }

    public Integer getMsgUserId() {
        return msgUserId;
    }

    public String getMsgUserName() {
        return msgUserName;
    }

    public String getMsgUserDpUrl() {
        return msgUserDpUrl;
    }

    public String getMsgMessage() {
        return msgMessage;
    }

    public String getMsgSentTime() {
        return msgSentTime;
    }

}
