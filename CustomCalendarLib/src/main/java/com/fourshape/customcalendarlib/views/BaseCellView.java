package com.fourshape.customcalendarlib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.fourshape.customcalendarlib.CellConfig;
import com.fourshape.customcalendarlib.data_formats.DateData;
import com.fourshape.customcalendarlib.data_formats.DayData;
import com.fourshape.customcalendarlib.listeners.OnDateClickListener;

public abstract class BaseCellView extends LinearLayoutCompat {

    private OnDateClickListener clickListener;
    private DateData date;
    int width, height;
    float density;
    float defaultCellSize;

    public BaseCellView(Context context) {
        super(context);
        density = context.getResources().getDisplayMetrics().density;
    }

    public BaseCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
    }

    public BaseCellView setDate(DateData date){
        this.date = date;
        return this;
    }

    public BaseCellView setOnDateClickListener(OnDateClickListener clickListener){
        this.clickListener = clickListener;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseCellView.this.clickListener != null) {
                    BaseCellView.this.clickListener.onDateClick(BaseCellView.this, date);
                }
            }
        });
        return this;
    }

    public BaseCellView removeOnDateClickListener(){
        this.clickListener = null;
        return this;
    }

    public OnDateClickListener getOnDateClickListener(){
        return this.clickListener;
    }

    @Override
    protected void onMeasure(int measureWidthSpec,int measureHeightSpec){

        width = measureWidth(measureWidthSpec);
        height = measureHeight(measureHeightSpec);
        //measureHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        //measureWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        //super.onMeasure(measureWidthSpec, measureHeightSpec);
        super.onMeasure(width, height);
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 0;
        if (specMode == MeasureSpec.AT_MOST) {
            result = (int) (CellConfig.cellWidth * density);
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = (int) (specSize * density);
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
            result = (int) (CellConfig.cellHeight * density);
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = (int) (specSize * density);
        } else {
            result = (int) CellConfig.cellHeight;
        }
        return result;
    }

    public abstract void setDisplayText(DayData day);

}
