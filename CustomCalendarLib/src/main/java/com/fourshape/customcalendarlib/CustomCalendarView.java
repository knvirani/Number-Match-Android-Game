package com.fourshape.customcalendarlib;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.fourshape.customcalendarlib.R;
import com.fourshape.customcalendarlib.adapters.CalendarViewAdapter;
import com.fourshape.customcalendarlib.data_formats.DateData;
import com.fourshape.customcalendarlib.data_formats.MarkedDates;
import com.fourshape.customcalendarlib.listeners.OnDateClickListener;
import com.fourshape.customcalendarlib.listeners.OnMonthChangeListener;
import com.fourshape.customcalendarlib.utils.AppColor;
import com.fourshape.customcalendarlib.utils.CalendarUtil;
import com.fourshape.customcalendarlib.utils.CurrentCalendar;

public class CustomCalendarView extends ViewPager {

    private int dateCellViewResId = -1;
    private View dateCellView = null;
    private int markedStyle = -1;
    private int markedCellResId = -1;
    private View markedCellView = null;
    private boolean hasTitle = true;

    private boolean initted = false;

    private DateData currentDate;
    private CalendarViewAdapter adapter;

    private int width, height;
    private int currentIndex;
    private AppColor appColor;
    private Context context;

    public CustomCalendarView(Context context) {
        super(context);
        this.context = context;
        if (context instanceof FragmentActivity){
            init((FragmentActivity) context);
        }
    }

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (context instanceof FragmentActivity){
            init((FragmentActivity) context);
        }
    }

    public void setViewTheme (int theme) {
        if (appColor != null) {
            appColor.setThemeId(theme);
        } else {
            appColor = new AppColor();
            appColor.setThemeId(theme);
        }

        if (this.context != null) {
            if (context instanceof FragmentActivity) {
                init((FragmentActivity)context);
            }
        }

    }

    public void init(FragmentActivity activity){

        if (initted){
            return;
        }

        initted = true;
        if (currentDate == null){
            currentDate = CurrentCalendar.getCurrentDateData();
        }

        appColor = new AppColor();

        // TODO: 15/8/28 Will this cause trouble when achieved?
        if (this.getId() == View.NO_ID){
            this.setId(R.id.calendarViewPager);
        }
        adapter = new CalendarViewAdapter(activity.getSupportFragmentManager()).setDate(currentDate);
        adapter.setAppColor(appColor);

        this.setAdapter(adapter);
        this.setCurrentItem(500);
//        addBackground();
        float density = getContext().getResources().getDisplayMetrics().density;

        float divideFactorForWidth = 10.0f;
        float divideFactorForHeight = 10.0f;

        CellConfig.cellHeight = getContext().getResources().getDisplayMetrics().widthPixels / divideFactorForHeight / density;
        CellConfig.cellWidth = getContext().getResources().getDisplayMetrics().widthPixels / divideFactorForWidth / density;

    }

    private void addBackground(){
        ShapeDrawable drawable = new ShapeDrawable(new RectShape());
        drawable.getPaint().setColor(Color.LTGRAY);
        drawable.getPaint().setStyle(Paint.Style.STROKE);
        this.setBackground(drawable);
    }

    public CustomCalendarView travelTo (DateData dateData) {
        this.currentDate = dateData;
        CalendarUtil.date = dateData;
        this.initted = false;
        init((FragmentActivity) getContext());
        return this;
    }

    public CustomCalendarView markDate(int year, int month, int day){
        MarkedDates.getInstance().add(new DateData(year, month, day));
        return this;
    }

    public CustomCalendarView unMarkDate(int year, int month, int day){
        MarkedDates.getInstance().remove(new DateData(year, month, day));
        return this;
    }

    public CustomCalendarView markDate(DateData date){
        MarkedDates.getInstance().add(date);
        return this;
    }

    public CustomCalendarView unMarkDate(DateData date){
        MarkedDates.getInstance().remove(date);
        return this;
    }

    public MarkedDates getMarkedDates(){
        return MarkedDates.getInstance();
    }

    public CustomCalendarView setDateCell(int resId){
        adapter.setDateCellId(resId);
        return this;
    }

    public CustomCalendarView setMarkedStyle(int style, int color){
        MarkStyle.current = style;
        MarkStyle.defaultColor = color;
        return this;
    }

    public CustomCalendarView setMarkedStyle(int style){
        MarkStyle.current = style;
        return this;
    }

    public CustomCalendarView setMarkedCell(int resId) {
        adapter.setMarkCellId(resId);
        return this;
    }

    public CustomCalendarView setOnMonthChangeListener(OnMonthChangeListener listener){
        this.addOnPageChangeListener(listener);
        return this;
    }

    public CustomCalendarView setOnDateClickListener(OnDateClickListener onDateClickListener){
        OnDateClickListener.instance = onDateClickListener;
        return this;
    }

    public CustomCalendarView hasTitle(boolean hasTitle){
        this.hasTitle = hasTitle;
        adapter.setTitle(hasTitle);
        return this;
    }

    @Override
    protected void onMeasure(int measureWidthSpec,int measureHeightSpec){
        /*
        width = measureWidth(measureWidthSpec);
        height = measureHeight(measureHeightSpec);
        measureHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        measureWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

         */
        super.onMeasure(measureWidthSpec, measureHeightSpec);
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 0;
        if (specMode == MeasureSpec.AT_MOST) {
            float destiney = getContext().getResources().getDisplayMetrics().density;
            result = (int) (CellConfig.cellWidth * 7 * destiney);
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) CellConfig.cellHeight;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 0;
        if (specMode == MeasureSpec.AT_MOST) {
            int columns = CalendarUtil.getWeekCount(currentIndex);
            columns = hasTitle ? columns + 1 : columns;
            float density = getContext().getResources().getDisplayMetrics().density;
            result = (int) (CellConfig.cellHeight * columns * density);
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) CellConfig.cellHeight;
        }
        return result;
    }


    public void measureCurrentView(int currentIndex) {
        this.currentIndex = currentIndex;
        requestLayout();
    }

}
