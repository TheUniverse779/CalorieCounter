package com.caloriecounter.calorie.ui.charging.model;

public class ChargingAnimation {
    private String fileName;
    private String textColor;
    private int animationType;
    private String tag;
    private float alpha;
    private double x;
    private double y;
    private long time;
    private boolean isShowRewardAds;

    public ChargingAnimation(String fileName, String textColor, int animationType, String tag, float alpha, double x, double y, boolean isShowRewardAds) {
        this.fileName = fileName;
        this.textColor = textColor;
        this.animationType = animationType;
        this.tag = tag;
        this.alpha = alpha;
        this.x = x;
        this.y = y;
        this.isShowRewardAds = isShowRewardAds;
    }

    public boolean isShowRewardAds() {
        return isShowRewardAds;
    }

    public void setShowRewardAds(boolean showRewardAds) {
        isShowRewardAds = showRewardAds;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public int getAnimationType() {
        return animationType;
    }

    public void setAnimationType(int animationType) {
        this.animationType = animationType;
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
