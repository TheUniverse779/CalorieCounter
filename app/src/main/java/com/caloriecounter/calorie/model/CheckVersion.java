package com.caloriecounter.calorie.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class CheckVersion {

    @NonNull
    @PrimaryKey
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("version")
    private String version;
    @Expose
    @SerializedName("isCheck")
    private String isCheck;
    @Expose
    @SerializedName("isShowAds")
    private String isShowAds;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getIsShowAds() {
        return isShowAds;
    }

    public void setIsShowAds(String isShowAds) {
        this.isShowAds = isShowAds;
    }
}
