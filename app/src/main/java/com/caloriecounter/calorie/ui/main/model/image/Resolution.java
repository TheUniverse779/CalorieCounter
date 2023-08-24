package com.caloriecounter.calorie.ui.main.model.image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Resolution implements Serializable {

    @Expose
    @SerializedName("width")
    private int width;
    @Expose
    @SerializedName("height")
    private int height;


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
