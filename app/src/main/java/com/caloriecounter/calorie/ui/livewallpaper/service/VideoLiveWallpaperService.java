package com.caloriecounter.calorie.ui.livewallpaper.service;

import static com.caloriecounter.calorie.ui.livewallpaper.service.SingletonStaticKt.keySharedPrefVideo;
import static com.caloriecounter.calorie.ui.livewallpaper.service.SingletonStaticKt.keyVideo;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

public class VideoLiveWallpaperService extends WallpaperService {
    protected static int playheadTime = 0;

    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }

    class VideoEngine extends Engine {

        private final String TAG = getClass().getSimpleName();
        private final MediaPlayer mediaPlayer;

        public VideoEngine() {
            super();
            Log.i(TAG, "( VideoEngine )");

            SharedPreferences sharedPref = getSharedPreferences(keySharedPrefVideo, Context.MODE_PRIVATE);
            Uri uri = Uri.parse(sharedPref.getString(keyVideo, null));
            mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
            mediaPlayer.setLooping(true);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "onSurfaceCreated");
            mediaPlayer.setSurface(holder.getSurface());
            mediaPlayer.start();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            Log.i(TAG, "( INativeWallpaperEngine ): onSurfaceDestroyed");
            playheadTime = mediaPlayer.getCurrentPosition();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }
}
