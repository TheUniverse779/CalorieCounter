package com.caloriecounter.calorie.ui.main.model.category;


import com.caloriecounter.calorie.ui.main.model.image.Image;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    //
    private int count;

    private ArrayList<Image> images;

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    //

    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("count_new")
    private String count_new;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount_new() {
        return count_new;
    }

    public void setCount_new(String count_new) {
        this.count_new = count_new;
    }



}
