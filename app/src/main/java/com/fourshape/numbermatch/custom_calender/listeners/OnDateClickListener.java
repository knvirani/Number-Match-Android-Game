package com.fourshape.numbermatch.custom_calender.listeners;

import android.view.View;

import com.fourshape.numbermatch.custom_calender.data_formats.DateData;

public abstract class OnDateClickListener {
    public static OnDateClickListener instance;
    public abstract void onDateClick(View view, DateData date);

}
