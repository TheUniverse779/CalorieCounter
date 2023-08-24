package com.caloriecounter.calorie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class Schedule {
    @Expose
    @SerializedName("time")
    private String time;
    @Expose
    @SerializedName("label")
    private String label;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
