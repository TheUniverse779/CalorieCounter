package com.caloriecounter.calorie.ui.main.model.image;

import androidx.room.TypeConverter;

import com.google.gson.Gson;


public class DataConverter {
    @TypeConverter
    public String fromCountryLangList(Variation variation) {
        if (variation == null) {
            return (null);
        }
        Gson gson = new Gson();
        String json = gson.toJson(variation);
        return json;
    }

    @TypeConverter
    public Variation toCountryLangList(String variation) {
        if (variation == null) {
            return (null);
        }
        Gson gson = new Gson();
        return gson.fromJson(variation, Variation.class);
    }
}
