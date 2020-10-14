package com.tahlilgargroup.androidchatlibrary;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TellListViewHolder extends RecyclerView.ViewHolder {
    public CardView TellList_Crd;

    public TextView Telltxt;
    public TellListViewHolder(@NonNull View itemView) {
        super(itemView);
        if(itemView!=null)
        {
            TellList_Crd=itemView.findViewById(R.id.TicketFileCrd);

            Telltxt=itemView.findViewById(R.id.TicketfileNametxt);
        }
    }
}
