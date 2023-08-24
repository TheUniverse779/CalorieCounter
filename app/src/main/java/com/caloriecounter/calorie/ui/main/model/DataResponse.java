package com.caloriecounter.calorie.ui.main.model;


import com.caloriecounter.calorie.ui.main.model.image.Image;

import java.util.ArrayList;

public class DataResponse {
    // for similar
    private boolean isLoadForSlide = false;
    //

    //
    private int offset;
    //
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private ArrayList<Image> items;

    public ArrayList<Image> getItems() {
        return items;
    }

    public void setItems(ArrayList<Image> items) {
        this.items = items;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isLoadForSlide() {
        return isLoadForSlide;
    }

    public void setLoadForSlide(boolean loadForSlide) {
        isLoadForSlide = loadForSlide;
    }
}
