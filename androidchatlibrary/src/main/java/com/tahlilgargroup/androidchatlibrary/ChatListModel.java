package com.tahlilgargroup.androidchatlibrary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/////////////////////////////////chat operator list model
public class ChatListModel implements Serializable {

    @SerializedName("icon")
    @Expose
    private int icon;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("counter")
    @Expose
    private String counter;

    @SerializedName("isNew")
    @Expose
    private boolean isNew;

    @SerializedName("OperatorID")
    @Expose
    private short OperatorID;

    private boolean isGroupHeader = false;

    public ChatListModel(String title, String counter, short operatorID) {
        this.title = title;
        this.counter = counter;
        OperatorID = operatorID;
    }

   /* public ChatListModel(String title, String counter) {
        super();
       // this.icon = icon;
        this.title = title;
        this.counter = counter;
    }*/

    public short getOperatorID() {
        return OperatorID;
    }

    public void setOperatorID(short operatorID) {
        OperatorID = operatorID;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public boolean isGroupHeader() {
        return isGroupHeader;
    }

    public void setGroupHeader(boolean groupHeader) {
        isGroupHeader = groupHeader;
    }
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
//gettters & setters...
}