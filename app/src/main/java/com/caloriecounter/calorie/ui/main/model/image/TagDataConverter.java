package com.caloriecounter.calorie.ui.main.model.image;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class TagDataConverter {
    @TypeConverter
    public String fromCountryLangList(List<String> variation) {
        if (variation == null) {
            return (null);
        }
        Gson gson = new Gson();
        String json = gson.toJson(variation);
        return json;
    }

    @TypeConverter
    public List<String> toCountryLangList(String variation) {
        if (variation == null) {
            return (null);
        }
        Type listType = new TypeToken<List<String>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(variation, listType);
    }
}
