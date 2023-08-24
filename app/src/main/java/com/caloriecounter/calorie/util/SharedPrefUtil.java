package com.caloriecounter.calorie.util;

import android.content.SharedPreferences;

import com.caloriecounter.calorie.Constant;


public class SharedPrefUtil {

    public static void insertTimeSchedule(SharedPreferences sharedPreferences, int data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constant.PrefKey.SCHEDULE, data);
        editor.apply();
    }

    public static int getTimeSchedule(SharedPreferences sharedPreferences) {
        return sharedPreferences.getInt(Constant.PrefKey.SCHEDULE, 0);
    }


}
