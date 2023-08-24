package com.caloriecounter.calorie.ui.livewallpaper.service;

import static com.caloriecounter.calorie.ui.livewallpaper.service.SingletonStaticKt.keySharedPrefVideo;
import static com.caloriecounter.calorie.ui.livewallpaper.service.SingletonStaticKt.keyVideo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;

public final class VideoLiveWallpaper extends WallpaperService {


    public final class VideoEngine extends Engine {
        public PlayerWrapper a;
        public SurfaceHolder b;
        public final SharedPreferences.OnSharedPreferenceChangeListener c = new a(this);

        public final class a implements SharedPreferences.OnSharedPreferenceChangeListener {
            public final /* synthetic */ VideoEngine a;

            public a(VideoEngine videoEngine) {
                this.a = videoEngine;
            }

            public final void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
                if (str == null) {
                    return;
                }
                this.a.a();
            }
        }

        public VideoEngine() {
            super();
        }

        public final void a() {
            if (this.b != null) {
                SharedPreferences sharedPref = getSharedPreferences(keySharedPrefVideo, Context.MODE_PRIVATE);
                Uri uri = Uri.parse(sharedPref.getString(keyVideo, null));
                b();
                VideoLiveWallpaper videoLiveWallpaper = VideoLiveWallpaper.this;
                Uri parse = uri;
                Intrinsics.checkNotNullExpressionValue(parse, "Uri.parse(filepath)");
                PlayerWrapper playerWrapper = new PlayerWrapper(videoLiveWallpaper, parse, null, null, null, 28);
                this.a = playerWrapper;
                Intrinsics.checkNotNull(playerWrapper);
                SurfaceHolder surfaceHolder = this.b;
                Intrinsics.checkNotNull(surfaceHolder);
                playerWrapper.setVideoSurfaceHolder(surfaceHolder);
                PlayerWrapper playerWrapper2 = this.a;
                Intrinsics.checkNotNull(playerWrapper2);
                playerWrapper2.start();
            }
        }

        public final void b() {
            PlayerWrapper playerWrapper = this.a;
            if (playerWrapper != null) {
                playerWrapper.stop();
            }
            this.a = null;
        }

        public void onDestroy() {
            super.onDestroy();
        }

        public void onSurfaceCreated(@NotNull SurfaceHolder surfaceHolder) {
            Intrinsics.checkNotNullParameter(surfaceHolder, "holder");
            super.onSurfaceCreated(surfaceHolder);
            this.b = surfaceHolder;
            a();
        }

        public void onSurfaceDestroyed(@NotNull SurfaceHolder surfaceHolder) {
            Intrinsics.checkNotNullParameter(surfaceHolder, "holder");
            super.onSurfaceDestroyed(surfaceHolder);
            b();
            this.b = null;
        }

        public void onVisibilityChanged(boolean z) {
            super.onVisibilityChanged(z);
            if (z) {
                a();
            } else {
                b();
            }
        }
    }

    public void onCreate() {
        super.onCreate();
    }

    @NotNull
    public Engine onCreateEngine() {
        return new VideoEngine();
    }
}
