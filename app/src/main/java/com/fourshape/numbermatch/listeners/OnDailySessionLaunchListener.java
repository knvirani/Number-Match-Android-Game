package com.fourshape.numbermatch.listeners;

import android.content.Intent;

public interface OnDailySessionLaunchListener {
    void onResume (Intent intent);
    void onRestart (Intent intent);
}
