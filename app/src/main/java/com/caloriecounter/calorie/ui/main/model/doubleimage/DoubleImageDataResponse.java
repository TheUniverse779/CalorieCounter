package com.caloriecounter.calorie.ui.main.model.doubleimage;


import java.util.ArrayList;

public class DoubleImageDataResponse {
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

    private ArrayList<DoubleImage> items;

    public ArrayList<DoubleImage> getItems() {
        return items;
    }

    public void setItems(ArrayList<DoubleImage> items) {
        this.items = items;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
