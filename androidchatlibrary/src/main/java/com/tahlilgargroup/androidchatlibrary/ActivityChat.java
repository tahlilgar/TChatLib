package com.tahlilgargroup.androidchatlibrary;

import android.Manifest;
import android.app.FragmentManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.appcenter.analytics.Analytics;
import com.tahlilgargroup.commonlibrary.CommonClass;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tahlilgargroup.androidchatlibrary.ChatClass.ActivityChatList;
import static com.tahlilgargroup.androidchatlibrary.ChatClass.NameFamily;
import static com.tahlilgargroup.androidchatlibrary.ChatClass.SignalRUrl;
import static com.tahlilgargroup.androidchatlibrary.ChatClass.appCode;
import static com.tahlilgargroup.androidchatlibrary.ChatClass.driverID;
import static com.tahlilgargroup.commonlibrary.CommonClass.DeviceProperty;


public class ActivityChat extends AppCompatActivity implements AudioRecordView.RecordingListener {

    public static HubConnection connection = new HubConnection(SignalRUrl/*"http://185.153.211.103:9003/"http://distribution.tahlilgargroup.com/""http://transport.tahlilgargroup.ir/""http://template.tahlilgargroup.ir/api/"*//*"http://webservices.tahlilgargroup.ir/"*//*"http://distribution.tahlilgargroup.com/""http://testwebserv.tahlilgargroup.ir/"*/);
    public static HubProxy hub = connection.createHubProxy("NotificationHub");
    public static String id;
    //ابزار ضبط صدا در چت
    public static AudioRecordView audioRecordView;
    public static EditMessageView editMessageView;

    public static Context context;
    //for message command dialog fragment
    public static FragmentManager fm;
    static List<ChatAppMsgDTO> msgDtoList;
    public static ChatAppMsgDTO msgDto;
    static ChatAppMsgAdapter chatAppMsgAdapter;


    //control that can't edit message when is in recording voice
    public static boolean IsRecordingVoice = false;


    //برای بررسی فعال بودن صفحه چت
    public static boolean active = false;

    //for upload video
    boolean isbig = false;
    public static String msgContent, outputFile;
    JsonArray jsonArray;

    //for reconnect signalr
    public static ServerSentEventsTransport sse;


    //نشان دادن وضعیت اتصال
    public static TextView txtStatus;
    RecyclerView msgRecyclerView;

    //timer for controll connection status text
    static Handler handler;
    static Runnable handlerTask;


    private static final int SELECT_PHOTO = 200;
    ImageView record, msgSendButton;
    MediaRecorder myRecorder;
    byte[] fileByte;
    Uri fileUri;

    //ضبط صدا
    MediaPlayer mediaPlayer;

    //نوع پیام از نظر متنی تصویری صوتی
    static int messageType;
    static String LocalPath;

    //نام اپراتور
    TextView txtOperatorName;

    //سوییپ برای دریافت پیام های قدیمی
    SwipeRefreshLayout mySwipe;


    //تایم ضبط صدا
    private long time;
    private int Wc, Hc;
    String OperatorName;

    //لودینگ تا نمایش پیام
    AnimationDrawable animation;
    ImageView loading;

    //for get data from server
    //اگر مقدار صحیح داشته باشد پیام های قدیمی را واکشی میکند در غیر اینصورت جدید
    static boolean isSwiping = false;

    static int newMsgPosition;
    boolean isNew = false;

    List<Server_Message> UnreadReceivedMessages = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(com.tahlilgargroup.androidchatlibrary.R.layout.activity_chat);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            context = this;
            fm = getFragmentManager();

            new CommonClass().DetectGPSTurn(ActivityChat.this);

            //////////////////////////////////////set operator id
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if (extras.containsKey("position")) {
                    if (extras.getString("position") != null && Objects.requireNonNull(extras.getString("position")).contains("-")) {
                        String[] operator = Objects.requireNonNull(extras.getString("position")).split("-");
                        id = operator[0];
                        OperatorName = operator[1];
                    }
                    if (id == null)
                        id = extras.getString("position");

                }

            }

            init();


            // پاک کردن نوتیکیشن پیام های این اپراتور
            NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nMgr != null) {
                nMgr.cancel(Integer.parseInt(id));//.cancelAll();
            }

            //TODO Set driverID
            //تنظیم کد راننده
            //ChatClass.driverID = new DoCommand_UserDB(ActivityChat.this).getListOfUsers().getCode();


            //مجوز دسترسی به میکروفون
            if (new CommonClass().CheckForPermission(ActivityChat.this, Manifest.permission.RECORD_AUDIO))
                audioRecordView.setRecordingListener(this);
            else
                new CommonClass().askForPermission(ActivityChat.this, Manifest.permission.RECORD_AUDIO, CommonClass.AUDIO);


            //انتخاب فایل
            audioRecordView.getAttachmentView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (new CommonClass().CheckForPermission(ActivityChat.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        ///////////select image
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/video/*");
                        ////////////onActivityResult
                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    } else {
                        new CommonClass().askForPermission(ActivityChat.this, Manifest.permission.READ_EXTERNAL_STORAGE, CommonClass.READ_EXST);
                        // audioRecordView.getAttachmentView().callOnClick();
                    }

                }
            });

            //اگر پیام خالی نیس و به اینترنت متصلیم پیام ارسال کن
            audioRecordView.getSendView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msgContent = audioRecordView.getMessageView().getText().toString();//msgInputText.getText().toString();
                    if (!TextUtils.isEmpty(msgContent.trim()) && msgContent.trim().length() != 0) {
                        if (connection.getState() == ConnectionState.Connected && connection.getState() != ConnectionState.Reconnecting && connection.getState() != ConnectionState.Connecting) {
                            if (new CommonClass().GpsIsActive(ActivityChat.this)) {
                                sendMessage(msgContent.trim(), 0);
                                ActivityChat.this.sendingState(0);
                                audioRecordView.getMessageView().setText("");

                            } else {
                                new CommonClass().ActiveGPSMessage(ActivityChat.this);
                            }

                        } else
                            new CommonClass().ShowToast(ActivityChat.this.getApplicationContext(), CommonClass.ToastMessages.Is_Disconnect, "");


                    } else {
                        new CommonClass().ShowToast(ActivityChat.this.getApplicationContext(), "پیام حاوی متن نیست!", Toast.LENGTH_SHORT);
                    }

                }
            });

            //پاک کردن کش ریسایکلر ویو
            msgRecyclerView.getRecycledViewPool().clear();
            chatAppMsgAdapter.notifyDataSetChanged();

            //گرفتن پیام ها از دیتابیس
            GetMessagesFromDB();


            //اگر به اینترنت متصل هستیم پیام هارا از سرور بگیر
            if (connection.getState() == ConnectionState.Connected && connection.getState() != ConnectionState.Reconnecting && connection.getState() != ConnectionState.Connecting && new CommonClass().isConnectingToInternet(ActivityChat.this)) {

                isSwiping = false;
                getServerMessagesSwip();
                // getServerMessages();

            } else {
                if (loading != null && animation != null) {
                    loading.setVisibility(View.GONE);
                    animation.stop();
                }
                new CommonClass().ShowToast(getApplicationContext(), CommonClass.ToastMessages.Is_Disconnect, "");

            }

            //بروزرسانی وضعیت اتصال
            StartTimer();

            //Todo uncomment
            connection.error(new ErrorCallback() {

                @Override
                public void onError(final Throwable error) {
                    runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    });

                }
            });

            hub.subscribe(new Object() {
                @SuppressWarnings("unused")
                public void messageReceived(final String name, final String message) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            // Toast.makeText(getApplicationContext(), name + ": " + message, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });


            //پیام هایی ک در سیگنال آر رد و بدل میشوند
            MessageRecive();


            mySwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //  getServerMessages();
                    if (audioRecordView.getVisibility() == View.VISIBLE) {
                        isSwiping = true;
                        ActivityChat.this.getServerMessagesSwip();
                    } else {
                        mySwipe.setRefreshing(false);
                        new CommonClass().ShowToast(ActivityChat.this, getResources().getString(R.string.FinishEditMessageFirst), Toast.LENGTH_SHORT);
                    }
                    //اعمالی را که میخواهیم هنگام تازه سازی انجام شود

                }
            });

            //ویرایش پیام
            editMessageView.getBtnOkEdit().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ActivityChat.audioRecordView.setVisibility(View.VISIBLE);
                    ActivityChat.editMessageView.setVisibility(View.GONE);
                    //اگر پیام ویرایش شده کاملا خالی نیست تغیرات را اعمال کن
                    if (editMessageView.getTxtMessage().getText().toString().length() != 0) {
                        List<Messages> messagelist = new DoCommand_MessageDB(context).getListOfMessages("MsgID = '" + MessageCmdAdapter.chatAppMsgDTO.getID() + "' AND isSend = 1");
                        if (messagelist.size() != 0) {
                            String msgID = messagelist.get(0).getMsgID();
                            if (ActivityChat.connection.getState() == ConnectionState.Connected &&
                                    ActivityChat.connection.getState() != ConnectionState.Reconnecting &&
                                    ActivityChat.connection.getState() != ConnectionState.Connecting) {

                                try {
                                    hub.invoke("Send", messagelist.get(0).getOperatorID(), messagelist.get(0).getDriverID(), editMessageView.getTxtMessage().getText().toString(), true, 0,
                                            CommonClass.DeviceIMEI != null ? CommonClass.DeviceIMEI : "",
                                            ChatClass.mCurrentLocation != null ? ChatClass.mCurrentLocation.getLatitude() : 0,
                                            ChatClass.mCurrentLocation != null ? ChatClass.mCurrentLocation.getLongitude() : 0,
                                            CommonClass.DeviceName != null ? CommonClass.DeviceName : "", "", "", BigInteger.valueOf(Long.parseLong(msgID)), true, 1,NameFamily);

                                    //ActivityChat.hub.invoke("messageUpdateDelete", 0, BigInteger.valueOf(Long.parseLong(msgID)), editMessageView.getTxtMessage().getText().toString(), true, 1);//byte OpMode 0 update 1 delete,int64 id,MsgText,bool RemoveBoth,byte SenderType(if bool false senderType is 1)

                                } catch (Exception e) {
                                    Analytics.trackEvent("ChatAC_" + "onCreate " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

                                }
                            } else {
                                new CommonClass().ShowToast(ActivityChat.context, CommonClass.ToastMessages.Is_Disconnect, "");
                            }


                        }
                    }
                }
            });

            //لغو ویرایش پیام
            editMessageView.getBtnCancelEdit().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editMessageView.getTxtMessage().setText("");
                    audioRecordView.setVisibility(View.VISIBLE);
                    editMessageView.setVisibility(View.GONE);
                }
            });

            //دریافت فایل برای اشتراک گذاری از دیگر برنامه ها
            if (ChatClass.IsReceiveFromOtherApp) {
                switch (ChatClass.otherAppFileType) {
                    case text: {
                        handleSendText(ChatClass.intent);
                        break;
                    }
                    case image: {
                        SendImage(ChatClass.intent);
                        break;
                    }
                    case video: {
                        SendVideo(ChatClass.intent);
                        break;
                    }
                }
            }

           /* TransportClass.setShowCaseView_(true, TransportClass.HelpPart.chat,
                    R.id.speakmsgImg, null, "تبدیل صوت به پیام متنی",
                    "پیامتان را بگویید تا اپلیکیشن تایپ کند!", ActivityChat.this);*/
        } catch (Exception e) {

            Analytics.trackEvent("ActivityChat" + "_" + "onCreate" + "_" + DeviceProperty + "_" + CommonClass.GetCurrentMDate() + "_" + e.getMessage());

        }


    }

    public void init() {

        try {
            audioRecordView = findViewById(R.id.recordingView);

            editMessageView = findViewById(R.id.EditTool);
            editMessageView.setVisibility(View.GONE);


            mySwipe = (SwipeRefreshLayout) findViewById(R.id.mySwipe);

            //msgInputText = null;//(EditText) findViewById(R.id.chat_input_msg);
            msgSendButton = null;// findViewById(R.id.chat_send_msg);
            // txtStatus.setText("dfgdg");
            txtStatus = findViewById(R.id.txtState);
            msgRecyclerView = findViewById(R.id.chat_recycler_view);

            record = null; //findViewById(R.id.chat_input_voice);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setStackFromEnd(true);
            msgRecyclerView.setLayoutManager(linearLayoutManager);

            msgDtoList = new ArrayList<ChatAppMsgDTO>();
            chatAppMsgAdapter = new ChatAppMsgAdapter(msgDtoList);
            msgRecyclerView.setAdapter(chatAppMsgAdapter);


            //بعد از اد کردن سیگنال آر
            Platform.loadPlatformComponent(new AndroidPlatformComponent());

            txtOperatorName = findViewById(R.id.txtOperatorName);
            txtOperatorName.setText(OperatorName);

            loading = findViewById(R.id.chatLoadingImg);
            animation = (AnimationDrawable) loading.getDrawable();
        } catch (Exception e) {

            Analytics.trackEvent("ActivityChat" + "_" + "init" + "_" + DeviceProperty + "_" + CommonClass.GetCurrentMDate() + "_" + e.getMessage());


        }

    }

    /////////////////////////add to recyclerview
    public void sendingState(int type) {

        //  msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, msgContent, " ", jsonArray.get(5).getAsString());

        try {

            if (type == 0)//text
            {
                messageType = 0;
                msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, msgContent, " ", "در حال ارسال...", null, false);

            } else if (type == 1)//image
            {
                messageType = 1;
                msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.IMG_TYPE_SENT, LocalPath /*fileByte*/, " ", "در حال ارسال...", null, false);


            } else if (type == 2)//film
            {
                messageType = 2;
                msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Video_TYPE_SENT, LocalPath/* fileUri*/, " ", "در حال ارسال...", null, false);

            } else if (type == 3)//voice
            {
                messageType = 3;
                msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Voice_TYPE_SENT, msgContent, " ", "در حال ارسال...", null, false);

            }
            if (type <= 3) {
                msgDtoList.add(msgDto);
                newMsgPosition = msgDtoList.size() - 1;
                //index = newMsgPosition;

                chatAppMsgAdapter.notifyItemInserted(newMsgPosition);
                msgRecyclerView.scrollToPosition(newMsgPosition);
            } else {
                new CommonClass().ShowToast(getApplicationContext(), getResources().getString(R.string.InvalidMsgType), Toast.LENGTH_LONG);
            }

        } catch (Exception e) {

            Analytics.trackEvent("ChatAC_" + "sendingState " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

        }

    }

    ////////////////////////////send message to signalr server
    public static void sendMessage(String message, int msgType) {


        String SERVER_METHOD_SEND = "Send";
        try {
            hub.invoke(SERVER_METHOD_SEND, Integer.parseInt(id), driverID, message, true, msgType,
                    CommonClass.DeviceIMEI != null ? CommonClass.DeviceIMEI : "",
                    ChatClass.mCurrentLocation != null ? ChatClass.mCurrentLocation.getLatitude() : 0,
                    ChatClass.mCurrentLocation != null ? ChatClass.mCurrentLocation.getLongitude() : 0,
                    CommonClass.DeviceName != null ? CommonClass.DeviceName : "", "", "", 0, false, 0,NameFamily);
            //Int16 KCode, string RCode, string Msgtext, bool SenderType, byte MsgType, string IMEI, float Lat, float Lng, string MachinName,
            // string ipAddress, string BrowserType, Int64 ID = 0, bool RemoveBoth = false, byte OpMode = 0

        } catch (Exception e) {

            Analytics.trackEvent("ChatAC_" + "sendMessage " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

        }

    }

    public void GetMessagesFromDB() {
        ////////////////////////////select dataBase

        try {
            loading.setVisibility(View.VISIBLE);
            animation.start();
            //پیام های این اپراتور را از دیتابیس بگیر
            List<Messages> messages = new DoCommand_MessageDB(context).getListOfMessages(/*"MsgID like '%%'"*/"OperatorID = '" + id + "'");

            if (messages.size() != 0) {

                for (int i = 0; i < messages.size(); i++) {
                    if (messages.get(i).getMsgType() == 0) {
                        if (messages.get(i).getIsSend() == 1)
                            msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, messages.get(i).getMsgContent(), " ", new CommonClass().PerisanNumber(messages.get(i).getMsgDate() + "\n" + messages.get(i).getMsgTime()), messages.get(i).getIsSeen() == 1, BigInteger.valueOf(Long.parseLong(messages.get(i).getMsgID())), messages.get(i).getIsEdited() == 1);
                        else
                            msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, messages.get(i).getMsgContent(), " ", new CommonClass().PerisanNumber(messages.get(i).getMsgDate() + "\n" + messages.get(i).getMsgTime()), BigInteger.valueOf(Long.parseLong(messages.get(i).getMsgID())), messages.get(i).getIsEdited() == 1);
                    } else if (messages.get(i).getMsgType() == 1) {
                        if (messages.get(i).getIsSend() == 1)
                            msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.IMG_TYPE_SENT, messages.get(i).getLocalPath()/*getImage()*/, " ", new CommonClass().PerisanNumber(messages.get(i).getMsgDate() + "\n" + messages.get(i).getMsgTime()), messages.get(i).getIsSeen() == 1, BigInteger.valueOf(Long.parseLong(messages.get(i).getMsgID())), messages.get(i).getIsEdited() == 1);
                        else
                            msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.IMG_TYPE_RECEIVED, messages.get(i).getLocalPath()/*getImage()*/, " ", new CommonClass().PerisanNumber(messages.get(i).getMsgDate() + "\n" + messages.get(i).getMsgTime()), BigInteger.valueOf(Long.parseLong(messages.get(i).getMsgID())), messages.get(i).getIsEdited() == 1);

                    } else if (messages.get(i).getMsgType() == 2) {
                        if (messages.get(i).getIsSend() == 1)
                            msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Video_TYPE_SENT, messages.get(i).getLocalPath(), " ", new CommonClass().PerisanNumber(messages.get(i).getMsgDate() + "\n" + messages.get(i).getMsgTime()), messages.get(i).getIsSeen() == 1, BigInteger.valueOf(Long.parseLong(messages.get(i).getMsgID())), messages.get(i).getIsEdited() == 1);
                        else
                            msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Video_TYPE_RECEIVED, messages.get(i).getLocalPath(), " ", new CommonClass().PerisanNumber(messages.get(i).getMsgDate() + "\n" + messages.get(i).getMsgTime()), BigInteger.valueOf(Long.parseLong(messages.get(i).getMsgID())), messages.get(i).getIsEdited() == 1);

                    } else if (messages.get(i).getMsgType() == 3) {
                        if (messages.get(i).getIsSend() == 1)
                            msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Voice_TYPE_SENT, messages.get(i).getLocalPath(), " ", new CommonClass().PerisanNumber(messages.get(i).getMsgDate() + "\n" + messages.get(i).getMsgTime()), messages.get(i).getIsSeen() == 1, BigInteger.valueOf(Long.parseLong(messages.get(i).getMsgID())), messages.get(i).getIsEdited() == 1);
                        else
                            msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Voice_TYPE_RECEIVED, messages.get(i).getLocalPath(), " ", new CommonClass().PerisanNumber(messages.get(i).getMsgDate() + "\n" + messages.get(i).getMsgTime()), BigInteger.valueOf(Long.parseLong(messages.get(i).getMsgID())), messages.get(i).getIsEdited() == 1);
                    }
                    if (messages.get(i).getMsgPos() == i) {
                        msgDtoList.add(messages.get(i).getMsgPos(), msgDto);
                        isNew = false;
                        chatAppMsgAdapter.notifyItemInserted(messages.get(i).getMsgPos());
                        msgRecyclerView.scrollToPosition(messages.get(i).getMsgPos());
                    }

                }

                loading.setVisibility(View.GONE);
                animation.stop();
                isNew = false;
            }

        } catch (Exception e) {
            Analytics.trackEvent("ChatAC_" + "GetMessagesFromDB " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());
        }
    }

    private void getServerMessagesSwip() {

        if (loading != null && animation != null && !isSwiping) {
            loading.setVisibility(View.VISIBLE);
            animation.start();
        }


        try {
            APIService service = ServiceGenerator.GetCommonClient().create(APIService.class);
            int pos = Objects.requireNonNull(msgRecyclerView.getAdapter()).getItemCount();
            DistributionChatParam distributionChatParam = new DistributionChatParam(driverID, (short) Integer.parseInt(id), Objects.requireNonNull(msgRecyclerView.getAdapter()).getItemCount(), false, isSwiping);
            Call<ArrayList<Server_Message>> call = service.GetMessages(distributionChatParam);
            call.enqueue(new Callback<ArrayList<Server_Message>>() {
                @Override
                public void onResponse(@Nullable Call<ArrayList<Server_Message>> call, @Nullable Response<ArrayList<Server_Message>> response) {
                    if (response != null) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                boolean a = Objects.requireNonNull(msgRecyclerView.getAdapter()).getItemCount() == 0;
                                boolean b = Objects.requireNonNull(msgRecyclerView.getAdapter()).getItemCount() > Objects.requireNonNull(response.body()).size();
                                boolean c = msgRecyclerView.getAdapter().getItemCount() < response.body().size();
                                //اگر هیچ پیامی نداریم دیتابیس و لیست را از نو از سرور بگیر
                                if (Objects.requireNonNull(msgRecyclerView.getAdapter()).getItemCount() == 0) {
                                    new DoCommand_MessageDB(ActivityChat.this).deleteMessages(id);
                                    int newMsgPosition = 0;
                                    if (response.body().size() != 0)
                                        for (int i = 0; i < response.body().size(); i++) {
                                            boolean isSend = response.body().get(i).isSenderType();//.getSender().equals(driverID);
                                            if (response.body().get(i).getMsgType() == 0) {
                                                if (isSend)
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, response.body().get(i).getMessage(), " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()), response.body().get(i).isSeen(), BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                else
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, response.body().get(i).getMessage(), " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()),
                                                            BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                            } else if (response.body().get(i).getMsgType() == 1) {
                                                if (isSend)
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.IMG_TYPE_SENT, CommonClass.FilesPath + "/" + response.body().get(i).getMessage()/*getImage()*/, " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()), response.body().get(i).isSeen(), BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                else
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.IMG_TYPE_RECEIVED, CommonClass.FilesPath + "/" + response.body().get(i).getMessage()/*getImage()*/, " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()),
                                                            BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());

                                            } else if (response.body().get(i).getMsgType() == 2) {
                                                if (isSend)
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Video_TYPE_SENT, CommonClass.FilesPath + "/" + response.body().get(i).getMessage(), " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()), response.body().get(i).isSeen(), BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                else
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Video_TYPE_RECEIVED, CommonClass.FilesPath + "/" + response.body().get(i).getMessage(), " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()),
                                                            BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());

                                            } else if (response.body().get(i).getMsgType() == 3) {
                                                if (isSend)
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Voice_TYPE_SENT, CommonClass.FilesPath + "/" + response.body().get(i).getMessage(), " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()), response.body().get(i).isSeen(), BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                else
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Voice_TYPE_RECEIVED, CommonClass.FilesPath + "/" + response.body().get(i).getMessage(), " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()),
                                                            BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                            }
                                            newMsgPosition = i; //msgDtoList.size();
                                            msgDtoList.add(newMsgPosition, msgDto);
                                            chatAppMsgAdapter.notifyItemInserted(newMsgPosition);

                                            CallDBMethods.MessageCommands(-1, response.body().get(i).getID(), response.body().get(i).getDriverID(), "",
                                                    response.body().get(i).getMsgType(), CommonClass.FilesPath + "/" + response.body().get(i).getMessage(),
                                                    response.body().get(i).getMessage(), response.body().get(i).getDateSend(), response.body().get(i).getTimeSend(),
                                                    newMsgPosition, response.body().get(i).getOperatorID() + "", isSend ? 1 : 0, response.body().get(i).isSeen() ? 1 : 0,
                                                    ActivityChat.this, CallDBMethods.CommandType.Insert, response.body().get(i).isEdit() ? 1 : 0);


                                        }

                                    if (!isSwiping) {
                                        msgRecyclerView.scrollToPosition(newMsgPosition);
                                        isNew = true;
                                    } else
                                        isNew = false;
                                  /*  else
                                        msgRecyclerView.scrollToPosition(pos);*/
                                    mySwipe.setRefreshing(false);
                                    if (loading != null && animation != null) {
                                        loading.setVisibility(View.GONE);
                                        animation.stop();
                                    }
                                } else {//پیامهایی که از سرورمیاد حتما بیشتر یا مساوی پیام های من هست پس احتمال اجرای این دستور بسیار پایین است چون من تعداد پیام هایی که دارم رو میدم ،همون تعداد یا بیشتر(پیام خوانده نشده) دریافت میکنم
                                    if (Objects.requireNonNull(msgRecyclerView.getAdapter()).getItemCount() > Objects.requireNonNull(response.body()).size()) {
                                        /*0*/
                                        /* diff*/
                                        if (msgRecyclerView.getAdapter().getItemCount() > response.body().size()) {
                                            msgDtoList.subList(response.body().size(), msgRecyclerView.getAdapter().getItemCount()).clear();
                                        }
                                        chatAppMsgAdapter.notifyItemRangeRemoved(response.body().size(), msgRecyclerView.getAdapter().getItemCount() - 1);

                                        mySwipe.setRefreshing(false);

                                    } else if (msgRecyclerView.getAdapter().getItemCount() < response.body().size()) {
                                        // اگر پیام جدیدی نسبت به دیتابیس داریم و مال این چت فعلی نیست نوتیفیکیشن بده در غیر اینصورت به لیست پیامها اضافه کن
                                        String OpID = String.valueOf(response.body().get(0).getOperatorID());
                                        if (/*!active*/(id != null && !OpID.equals(id)) || !active)
                                            Notify();
                                        else {
                                            int newMsgPosition = 0;
                                            for (int i = /*0*/ msgRecyclerView.getAdapter().getItemCount(); i < response.body().size() /*diff*/; i++) {
                                                boolean isSend = response.body().get(i).isSenderType();//.getSender().equals(driverID);
                                                if (response.body().get(i).getMsgType() == 0) {
                                                    if (isSend)
                                                        msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, response.body().get(i).getMessage(), " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()), response.body().get(i).isSeen(), BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                    else
                                                        msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, response.body().get(i).getMessage(), " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()),
                                                                BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                } else if (response.body().get(i).getMsgType() == 1) {
                                                    if (isSend)
                                                        msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.IMG_TYPE_SENT, CommonClass.FilesPath + "/" + response.body().get(i).getMessage()/*getImage()*/, " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()), response.body().get(i).isSeen(), BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                    else
                                                        msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.IMG_TYPE_RECEIVED, CommonClass.FilesPath + "/" + response.body().get(i).getMessage()/*getImage()*/, " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()),
                                                                BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());

                                                } else if (response.body().get(i).getMsgType() == 2) {
                                                    if (isSend)
                                                        msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Video_TYPE_SENT, CommonClass.FilesPath + "/" + response.body().get(i).getMessage(), " ", response.body().get(i).getTimeSend(), response.body().get(i).isSeen(), BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                    else
                                                        msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Video_TYPE_RECEIVED, CommonClass.FilesPath + "/" + response.body().get(i).getMessage(), " ", response.body().get(i).getTimeSend(),
                                                                BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());

                                                } else if (response.body().get(i).getMsgType() == 3) {
                                                    if (isSend)
                                                        msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Voice_TYPE_SENT, CommonClass.FilesPath + "/" + response.body().get(i).getMessage(), " ", response.body().get(i).getTimeSend() + " " + response.body().get(i).getDateSend(), response.body().get(i).isSeen(), BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                    else
                                                        msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Voice_TYPE_RECEIVED, CommonClass.FilesPath + "/" + response.body().get(i).getMessage(), " ", response.body().get(i).getTimeSend() + " " + response.body().get(i).getDateSend(),
                                                                BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                }
                                                newMsgPosition = i; //msgDtoList.size();
                                                msgDtoList.add(newMsgPosition, msgDto);
                                                chatAppMsgAdapter.notifyItemInserted(newMsgPosition);
                                              /*  if (!isSwiping)
                                                    msgRecyclerView.scrollToPosition(newMsgPosition);
                                                else
                                                    msgRecyclerView.scrollToPosition(pos);*/

                                                CallDBMethods.MessageCommands(-1, response.body().get(i).getID(), response.body().get(i).getDriverID(), "",
                                                        response.body().get(i).getMsgType(), CommonClass.FilesPath + "/" + response.body().get(i).getMessage(),
                                                        response.body().get(i).getMessage(), response.body().get(i).getDateSend(), response.body().get(i).getTimeSend(),
                                                        newMsgPosition, String.valueOf(response.body().get(i).getOperatorID()), isSend ? 1 : 0, response.body().get(i).isSeen() ? 1 : 0,
                                                        ActivityChat.this, CallDBMethods.CommandType.Insert, response.body().get(i).isEdit() ? 1 : 0);

                                            }
                                            if (!isSwiping) {
                                                isNew = true;
                                                msgRecyclerView.scrollToPosition(newMsgPosition);
                                            } else
                                                isNew = false;
                                           /* else
                                                msgRecyclerView.scrollToPosition(pos);*/
                                            mySwipe.setRefreshing(false);
                                        }
                                    }
                                    UnreadReceivedMessages = new ArrayList<>();
                                    //همگام سازی پیام های موجود با پیام های سرور
                                    if (msgRecyclerView.getAdapter().getItemCount() == response.body().size()) {
                                        new DoCommand_MessageDB(ActivityChat.this).deleteMessages(id);
                                        int newMsgPosition = 0;
                                        for (int i = 0; i < response.body().size(); i++) {
                                            boolean isSend = response.body().get(i).isSenderType();//.getSender().equals(driverID);
                                            if (response.body().get(i).getMsgType() == 0) {
                                                if (isSend)
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT,
                                                            response.body().get(i).getMessage(),
                                                            " ",
                                                            new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()),
                                                            response.body().get(i).isSeen(),
                                                            BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                else
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, response.body().get(i).getMessage(), " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()), response.body().get(i).isSeen(), BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                            } else if (response.body().get(i).getMsgType() == 1) {
                                                if (isSend)
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.IMG_TYPE_SENT, CommonClass.FilesPath + "/" + response.body().get(i).getMessage()/*getImage()*/, " ",
                                                            new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()),
                                                            response.body().get(i).isSeen(),
                                                            BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                else
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.IMG_TYPE_RECEIVED, CommonClass.FilesPath + "/" + response.body().get(i).getMessage()/*getImage()*/, " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()), response.body().get(i).isSeen(), BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());

                                            } else if (response.body().get(i).getMsgType() == 2) {
                                                if (isSend)
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Video_TYPE_SENT, CommonClass.FilesPath + "/" + response.body().get(i).getMessage(),
                                                            " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()),
                                                            response.body().get(i).isSeen(),
                                                            BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                else
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Video_TYPE_RECEIVED, CommonClass.FilesPath + "/" + response.body().get(i).getMessage(), " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()), response.body().get(i).isSeen(), BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());

                                            } else if (response.body().get(i).getMsgType() == 3) {
                                                if (isSend)
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Voice_TYPE_SENT, CommonClass.FilesPath + "/" + response.body().get(i).getMessage(),
                                                            " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()),
                                                            response.body().get(i).isSeen(),
                                                            BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                                else
                                                    msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Voice_TYPE_RECEIVED, CommonClass.FilesPath + "/" + response.body().get(i).getMessage(), " ", new CommonClass().PerisanNumber(response.body().get(i).getDateSend() + "\n" + response.body().get(i).getTimeSend()), response.body().get(i).isSeen(), BigInteger.valueOf(Long.parseLong(response.body().get(i).getID())), response.body().get(i).isEdit());
                                            }
                                            newMsgPosition = i;//msgDtoList.size();
                                            msgDtoList.set(newMsgPosition, msgDto);
                                            chatAppMsgAdapter.notifyItemChanged(newMsgPosition);

                                            //اگر پیام از سمت اپراتوری هست که در صفحه چتش هستیم و خونده نشده و فرستده من نیستم به لیست ارسال برای اعلام خوانده شدن اضافه کن
                                            if (/*response.body().get(i).getSender().equals(id)*/
                                                    (!response.body().get(i).isSenderType() && response.body().get(i).getOperatorID() == Integer.parseInt(id))
                                                            && !response.body().get(i).isSeen())
                                                UnreadReceivedMessages.add(response.body().get(i));

                                            CallDBMethods.MessageCommands(-1, response.body().get(i).getID(), response.body().get(i).getDriverID(), "",
                                                    response.body().get(i).getMsgType(), CommonClass.FilesPath + "/" + response.body().get(i).getMessage(),
                                                    response.body().get(i).getMessage(), response.body().get(i).getDateSend(), response.body().get(i).getTimeSend(),
                                                    newMsgPosition, String.valueOf(response.body().get(i).getOperatorID()), isSend ? 1 : 0, response.body().get(i).isSeen() ? 1 : 0,
                                                    ActivityChat.this, CallDBMethods.CommandType.Insert, response.body().get(i).isEdit() ? 1 : 0);


                                        }
                                        if (!isSwiping)
                                            msgRecyclerView.scrollToPosition(newMsgPosition);
                                       /* else
                                            msgRecyclerView.scrollToPosition(pos);*/

                                        readMsgs(UnreadReceivedMessages);
                                        mySwipe.setRefreshing(false);

                                    }
                                    if (loading != null && animation != null) {
                                        loading.setVisibility(View.GONE);
                                        animation.stop();
                                    }

                                }

                            }

                        } else {

                            int errMsg = response.raw().code();
                            if (errMsg != 0) {
                                new CommonClass().ShowToast(ActivityChat.this, new CommonClass().ErrorMessages(errMsg, ActivityChat.this), Toast.LENGTH_SHORT);
                            } else {
                                new CommonClass().ShowToast(ActivityChat.this, response.raw().message(), Toast.LENGTH_SHORT);
                            }

                            Analytics.trackEvent("ChatAC_" + "getServerMessagesSwip1 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + errMsg);


                            mySwipe.setRefreshing(false);
                            if (loading != null && animation != null) {
                                loading.setVisibility(View.GONE);
                                animation.stop();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Server_Message>> call, @NonNull Throwable t) {
                    if (t != null) {
                        new CommonClass().ShowToast(ChatClass.context, CommonClass.ToastMessages.Network_Problem, t.getMessage());
                    }
                    mySwipe.setRefreshing(false);
                    if (loading != null && animation != null) {
                        loading.setVisibility(View.GONE);
                        animation.stop();
                    }
                }
            });
        } catch (Exception ex) {
            String d = ex.getMessage();
            new CommonClass().ShowToast(this, new CommonClass().ErrorMessages(11, ActivityChat.this) + d, Toast.LENGTH_SHORT);
            Analytics.trackEvent("ChatAC_" + "getServerMessagesSwip3 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + ex.getMessage());
            mySwipe.setRefreshing(false);
            if (loading != null && animation != null) {
                loading.setVisibility(View.GONE);
                animation.stop();
            }
        }
    }

    public void Notify() {

        try {

            boolean showNotifiation = false;
            //اگر چت باز نیست باید اعلان بیاد
            if (active) {
                //اگر چت بازه اگه پیام مال این اپراتور نیس باید اعلان بیاد
                if (id != null && jsonArray != null && !id.equals(jsonArray.get(5).getAsString())) {
                    showNotifiation = true;
                }
            } else
                showNotifiation = true;
            if (/*(id!=null && jsonArray!=null && !id.equals(jsonArray.get(2).getAsString())) || !active*/showNotifiation) { /*} else (!active ) {*/

                final String NOTIFICATION_CHANNEL_ID = "10001";
                // ActivityChat.id = jsonArray.get(2).getAsString();

           /* String notification_title = jsonArray.get(1).getAsString();
            String notification_message = jsonArray.get(0).getAsString();*/

                String notification_title = /*jsonArray != null && jsonArray.size() != 0 ? jsonArray.get(7).getAsString() :*/ getResources().getString(R.string.label);
                String notification_message = jsonArray != null && jsonArray.size() != 0 ? jsonArray.get(4).getAsString() : "پیام جدید";

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(ActivityChat.this)
                                .setSmallIcon(R.drawable.tahlil)
                                .setContentTitle(notification_title)
                                .setContentText(notification_message)
                                .setAutoCancel(true)
                                .setVibrate(new long[]{100, 200, 300, 400/*, 500, 400, 300, 200, 400*/})
                                .setSound(alarmSound);
                                              /*  .setSound(Uri.parse("android.resource://"
                                                        + myContext.getApplication().getPackageName() + "/" + R.raw.sms)*//* alarmSound*//*);*/


                //رو اعلان که زد باید لیست اپراتور ها باز بشه
                Intent resultIntent = new Intent(getApplicationContext(), ActivityChatList);
                resultIntent.putExtra("menuFragment", "favoritesMenuItem");
                // resultIntent.putExtra("user_id", from_user_id);

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                ActivityChat.this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT
                        );

                mBuilder.setContentIntent(resultPendingIntent);
                //برای مدیریت نوتیفیکیشن برای هر اپراتور بصورت جدا
                int mNotificationId = jsonArray != null ? Integer.parseInt(jsonArray.get(5).getAsString()) : 1000; //(int) System.currentTimeMillis();
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

                    mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                    if (mNotifyMgr != null) {
                        mNotifyMgr.createNotificationChannel(notificationChannel);
                        mNotifyMgr.notify(mNotificationId, mBuilder.build());
                    }
                }

                //TODO set refresh solution
                //بروزرسانی پیام های خوانده نشده هر اپراتور
               /* ActivityChatList chatlistfragment = new ActivityChatList();
                ActivityChatList.generateData(getApplicationContext());*/


                //اگر فرگمنت چت انتخاب نیست بدیج نشون بده
              /*  if (MainActivity.curvedBottomNavigationView.getSelectedItemId() != R.id.navigation_chat)
                    BottomMenuHelper.showBadgeMenu(getApplicationContext(), MainActivity.curvedBottomNavigationView, R.id.navigation_chat, "");*/


            }
        } catch (Exception e) {
            Analytics.trackEvent("ChatAC_" + "Notify " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());
        }

    }

    //خواندن لیستی از پیام های اپرااتور
    private void readMsgs(List<Server_Message> messageid) {
        String SERVER_METHOD_SEND = "ChangeAllStatusChat";
        try {
            hub.invoke(SERVER_METHOD_SEND, messageid);

        } catch (Exception e) {
            Analytics.trackEvent("ChatAC_" + "readMsgs " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

        }
    }

    static boolean getMsg = false;

    public void StartTimer() {
        // Toast.makeText(ActivityMain.context,"123",Toast.LENGTH_SHORT).show();

        try {
            handler = new Handler();
            handlerTask = new Runnable() {
                @Override
                public void run() {
                    // do something
                    // Toast.makeText(ActivityMain.context,"11",Toast.LENGTH_SHORT).show();

                    if (connection.getState() == ConnectionState.Disconnected) {
                        //Toast.makeText(ActivityMain.context,"12",Toast.LENGTH_SHORT).show();

                        txtStatus.setText("خطا در اتصال");
                        getMsg = true;
                    }
                    if (connection.getState() == ConnectionState.Connected && connection.getState() != ConnectionState.Reconnecting && connection.getState() != ConnectionState.Connecting) {
                        txtStatus.setText("متصل");
                        // Toast.makeText(ActivityMain.context,"13",Toast.LENGTH_SHORT).show();

                        // اگر تازه متصل شده(قبلش قطعی اتصال نداشتیم)برو پیامهای جدید رو بگیر
                        if (getMsg) {
                            // getServerMessages();
                            // براثر تایمر رفته واکشی نه سوییپ پس لودینگ فعال شود چون سوییپ نداریم
                            isSwiping = false;
                            getServerMessagesSwip();

                            getMsg = false;
                        }
                    }
                    if (connection.getState() == ConnectionState.Reconnecting || connection.getState() == ConnectionState.Connecting) {
                        txtStatus.setText("در حال اتصال...");
                        //  Toast.makeText(ActivityMain.context,"14",Toast.LENGTH_SHORT).show();

                        getMsg = true;
                    }

                    handler.postDelayed(handlerTask, 1000);
                }
            };
            handlerTask.run();
        } catch (Exception e) {
            Toast.makeText(ChatClass.context, "222  " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    /////////////////////////Json
    private void MessageRecive() {

        connection.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(final JsonElement json) {
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void run() {
                        JsonObject jsonObject = json.getAsJsonObject();
                        if (jsonObject != null && jsonObject.has("A")) {
                            try {

                                String method = jsonObject.get("M").getAsString();
                                if (method.equals("addNewMessage")) {//دریافت پیام جدید

                                    //Msgtext, senderid, ReceiverId, persianDate.simpleDate(),GetTimeNow(), MsgType, Convert.ToInt32(MsgID), SenderName
                                    jsonArray = jsonObject.getAsJsonArray("A");
                                    if (jsonArray != null && jsonArray.size() != 0) {
                                        if (jsonArray.get(2).getAsString().equals(driverID))//هرپیامی برای من اومد ناتیفکیشن باید بده
                                            Notify();
                                        if (jsonArray.get(1).getAsString().equals(id) && jsonArray.get(2).getAsString().equals(driverID)) {
                                            //agar operator folanie va bara man ferestade

                                            if (jsonArray.get(5).getAsInt() == 0) {//نوع پیام متنی
                                                msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED,
                                                        jsonArray.get(0).getAsString(),
                                                        "",
                                                        new CommonClass().PerisanNumber(jsonArray.get(3).getAsString() + "\n" + jsonArray.get(4).getAsString()),
                                                        jsonArray.get(6).getAsBigInteger(), false);

                                            } else if (jsonArray.get(5).getAsInt() == 1) {//نوع پیام عکس
                                                msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.IMG_TYPE_RECEIVED,
                                                        jsonArray.get(0).getAsString(),
                                                        "",
                                                        new CommonClass().PerisanNumber(jsonArray.get(3).getAsString() + "\n" + jsonArray.get(4).getAsString()),
                                                        jsonArray.get(6).getAsBigInteger(), false);

                                            } else if (jsonArray.get(5).getAsInt() == 2) {//نوع پیام فیلم
                                                msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Video_TYPE_RECEIVED,
                                                        jsonArray.get(0).getAsString(),
                                                        "",
                                                        new CommonClass().PerisanNumber(jsonArray.get(3).getAsString() + "\n" + jsonArray.get(4).getAsString()),
                                                        jsonArray.get(6).getAsBigInteger(), false);

                                            } else if (jsonArray.get(5).getAsInt() == 3) {//نوع پیام صدا
                                                msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Voice_TYPE_RECEIVED,
                                                        jsonArray.get(0).getAsString(),
                                                        "",
                                                        new CommonClass().PerisanNumber(jsonArray.get(3).getAsString() + "\n" + jsonArray.get(4).getAsString()),
                                                        jsonArray.get(6).getAsBigInteger(), false);

                                            }
                                            int newMsgPosition = msgDtoList.size();
                                            msgDtoList.add(msgDto);
                                            //msgDtoList.set(jsonArray.get(6).getAsInt(), msgDto);
                                            chatAppMsgAdapter.notifyItemInserted(newMsgPosition/*jsonArray.get(6).getAsInt()*/);
                                            msgRecyclerView.scrollToPosition(newMsgPosition/*jsonArray.get(6).getAsInt()*/);

                                            //اگر هنگام دریافت صفحه چت باز باشه پیام خونده میشه و باید به اپراتور اطلاع داده بشه
                                            if (active)
                                                readMsg(jsonArray.get(6).getAsInt());

                                            //ذخیره پیام در دیتابیس محلی
                                            boolean res = CallDBMethods.MessageCommands(-1, jsonArray.get(6).getAsString().equals("") ? connection.getMessageId() : jsonArray.get(6).getAsString(),
                                                    driverID, "", jsonArray.get(5).getAsInt(), CommonClass.FilesPath + "/" + jsonArray.get(0).getAsString(),
                                                    jsonArray.get(0).getAsString(), jsonArray.get(3).getAsString(), jsonArray.get(4).getAsString(), newMsgPosition,
                                                    id, 0, -1, ActivityChat.this, CallDBMethods.CommandType.Insert, 0);


                                            if (!res)
                                                new CommonClass().ShowToast(getApplicationContext(), "ذخیره نشد", Toast.LENGTH_SHORT);
                                            isNew = true;
                                        } else if (jsonArray.get(1).getAsString().equals(driverID) && jsonArray.get(2).getAsString().equals(id)) {
                                            //agar man ferestadam (2==sender id)

                                            // if (msgDtoList.get(jsonArray.get(6).getAsInt()).getTime() == "در حال ارسال...") {

                                            // msgDtoList.get(index).setTime(jsonArray.get(5).getAsString());
                                            //  ChatAppMsgAdapter.Sendstate = ChatAppMsgAdapter.state.send;
                                            if (jsonArray.get(5).getAsInt() == 0) {
                                                msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT,
                                                        msgContent,
                                                        "",
                                                        new CommonClass().PerisanNumber(jsonArray.get(3).getAsString() + "\n" + jsonArray.get(4).getAsString()),
                                                        jsonArray.get(6).getAsBigInteger(), false);

                                            } else if (jsonArray.get(5).getAsInt() == 1) {
                                                msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.IMG_TYPE_SENT,
                                                        LocalPath,
                                                        "",
                                                        new CommonClass().PerisanNumber(jsonArray.get(3).getAsString() + "\n" + jsonArray.get(4).getAsString()),
                                                        jsonArray.get(6).getAsBigInteger(), false);

                                            } else if (jsonArray.get(5).getAsInt() == 2) {
                                                msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Video_TYPE_SENT,
                                                        LocalPath,
                                                        "",
                                                        new CommonClass().PerisanNumber(jsonArray.get(3).getAsString() + "\n" + jsonArray.get(4).getAsString()),
                                                        jsonArray.get(6).getAsBigInteger(), false);

                                            } else if (jsonArray.get(5).getAsInt() == 3) {
                                                msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.Voice_TYPE_SENT,
                                                        msgContent,
                                                        "",
                                                        new CommonClass().PerisanNumber(jsonArray.get(3).getAsString() + "\n" + jsonArray.get(4).getAsString()),
                                                        jsonArray.get(6).getAsBigInteger(), false);

                                            }
                                            // int newMsgPosition = msgDtoList.size();
                                            //  msgDtoList.add(msgDto);
                                            //int newMsgPosition = msgDtoList.size() - 1;
                                            //index = newMsgPosition;

                                            // chatAppMsgAdapter.notifyItemInserted(/*jsonArray.get(6).getAsInt()*/newMsgPosition);
                                            //  msgRecyclerView.scrollToPosition(/*jsonArray.get(6).getAsInt()*/newMsgPosition);

                                            msgDtoList.set(newMsgPosition/*jsonArray.get(6).getAsInt()*/, msgDto);
                                            chatAppMsgAdapter.notifyItemChanged(newMsgPosition/*jsonArray.get(6).getAsInt()*/);
                                            msgRecyclerView.scrollToPosition(newMsgPosition/*jsonArray.get(6).getAsInt()*/);


                                            String filename = LocalPath == null || LocalPath.length() == 0 ? "" : LocalPath.substring(Objects.requireNonNull(LocalPath).lastIndexOf("/") + 1);
                                            if (messageType != 0)
                                                msgContent = /*filename.equals("") ? "" : CommonClass.FilesURL + */filename;
                                            boolean res = CallDBMethods.MessageCommands(-1, jsonArray.get(6).getAsString().equals("") ? connection.getMessageId() : jsonArray.get(6).getAsString(), driverID,
                                                    "", messageType, LocalPath, msgContent, jsonArray.get(3).getAsString(), jsonArray.get(4).getAsString(), newMsgPosition, id, 1, -1,
                                                    ActivityChat.this, CallDBMethods.CommandType.Insert, 0);

                                            if (!res)
                                                Toast.makeText(getApplicationContext(), "ذخیره نشد", Toast.LENGTH_SHORT).show();
                                            isNew = false;
                                            //  }

                                        }
                                    }
                                } else if (method.equals("resultChangeStatusChat")) {
                                    //اپراتور یکی از پیام های ما را خوانده پس این سمت برای پیام علامت خوانده شده میخورد
                                    JsonArray json = jsonObject.getAsJsonArray("A");
                                    if (json.get(1).getAsString().equals(driverID))
                                        for (int i = msgDtoList.size() - 1; i >= 0; i--) {
                                            if (msgDtoList.get(i).getID().equals(json.get(0).getAsBigInteger())) {
                                                msgDto = new ChatAppMsgDTO(msgDtoList.get(i).getMsgType(), msgDtoList.get(i).getMsgContent(), "", new CommonClass().PerisanNumber(msgDtoList.get(i).getTime()), true, json.get(0).getAsBigInteger(), msgDtoList.get(i).isEdited());
                                                msgDtoList.set(i, msgDto);
                                                chatAppMsgAdapter.notifyItemChanged(i);

                                                List<Messages> messagelist = new DoCommand_MessageDB(context).getListOfMessages(/*"MsgID like '%%'"*/"MsgID = '" + json.get(0).getAsString() + "' AND isSend = 1");
                                                if (messagelist.size() != 0) {
                                                    int uniqDBId = messagelist.get(0).getID();
                                                    String[] s = msgDtoList.get(i).getTime().split("\n");

                                                    CallDBMethods.MessageCommands(uniqDBId, json.get(0).getAsString(), driverID, "", messagelist.get(0).getMsgType(),
                                                            CommonClass.FilesPath + "/" + msgDtoList.get(i).getMsgContent(), msgDtoList.get(i).getMsgContent(), s[0],
                                                            s[1], i, id, 1, 1, ActivityChat.this, CallDBMethods.CommandType.Update, msgDtoList.get(i).isEdited() ? 1 : 0);


                                                }

                                                //  msgRecyclerView.scrollToPosition(i);
                                                break;
                                            }
                                        }
                                } else if (method.equals("returnChangeStatusList")) {
                                    //لیستی از پیام های ما که اپراتور خوانده
                                    JsonArray json = jsonObject.getAsJsonArray("A");
                                    JsonArray MsgSeenListID = json.get(0).getAsJsonArray();
                                    if (json.get(1).getAsString().equals(driverID)) {
                                        for (int j = 0; j < MsgSeenListID.size(); j++) {
                                            for (int i = msgDtoList.size() - 1; i >= 0; i--) {
                                                if (msgDtoList.get(i).getID().equals(MsgSeenListID.get(j).getAsBigInteger())) {
                                                    msgDto = new ChatAppMsgDTO(msgDtoList.get(i).getMsgType(), msgDtoList.get(i).getMsgContent(), "", new CommonClass().PerisanNumber(msgDtoList.get(i).getTime()), true, MsgSeenListID.get(j).getAsBigInteger(), msgDtoList.get(i).isEdited());
                                                    msgDtoList.set(i, msgDto);
                                                    chatAppMsgAdapter.notifyItemChanged(i);

                                                    List<Messages> messagelist = new DoCommand_MessageDB(context).getListOfMessages(/*"MsgID like '%%'"*/"MsgID = '" + MsgSeenListID.get(j).getAsString() + "' AND isSend = 1");
                                                    if (messagelist.size() != 0) {
                                                        int uniqDBId = messagelist.get(0).getID();
                                                        String[] s = msgDtoList.get(i).getTime().split("\n");

                                                        CallDBMethods.MessageCommands(uniqDBId, MsgSeenListID.get(j).getAsString(), driverID, "", messagelist.get(0).getMsgType(),
                                                                CommonClass.FilesPath + "/" + msgDtoList.get(i).getMsgContent(), msgDtoList.get(i).getMsgContent(), s[0], s[1],
                                                                i, id, 1, 1, ActivityChat.this, CallDBMethods.CommandType.Update, msgDtoList.get(i).isEdited() ? 1 : 0);


                                                    }


                                                    break;

                                                }

                                            }
                                        }
                                    }
                                } else if (method.equals("resultMessageUpdateDelete")) {
                                    JsonArray json = jsonObject.getAsJsonArray("A");

                                    String Msid = json.get(0).getAsString();
                                    String TextMsg = json.get(1).getAsString();
                                    boolean result = json.get(2).getAsBoolean();
                                    String ResultMsg = json.get(3).getAsString();
                                    byte OpMode = json.get(4).getAsByte();

                                    if (result) {
                                        if (OpMode == 2) {

                                            List<Messages> messages = new DoCommand_MessageDB(ActivityChat.context).getListOfMessages("MsgID = '" + Msid + "' and DriverID = '" + driverID + "' and OperatorID = '" + ActivityChat.id + "'");
                                            if (messages.size() != 0) {
                                                int pos = messages.get(0).getMsgPos();
                                                msgDtoList.remove(pos);
                                                chatAppMsgAdapter.notifyItemRemoved(pos);
                                                chatAppMsgAdapter.notifyDataSetChanged();
                                                new DoCommand_MessageDB(ActivityChat.context).deleteMessage(Msid);
                                            }
                                        } else if (OpMode == 1) {
                                            List<Messages> messages = new DoCommand_MessageDB(ActivityChat.context).getListOfMessages("MsgID = '" + Msid + "' and DriverID = '" + driverID + "' and OperatorID = '" + ActivityChat.id + "'");
                                            if (messages.size() != 0) {
                                                int pos = messages.get(0).getMsgPos();// MessageCmdAdapter.EditMsgPsition;

                                                msgDto = new ChatAppMsgDTO(MessageCmdAdapter.chatAppMsgDTO.getMsgType(),
                                                        editMessageView.getTxtMessage().getText().toString(),
                                                        MessageCmdAdapter.chatAppMsgDTO.getOperatorName(),
                                                        MessageCmdAdapter.chatAppMsgDTO.getTime(),
                                                        MessageCmdAdapter.chatAppMsgDTO.isSeen(),
                                                        MessageCmdAdapter.chatAppMsgDTO.getID(), true);

                                                msgDtoList.set(pos, msgDto);
                                                chatAppMsgAdapter.notifyItemChanged(pos);


                                                //جایگزاری پیام ویرایش شده در لیست چت ها
                                                //todo jaygozari maqadir static ba maqadir vakeshi az database
                                                int uniqDBId = messages.get(0).getID();
                                                String[] s = MessageCmdAdapter.chatAppMsgDTO.getTime().split("\n");
                                                CallDBMethods.MessageCommands(uniqDBId, MessageCmdAdapter.chatAppMsgDTO.getID().toString(), driverID, "", messages.get(0).getMsgType(),
                                                        "", editMessageView.getTxtMessage().getText().toString(), s[0],
                                                        s[1], pos, id, 1, MessageCmdAdapter.chatAppMsgDTO.isSeen() ? 1 : 0, ActivityChat.this, CallDBMethods.CommandType.Update, 1);

                                                editMessageView.getTxtMessage().setText("");

                                            }
                                        }
                                    } else {
                                        new CommonClass().ShowToast(ActivityChat.this, ResultMsg, Toast.LENGTH_SHORT);
                                    }
                                }
                            } catch (Exception e) {

                                Analytics.trackEvent("ChatAC_" + "MessageRecive " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

                            }
                        }
                    }
                });
            }
        });
    }

    //خواندن یک پیام اپراتور
    private void readMsg(int messageid) {
        String SERVER_METHOD_SEND = "changestatuschat";
        try {
            hub.invoke(SERVER_METHOD_SEND, messageid, id);
        } catch (Exception e) {
            Analytics.trackEvent("ChatAC_" + "readMsg " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());
        }
    }

    //copy and paste possible
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            if (w != null) {
                w.getLocationOnScreen(scrcoords);
            }
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom())) {

                ((EditText) view).setError(null);
                view.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null && getWindow().getCurrentFocus() != null && getWindow().getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                }
            }
        }
        return ret;
    }

    //تبدیل صدا به متن
    public void voiceTotxt(View view) {
        try {

           /* if (TransportClass.showCaseView != null && TransportClass.showCaseView.isShowing()) {
                TransportClass.showCaseView.hide();
            } else*/
            {
                new CommonClass().askForPermission(ActivityChat.this, Manifest.permission.RECORD_AUDIO, CommonClass.AUDIO);
                if (new CommonClass().CheckForPermission(ActivityChat.this, Manifest.permission.RECORD_AUDIO))
                    new CommonClass().voiceTotxt(ActivityChat.this);
                else {
                    new CommonClass().ShowToast(ActivityChat.this, CommonClass.ToastMessages.permission_Denied, "");
                    voiceTotxt(view);
                }

            }
        } catch (Exception e) {
            Analytics.trackEvent("ChatAC_" + "voiceTotxt " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

        }
    }

    /////////////////start record voice
    public void start() {
        try {
            myRecorder.prepare();
            myRecorder.start();
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            Analytics.trackEvent("ChatAC_" + "start1 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            Analytics.trackEvent("ChatAC_" + "start2 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

            e.printStackTrace();
        }

    }

    /////////////////stop record voice
    public void stop() {
        try {
            myRecorder.stop();
            myRecorder.release();
            myRecorder = null;


        } catch (IllegalStateException e) {
            e.printStackTrace();
            Analytics.trackEvent("ChatAC_" + "stop1 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
            Analytics.trackEvent("ChatAC_" + "stop2 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

        }
    }

    /////////////////recording voice animation

    @Override
    public void onRecordingStarted() {
        try {

            IsRecordingVoice = true;
            //  if (connection.getState() == ConnectionState.Connected && connection.getState() != ConnectionState.Reconnecting && connection.getState() != ConnectionState.Connecting) {
            time = System.currentTimeMillis() / (1000);
            //  String root = Environment.getExternalStorageDirectory().toString();
            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            String time = (today.format("%k:%M:%S"));
            String name = driverID + "-" + id + "-" + /*ChangeDate.getCurrentDate()*/CommonClass.GetCurrentMDate().replace('/', '-') + "-" + time.replace(':', '-'); // UUID.randomUUID().toString();

            File myDir = new File(/*root + "/tahlilgar"*/CommonClass.FilesPath);
            myDir.mkdirs();

            if (!myDir.exists()) {
                myDir.mkdir();
            }


            outputFile = /*root + "/tahlilgar/"*/CommonClass.FilesPath + "/" + name + ".ogg"; //Environment.getExternalStorageDirectory().getAbsolutePath() + "/javacodegeeksRecording.ogg";//3gpp";

            try {
                myRecorder = new MediaRecorder();
                myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                myRecorder.setOutputFile(outputFile);
            } catch (Exception e) {
                Analytics.trackEvent("ChatAC_" + "onRecordingStarted1 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

                e.printStackTrace();
            }

            start();
      /*  } else {
            MainActivity.new CommonClass().ShowToast(getApplicationContext(), CommonClass.ToastMessages.Is_Disconnect, "");
        }*/
        } catch (Exception e) {
            Analytics.trackEvent("ChatAC_" + "onRecordingStarted2 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

        }

    }

    @Override
    public void onRecordingLocked() {

    }

    @Override
    public void onRecordingCompleted() {

        try {

            int recordTime = (int) ((System.currentTimeMillis() / (1000)) - time);

            if (recordTime > 2) {

                stop();
/////////////////////////////////////////////////////convert voice to byte
                if (new CommonClass().CheckForPermission(ActivityChat.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    saveVoice();
                } else {
                    new CommonClass().askForPermission(ActivityChat.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, CommonClass.WRITE_EXST);
                    saveVoice();
                }

            }
        } catch (Exception e) {
            Analytics.trackEvent("ChatAC_" + "onRecordingCompleted " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

        }

    }

    private void saveVoice() {
        try {


            File file = new File(outputFile);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                Analytics.trackEvent("ChatAC_" + "saveVoice1 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

                e.printStackTrace();
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];

            try {
                for (int readNum; (readNum = fis.read(b)) != -1; ) {
                    bos.write(b, 0, readNum);
                }
            } catch (Exception e) {
                Analytics.trackEvent("ChatAC_" + "saveVoice2 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

            }


            byte[] bytes = bos.toByteArray();
            fileByte = bytes;
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(outputFile));

            // msgContent =// mediaPlayer.getDuration() + "";
            LocalPath = outputFile;
            msgContent = LocalPath;
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(outputFile));

            if (connection.getState() == ConnectionState.Connected && connection.getState() != ConnectionState.Reconnecting && connection.getState() != ConnectionState.Connecting) {
                UploadFile(LocalPath, 3);
         /*   if (UploadResult) {
                String filename = LocalPath.substring(Objects.requireNonNull(LocalPath).lastIndexOf("/") + 1);
                sendMessage(filename, 3);
                sendingState(3);
                UploadResult=false;
            }*/
            } else {
                new CommonClass().ShowToast(getApplicationContext(), CommonClass.ToastMessages.Is_Disconnect, "");

            }
            IsRecordingVoice = false;
        } catch (Exception e) {
            Analytics.trackEvent("ChatAC_" + "saveVoice3 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

        }
    }

    private void UploadFile(final String FilePath, final int msgType) {

        if (new CommonClass().GpsIsActive(ActivityChat.this)) {
            try {
                final ProgressDialog pd = new ProgressDialog(this);
                pd.setMessage(getResources().getString(R.string.sending));
                pd.setCancelable(false);
                pd.show();


                //creating a file
                File file = new File(FilePath);
                //creating request body for file
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file",/* file.getName()*/ URLEncoder.encode(file.getName(), "utf-8"), requestFile);


                APIService service =
                        ServiceGenerator.GetCommonClient().create(APIService.class);

                Call<String> call = service.uploadMulFiles(body/*, ChatClass.driverID, id, driverName*/, appCode);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@Nullable Call<String> call,
                                           @Nullable Response<String> response) {

                        if (response != null && response.isSuccessful()) {
                            //  MainActivity.new CommonClass().ShowToast(activity_new_ticket.this, response.body() + "", Toast.LENGTH_SHORT);
                            //اگر فایل اپلود شد به سیگنال آر پیام بده
                            pd.cancel();
                            String filename = FilePath.substring(Objects.requireNonNull(FilePath).lastIndexOf("/") + 1);
                            sendMessage(filename, msgType);
                            sendingState(msgType);
                            //  UploadResult = true;
                        } else {
                            int errMsg = response.raw().code();
                            if (errMsg != 0) {
                                new CommonClass().ShowToast(ActivityChat.this, new CommonClass().ErrorMessages(errMsg, ActivityChat.this), Toast.LENGTH_SHORT);
                            } else {
                                new CommonClass().ShowToast(ActivityChat.this, response.raw().message(), Toast.LENGTH_SHORT);
                            }


                            Analytics.trackEvent("ChatAC_" + "UploadFile1 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + errMsg);

                            pd.cancel();
                            new CommonClass().ShowToast(ActivityChat.this, getResources().getString(R.string.FailUpload), Toast.LENGTH_SHORT);
                            // UploadResult = false;
                        }

                    }

                    @Override
                    public void onFailure(@Nullable Call<String> call, @Nullable Throwable t) {
                        if (t != null) {
                            new CommonClass().ShowToast(ActivityChat.this, t.getMessage(), Toast.LENGTH_SHORT);
                        }
                        pd.cancel();
                        // UploadResult = false;
                    }
                });
            } catch (Exception ex) {
                Analytics.trackEvent("ChatAC_" + "UploadFile3 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + ex.getMessage());

                String d = ex.getMessage();
                new CommonClass().ShowToast(ActivityChat.this, new CommonClass().ErrorMessages(11, ActivityChat.this) + d, Toast.LENGTH_SHORT);
            }
        } else {
            new CommonClass().ActiveGPSMessage(ActivityChat.this);
        }

    }


    @Override
    public void onRecordingCanceled() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {

            //TODO set solution
            //ActivityChatList chatlistfragment = new ActivityChatList();
            //chatlistfragment.generateData(getApplicationContext());//);
        } catch (Exception e) {
            Analytics.trackEvent("ChatAC_" + "onBackPressed " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());
        }
    }

    /////////////////////////start connection
    public static void startConnection() {

        try {

            //Toast.makeText(ActivityMain.context,"5 "+connection.toString()+"\n"+connection.getConnectionData()+"\n"+connection.getState()+"\n"+connection.getUrl(),Toast.LENGTH_SHORT).show();

            if (connection.getState() == ConnectionState.Disconnected) {
                //Toast.makeText(ActivityMain.context,"6",Toast.LENGTH_SHORT).show();

                connection.start(sse).done(new Action<Void>() {
                    @Override
                    public void run(Void aVoid) throws Exception {

                        txtStatus.setText("متصل");
                    }
                });

              /*  connection.start().done(new Action<Void>() {
                    @Override
                    public void run(Void aVoid) throws Exception {
                        txtStatus.setText("متصل");

                    }
                });*/

               /* connection.start(sse).onError(new ErrorCallback() {
                    @Override
                    public void onError(Throwable throwable) {
                        String s=throwable.getMessage();
                    }
                });

                connection.start(sse).onCancelled(new Runnable() {
                    @Override
                    public void run() {
                    }
                });*/

            }

        } catch (Exception e) {
            Toast.makeText(ChatClass.context, "333  " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    //////////////////////////reconnect after disconnect
    public static void CloseConnection() {
        try {
            //   Toast.makeText(ActivityMain.context,"7",Toast.LENGTH_SHORT).show();

            connection.closed(new Runnable() {
                @Override
                public void run() {
                    new Timer(false).schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //   Toast.makeText(ActivityMain.context,"10",Toast.LENGTH_SHORT).show();

                            connection.start(sse);
                        }
                    }, 2000);
                    // Toast.makeText(ActivityMain.context,"9",Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            Toast.makeText(ChatClass.context, "444  " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }


    String name = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        try {

            if (requestCode == SELECT_PHOTO) {//انتخاب عکس و فیلم از گالری
                if (resultCode == RESULT_OK) {
                    //  Uri selectedMedia = imageReturnedIntent.getData();
                    Uri selectedMedia = intentToUri(imageReturnedIntent);
               /* InputStream MediaStream = null;
                try {
                    if (selectedMedia != null) {
                        MediaStream = getContentResolver().openInputStream(selectedMedia);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(MediaStream);*/


                    ////////////////////get type of selection file
                    ContentResolver cr = this.getContentResolver();
                    String mime = null;
                    if (selectedMedia != null) {
                        mime = cr.getType(selectedMedia);
                    }


                    /////////////////////save in directory


                    File myDir = new File(CommonClass.FilesPath);
                    myDir.mkdirs();

                    if (!myDir.exists()) {
                        myDir.mkdir();
                    }
                    new CommonClass().askForPermission(ActivityChat.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, CommonClass.WRITE_EXST);
                    if (new CommonClass().CheckForPermission(ActivityChat.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        if (mime != null) {
                            if (mime.contains("image")) {

                                SendImage(imageReturnedIntent);

                            } else if (mime.contains("video")) {

                                SendVideo(imageReturnedIntent);
                            }
                        }

                    } else {
                        new CommonClass().ShowToast(ActivityChat.this, CommonClass.ToastMessages.permission_Denied, "");
                        new CommonClass().askForPermission(ActivityChat.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, CommonClass.WRITE_EXST);
                    }
                }
            }

            //voice to text
            if (requestCode == 100) {
                if (resultCode == RESULT_OK && imageReturnedIntent != null) {
                    ArrayList<String> result = imageReturnedIntent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result != null) {
                        //تبدیل صدا به متن در چت
                        audioRecordView.getMessageView().setText(result.get(0));
                    }

                }
            }
        } catch (Exception e) {

            Analytics.trackEvent("ChatAC_" + "onActivityResult " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

        }
    }

    private Uri intentToUri(Intent imageReturnedIntent) {
        Uri selectedMedia = null;
        try {

            if (imageReturnedIntent.getData() == null) {
                selectedMedia = (Uri) imageReturnedIntent.getParcelableExtra(Intent.EXTRA_STREAM);

            } else {
                selectedMedia = imageReturnedIntent.getData();
            }
        } catch (Exception e) {
            Analytics.trackEvent("ChatAC_" + "intentToUri " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());
        }
        return selectedMedia;
    }

    private void SendVideo(Intent intent) {

        try {
            Uri selectedMedia = null;

            if (Intent.ACTION_SEND.equals(intent.getAction())) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    selectedMedia = (Uri) bundle.get(Intent.EXTRA_STREAM);
                }
            } else {
                selectedMedia = intent.getData();

            }
            if (selectedMedia != null) {
                try {
                    Time today = new Time(Time.getCurrentTimezone());
                    today.setToNow();

                    String time = (today.format("%k:%M:%S"));  // Current time
                    name = "" + driverID + "-" + id + "-" + CommonClass.GetCurrentMDate().replace('/', '-') + "-" + time.replace(':', '-'); //UUID.randomUUID().toString();
                    File storeDirectory12 = new File(CommonClass.FilesPath + "/" + name + ".mp4");

                    ///////get video size
                    InputStream inputStream = getContentResolver().openInputStream(selectedMedia);
                    if (inputStream != null) {
                        if (inputStream.available() / 1024 > 39936) {
                            isbig = true;
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.SelectVideoSizeFiewerThan40), Toast.LENGTH_SHORT).show();

                        } else {
                            isbig = false;
                            FileOutputStream fileOutputStream = new FileOutputStream(storeDirectory12);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            byte[] b = new byte[1024];

                            try {
                                for (int readNum; (readNum = inputStream.read(b)) != -1; ) {
                                    bos.write(b, 0, readNum);
                                }
                            } catch (Exception e) {
                                Analytics.trackEvent("ChatAC_" + "SendVideo1 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

                            }
                            byte[] bytes = bos.toByteArray();
                            fileByte = bytes;
                            // byte[] bytes = bos.toByteArray();
                            try {
                                fileOutputStream.write(bos.toByteArray());
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                // Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                Analytics.trackEvent("ChatAC_" + "SendVideo2 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

                                e.printStackTrace();
                            }
                        }
                    }
                               /* (inputStream, fileOutputStream);
                                fileOutputStream.close();*/
                    inputStream.close();
                } catch (FileNotFoundException e) {
                    Log.e("Exception", "" + e);
                    Analytics.trackEvent("ChatAC_" + "SendVideo3 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

                } catch (IOException e) {
                    Analytics.trackEvent("ChatAC_" + "SendVideo4 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

                    Log.e("Exception", "" + e);
                }

                if (!isbig) {
                    fileUri = selectedMedia;
                    LocalPath = CommonClass.FilesPath + "/" + name + ".mp4";
                    if (connection.getState() == ConnectionState.Connected && connection.getState() != ConnectionState.Reconnecting && connection.getState() != ConnectionState.Connecting) {
                        UploadFile(LocalPath, 2);

                    } else {
                        new CommonClass().ShowToast(getApplicationContext(), CommonClass.ToastMessages.Is_Disconnect, "");

                    }


                }
            }
        } catch (Exception e) {
            Analytics.trackEvent("ChatAC_" + "SendVideo5 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {

                //Read External Storage
                case 4:
                    Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageIntent, 11);
                    break;
                //Camera
                case 5:
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 12);
                    }
                    break;

            }

            //   Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            new CommonClass().ShowToast(ActivityChat.this, CommonClass.ToastMessages.permission_Denied, "");
        }
    }

    /////////////////////////timer for check connection status
    private void SendImage(Intent imageReturnedIntent) {
        try {


            Uri selectedMedia;
       /* if (MainActivity.IsReceiveFromOtherApp) {
            selectedMedia = (Uri) imageReturnedIntent.getParcelableExtra(Intent.EXTRA_STREAM);
            MainActivity.IsReceiveFromOtherApp = false;
        } else {*/
            selectedMedia = intentToUri(imageReturnedIntent);
            //}

            ChatClass.IsReceiveFromOtherApp = false;


            InputStream MediaStream = null;
            try {
                if (selectedMedia != null) {
                    MediaStream = getContentResolver().openInputStream(selectedMedia);
                }
            } catch (FileNotFoundException e) {

                Analytics.trackEvent("ChatAC_" + "SendImage1 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());


                e.printStackTrace();
            }
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(MediaStream);

            int imageHeight1 = yourSelectedImage.getHeight();
            int imageWidth1 = yourSelectedImage.getWidth();
            if (ExternalStorageUtil.isExternalStorageMounted()) {

                FileOutputStream fos = null;
                //img.setImageBitmap(bmp);


                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                yourSelectedImage.setHasAlpha(true);
                yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();

                String time = (today.format("%k:%M:%S"));

                name = "" + driverID + "-" + id + "-" + /*ChangeDate.getCurrentDate()*/CommonClass.GetCurrentMDate().replace('/', '-') + "-" + time.replace(':', '-');// UUID.randomUUID().toString();
                File myDirFile = new File(/*root + "/tahlilgar/"*/CommonClass.FilesPath + "/" + name + ".jpg");


                try {
                    if (myDirFile.exists()) {
                        myDirFile.delete();

                    }
                    myDirFile.createNewFile();
                } catch (IOException e) {
                    Analytics.trackEvent("ChatAC_" + "SendImage2 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

                    e.printStackTrace();
                }
                try {
                    fos = new FileOutputStream(myDirFile);
                } catch (FileNotFoundException e) {
                    Analytics.trackEvent("ChatAC_" + "SendImage3 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

                    e.printStackTrace();
                }
                try {
                    if (fos != null) {
                        fos.write(bytes.toByteArray());
                    }
                    if (fos != null) {
                        fos.flush();
                    }
                    fos.close();
                    // Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Analytics.trackEvent("ChatAC_" + "SendImage4 " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

                    e.printStackTrace();
                }


                byte[] byteArray = bytes.toByteArray();
                yourSelectedImage.recycle();
                fileByte = byteArray;

            }


            if (name != "")
                LocalPath = CommonClass.FilesPath + "/" + name + ".jpg";


            UploadFile(LocalPath, 1);
            if (connection.getState() == ConnectionState.Connected && connection.getState() != ConnectionState.Reconnecting && connection.getState() != ConnectionState.Connecting) {


            } else {
                new CommonClass().ShowToast(getApplicationContext(), CommonClass.ToastMessages.Is_Disconnect, "");

            }
        } catch (Exception e) {

            Analytics.trackEvent("ChatAC_" + "SendImage " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //for notification
        active = false;

        //اگر درحال پخش صدا هستیم با خروج از صفحه چت متوقف بشه
        if (chatAppMsgAdapter.mediaPlayer != null)
            chatAppMsgAdapter.mediaPlayer.stop();

    }


    //ارسال پیامی که از اشتراک گذاری دیگر اپلیکیشن ها امده
    public static void handleSendText(Intent intent) {
        try {

            ChatClass.IsReceiveFromOtherApp = false;
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null) {
                audioRecordView.getMessageView().setText(sharedText);
            }
        } catch (Exception e) {
            Analytics.trackEvent("ChatAC_" + "handleSendText " + driverID + "_" + CommonClass.GetCurrentMDate() + "_" + DeviceProperty + "_" + e.getMessage());

        }
    }


}
