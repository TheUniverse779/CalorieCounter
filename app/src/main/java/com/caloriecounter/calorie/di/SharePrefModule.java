package com.caloriecounter.calorie.di;

import android.content.Context;

import com.caloriecounter.calorie.util.PreferenceUtil;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;


@Module
@InstallIn(SingletonComponent.class)
public class SharePrefModule {
    @Provides
    public static PreferenceUtil providePreferenceUtil(@ApplicationContext Context context) {
        return new PreferenceUtil(context);
    }
}
