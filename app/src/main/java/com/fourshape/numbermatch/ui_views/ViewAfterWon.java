package com.fourshape.numbermatch.ui_views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.FormattedData;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.listeners.OnAfterWonControlListener;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class ViewAfterWon {

    private View mainView;

    private TextView sessionCompletedTV, levelHeaderTV, levelValueTV, stepHeaderTV, stepValueTV, scoreHeaderTV, scoreValueTV, timeHeaderTV, timeValueTV, newBestScoreTV;
    private View dividerView1, dividerView2, dividerView3, dividerView4, dividerView5, dividerView6, dividerView7;
    private MaterialButton homeMB, newGameMB;
    private MaterialCardView resultDataCV;
    private AppColor appColor;

    private OnAfterWonControlListener onAfterWonControlListener;

    public ViewAfterWon (Context context) {
        mainView = LayoutInflater.from(context).inflate(R.layout.view_after_won, null);
        appColor = new AppColor();
        prepare();
        refreshViewsColor();
    }

    public ViewAfterWon setOnAfterWonControlListener(OnAfterWonControlListener onAfterWonControlListener) {
        this.onAfterWonControlListener = onAfterWonControlListener;
        return this;
    }

    public View getMainView () {
        return this.mainView;
    }

    public void setData (int gameLevelValue, String gameLevel, int gameStep, int gameScore, int time) {
        levelValueTV.setText(gameLevel);
        stepValueTV.setText(String.valueOf(gameStep));
        scoreValueTV.setText(String.valueOf(gameScore));
        timeValueTV.setText(FormattedData.getFormattedTime(time));

        if (new SharedData(levelHeaderTV.getContext()).getAllTimeBestScore(gameLevelValue) > gameScore) {
            dividerView7.setVisibility(View.GONE);
            newBestScoreTV.setVisibility(View.GONE);
        } else {
            dividerView7.setVisibility(View.VISIBLE);
            newBestScoreTV.setVisibility(View.VISIBLE);
        }

    }

    public void refreshViewsColor () {

        appColor.setTheme(new CommonSharedData(mainView.getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        Context context = mainView.getContext();

        sessionCompletedTV.setTextColor(context.getColor(appColor.getAppBarTitleTextColor()));
        levelHeaderTV.setTextColor(context.getColor(appColor.getResultPageScoreTitleColor()));
        levelValueTV.setTextColor(context.getColor(appColor.getResultPageScoreValueColor()));
        stepHeaderTV.setTextColor(context.getColor(appColor.getResultPageScoreTitleColor()));
        stepValueTV.setTextColor(context.getColor(appColor.getResultPageScoreValueColor()));
        scoreHeaderTV.setTextColor(context.getColor(appColor.getResultPageScoreTitleColor()));
        scoreValueTV.setTextColor(context.getColor(appColor.getResultPageScoreValueColor()));
        timeHeaderTV.setTextColor(context.getColor(appColor.getResultPageScoreTitleColor()));
        timeValueTV.setTextColor(context.getColor(appColor.getResultPageScoreValueColor()));
        newBestScoreTV.setTextColor(context.getColor(appColor.getCelebrationBlueColor()));

        newGameMB.setBackgroundTintList(ColorStateList.valueOf(context.getColor(appColor.getPrimaryBtnBackgroundColor())));
        newGameMB.setTextColor(context.getColor(appColor.getPrimaryBtnTextColor()));
        homeMB.setBackgroundTintList(ColorStateList.valueOf(context.getColor(appColor.getPrimaryBtnBackgroundColor())));
        homeMB.setTextColor(context.getColor(appColor.getPrimaryBtnTextColor()));

        dividerView1.setBackgroundColor(context.getColor(appColor.getNormalDividerColor()));
        dividerView2.setBackgroundColor(context.getColor(appColor.getNormalDividerColor()));
        dividerView3.setBackgroundColor(context.getColor(appColor.getNormalDividerColor()));
        dividerView4.setBackgroundColor(context.getColor(appColor.getNormalDividerColor()));
        dividerView5.setBackgroundColor(context.getColor(appColor.getNormalDividerColor()));
        dividerView6.setBackgroundColor(context.getColor(appColor.getNormalDividerColor()));
        dividerView7.setBackgroundColor(context.getColor(appColor.getNormalDividerColor()));

        resultDataCV.setCardBackgroundColor(context.getColor(appColor.getPopupCardBackgroundColor()));

    }

    public void hideNewSessionButton () {

        if (newGameMB != null) {
            newGameMB.setVisibility(View.GONE);
        }

    }

    private void prepare () {

        resultDataCV = mainView.findViewById(R.id.data_container_cv);
        sessionCompletedTV = mainView.findViewById(R.id.session_complete_tv);
        levelHeaderTV = mainView.findViewById(R.id.level_header_tv);
        levelValueTV = mainView.findViewById(R.id.level_value_tv);
        stepHeaderTV = mainView.findViewById(R.id.step_header_tv);
        stepValueTV = mainView.findViewById(R.id.step_value_tv);
        scoreHeaderTV = mainView.findViewById(R.id.score_header_tv);
        scoreValueTV = mainView.findViewById(R.id.score_value_tv);
        timeHeaderTV = mainView.findViewById(R.id.time_header_tv);
        timeValueTV = mainView.findViewById(R.id.time_value_tv);
        newBestScoreTV = mainView.findViewById(R.id.new_best_score_notice_tv);
        dividerView1 = mainView.findViewById(R.id.divider_view_1);
        dividerView2 = mainView.findViewById(R.id.divider_view_2);
        dividerView3 = mainView.findViewById(R.id.divider_view_3);
        dividerView4 = mainView.findViewById(R.id.divider_view_4);
        dividerView5 = mainView.findViewById(R.id.divider_view_5);
        dividerView6 = mainView.findViewById(R.id.divider_view_6);
        dividerView7 = mainView.findViewById(R.id.divider_view_7);

        homeMB = mainView.findViewById(R.id.home_mb);
        newGameMB = mainView.findViewById(R.id.new_game_mb);

        setViewsListener();

    }

    private void setViewsListener () {

        if (newGameMB != null) {
            newGameMB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onAfterWonControlListener != null)
                        onAfterWonControlListener.onNewSession();
                }
            });
        }

        if (homeMB != null) {
            homeMB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onAfterWonControlListener != null)
                        onAfterWonControlListener.onHome();
                }
            });
        }

    }

}
