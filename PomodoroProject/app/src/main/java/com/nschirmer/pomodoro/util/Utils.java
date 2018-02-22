package com.nschirmer.pomodoro.util;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.nschirmer.pomodoro.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    public static void hideKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static long minutesToMilliseconds(int minutes){
        return TimeUnit.MINUTES.toMillis(minutes);
    }


    public static void saveMaxMilliTaskTime(Activity activity, long milliseconds){
        activity.getSharedPreferences(activity.getPackageName(), MODE_PRIVATE)
                .edit().putLong(Dictionary.SHAREDPREFERENCES_MAXTIME_MILLISECONDS, milliseconds)
                .apply();
    }


    public static long getMaxMilliTaskTime(Activity activity){
        return activity.getSharedPreferences(activity.getPackageName(), MODE_PRIVATE)
                .getLong(Dictionary.SHAREDPREFERENCES_MAXTIME_MILLISECONDS,
                        minutesToMilliseconds(Dictionary.DEFAULT_TASK_MINUTES_MAXTIME)
                );
    }


    public static String millisecondsToFormattedString(long milliseconds){
        return String.format(Locale.ENGLISH,
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }


    public static int getPercentageOfTimeSpent(Activity activity, long milliSpent){
        return (int) ((milliSpent * 100) / getMaxMilliTaskTime(activity));
    }


    public static String getPrettyDateFromTimestamp(Timestamp timestamp){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        return DateFormat.format("dd/MM/yyyy", cal).toString();
    }


    public static boolean isToday(Timestamp timestamp){
        return DateUtils.isToday(timestamp.getTime());
    }


    public static boolean isYesterday(Timestamp timestamp){
        return DateUtils.isToday(timestamp.getTime() + DateUtils.DAY_IN_MILLIS);
    }


    public static String getTimeAgo(Timestamp date){
        return String.valueOf(DateUtils.getRelativeTimeSpanString(date.getTime()));
    }


    public static void pushNotification(Context context, String title, String content, int versionID){
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, Dictionary.NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_app_round)
                        .setContentTitle(title)
                        .setContentText(content);

//        if(timeIsRunning){
//            notificationBuilder.addAction(R.drawable.ic_pause, context.getString(R.string.notification_pause), sIntent)
//        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null) {
            notificationManager.notify(versionID, notificationBuilder.build());

            vibrateForMilliseconds(context, 500);

        }
    }

    public static void vibrateForMilliseconds(Context context, long milliseconds){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if(vibrator != null) vibrator.vibrate(milliseconds);
    }
}
