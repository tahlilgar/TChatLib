package com.tahlilgargroup.androidchatlibrary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotyParams implements Serializable {
    @SerializedName("Sender")
    @Expose
    private String Sender;

    @SerializedName("Receiver")
    @Expose
    private String Receiver;

    @SerializedName("ID")
    @Expose
    private int ID;

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}