package com.nschirmer.pomodoro.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nschirmer.pomodoro.R;
import com.nschirmer.pomodoro.adapter.historylist.HistoryListAdapter;
import com.nschirmer.pomodoro.db.HelperDB;
import com.nschirmer.pomodoro.model.PomodoroTask;
import com.nschirmer.pomodoro.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    RecyclerView historyList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        historyList = (RecyclerView) view.findViewById(R.id.history_list_recycler);

        List<PomodoroTask> pomodoroTasks = HelperDB.getAllPomodoroTasksFromDB();

        List<Object> pomodoroWithHeaders = organizeListWithHeaders(pomodoroTasks);

        historyList.setAdapter(new HistoryListAdapter(getContext(), pomodoroWithHeaders));
        historyList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }


    private List<Object> organizeListWithHeaders(List<PomodoroTask> pomodoroTasks){
        List<Object> listWithHeaders = new ArrayList<>();

        boolean hasTodayHeader = false, hasYesterdayHeader = false;

        for(PomodoroTask pomodoroTask : pomodoroTasks) {
            if(Utils.isToday(pomodoroTask.getWhenEnded()) && ! hasTodayHeader){
                listWithHeaders.add(getContext().getString(R.string.history_lit_item_today));
                hasTodayHeader = true;

            } else if(Utils.isYesterday(pomodoroTask.getWhenEnded()) && ! hasYesterdayHeader){
                listWithHeaders.add(getContext().getString(R.string.history_lit_item_yesterday));
                hasYesterdayHeader = true;

            } else if(! Utils.isYesterday(pomodoroTask.getWhenEnded()) && ! hasYesterdayHeader && ! hasTodayHeader) {
                listWithHeaders.add(Utils.getPrettyDateFromTimestamp(pomodoroTask.getWhenEnded()));
            }

            listWithHeaders.add(pomodoroTask);
        }

        return listWithHeaders;
    }
}
