package com.caloriecounter.calorie.ui.charging.helpers;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.IconCompat;


import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.ui.charging.battery.BatteryService;
import com.caloriecounter.calorie.ui.splash.view.SplashActivity;

public class NotificationHelper {

    public static void showForegroundNotification(Service service){
        Intent intent = new Intent(service, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent activity = PendingIntent.getActivity(service, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationManager notificationManager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel("10001", service.getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW);
                notificationChannel.setSound(null, null);
                notificationChannel.setShowBadge(false);
                notificationChannel.setLightColor(Color.WHITE);
                notificationManager.createNotificationChannel(notificationChannel);

            }
            String battery = BatteryService.get().getBatteryPercentage()+"";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(service.getApplicationContext(),"10001");
            builder.setSmallIcon(IconCompat.createWithBitmap(textAsBitmap(service,battery)))
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setChannelId("10001")
                    .setPriority(Notification.PRIORITY_LOW)
                    .setContentIntent(activity);
            builder.setContentTitle("Battery: "+battery+"%");
            builder.setContentText("Keep charging animation running...");

            service.startForeground(1,builder.build());
        }
    }

    public static void showBatteryFull(Service service){
        MediaPlayer mediaPlayer = MediaPlayer.create(service,R.raw.full);
        mediaPlayer.setVolume(1f,1f);
        mediaPlayer.start();
        Intent intent = new Intent();
        PendingIntent activity = PendingIntent.getActivity(service, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationManager notificationManager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel("10002", service.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(null, null);
                notificationChannel.setShowBadge(false);
                notificationChannel.setLightColor(Color.WHITE);
                notificationManager.createNotificationChannel(notificationChannel);

            }
            String battery = BatteryService.get().getBatteryPercentage()+"";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(service.getApplicationContext(),"10002");
            builder.setSmallIcon(IconCompat.createWithBitmap(textAsBitmap(service,battery)))
                    .setChannelId("10002")
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(activity);
            builder.setContentTitle("BATTERY FULL");
            builder.setContentText("Please disconnect your charger");

            notificationManager.notify(66,builder.build());
        }
    }


    public static void hideForegroundNotification(Service service){
        NotificationManager notificationManager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }

    public static Bitmap textAsBitmap(Context context,String text) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);
        paint.setTypeface(ResourcesCompat.getFont(context,R.font.k2d_medium));
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setFakeBoldText(true);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
}
