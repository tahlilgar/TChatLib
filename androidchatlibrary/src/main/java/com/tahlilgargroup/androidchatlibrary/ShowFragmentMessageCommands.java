package com.tahlilgargroup.androidchatlibrary;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//نمایش لیست عملیات در آلرت دیالوگ

public class ShowFragmentMessageCommands extends DialogFragment {

    //fragment dialog for message commands (edit , delete ,..)
    RecyclerView rv;
    MessageCmdAdapter adapter;
    List<CmdMessageItem> CmdListModels = new ArrayList<>();

    public static CmdMessageData messageData;
    private static CmdMessageItem CmdName;
    int newMsgPosition;
    static Dialog dialog;

    public enum MsgTypeToCommand{SentTextMsg,ReceiveMsg,SentNoTextMsg}
    public static MsgTypeToCommand typeToCommand;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fraglayout, container,false);
        //RECYCER
        rv = rootView.findViewById(R.id.mRecyerID);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        dialog=getDialog();

        //ADAPTER
        adapter = new MessageCmdAdapter(CmdListModels);
        rv.setAdapter(adapter);

        //حذف برای هر نوع پیام ارسالی
        if(typeToCommand== MsgTypeToCommand.SentTextMsg || typeToCommand== MsgTypeToCommand.SentNoTextMsg)
        {

            CmdName =new CmdMessageItem(messageData,getString(R.string.Delete)) ;
            CmdListModels.add(CmdName);
            newMsgPosition = CmdListModels.size() - 1;
            adapter.notifyItemInserted(newMsgPosition);
            adapter.notifyDataSetChanged();
        }

        //ویرایش برای پیام متنی ارسالی
        if(typeToCommand== MsgTypeToCommand.SentTextMsg)
        {
            CmdName =new CmdMessageItem(messageData,getString(R.string.Edit)) ;
            CmdListModels.add(CmdName);
            newMsgPosition = CmdListModels.size() - 1;
            adapter.notifyItemInserted(newMsgPosition);
            adapter.notifyDataSetChanged();
        }


        //اشتراک گذاری برای همه
        CmdName =new CmdMessageItem(messageData,getString(R.string.Share)) ;
        CmdListModels.add(CmdName);
        newMsgPosition = CmdListModels.size() - 1;
        adapter.notifyItemInserted(newMsgPosition);
        adapter.notifyDataSetChanged();


        return rootView;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        //remove title place and set style programmatically
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lighgray2)));
        dialog.getWindow().setLayout(350, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //پیامی که انتخاب شده آبی شده اگه پنجره عملیات بدون انجام هیچ عملیاتی بسته بشه پیام باید به رنگ اصلی برگرده
        CmdName.getCmdMessageData().getMessageLayout().setBackgroundColor(Color.TRANSPARENT);
        dialog.cancel();

    }

}