package com.tahlilgargroup.androidchatlibrary;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.appcenter.analytics.Analytics;
import com.tahlilgargroup.commonlibrary.CommonClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.tahlilgargroup.androidchatlibrary.ActivityChat.CloseConnection;
import static com.tahlilgargroup.androidchatlibrary.ActivityChat.sse;
import static com.tahlilgargroup.androidchatlibrary.ActivityChat.startConnection;
import static com.tahlilgargroup.commonlibrary.CommonClass.DeviceProperty;

public class ChatClass {
    public static String driverID;
    public static String NameFamily;

    public static Location mCurrentLocation;
    public static boolean IsReceiveFromOtherApp = false;

    public enum ReceiveFromOtherAppFileType {text, image, video, none}

    public static ReceiveFromOtherAppFileType otherAppFileType = ReceiveFromOtherAppFileType.none;
    public static Intent intent;
    public static JsonArray jsonArray;
    public static Context context;
    public static int VID;
    // public static  Class NotifyContext;

    public static Class ActivityChatList;
    public static Class ActivityNotifications;
    public static Class ChatMethodsClass;


    public static String API_Common_URL;

    public static String SignalRUrl;


    public static int appCode;

 /*   public static String GetMessagesRoute="";
    public static final String GetMessagesPass="";

    public static final String DownloadFileRoute="";
    public static final String DownloadFilePass="";

    public static final String uploadMulFilesRoute="";
    public static final String uploadMulFilesPass="";

    public  final String getGetMessagesRoute() {
        return GetMessagesRoute;
    }

    public static final void setGetMessagesRoute(String getMessagesRoute) {
        GetMessagesRoute = getMessagesRoute;
    }*/

    public static void SetConnection() {

      /*  AppCenter.start((Application)context, "f5e093b0-587a-43a2-8dd1-9f83e2286d26",
                Analytics.class, Crashes.class);*/

        sse = new ServerSentEventsTransport(new Logger() {
            @Override
            public void log(String message, LogLevel level) {
                Log.println(level.ordinal(), "signalr-sse", message);
            }
        });

        startConnection();
        CloseConnection();



        ActivityChat.connection.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(final JsonElement json) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void run() {

                        JsonObject jsonObject = json.getAsJsonObject();
                        if (jsonObject != null && jsonObject.has("A")) {
                            jsonArray = jsonObject.getAsJsonArray("A");
                            String method = jsonObject.get("M").getAsString();
                            //PushNotifications(method,jsonArray);
                            if (method.equals("addNewMessage")) {
                                if (jsonArray != null && jsonArray.size() != 0) {
                                    if (jsonArray.get(6).getAsString().equals(driverID)) {
                                        //از حالت راهنما نتونه یه دفعه با نوتیفیکیشن بره تو چت
                                        //if (TransportClass.showCaseView != null && !TransportClass.showCaseView.isShowing()) {
                                        Notificate();
                                        // }
                                    }
                                }
                            } else if (method.equals("ResultNotification")) {
                                if (jsonArray != null && jsonArray.size() != 0) {
                                    List<Integer> OperatorOkSent = new ArrayList<>();
                                    if (jsonArray.get(2) instanceof JsonArray) {
                                        JsonArray array = jsonArray.get(2).getAsJsonArray();
                                        for (int i = 0; i < array.size(); i++) {
                                            if (i == 0) OperatorOkSent.clear();
                                            JsonElement Operator = array.get(i);
                                            JsonObject receiverCode = Operator.getAsJsonObject();
                                            int s = receiverCode.get("Receiver").getAsInt();
                                            boolean ImReceiver = s == VID;
                                            if (ImReceiver && jsonArray.get(1).getAsBoolean()) {
                                                PublicNotificate(new CommonClass().ErrorMessages(jsonArray.get(0).getAsInt(), context), new CommonClass().ErrorMessages(jsonArray.get(0).getAsInt(), context));
                                            }
                                            int Sender = receiverCode.get("Sender").getAsInt();
                                            boolean ImSender = Sender == VID;
                                            if (ImSender) {
                                                if (jsonArray.get(1).getAsBoolean()) {
                                                    OperatorOkSent.add(receiverCode.get("Receiver").getAsInt());


                                                } else {
                                                    new CommonClass().ShowToast(context, new CommonClass().ErrorMessages(11, context), Toast.LENGTH_SHORT);

                                                }
                                            }
                                        }


                                    }
                                    //اگر حداقل برای یک اپراتور ارسال شد پیغام موفقیت نمایش بده
                                    if (OperatorOkSent.size() > 0) {
                                        new CommonClass().ShowToast(context, new CommonClass().ErrorMessages(200, context), Toast.LENGTH_SHORT);

                                    }

                                }

                            }

                        }
                    }
                });
            }
        });
    }

    public static void Notificate() {
        try {

            boolean showNotifiation = false;
            if (ActivityChat.active) {
                if (ActivityChat.id != null && jsonArray != null && !ActivityChat.id.equals(jsonArray.get(2).getAsString())) {
                    showNotifiation = true;
                }
            } else
                showNotifiation = true;
            if (/*(id!=null && jsonArray!=null && !id.equals(jsonArray.get(2).getAsString())) || !active*/showNotifiation) { /*} else (!active ) {*/
                final String NOTIFICATION_CHANNEL_ID = "10001";
                //ActivityChat.id = jsonArray.get(2).getAsString();

               // String notification_title = /*jsonArray != null && jsonArray.size() != 0 ? jsonArray.get(7).getAsString() :*/ context.getResources().getString(R.string.label);
                String notification_message = jsonArray != null && jsonArray.size() != 0 ? jsonArray.get(4).getAsString() : "پیام جدید";

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.tahlil)
                               // .setContentTitle(notification_title)
                                .setContentText(notification_message)
                                .setAutoCancel(true)
                                .setVibrate(new long[]{100, 200, 300, 400/*, 500, 400, 300, 200, 400*/})
                                .setSound(alarmSound);
                                              /*  .setSound(Uri.parse("android.resource://"
                                                        + myContext.getApplication().getPackageName() + "/" + R.raw.sms)*//* alarmSound*//*);*/


                Intent resultIntent = new Intent(context.getApplicationContext(), ActivityChatList);
                resultIntent.putExtra("menuFragment", "favoritesMenuItem");
                // resultIntent.putExtra("user_id", from_user_id);

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                context.getApplicationContext(),
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT
                        );

                mBuilder.setContentIntent(resultPendingIntent);
                int mNotificationId = jsonArray != null ? Integer.parseInt(jsonArray.get(5).getAsString()) : 1000; //(int) System.currentTimeMillis();
                NotificationManager mNotifyMgr =
                        (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

                    mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                    if (mNotifyMgr != null) {
                        mNotifyMgr.createNotificationChannel(notificationChannel);
                    }
                }
                if (mNotifyMgr != null) {
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());
                }


                Method m= null;
                try {
                    m = ChatMethodsClass.getMethod("generateContactList", Context.class);
                    m.invoke(ChatMethodsClass.newInstance(),context);

                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
               /* ActivityChatList chatlistfragment = new ActivityChatList();
                chatlistfragment.generateData(ActivityMain.context);*/

           /* if (jsonArray != null) {
                for (int i = 0; i < FragmentChatList.models.size(); i++) {
                    if (jsonArray.get(2).getAsString().equals(FragmentChatList.models.get(i).getOperatorID())) {
                        FragmentChatList.counts.set(i*//*id - 1*//*, FragmentChatList.counts.get(i) + 1);
                        break;
                    }
                }
            }*/
                //ezafe shodan new message ba amadan payam
                /*if (curvedBottomNavigationView.getSelectedItemId() != R.id.navigation_chat)
                    BottomMenuHelper.showBadgeMenu(context, curvedBottomNavigationView, R.id.navigation_chat, "");*/
                try {
                    m = ChatMethodsClass.getMethod("ChatBadage");
                    m.invoke(ChatMethodsClass.newInstance());

                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            Analytics.trackEvent("MainAC_Notificate " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.toString());
        }
    }

    /**
     * for server operation notifications
     *
     * @param Title
     * @param Content
     */
    public static void PublicNotificate(String Title, String Content) {
        try {

            final String NOTIFICATION_CHANNEL_ID = "10001";
            //ActivityChat.id = jsonArray.get(2).getAsString();

            String notification_title = Title;
            String notification_message = Content;

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.tahlil)
                            .setContentTitle(notification_title)
                            .setContentText(notification_message)
                            .setAutoCancel(true)
                            .setVibrate(new long[]{100, 200, 300, 400/*, 500, 400, 300, 200, 400*/})
                            .setSound(alarmSound);


            Intent resultIntent = new Intent(context.getApplicationContext(), ActivityNotifications);
            resultIntent.putExtra("Noti", "newNoti");
            // resultIntent.putExtra("user_id", from_user_id);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context.getApplicationContext(),
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT
                    );

            mBuilder.setContentIntent(resultPendingIntent);
            int mNotificationId = 123456789; //(int) System.currentTimeMillis();
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                if (mNotifyMgr != null) {
                    mNotifyMgr.createNotificationChannel(notificationChannel);
                }
            }
            if (mNotifyMgr != null) {
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }
        } catch (Exception e) {
            Analytics.trackEvent("MainAC_PublicNotificate " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());
        }

    }

    public static void EditOrderSignal(int OpCode,List<NotyParams> ResultNotifyParam)
    {
        if (ActivityChat.connection.getState() == ConnectionState.Connected &&
                ActivityChat.connection.getState() != ConnectionState.Reconnecting &&
                ActivityChat.connection.getState() != ConnectionState.Connecting) {

            try {

                ActivityChat.hub.invoke("SendNotification",OpCode,ResultNotifyParam);


            } catch (Exception ex) {
                Analytics.trackEvent("TransportClass_" + "EditOrderAPI1 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + ex.getMessage());

            }
        } else {
            new CommonClass().ShowToast(context, CommonClass.ToastMessages.Is_Disconnect, "");
        }
    }

}
