package com.fourshape.numbermatch.utils;

import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class ActionSnackbar {

    public void show (View linkView, boolean positiveStatus, String text) {
        Snackbar snackbar = Snackbar.make( (View) linkView, text, Snackbar.LENGTH_SHORT);
        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }


}
