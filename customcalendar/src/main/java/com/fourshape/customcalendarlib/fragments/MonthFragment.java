package com.fourshape.customcalendarlib.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.fourshape.customcalendarlib.R;
import com.fourshape.customcalendarlib.adapters.CalendarAdapter;
import com.fourshape.customcalendarlib.data_formats.MonthData;
import com.fourshape.customcalendarlib.utils.AppColor;
import com.fourshape.customcalendarlib.utils.MakeLog;
import com.fourshape.customcalendarlib.views.MonthView;

public class MonthFragment extends Fragment {

    private static final String TAG = "MonthFragment";

    private MonthData monthData;
    private int cellView = -1;
    private int markView = -1;
    private boolean hasTitle = true;
    private AppColor appColor;

    public void setData(MonthData monthData, int cellView, int markView) {
        this.monthData = monthData;
        this.cellView = cellView;
        this.markView = markView;
    }

    public void setAppColor (AppColor appColor) {

        this.appColor = new AppColor();
        this.appColor.setThemeId(appColor.getThemeId());

    }

    public void setTitle(boolean hasTitle) {
        this.hasTitle = hasTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        LinearLayout ret = new LinearLayout(getContext());

        ret.setOrientation(LinearLayout.VERTICAL);
        ret.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ret.setGravity(Gravity.CENTER);

        /*
        View titleView = inflater.inflate(R.layout.calendar_month_year_title, null);
        TextView monthTitleTV, yearTitleTV;

        monthTitleTV = titleView.findViewById(R.id.month_title);
        yearTitleTV = titleView.findViewById(R.id.year_title);

         */

        if ((monthData != null) && (monthData.getDate() != null)) {

            /*
            if (hasTitle) {
                monthTitleTV.setText(getMonth(monthData.getDate().getMonth()));
                yearTitleTV.setText(String.format(Locale.getDefault(), "%d", monthData.getDate().getYear()));
            }

             */

            MonthView monthView = new MonthView(getContext());
            monthView.setAppColor(appColor);
            MakeLog.info(TAG, "AppColor Theme: " + appColor.getThemeId());
            monthView.setAdapter(new CalendarAdapter(getContext(), 1, monthData.getData(), appColor).setCellViews(cellView, markView));
            //((ViewGroup)titleView).addView(monthView);

            ret.addView(monthView);

        }

        return ret;
    }

    private static String getMonth (int date) {
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

}
