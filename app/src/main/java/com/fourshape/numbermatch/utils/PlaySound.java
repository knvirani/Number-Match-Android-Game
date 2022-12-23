package com.fourshape.numbermatch.utils;

import android.media.SoundPool;

public class PlaySound {

    public static void now (SoundPool soundPool, int soundId) {

        if (LiveGameSettings.GAME_SOUND) {
            if (soundPool != null && soundId != -1) {
                soundPool.play(soundId, 1,1,1,0, 1.0f);
            }
        }

    }

}
