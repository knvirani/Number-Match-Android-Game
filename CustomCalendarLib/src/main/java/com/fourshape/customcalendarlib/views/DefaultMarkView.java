package com.fourshape.customcalendarlib.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fourshape.customcalendarlib.R;
import com.fourshape.customcalendarlib.CellConfig;
import com.fourshape.customcalendarlib.MarkStyle;
import com.fourshape.customcalendarlib.data_formats.DayData;
import com.fourshape.customcalendarlib.utils.AppColor;
import com.fourshape.customcalendarlib.utils.MakeLog;

public class DefaultMarkView extends BaseMarkView {

    private TextView textView;

    private AbsListView.LayoutParams matchParentParams;
    private int orientation;

    private View sideBar;
    private TextView markTextView;
    private ShapeDrawable circleDrawable;
    private Context context;

    private Typeface robotoRegularSlimTypeface;

    private AppColor appColor;

    public DefaultMarkView(Context context) {
        super(context);
        this.context = context;
        this.appColor = new AppColor();
        robotoRegularSlimTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular_slim.ttf");
    }

    public DefaultMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.appColor = new AppColor();
        robotoRegularSlimTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular_slim.ttf");
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

    private void initLayoutWithStyle(MarkStyle style){

        textView = new TextView(getContext());
        textView.setTypeface(robotoRegularSlimTypeface);

        textView.setGravity(Gravity.CENTER);
        matchParentParams = new AbsListView.LayoutParams((int) CellConfig.cellWidth, (int) CellConfig.cellHeight);

        switch (style.getStyle()){

            case MarkStyle.DEFAULT:

            case MarkStyle.BACKGROUND:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setTextColor(Color.WHITE);
                this.setPadding(11, 5, 11, 5);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));

                Drawable drawable = textView.getContext().getDrawable(R.drawable.bg_game_session_completion);
                drawable.setTintList(ColorStateList.valueOf(context.getColor(appColor.getSessionCompletionBackgroundColor())));

                textView.setBackground(drawable);
                this.addView(textView);
                return;

            case MarkStyle.SELECTED_DATE:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setTextColor(context.getColor(R.color.default_selected_date_text));
                this.setPadding(11, 5, 11, 5);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));

                Drawable sDrawable = textView.getContext().getDrawable(R.drawable.bg_selected_date_in_calendar);
                sDrawable.setTintList(ColorStateList.valueOf(context.getColor(appColor.getSelectedDateBackgroundColor())));

                textView.setBackground(sDrawable);
                this.addView(textView);
                return;

            case MarkStyle.TODAY_DATE_INCOMPLETE:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);

                textView.setTextColor(context.getColor(appColor.getTodaySessionIncompleteTextColor()));

                this.setPadding(11, 5, 11, 5);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));

                Drawable tDrawable = context.getDrawable(R.drawable.bg_today_in_calendar_incomplete);
                tDrawable.setTintList(ColorStateList.valueOf(context.getColor(appColor.getTodaySessionIncompleteBackgroundColor())));

                textView.setBackground(tDrawable);
                this.addView(textView);
                return;

            case MarkStyle.TODAY_DATE_COMPLETE:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);

                textView.setTextColor(context.getColor(appColor.getTodaySessionCompleteTextColor()));

                this.setPadding(11, 5, 11, 5);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));

                Drawable tCDrawable = context.getDrawable(R.drawable.bg_today_in_calendar_completion);
                tCDrawable.setTintList(ColorStateList.valueOf(context.getColor(appColor.getTodaySessionCompleteBackgroundColor())));

                textView.setBackground(tCDrawable);
                this.addView(textView);
                return;

            case MarkStyle.SESSION_COMPLETE:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);

                textView.setTextColor(context.getColor(appColor.getSessionCompleteTextColor()));

                this.setPadding(11, 5, 11, 5);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));

                Drawable sCDrawable = context.getDrawable(R.drawable.bg_game_session_completion);
                sCDrawable.setTintList(ColorStateList.valueOf(context.getColor(appColor.getSessionCompletionBackgroundColor())));

                textView.setBackground(sCDrawable);
                this.addView(textView);
                return;

            case MarkStyle.SESSION_INCOMPLETE:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);

                textView.setTextColor(context.getColor(appColor.getSessionIncompleteTextColor()));

                this.setPadding(11, 5, 11, 5);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0));

                Drawable sIDrawable = context.getDrawable(R.drawable.bg_game_session_incomplete);
                sIDrawable.setTintList(ColorStateList.valueOf(context.getColor(appColor.getSessionIncompleteBackgroundColor())));

                textView.setBackground(sIDrawable);
                this.addView(textView);
                return;

            case MarkStyle.DOT:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(VERTICAL);
                textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 2.0));
                this.addView(new PlaceHolderVertical(getContext()));
                this.addView(textView);
                this.addView(new Dot(getContext(), style.getColor()));

                return;

            case MarkStyle.RIGHTSIDEBAR:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 3.0));
                this.addView(new PlaceHolderHorizontal(getContext()));
                this.addView(textView);
                PlaceHolderHorizontal barRight = new PlaceHolderHorizontal(getContext());
                barRight.setBackgroundColor(style.getColor());
                this.addView(barRight);

                return;

            case MarkStyle.LEFTSIDEBAR:

                this.setLayoutParams(matchParentParams);
                this.setOrientation(HORIZONTAL);
                textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 3.0));
                PlaceHolderHorizontal barLeft = new PlaceHolderHorizontal(getContext());
                barLeft.setBackgroundColor(style.getColor());
                this.addView(barLeft);
                this.addView(textView);
                this.addView(new PlaceHolderHorizontal(getContext()));

                return;

            default:
                throw new IllegalArgumentException("Invalid Mark Style Configuration!");
        }
    }

    @Override
    public void setDisplayText(DayData day) {

        initLayoutWithStyle(day.getDate().getMarkStyle());
        textView.setText(day.getText());

    }

    static class PlaceHolderHorizontal extends View{

        LayoutParams params;
        public PlaceHolderHorizontal(Context context) {
            super(context);
            params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0);
            this.setLayoutParams(params);
        }

        public PlaceHolderHorizontal(Context context, AttributeSet attrs) {
            super(context, attrs);
            params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (float) 1.0);
            this.setLayoutParams(params);
        }
    }

    static class PlaceHolderVertical extends View{

        LayoutParams params;
        public PlaceHolderVertical(Context context) {
            super(context);
            params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0);
            this.setLayoutParams(params);
        }

        public PlaceHolderVertical(Context context, AttributeSet attrs) {
            super(context, attrs);
            params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0);
            this.setLayoutParams(params);
        }
    }

    static class Dot extends RelativeLayout {

        public Dot(Context context, int color) {
            super(context);
            this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, (float) 1.0));
            View dotView = new View(getContext());
            LayoutParams lp = new LayoutParams(10, 10);
            lp.addRule(CENTER_IN_PARENT,TRUE);
            dotView.setLayoutParams(lp);
            ShapeDrawable dot = new ShapeDrawable(new OvalShape());

            dot.getPaint().setColor(color);
            dotView.setBackground(dot);
            this.addView(dotView);
        }
    }

}
