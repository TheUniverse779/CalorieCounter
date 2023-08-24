package com.caloriecounter.calorie.ui.main.model.image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class VariationItem implements Serializable {
    @Expose
    @SerializedName("url")
    private String url;
    @Expose
    @SerializedName("size")
    private int size;
    @Expose
    @SerializedName("resolution")
    private Resolution resolution;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }
}
