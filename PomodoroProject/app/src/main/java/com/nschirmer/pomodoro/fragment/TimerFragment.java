package com.nschirmer.pomodoro.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nschirmer.pomodoro.R;

import at.grabner.circleprogress.CircleProgressView;

public class TimerFragment extends Fragment {

    private CircleProgressView circleProgressView;
    private ImageView playPauseButton, cancelButton;


    boolean isRunning = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        circleProgressView = (CircleProgressView) view.findViewById(R.id.timer_circleView);
        playPauseButton = (ImageView) view.findViewById(R.id.timer_control_play_pause);
        playPauseButton.setOnClickListener(buttonsControlClickListener);
        cancelButton = (ImageView) view.findViewById(R.id.timer_control_cancel);
        cancelButton.setOnClickListener(buttonsControlClickListener);
    }


    private View.OnClickListener buttonsControlClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.timer_control_play_pause:
                    if(isRunning){
                        stopCountDown(true);

                    } else{
                        startCountDown();
                    }
                    break;

                case R.id.timer_control_cancel:
                    stopCountDown(false);
                    break;
            }
        }
    };


    private void startCountDown(){
        isRunning = true;
        cancelButton.setVisibility(View.VISIBLE);
        playPauseButton.setImageResource(R.drawable.ic_pause);
        circleProgressView.setValue(100);
        circleProgressView.setTextColor(ContextCompat.getColor(getContext(), R.color.progress_text_red));
    }


    private void stopCountDown(boolean saveIntoDatabase){
        isRunning = false;
        cancelButton.setVisibility(View.GONE);
        playPauseButton.setImageResource(R.drawable.ic_play);
        circleProgressView.setValue(0);
        circleProgressView.setTextColor(ContextCompat.getColor(getContext(), R.color.progress_text_gray));
    }


}
