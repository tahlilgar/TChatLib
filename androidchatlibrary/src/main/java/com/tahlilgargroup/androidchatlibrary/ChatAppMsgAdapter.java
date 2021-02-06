package com.tahlilgargroup.androidchatlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.microsoft.appcenter.analytics.Analytics;
import com.tahlilgargroup.commonlibrary.CommonClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.tahlilgargroup.androidchatlibrary.ChatClass.driverID;
import static com.tahlilgargroup.commonlibrary.CommonClass.DeviceProperty;


public class ChatAppMsgAdapter extends RecyclerView.Adapter<ChatAppMsgViewHolder> {

    private List<ChatAppMsgDTO> msgDtoList = null;
    public MediaPlayer mediaPlayer = null;
    private int Wc, Hc;

    // public enum  state{sending,send,feild}
    // public static state Sendstate;

    public ChatAppMsgAdapter(List<ChatAppMsgDTO> msgDtoList) {
        this.msgDtoList = msgDtoList;
    }

    @SuppressLint({"ClickableViewAccessibility", "ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ChatAppMsgViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        try {
            final ChatAppMsgDTO msgDto = this.msgDtoList.get(position);
            // If the message is a received message.
            if (ChatAppMsgDTO.MSG_TYPE_RECEIVED.equals(msgDto.getMsgType())) {
                // Show received message in left linearlayout.
                holder.leftMsgLayout.setVisibility(ConstraintLayout.VISIBLE);
                holder.leftMsgTextView.setText(msgDto.getMsgContent());
                // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
                // Otherwise each iteview's distance is too big.
                holder.rightMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.voice_send_seen.setVisibility(View.GONE);
                holder.video_send_seen.setVisibility(View.GONE);
                holder.img_send_seen.setVisibility(View.GONE);
                holder.msg_send_seen.setVisibility(View.GONE);

                holder.Operator_name.setText(msgDto.getOperatorName());
                holder.TimeResive.setText(msgDto.getTime());

                //اگر ویرایش شده ایکون ویرایش شده کنارش نمایش داده بشه
                if (msgDto.isEdited())
                    holder.msg_receive_edit.setVisibility(View.VISIBLE);
                else
                    holder.msg_receive_edit.setVisibility(View.GONE);
          /*  if (msgDto.isSearched()) {
                holder.leftMsgLayout.setBackgroundColor(R.color.colorPrimary);
            }*/

            }
            // If the message is a sent message.
            else if (ChatAppMsgDTO.MSG_TYPE_SENT.equals(msgDto.getMsgType())) {
                // Show sent message in right linearlayout.
                holder.rightMsgLayout.setVisibility(ConstraintLayout.VISIBLE);
                holder.rightMsgTextView.setText(msgDto.getMsgContent());
                // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
                // Otherwise each iteview's distance is too big.
                holder.leftMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.voice_send_seen.setVisibility(View.GONE);
                holder.video_send_seen.setVisibility(View.GONE);
                holder.img_send_seen.setVisibility(View.GONE);
                holder.msg_send_seen.setVisibility(View.GONE);
                if (msgDto.isSeen())
                    holder.msg_send_seen.setVisibility(View.VISIBLE);
                holder.Timesend.setText(msgDto.getTime());

                if (msgDto.isEdited())
                    holder.Msg_Send_Edit.setVisibility(View.VISIBLE);
                else
                    holder.Msg_Send_Edit.setVisibility(View.GONE);
          /*  if(msgDto.isSearched())
            {
                holder.rightMsgLayout.setBackgroundColor(R.color.colorPrimary);
            }*/

            }
            else if (ChatAppMsgDTO.IMG_TYPE_RECEIVED.equals(msgDto.getMsgType())) {
                // Show received message in left linearlayout.
                holder.leftImgLayout.setVisibility(ConstraintLayout.VISIBLE);
                File imgFile = new File(msgDto.getMsgContent());
                Bitmap bmp = null;
                if (imgFile.exists()) {
                    bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                } else {
                    bmp = BitmapFactory.decodeResource(ChatClass.context.getResources(), R.drawable.unknowimage);
                }
                // Bitmap bmp = BitmapFactory.decodeByteArray(/*msgDto.getMsgFile()*/, 0, msgDto.getMsgFile().length);
                if (bmp != null) {
                    Display display = ((Activity) ChatClass.context).getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    if (display != null) {
                        display.getSize(size);
                        int width = size.x;
                        int height = size.y;

                        int imageHeight = bmp.getHeight();
                        int imageWidth = bmp.getWidth();
                        //String imageType = options.outMimeType;
                        Hc = 0;
                        Wc = 0;

                        int a = (height / 3) * 2;
                        int b = (width / 3) * 2;
                        while (imageHeight > /*600*/((height / 3) * 2)) {
                            imageHeight /= 2;
                            Hc++;
                        }
                        while (imageWidth > /*600*/((width / 3) * 2)) {
                            imageWidth /= 2;
                            Wc++;
                        }
                        if (Hc != Wc) {
                            if (Hc > Wc) {
                                int c = Hc - Wc;
                                while (c > 0) {
                                    imageWidth /= 2;
                                    c -= 1;
                                }

                            } else {
                                int c = Wc - Hc;
                                while (c > 0) {
                                    imageHeight /= 2;
                                    c -= 1;
                                }
                            }
                        }

                        holder.imageResive.getLayoutParams().width = imageWidth;
                        holder.imageResive.getLayoutParams().height = imageHeight;

                        holder.imageResive.setImageBitmap(bmp);
                    }
                }
                holder.rightMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.voice_send_seen.setVisibility(View.GONE);
                holder.video_send_seen.setVisibility(View.GONE);
                holder.img_send_seen.setVisibility(View.GONE);
                holder.msg_send_seen.setVisibility(View.GONE);

                holder.TimeImgResive.setText(msgDto.getTime());

            }
            else if (ChatAppMsgDTO.IMG_TYPE_SENT.equals(msgDto.getMsgType())) {
                holder.rightImgLayout.setVisibility(ConstraintLayout.VISIBLE);

                File imgFile = new File(msgDto.getMsgContent());
                Bitmap bmp = null;
                if (imgFile.exists()) {
                    bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                } else {
                    bmp = BitmapFactory.decodeResource(ChatClass.context.getResources(), R.drawable.unknowimage);
                }

                // Bitmap bmp = BitmapFactory.decodeByteArray(msgDto.getMsgFile(), 0, msgDto.getMsgFile().length);

                //سایز عکسها از یه اندازه ای به بعد نمایش داده نمیشه باید سایزش تنظیم بشه

                if (bmp != null) {
                    //تنظیم طول و عرض عکس طبق اتدازه گوشی موبایل
                    Display display = ((Activity) ChatClass.context).getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    if (display != null) {
                        display.getSize(size);
                        int width = size.x;
                        int height = size.y;


                        int imageHeight = bmp.getHeight();
                        int imageWidth = bmp.getWidth();
                        //String imageType = options.outMimeType;
                        Hc = 0;
                        Wc = 0;

                        int a = (height / 3) * 2;
                        int b = (width / 3) * 2;
                        while (imageHeight > /*600*/((height / 3) * 2)) {
                            imageHeight /= 2;
                            Hc++;
                        }
                        while (imageWidth > /*600*/((width / 3) * 2)) {
                            imageWidth /= 2;
                            Wc++;
                        }
                        if (Hc != Wc) {
                            if (Hc > Wc) {
                                int c = Hc - Wc;
                                while (c > 0) {
                                    imageWidth /= 2;
                                    c -= 1;
                                }

                            } else {
                                int c = Wc - Hc;
                                while (c > 0) {
                                    imageHeight /= 2;
                                    c -= 1;
                                }
                            }
                        }

                        holder.imageSend.getLayoutParams().width = imageWidth;
                        holder.imageSend.getLayoutParams().height = imageHeight;
                        holder.imageSend.setImageBitmap(bmp);
                    }
                }
                holder.rightMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.voice_send_seen.setVisibility(View.GONE);
                holder.video_send_seen.setVisibility(View.GONE);
                holder.img_send_seen.setVisibility(View.GONE);
                holder.msg_send_seen.setVisibility(View.GONE);
                if (msgDto.isSeen())
                    holder.img_send_seen.setVisibility(View.VISIBLE);
                holder.TimeImgsend.setText(msgDto.getTime());

            }
            else if (ChatAppMsgDTO.Video_TYPE_RECEIVED.equals(msgDto.getMsgType())) {
                holder.leftVideoLayout.setVisibility(ConstraintLayout.VISIBLE);
                if (msgDto.getMsgContent() != null && msgDto.getMsgContent().length() != 0) {
                    File file = new File(msgDto.getMsgContent());
                    // if (file.exists())
                    // holder.VideoSend.setVideoPath(msgDto.getMsgContent());

                }
                holder.rightVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.voice_send_seen.setVisibility(View.GONE);
                holder.video_send_seen.setVisibility(View.GONE);
                holder.img_send_seen.setVisibility(View.GONE);
                holder.msg_send_seen.setVisibility(View.GONE);

                holder.TimeVideoResive.setText(msgDto.getTime());

            }
            else if (ChatAppMsgDTO.Video_TYPE_SENT.equals(msgDto.getMsgType())) {
                holder.rightVideoLayout.setVisibility(ConstraintLayout.VISIBLE);
                if (msgDto.getMsgContent() != null && msgDto.getMsgContent().length() != 0) {
                    File file = new File(msgDto.getMsgContent());
                    // if (file.exists())
                    //  holder.VideoSend.setVideoPath(msgDto.getMsgContent());

                }
                holder.leftVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.voice_send_seen.setVisibility(View.GONE);
                holder.video_send_seen.setVisibility(View.GONE);
                holder.img_send_seen.setVisibility(View.GONE);
                holder.msg_send_seen.setVisibility(View.GONE);
                if (msgDto.isSeen())
                    holder.video_send_seen.setVisibility(View.VISIBLE);
                holder.TimeVideosend.setText(msgDto.getTime());

            }
            else if (ChatAppMsgDTO.Voice_TYPE_RECEIVED.equals(msgDto.getMsgType())) {
                holder.leftVoiceLayout.setVisibility(ConstraintLayout.VISIBLE);


                //اگر آدرس صدا موجود باشد به صدا تبدیل شده و زمان نشان داده میشود
                if (msgDto.getMsgContent() != null && msgDto.getMsgContent().length() != 0) {
                    String path = msgDto.getMsgContent();
                    MediaPlayer mdia = MediaPlayer.create(ActivityChat.context, Uri.parse(path));
                    if (mdia != null) {

                        //بدست اوردن زمان صدا
                        int t = mdia.getDuration(); //Integer.parseInt(msgDto.getMsgContent());
                        int s = t / 1000;
                        int m = s / 60;
                        while (s >= 60) {
                            s -= 60;
                        }
                        holder.VoiceSendName.setText(m + ":" + s);
                    }
                } else {
                    holder.VoiceSendName.setText("صدا");
                }

                holder.rightVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.voice_send_seen.setVisibility(View.GONE);
                holder.video_send_seen.setVisibility(View.GONE);
                holder.img_send_seen.setVisibility(View.GONE);
                holder.msg_send_seen.setVisibility(View.GONE);

                holder.TimeVoiceResive.setText(msgDto.getTime());

            }
            else if (ChatAppMsgDTO.Voice_TYPE_SENT.equals(msgDto.getMsgType())) {
                holder.rightVoiceLayout.setVisibility(ConstraintLayout.VISIBLE);


                if (msgDto.getMsgContent() != null && msgDto.getMsgContent().length() != 0) {
                    String path = msgDto.getMsgContent();
                    MediaPlayer mdia = MediaPlayer.create(ActivityChat.context, Uri.parse(path));
                    if (mdia != null) {

                        //بدست اوردن زمان صدا
                        int t = mdia.getDuration(); //Integer.parseInt(msgDto.getMsgContent());
                        int s = t / 1000;
                        int m = s / 60;
                        while (s >= 60) {
                            s -= 60;
                        }
                        holder.VoiceSendName.setText(m + ":" + s);
                    }
                } else {
                    holder.VoiceSendName.setText("صدا");
                }


                holder.leftVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.voice_send_seen.setVisibility(View.GONE);
                holder.video_send_seen.setVisibility(View.GONE);
                holder.img_send_seen.setVisibility(View.GONE);
                holder.msg_send_seen.setVisibility(View.GONE);
                if (msgDto.isSeen())
                    holder.voice_send_seen.setVisibility(View.VISIBLE);
                holder.TimeVoicesend.setText(msgDto.getTime());
            }
            else if (ChatAppMsgDTO.TYPE_DATE.equals(msgDto.getMsgType())) {
                holder.DateLay.setVisibility(ConstraintLayout.VISIBLE);

                holder.txtDate.setText(msgDto.getMsgContent());

                holder.rightVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVoiceLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftMsgLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightImgLayout.setVisibility(ConstraintLayout.GONE);
                holder.rightVideoLayout.setVisibility(ConstraintLayout.GONE);
                holder.leftVideoLayout.setVisibility(ConstraintLayout.GONE);
            }


            holder.playSendVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatAppMsgAdapter.this.playVoice(position, holder);
                }
            });

            holder.pauseSendVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatAppMsgAdapter.this.pauseVoice(position);
                }
            });

            holder.playResiceVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatAppMsgAdapter.this.playVoice(position, holder);
                }
            });

            holder.pauseReciveVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatAppMsgAdapter.this.pauseVoice(position);
                }
            });

            holder.VideoSend.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        ChatAppMsgAdapter.this.playVideo(position);
                    return false;
                }
            });

            holder.VideoResive.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        ChatAppMsgAdapter.this.playVideo(position);
                    return false;
                }
            });

            holder.imageSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatAppMsgAdapter.this.showImage(position);
                }
            });

            holder.imageResive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatAppMsgAdapter.this.showImage(position);
                }
            });

            //long click to delete message


            //پیام های متنی قابلیت ویرایش و حذف و غیر متنی ها قابلیت حذف داشته باشند
            holder.rightMsgLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    holder.rightMsgLayout.setBackgroundColor(Color.rgb(141, 236, 250));
                    ShowFragmentMessageCommands.typeToCommand = ShowFragmentMessageCommands.MsgTypeToCommand.SentTextMsg;
                    ShowFragmentMessageCommands.messageData = new CmdMessageData(position, "متنی", holder.rightMsgLayout, msgDto);
                    final ShowFragmentMessageCommands tv = new ShowFragmentMessageCommands();
                    tv.show(ActivityChat.fm, "TV_tag");
                    // DeleteMessage(position, "متنی",holder.rightMsgLayout);
                    return false;

                }
            });


            holder.rightImgLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    holder.rightMsgLayout.setBackgroundColor(Color.rgb(141, 236, 250));
                    ShowFragmentMessageCommands.typeToCommand = ShowFragmentMessageCommands.MsgTypeToCommand.SentNoTextMsg;
                    ShowFragmentMessageCommands.messageData = new CmdMessageData(position, "تصویری", holder.rightImgLayout, msgDto);
                    final ShowFragmentMessageCommands tv = new ShowFragmentMessageCommands();
                    tv.show(ActivityChat.fm, "TV_tag");
                    //TransportClass.DeleteMessage(position, "تصویری",holder.rightImgLayout);
                    return false;

                }
            });

            holder.rightVideoLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    //TransportClass.DeleteMessage(position, "ویدئویی", holder.rightVideoLayout);
                    ChatAppMsgAdapter.this.OpenCommandWindow(holder, position, msgDto, ShowFragmentMessageCommands.MsgTypeToCommand.SentNoTextMsg, "ویدئویی", holder.rightVideoLayout);
                    return false;

                }
            });

            holder.rightVoiceLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
           /* ShowFragmentMessageCommands.messageData=new CmdMessageData(position, "صوتی",holder.rightMsgLayout);
            final ShowFragmentMessageCommands tv = new ShowFragmentMessageCommands();
            tv.show(ActivityChat.fm, "TV_tag");*/

                    // TransportClass.DeleteMessage(position, "صوتی", holder.rightVoiceLayout);

                    ChatAppMsgAdapter.this.OpenCommandWindow(holder, position, msgDto, ShowFragmentMessageCommands.MsgTypeToCommand.SentNoTextMsg, "صوتی", holder.rightVoiceLayout);

                    return false;

                }
            });





            holder.leftMsgLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    ChatAppMsgAdapter.this.OpenCommandWindow(holder, position, msgDto, ShowFragmentMessageCommands.MsgTypeToCommand.ReceiveMsg, "متنی", holder.leftMsgLayout);
                    return false;

                }
            });
            holder.leftImgLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    ChatAppMsgAdapter.this.OpenCommandWindow(holder, position, msgDto, ShowFragmentMessageCommands.MsgTypeToCommand.ReceiveMsg, "تصویری", holder.leftImgLayout);
                    return false;

                }
            });
            holder.leftVideoLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    ChatAppMsgAdapter.this.OpenCommandWindow(holder, position, msgDto, ShowFragmentMessageCommands.MsgTypeToCommand.ReceiveMsg, "ویدئویی", holder.leftVideoLayout);
                    return false;

                }
            });
            holder.leftVoiceLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    ChatAppMsgAdapter.this.OpenCommandWindow(holder, position, msgDto, ShowFragmentMessageCommands.MsgTypeToCommand.ReceiveMsg, "صوتی", holder.leftVoiceLayout);
                    return false;

                }
            });
        }catch (Exception e)
        {
            Analytics.trackEvent("ChatAppMsgAdapter_" + "onBindViewHolder " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());
            new CommonClass().ShowToast(ActivityChat.context,e.getMessage(), Toast.LENGTH_SHORT);
        }

    }

    private void OpenCommandWindow(ChatAppMsgViewHolder holder,int position,ChatAppMsgDTO msgDto,ShowFragmentMessageCommands.MsgTypeToCommand typeToCommand,String type,View view)
    {
        holder.rightMsgLayout.setBackgroundColor(Color.rgb(141, 236, 250));
        ShowFragmentMessageCommands.typeToCommand=typeToCommand;
        ShowFragmentMessageCommands.messageData=new CmdMessageData(position, type,view,msgDto);
        final ShowFragmentMessageCommands tv = new ShowFragmentMessageCommands();
        tv.show(ActivityChat.fm, "TV_tag");
    }

    @NonNull
    @Override
    public ChatAppMsgViewHolder onCreateViewHolder(@Nullable ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = null;
        if (parent != null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = null;
        if (layoutInflater != null) {
            view = layoutInflater.inflate(R.layout.list_item_message, parent, false);
        }
        return new ChatAppMsgViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (msgDtoList == null) {
            msgDtoList = new ArrayList<ChatAppMsgDTO>();
        }
        return msgDtoList.size();
    }

    private void playVoice(int position, ChatAppMsgViewHolder holder) {
        try {

            if (mediaPlayer != null)
                mediaPlayer.stop();
            //گرفتن ادرس فایل براساس مکانش در ریسایکلرویو
            List<Messages> messages = new DoCommand_MessageDB(ActivityChat.context).getListOfMessages("MsgPos = " + position + " and DriverID = '" + driverID + "' and OperatorID = '" + ActivityChat.id + "'");
            if (messages.size() != 0) {
                int isSend = messages.get(0).getIsSend();
                String fileName = "";
                String locpath = messages.get(0).getLocalPath();
                if (locpath.length() == 0)
                    locpath = CommonClass.FilesPath + "/" + messages.get(0).getMsgContent();
                if (messages.get(0).getLocalPath().length() != 0) {
                    fileName = locpath.substring(locpath.lastIndexOf("/") + 1);
                    File f = new File(messages.get(0).getLocalPath());
                    //اگر فایل را داریم پخش کن در غیراینصورت دانلود و سپس پخش کن
                    if (f.exists()) {
                        mediaPlayer = MediaPlayer.create(ActivityChat.context, Uri.parse(messages.get(0).getLocalPath()));
                        if (mediaPlayer != null)
                            mediaPlayer.start();
                    } else {

                        Download download = new Download();
                        if (fileName.length() != 0)
                            download.DownloadAPI(fileName, isSend == 1 ? holder.VoiceSendName : holder.VoiceReciveName, 3);
                        //message type 3 is voice
                    }
                }

            }
        }catch (Exception e)
        {
            Analytics.trackEvent("ChatAppMsgAdapter_" + "playVoice " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());
            new CommonClass().ShowToast(ActivityChat.context,e.getMessage(), Toast.LENGTH_SHORT);
        }

    }


    private void pauseVoice(int position) {
        if (mediaPlayer != null)
            mediaPlayer.stop();
    }

    private void playVideo(int position) {

        try {
            String locPath = "";
            ActivityVideo.videoPos = position;
            List<Messages> messages = new DoCommand_MessageDB(ActivityChat.context).getListOfMessages("MsgPos = " + position + " and DriverID = '" + driverID + "' and OperatorID = '" + ActivityChat.id + "'");
            if (messages.size() != 0)
                locPath = messages.get(0).getLocalPath();
            if (locPath.length() == 0)
                locPath = CommonClass.FilesPath + "/" + messages.get(0).getMsgContent();
            int msgType = messages.get(0).getMsgType();
            //اگر نوع پیام ویدئو هست و مسیر داره بره برای بارگذاری و پخش
            if (msgType == 2 && !locPath.equals("")) {
                Intent intent = new Intent(ActivityChat.context, ActivityVideo.class);
                intent.putExtra("Video", locPath);
                ActivityChat.context.startActivity(intent);
            }
        }catch (Exception e)
        {
            Analytics.trackEvent("ChatAppMsgAdapter_" + "playVideo " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());
            new CommonClass().ShowToast(ActivityChat.context,e.getMessage(), Toast.LENGTH_SHORT);
        }


    }

    private void showImage(int position) {

        try {
            String locPath = "";
            ActivityImage.ImagePos = position;
            List<Messages> messages = new DoCommand_MessageDB(ActivityChat.context).getListOfMessages("MsgPos = " + position + " and DriverID = '" + driverID + "' and OperatorID = '" + ActivityChat.id + "'");
            if (messages.size() != 0) {
                locPath = messages.get(0).getLocalPath();
                //چون برای فایل ها متن پیام، نام و نوع فایل است اگر مسیر نداشتیم میتوانیم از آن استفاده کنیم
                if (locPath.length() == 0)
                    locPath = CommonClass.FilesPath + "/" + messages.get(0).getMsgContent();
                int msgType = messages.get(0).getMsgType();
                if (msgType == 1) {
                    //مسیر را به صفحه دانلود و نمایش عکس هدایت میکنیم
                    Intent intent = new Intent(ActivityChat.context, ActivityImage.class);
                    intent.putExtra("picture",/* messages.get(0).getImage()*/locPath);
                    ActivityChat.context.startActivity(intent);

                }
            }
        }catch (Exception e)
        {
            Analytics.trackEvent("ChatAppMsgAdapter_" + "showImage " + driverID + "_" + CommonClass.GetCurrentMDate() + "_"+DeviceProperty+"_"  + e.getMessage());
            new CommonClass().ShowToast(ActivityChat.context,e.getMessage(), Toast.LENGTH_SHORT);
        }


    }

    public void clear() {
        int size = msgDtoList.size();
        msgDtoList.clear();
        notifyItemRangeRemoved(0, size);
    }


   /* public void searchedText(String txtSearch) {
        ChatAppMsgDTO msgDTO;
        if (txtSearch.length() != 0) {
            for (int i = 0; i < msgDtoList.size(); i++) {
                if (msgDtoList.get(i).getMsgContent().contains(txtSearch)) {
                    msgDTO = new ChatAppMsgDTO(msgDtoList.get(i).getMsgContent(), true, msgDtoList.get(i).getMsgType(), msgDtoList.get(i).getOperatorName(), msgDtoList.get(i).getTime());
                    msgDtoList.set(i, msgDTO);
                    notifyItemChanged(i);
                }
            }
        }
    }*/



}
