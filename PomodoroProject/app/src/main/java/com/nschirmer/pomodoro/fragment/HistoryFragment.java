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
import com.nschirmer.pomodoro.model.PomodoroTask;

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

        List<PomodoroTask> tasks = new ArrayList<>();
        tasks.add(new PomodoroTask());
        tasks.add(new PomodoroTask());
        tasks.add(new PomodoroTask());
        tasks.add(new PomodoroTask());
        tasks.add(new PomodoroTask());
        tasks.add(new PomodoroTask());

        historyList.setAdapter(new HistoryListAdapter(getContext(), tasks));
        historyList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }
}
