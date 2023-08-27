package com.caloriecounter.calorie.ui.main.model.dish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Dish implements Serializable {
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("image_url")
    private String imageUrl;

    @Expose
    @SerializedName("nutritional_contents")
    private NutritionalContent nutritionalContents;

    public NutritionalContent getNutritionalContents() {
        return nutritionalContents;
    }

    public void setNutritionalContents(NutritionalContent nutritionalContents) {
        this.nutritionalContents = nutritionalContents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
