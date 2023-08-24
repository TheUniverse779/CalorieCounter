package com.caloriecounter.calorie.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.di.NetworkModule
import com.caloriecounter.calorie.network.APIService
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.splash.view.SplashActivity
import com.caloriecounter.calorie.util.PreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class DailyForecastBR : BroadcastReceiver() {
    @Inject
    lateinit var preferenceUtil: PreferenceUtil
    private var notificationBuilder: NotificationCompat.Builder? = null

    @NetworkModule.NormalAPIService
    @Inject
    lateinit var apiService: APIService

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("DailyForecastBR", "hehhee")
//        Toast.makeText(context, "hehhee", Toast.LENGTH_SHORT).show()


        try {

            CoroutineScope(Dispatchers.IO).launch() {
                var image : Image? = null
                try {
                    image = apiService.getRandom()

                    withContext(Dispatchers.Main) {

                        try {
                            Glide.with(context)
                                .asBitmap()
                                .load(image.variations.preview_small.url)
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                        try {
                                            val intentOpen = Intent(context, SplashActivity::class.java)
                                            intentOpen.putExtra(Constant.IntentKey.DATA, image)
                                            intentOpen.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            var pendingIntent: PendingIntent? = null
                                            pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                PendingIntent.getActivity(context, 0 /* Request code */, intentOpen, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                                            } else {
                                                PendingIntent.getActivity(context, 0 /* Request code */, intentOpen, PendingIntent.FLAG_UPDATE_CURRENT)
                                            }

                                            val channelId = "4K wallpaper daily"
                                            notificationBuilder = NotificationCompat.Builder(context, channelId)
                                                .setSmallIcon(R.drawable.icon_5_noti)
                                                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                                                .setContentTitle("❤ ❤ ❤ Have a pleasant evening!")
                                                .setContentText("✅ ☛ ⚡ Click to enjoy this photo and thousands of others")
                                                .setAutoCancel(true)
                                                .setPriority(NotificationCompat.PRIORITY_LOW)
                                                .setLargeIcon(resource)
                                                .setStyle(NotificationCompat.BigPictureStyle()
                                                    .bigPicture(resource).bigLargeIcon(resource))
                                                .setCategory(NotificationCompat.CATEGORY_SYSTEM)
                                                .setContentIntent(pendingIntent)
                                            val notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

                                            // Since android Oreo notification channel is needed.
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                val channel = NotificationChannel(
                                                    channelId,
                                                    "4K wallpaper daily",
                                                    NotificationManager.IMPORTANCE_LOW
                                                )
                                                notificationManager.createNotificationChannel(channel)
                                            }
                                            notificationManager.notify(1, notificationBuilder?.build())
                                            WeatherApplication.trackingEvent("Create_daily_notify")
                                        } catch (e: Exception) {
                                        }
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {

                                    }

                                    override fun onLoadFailed(errorDrawable: Drawable?) {

                                    }

                                });



                        } catch (e: Exception) {
                        }
                    }
                }catch (e : Exception){

                }

            }
        } catch (e: Exception) {
        }


    }

}