package com.caloriecounter.calorie.ui.newlivewallpaper;

import com.caloriecounter.calorie.ui.newlivewallpaper.task.Image;

import java.io.Serializable;

public class DownloadedContent implements Serializable {
    private int type = 0; // 0 ảnh, 1 video

    private Image image;


    private WallpaperCard wallpaperCard;

    public WallpaperCard getWallpaperCard() {
        return wallpaperCard;
    }

    public void setWallpaperCard(WallpaperCard wallpaperCard) {
        this.wallpaperCard = wallpaperCard;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
