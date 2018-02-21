package com.nschirmer.pomodoro.adapter.historylist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nschirmer.pomodoro.R;
import com.nschirmer.pomodoro.model.PomodoroTask;

import java.util.ArrayList;
import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter {

    private List<PomodoroTask> pomodoroTasks = new ArrayList<>();
    private Context context;


    public HistoryListAdapter(Context context, List<PomodoroTask> pomodoroTasks){
        this.pomodoroTasks = pomodoroTasks;
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {

        // TODO
        if(position == 0) return 0;

        return 1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (HistoryListViewType.values()[viewType]){
            case HEADER:
                return new ViewHolderHistoryListHeader(LayoutInflater.from(context).inflate(R.layout.fragment_history_list_item_header, parent, false));

            default:
                return new ViewHolderHistoryListTask(LayoutInflater.from(context).inflate(R.layout.fragment_history_list_item, parent, false));
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (HistoryListViewType.values()[holder.getItemViewType()]){
            case HEADER:
                break;

            default:
                break;
        }
    }


    @Override
    public int getItemCount() {
        // TODO calcular quantidade de headers
        return pomodoroTasks.size() +1;
    }
}
