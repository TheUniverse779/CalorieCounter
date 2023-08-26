package com.caloriecounter.calorie.ui.main.model.dish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Energy {
    @Expose
    @SerializedName("value")
    private double value;
    @Expose
    @SerializedName("unit")
    private String unit;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
