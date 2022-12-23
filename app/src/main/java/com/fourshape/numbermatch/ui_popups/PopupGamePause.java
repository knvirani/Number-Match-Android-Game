package com.fourshape.numbermatch.ui_popups;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fourshape.easythingslib.ContactActivity;
import com.fourshape.easythingslib.listeners.OnDialogDismissListener;
import com.fourshape.easythingslib.listeners.OnDialogShowListener;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.FormattedData;
import com.fourshape.easythingslib.utils.MakeLog;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.listeners.OnPauseStatusListener;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.utils.DimPopupWindow;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.card.MaterialCardView;

public class PopupGamePause {

    private static final String TAG = "PopupGamePause";

    private View view;
    private PopupWindow popupWindow;

    private MaterialCardView parentCV, exitCV, resumeCV, reportCV;
    private TextView popupHeaderTV, scoreHeaderTV, scoreValueTV, stepHeaderTV, stepValueTV, gameLevelHeaderTV, levelValueTV, sessionTimeHeaderTV, sessionTimeValueTV, reportTV, exitTV, resumeTV;
    private View dividerView1, dividerView2;

    private AppColor appColor;

    private OnPauseStatusListener onPauseStatusListener;
    private OnDialogDismissListener onDialogDismissListener;
    private OnDialogShowListener onDialogShowListener;

    private boolean hasActionTaken = false;

    public PopupGamePause (Context context) {

        this.view = LayoutInflater.from(context).inflate(R.layout.popup_game_pause, null);
        this.appColor = new AppColor();

        preparePopup();

    }

    public PopupGamePause setData (int totalScore, int totalSteps, int sessionSeconds, String gameLevel) {

        stepValueTV.setText("Step: " + String.valueOf(totalSteps));
        scoreValueTV.setText(String.valueOf(totalScore));
        levelValueTV.setText(gameLevel);
        sessionTimeValueTV.setText(FormattedData.getFormattedTime(sessionSeconds));

        return this;
    }

    public PopupGamePause setOnDialogShowListener(OnDialogShowListener onDialogShowListener) {
        this.onDialogShowListener = onDialogShowListener;
        return this;
    }

    public PopupGamePause setOnDialogDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.onDialogDismissListener = onDialogDismissListener;
        return this;
    }

    public PopupGamePause setOnPauseStatusListener (OnPauseStatusListener onPauseStatusListener) {
        this.onPauseStatusListener = onPauseStatusListener;
        return this;
    }

    public PopupGamePause show () {

        hasActionTaken = false;

        refreshViewsColor();

        if (this.view == null) {
            MakeLog.error(TAG, "Holder view is NULL");
            return this;
        }

        if (this.popupWindow == null) {
            MakeLog.error(TAG, "Window is NULL");
            return this;
        }

        if (this.popupWindow.isShowing()) {
            MakeLog.error(TAG, "Already visible. No need to make a duplicate.");
            return this;
        }

        popupWindow.showAtLocation(this.view, Gravity.CENTER, 0, 0);
        DimPopupWindow.dimBehindWithFactor(popupWindow, 0.5f);
        MakeLog.info(TAG, "Popup is Showing.");

        if (onDialogShowListener != null) {
            onDialogShowListener.onShow();
        }

        return this;

    }

    private void refreshViewsColor () {

        this.appColor.setTheme(new CommonSharedData(this.view.getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        if (parentCV != null) {
            parentCV.setCardBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupCardBackgroundColor()));
        }

        if (exitCV != null) {
            exitCV.setCardBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupSecondaryButtonBackgroundColor()));
        }

        if (resumeCV != null) {
            resumeCV.setCardBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupPrimaryButtonBackgroundColor()));
        }

        if (reportCV != null) {
            reportCV.setCardBackgroundColor(this.view.getContext().getColor(this.appColor.getPopupCardBackgroundColor()));
        }

        if (popupHeaderTV != null) {
            popupHeaderTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupTitleTextColor()));
            popupHeaderTV.setCompoundDrawableTintList(ColorStateList.valueOf(this.view.getContext().getColor(this.appColor.getPopupPrimaryButtonBackgroundColor())));
        }

        if (scoreHeaderTV != null) {
            scoreHeaderTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor()));
        }
        if (scoreValueTV != null) {
            scoreValueTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor()));
        }

        if (gameLevelHeaderTV != null) {
            gameLevelHeaderTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor()));
        }

        if (levelValueTV != null) {
            levelValueTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor()));
        }

        if (sessionTimeHeaderTV != null) {
            sessionTimeHeaderTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor()));
        }

        if (sessionTimeValueTV != null) {
            sessionTimeValueTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor()));
        }

        if (stepHeaderTV != null) {
            stepHeaderTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor()));
        }

        if (stepValueTV != null) {
            stepValueTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor()));
        }

        if (reportTV != null) {
            reportTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupBodyTextColor()));
        }

        if (exitTV != null) {
            exitTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupSecondaryButtonTextColor()));
        }

        if (resumeTV != null) {
            resumeTV.setTextColor(this.view.getContext().getColor(this.appColor.getPopupPrimaryButtonTextColor()));
        }

        if (dividerView1 != null) {
            dividerView1.setBackgroundColor(this.view.getContext().getColor(this.appColor.getNormalDividerColor()));
        }

        if (dividerView2 != null) {
            dividerView2.setBackgroundColor(this.view.getContext().getColor(this.appColor.getNormalDividerColor()));
        }

    }

    private void preparePopup () {

        parentCV = this.view.findViewById(R.id.parent_cv);
        exitCV = this.view.findViewById(R.id.exit_cv);
        resumeCV = this.view.findViewById(R.id.resume_cv);
        reportCV = this.view.findViewById(R.id.report_cv);
        popupHeaderTV = this.view.findViewById(R.id.popup_header);
        scoreHeaderTV = this.view.findViewById(R.id.score_header_tv);
        scoreValueTV = this.view.findViewById(R.id.score_value_tv);
        gameLevelHeaderTV = this.view.findViewById(R.id.level_header_tv);
        levelValueTV = this.view.findViewById(R.id.level_value_tv);
        sessionTimeHeaderTV = this.view.findViewById(R.id.time_header_tv);
        sessionTimeValueTV = this.view.findViewById(R.id.time_value_tv);
        reportTV = this.view.findViewById(R.id.report_tv);
        exitTV = this.view.findViewById(R.id.exit_tv);
        resumeTV = this.view.findViewById(R.id.resume_tv);
        dividerView1 = this.view.findViewById(R.id.divider_view_1);
        dividerView2 = this.view.findViewById(R.id.divider_view_2);
        stepHeaderTV = this.view.findViewById(R.id.step_header_tv);
        stepValueTV = this.view.findViewById(R.id.step_value_tv);

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false;

        this.popupWindow = new PopupWindow(this.view, width, height, focusable);

        setViewsListener();

    }

    private void setViewsListener () {

        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                if (onDialogDismissListener != null) {
                    onDialogDismissListener.onDismiss();
                }

                if (!hasActionTaken) {
                    if (onPauseStatusListener != null) {
                        onPauseStatusListener.onResume();
                    }
                }

            }
        });

        exitCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hasActionTaken = true;
                popupWindow.dismiss();

                if (onPauseStatusListener != null) {
                    onPauseStatusListener.onExit();
                }

            }
        });

        reportCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), ContactActivity.class));
            }
        });

        resumeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hasActionTaken = true;

                popupWindow.dismiss();

                if (onPauseStatusListener != null) {
                    onPauseStatusListener.onResume();
                }

            }
        });

    }

}
