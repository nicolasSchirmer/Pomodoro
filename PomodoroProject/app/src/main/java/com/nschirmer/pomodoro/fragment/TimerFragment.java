package com.nschirmer.pomodoro.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nschirmer.pomodoro.R;
import com.nschirmer.pomodoro.util.Utils;

import at.grabner.circleprogress.CircleProgressView;

public class TimerFragment extends Fragment {

    private CircleProgressView circleProgressView;
    private ImageView playPauseButton, cancelButton;
    private EditText titleEdit;
    private TextView titleText;


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

        titleEdit = (EditText) view.findViewById(R.id.timer_editText);
        titleText = (TextView) view.findViewById(R.id.timer_title);
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
        titleText.setText(titleEdit.getText().toString());
        titleText.setVisibility(View.VISIBLE);
        titleEdit.setVisibility(View.GONE);
        Utils.hideKeyboard(getContext(), titleEdit);
    }


    private void stopCountDown(boolean saveIntoDatabase){
        isRunning = false;
        cancelButton.setVisibility(View.GONE);
        playPauseButton.setImageResource(R.drawable.ic_play);
        circleProgressView.setValue(0);
        circleProgressView.setTextColor(ContextCompat.getColor(getContext(), R.color.progress_text_gray));
        titleText.setVisibility(View.GONE);
        titleEdit.setVisibility(View.VISIBLE);
        titleEdit.setText("");
    }


}
