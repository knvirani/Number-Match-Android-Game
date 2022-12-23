package com.fourshape.customcalendarlib.utils;

import com.fourshape.customcalendarlib.R;

public class AppColor {

    private int themeId;

    public AppColor () {
    }

    public void setThemeId (int themeId) {
        this.themeId = themeId;
    }

    public int getCalendarWeekDayRowBackgroundColor () {
        if (themeId == 10)
            return R.color.default_calendar_week_day_row_bg;
        else if (themeId == 11)
            return R.color.theme_1_calendar_week_day_row_bg;

        return R.color.default_calendar_week_day_row_bg;
    }

    public int getCalendarWeekDayTitleColor () {

        if (themeId == 10)
            return R.color.default_calendar_week_day_title;
        else if (themeId == 11)
            return R.color.theme_1_calendar_week_day_title;

        return R.color.theme_1_calendar_week_day_title;

    }

    public int getCalendarDayColorTitleColor () {

        if (themeId == 10)
            return R.color.default_calendar_day;
        else if (themeId == 11)
            return R.color.theme_1_calendar_day;

        return R.color.default_calendar_day;

    }

    public int getInvalidCalendarDateTitleColor () {

        if (themeId == 10)
            return R.color.default_invalid_calendar_date;
        else if (themeId == 11)
            return R.color.theme_1_invalid_calendar_date;

        return R.color.default_invalid_calendar_date;

    }

    public int getSelectedDateBackgroundColor () {

        if (themeId == 10)
            return R.color.default_selected_date_bg;
        else if (themeId == 11)
            return R.color.theme_1_selected_date_bg;

        return R.color.default_selected_date_bg;

    }

    public int getSessionIncompleteBackgroundColor () {

        if (themeId == 10)
            return R.color.default_session_incomplete_date_bg;
        else if (themeId == 11)
            return R.color.theme_1_session_incomplete_date_bg;

        return R.color.default_session_incomplete_date_bg;

    }

    public int getSessionIncompleteTextColor () {

        if (themeId == 10)
            return R.color.default_session_incomplete_date_text;
        else if (themeId == 11)
            return R.color.theme_1_session_incomplete_date_text;

        return R.color.default_session_incomplete_date_text;

    }

    public int getSessionCompleteTextColor () {

        if (themeId == 10)
            return R.color.default_session_complete_date_text;
        else if (themeId == 11)
            return R.color.theme_1_session_complete_date_text;

        return R.color.default_session_complete_date_text;

    }

    public int getSessionCompletionBackgroundColor () {

        if (themeId == 10)
            return R.color.default_session_complete_date_bg;
        else if (themeId == 11)
            return R.color.theme_1_session_complete_date_bg;

        return R.color.default_session_complete_date_bg;

    }

    public int getTodaySessionCompleteTextColor () {

        if (themeId == 10)
            return R.color.default_today_complete_text;
        else if (themeId == 11)
            return R.color.theme_1_today_complete_text;

        return R.color.default_today_complete_text;

    }

    public int getTodaySessionCompleteBackgroundColor () {

        if (themeId == 10)
            return R.color.default_today_complete_bg;
        else if (themeId == 11)
            return R.color.theme_1_today_complete_bg;

        return R.color.default_today_complete_bg;

    }

    public int getTodaySessionIncompleteTextColor () {

        if (themeId == 10)
            return R.color.default_today_incomplete_text;
        else if (themeId == 11)
            return R.color.theme_1_today_incomplete_text;

        return R.color.default_today_incomplete_text;

    }

    public int getTodaySessionIncompleteBackgroundColor () {

        if (themeId == 10)
            return R.color.default_today_in_calendar;
        else if (themeId == 11)
            return R.color.theme_1_today_in_calendar;

        return R.color.default_today_in_calendar;

    }

}
