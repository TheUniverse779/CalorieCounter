package com.caloriecounter.calorie.ui.charging;

import android.util.Log;

import com.caloriecounter.calorie.BuildConfig;


public class DebugLog {

    public static void e(String tag,String msg){
        if(BuildConfig.DEBUG){
            Log.e(tag,msg);
        }
    }
    public static void d(String tag,String msg){
        if(BuildConfig.DEBUG){
            Log.d(tag,msg);
        }
    }
    public static void i(String tag,String msg){
        if(BuildConfig.DEBUG){
            Log.i(tag,msg);
        }
    }
    public static void w(String tag,String msg){
        if(BuildConfig.DEBUG){
            Log.i(tag,msg);
        }
    }

    public static void e(Object tag,String msg){
        Log.e(tag.getClass().getSimpleName(),msg);
    }
    public static void w(Object tag,String msg){
        Log.w(tag.getClass().getSimpleName(),msg);
    }
    public static void d(Object tag,String msg){
        Log.d(tag.getClass().getSimpleName(),msg);
    }
    public static void i(Object tag,String msg){
        Log.i(tag.getClass().getSimpleName(),msg);
    }
}
