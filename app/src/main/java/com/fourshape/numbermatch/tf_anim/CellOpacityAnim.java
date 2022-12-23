package com.fourshape.numbermatch.tf_anim;

import com.fourshape.numbermatch.listeners.OnCellOpacityAnimationListener;

public class CellOpacityAnim {

    private static final String TAG = "CellOpacityAnim";
    private static final int[] cellOpacityAnimationArr = new int[] {
            255, 235, 215, 195, 175, 155, 135, 115, 95, 75, 55, 35, 15, 35, 55, 75, 95, 115, 135, 155, 175, 195, 215, 235, 255
    };
    private static final float FACTOR = 0.8f;
    private static final float INIT_VAL = 0.1f;
    private boolean lockAnim;
    private float value = INIT_VAL;
    private OnCellOpacityAnimationListener onCellOpacityAnimationListener;

    public CellOpacityAnim () {
        reset ();
    }

    public void setOnCellOpacityAnimationListener(OnCellOpacityAnimationListener onCellOpacityAnimationListener) {
        this.onCellOpacityAnimationListener = onCellOpacityAnimationListener;
    }

    public void unlock () {
        lockAnim = false;
    }

    public void lock () {
        lockAnim = true;
    }

    public boolean isAnimUnlocked () {
        return !lockAnim;
    }

    public void run () {

        if (lockAnim)
            return;

        value += FACTOR;

        if (value >= 25) {
            lockAnim = true;
            if (onCellOpacityAnimationListener != null) {
                onCellOpacityAnimationListener.onFinish();
            }
        }

    }

    public int getValue() {

        try {
            return cellOpacityAnimationArr[(int) value];
        } catch (Exception e) {
            return 255;
        }

    }

    public void reset () {
        value = INIT_VAL;
        lockAnim = true;
    }

}
