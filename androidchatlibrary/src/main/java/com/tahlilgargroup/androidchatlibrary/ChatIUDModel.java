package com.tahlilgargroup.androidchatlibrary;

public class ChatIUDModel {

    private int ID; //0//for get and edit and delete=>id message
    private int OpMode; ////new 0//edit 1//delete 2
    private int KCode;//id person get message
    private String PCode;//id person sender message
    private String Message;// text message
    private boolean SenderType;//true
    private int MessageType;///text 0//photo 1//video 2//voice 3
    private String IMEI;
    private String MachineName;
    private String ipAddress;
    private double Lat;
    private double Lng;
    private boolean RemoveBoth;//remove tow >true else false

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getOpMode() {
        return OpMode;
    }

    public void setOpMode(int opMode) {
        OpMode = opMode;
    }

    public int getKCode() {
        return KCode;
    }

    public void setKCode(int KCode) {
        this.KCode = KCode;
    }

    public String getPCode() {
        return PCode;
    }

    public void setPCode(String PCode) {
        this.PCode = PCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isSenderType() {
        return SenderType;
    }

    public void setSenderType(boolean senderType) {
        SenderType = senderType;
    }

    public int getMessageType() {
        return MessageType;
    }

    public void setMessageType(int messageType) {
        MessageType = messageType;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getMachineName() {
        return MachineName;
    }

    public void setMachineName(String machineName) {
        MachineName = machineName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public boolean isRemoveBoth() {
        return RemoveBoth;
    }

    public void setRemoveBoth(boolean removeBoth) {
        RemoveBoth = removeBoth;
    }
}
