package com.nschirmer.pomodoro.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
}
