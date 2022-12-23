package com.fourshape.numbermatch.ui_popups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.fourshape.easythingslib.listeners.OnInternetStatusListener;
import com.fourshape.easythingslib.utils.CheckInternet;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.numbermatch.app_ads.VideoAd;
import com.fourshape.easythingslib.listeners.OnBasicVideoAdListener;
import com.fourshape.numbermatch.GameSessionActivity;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_ads.PlacementIds;
import com.fourshape.numbermatch.puzzle_core.GameLevel;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.utils.BundleParams;
import com.fourshape.numbermatch.utils.DimPopupWindow;
import com.fourshape.numbermatch.puzzle_core.GameType;
import com.fourshape.numbermatch.utils.MakeLog;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.lang.ref.WeakReference;

public class PopupGameLevel {

    private static final String TAG = "PopupGameLevel";

    private View view;
    private PopupWindow popupWindow;
    private MaterialCardView parentCV, easyCV, mediumCV, hardCV, closeCV;
    private TextView easyTV, mediumTV, hardTV;
    private AppColor appColor;
    private Activity activity;
    private LinearLayoutCompat contentLayout;
    private CircularProgressIndicator progressIndicator;
    private int delayMilliSeconds = 500;
    private CommonSharedData commonSharedData;
    private VideoAd nonRewardVideoAd;
    private String adPlacementId;

    private int gameLevel = -1;

    public PopupGameLevel (Context context, Activity activity) {
        this.activity = activity;
        this.view = LayoutInflater.from(context).inflate(R.layout.popup_game_level, null);
        commonSharedData = new CommonSharedData(context, SharedData.SHARED_PREF_TITLE);
        appColor = new AppColor();
        appColor.setTheme(commonSharedData.getAppCurrentTheme());
        preparePopup();
    }

    private void preparePopup () {

        this.contentLayout = this.view.findViewById(R.id.content_layout);
        this.progressIndicator = this.view.findViewById(R.id.progress_circular);
        this.closeCV = this.view.findViewById(R.id.close_cv);
        this.parentCV = this.view.findViewById(R.id.parent_cv);
        this.easyCV = this.view.findViewById(R.id.easy_cv);
        this.mediumCV = this.view.findViewById(R.id.medium_cv);
        this.hardCV = this.view.findViewById(R.id.hard_cv);
        this.easyTV = this.view.findViewById(R.id.easy_tv);
        this.mediumTV = this.view.findViewById(R.id.medium_tv);
        this.hardTV = this.view.findViewById(R.id.hard_tv);

        resetViewsColor();
        setViewsListener();

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false;

        popupWindow = new PopupWindow(this.view, width, height, focusable);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (nonRewardVideoAd != null) {
                    nonRewardVideoAd.disableAd();
                }
            }
        });

    }

    public PopupGameLevel setAdPlacementId (String adPlacementId) {
        this.adPlacementId = adPlacementId;
        return this;
    }

    public void show () {

        if (this.view == null) {
            MakeLog.error(TAG, "View is NULL.");
            return;
        }

        if (this.popupWindow == null) {
            MakeLog.error(TAG, "Window is NULL");
            return;
        }

        if (this.popupWindow.isShowing()) {
            MakeLog.error(TAG, "Window is already visible");
            return;
        }

        if (activity != null) {
            if (activity.isDestroyed() || activity.isFinishing())
                return;
        } else {
            return;
        }

        popupWindow.setOverlapAnchor(true);
        popupWindow.setAttachedInDecor(true);
        popupWindow.showAtLocation(this.view, Gravity.NO_GRAVITY, 0, 0);
        DimPopupWindow.dimBehindWithFactor(popupWindow, 0.5f);
        MakeLog.info(TAG, "Popup is Showing.");

    }

    private void resetViewsColor () {

        this.parentCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getPopupCardBackgroundColor()));
        this.easyCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getEasyLevelCardBackgroundColor()));
        this.mediumCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getMediumLevelCardBackgroundColor()));
        this.hardCV.setCardBackgroundColor(this.view.getContext().getColor(appColor.getHardLevelCardBackgroundColor()));
        this.easyTV.setTextColor(this.view.getContext().getColor(appColor.getDifficultyLevelTitleColor()));
        this.mediumTV.setTextColor(this.view.getContext().getColor(appColor.getDifficultyLevelTitleColor()));
        this.hardTV.setTextColor(this.view.getContext().getColor(appColor.getDifficultyLevelTitleColor()));
        this.progressIndicator.setIndicatorColor(this.view.getContext().getColor(appColor.getProgressCircularIndicatorColor()));
        this.progressIndicator.setTrackColor(this.view.getContext().getColor(this.appColor.getGameSessionLinearProgressTrackColor()));

    }

    private void setViewsListener () {

        this.closeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null)
                    if (popupWindow.isShowing())
                        popupWindow.dismiss();
            }
        });

        this.easyCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeCV.setVisibility(View.INVISIBLE);
                gameLevel = GameLevel.EASY;
                startOp();
            }
        });

        this.mediumCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeCV.setVisibility(View.INVISIBLE);
                gameLevel = GameLevel.MEDIUM;
                startOp();
            }
        });

        this.hardCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeCV.setVisibility(View.INVISIBLE);
                gameLevel = GameLevel.HARD;
                startOp();
            }
        });

    }

    private void startOp () {

        triggerProgressLayout();

        if (commonSharedData.isFirstTime()) {
            commonSharedData.setFirstTime();
        }

        startGame();

    }

    private void triggerProgressLayout () {

        if (this.contentLayout != null) {
            if (this.contentLayout.getVisibility() == View.VISIBLE)
                this.contentLayout.setVisibility(View.INVISIBLE);
        }

        if (this.progressIndicator != null) {
            if (this.progressIndicator.getVisibility() != View.VISIBLE)
                this.progressIndicator.setVisibility(View.VISIBLE);
        }

    }

    private void startVideoAd () {

        nonRewardVideoAd = new VideoAd()
                .withContext(view.getContext())
                .withActivity(activity)
                .setOnBasicVideoAdListener(onBasicVideoAdListener)
                .setPlacementId(adPlacementId);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nonRewardVideoAd.load();
            }
        }, delayMilliSeconds);

    }

    private void startGame () {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (popupWindow != null) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        Intent intent = new Intent(view.getContext(), GameSessionActivity.class);
                        intent.putExtra(BundleParams.GAME_LEVEL, gameLevel);
                        intent.putExtra(BundleParams.GAME_TYPE, GameType.GENERAL);
                        view.getContext().startActivity(intent);
                    }
                }

            }
        }, delayMilliSeconds);

    }

    private final OnInternetStatusListener onInternetStatusListener = new OnInternetStatusListener() {
        @Override
        public void onOnline() {

            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startVideoAd();
                    }
                });
            }

        }

        @Override
        public void onOffline() {

            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startGame();
                    }
                });
            }

        }
    };

    private final OnBasicVideoAdListener onBasicVideoAdListener = new OnBasicVideoAdListener() {
        @Override
        public void onShowRequest() {

        }

        @Override
        public void onProceedAfter() {
            startGame();
        }
    };

}
