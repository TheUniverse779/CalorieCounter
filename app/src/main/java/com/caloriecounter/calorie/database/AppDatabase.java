package com.caloriecounter.calorie.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.caloriecounter.calorie.model.CheckVersion;
import com.caloriecounter.calorie.model.Favorite;
import com.caloriecounter.calorie.model.Recent;
import com.caloriecounter.calorie.ui.main.model.image.DataConverter;
import com.caloriecounter.calorie.ui.main.model.image.Image;
import com.caloriecounter.calorie.ui.main.model.image.TagDataConverter;

@Database(entities  = {CheckVersion.class, Favorite.class, Recent.class, Image.class} , version = 18)
@TypeConverters({ DataConverter.class, TagDataConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract WeatherDao foodDao();

    public static AppDatabase getInMemoryDatabase(Context context) {

        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "wallpaper4k").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
