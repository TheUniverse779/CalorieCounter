package com.caloriecounter.calorie.ui.charging.services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import com.caloriecounter.calorie.ui.charging.AnimationSetting;
import com.caloriecounter.calorie.ui.charging.DebugLog;
import com.caloriecounter.calorie.ui.charging.battery.BatteryService;
import com.caloriecounter.calorie.ui.charging.helpers.NotificationHelper;
import com.caloriecounter.calorie.ui.charging.view.ChargingActivity;


public class WorkService extends Service {

    private final BroadcastReceiver mChargingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction()+"";
            DebugLog.i(WorkService.this,action+" /onReceive mChargingReceiver");
            switch (action) {
                case Intent.ACTION_POWER_CONNECTED:
                    BatteryService.get().sendBatteryConnected();
                    Intent intent1 = new Intent(context, ChargingActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_NO_USER_ACTION
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NO_ANIMATION
                            | Intent.FLAG_FROM_BACKGROUND);
                    context.startActivity(intent1);
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    BatteryService.get().chargedTodayUp();
                    BatteryService.get().sendBatteryDisconnected();
                    break;
                case Intent.ACTION_BATTERY_CHANGED:
                    BatteryService.get().sendBatteryChanged();
                    NotificationHelper.showForegroundNotification(WorkService.this);
                    if(AnimationSetting.isAlarm()){
                        if(BatteryService.get().getBatteryPercentage()==100){
                            if(BatteryService.get().isDeviceCharging()){
                                NotificationHelper.showBatteryFull(WorkService.this);
                            }
                        }
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DebugLog.i(WorkService.this,"onCreate");

        IntentFilter batteryIntents = new IntentFilter();
        batteryIntents.addAction(Intent.ACTION_POWER_CONNECTED);
        batteryIntents.addAction(Intent.ACTION_POWER_DISCONNECTED);
        batteryIntents.addAction(Intent.ACTION_BATTERY_CHANGED);
        try {
            registerReceiver(mChargingReceiver, batteryIntents);
        }catch (Exception e){
            e.printStackTrace();
            DebugLog.e(WorkService.this,e.getMessage()+" /registerReceiver mChargingReceiver");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent.getAction().equals("STOPFOREGROUND_ACTION")) {
                stopForeground(true);
                stopSelfResult(startId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationHelper.showForegroundNotification(this);
        DebugLog.i(WorkService.this,"onStartCommand");
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        DebugLog.i(WorkService.this,"onDestroy");
        try {
            unregisterReceiver(mChargingReceiver);
        }catch (Exception e){
            e.printStackTrace();
            DebugLog.e(WorkService.this,e.getMessage()+" /unregisterReceiver mChargingReceiver");
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Intent restartServiceIntent = new Intent(getApplicationContext(),
                this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            restartServicePendingIntent = PendingIntent.getService(
                    getApplicationContext(), 1, restartServiceIntent,
                    PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        }else {
            restartServicePendingIntent = PendingIntent.getService(
                    getApplicationContext(), 1, restartServiceIntent,
                    PendingIntent.FLAG_ONE_SHOT);
        }
        AlarmManager alarmService = (AlarmManager) getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        if (alarmService != null) {
            alarmService.set(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + 1000,
                    restartServicePendingIntent);
        }

        super.onTaskRemoved(rootIntent);

    }


}
