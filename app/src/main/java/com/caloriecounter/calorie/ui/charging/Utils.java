package com.caloriecounter.calorie.ui.charging;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.DisplayMetrics;
import android.util.Log;


import com.caloriecounter.calorie.WeatherApplication;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Utils {

    public static String format10(long i){
        if(i<10){
            return "0"+i;
        }else {
            return i+"";
        }
    }

    @SuppressLint("DefaultLocale")
    public static String fmt(double d) {
        DecimalFormat format = new DecimalFormat("0.##");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(d);
    }

    public static float dp(){
        return 1 * ((float) WeatherApplication.get().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
    //获取当日00:00:00的时间戳,东八区则为早上八点
    public static long getZeroClockTimestamp(long time) {
        long currentStamp = time;
        currentStamp -= currentStamp % DAY_IN_MILLIS;
        Log.i("Wingbu", " DateTransUtils-getZeroClockTimestamp()  获取当日00:00:00的时间戳,东八区则为早上八点 :" + currentStamp);
        return currentStamp;
    }

    public static String getLauncherPackageName() {
        PackageManager localPackageManager = WeatherApplication.get().getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        ResolveInfo info = localPackageManager.resolveActivity(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (info != null) {
            return info.activityInfo.packageName;
        } else {
            return "";
        }

    }

    @SuppressLint("DefaultLocale")
    public static String convertTimes(int count) {

        int hours = count / 3600;
        int minutes = (count % 3600) / 60;
        int seconds = count % 60;
        String timeString;
        if (hours == 0 && seconds == 0) {
            timeString = String.format("%2dm", minutes);
        } else if (minutes == 0 && hours == 0) {
            timeString = String.format("%2ds", seconds);
        } else if (hours != 0) {
            timeString = String.format("%2dh%2dm", hours, minutes);
        } else {
            timeString = String.format("%2dm", minutes);
        }
        return timeString;
    }



}
