package com.tahlilgargroup.androidchatlibrary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DistributionChatParam implements Serializable {


    @SerializedName("PCode")
    @Expose
    private String PCode;

    @SerializedName("KCode")
    @Expose
    private short KCode;

    @SerializedName("Count")
    @Expose
    private int Count;

    @SerializedName("SenderType")
    @Expose
    private boolean SenderType;

    @SerializedName("IsSwipe")
    @Expose
    private boolean IsSwipe;

    public DistributionChatParam(String RCode, short KCode, int count, boolean senderType, boolean isSwipe) {
        this.PCode = RCode;
        this.KCode = KCode;
        Count = count;
        SenderType = senderType;
        IsSwipe = isSwipe;
    }

    public String getPCode() {
        return PCode;
    }

    public void setPCode(String PCode) {
        this.PCode = PCode;
    }

    public short getKCode() {
        return KCode;
    }

    public void setKCode(short KCode) {
        this.KCode = KCode;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public boolean isSenderType() {
        return SenderType;
    }

    public void setSenderType(boolean senderType) {
        SenderType = senderType;
    }

    public boolean isSwipe() {
        return IsSwipe;
    }

    public void setSwipe(boolean swipe) {
        IsSwipe = swipe;
    }
}
