package com.caloriecounter.calorie.ui.livewallpaper.service;


import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public final class PlayerWrapper$mediaSourceEventListener$1 implements MediaSourceEventListener {
    public final PlayerWrapper a;



    public PlayerWrapper$mediaSourceEventListener$1(PlayerWrapper playerWrapper) {
        this.a = playerWrapper;
    }

    @Override
    public void onLoadCompleted(int i, @androidx.annotation.Nullable @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {
        Function1 function1 = this.a.f;
        if (function1 != null) {
            Unit unit = (Unit) function1.invoke(loadEventInfo);
        }
    }

    @Override
    public void onLoadStarted(int windowIndex, @androidx.annotation.Nullable @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

    }

    @Override
    public void onLoadCanceled(int windowIndex, @androidx.annotation.Nullable @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

    }

    @Override
    public void onLoadError(int windowIndex, @androidx.annotation.Nullable @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {

    }

    @Override
    public void onUpstreamDiscarded(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {

    }

    @Override
    public void onDownstreamFormatChanged(int windowIndex, @androidx.annotation.Nullable @Nullable MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {

    }
}
