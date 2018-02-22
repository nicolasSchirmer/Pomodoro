package com.nschirmer.pomodoro.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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

import com.esotericsoftware.kryo.util.Util;
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

    private long intervalTimeMilli;
    private int pomodorosCount = 0;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        countDownServiceIntent = new Intent(getActivity(), CountdownService.class);

        circleProgressView = (CircleProgressView) view.findViewById(R.id.timer_circleView);
        playPauseButton = (ImageView) view.findViewById(R.id.timer_control_play_pause);
        playPauseButton.setOnClickListener(buttonsControlClickListener);
        cancelButton = (ImageView) view.findViewById(R.id.timer_control_cancel);
        cancelButton.setOnClickListener(buttonsControlClickListener);

        titleEdit = (EditText) view.findViewById(R.id.timer_editText);
        titleText = (TextView) view.findViewById(R.id.timer_title);

        String maxTimeFormatted = Utils.millisecondsToFormattedString(Utils.getMaxMilliTaskTime(getActivity()));
        circleProgressView.setText(maxTimeFormatted);

        if(! Utils.ableToPomodoro(getActivity())){
            disableControlButton();
            intervalTimeMilli = Utils.getSavedIntervalMilli(getActivity());
            pomodorosCount = Utils.getSavedPomodoroCount(getActivity());
            startCountdownService(intervalTimeMilli, true);

            Toast.makeText(getActivity(), getString(R.string.timer_still_penalty), Toast.LENGTH_SHORT).show();
        }
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
        circleProgressView.setTextColor(ContextCompat.getColor(getActivity(), R.color.progress_text_red));
        String title = titleEdit.getText().toString();
        titleText.setText(title.isEmpty() ? getString(R.string.timer_title_default) : title);
        taskTitle = titleText.getText().toString();
        titleText.setVisibility(View.VISIBLE);
        titleEdit.setVisibility(View.GONE);
        Utils.hideKeyboard(getActivity(), titleEdit);

        startCountdownService();

        pomodorosCount ++;
        setIntervalTime();
        Utils.savePenaltiesStatus(getActivity(), intervalTimeMilli, pomodorosCount);
    }


    private void setIntervalTime(){
        if(pomodorosCount == Dictionary.DEFAULT_TASK_MAX_COUNTER){
            intervalTimeMilli = Utils.minutesToMilliseconds(Dictionary.DEFAULT_TASK_MINUTES_LONGER_INTERVAL);

        } else {
            intervalTimeMilli = Utils.minutesToMilliseconds(Dictionary.DEFAULT_TASK_MINUTES_INTERVAL);
        }
    }


    private void disableControlButton(){
        playPauseButton.setClickable(false);
        Utils.setIconColor(getActivity(), playPauseButton, R.color.gray);
    }

    private void enableControlButton(){
        playPauseButton.setClickable(true);
        Utils.setIconColor(getActivity(), playPauseButton, R.color.colorPrimary);
    }


    private void clearPenalties(){
        intervalTimeMilli = 0;

        if(pomodorosCount == Dictionary.DEFAULT_TASK_MAX_COUNTER){
            pomodorosCount = 0;
        }

        Utils.savePenaltiesStatus(getActivity(), intervalTimeMilli, pomodorosCount);
    }


    // receive the time update from the countdownService
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras() != null){
                if(intent.getBooleanExtra(Dictionary.SERVICE_COUNTDOWN_INTENT_FINISHED, false)){
                    stopCountDown(false);

                    if(intent.getBooleanExtra(Dictionary.SERVICE_COUNTDOWN_INTENT_NEED_INTERVAL, false)){
                        enableControlButton();
                        clearPenalties();
                    }
                }

                updateTimer(intent.getLongExtra(
                        Dictionary.SERVICE_COUNTDOWN_INTENT_TICK,
                        Utils.getMaxMilliTaskTime(getActivity()))
                );
            }
        }
    };


    private void stopCountDown(boolean canceled){
        isRunning = false;

        getActivity().stopService(countDownServiceIntent);

        cancelButton.setVisibility(View.GONE);
        playPauseButton.setImageResource(R.drawable.ic_play);
        circleProgressView.setValue(0);
        circleProgressView.setTextColor(ContextCompat.getColor(getActivity(), R.color.progress_text_gray));
        titleText.setVisibility(View.GONE);
        titleEdit.setVisibility(View.VISIBLE);
        titleEdit.setText("");

        String maxTimeFormatted = Utils.millisecondsToFormattedString(Utils.getMaxMilliTaskTime(getActivity()));
        circleProgressView.setText(maxTimeFormatted);
        circleProgressView.setValue(0);

        stopCountdownService();

        if(canceled){
            Toast.makeText(getActivity(), R.string.timer_canceled, Toast.LENGTH_SHORT).show();
            //stopCountdownService(); // TODO
        }
    }


    private void updateTimer(long milliseconds){
        circleProgressView.setText(Utils.millisecondsToFormattedString(milliseconds));
        circleProgressView.setValue(Utils.getPercentageOfTimeSpent(getActivity(), milliseconds));
    }


    private void startCountdownService(){
        countDownServiceIntent.putExtra(Dictionary.SERVICE_COUNTDOWN_INTENT_TITLE , taskTitle);
        startCountdownService(Utils.getMaxMilliTaskTime(getActivity()), false);
    }

    private void startCountdownService(long maxTimeMilli, boolean needInterval){
        countDownServiceIntent.putExtra(Dictionary.SERVICE_COUNTDOWN_INTENT_MAXTIME, maxTimeMilli);
        countDownServiceIntent.putExtra(Dictionary.SERVICE_COUNTDOWN_INTENT_NEED_INTERVAL, needInterval);
        getActivity().startService(countDownServiceIntent);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Dictionary.SERVICE_COUNTDOWN_TAG));
    }


    private void stopCountdownService(){
        try {
            Utils.savePenaltiesStatus(getActivity(), intervalTimeMilli, pomodorosCount);
            getActivity().unregisterReceiver(broadcastReceiver);
            getActivity().stopService(countDownServiceIntent);

            // some services problem
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(! Utils.ableToPomodoro(getActivity())) {
                        disableControlButton();
                        Utils.pushNotification(getActivity(),
                                getString(R.string.notification_interval_title),
                                getString(R.string.notification_interval_content),
                                0
                        );
                        startCountdownService(intervalTimeMilli, true);
                    }
                }
            }, 100);


        } catch (Exception e){}
    }

    @Override
    public void onPause() {
        Utils.savePenaltiesStatus(getActivity(), intervalTimeMilli, pomodorosCount);
        Utils.hideKeyboard(getActivity(), titleEdit);

        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().stopService(countDownServiceIntent);

        super.onPause();
    }
}
