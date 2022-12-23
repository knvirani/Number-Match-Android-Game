package com.fourshape.numbermatch.app_ads;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.fourshape.easythingslib.listeners.OnBasicVideoAdListener;
import com.fourshape.easythingslib.utils.TestDevices;
import com.fourshape.numbermatch.utils.LiveGameSettings;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class NonRewardVideoAd {

    private String placementId = "";
    private Activity activity;
    private Context context;
    private OnBasicVideoAdListener onBasicVideoAdListener;
    private boolean isEnabled = true;

    public boolean isDisabled() {
        return !isEnabled;
    }

    public NonRewardVideoAd setPlacementId (String placementId) {
        this.placementId = placementId;
        return this;
    }

    public void disableAd (){
        isEnabled = false;
        if (onBasicVideoAdListener != null) {
            onBasicVideoAdListener.onProceedAfter();
        }
    }

    public NonRewardVideoAd withContext (Context context) {
        this.context = context;
        return this;
    }

    public NonRewardVideoAd withActivity (Activity activity) {
        this.activity = activity;
        return this;
    }

    public NonRewardVideoAd setOnBasicVideoAdListener (OnBasicVideoAdListener onBasicVideoAdListener) {
        this.onBasicVideoAdListener = onBasicVideoAdListener;
        return this;
    }

    public NonRewardVideoAd load () {

        try {
            MobileAds.initialize(context);
            MobileAds.setAppMuted(new SharedData(context).getAdsSoundStatus());
        } catch (Exception e) {}

        AdRequest adRequest = new AdRequest.Builder().build();
        new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(TestDevices.ADMOB_TEST_DEVICE)).build();
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
                    if (isEnabled) {
                        interstitialAd.show(activity);
                    }
                } else {
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


