package com.fourshape.customcalendarlib.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.fourshape.customcalendarlib.R;
import com.fourshape.customcalendarlib.MarkStyle;
import com.fourshape.customcalendarlib.data_formats.DayData;
import com.fourshape.customcalendarlib.data_formats.MarkedDates;
import com.fourshape.customcalendarlib.listeners.OnDateClickListener;
import com.fourshape.customcalendarlib.utils.AppColor;
import com.fourshape.customcalendarlib.utils.CurrentCalendar;
import com.fourshape.customcalendarlib.views.BaseCellView;
import com.fourshape.customcalendarlib.views.BaseMarkView;
import com.fourshape.customcalendarlib.views.DefaultCellView;
import com.fourshape.customcalendarlib.views.DefaultMarkView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CalendarAdapter extends ArrayAdapter implements Observer {

    private ArrayList data;
    private int cellView = -1;
    private int markView = -1;
    private AppColor appColor;

    public CalendarAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects, AppColor appColor) {
        super(context, resource, objects);
        this.data = objects;
        this.appColor = appColor;
        MarkedDates.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return data.size();
    }

    public CalendarAdapter setCellViews(int cellView, int markView){
        this.cellView = cellView;
        this.markView = markView;
        return this;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup){
        View ret = null;
        DayData dayData = (DayData) data.get(position);
        MarkStyle style = MarkedDates.getInstance().check(dayData.getDate());
        boolean marked = style != null;
        if (marked){

            dayData.getDate().setMarkStyle(style);

            if (markView > 0){
                BaseMarkView baseMarkView = (BaseMarkView) View.inflate(getContext(), markView, null);
                baseMarkView.setDisplayText(dayData);
                ret = baseMarkView;
            } else {
                ret = new DefaultMarkView(getContext());
                ((DefaultMarkView) ret).setDisplayText(dayData);
            }
        } else {
            if (cellView > 0) {
                BaseCellView baseCellView = (BaseCellView) View.inflate(getContext(), cellView, null);
                baseCellView.setDisplayText(dayData);
                ret = baseCellView;
            } else {
                ret = new DefaultCellView(getContext());
                ((DefaultCellView) ret).setAppColor(appColor);
                ((DefaultCellView) ret).setDisplayText(dayData);
            }
        }
        ((BaseCellView) ret).setDate(dayData.getDate());
        if (OnDateClickListener.instance != null) {
            ((BaseCellView) ret).setOnDateClickListener(OnDateClickListener.instance);
        }
        return ret;
    }


}
