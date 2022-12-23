package com.fourshape.customcalendarlib.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fourshape.customcalendarlib.CustomCalendarView;
import com.fourshape.customcalendarlib.data_formats.DateData;
import com.fourshape.customcalendarlib.data_formats.MonthData;
import com.fourshape.customcalendarlib.fragments.MonthFragment;
import com.fourshape.customcalendarlib.utils.AppColor;
import com.fourshape.customcalendarlib.utils.CalendarUtil;
import com.fourshape.customcalendarlib.utils.MakeLog;

import org.jetbrains.annotations.NotNull;

public class CalendarViewAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "CalendarViewAdapter";

    private DateData date;

    private int dateCellId;
    private int markCellId;
    private boolean hasTitle = true;

    private Context context;
    private int mCurrentPosition = -1;

    private AppColor appColor;

    public CalendarViewAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }

    public CalendarViewAdapter setDate(DateData date){
        this.date = date;
        return this;
    }

    public void setAppColor (AppColor appColor) {

        if (this.appColor == null)
            this.appColor = new AppColor();

        this.appColor.setThemeId(appColor.getThemeId());

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public CalendarViewAdapter setDateCellId(int dateCellRes){
        this.dateCellId =  dateCellRes;
        return this;
    }

    public CalendarViewAdapter setMarkCellId(int markCellId){
        this.markCellId = markCellId;
        return this;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        int year = CalendarUtil.position2Year(position);
        int month = CalendarUtil.position2Month(position);

        MonthFragment fragment = new MonthFragment();
        fragment.setTitle(hasTitle);
        fragment.setAppColor(appColor);
        MakeLog.info(TAG, "AppColor theme: " + appColor.getThemeId());
        MonthData monthData = new MonthData(new DateData(year, month, month / 2), hasTitle);
        fragment.setData(monthData, dateCellId, markCellId);
        return fragment;
    }

    @Override
    public int getCount() {
        return 1000;
    }

    public CalendarViewAdapter setTitle(boolean hasTitle){
        this.hasTitle = hasTitle;
        return this;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NotNull Object object) {
        super.setPrimaryItem(container, position, object);
        ((CustomCalendarView) container).measureCurrentView(position);
    }
}
