package com.nschirmer.pomodoro.service;


import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.nschirmer.pomodoro.db.HelperDB;
import com.nschirmer.pomodoro.model.PomodoroTask;
import com.nschirmer.pomodoro.util.Utils;

import java.sql.Timestamp;

import static com.nschirmer.pomodoro.util.Dictionary.*;

public class CountdownService extends Service {

    private PomodoroTask pomodoroTask = new PomodoroTask();
    private CountDownTimer countDownTimer;
    private boolean hasSaved = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // get the task title
        pomodoroTask.setTitle(intent.getStringExtra(SERVICE_COUNTDOWN_INTENT_TITLE));

        // get the max time of the task
        long maxTimeMilli = intent.getLongExtra(
                SERVICE_COUNTDOWN_INTENT_MAXTIME,
                Utils.minutesToMilliseconds(DEFAULT_TASK_MINUTES_MAXTIME)
        );

        pomodoroTask.setTaskMaxTime(maxTimeMilli);

        startCountdown(maxTimeMilli);

        return super.onStartCommand(intent, flags, startId);
    }


    private void startCountdown(final long maxTimeMilli){
        // prepare the intent to send back the time spent
        final Intent intentForActivity = new Intent(SERVICE_COUNTDOWN_TAG);

        // the tick will be populated every 1 second
        countDownTimer = new CountDownTimer(maxTimeMilli, SERVICE_COUNTDOWN_TICK_INTERVAL) {
            @Override
            public void onTick(long millisSpent) {
                pomodoroTask.setTimeSpentDoing(pomodoroTask.getTaskMaxTime() - millisSpent);

                intentForActivity.putExtra(
                        SERVICE_COUNTDOWN_INTENT_TICK,
                        millisSpent
                );

                sendBroadcast(intentForActivity);
            }

            @Override
            public void onFinish() {
                pomodoroTask.setWhenEnded(new Timestamp(System.currentTimeMillis()));

                if(pomodoroTask.hasValidTaskToSave() && !hasSaved) {
                    HelperDB.saveIntoDB(pomodoroTask);
                    hasSaved = true;
                }

                intentForActivity.putExtra(SERVICE_COUNTDOWN_INTENT_FINISHED, true);
                sendBroadcast(intentForActivity);

                countDownTimer.cancel();
            }
        }.start();
    }


    @Override
    public void onDestroy() {
        if(countDownTimer != null) countDownTimer.onFinish();

        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
