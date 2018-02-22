package com.nschirmer.pomodoro.adapter.historylist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nschirmer.pomodoro.R;
import com.nschirmer.pomodoro.model.PomodoroTask;
import com.nschirmer.pomodoro.util.Utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter {

    private List<Object> pomodoroTasksWithHeader = new ArrayList<>();
    private Context context;


    public HistoryListAdapter(Context context, List<Object> pomodoroTasksWithHeaders){
        this.pomodoroTasksWithHeader = pomodoroTasksWithHeaders;
        this.context = context;
    }


    @Override
    public int getItemViewType(int position) {
        // if is header (just a String) the view type is 0 = HEADER
        // if is the pomodoroTask object the view type is 1
        return pomodoroTasksWithHeader.get(position) instanceof String ? 0 : 1;
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
                ViewHolderHistoryListHeader viewHeader = (ViewHolderHistoryListHeader) holder;

                String title = (String) pomodoroTasksWithHeader.get(position);
                viewHeader.getHeaderTitle().setText(title);

                break;

            default:
                PomodoroTask pomodoroTask = (PomodoroTask) pomodoroTasksWithHeader.get(position);

                ViewHolderHistoryListTask view = (ViewHolderHistoryListTask) holder;
                view.getTitle().setText(pomodoroTask.getTitle());
                view.getTimeSpent().setText(Utils.millisecondsToFormattedString(pomodoroTask.getTimeSpentDoing()));
                view.getStatus().setText(pomodoroTask.isCompleted() ?
                        context.getString(R.string.history_list_item_status_completed) :
                        context.getString(R.string.history_list_item_status_stopped)
                );

                Timestamp whenEnded = pomodoroTask.getWhenEnded();
                String dateStringWhenEnded = Utils.getTimeAgo(whenEnded);

                view.getTimeAgo().setText(dateStringWhenEnded);

                break;
        }
    }


    @Override
    public int getItemCount() {
        return pomodoroTasksWithHeader.size();
    }
}
