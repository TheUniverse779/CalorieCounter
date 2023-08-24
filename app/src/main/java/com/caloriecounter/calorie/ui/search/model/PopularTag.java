package com.caloriecounter.calorie.ui.search.model;

import com.google.gson.annotations.SerializedName;

public class PopularTag {
    private String title;
    @SerializedName("preview_small_url")
    private String preview;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }
}
