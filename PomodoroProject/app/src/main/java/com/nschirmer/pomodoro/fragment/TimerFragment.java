package com.nschirmer.pomodoro.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nschirmer.pomodoro.R;
import com.nschirmer.pomodoro.service.CountdownService;
import com.nschirmer.pomodoro.util.Dictionary;
import com.nschirmer.pomodoro.util.Utils;

import at.grabner.circleprogress.CircleProgressView;

public class TimerFragment extends Fragment {

    private CircleProgressView circleProgressView;
    private ImageView playPauseButton, cancelButton;
    private EditText titleEdit;
    private TextView titleText;
    private Intent countDownServiceIntent;
    private boolean isRunning = false;
    private String taskTitle;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        countDownServiceIntent = new Intent(getContext(), CountdownService.class);

        circleProgressView = (CircleProgressView) view.findViewById(R.id.timer_circleView);
        playPauseButton = (ImageView) view.findViewById(R.id.timer_control_play_pause);
        playPauseButton.setOnClickListener(buttonsControlClickListener);
        cancelButton = (ImageView) view.findViewById(R.id.timer_control_cancel);
        cancelButton.setOnClickListener(buttonsControlClickListener);

        titleEdit = (EditText) view.findViewById(R.id.timer_editText);
        titleText = (TextView) view.findViewById(R.id.timer_title);

        String maxTimeFormatted = Utils.millisecondsToFormattedString(Utils.getMaxMilliTaskTime(getActivity()));
        circleProgressView.setText(maxTimeFormatted);
    }


    private View.OnClickListener buttonsControlClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.timer_control_play_pause:
                    if(isRunning){
                        stopCountDown(false);

                    } else{
                        startCountDown();
                    }
                    break;

                case R.id.timer_control_cancel:
                    stopCountDown(true);
                    break;
            }
        }
    };


    private void startCountDown(){
        isRunning = true;
        cancelButton.setVisibility(View.VISIBLE);
        playPauseButton.setImageResource(R.drawable.ic_pause);
        circleProgressView.setTextColor(ContextCompat.getColor(getContext(), R.color.progress_text_red));
        String title = titleEdit.getText().toString();
        titleText.setText(title.isEmpty() ? getString(R.string.timer_title_default) : title);
        taskTitle = titleText.getText().toString();
        titleText.setVisibility(View.VISIBLE);
        titleEdit.setVisibility(View.GONE);
        Utils.hideKeyboard(getContext(), titleEdit);

        startCountdownService();
    }


    // receive the time update from the countdownService
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras() != null){
                updateTimer(intent.getLongExtra(
                        Dictionary.SERVICE_COUNTDOWN_INTENT_TICK,
                        Utils.getMaxMilliTaskTime(getActivity()))
                );

                if(intent.getBooleanExtra(Dictionary.SERVICE_COUNTDOWN_INTENT_FINISHED, false)){
                    stopCountDown(false);
                }
            }
        }
    };


    private void stopCountDown(boolean canceled){
        isRunning = false;

        getActivity().stopService(countDownServiceIntent);

        cancelButton.setVisibility(View.GONE);
        playPauseButton.setImageResource(R.drawable.ic_play);
        circleProgressView.setValue(0);
        circleProgressView.setTextColor(ContextCompat.getColor(getContext(), R.color.progress_text_gray));
        titleText.setVisibility(View.GONE);
        titleEdit.setVisibility(View.VISIBLE);
        titleEdit.setText("");

        String maxTimeFormatted = Utils.millisecondsToFormattedString(Utils.getMaxMilliTaskTime(getActivity()));
        circleProgressView.setText(maxTimeFormatted);
        circleProgressView.setValue(0);

        stopCountdownService();

        if(canceled){
            Toast.makeText(getContext(), R.string.timer_canceled, Toast.LENGTH_SHORT).show();
            //stopCountdownService(); // TODO
        }
    }


    private void updateTimer(long milliseconds){
        circleProgressView.setText(Utils.millisecondsToFormattedString(milliseconds));
        circleProgressView.setValue(Utils.getPercentageOfTimeSpent(getActivity(), milliseconds));
    }


    private void startCountdownService(){
        countDownServiceIntent.putExtra(Dictionary.SERVICE_COUNTDOWN_INTENT_TITLE , taskTitle);
        countDownServiceIntent.putExtra(Dictionary.SERVICE_COUNTDOWN_INTENT_MAXTIME, Utils.getMaxMilliTaskTime(getActivity()));
        getActivity().startService(countDownServiceIntent);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Dictionary.SERVICE_COUNTDOWN_TAG));
    }


    private void stopCountdownService(){
        try {
            getActivity().unregisterReceiver(broadcastReceiver);
            getActivity().stopService(countDownServiceIntent);
        } catch (Exception e){}
    }


    @Override
    public void onDestroy() {
        stopCountdownService();
        Utils.hideKeyboard(getContext(), titleEdit);
        super.onDestroy();
    }
}
