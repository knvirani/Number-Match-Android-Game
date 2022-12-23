package com.fourshape.customcalendarlib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.fourshape.customcalendarlib.adapters.CalendarAdapter;
import com.fourshape.customcalendarlib.data_formats.MonthData;
import com.fourshape.customcalendarlib.utils.AppColor;

public class MonthView extends GridView {

    private AppColor appColor;
    private MonthData monthData;
    private CalendarAdapter adapter;

    public void setAppColor (AppColor appColor) {
        this.appColor = new AppColor();
        this.appColor.setThemeId(appColor.getThemeId());
    }

    public MonthView(Context context) {
        super(context);
        this.setNumColumns(7);
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setNumColumns(7);
    }

    public MonthView displayMonth(MonthData monthData){
        adapter = new CalendarAdapter(getContext(), 1, monthData.getData(), appColor);
        return this;
    }

}
