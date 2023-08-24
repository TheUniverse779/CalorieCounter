package com.caloriecounter.calorie.ui.charging;

import android.content.Context;
import android.content.SharedPreferences;


import com.caloriecounter.calorie.WeatherApplication;

public class TutorialHelper {
    public static boolean isShowed(){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("tutorial", Context.MODE_PRIVATE);
        return preferences.getBoolean("is_showed",false);
    }
    public static void setShowed(){
        SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("tutorial", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("is_showed",true).apply();
    }
}
