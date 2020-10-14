package com.tahlilgargroup.androidchatlibrary;

import android.content.Context;
import android.widget.Toast;

import com.tahlilgargroup.commonlibrary.CommonClass;


public class CallDBMethods {

    public enum CommandType {Insert, Update, Delete}

    public static boolean MessageCommands(int ID, String MsgID, String DriverID, String HostPath,
                                          int messageType, String LcalPath, String MsgContent,
                                          String Date, String Time, int MsgPosition, String OperatorID,
                                          int IsSend, int ISSeen, Context context, CommandType commandType,int IsEdited) {
        boolean result = false;
        try {
            Messages messages = new Messages();
            if (ID != -1)
                messages.setID(ID);
            messages.setMsgID(MsgID);
            messages.setDriverID(DriverID);
            messages.setHostPath(HostPath);
            if (messageType != 0) {
                if (messageType == 1)
                    messages.setImage(null);
                messages.setLocalPath(LcalPath);
            } else {
                messages.setLocalPath("");
            }
            messages.setMsgContent(MsgContent);
            messages.setMsgDate(Date);
            messages.setMsgPos(MsgPosition);
            messages.setMsgTime(Time);
            messages.setMsgType(messageType);
            messages.setOperatorID(OperatorID);
            messages.setIsSend(IsSend);
            messages.setIsEdited(IsEdited);
            if (ISSeen != -1)
                messages.setIsSeen(ISSeen);
            switch (commandType) {
                case Insert: {
                    result = new DoCommand_MessageDB(context).addNewMessages(messages) != -1;
                    break;
                }
                case Update: {
                    new DoCommand_MessageDB(context).editMessages(messages);
                    result = true;
                    break;
                }
                case Delete: {
                    new DoCommand_MessageDB(context).deleteMessages(OperatorID);
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            new CommonClass().ShowToast(context, e.getMessage(), Toast.LENGTH_SHORT);
        }


        return result;

    }
}
