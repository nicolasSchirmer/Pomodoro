package com.nschirmer.pomodoro.service;


import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.nschirmer.pomodoro.R;
import com.nschirmer.pomodoro.db.HelperDB;
import com.nschirmer.pomodoro.model.PomodoroTask;
import com.nschirmer.pomodoro.util.Dictionary;
import com.nschirmer.pomodoro.util.Utils;

import java.sql.Timestamp;

import static com.nschirmer.pomodoro.util.Dictionary.*;

public class CountdownService extends Service {

    private PomodoroTask pomodoroTask = new PomodoroTask();
    private CountDownTimer countDownTimer;
    private boolean hasSaved = false, isInterval, onDestroy = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // get the task title
        pomodoroTask.setTitle(intent.getStringExtra(SERVICE_COUNTDOWN_INTENT_TITLE));

        // get the max time of the task
        long maxTimeMilli = intent.getLongExtra(
                SERVICE_COUNTDOWN_INTENT_MAXTIME,
                Utils.minutesToMilliseconds(DEFAULT_TASK_MINUTES_MAXTIME)
        );

        isInterval = intent.getBooleanExtra(
                Dictionary.SERVICE_COUNTDOWN_INTENT_NEED_INTERVAL,
                false
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
                if(! isInterval){
                    pomodoroTask.setTimeSpentDoing(pomodoroTask.getTaskMaxTime() - millisSpent);

                    intentForActivity.putExtra(
                            SERVICE_COUNTDOWN_INTENT_TICK,
                            millisSpent
                    );

                    sendBroadcast(intentForActivity);
                }
            }

            @Override
            public void onFinish() {
                pomodoroTask.setWhenEnded(new Timestamp(System.currentTimeMillis()));

                if(isInterval && !onDestroy){
                    Utils.pushNotification(CountdownService.this,
                            getString(R.string.notification_interval_over_title),
                            getString(R.string.notification_interval_over_content),
                            0
                    );

                } else if (pomodoroTask.hasValidTaskToSave() && !hasSaved) {
                    HelperDB.saveIntoDB(pomodoroTask);
                    hasSaved = true;

                    if(! onDestroy) {
                        if (pomodoroTask.isCompleted()) {
                            Utils.pushNotification(CountdownService.this,
                                    getString(R.string.notification_ended_title),
                                    getString(R.string.notification_ended_content),
                                    0
                            );

                        } else {
                            Utils.pushNotification(CountdownService.this,
                                    getString(R.string.notification_paused_title),
                                    getString(R.string.notification_paused_content),
                                    0
                            );
                        }
                    }
                }

                intentForActivity.putExtra(SERVICE_COUNTDOWN_INTENT_FINISHED, true);
                intentForActivity.putExtra(SERVICE_COUNTDOWN_INTENT_NEED_INTERVAL, isInterval);
                sendBroadcast(intentForActivity);

                if(countDownTimer != null) countDownTimer.cancel();
            }
        }.start();
    }


    @Override
    public void onDestroy() {
        if(countDownTimer != null) {
            onDestroy = true;
            countDownTimer.onFinish();
        }

        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
