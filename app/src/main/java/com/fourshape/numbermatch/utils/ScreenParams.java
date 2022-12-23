package com.fourshape.numbermatch.utils;

import android.content.Context;

public class ScreenParams {

    private static final String TAG = "ScreenParams";

    public static float getDisplayDensity (Context context) {

        if (context != null)
            return context.getResources().getDisplayMetrics().density;
        else {
            MakeLog.error(TAG, "NULL Context in DisplayDensity");
            return 0.0f;
        }

    }

    public static int getDisplayWidthPixels (Context context) {

        if (context != null)
            return context.getResources().getDisplayMetrics().widthPixels;
        else {
            MakeLog.error(TAG, "NULL Context in WidthPixels");
            return 0;
        }

    }

    public static int getDisplayHeightPixels (Context context) {

        if (context != null) {
            return context.getResources().getDisplayMetrics().heightPixels;
        } else {
            MakeLog.error(TAG, "NULL Context in HeightPixels");
            return 0;
        }

    }

}
