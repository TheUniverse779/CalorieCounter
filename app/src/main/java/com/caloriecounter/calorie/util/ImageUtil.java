package com.caloriecounter.calorie.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageUtil {
    public static void loadIconWeather(String iconCode, Context context, ImageView imageView){
        try {
            int id = context.getResources().getIdentifier("ic_"+iconCode, "drawable", context.getPackageName());
            Glide.with(context).load(id).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
