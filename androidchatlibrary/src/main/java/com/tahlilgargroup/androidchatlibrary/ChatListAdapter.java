package com.tahlilgargroup.androidchatlibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

//////////////////////////chat operators list adaptor
public class ChatListAdapter extends ArrayAdapter<ChatListModel> {

    private final Context context;
    private final ArrayList<ChatListModel> modelsArrayList;

    //اداپتور لیست اپراتورها
    public ChatListAdapter(Context context, ArrayList<ChatListModel> modelsArrayList) {

        super(context, R.layout.list_item_chat, modelsArrayList);

        this.context = context;
        this.modelsArrayList = modelsArrayList;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater

        View rowView = null;
        rowView = Objects.requireNonNull(inflater).inflate(R.layout.list_item_chat, parent, false);

        // 3. Get icon,title & counter views from the rowView
        TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
        TextView counterView = (TextView) rowView.findViewById(R.id.item_counter);

        // 4. Set the text for textView
        titleView.setText(modelsArrayList.get(position).getTitle());
        counterView.setText(modelsArrayList.get(position).getCounter());



        // 5. retrn rowView
        return rowView;
    }
}
