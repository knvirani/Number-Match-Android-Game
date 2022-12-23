package com.fourshape.numbermatch.utils;

import android.content.Context;

import com.fourshape.easythingslib.utils.CommonSharedData;

public class LiveGameSettings {

    public static boolean GAME_TIME = true;
    public static boolean GAME_SCORE = true;
    public static boolean GAME_SOUND = true;
    public static boolean GAME_VIBRATION = true;
    public static boolean ADS_SOUND_STATUS = true;

    public static void update (Context context) {

        SharedData sharedData = new SharedData(context);

        GAME_TIME = sharedData.getGameTimeStatus();
        GAME_SCORE = sharedData.getGameScoreStatus();

        CommonSharedData commonSharedData = new CommonSharedData(context, SharedData.SHARED_PREF_TITLE);

        GAME_VIBRATION = commonSharedData.getGameVibrationStatus();
        GAME_SOUND = commonSharedData.getGameSoundStatus();

    }

}
