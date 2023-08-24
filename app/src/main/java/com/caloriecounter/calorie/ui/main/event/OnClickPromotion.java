package com.caloriecounter.calorie.ui.main.event;

public class OnClickPromotion {
    private String key;

    public OnClickPromotion(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
