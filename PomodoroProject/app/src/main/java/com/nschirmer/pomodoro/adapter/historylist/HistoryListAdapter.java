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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderHistoryListTask(LayoutInflater.from(context).inflate(R.layout.fragment_history_list_item, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        
    }


    @Override
    public int getItemCount() {
        return pomodoroTasks.size();
    }
}
