package com.fourshape.numbermatch.tf_anim;

import com.fourshape.numbermatch.listeners.OnBoardStartAnimationListener;

public class BoardBeginAnim {

    private static final String TAG = "BoardBeginAnim";
    private boolean lockAnim;
    private float counter = 50.0F;
    private static final float FACTOR = 1.5f;
    private OnBoardStartAnimationListener onBoardStartAnimationListener;

    public BoardBeginAnim () {
        reset();
    }

    public void setOnBoardStartAnimationListener(OnBoardStartAnimationListener onBoardStartAnimationListener) {
        this.onBoardStartAnimationListener = onBoardStartAnimationListener;
    }

    public void run () {

        if (lockAnim)
            return;

        counter -= FACTOR;

        if (counter < FACTOR) {
            lockAnim = true;
            if (onBoardStartAnimationListener != null) {
                reset();
                onBoardStartAnimationListener.onFinish();
            }
        }

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

    public float getValue () {
        return counter;
    }

    private void reset () {
        counter = 50.0f;
        lockAnim = true;
    }

}
