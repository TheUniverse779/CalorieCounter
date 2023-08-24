package com.caloriecounter.calorie.ui.main.model.image;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.android.gms.ads.nativead.NativeAd;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Image implements Serializable {

    @Ignore
    private ArrayList<Image> images;

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    @Ignore
    private int itemType = 0;

    @Ignore
    private transient NativeAd nativeAd;

    public NativeAd getNativeAd() {
        return nativeAd;
    }

    public void setNativeAd(NativeAd nativeAd) {
        this.nativeAd = nativeAd;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Expose
    @SerializedName("uploaded_at")
    private String uploaded_at;
    @TypeConverters(TagDataConverter.class)
    @Expose
    @SerializedName("tags")
    private List<String> tags;
    @Expose
    @SerializedName("source_link")
    private String source_link;
    @Expose
    @SerializedName("rating")
    private String rating;
    @Expose
    @SerializedName("min_cost_ends_at")
    private String min_cost_ends_at;
    @Expose
    @SerializedName("license")
    private String license;
    @PrimaryKey
    @NonNull
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("for_adult_only")
    private boolean for_adult_only;
    @Expose
    @SerializedName("downloads")
    private String downloads;
    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("cost")
    private String cost;
    @Expose
    @SerializedName("content_type")
    private String content_type;
    @Expose
    @SerializedName("category_id")
    private String category_id;
    @Expose
    @SerializedName("author")
    private String author;
    @TypeConverters(DataConverter.class)
    @Expose
    @SerializedName("variations")
    private Variation variations;

    //for video
    @TypeConverters(DataConverter.class)
    @Expose
    @SerializedName("image_variations")
    private Variation imageVariations;

    @TypeConverters(DataConverter.class)
    @Expose
    @SerializedName("video_variations")
    private Variation videoVariations;

    public Variation getImageVariations() {
        return imageVariations;
    }

    public void setImageVariations(Variation imageVariations) {
        this.imageVariations = imageVariations;
    }

    public Variation getVideoVariations() {
        return videoVariations;
    }

    public void setVideoVariations(Variation videoVariations) {
        this.videoVariations = videoVariations;
    }


    //for video

    public String getUploaded_at() {
        return uploaded_at;
    }

    public void setUploaded_at(String uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSource_link() {
        return source_link;
    }

    public void setSource_link(String source_link) {
        this.source_link = source_link;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMin_cost_ends_at() {
        return min_cost_ends_at;
    }

    public void setMin_cost_ends_at(String min_cost_ends_at) {
        this.min_cost_ends_at = min_cost_ends_at;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFor_adult_only() {
        return for_adult_only;
    }

    public void setFor_adult_only(boolean for_adult_only) {
        this.for_adult_only = for_adult_only;
    }

    public String getDownloads() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Variation getVariations() {
        return variations;
    }

    public void setVariations(Variation variations) {
        this.variations = variations;
    }

}
