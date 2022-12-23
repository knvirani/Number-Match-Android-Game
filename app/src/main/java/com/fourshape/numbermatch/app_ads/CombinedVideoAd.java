package com.fourshape.numbermatch.app_ads;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.fourshape.easythingslib.listeners.OnRewardedAdItemAccountListener;
import com.fourshape.easythingslib.utils.CommonSharedData;
import com.fourshape.easythingslib.utils.TestDevices;
import com.fourshape.numbermatch.utils.MakeLog;
import com.fourshape.numbermatch.utils.SharedData;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public class CombinedVideoAd {

    private static final String TAG = "VideoAd";
    private static final String REWARDED_AD_TAG = TAG + " " + "RewardedAd";
    private static final String REWARDED_INTERSTITIAL_AD_TAG = TAG + " " + "RewardedInterstitialAd";
    private static final String INTERSTITIAL_AD_TAG = TAG + " " + "InterstitialAd";
    private static final String REWARDED_VIDEO_AD = PlacementIds.REWARDED_FEATURE_BOX_AD;
    private static final String REWARDED_INTERSTITIAL_AD = PlacementIds.REWARDED_INTERSTITIAL_FEATURE_BOX_AD;
    private static final String INTERSTITIAL_AD = PlacementIds.INTERSTITIAL_FEATURE_BOX_AD;

    private Context context;
    private Activity activity;

    private RewardedAd mRewardedAd;
    private RewardedInterstitialAd mRewardedInterstitialAd;

    private boolean isRewardedAdLoaded, isRewardedInterstitialAdLoaded;

    private OnRewardedAdItemAccountListener onRewardedAdItemAccountListener;

    public CombinedVideoAd (Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.isRewardedAdLoaded = false;
        this.isRewardedInterstitialAdLoaded = false;
    }

    public CombinedVideoAd setOnRewardedAdItemAccountListener (OnRewardedAdItemAccountListener rewardItemAccountListener) {
        this.onRewardedAdItemAccountListener = rewardItemAccountListener;
        return this;
    }

    public CombinedVideoAd load () {
        loadRewardedVideoAd();
        return this;
    }

    private void loadRewardedVideoAd () {

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull @NotNull InitializationStatus initializationStatus) {

                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {

                    AdapterStatus status = statusMap.get(adapterClass);

                    if (status != null) {
                        MakeLog.info(TAG, String.format(Locale.getDefault(),
                                "Adapter name: %s, Description: %s, Latency: %d",
                                adapterClass, status.getDescription(), status.getLatency()));
                    }

                }

                MobileAds.setAppMuted(new SharedData(context).getAdsSoundStatus());

                AdRequest adRequest = new AdRequest.Builder().build();
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList(TestDevices.ADMOB_TEST_DEVICE)).build();

                RewardedAd.load(context, REWARDED_VIDEO_AD, adRequest, rewardedAdLoadCallback);

            }
        });

    }

    private void loadRewardedInterstitialAd () {

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedInterstitialAd.load(context, REWARDED_INTERSTITIAL_AD, adRequest, rewardedInterstitialAdLoadCallback);

    }

    private void loadInterstitialAd () {

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, INTERSTITIAL_AD, adRequest, interstitialAdLoadCallback);

    }

    private InterstitialAdLoadCallback interstitialAdLoadCallback = new InterstitialAdLoadCallback() {
        @Override
        public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {

            super.onAdFailedToLoad(loadAdError);

            MakeLog.error(INTERSTITIAL_AD_TAG, "onAdFailedToLoad");
            MakeLog.error(INTERSTITIAL_AD_TAG, "Error: " + loadAdError.toString());

            if (onRewardedAdItemAccountListener != null) {
                onRewardedAdItemAccountListener.onFailed(loadAdError.getCode());
            }

        }

        @Override
        public void onAdLoaded(@NonNull @NotNull InterstitialAd interstitialAd) {

            super.onAdLoaded(interstitialAd);

            interstitialAd.setFullScreenContentCallback(fullScreenContentCallback);

            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        interstitialAd.show(activity);
                    }
                });
            }

        }
    };

    private RewardedInterstitialAdLoadCallback rewardedInterstitialAdLoadCallback = new RewardedInterstitialAdLoadCallback() {
        @Override
        public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {

            super.onAdFailedToLoad(loadAdError);

            isRewardedInterstitialAdLoaded = false;

            MakeLog.error(REWARDED_INTERSTITIAL_AD_TAG, "onAdFailedToLoad");
            MakeLog.error(REWARDED_INTERSTITIAL_AD_TAG, "Error: " + loadAdError.toString());

            loadInterstitialAd();

        }

        @Override
        public void onAdLoaded(@NonNull @NotNull RewardedInterstitialAd rewardedInterstitialAd) {

            super.onAdLoaded(rewardedInterstitialAd);

            MakeLog.info(REWARDED_INTERSTITIAL_AD_TAG, "onAdLoaded");
            MakeLog.info(REWARDED_INTERSTITIAL_AD_TAG, "Ads Loaded by: " + rewardedInterstitialAd.getResponseInfo().getMediationAdapterClassName());

            mRewardedInterstitialAd = rewardedInterstitialAd;
            mRewardedInterstitialAd.setFullScreenContentCallback(fullScreenContentCallback);

            isRewardedInterstitialAdLoaded = true;

            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRewardedInterstitialAd.show(activity, onUserEarnedRewardListener);
                    }
                });
            }


        }
    };

    private RewardedAdLoadCallback rewardedAdLoadCallback = new RewardedAdLoadCallback() {
        @Override
        public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {

            super.onAdFailedToLoad(loadAdError);

            MakeLog.error(REWARDED_AD_TAG, "onAdFailedToLoad");
            MakeLog.error(REWARDED_AD_TAG, "Error: " + loadAdError.toString());

            isRewardedAdLoaded = false;

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadRewardedInterstitialAd();
                }
            });

        }

        @Override
        public void onAdLoaded(@NonNull @NotNull RewardedAd rewardedAd) {

            super.onAdLoaded(rewardedAd);

            MakeLog.info(REWARDED_AD_TAG, "onAdLoaded");
            MakeLog.info(REWARDED_AD_TAG, "Ads Loaded by: " + rewardedAd.getResponseInfo().getMediationAdapterClassName());

            mRewardedAd = rewardedAd;
            mRewardedAd.setFullScreenContentCallback(fullScreenContentCallback);
            isRewardedAdLoaded = true;

            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRewardedAd.show(activity, onUserEarnedRewardListener);
                    }
                });
            }

        }

    };

    private OnUserEarnedRewardListener onUserEarnedRewardListener = new OnUserEarnedRewardListener() {
        @Override
        public void onUserEarnedReward(@NonNull @NotNull RewardItem rewardItem) {
            MakeLog.info(REWARDED_AD_TAG, "onUserEarnedReward");
            if (onRewardedAdItemAccountListener != null) {
                onRewardedAdItemAccountListener.onItemRewarded(true);
            }
        }
    };

    private FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
        @Override
        public void onAdClicked() {
            super.onAdClicked();
            MakeLog.info(REWARDED_AD_TAG, "onAdClicked");
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent();
            MakeLog.info(REWARDED_AD_TAG, "onAdDismissedFullScreenContent");

            if (!isRewardedAdLoaded && !isRewardedInterstitialAdLoaded) {
                if (onRewardedAdItemAccountListener != null) {
                    onRewardedAdItemAccountListener.onItemRewarded(true);
                }
            }

        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
            super.onAdFailedToShowFullScreenContent(adError);
            MakeLog.info(REWARDED_AD_TAG, "onAdFailedToShowFullScreenContent");
            MakeLog.error(REWARDED_AD_TAG, "Error Code: " + adError.getCode());
            MakeLog.error(REWARDED_AD_TAG, "Error Message: " + adError.getMessage());
            if (onRewardedAdItemAccountListener != null) {
                onRewardedAdItemAccountListener.onFailed(3);
            }
        }

        @Override
        public void onAdImpression() {
            super.onAdImpression();
            MakeLog.info(REWARDED_AD_TAG, "onAdImpression");
        }

        @Override
        public void onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent();
            MakeLog.info(REWARDED_AD_TAG, "onAdShowedFullScreenContent");
        }
    };


}
