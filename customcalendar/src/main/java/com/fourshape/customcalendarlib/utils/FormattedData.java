package com.fourshape.customcalendarlib.utils;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;

public class FormattedData {

    /*
    public static String getFormattedDate (DateData dateData) {
        return dateData.getDay() + " " + getMonth(dateData.getMonth()) + ", " + dateData.getYear();
    }

     */

    public static Spanned formattedContent (String text) {
        if (text != null) {
            if (Build.VERSION.SDK_INT >= 24)
                return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
            else
                return Html.fromHtml(text);
        } else {
            if (Build.VERSION.SDK_INT >= 24)
                return Html.fromHtml("", Html.FROM_HTML_MODE_LEGACY);
            else
                return Html.fromHtml("");
        }
    }

    public static int getAttrValue (Context context, int attrId) {

        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(attrId, typedValue, true);
            return typedValue.data;
        } catch (Exception e) {
            MakeLog.exception(e);
            return -1;
        }

    }

    public static String getMonth (int date) {
        if (date == 1)
            return "Jan";
        else if (date == 2)
            return "Feb";
        else if (date == 3)
            return "Mar";
        else if (date == 4)
            return "Apr";
        else if (date == 5)
            return "May";
        else if (date == 6)
            return "Jun";
        else if (date == 7)
            return "Jul";
        else if (date == 8)
            return "Aug";
        else if (date == 9)
            return "Sep";
        else if (date == 10)
            return "Oct";
        else if (date == 11)
            return "Nov";
        else if (date == 12)
            return "Dec";
        else
            return "Nom";
    }


    public static String getFormattedTime (int time) {

        String formattedTime = "";

        if (time < 60){
            if (time < 10) {
                formattedTime = "00" + ":" + "0" + time;
            } else {
                formattedTime = "00" + ":" + time;
            }
        } else if (time < 3600) {

            int minute = time/60;
            int second = time%60;

            if (minute < 10)
                formattedTime = "0" + minute;
            else
                formattedTime = String.valueOf(minute);

            if (second < 10)
                formattedTime += ":" + "0" + second;
            else
                formattedTime += ":" + second;

        } else if (time < 36000) {
            int hour = time/3600;

            formattedTime = "0" + hour;

            int data_for_m_s = time%3600;
            int minute = data_for_m_s/60;
            int second = data_for_m_s%60;

            if (minute < 10)
                formattedTime += ":" + "0" + minute;
            else
                formattedTime += ":" + minute;

            if (second < 10)
                formattedTime += ":" + "0" + second;
            else
                formattedTime += ":" + second;
        } else {
            return String.valueOf(time);
        }

        return formattedTime;
    }

}
