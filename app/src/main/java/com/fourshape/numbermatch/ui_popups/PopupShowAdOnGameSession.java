package com.fourshape.numbermatch.ui_popups;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fourshape.numbermatch.R;
import com.fourshape.numbermatch.app_ads.NonRewardVideoAd;
import com.fourshape.numbermatch.app_color.AppColor;
import com.fourshape.numbermatch.utils.DimPopupWindow;
import com.fourshape.numbermatch.utils.MakeLog;
import com.fourshape.numbermatch.utils.SharedData;
import com.fourshape.easythingslib.listeners.OnBasicVideoAdListener;
import com.fourshape.easythingslib.listeners.OnDialogDismissListener;
import com.fourshape.easythingslib.listeners.OnDialogShowListener;
import com.fourshape.easythingslib.listeners.OnInternetStatusListener;
import com.fourshape.easythingslib.utils.CheckInternet;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class PopupShowAdOnGameSession {

    private static final String TAG = "PopupShowAdOnGameSession";

    private final PopupWindow popupWindow;
    private NonRewardVideoAd nonRewardVideoAd;
    private OnDialogDismissListener onDialogDismissListener;
    private OnDialogShowListener onDialogShowListener;

    public PopupShowAdOnGameSession (Context context, Activity activity, String placementId) {

        final View view = LayoutInflater.from(context).inflate(R.layout.popup_ads_on_game_session, null);

        MaterialCardView parentCV = view.findViewById(R.id.parent_cv);
        CircularProgressIndicator progressIndicator = view.findViewById(R.id.progress_circular);
        TextView loadingTV = view.findViewById(R.id.loading_ads_tv);

        AppColor appColor = new AppColor();
        appColor.setTheme(new CommonSharedData(context, SharedData.SHARED_PREF_TITLE).getAppCurrentTheme());

        parentCV.setCardBackgroundColor(ColorStateList.valueOf(context.getColor(appColor.getPopupCardBackgroundColor())));
        progressIndicator.setIndicatorColor(context.getColor(appColor.getProgressCircularIndicatorColor()));
        loadingTV.setTextColor(ColorStateList.valueOf(context.getColor(appColor.getAppBarTitleTextColor())));

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false;

        popupWindow = new PopupWindow(view, width, height, focusable);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                nonRewardVideoAd.disableAd();
                MakeLog.info(TAG, "Dismissed");
                nonRewardVideoAd = null;
                if (onDialogDismissListener != null) {
                    onDialogDismissListener.onDismiss();
                }
            }
        });

        nonRewardVideoAd = new NonRewardVideoAd().setOnBasicVideoAdListener(new OnBasicVideoAdListener() {
            @Override
            public void onShowRequest() {

            }

            @Override
            public void onProceedAfter() {
                MakeLog.info(TAG, "Dismissing After Ad-Watch.");
                popupWindow.dismiss();

            }
        }).withActivity(activity).withContext(context).setPlacementId(placementId);

        new CheckInternet().setOnInternetStatusListener(new OnInternetStatusListener() {
            @Override
            public void onOnline() {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        showDialogAndLoadAd(view);
                    }
                });

            }

            @Override
            public void onOffline() {
                MakeLog.info(TAG, "Can't load as no internet available.");
            }
        }).now();

    }

    private void showDialogAndLoadAd (View view) {

        if (onDialogShowListener != null) {

            onDialogShowListener.onShow();
            MakeLog.info(TAG, "Showing.");

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            DimPopupWindow.dimBehind(popupWindow);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MakeLog.info(TAG, "Loading Session Ad");
                    if (nonRewardVideoAd != null)
                        nonRewardVideoAd.load();
                    else {
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                }
            }, 750);

        }

    }

    public PopupShowAdOnGameSession setOnDialogShowListener(OnDialogShowListener onDialogShowListener) {
        this.onDialogShowListener = onDialogShowListener;
        return this;
    }

    public PopupShowAdOnGameSession setOnDialogDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.onDialogDismissListener = onDialogDismissListener;
        return this;
    }
}
