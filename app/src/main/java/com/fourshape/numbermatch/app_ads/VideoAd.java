package com.fourshape.numbermatch.app_ads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;


import com.fourshape.easythingslib.listeners.OnBasicVideoAdListener;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.jetbrains.annotations.NotNull;

public class VideoAd {

    private String placementId = "";
    private Activity activity;
    private Context context;
    private OnBasicVideoAdListener onBasicVideoAdListener;
    private Intent intent;
    private boolean isEnabled = true;

    public VideoAd setPlacementId (String placementId) {
        this.placementId = placementId;
        return this;
    }

    public void disableAd () {
        isEnabled = false;
    }

    public boolean isDisabled () {
        return !isEnabled;
    }

    public void setTargetActivityIntent (Intent intent) {
        this.intent = intent;
    }

    public Intent getTargetActivityIntent () {
        return intent;
    }

    public VideoAd withContext (Context context) {
        this.context = context;
        return this;
    }

    public VideoAd withActivity (Activity activity) {
        this.activity = activity;
        return this;
    }

    public VideoAd setOnBasicVideoAdListener (OnBasicVideoAdListener onBasicVideoAdListener) {
        this.onBasicVideoAdListener = onBasicVideoAdListener;
        return this;
    }

    public VideoAd load () {

        try {
            MobileAds.initialize(context);
            MobileAds.setAppMuted(new SharedData(context).getAdsSoundStatus());
        } catch (Exception e) {

        }

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this.context, placementId, adRequest, interstitialAdLoadCallback);

        return this;
    }

    private final InterstitialAdLoadCallback interstitialAdLoadCallback = new InterstitialAdLoadCallback() {
        @Override
        public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);
            if (onBasicVideoAdListener != null) {
                onBasicVideoAdListener.onProceedAfter();
            }
        }

        @Override
        public void onAdLoaded(@NonNull @NotNull InterstitialAd interstitialAd) {
            super.onAdLoaded(interstitialAd);

            interstitialAd.setFullScreenContentCallback(fullScreenContentCallback);

            if (activity != null){
                if (!activity.isDestroyed()) {
                    if (isEnabled)
                        interstitialAd.show(activity);
                }
                else {
                    if (onBasicVideoAdListener != null) {
                        onBasicVideoAdListener.onProceedAfter();
                    }
                }
            } else {
                if (onBasicVideoAdListener != null) {
                    onBasicVideoAdListener.onProceedAfter();
                }
            }

        }
    };

    private final FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
        @Override
        public void onAdClicked() {
            super.onAdClicked();
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent();
            if (onBasicVideoAdListener != null) {
                onBasicVideoAdListener.onProceedAfter();
            }
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
            super.onAdFailedToShowFullScreenContent(adError);
            if (onBasicVideoAdListener != null) {
                onBasicVideoAdListener.onProceedAfter();
            }
        }

        @Override
        public void onAdImpression() {
            super.onAdImpression();
        }

        @Override
        public void onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent();
        }
    };

}
