package com.caloriecounter.calorie.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAdRevenue;
import com.adjust.sdk.AdjustConfig;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.caloriecounter.calorie.BuildConfig;
import com.caloriecounter.calorie.WeatherApplication;

import java.util.Date;

public class RewardedAds {
    private static final String REWARD_TEST_ID = "ca-app-pub-3940256099942544/5224354917";
    private static final String REWARD_ID_DEFAULT = "ca-app-pub-6989606115612474/6587173894";

    public static final String REWARD_ID_HIGH = "";
    private static RewardedAd mRewardedAd;
    private static boolean isLoading = false;
    private static boolean isShowing = false;
    private static long loadTimeAd = 0;
    private static boolean isUserEarned;

    public static void initRewardAds(final Context ac, Callback callback) {
        if (isCanLoadAds()) {
            mRewardedAd = null;
            isLoading = true;
            RewardedAd.load(ac, BuildConfig.DEBUG ? REWARD_TEST_ID : REWARD_ID_DEFAULT, new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                    super.onAdLoaded(rewardedAd);
                    Log.e("xxx", "onAdLoaded: ");
                    mRewardedAd = rewardedAd;
                    mRewardedAd.setOnPaidEventListener(adValue -> {
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
                    Log.e("xxx", "onAdFailedToLoad: ");
                    mRewardedAd = null;
                    isLoading = false;
                    if (callback != null) {
                        callback.callback();
                    }
                }
            });
        }
    }

    private static boolean isCanLoadAds() {
        if (isLoading) {
            return false;
        }
        if (isShowing) {
            return false;
        }
//        if (mRewardedAd == null) {
//            return true;
//        } else {
//            return isAdsOverdue();
//        }
        return true;
    }
    private static boolean isAdsOverdue() {
        long dateDifference = (new Date()).getTime() - loadTimeAd;
        long numMilliSecondsPerHour = 3600000;
        return dateDifference > (numMilliSecondsPerHour * (long) 4);
    }
    public static boolean isCanShowAds() {
        if (isLoading) {
            return false;
        }
        if (isShowing) {
            return false;
        }
        if (mRewardedAd == null) {
            return false;
        } else {
            return !isAdsOverdue();
        }

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
        isUserEarned = false;
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                Log.e("xxx", "onAdFailedToShowFullScreenContent: ");
                mRewardedAd = null;
                isShowing = false;
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                Log.e("xxx", "onAdShowedFullScreenContent: ");
                isShowing = true;
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                Log.e("xxx", "onAdDismissedFullScreenContent: ");
                isShowing = false;
                mRewardedAd = null;
                initRewardAds(context, null);
                if(isUserEarned){
                    if(callback != null){
                        callback.callback();
                    }
                }
            }

        });
        mRewardedAd.show(context, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                Log.e("xxx", "onUserEarnedReward: ");
                isUserEarned = true;
            }
        });
    }

    public static boolean isShowing() {
        return isShowing;
    }


}
