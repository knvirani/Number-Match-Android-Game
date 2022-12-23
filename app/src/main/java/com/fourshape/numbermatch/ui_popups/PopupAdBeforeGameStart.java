package com.fourshape.numbermatch.ui_popups;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fourshape.easythingslib.listeners.OnDialogDismissListener;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_ads.VideoAd;
import com.fourshape.numbermatch.app_ads.PlacementIds;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.utils.DimPopupWindow;
import com.fourshape.numbermatch.utils.MakeLog;
import com.fourshape.easythingslib.listeners.OnBasicVideoAdListener;
import com.fourshape.easythingslib.listeners.OnInternetStatusListener;
import com.fourshape.easythingslib.utils.CheckInternet;
import com.fourshape.numbermatch.app_ads.VideoAd;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.lang.ref.WeakReference;

public class PopupAdBeforeGameStart {

    private static final String TAG = "PopupAdBeforeGameStart";

    private View mainView;
    private MaterialCardView parentCV;
    private CircularProgressIndicator progressIndicator;
    private Activity activity;
    private Intent intent;
    private PopupWindow popupWindow;
    private VideoAd nonRewardVideoAd;
    private String adPlacementId;

    private OnDialogDismissListener onDialogDismissListener;

    public PopupAdBeforeGameStart (Context context) {
        this.mainView = LayoutInflater.from(context).inflate(R.layout.popup_ads_loading_before_game, null);
        prepare();
    }

    public PopupAdBeforeGameStart setOnDialogDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.onDialogDismissListener = onDialogDismissListener;
        return this;
    }

    public PopupAdBeforeGameStart with (Activity activity, Intent intent) {
        this.activity = activity;
        this.intent = intent;
        return this;
    }

    public PopupAdBeforeGameStart setAdPlacementId (String adPlacementId) {
        this.adPlacementId = adPlacementId;
        return this;
    }

    public void show () {

        if (this.popupWindow == null) {
            MakeLog.error(TAG, "PopupWindow is NULL");
            return;
        }

        if (this.mainView == null) {
            MakeLog.error(TAG, "HolderView is NULL");
            return;
        }

        popupWindow.showAtLocation(this.mainView, Gravity.BOTTOM, 0, 0);
        DimPopupWindow.lightDimBehind(popupWindow);
        MakeLog.info(TAG, "Popup is Showing.");

        nonRewardVideoAd = new VideoAd()
                .withContext(new WeakReference<Context>(mainView.getContext()).get())
                .withActivity(new WeakReference<Activity>(activity).get())
                .setOnBasicVideoAdListener(onBasicVideoAdListener)
                .setPlacementId(adPlacementId);

        new CheckInternet().setOnInternetStatusListener(onInternetStatusListener).now();

    }

    private void prepare () {

        progressIndicator = this.mainView.findViewById(R.id.progress_circular);
        parentCV = this.mainView.findViewById(R.id.parent_cv);

        AppColor appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(mainView.getContext(), SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        parentCV.setCardBackgroundColor(ColorStateList.valueOf(mainView.getContext().getColor(appColor.getPopupCardBackgroundColor())));
        progressIndicator.setIndicatorColor(mainView.getContext().getColor(appColor.getProgressCircularIndicatorColor()));

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false;

        popupWindow = new PopupWindow(mainView, width, height, focusable);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                if (onDialogDismissListener != null) {
                    onDialogDismissListener.onDismiss();
                }

                if (nonRewardVideoAd != null) {
                    nonRewardVideoAd.disableAd();
                }
            }
        });

    }

    private final OnInternetStatusListener onInternetStatusListener = new OnInternetStatusListener() {
        @Override
        public void onOnline() {

            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nonRewardVideoAd.load();
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
                        if (popupWindow != null) {

                            if (popupWindow.isShowing()) {
                                popupWindow.dismiss();
                                mainView.getContext().startActivity(intent);
                            }

                        }

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

            if (nonRewardVideoAd != null) {

                if (!nonRewardVideoAd.isDisabled()) {
                    if (popupWindow != null) {
                        if (popupWindow.isShowing())
                            popupWindow.dismiss();
                    }

                    mainView.getContext().startActivity(intent);
                } else {
                    if (popupWindow != null) {
                        if (popupWindow.isShowing())
                            popupWindow.dismiss();
                    }
                }

            }

        }

    };

}
