package com.nschirmer.pomodoro;

import android.app.Application;

import com.pacoworks.rxpaper2.RxPaperBook;

public class ApplicationInit extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // init the database instance
        RxPaperBook.init(this);
    }
}
