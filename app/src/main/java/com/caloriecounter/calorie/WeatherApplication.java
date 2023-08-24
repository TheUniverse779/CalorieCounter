package com.caloriecounter.calorie;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.caloriecounter.calorie.ads.OpenAdsHelper;
import com.caloriecounter.calorie.ads.Prefs;
import com.caloriecounter.calorie.database.AppDatabase;
import com.caloriecounter.calorie.ui.charging.services.WorkService;
import com.caloriecounter.calorie.ui.livewallpaper.service.WallpaperCard;
import com.caloriecounter.calorie.util.PreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class WeatherApplication extends Application{
    public static String location = "";
    private static SharedPreferences sharedPref;

    private AppDatabase database;
    private static WeatherApplication instance;


    public static FirebaseAnalytics mFirebaseAnalytics;



    Prefs prefs;


    private static final String TAG = "LWApplication";
    public static final String JSON_FILE_NAME = "data.json";
    private static final String CURRENT_CARD_PREF = "currentWallpaperCard";
    public static final String OPTIONS_PREF = "options";
    public static final String SLIDE_WALLPAPER_KEY = "slideWallpaper";
    private static final String INTERNAL_WALLPAPER_IMAGE_PATH = "wallpapers/fire-rain/fire-rain-512x384.webp";
    private static final String INTERNAL_WALLPAPER_VIDEO_PATH = "wallpapers/fire-rain/fire-rain-720x720.mp4";
    private static List<WallpaperCard> cards = null;
    private static WallpaperCard currentWallpaperCard = null;
    private static WallpaperCard previewWallpaperCard = null;

    public static List<WallpaperCard> getCards(@NonNull final Context context) {
        if (cards == null) {
            initCards(context);
        }
        return cards;
    }

    public static WallpaperCard getCurrentWallpaperCard(@NonNull final Context context) {
        if (currentWallpaperCard == null) {
            currentWallpaperCard = loadWallpaperCardPreference(context);
        }
        return currentWallpaperCard;
    }

    public static void setCurrentWallpaperCard(@NonNull final Context context, final WallpaperCard wallpaperCard) {
        currentWallpaperCard = wallpaperCard;
        if (wallpaperCard != null) {
            saveWallpaperCardPreference(context, wallpaperCard);
        }
    }

    public static boolean isCurrentWallpaperCard(WallpaperCard wallpaperCard) {
        // Only check variable, no SharedPreference.
        // If wallpaper is not this app, Preference should not be cleared,
        // only currentWallpaperCard is set to null.
        // Because when we getCurrentWallpaperCard(), it loads Preference,
        // and set currentWallpaperCard to non-null.
        // We want to detect whether service is selected
        // by checking whether currentWallpaperCard is null.
        // If we use getCurrentWallpaperCard(), we cannot archive this.
        return currentWallpaperCard != null && wallpaperCard.equals(currentWallpaperCard);
    }

    public static WallpaperCard getPreviewWallpaperCard() {
        return previewWallpaperCard;
    }

    public static void setPreviewWallpaperCard(final WallpaperCard wallpaperCard) {
        previewWallpaperCard = wallpaperCard;
    }

    public static JSONArray getCardsJSONArray() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        if (cards != null) {
            for (WallpaperCard card : cards) {
                // INTERNAL WallpaperCard don't need to save.
                if (card.getType() != WallpaperCard.Type.INTERNAL) {
                    jsonArray.put(card.toJSON());
                }
            }
        }
        return jsonArray;
    }

    private static void saveWallpaperCardPreference(@NonNull final Context context, final WallpaperCard wallpaperCard) {
        final SharedPreferences pref = context.getSharedPreferences(CURRENT_CARD_PREF, MODE_PRIVATE);
        // Save to preference.
        final SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString("name", wallpaperCard.getName());
        prefEditor.putString("path", wallpaperCard.getPath());
        switch (wallpaperCard.getType()) {
            case INTERNAL:
                prefEditor.putString("type",  "INTERNAL");
                break;
            case EXTERNAL:
                prefEditor.putString("type", "EXTERNAL");
                break;
        }
        prefEditor.apply();
    }

    private static WallpaperCard loadWallpaperCardPreference(@NonNull final Context context) {
        final SharedPreferences pref = context.getSharedPreferences(CURRENT_CARD_PREF, MODE_PRIVATE);
        final String name = pref.getString("name", null);
        final String path = pref.getString("path", null);
        if (name == null || path == null) {
            return null;
        }
        WallpaperCard.Type type = WallpaperCard.Type.EXTERNAL;
        Uri uri;
        if (Objects.equals(pref.getString("type", null), "INTERNAL")) {
            type = WallpaperCard.Type.INTERNAL;
            uri = Uri.parse("file:///android_asset/" + path);
        } else {
            uri = Uri.parse(path);
        }
        return new WallpaperCard(name, path, uri, type, null);
    }

    private static void initCards(@NonNull final Context context) {
        cards = new ArrayList<>();
        Bitmap thumbnail = null;
        try {
            thumbnail = BitmapFactory.decodeStream(
                    context.getAssets().open(INTERNAL_WALLPAPER_IMAGE_PATH)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        cards.add(new WallpaperCard(
                context.getResources().getString(R.string.fire_rain),
                INTERNAL_WALLPAPER_VIDEO_PATH, Uri.parse(
                "file:///android_asset/" + INTERNAL_WALLPAPER_VIDEO_PATH
        ), WallpaperCard.Type.INTERNAL, thumbnail
        ));
    }



    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());

        new OpenAdsHelper().setup(this);

        initRemoteConfig();

        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(
                            @NonNull InitializationStatus initializationStatus) {
                    }
                });


        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "XKFTXY94GXWJJV8847GN");

        instance = this;
        initDatabase();

        initAdjust();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

//        PreferenceUtil.getInstance(this).setValue(Constant.SharePrefKey.COIN, 0);


        prefs = new Prefs(this);
        prefs.setPremium(0);

    }

    private void initDatabase(){
        database = AppDatabase.getInMemoryDatabase(this);
    }


    public synchronized static WeatherApplication get() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }




    //////////////////////
    private void initAdjust() {
        String appToken = "qe8n3sxl5udc";
        String environment = BuildConfig.DEBUG ? AdjustConfig.ENVIRONMENT_SANDBOX : AdjustConfig.ENVIRONMENT_PRODUCTION;
        AdjustConfig config = new AdjustConfig(this, appToken, environment);
        Adjust.onCreate(config);
    }

    public static void initROAS(double revenue, String currency) {
        try {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(instance);
            SharedPreferences.Editor editor = sharedPref.edit();
            double currentImpressionRevenue = revenue/1000000;
            LogTroasFirebaseAdImpression(currentImpressionRevenue, currency); //LTV pingback provides value in micros, so if you are using that directly,
            // make sure to divide by 10^6
            float previousTroasCache = sharedPref.getFloat("TroasCache", 0); //Use App Local storage to store cache of tROAS
            float currentTroasCache = (float) (previousTroasCache + currentImpressionRevenue);
//check whether to trigger  tROAS event
            if (currentTroasCache >= 0.01) {
                LogTroasFirebaseAdRevenueEvent(currentTroasCache,currency);
                editor.putFloat("TroasCache", 0);//reset TroasCache
            } else {
                editor.putFloat("TroasCache", currentTroasCache);
            }
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void LogTroasFirebaseAdRevenueEvent(float tRoasCache, String currency) {
        try {
            Bundle bundle = new Bundle();
            bundle.putDouble(FirebaseAnalytics.Param.VALUE, tRoasCache);//(Required)tROAS event must include Double Value
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency);//put in the correct currency
            mFirebaseAnalytics.logEvent("Daily_Ads_Revenue", bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void LogTroasFirebaseAdImpression(double tRoasCache, String currency) {
        try {
            Bundle bundle = new Bundle();
            bundle.putDouble(FirebaseAnalytics.Param.VALUE, tRoasCache);//(Required)tROAS event must include Double Value
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency);//put in the correct currency
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.AD_IMPRESSION, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static final class AdjustLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
    }

    public static void trackingEvent(String event) {
        try {
            FlurryAgent.logEvent(event);
            Bundle params = new Bundle();
            Log.e("eventtttt", event);
            mFirebaseAnalytics.logEvent(event, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trackingEvent(String event, String key, String value) {
        try {
            try {
                Map<String, String> paramFlurry = new HashMap<String, String>();
                paramFlurry.put(key, value);
                FlurryAgent.logEvent(event, paramFlurry);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Bundle params = new Bundle();
                params.putString(key, value);
                mFirebaseAnalytics.logEvent(event, params);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("eventttt", event+"-"+key+value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initRemoteConfig() {
        try {
            FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(3600)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

            mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener( new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull Task<Boolean> task) {
                    fetchConfig();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchConfig() {
        try {
            FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//            String show_reward = mFirebaseRemoteConfig.getString(Constant.RemoteConfigKey.SHOW_REWARD);
//            Log.e("heheheheheh", show_reward);
//            PreferenceUtil.getInstance(this).setValue(Constant.SharePrefKey.SHOW_REWARD, show_reward);

            String new_reward = mFirebaseRemoteConfig.getString(Constant.RemoteConfigKey.NEW_REWARD);
            Log.e("heheheheheh", new_reward);
            PreferenceUtil.getInstance(this).setValue(Constant.SharePrefKey.NEW_REWARD, new_reward);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startWorkService() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, WorkService.class));
        }else {
            startService(new Intent(this, WorkService.class));
        }
    }

}
