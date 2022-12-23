package com.fourshape.numbermatch.puzzle_core.structure;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.card.MaterialCardView;

public class Achievement {

    private boolean firstNumber;
    private boolean firstRow;
    private boolean firstStep;
    private boolean firstSession;

    public boolean getFirstNumber () {
        return firstNumber;
    }

    public boolean getFirstRow () {
        return firstRow;
    }

    public boolean getFirstStep () {
        return firstStep;
    }

    public boolean getFirstSession () {
        return firstSession;
    }

    public Achievement () {

    }

    public Achievement (Achievement achievement) {
        this.firstNumber = achievement.getFirstNumber();
        this.firstRow = achievement.getFirstRow();
        this.firstStep = achievement.getFirstStep();
        this.firstSession = achievement.getFirstSession();
    }

    public void setFirstNumber(boolean firstNumber) {
        this.firstNumber = firstNumber;
    }

    public void setFirstRow(boolean firstRow) {
        this.firstRow = firstRow;
    }

    public void setFirstSession(boolean firstSession) {
        this.firstSession = firstSession;
    }

    public void setFirstStep(boolean firstStep) {
        this.firstStep = firstStep;
    }

    public void setView (ViewGroup container) {

        container.addView(getView(container.getContext(), this.firstNumber, "FIRST NUMBER"));
        container.addView(getView(container.getContext(), this.firstRow, "FIRST ROW"));
        container.addView(getView(container.getContext(), this.firstStep, "FIRST STEP"));
        container.addView(getView(container.getContext(), this.firstSession, "FIRST SESSION"));

    }

    private View getView (Context context, boolean status, String title) {

        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_child_for_achievement, null);

        AppColor appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(context, SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        MaterialCardView parentCV = view.findViewById(R.id.parent_cv);
        ImageView statusIV = view.findViewById(R.id.achievement_status_iv);
        TextView statusTV = view.findViewById(R.id.achievement_title);

        if (status) {
            statusIV.setImageTintList(ColorStateList.valueOf(context.getColor(appColor.getPrimaryBtnBackgroundColor())));
        } else {
            statusIV.setImageTintList(ColorStateList.valueOf(context.getColor(appColor.getAppBarIconTintColor())));
        }

        statusTV.setTextColor(context.getColor(appColor.getAppBarTitleTextColor()));
        parentCV.setCardBackgroundColor(context.getColor(appColor.getDynamicStatsCardBackgroundColor()));
        statusTV.setText(title);

        parentCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;

    }
}
