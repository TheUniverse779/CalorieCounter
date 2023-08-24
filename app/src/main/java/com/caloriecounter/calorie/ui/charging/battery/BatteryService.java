package com.caloriecounter.calorie.ui.charging.battery;

import static android.content.Context.BATTERY_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;


import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.ui.charging.iinterface.BatteryCallback;
import com.caloriecounter.calorie.ui.charging.model.BatteryModeEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BatteryService {

    private static BatteryService service;

    private final ArrayList<BatteryCallback> callbacks = new ArrayList<>();

    private final SharedPreferences preferences = WeatherApplication.get().getSharedPreferences("battery_service", Context.MODE_PRIVATE);

    private final SimpleDateFormat format = new SimpleDateFormat("dd_MMMM_yyyy", Locale.ENGLISH);

    public static BatteryService get() {
        if(service==null){
            service = new BatteryService();
        }
        return service;
    }

    public long getChargedToday(){
        return preferences.getLong(format.format(new Date()),0);
    }
    public void chargedTodayUp(){
        preferences.edit().putLong(format.format(new Date()),getChargedToday()+1).apply();
    }

    public void sendBatteryConnected(){
        for (BatteryCallback callback:callbacks){
            callback.OnBatteryConnected();
        }
    }

    public void sendBatteryDisconnected(){
        for (BatteryCallback callback:callbacks){
            callback.OnBatteryDisconnected();
        }
    }

    public void sendBatteryChanged(){
        for (BatteryCallback callback:callbacks){
            callback.OnBatteryChanged();
        }
    }

    public void addListen(BatteryCallback batteryCallback){
        callbacks.add(batteryCallback);
    }
    public void removeListen(BatteryCallback batteryCallback){
        callbacks.remove(batteryCallback);
    }


    /**
     * Gets battery percentage.
     *
     * @return the battery percentage
     */
    public final int getBatteryPercentage() {
        BatteryManager bm = (BatteryManager) WeatherApplication.get().getSystemService(BATTERY_SERVICE);
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

//        int percentage = 0;
//        Intent batteryStatus = getBatteryStatusIntent();
//        if (batteryStatus != null) {
//            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//            percentage = (int) ((level / (float) scale) * 100);
//        }
//        return percentage;
    }

    public final float getBatteryCurrentCapacity(){
        final BatteryManager batteryManager = (BatteryManager) WeatherApplication.get().getSystemService(Context.BATTERY_SERVICE);
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)/(float)1000;
    }

    public final float getBatteryCurrentAmpere(){
        final BatteryManager batteryManager = (BatteryManager) WeatherApplication.get().getSystemService(Context.BATTERY_SERVICE);
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
    }

    /**
     * Is device charging boolean.
     *
     * @return is battery charging boolean
     */
    public final boolean isDeviceCharging() {
        Intent batteryStatus = getBatteryStatusIntent();
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL;
    }

    /**
     * Gets battery health.
     *
     * @return the battery health
     */
    public final boolean isBatteryGood() {
        boolean isGood = true;
        Intent batteryStatus = getBatteryStatusIntent();
        if (batteryStatus != null) {
            int health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            isGood = health == BatteryManager.BATTERY_HEALTH_GOOD;
        }
        return isGood;
    }

    /**
     * Gets battery temprature.
     *
     * @return the battery temprature
     */
    public final float getBatteryTemperature() {
        float temp = 0.0f;
        Intent batteryStatus = getBatteryStatusIntent();
        if (batteryStatus != null) {
            temp = (float) (batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10.0);
        }
        return temp;
    }

    /**
     * Gets battery voltage.
     *
     * @return the battery voltage
     */
    public final int getBatteryVoltage() {
        int volt = 0;
        Intent batteryStatus = getBatteryStatusIntent();
        if (batteryStatus != null) {
            volt = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        }
        return volt;
    }

    /**
     * Is battery present boolean.
     *
     * @return the boolean
     */
    public final boolean isBatteryPresent() {
        return getBatteryStatusIntent().getExtras() != null && getBatteryStatusIntent().getExtras()
                .getBoolean(BatteryManager.EXTRA_PRESENT);
    }

    private Intent getBatteryStatusIntent() {
        IntentFilter batFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        return WeatherApplication.get().registerReceiver(null, batFilter);
    }

    @SuppressLint("PrivateApi")
    public double getBatteryCapacity() {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS).getConstructor(Context.class).newInstance(WeatherApplication.get());
            batteryCapacity = (int) ((double) Class.forName(POWER_PROFILE_CLASS).getMethod("getBatteryCapacity").invoke(mPowerProfile));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return batteryCapacity;
    }

    public BatteryModeEntity.Brightness getBrightnessType(boolean bAuto, int parent) {
        if (bAuto) {
            return BatteryModeEntity.Brightness.AUTO;
        }
        if (parent <= 30) {
            return BatteryModeEntity.Brightness.PERCENT_10;
        }
        if (parent <= 65) {
            return BatteryModeEntity.Brightness.PERCENT_50;
        }
        if (parent <= 90) {
            return BatteryModeEntity.Brightness.PERCENT_80;
        }
        return BatteryModeEntity.Brightness.PERCENT_100;
    }

    public BatteryModeEntity.ScreenLock getScreenLockType(int millisecond) {
        if (((long) millisecond) <= TimeUnit.SECONDS.toMillis(15)) {
            return BatteryModeEntity.ScreenLock.SEC_15;
        }
        if (((long) millisecond) <= TimeUnit.SECONDS.toMillis(45)) {
            return BatteryModeEntity.ScreenLock.SEC_30;
        }
        if (((long) millisecond) <= TimeUnit.SECONDS.toMillis(85)) {
            return BatteryModeEntity.ScreenLock.MIN_01;
        }
        if (((long) millisecond) <= TimeUnit.SECONDS.toMillis(350)) {
            return BatteryModeEntity.ScreenLock.MIN_02;
        }
        if (((long) millisecond) <= TimeUnit.SECONDS.toMillis(1799)) {
            return BatteryModeEntity.ScreenLock.MIN_10;
        }
        return BatteryModeEntity.ScreenLock.MIN_30;
    }


}
