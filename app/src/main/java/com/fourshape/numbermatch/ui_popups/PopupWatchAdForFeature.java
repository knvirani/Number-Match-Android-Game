package com.fourshape.numbermatch.ui_popups;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.fourshape.easythingslib.listeners.OnInternetStatusListener;
import com.fourshape.easythingslib.listeners.OnRewardedAdItemAccountListener;
import com.fourshape.easythingslib.ui_popups.PopupGujMCQAd;
import com.fourshape.easythingslib.ui_popups.structure.GujMCQAdSet;
import com.fourshape.easythingslib.utils.CheckInternet;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.DimPopupWindow;
import com.fourshape.easythingslib.utils.RandomNumber;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_ads.CombinedVideoAd;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class PopupWatchAdForFeature {

    private View mainView;
    private MaterialCardView parentCV;
    private TextView headerTV, bodyTV;
    private MaterialButton watchAdMB, closeMB, statusMB, closeWindowMB;
    private CircularProgressIndicator progressIndicator;
    private LinearLayoutCompat btnContainer, statusContainer;
    private OnRewardedAdItemAccountListener onRewardedAdItemAccountListener;
    private PopupWindow popupWindow;
    private Activity activity;
    private AppColor appColor;
    private boolean hasRewarded = false;
    private int errorCode = -1;

    public PopupWatchAdForFeature(Context context) {

        mainView = LayoutInflater.from(context).inflate(R.layout.popup_watch_ad_for_feature, null);
        appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(context, SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        prepare();
        refreshViewsColor();
        setViewsClickListener();
        prepareWindow();

    }

    private void reset () {

        hasRewarded = false;
        errorCode = -1;

        if (btnContainer.getVisibility() != View.VISIBLE)
            btnContainer.setVisibility(View.VISIBLE);

        if (statusContainer.getVisibility() == View.VISIBLE)
            statusContainer.setVisibility(View.INVISIBLE);

        if (progressIndicator.getVisibility() == View.VISIBLE)
            progressIndicator.setVisibility(View.GONE);

    }

    public PopupWatchAdForFeature setActivity (Activity activity) {
        this.activity = activity;
        return this;
    }

    public PopupWatchAdForFeature setOnRewardedAdItemAccountListener(OnRewardedAdItemAccountListener onRewardedAdItemAccountListener) {
        this.onRewardedAdItemAccountListener = onRewardedAdItemAccountListener;
        return this;
    }

    public void show () {

        if (this.popupWindow == null)
            return;

        if (this.popupWindow.isShowing())
            return;

        if (this.mainView == null)
            return;

        reset();

        popupWindow.showAtLocation(mainView, Gravity.CENTER, 0, 0);
        DimPopupWindow.dimBehindWithFactor(popupWindow, 0.5f);

    }

    private void prepare () {
        parentCV = mainView.findViewById(R.id.parent_cv);
        headerTV = mainView.findViewById(R.id.popup_header);
        bodyTV = mainView.findViewById(R.id.popup_body);
        watchAdMB = mainView.findViewById(R.id.watch_ad_mb);
        closeMB = mainView.findViewById(R.id.cancel_watch_ad_mb);
        progressIndicator = mainView.findViewById(R.id.progress_circular);
        btnContainer = mainView.findViewById(R.id.btn_container);
        statusContainer = mainView.findViewById(R.id.status_container);
        statusMB = mainView.findViewById(R.id.status_mb);
        closeWindowMB = mainView.findViewById(R.id.close_mb);
    }

    private void prepareWindow () {

        int width = LinearLayoutCompat.LayoutParams.MATCH_PARENT;
        int height = LinearLayoutCompat.LayoutParams.MATCH_PARENT;
        boolean focusable = false;

        popupWindow = new PopupWindow(mainView, width, height, focusable);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                if (hasRewarded) {
                    if (onRewardedAdItemAccountListener != null)
                        onRewardedAdItemAccountListener.onItemRewarded(true);
                } else {
                    if (onRewardedAdItemAccountListener != null)
                        onRewardedAdItemAccountListener.onFailed(errorCode);
                }


            }
        });

    }

    private void refreshViewsColor () {

        Context context = mainView.getContext();

        if (context == null)
            return;

        parentCV.setCardBackgroundColor(context.getColor(appColor.getPopupCardBackgroundColor()));
        headerTV.setTextColor(context.getColor(appColor.getPopupTitleTextColor()));
        bodyTV.setTextColor(context.getColor(appColor.getPopupBodyTextColor()));
        watchAdMB.setBackgroundTintList(ColorStateList.valueOf(context.getColor(appColor.getPrimaryBtnBackgroundColor())));
        watchAdMB.setTextColor(context.getColor(appColor.getPrimaryBtnTextColor()));
        closeMB.setBackgroundTintList(ColorStateList.valueOf(context.getColor(appColor.getPopupSecondaryButtonBackgroundColor())));
        closeMB.setTextColor(context.getColor(appColor.getPopupSecondaryButtonTextColor()));
        progressIndicator.setIndicatorColor(context.getColor(appColor.getProgressCircularIndicatorColor()));
        progressIndicator.setTrackColor(context.getColor(appColor.getGameSessionLinearProgressTrackColor()));
        closeWindowMB.setBackgroundTintList(ColorStateList.valueOf(context.getColor(appColor.getPrimaryBtnBackgroundColor())));
        closeWindowMB.setTextColor(context.getColor(appColor.getPrimaryBtnTextColor()));
        statusMB.setTextColor(context.getColor(appColor.getSuccessTextColor()));
        statusMB.setIconTint(ColorStateList.valueOf(context.getColor(appColor.getSuccessTextColor())));

    }

    private void setViewsClickListener () {

        if (closeWindowMB != null) {
            closeWindowMB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });
        }

        if (watchAdMB != null) {
            watchAdMB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (btnContainer != null) {
                        if (btnContainer.getVisibility() == View.VISIBLE)
                            btnContainer.setVisibility(View.INVISIBLE);
                    }

                    if (progressIndicator != null) {
                        if (progressIndicator.getVisibility() != View.VISIBLE)
                            progressIndicator.setVisibility(View.VISIBLE);
                    }

                    new CheckInternet().setOnInternetStatusListener(onInternetStatusListener).now();

                }
            });
        }

        if (closeMB != null) {
            closeMB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });
        }

    }

    private final OnInternetStatusListener onInternetStatusListener = new OnInternetStatusListener() {
        @Override
        public void onOnline() {

            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new CombinedVideoAd(mainView.getContext(), activity).setOnRewardedAdItemAccountListener(onRewardedAdItemAccountListenerForPopup).load();
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
                        rewardError(0);
                    }
                });
            }

        }
    };

    private void rewardError (int errorCode) {

        hasRewarded = false;
        PopupWatchAdForFeature.this.errorCode = errorCode;

        if (progressIndicator != null){
            if (progressIndicator.getVisibility() == View.VISIBLE) {
                progressIndicator.setVisibility(View.GONE);
            }
        }

        if (statusContainer != null){
            if (statusContainer.getVisibility() != View.VISIBLE) {
                statusContainer.setVisibility(View.VISIBLE);
            }
        }

        if (errorCode == 0) {
            if (bodyTV != null) {
                bodyTV.setText("Your internet connection looks like offline.");
            }
        }

        if (statusMB != null) {
            statusMB.setIcon(mainView.getContext().getDrawable(R.drawable.ic_close));
            statusMB.setBackgroundTintList(ColorStateList.valueOf(mainView.getContext().getColor(appColor.getErrorBackgroundColor())));
        }

    }

    private void rewardItem () {
        hasRewarded = true;

        if (progressIndicator != null)
            if (progressIndicator.getVisibility() == View.VISIBLE)
                progressIndicator.setVisibility(View.GONE);

        if (statusContainer != null)
            if (statusContainer.getVisibility() != View.VISIBLE)
                statusContainer.setVisibility(View.VISIBLE);

        if (statusMB != null) {
            statusMB.setIcon(mainView.getContext().getDrawable(R.drawable.ic_filled_check));
            statusMB.setBackgroundTintList(ColorStateList.valueOf(mainView.getContext().getColor(appColor.getSuccessBackgroundColor())));
            statusMB.setText("Rewarded Successfully.");
        }

        if (closeWindowMB != null) {
            closeWindowMB.setText("Close");
        }
    }

    private final OnRewardedAdItemAccountListener onRewardedAdItemAccountListenerForPopup = new OnRewardedAdItemAccountListener() {
        @Override
        public void onItemRewarded(boolean isRewarded) {
            rewardItem();
        }

        @Override
        public void onFailed(int errCode) {
            errorCode = errCode;
            if (errorCode == 3 || errorCode == 0) {

                AppColor appColor = new AppColor();
                appColor.setTheme(new CommonSharedData(mainView.getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

                new PopupGujMCQAd(mainView.getContext(), false)
                        .setOnRewardedAdItemAccountListener(onRewardedAdItemAccountListenerForPopup)
                        .setAdSet(new GujMCQAdSet()
                                .setCountryFlag(R.drawable.ic_united_states_of_america)
                                .setIdToSkip(GujMCQAdSet.NUMBER_MATCH)
                                .setAppId(RandomNumber.get(GujMCQAdSet.SUDOKU_16X16, GujMCQAdSet.NUMBER_MATCH)).getSet()
                        )
                        .setBackgroundColor(appColor.getPopupCardBackgroundColor())
                        .setAppTitleTextColor(appColor.getPopupTitleTextColor())
                        .setAppTagLineTextColor(appColor.getPopupBodyTextColor())
                        .setAppPopularityTextColor(appColor.getPopupBodyTextColor())
                        .setGetAppMBColor(
                                appColor.getPrimaryBtnBackgroundColor(),appColor.getPrimaryBtnTextColor(), appColor.getPrimaryBtnTextColor())
                        .setRewardMBColor(
                                appColor.getHomeSecondaryBtnBackgroundColor(), appColor.getPopupBodyTextColor(), appColor.getPopupBodyTextColor()
                        ).start();

            } else {
                rewardError(errorCode);
            }
        }
    };

}
