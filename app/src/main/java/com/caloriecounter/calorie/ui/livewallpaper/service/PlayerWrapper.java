package com.caloriecounter.calorie.ui.livewallpaper.service;

import static com.google.android.exoplayer2.Player.REPEAT_MODE_ALL;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

public final class PlayerWrapper {
    @Nullable
    public SimpleExoPlayer a;
    public final PlayerWrapper$playerEventListener$1 b;
    public final PlayerWrapper$mediaSourceEventListener$1 c;
    public final Context d;
    public final Uri e;
    public final Function1<MediaSourceEventListener.LoadEventInfo, Unit> f;
    public final Function1<ExoPlaybackException, Unit> g;
    public final Function2<Boolean, Integer, Unit> h;


    public PlayerWrapper(@NotNull Context context, @NotNull Uri uri, @Nullable Function1<? super MediaSourceEventListener.LoadEventInfo, Unit> function1, @Nullable Function1<? super ExoPlaybackException, Unit> function12, @Nullable Function2<? super Boolean, ? super Integer, Unit> function2) {
        this.d = context;
        this.e = uri;
        this.f = (Function1<MediaSourceEventListener.LoadEventInfo, Unit>) function1;
        this.g = (Function1<ExoPlaybackException, Unit>) function12;
        this.h = (Function2<Boolean, Integer, Unit>) function2;
        this.b = new PlayerWrapper$playerEventListener$1(this);
        this.c = new PlayerWrapper$mediaSourceEventListener$1(this);
        a();
    }

    public final void a() {
        DefaultTrackSelector defaultTrackSelector = new DefaultTrackSelector();
        SimpleExoPlayer newSimpleInstance = ExoPlayerFactory.newSimpleInstance(this.d, defaultTrackSelector);
        this.a = newSimpleInstance;
        Intrinsics.checkNotNull(newSimpleInstance);
        newSimpleInstance.addListener(this.b);
        SimpleExoPlayer simpleExoPlayer = this.a;
        Intrinsics.checkNotNull(simpleExoPlayer);
        simpleExoPlayer.setVolume(0.0f);
        SimpleExoPlayer simpleExoPlayer2 = this.a;
        Intrinsics.checkNotNull(simpleExoPlayer2);
        int rendererCount = simpleExoPlayer2.getRendererCount();
        for (int i = 0; i < rendererCount; i++) {
            SimpleExoPlayer simpleExoPlayer3 = this.a;
            Intrinsics.checkNotNull(simpleExoPlayer3);
            if (simpleExoPlayer3.getRendererType(i) == 1) {
                defaultTrackSelector.setParameters(defaultTrackSelector.buildUponParameters().setRendererDisabled(i, true));
            }
        }
        Context context = this.d;
        ExtractorMediaSource createMediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(context, Util.getUserAgent(context, "com.wallpaperscraft.wallpaper"))).createMediaSource(this.e, new Handler(), this.c);
        SimpleExoPlayer simpleExoPlayer4 = this.a;
        Intrinsics.checkNotNull(simpleExoPlayer4);
        simpleExoPlayer4.setRepeatMode(REPEAT_MODE_ALL);
        SimpleExoPlayer simpleExoPlayer5 = this.a;
        Intrinsics.checkNotNull(simpleExoPlayer5);
        simpleExoPlayer5.prepare(createMediaSource);
    }

    @Nullable
    public final SimpleExoPlayer getInstance() {
        return this.a;
    }

    public final void pause() {
        SimpleExoPlayer simpleExoPlayer = this.a;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
        }
    }

    public final void setVideoSurfaceHolder(@NotNull SurfaceHolder surfaceHolder) {
        Intrinsics.checkNotNullParameter(surfaceHolder, "holder");
        SimpleExoPlayer simpleExoPlayer = this.a;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setVideoSurfaceHolder(surfaceHolder);
        }
    }

    public final void start() {
        SimpleExoPlayer simpleExoPlayer = this.a;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    public final void stop() {
        SimpleExoPlayer simpleExoPlayer = this.a;
        if (simpleExoPlayer != null) {
            Intrinsics.checkNotNull(simpleExoPlayer);
            simpleExoPlayer.removeListener(this.b);
            SimpleExoPlayer simpleExoPlayer2 = this.a;
            Intrinsics.checkNotNull(simpleExoPlayer2);
            simpleExoPlayer2.setPlayWhenReady(false);
            SimpleExoPlayer simpleExoPlayer3 = this.a;
            Intrinsics.checkNotNull(simpleExoPlayer3);
            simpleExoPlayer3.stop();
            SimpleExoPlayer simpleExoPlayer4 = this.a;
            Intrinsics.checkNotNull(simpleExoPlayer4);
            simpleExoPlayer4.release();
            this.a = null;
        }
    }

    public PlayerWrapper(Context context, Uri uri, Function1 function1, Function1 function12, Function2 function2, int i) {
        this(context, uri, (i & 4) != 0 ? null : function1, (i & 8) != 0 ? null : function12, (i & 16) != 0 ? null : function2);
    }

}
