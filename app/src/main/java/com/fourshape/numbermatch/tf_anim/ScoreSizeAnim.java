package com.fourshape.numbermatch.tf_anim;

import com.fourshape.numbermatch.listeners.OnScoreSizeAnimListener;

public class ScoreSizeAnim {

    private static final String TAG = "ScoreSizeAnim";

    private static final float FACTOR = 0.015f;
    private static final float INIT_VAL = 0.0f;
    private static final float FINAL_VAL = 0.25f;
    private static final int OPACITY_INIT_VAL = 255;
    private float currentVal = 0.0f;
    private boolean lockAnim;
    private OnScoreSizeAnimListener onScoreSizeAnimListener;
    private int scoreValue;

    public ScoreSizeAnim () {
        reset();
    }

    public void setOnScoreSizeAnimListener(OnScoreSizeAnimListener onScoreSizeAnimListener) {
        this.onScoreSizeAnimListener = onScoreSizeAnimListener;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public float getValueForSize() {
        return currentVal;
    }

    public boolean isAnimationUnlocked () {
        return !lockAnim;
    }

    public void unlock () {
        lockAnim = false;
    }

    public void lock () {
        lockAnim = true;
    }

    public int getValueForOpacity () {
        int temp =  (int)((currentVal/FINAL_VAL) * OPACITY_INIT_VAL);
        return OPACITY_INIT_VAL - temp < OPACITY_INIT_VAL*0.45 ? OPACITY_INIT_VAL-temp : OPACITY_INIT_VAL ;
    }

    public void run () {

        if (lockAnim)
            return;

        currentVal += FACTOR;

        if (onScoreSizeAnimListener != null && currentVal > FINAL_VAL) {
            lock();
            onScoreSizeAnimListener.onEnd();
        }

    }

    public void reset () {
        currentVal = INIT_VAL;
        lockAnim = true;
    }

}
