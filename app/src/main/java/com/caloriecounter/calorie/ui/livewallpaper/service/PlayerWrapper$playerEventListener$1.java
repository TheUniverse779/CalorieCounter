package com.caloriecounter.calorie.ui.livewallpaper.service;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public final class PlayerWrapper$playerEventListener$1 implements Player.EventListener {
    public final PlayerWrapper a;

    public PlayerWrapper$playerEventListener$1(PlayerWrapper playerWrapper) {
        this.a = playerWrapper;
    }
    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Function1 function1 = this.a.g;
        if (function1 != null) {
            Unit unit = (Unit) function1.invoke(error);
        }
    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Function2 function2 = this.a.h;
        if (function2 != null) {
            Unit unit = (Unit) function2.invoke(Boolean.valueOf(playWhenReady), Integer.valueOf(playbackState));
        }
    }
}
