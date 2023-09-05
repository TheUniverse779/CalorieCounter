package com.caloriecounter.calorie.ui.main.model.dish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Dish implements Serializable {
    @Expose
    @SerializedName(value="name", alternate={"brand_name"})
    private String name;
    @Expose
    @SerializedName("image_url")
    private String imageUrl;

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("tags")
    private ArrayList<Tag> tags;

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
