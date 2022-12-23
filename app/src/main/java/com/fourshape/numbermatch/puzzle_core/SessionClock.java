package com.fourshape.numbermatch.puzzle_core;

import android.os.Handler;

import com.fourshape.numbermatch.listeners.OnSessionClockTickListener;
import com.fourshape.numbermatch.utils.MakeLog;

public class SessionClock {

    private static final String TAG = "SessionClock";

    private static int seconds;

    private static boolean shouldLogTick = false;

    private static Handler handler;
    private static Runnable runnable;
    private static boolean isRunning = false;

    private static boolean shouldLog = true;

    private static OnSessionClockTickListener onSessionClockTickListener;

    public static void setOnSessionClockTickListener (OnSessionClockTickListener onSessionClockTickListener) {
        SessionClock.onSessionClockTickListener = onSessionClockTickListener;
    }

    public static void init () {
        seconds = 0;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                increaseSeconds();

                if (shouldLogTick) {
                    if (SessionClock.onSessionClockTickListener != null) {
                        SessionClock.onSessionClockTickListener.onTick(seconds);
                    }
                }

            }
        };
    }

    public static void start () {
        shouldLogTick = true;
        try {
            runnable.run();
        } catch (Exception e) {}
        if (shouldLog) {
            MakeLog.info(TAG, "onStart");
        }
    }

    public static void resume () {
        shouldLogTick = true;
        try {
            runnable.run();
        } catch (Exception e) {}
        if (shouldLog) {
            MakeLog.info(TAG, "onResume");
        }
    }

    public static void stop () {

        isRunning = false;

        if (handler != null && runnable != null)
        {
            try {
                handler.removeCallbacks(runnable);
            } catch (Exception e) {}
        }

        shouldLogTick = false;

        if (shouldLog) {
            MakeLog.info(TAG, "onStop");
        }

    }

    public static int getSeconds () {
        return seconds;
    }

    public static void resetClock () {

        isRunning = false;
        shouldLogTick = true;

        if (handler != null && runnable != null)
        {
            try {
                handler.removeCallbacks(runnable);
            } catch (Exception e) {}
        }

        seconds = 0;
        if (shouldLog) {
            MakeLog.info(TAG, "onReset");
        }

    }

    public static boolean isTerminated () {
        return handler == null || runnable == null;
    }

    public static void setSeconds (int seconds) {

        SessionClock.seconds = seconds;
        if (shouldLog) {
            MakeLog.info(TAG, "onSetSeconds: " + seconds + " secs");
        }

    }

    private static void increaseSeconds () {
        seconds++;

        try {
            handler.postDelayed(runnable, 1000);
        } catch (Exception e) {

        }

        if (shouldLog) {
            MakeLog.info(TAG, "onRun");
        }
        isRunning = true;
    }

    public static boolean isRunning () {
        return isRunning;
    }

    public static void terminate () {

        isRunning = false;

        if (handler != null && runnable != null)
            handler.removeCallbacks(runnable);

        shouldLogTick = false;
        seconds = 0;
        runnable = null;
        handler = null;
        if (shouldLog) {
            MakeLog.info(TAG, "onTerminate");
        }
    }


}
