package com.fourshape.customcalendarlib.listeners;

import android.view.View;

import com.fourshape.customcalendarlib.data_formats.DateData;

public abstract class OnDateClickListener {
    public static OnDateClickListener instance;
    public abstract void onDateClick(View view, DateData date);

}
