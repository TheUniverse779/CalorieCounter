package com.caloriecounter.calorie.ui.charging;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.ui.charging.model.ChargingAnimation;

public class AnimationSetting {

    public static void setPlayDuration(int i){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        preferences.edit().putInt("play_duration",i).apply();
    }
    public static int getPlayDuration(){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        return preferences.getInt("play_duration",1);
    }

    public static void setClosingMethod(int i){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        preferences.edit().putInt("closing_method",i).apply();
    }

    public static int getClosingMethod(){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        return preferences.getInt("closing_method",1);
    }

    public static void setShowBatteryLevel(boolean bl){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("show_battery_level",bl).apply();
    }

    public static boolean isShowBatteryLevel(){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        return preferences.getBoolean("show_battery_level",true);
    }

    public static void setOnlyShowLockScreen(boolean bl){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("only_show_lock_screen",bl).apply();
    }

    public static boolean isOnlyShowLockScreen(){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        return preferences.getBoolean("only_show_lock_screen",false);
    }

    public static void setPlaySoundWithAnimation(boolean bl){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("play_sound_with_animation",bl).apply();
    }

    public static boolean isPlaySoundWithAnimation(){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        return preferences.getBoolean("play_sound_with_animation",true);
    }

    public static void setAlarm(boolean bl){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("alarm",bl).apply();
    }

    public static boolean isAlarm(){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        return preferences.getBoolean("alarm",false);
    }

    public static void setChargingAnimation(ChargingAnimation chargingAnimation){
        String json = new Gson().toJson(chargingAnimation);
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        preferences.edit().putString("charging_animation",json).apply();
        preferences.edit().putString("charging_animation_file",chargingAnimation.getFileName()).apply();
    }
    public static ChargingAnimation getChargingAnimation(){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        String json = preferences.getString("charging_animation","");
        ChargingAnimation chargingAnimation = null;
        try {
            chargingAnimation = new Gson().fromJson(json,ChargingAnimation.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return chargingAnimation;
    }

    public static void clearAnimation(){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        preferences.edit().putString("charging_animation","").apply();
    }

    public static void clearAnimationFile(){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        preferences.edit().putString("charging_animation_file","0").apply();
    }



    public static String getChargingAnimationFile(){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("animation_setting", Context.MODE_PRIVATE);
        return preferences.getString("charging_animation_file","0");
    }
}
