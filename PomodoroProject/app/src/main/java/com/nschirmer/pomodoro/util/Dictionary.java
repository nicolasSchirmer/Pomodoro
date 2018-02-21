package com.nschirmer.pomodoro.util;

public class Dictionary {

    // fragments identifiers
    public static final String FRAGMENT_TAG_TIMER = "fragment_timer_tag";
    public static final String FRAGMENT_TAG_HISTORY = "fragment_history_tag";


    // service broadcast identifier
    public static final String SERVICE_COUNTDOWN_TAG = "service_countdown_receiver";
    public static final String SERVICE_COUNTDOWN_INTENT_MAXTIME = "service_countdown_intent_maxtime";
    public static final String SERVICE_COUNTDOWN_INTENT_TITLE = "service_countdown_intent_title";
    public static final String SERVICE_COUNTDOWN_INTENT_TICK = "service_countdown_intent_activity_tick";


    // the default interval of the tick capability from CountDown class
    public static final long SERVICE_COUNTDOWN_TICK_INTERVAL = 1000; // 1 second


    // default minutes to pomodoro
    public static final int DEFAULT_TASK_MINUTES_MAXTIME = 25;


    // paper key helper
    public static final String PAPER_KEY_POMODOROTASK = "paper_key_pomodorotask_";
    public static final String BOOK_KEY_POMODOROTASK = "paper_book_key_pomodorotask_";

}
