package com.tahlilgargroup.androidchatlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.List;

public class EditMessageView extends FrameLayout {

    private ImageButton BtnCancelEdit;
    private View BtnOkEdit;
    private EditText txtMessage;

    public EditMessageView(@NonNull Context context) {
        super(context);
        init();
    }

    public EditMessageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditMessageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

   private void init()
   {
       View view = inflate(getContext(), R.layout.edit_message_tool, null);
       addView(view);

       BtnOkEdit= view.findViewById(R.id.BtnOkEditMsg);
       BtnCancelEdit=view.findViewById(R.id.BtnCancelEditMsg);
       txtMessage=view.findViewById(R.id.txtEditMessage);
   }

    public View getBtnOkEdit() {
        return BtnOkEdit;
    }

    public void setBtnOkEdit(View btnOkEdit) {
        BtnOkEdit = btnOkEdit;
    }

    public ImageButton getBtnCancelEdit() {
        return BtnCancelEdit;
    }

    public void setBtnCancelEdit(ImageButton btnCancelEdit) {
        BtnCancelEdit = btnCancelEdit;
    }

    public EditText getTxtMessage() {
        return txtMessage;
    }

    public void setTxtMessage(EditText txtMessage) {
        this.txtMessage = txtMessage;
    }

    public void EditMessage(ChatAppMsgAdapter chatAppMsgAdapter, List<ChatAppMsgDTO> chatAppMsgDTOList)
    {
        BtnOkEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtMessage.setText("");
                ActivityChat.audioRecordView.setVisibility(View.VISIBLE);
                ActivityChat.editMessageView.setVisibility(View.GONE);
            }
        });
    }
}
