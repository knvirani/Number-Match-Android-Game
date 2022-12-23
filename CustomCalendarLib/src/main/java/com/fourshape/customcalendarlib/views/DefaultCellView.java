package com.fourshape.customcalendarlib.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.fourshape.customcalendarlib.DesignConfig;
import com.fourshape.customcalendarlib.R;
import com.fourshape.customcalendarlib.CellConfig;
import com.fourshape.customcalendarlib.DateValidation;
import com.fourshape.customcalendarlib.MarkStyleExp;
import com.fourshape.customcalendarlib.data_formats.DayData;
import com.fourshape.customcalendarlib.utils.AppColor;
import com.fourshape.customcalendarlib.utils.FormattedData;
import com.fourshape.customcalendarlib.utils.MakeLog;

public class DefaultCellView extends BaseCellView {

    public TextView textView;
    private AbsListView.LayoutParams matchParentParams;
    private Context context;
    private Typeface robotoRegularSlimTypeface, robotoMediumTypeface;
    private AppColor appColor;

    public DefaultCellView(Context context) {
        super(context);
        this.context = context;
        robotoRegularSlimTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular_slim.ttf");
        robotoMediumTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_medium.ttf");
        initLayout();
    }

    public DefaultCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        robotoRegularSlimTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular_slim.ttf");
        robotoMediumTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_medium.ttf");
        initLayout();
    }

    public void setAppColor (AppColor appColor) {
        this.appColor = appColor;
        initLayout();
    }

    private int getAttrValue (int attrId) {

        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(attrId, typedValue, true);
            return typedValue.data;
        } catch (Exception e) {
            MakeLog.exception(e);
            return -1;
        }

    }

    private void initLayout(){

        MakeLog.info("CellConfig", "Width: " + CellConfig.cellWidth + " Height: " + CellConfig.cellHeight);

        appColor = new AppColor();
        matchParentParams = new AbsListView.LayoutParams((int) CellConfig.cellWidth, (int) CellConfig.cellHeight);
        this.setLayoutParams(matchParentParams);
        this.setOrientation(VERTICAL);

        View view = LayoutInflater.from(context).inflate(R.layout.date_text_view, null);

        textView = view.findViewById(R.id.text_view);
        textView.setTextSize(DesignConfig.DATE_TEXT_SIZE);
        //textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT, 1.0F));
        textView.setTypeface(robotoRegularSlimTypeface);
        textView.setGravity(Gravity.CENTER);

        this.removeAllViews();
        this.addView(view);
    }

    @Override
    public void setDisplayText(DayData day) {

        if (day.getText().trim().equals("Sun") || day.getText().trim().equals("Mon") || day.getText().trim().equals("Tue") || day.getText().trim().equals("Wed") || day.getText().trim().equals("Thu") || day.getText().trim().equals("Fri") || day.getText().trim().equals("Sat"))
        {
            textView.setTypeface(robotoMediumTypeface);
            //textView.setBackgroundColor(context.getColor(this.appColor.getCalendarWeekDayRowBackgroundColor()));
            textView.setTextColor(context.getColor(this.appColor.getCalendarWeekDayTitleColor()));
        } else {

            //textView.setTextAppearance(R.style.DateTextAppearance);
            if (DateValidation.isValidDate(day.getDate())) {
                textView.setTextColor(context.getColor(this.appColor.getCalendarDayColorTitleColor()));
            } else {
                textView.setTextColor(context.getColor(this.appColor.getInvalidCalendarDateTitleColor()));
            }

        }

        textView.setText(day.getText());
        //MakeLog.info("DefaultCellView", day.getText());

    }

    @Override
    protected void onMeasure(int measureWidthSpec,int measureHeightSpec){
        super.onMeasure(measureWidthSpec, measureHeightSpec);
    }

    public boolean setDateChoose() {
        setBackgroundDrawable(MarkStyleExp.choose);
        textView.setTextColor(Color.WHITE);
        return true ;
    }

    public void setDateToday(){
        setBackgroundDrawable(MarkStyleExp.today);
        textView.setTextColor(Color.rgb(105, 75, 125));
    }

    public void setDateNormal() {
        textView.setTextColor(Color.BLACK);
        setBackgroundDrawable(null);
    }

    public void setTextColor(String text, int color) {
        textView.setText(text);
        if (color != 0) {
            textView.setTextColor(color);
        }
    }

}
