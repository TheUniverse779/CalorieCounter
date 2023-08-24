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
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.caloriecounter.calorie.WeatherApplication;

import java.util.Date;

public class OpenAds {

    private static final String OPEN_TEST_ID = "ca-app-pub-3940256099942544/1033173712";

    private static final String OPEN_ID_DEFAULT = "ca-app-pub-6989606115612474/7568233016";

    private static AppOpenAd appOpenAd;

    private static boolean isOpenShowingAd = false;

    private static long loadTimeOpenAd = 0;
    public static int flagQC = 1;

    public static void initOpenAds(final Context ac, Callback callback) {

        if (appOpenAd == null || !isOpenAdsCanUse()) {
            appOpenAd = null;
            AppOpenAd.load(ac, BuildConfig.DEBUG ? OPEN_TEST_ID : OPEN_ID_DEFAULT, getAdRequest(), AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd openAd) {
                    super.onAdLoaded(openAd);
                    appOpenAd = openAd;
                    appOpenAd.setOnPaidEventListener(adValue -> {
                        try {
                            WeatherApplication.initROAS(adValue.getValueMicros(),adValue.getCurrencyCode());
                            AdjustAdRevenue adRevenue = new AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB);
                            adRevenue.setRevenue((double) (adValue.getValueMicros() / 1000000f), adValue.getCurrencyCode());
                            Adjust.trackAdRevenue(adRevenue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    loadTimeOpenAd = (new Date()).getTime();
                    if (callback != null) {
                        callback.callback();
                    }
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    appOpenAd = null;
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

    private static boolean isOpenAdsCanUse() {
        long dateDifference = (new Date()).getTime() - loadTimeOpenAd;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * 4));
    }

    private static AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    public static boolean isCanShowOpenAds() {
        return appOpenAd != null && !isOpenShowingAd && isOpenAdsCanUse();
    }

    public static void showOpenAds(final Activity context, final Callback callback) {
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
        if (flagQC == 1) {
            if (isCanShowOpenAds()) {
                appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        appOpenAd = null;
                        callback.callback();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        isOpenShowingAd = true;
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        isOpenShowingAd = false;
                        appOpenAd = null;
                        initOpenAds(context, null);
                       // InterAds.startDelay();
                        startDelay();
                        callback.callback();
                    }

                });
                appOpenAd.show(context);
            } else {
                callback.callback();
            }
        } else {
            callback.callback();
        }

    }

    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static final Runnable runnable = () -> flagQC = 1;

    public static void startDelay() {
        flagQC = 0;
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, BuildConfig.DEBUG ? 15000 : 1000 * 60);
    }

}
