package com.nschirmer.pomodoro.adapter.historylist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nschirmer.pomodoro.R;

public class ViewHolderHistoryListHeader extends RecyclerView.ViewHolder{

    private TextView headerTitle;

    ViewHolderHistoryListHeader(View itemView) {
        super(itemView);

        headerTitle = (TextView) itemView.findViewById(R.id.fragment_history_list_header_title);
    }

    public TextView getHeaderTitle() {
        return headerTitle;
    }
}
