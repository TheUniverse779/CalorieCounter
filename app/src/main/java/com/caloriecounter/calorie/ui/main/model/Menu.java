package com.caloriecounter.calorie.ui.main.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Menu {

    @Expose
    @SerializedName("isSelected")
    private boolean isSelected;
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("label")
    private String label;
    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("content_type")
    private String[] contentType;
    @Expose
    @SerializedName("isShowPromotion")
    private boolean isShowPromotion;

    public boolean isShowPromotion() {
        return isShowPromotion;
    }

    public void setShowPromotion(boolean showPromotion) {
        isShowPromotion = showPromotion;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getContentType() {
        return contentType;
    }

    public void setContentType(String[] contentType) {
        this.contentType = contentType;
    }
}
