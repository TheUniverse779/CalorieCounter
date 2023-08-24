package com.caloriecounter.calorie.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAdRevenue;
import com.adjust.sdk.AdjustConfig;
import com.caloriecounter.calorie.BuildConfig;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.caloriecounter.calorie.WeatherApplication;

import java.util.Date;

public class InterAds {

    private static final String INTER_TEST_ID = "ca-app-pub-3940256099942544/1033173712";

    private static final String INTER_ID_DEFAULT = "ca-app-pub-6989606115612474/5596675793";

    private static InterstitialAd mInterstitialAd;
    private static boolean isLoading = false;
    private static boolean isShowing = false;
    private static long loadTimeAd = 0;

    public static int flagQC = 1;

    public static void initInterAds(final Context ac, Callback callback) {
        if (isCanLoadAds()) {
            mInterstitialAd = null;
            isLoading = true;
            InterstitialAd.load(ac, BuildConfig.DEBUG ? INTER_TEST_ID : INTER_ID_DEFAULT, getAdRequest(), new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);
                    mInterstitialAd = interstitialAd;
                    mInterstitialAd.setOnPaidEventListener(adValue -> {
                        try {
                            WeatherApplication.initROAS(adValue.getValueMicros(),adValue.getCurrencyCode());
                            AdjustAdRevenue adRevenue = new AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB);
                            adRevenue.setRevenue((double) (adValue.getValueMicros() / 1000000f), adValue.getCurrencyCode());
                            Adjust.trackAdRevenue(adRevenue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    isLoading = false;
                    loadTimeAd = (new Date()).getTime();
                    if (callback != null) {
                        callback.callback();
                    }
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    mInterstitialAd = null;
                    isLoading = false;
                    if (callback != null) {
                        callback.callback();
                    }
                }
            });

        } else {
            if (callback != null) {
                callback.callback();
            }
        }
    }

    private static AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private static boolean isCanLoadAds() {
        if (isLoading) {
            return false;
        }
        if (isShowing) {
            return false;
        }
        if (mInterstitialAd == null) {
            return true;
        } else {
            return isAdsOverdue();
        }
    }

    private static boolean isCanShowAds() {
        if (isLoading) {
            return false;
        }
        if (isShowing) {
            return false;
        }
        if (flagQC == 0) {
            return false;
        }
        if (mInterstitialAd == null) {
            return false;
        } else {
            return !isAdsOverdue();
        }

    }

    private static boolean isAdsOverdue() {
        long dateDifference = (new Date()).getTime() - loadTimeAd;
        long numMilliSecondsPerHour = 3600000;
        return dateDifference > (numMilliSecondsPerHour * (long) 4);
    }

    public static void showAdsBreak(Activity activity, Callback callback) {
        try {
            int isPro = new Prefs(WeatherApplication.get()).getPremium();
            boolean isSub = new Prefs(WeatherApplication.get()).isRemoveAd();
            if (isPro == 1) {
                callback.callback();
                return;
            } else if (isSub) {
                callback.callback();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isCanShowAds()) {
//            Dialog dialog = new Dialog(activity, R.style.Dialog90);
//            dialog.setContentView(R.layout.inter_dialog_loading);
//            dialog.setCancelable(false);
//            dialog.show();
//            Handler handler = new Handler(Looper.getMainLooper());
//            Runnable runnable = () -> {
//                if(!activity.isFinishing()){
//                    dialog.dismiss();
//                    showAdsFull(activity, callback);
//                }
//            };
//            handler.postDelayed(runnable, 1300);
            try {
                showAdsFull(activity, callback);
            } catch (Exception e) {
                e.printStackTrace();
                callback.callback();
            }
        } else {
            callback.callback();
        }
    }

    private static void showAdsFull(final Activity context, final Callback callback) {
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                mInterstitialAd = null;
                isShowing = false;
                callback.callback();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                isShowing = true;
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                isShowing = false;
                mInterstitialAd = null;
                startDelay();
                initInterAds(context, null);
                callback.callback();
            }

        });
        mInterstitialAd.show(context);
    }

    public static boolean isShowing() {
        return isShowing;
    }

    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static final Runnable runnable = () -> flagQC = 1;

    public static void startDelay() {
        flagQC = 0;
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, BuildConfig.DEBUG ? 6000 : 30000);
    }

}
