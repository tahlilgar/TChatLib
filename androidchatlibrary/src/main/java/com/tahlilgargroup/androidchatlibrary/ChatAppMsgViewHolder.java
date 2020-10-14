package com.tahlilgargroup.androidchatlibrary;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


public class ChatAppMsgViewHolder extends RecyclerView.ViewHolder {

    public ConstraintLayout leftMsgLayout,rightMsgLayout,leftImgLayout,rightImgLayout,leftVideoLayout,rightVideoLayout,
    leftVoiceLayout,rightVoiceLayout,DateLay;

    public  TextView leftMsgTextView,rightMsgTextView,Operator_name,Timesend,TimeResive
            ,TimeImgsend,TimeImgResive,TimeVideosend,TimeVideoResive,TimeVoicesend
            ,TimeVoiceResive,VoiceReciveName,VoiceSendName,txtDate;

    public ImageView imageSend,imageResive,msg_send_seen,
            img_send_seen,video_send_seen
            ,voice_send_seen,Msg_Send_Edit,msg_receive_edit;
    public VideoView VideoSend,VideoResive;

    public ImageButton playSendVoice,playResiceVoice,pauseSendVoice,pauseReciveVoice;
    public SeekBar sendSeek,reciveSeek;


    public ChatAppMsgViewHolder(View itemView) {
        super(itemView);

        if(itemView!=null) {

            leftVoiceLayout=itemView.findViewById(R.id.resiveVoiceLay);
            rightVoiceLayout=itemView.findViewById(R.id.sendVoiceLay);

            TimeVoicesend=itemView.findViewById(R.id.text_Voice_time);
            TimeVoiceResive=itemView.findViewById(R.id.text_Voice_time1);

            VoiceReciveName=itemView.findViewById(R.id.txt_Voice_name1);
            VoiceSendName=itemView.findViewById(R.id.txt_Voice_name);

            playSendVoice=itemView.findViewById(R.id.play_Voice);
            pauseSendVoice=itemView.findViewById(R.id.pause_Voice);

            pauseReciveVoice=itemView.findViewById(R.id.pause_Voice1);
            playResiceVoice=itemView.findViewById(R.id.play_Voice1);





            leftMsgLayout       =   itemView.findViewById(R.id.resive);
            rightMsgLayout      =   itemView.findViewById(R.id.sendlay);
            leftMsgTextView     =   itemView.findViewById(R.id.text_message_body1);
            rightMsgTextView    =   itemView.findViewById(R.id.text_message_body);
            Operator_name       =   itemView.findViewById(R.id.text_message_name);
            TimeResive          =   itemView.findViewById(R.id.text_message_time1);
            Timesend            =   itemView.findViewById(R.id.text_message_time);


            leftImgLayout       =   itemView.findViewById(R.id.reciveImgLay);
            rightImgLayout      =   itemView.findViewById(R.id.sendImgLay);
            imageSend           =   itemView.findViewById(R.id.image_message_body);
            imageResive         =   itemView.findViewById(R.id.image_message_body1);
            TimeImgResive       =   itemView.findViewById(R.id.text_image_time1);
            TimeImgsend         =   itemView.findViewById(R.id.text_image_time);



            leftVideoLayout       =   itemView.findViewById(R.id.reciveVideoLay);
            rightVideoLayout      =   itemView.findViewById(R.id.sendVideoLay);
            TimeVideoResive       =   itemView.findViewById(R.id.text_Video_time1);
            TimeVideosend         =   itemView.findViewById(R.id.text_Video_time);
            VideoSend             =   itemView.findViewById(R.id.Video_message_body);
            VideoResive           =   itemView.findViewById(R.id.Video_message_body1);

            DateLay               =   itemView.findViewById(R.id.dateLay);
            txtDate               =   itemView.findViewById(R.id.txt_Date);

            msg_send_seen         =   itemView.findViewById(R.id.Img_send_msg_seen);

            img_send_seen         =   itemView.findViewById(R.id.Img_Send_img_seen);

            video_send_seen       =   itemView.findViewById(R.id.Img_Send_video_seen);

            voice_send_seen       =   itemView.findViewById(R.id.Img_Send_voice_seen);

            Msg_Send_Edit         =   itemView.findViewById(R.id.ImgSendMsgEdited);

            msg_receive_edit      =   itemView.findViewById(R.id.ImgReceiveMsgEdited);

        }
    }


}
