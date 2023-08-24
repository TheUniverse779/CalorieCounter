package com.caloriecounter.calorie.ui.main.model.doubleimage;

import androidx.room.TypeConverters;

import com.caloriecounter.calorie.ui.main.model.image.DataConverter;
import com.caloriecounter.calorie.ui.main.model.image.Variation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DoubleImage implements Serializable {

    @Expose
    @SerializedName("rating")
    private String rating;
    @Expose
    @SerializedName("license")
    private String license;
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("downloads")
    private String downloads;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("cost")
    private String cost;
    @Expose
    @SerializedName("author")
    private String author;
    @TypeConverters(DataConverter.class)
    @Expose
    @SerializedName("home_variations")
    private Variation homeVariation;
    @TypeConverters(DataConverter.class)
    @Expose
    @SerializedName("lock_variations")
    private Variation lockVariation;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDownloads() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Variation getHomeVariation() {
        return homeVariation;
    }

    public void setHomeVariation(Variation homeVariation) {
        this.homeVariation = homeVariation;
    }

    public Variation getLockVariation() {
        return lockVariation;
    }

    public void setLockVariation(Variation lockVariation) {
        this.lockVariation = lockVariation;
    }
}
