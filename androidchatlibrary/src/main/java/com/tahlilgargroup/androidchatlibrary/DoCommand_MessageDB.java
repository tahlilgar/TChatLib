package com.tahlilgargroup.androidchatlibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.microsoft.appcenter.analytics.Analytics;
import com.tahlilgargroup.commonlibrary.CommonClass;

import java.util.ArrayList;
import java.util.List;


//انجام عملیات روی دیتابیس محلی چت
public class DoCommand_MessageDB {
    private SQLiteDatabase mydb;

    public DoCommand_MessageDB(Context context) {
        mydb = new DB_Creator(context).getWritableDatabase();
    }

    public long addNewMessages(Messages messages) {
        long res = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("MsgID", messages.getMsgID());
            values.put("MsgType", messages.getMsgType());
            values.put("MsgPos", messages.getMsgPos());
            values.put("MsgContent", messages.getMsgContent());
            values.put("LocalPath", messages.getLocalPath());
            values.put("HostPath", messages.getHostPath());
            values.put("DriverID", messages.getDriverID());
            values.put("OperatorID", messages.getOperatorID());
            values.put("MsgDate", messages.getMsgDate());
            values.put("MsgTime", messages.getMsgTime());
            values.put("isSend", messages.getIsSend());
            values.put("IsSeen",messages.getIsSeen());
            values.put("IsEdited",messages.getIsEdited());
            values.put("Image", messages.getImage());
            res = mydb.insert(DB_Creator.tbl_Message, null, values);

        } catch (Exception e) {
            Analytics.trackEvent("DoCommand_MessageDB" + "_" + "addNewMessages" + "_" + CommonClass.DeviceProperty + "_" + CommonClass.GetCurrentMDate() + "_" + e.getMessage());
        }
        mydb.close();
        return res;
    }

    public List<Messages> getListOfMessages(String whereStr) {

        List<Messages> Messages = new ArrayList<>();
        try {

            Cursor c = mydb.rawQuery("select * from " + /*dataBaseHelper*/DB_Creator.tbl_Message + " where " + whereStr, null);
            if (c.getCount() != 0)
                while (c.moveToNext()) {

                    Messages em = new Messages();
                    em.setMsgID(c.getString(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.MsgID)));
                    em.setMsgType(c.getInt(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.MsgType)));
                    em.setMsgPos(c.getInt(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.MsgPos)));
                    em.setMsgContent(c.getString(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.MsgContent)));
                    em.setLocalPath(c.getString(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.LocalPath)));
                    em.setHostPath(c.getString(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.HostPath)));
                    em.setDriverID(c.getString(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.DriverID)));
                    em.setOperatorID(c.getString(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.OperatorID)));
                    em.setMsgDate(c.getString(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.MsgDate)));
                    em.setMsgTime(c.getString(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.MsgTime)));
                    em.setIsSend(c.getInt(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.isSend)));
                    em.setIsSeen(c.getInt(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.IsSeen)));
                    em.setIsEdited(c.getInt(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.IsEdited)));
                    em.setImage(c.getBlob(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.Image)));
                    em.setID(c.getInt(c.getColumnIndex(DB_Creator.ID)));

                    Messages.add(em);
                }

            c.close();
            mydb.close();
        }catch (Exception e) {
            Analytics.trackEvent("DoCommand_MessageDB" + "_" + "getListOfMessages" + "_" + CommonClass.DeviceProperty + "_" + CommonClass.GetCurrentMDate() + "_" + e.getMessage());
        }
        return Messages;
    }

    public void editMessages(Messages messages) {
        try {

            ContentValues values = new ContentValues();
            values.put("MsgID", messages.getMsgID());
            values.put("MsgType", messages.getMsgType());
            values.put("MsgPos", messages.getMsgPos());
            values.put("MsgContent", messages.getMsgContent());
            values.put("LocalPath", messages.getLocalPath());
            values.put("HostPath", messages.getHostPath());
            values.put("DriverID", messages.getDriverID());
            values.put("OperatorID", messages.getOperatorID());
            values.put("MsgDate", messages.getMsgDate());
            values.put("MsgTime", messages.getMsgTime());
            values.put("isSend", messages.getIsSend());
            values.put("IsSeen", messages.getIsSeen());
            values.put("IsEdited", messages.getIsEdited());
            values.put("Image", messages.getImage());
            mydb.update(DB_Creator.tbl_Message, values, "MsgID = '" + messages.getMsgID()+"'", null);
            mydb.close();
        }catch (Exception e) {
            Analytics.trackEvent("DoCommand_MessageDB" + "_" + "editMessages" + "_" + CommonClass.DeviceProperty + "_" + CommonClass.GetCurrentMDate() + "_" + e.getMessage());
        }
    }

    public void deleteMessages(String OperatorID) {
        try {

            mydb.delete(DB_Creator.tbl_Message, /*""*/"OperatorID = '" + /*messages.getMsgID()*/OperatorID+"'", null);
            mydb.close();
        }catch (Exception e) {
            Analytics.trackEvent("DoCommand_MessageDB" + "_" + "deleteMessages" + "_" + CommonClass.DeviceProperty + "_" + CommonClass.GetCurrentMDate() + "_" + e.getMessage());
        }
    }
    public void deleteMessage(String MsgID) {
        try {

            mydb.delete(DB_Creator.tbl_Message, /*""*/"MsgID = '" + /*messages.getMsgID()*/MsgID+"'", null);
            mydb.close();
        }catch (Exception e) {
            Analytics.trackEvent("DoCommand_MessageDB" + "_" + "deleteMessage" + "_" + CommonClass.DeviceProperty + "_" + CommonClass.GetCurrentMDate() + "_" + e.getMessage());
        }
    }
    public void deleteAllMessages(/*Messages messages*/) {
        mydb.delete(/*dataBaseHelper*/DB_Creator.tbl_Message, /*""*/"", null);
        mydb.close();
    }

    public List<Messages> searchByName(String MsgContent) {

        List<Messages> messages = new ArrayList<>();
        try {
            Cursor c = mydb.rawQuery("select * from " + /*dataBaseHelper*/DB_Creator.tbl_Message + " where MsgContent like '%" + MsgContent + "%'", null);

            while (c.moveToNext()) {

                Messages em = new Messages();
                em.setMsgID(c.getString(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.MsgID)));
                em.setMsgType(c.getInt(c.getColumnIndex(String.valueOf(/*dataBaseHelper*/DB_Creator.MsgType))));
                em.setMsgPos(c.getInt(c.getColumnIndex(String.valueOf(/*dataBaseHelper*/DB_Creator.MsgPos))));
                em.setMsgContent(c.getString(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.MsgContent)));
                em.setLocalPath(c.getString(c.getColumnIndex(String.valueOf(/*dataBaseHelper*/DB_Creator.LocalPath))));
                em.setHostPath(c.getString(c.getColumnIndex(String.valueOf(/*dataBaseHelper*/DB_Creator.HostPath))));
                em.setDriverID(c.getString(c.getColumnIndex(String.valueOf(/*dataBaseHelper*/DB_Creator.DriverID))));
                em.setOperatorID(c.getString(c.getColumnIndex(String.valueOf(/*dataBaseHelper*/DB_Creator.OperatorID))));
                em.setMsgDate(c.getString(c.getColumnIndex(String.valueOf(/*dataBaseHelper*/DB_Creator.MsgDate))));
                em.setMsgTime(c.getString(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.MsgTime)));
                em.setIsSend(c.getInt(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.isSend)));
                em.setIsSeen(c.getInt(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.IsSeen)));
                em.setImage(c.getBlob(c.getColumnIndex(/*dataBaseHelper*/DB_Creator.Image)));
                messages.add(em);
            }
            c.close();
        } catch (Exception e) {
            Analytics.trackEvent("DoCommand_MessageDB" + "_" + "searchByName" + "_" + CommonClass.DeviceProperty + "_" + CommonClass.GetCurrentMDate() + "_" + e.getMessage());

        }
        mydb.close();
        return messages;
    }

}
