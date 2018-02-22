package com.nschirmer.pomodoro.adapter.historylist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nschirmer.pomodoro.R;

public class ViewHolderHistoryListTask extends RecyclerView.ViewHolder {

    private TextView title, timeSpent, timeAgo, state;

    ViewHolderHistoryListTask(View view){
        super(view);

        title = (TextView) view.findViewById(R.id.history_list_item_title);
        timeSpent = (TextView) view.findViewById(R.id.history_list_item_time_spent);
        timeAgo = (TextView) view.findViewById(R.id.history_list_item_timeago);
        state = (TextView) view.findViewById(R.id.history_list_item_state);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getTimeSpent() {
        return timeSpent;
    }

    public TextView getTimeAgo() {
        return timeAgo;
    }

    public TextView getStatus() {
        return state;
    }
}
