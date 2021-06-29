package com.tahlilgargroup.androidchatlibrary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.microsoft.appcenter.analytics.Analytics;
import com.tahlilgargroup.commonlibrary.CommonClass;

import java.util.ArrayList;
import java.util.List;

import microsoft.aspnet.signalr.client.ConnectionState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tahlilgargroup.androidchatlibrary.ActivityChat.hub;
import static com.tahlilgargroup.androidchatlibrary.ChatClass.NameFamily;
import static com.tahlilgargroup.androidchatlibrary.ChatClass.context;
import static com.tahlilgargroup.androidchatlibrary.ChatClass.driverID;
import static com.tahlilgargroup.commonlibrary.CommonClass.DeviceProperty;


public class MessageCmdAdapter extends RecyclerView.Adapter<TellListViewHolder> {

    //آداپتور لیست عمیاتی که میشود روی پیام های چت انجام شد
    private List<CmdMessageItem> CmdList;

    //for Ok Edit Message Method in EditMessage view
    public static int EditMsgPsition;
    public static ChatAppMsgDTO chatAppMsgDTO;

    public MessageCmdAdapter(List<CmdMessageItem> cmdList) {
        CmdList = cmdList;
    }

    @NonNull
    @Override
    public TellListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_tell, parent, false);
        return new TellListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TellListViewHolder tellListViewHolder, final int position) {
        final CmdMessageItem messageItem = this.CmdList.get(position);

        //تنظیم نام عملیات
        tellListViewHolder.Telltxt.setText(messageItem.getCommandName());

        //انجام عملیان با کلیک روی آیتم
        tellListViewHolder.TellList_Crd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //close Command list window
                ShowFragmentMessageCommands messageCommands = new ShowFragmentMessageCommands();
                messageCommands.onPause();

                if (ShowFragmentMessageCommands.typeToCommand != ShowFragmentMessageCommands.MsgTypeToCommand.ReceiveMsg && position == 0)//روی حذف زده
                {
                    DeleteMessage(messageItem.getCmdMessageData().getMessagePosition(),
                            messageItem.getCmdMessageData().getMessageType(),
                            messageItem.getCmdMessageData().getMessageLayout());
                } else if (ShowFragmentMessageCommands.typeToCommand == ShowFragmentMessageCommands.MsgTypeToCommand.SentTextMsg && position == 1)//روی ویرایش زده
                {
                    if (!ActivityChat.IsRecordingVoice) {
                        //for done command in chat activity on selected message(message data)
                        EditMsgPsition = messageItem.getCmdMessageData().getMessagePosition();
                        chatAppMsgDTO = messageItem.getCmdMessageData().getChatAppMsgDTO();


                        messageItem.getCmdMessageData().getMessageLayout().setBackgroundColor(Color.TRANSPARENT);

                        //display Message editor tool in chat activity and put selected message text in it
                        ActivityChat.audioRecordView.setVisibility(View.GONE);
                        ActivityChat.editMessageView.setVisibility(View.VISIBLE);
                        ActivityChat.editMessageView.getTxtMessage().setText(messageItem.getCmdMessageData().getChatAppMsgDTO().getMsgContent());
                    } else {
                        new CommonClass().ShowToast(ActivityChat.context, ActivityChat.context.getString(R.string.PleaseStopRecordingVoice), Toast.LENGTH_SHORT);
                    }
                } else if ((ShowFragmentMessageCommands.typeToCommand == ShowFragmentMessageCommands.MsgTypeToCommand.SentTextMsg && position == 2)
                        || (ShowFragmentMessageCommands.typeToCommand == ShowFragmentMessageCommands.MsgTypeToCommand.SentNoTextMsg && position == 1)
                        || (ShowFragmentMessageCommands.typeToCommand == ShowFragmentMessageCommands.MsgTypeToCommand.ReceiveMsg && position == 0)) //share
                {

                    switch (messageItem.getCmdMessageData().getMessageType()) {
                        case "متنی": {
                            new CommonClass().Share(ActivityChat.context, messageItem.getCmdMessageData().getChatAppMsgDTO().getMsgContent(), CommonClass.TypeToShare.text);
                            break;
                        }
                        case "تصویری": {
                            new CommonClass().Share(ActivityChat.context, messageItem.getCmdMessageData().getChatAppMsgDTO().getMsgContent(), CommonClass.TypeToShare.Image);
                            break;
                        }
                        case "ویدئویی": {
                            new CommonClass().Share(ActivityChat.context, messageItem.getCmdMessageData().getChatAppMsgDTO().getMsgContent(), CommonClass.TypeToShare.Video);
                            break;
                        }
                        case "صوتی": {
                            new CommonClass().Share(ActivityChat.context, messageItem.getCmdMessageData().getChatAppMsgDTO().getMsgContent(), CommonClass.TypeToShare.Voice);
                            break;
                        }
                    }


              /*  CommonClass.TypeToShare typeToShare=null;

                if( messageItem.getCmdMessageData().getMessageType().equals("متنی"))
                ComObj.Share(messageItem.getCmdMessageData().getChatAppMsgDTO().getMsgContent(),);*/



               /* Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                ActivityChat.context.startActivity(Intent.createChooser(sharingIntent, "Share via"));*/

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (CmdList == null) {
            CmdList = new ArrayList<CmdMessageItem>();
        }
        return CmdList.size();
    }

    /**
     * delete message from chat
     *
     * @param position
     * @param messageTypeName
     * @param layout
     */
    public static void DeleteMessage(final int position, String messageTypeName, final View layout) {
        layout.setBackgroundColor(Color.rgb(141, 236, 250));

        View checkBoxView = View.inflate(ActivityChat.context, R.layout.checkbox, null);
        final CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        //اگر پرنتش حذف نشود نمیشود به آلرت دیالوگ نسبتش داد
        if (checkBox.getParent() != null) {
            ((ViewGroup) checkBox.getParent()).removeView(checkBox); // <- fix
        }
        //  layout.addView(tv);

        AlertDialog.Builder alertDialog;
        alertDialog = new AlertDialog.Builder(ActivityChat.context)
                .setView(checkBox)
                .setTitle(R.string.DoYouSureToDeleteMessage /*+ messageTypeName + " هستید؟ "*/)
                //  .setMessage("این پیام برای اپراتور نیز حذف خواهد شد!")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setNegativeButton(ChatClass.context.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        layout.setBackgroundColor(Color.TRANSPARENT);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

        // A null listener allows the button to dismiss the dialog and take no further action.
        alertDialog.setPositiveButton(ChatClass.context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                layout.setBackgroundColor(Color.TRANSPARENT);
                List<Messages> messages = new DoCommand_MessageDB(ActivityChat.context).getListOfMessages("MsgPos = " + position + " and DriverID = '" + driverID + "' and OperatorID = '" + ActivityChat.id + "'");
                if (messages.size() != 0) {

                    if (messages.get(0).getIsSeen() == 1)
                        new CommonClass().ShowToast(ActivityChat.context, ActivityChat.context.getString(R.string.DontAllowDelete), Toast.LENGTH_SHORT);
                    else {
                        String msgID = messages.get(0).getMsgID();
                        if (ActivityChat.connection.getState() == ConnectionState.Connected &&
                                ActivityChat.connection.getState() != ConnectionState.Reconnecting &&
                                ActivityChat.connection.getState() != ConnectionState.Connecting) {

                            try {
                                boolean isCheck = checkBox.isChecked();
                          /*  hub.invoke(SERVER_METHOD_SEND,(byte)0,0,Short.parseShort(id),driverID,
                                    message,true,msgType,CommonClass.DeviceIMEI != null ? CommonClass.DeviceIMEI : "",
                                    CommonClass.DeviceName != null ? CommonClass.DeviceName : "",
                                    "","", ActivityMain.mCurrentLocation != null ? ActivityMain.mCurrentLocation.getLatitude() : 0,
                                    ActivityMain.mCurrentLocation != null ? ActivityMain.mCurrentLocation.getLongitude() : 0);*/


                                //////////////////////////////////////////////////////////////////////////////////////////////

//                                hub.invoke("Send", messages.get(0).getOperatorID(), messages.get(0).getDriverID(), "", true, 0,
//                                        CommonClass.DeviceIMEI != null ? CommonClass.DeviceIMEI : "",
//                                        CommonClass.mCurrentLocation != null ? CommonClass.mCurrentLocation.getLatitude() : 0,
//                                        CommonClass.mCurrentLocation != null ? CommonClass.mCurrentLocation.getLongitude() : 0,
//                                        CommonClass.DeviceName != null ? CommonClass.DeviceName : "", "", "", BigInteger.valueOf(Long.parseLong(msgID)), isCheck, 2, NameFamily);

                                //////////////////////////////////////////////////////////////////////////////////////////////

                                ChatIUDModel chatIUDModel = new ChatIUDModel();
                                chatIUDModel.setID(Integer.valueOf(msgID));
                                chatIUDModel.setOpMode(2);
                                chatIUDModel.setKCode(Integer.valueOf(messages.get(0).getOperatorID()));
                                chatIUDModel.setPCode(messages.get(0).getDriverID());
                                chatIUDModel.setMessage("");
                                chatIUDModel.setSenderType(true);
                                chatIUDModel.setMessageType(0);
                                chatIUDModel.setIMEI(CommonClass.DeviceIMEI != null ? CommonClass.DeviceIMEI : "");
                                chatIUDModel.setMachineName(CommonClass.DeviceName != null ? CommonClass.DeviceName : "");
                                chatIUDModel.setIpAddress("");
                                chatIUDModel.setBrowserType("");
                                chatIUDModel.setLat(CommonClass.mCurrentLocation != null ? CommonClass.mCurrentLocation.getLatitude() : 0);
                                chatIUDModel.setLng(CommonClass.mCurrentLocation != null ? CommonClass.mCurrentLocation.getLongitude() : 0);
                                chatIUDModel.setRemoveBoth(isCheck);

                                ChatIUD(chatIUDModel);


                            } catch (Exception ignored) {

                            }
                        } else {
                            new CommonClass().ShowToast(ActivityChat.context, CommonClass.ToastMessages.Is_Disconnect, "");
                        }
                    }


                }
            }
        });
        //کنسل کردن آلرت دیالوگ با کلیلک خارج از پنجره اش
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                layout.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        alertDialog.show();
    }

    public static void ChatIUD(final ChatIUDModel chatIUDModel) {
        try {

            new CommonClass().ShowWaitingDialog(ChatClass.context, context.getString(R.string.Downloading));
            APIService service =
                    ServiceGenerator.GetCommonClient().create(APIService.class);
            Call<List<String>> call2 = service.ChatIUD(chatIUDModel, null);
            call2.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(@Nullable Call<List<String>> call, @Nullable Response<List<String>> response) {

                    if (response != null && response.isSuccessful()) {
                        if (response.body() != null) {

                            notifSignallR(response.body().get(1), chatIUDModel.getOpMode(), Integer.parseInt(response.body().get(0)), chatIUDModel.getMessage(), chatIUDModel.getMessageType(), chatIUDModel.getKCode());

                        } else {

                            new CommonClass().CancelWaitingDialog();

                            int errMsg = 0;
                            if (response != null) {
                                errMsg = response.raw().code();
                            }
                            if (errMsg != 0) {
                                new CommonClass().ShowToast(ChatClass.context, new CommonClass().ErrorMessages(errMsg, ChatClass.context), Toast.LENGTH_SHORT);
                            } else {
                                if (response != null) {
                                    new CommonClass().ShowToast(ChatClass.context, response.raw().message(), Toast.LENGTH_SHORT);
                                }
                            }

                            Analytics.trackEvent("ChatIUD_" + "ChatIUDAPI " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + errMsg);

                        }
                    } else {

                        new CommonClass().CancelWaitingDialog();

                        int errMsg = 0;
                        if (response != null) {
                            errMsg = response.raw().code();
                        }
                        if (errMsg != 0) {
                            new CommonClass().ShowToast(ChatClass.context, new CommonClass().ErrorMessages(errMsg, ChatClass.context), Toast.LENGTH_SHORT);
                        } else {
                            if (response != null) {
                                new CommonClass().ShowToast(ChatClass.context, response.raw().message(), Toast.LENGTH_SHORT);
                            }
                        }

                        Analytics.trackEvent("ChatIUD_" + "ChatIUDAPI " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + errMsg);

                    }
                    new CommonClass().CancelWaitingDialog();

                }


                @Override
                public void onFailure(@Nullable Call<List<String>> call, @Nullable Throwable t) {
                    new CommonClass().CancelWaitingDialog();

                    if (t != null) {
                        if (ChatClass.context != null)
                            new CommonClass().ShowToast(ChatClass.context, CommonClass.ToastMessages.Network_Problem, t.getMessage());
                    }

                }
            });
        } catch (Exception ex) {
            new CommonClass().CancelWaitingDialog();
            if (ChatClass.context != null) {
                String d = ex.getMessage();
                new CommonClass().ShowToast(ChatClass.context, new CommonClass().ErrorMessages(11, ChatClass.context) + d, Toast.LENGTH_SHORT);

            }
        }


    }

    public static void notifSignallR(String nameFile, int OpMode, int ID, String message, int msgType, int id) {

        String sendMessage = "";
        if (OpMode == 0 && msgType != 0) {
            sendMessage = nameFile;
        } else {
            sendMessage = message;
        }
        try {

            hub.invoke(ActivityChat.EXTRA_CHAT_NOTIFICATION, OpMode, ID, sendMessage, driverID, NameFamily, id, msgType);

        } catch (Exception e) {

            Analytics.trackEvent("ChatAC_" + "sendMessage " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

        }


    }


}

