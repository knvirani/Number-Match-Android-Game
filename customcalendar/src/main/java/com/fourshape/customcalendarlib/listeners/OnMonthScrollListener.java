package com.fourshape.customcalendarlib.listeners;

import androidx.viewpager.widget.ViewPager;
import com.fourshape.customcalendarlib.CellConfig;
import com.fourshape.customcalendarlib.data_formats.DateData;
import com.fourshape.customcalendarlib.utils.ExpCalendarUtil;

public abstract class OnMonthScrollListener implements ViewPager.OnPageChangeListener {

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        onMonthScroll(positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        CellConfig.middlePosition = position;
        DateData date;

        if (CellConfig.ifMonth)
            date = ExpCalendarUtil.position2Month(position);
        else
            date = ExpCalendarUtil.position2Week(position);
        onMonthChange(date.getYear(), date.getMonth());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public abstract void onMonthChange(int year, int month);

    public abstract void onMonthScroll(float positionOffset);

}
