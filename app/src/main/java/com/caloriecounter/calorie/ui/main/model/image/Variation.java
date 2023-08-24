package com.caloriecounter.calorie.ui.main.model.image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Variation implements Serializable {
    @Expose
    @SerializedName("adapted")
    private VariationItem adapted;

    @Expose
    @SerializedName("adapted_landscape")
    private VariationItem adapted_landscape;

    @Expose
    @SerializedName("original")
    private VariationItem original;

    @Expose
    @SerializedName("preview_small")
    private VariationItem preview_small;

    public VariationItem getAdapted() {
        return adapted;
    }

    public void setAdapted(VariationItem adapted) {
        this.adapted = adapted;
    }

    public VariationItem getAdapted_landscape() {
        return adapted_landscape;
    }

    public void setAdapted_landscape(VariationItem adapted_landscape) {
        this.adapted_landscape = adapted_landscape;
    }

    public VariationItem getOriginal() {
        return original;
    }

    public void setOriginal(VariationItem original) {
        this.original = original;
    }

    public VariationItem getPreview_small() {
        return preview_small;
    }

    public void setPreview_small(VariationItem preview_small) {
        this.preview_small = preview_small;
    }
}
