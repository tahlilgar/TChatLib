package com.tahlilgargroup.androidchatlibrary;

public class Messages {
    private int ID;
    private String MsgID;
    private int MsgType;
    private int MsgPos;
    private String MsgContent;
    private String LocalPath;
    private String HostPath;
    private String DriverID;
    private String OperatorID;
    private String MsgDate;
    private String MsgTime;
    private int isSend;
    private byte[] Image;
    private int IsSeen;
    private int IsEdited;


    public int getIsSeen() {
        return IsSeen;
    }

    public void setIsSeen(int isSeen) {
        IsSeen = isSeen;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public String getMsgID() {
        return MsgID;
    }

    public void setMsgID(String msgID) {
        MsgID = msgID;
    }

    public int getMsgType() {
        return MsgType;
    }

    public void setMsgType(int msgType) {
        MsgType = msgType;
    }

    public int getMsgPos() {
        return MsgPos;
    }

    public void setMsgPos(int msgPos) {
        MsgPos = msgPos;
    }

    public String getMsgContent() {
        return MsgContent;
    }

    public void setMsgContent(String msgContent) {
        MsgContent = msgContent;
    }

    public String getLocalPath() {
        return LocalPath;
    }

    public void setLocalPath(String localPath) {
        LocalPath = localPath;
    }

    public String getHostPath() {
        return HostPath;
    }

    public void setHostPath(String hostPath) {
        HostPath = hostPath;
    }

    public String getDriverID() {
        return DriverID;
    }

    public void setDriverID(String driverID) {
        DriverID = driverID;
    }

    public String getOperatorID() {
        return OperatorID;
    }

    public void setOperatorID(String operatorID) {
        OperatorID = operatorID;
    }

    public String getMsgDate() {
        return MsgDate;
    }

    public void setMsgDate(String msgDate) {
        MsgDate = msgDate;
    }

    public String getMsgTime() {
        return MsgTime;
    }

    public void setMsgTime(String msgTime) {
        MsgTime = msgTime;
    }

    public int getIsEdited() {
        return IsEdited;
    }

    public void setIsEdited(int isEdited) {
        IsEdited = isEdited;
    }
}
