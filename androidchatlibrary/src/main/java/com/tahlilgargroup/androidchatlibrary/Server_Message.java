package com.tahlilgargroup.androidchatlibrary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class Server_Message {
    @SerializedName("ID")
    @Expose
    private BigInteger ID;

    @SerializedName("KCode")
    @Expose
    private short KCode;

    @SerializedName("PCode")
    @Expose
    private String RCode;

    @SerializedName("MsgText")
    @Expose
    private String MsgText;

    @SerializedName("SenderType")
    @Expose
    private boolean SenderType;//if Drier is Sender, Sender Type is true

    @SerializedName("IsSeen")
    @Expose
    private boolean IsSeen;



    @SerializedName("MsgType")
    @Expose
    private byte MsgType;

    @SerializedName("IsEdit")
    @Expose
    private boolean IsEdit;


    @SerializedName("PersianDate")
    @Expose
    private String PersianDate;

    @SerializedName("PersianTime")
    @Expose
    private String PersianTime;




    public String getID() {
        return String.valueOf(ID);
    }

    public void setID(String ID) {
        this.ID = BigInteger.valueOf(Long.parseLong(ID));
    }

    public int getOperatorID() {
        return KCode;
    }

    public void setOperatorID(int operatorID) {
        KCode = (short) operatorID;
    }

    public String getDriverID() {
        return RCode;
    }

    public void setDriverID(String driverID) {
        RCode = driverID;
    }

    public String getMessage() {
        return MsgText;
    }

    public void setMessage(String msgText) {
        MsgText = msgText;
    }

    public boolean isSenderType() {
        return SenderType;
    }

    public void setSenderType(boolean senderType) {
        SenderType = senderType;
    }

    public boolean isSeen() {
        return IsSeen;
    }

    public void setSeen(boolean seen) {
        IsSeen = seen;
    }

    public byte getMsgType() {
        return MsgType;
    }

    public void setMsgType(byte msgType) {
        MsgType = msgType;
    }

    public boolean isEdit() {
        return IsEdit;
    }

    public void setEdit(boolean edit) {
        IsEdit = edit;
    }

    public String getDateSend() {
        return PersianDate;
    }

    public void setDateSend(String dateSend) {
        PersianDate = dateSend;
    }

    public String getTimeSend() {
        return PersianTime;
    }

    public void setTimeSend(String timeSend) {
        PersianTime = timeSend;
    }


}
