package com.fourshape.numbermatch.custom_calender;

import android.annotation.SuppressLint;

import com.fourshape.numbermatch.custom_calender.data_formats.DateData;
import com.fourshape.numbermatch.custom_calender.utils.CurrentCalendar;
import com.fourshape.numbermatch.utils.MakeLog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidation {

    private static final String TAG = "DateValidation";

    public static int getDateDifference (DateData date1, DateData date2) {

        if (date1 == null || date2 == null)
            return -1;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy");

        try {

            Date parsedDate1 = simpleDateFormat.parse("" + date1.getDay() + "-" + date1.getMonth()+ "-" + date1.getYear());
            Date parsedDate2 = simpleDateFormat.parse("" + date2.getDay() + "-" + date2.getMonth()+ "-" + date2.getYear());

            long date1Time = parsedDate1.getTime();
            long date2Time = parsedDate2.getTime();

            return (int) ((date2Time-date1Time < 0) ? (date2Time-date1Time)*-1 : date2Time-date1Time);

        } catch (Exception e) {
            MakeLog.exception(e);
        }

        return -1;

    }

    public static boolean isDateValidForDBRecordUpdate (DateData proposedDateValue) {

        if (proposedDateValue == null)
            return false;

        DateData currentDate = CurrentCalendar.getCurrentDateData();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy");

        try {

            Date proposedDate = simpleDateFormat.parse("" + proposedDateValue.getDay() + "-" + proposedDateValue.getMonth()+ "-" + proposedDateValue.getYear());
            Date cDate = simpleDateFormat.parse("" + currentDate.getDay() + "-" + currentDate.getMonth()+ "-" + currentDate.getYear());

            if (proposedDate != null && cDate != null) {
                return proposedDate.getTime() < cDate.getTime();
            } else {
                return false;
            }

        } catch (Exception e) {
            MakeLog.exception(e);
            return false;
        }

    }

    public static boolean isValidDate (DateData proposedDateData) {

        DateData currentDate = CurrentCalendar.getCurrentDateData();

        if (proposedDateData == null)
            return false;

        int p_year = proposedDateData.getYear();
        int p_month = proposedDateData.getMonth();
        int p_day = proposedDateData.getDay();

        int c_year = currentDate.getYear();
        int c_month = currentDate.getMonth();
        int c_day = currentDate.getDay();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy");

        try {

            Date proposedDate = simpleDateFormat.parse("" + p_day + "-" + p_month + "-" + p_year);
            Date cDate = simpleDateFormat.parse("" + c_day + "-" + c_month+ "-" + c_year);

            if (proposedDate != null && cDate != null) {
                return proposedDate.getTime() <= cDate.getTime();
            } else {
                return false;
            }

        } catch (Exception e) {
            MakeLog.exception(e);
            return false;
        }

    }

    private static String getValidStatus (boolean isValid) {
        return (isValid) ? "Valid" : "Invalid";
    }

    private static void printDate (int day, int month, int year) {
        MakeLog.info(TAG + " Default", "" + day + "-" + month + "-" + year);
    }

}
