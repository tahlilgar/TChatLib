package com.tahlilgargroup.androidchatlibrary;

import android.net.Uri;

import java.math.BigInteger;

public class ChatAppMsgDTO {
    public final static String MSG_TYPE_SENT = "MSG_TYPE_SENT";

    public final static String MSG_TYPE_RECEIVED = "MSG_TYPE_RECEIVED";

    public final static String IMG_TYPE_SENT = "IMG_TYPE_SENT";

    public final static String IMG_TYPE_RECEIVED = "IMG_TYPE_RECEIVED";

    public final static String Video_TYPE_SENT = "Video_TYPE_SENT";

    public final static String Video_TYPE_RECEIVED = "Video_TYPE_RECEIVED";

    public final static String Voice_TYPE_SENT = "Voice_TYPE_SENT";

    public final static String Voice_TYPE_RECEIVED = "Voice_TYPE_RECEIVED";

    public final static String TYPE_DATE = "TYPE_DATE";


    // Message content.
    private String msgContent;


    private boolean IsSearched;

    //for image or video of voice message
    private byte[] msgFile;
    private Uri msgVideo;

    // Message type.
    private String msgType;
    private String OperatorName;
    private String time;
    private boolean Seen;
    private BigInteger ID;
    private boolean Edited;

    public ChatAppMsgDTO(String msgType, String msgContent, String operatorName, String time, BigInteger id, boolean edited) {
        this.msgType = msgType;
        this.msgContent = msgContent;
        this.OperatorName = operatorName;
        this.time = time;
        this.ID=id;
        this.Edited=edited;
    }

    //for Search
    public ChatAppMsgDTO(String msgContent, boolean isSearched, String msgType, String operatorName, String time) {
        this.msgContent = msgContent;
        this.IsSearched = isSearched;
        this.msgType = msgType;
        OperatorName = operatorName;
        this.time = time;
    }

    //for sent messages
    public ChatAppMsgDTO(String msgType, String msgContent, String operatorName, String time, boolean seen , BigInteger id, boolean edited) {
        this.msgContent = msgContent;
        this.msgType = msgType;
        this.OperatorName = operatorName;
        this.time = time;
        this.Seen = seen;
        this.ID=id;
        this.Edited=edited;
    }



    /* public ChatAppMsgDTO(String msgType, byte[] msgFile, String operatorName, String time) {
        this.msgType = msgType;
        this.msgFile = msgFile;
        this.OperatorName=operatorName;
        this.time=time;
    }
    public ChatAppMsgDTO(String msgType, Uri msgFile, String operatorName, String time) {
        this.msgType = msgType;
        this.msgVideo = msgFile;
        this.OperatorName=operatorName;
        this.time=time;
    }*/

    /*  public byte[] getMsgFile() {
          return msgFile;
      }

      public void setMsgFile(byte[] msgFile) {
          this.msgFile = msgFile;
      }
  */
    public String getOperatorName() {
        return OperatorName;
    }

    public void setOperatorName(String operatorName) {
        OperatorName = operatorName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }


    public boolean isSearched() {
        return IsSearched;
    }

    public void setSearched(boolean searched) {
        IsSearched = searched;
    }

    public boolean isSeen() {
        return Seen;
    }

    public void setSeen(boolean seen) {
        Seen = seen;
    }

    public BigInteger getID() {
        return ID;
    }

    public void setID(BigInteger ID) {
        this.ID = ID;
    }

    public boolean isEdited() {
        return Edited;
    }

    public void setEdited(boolean edited) {
        Edited = edited;
    }
}
