package com.nschirmer.pomodoro.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.concurrent.TimeUnit;

public class Utils {

    public static void hideKeyboard(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static long minutesToMilliseconds(int minutes){
        return TimeUnit.MINUTES.toMillis(minutes);
    }

}
