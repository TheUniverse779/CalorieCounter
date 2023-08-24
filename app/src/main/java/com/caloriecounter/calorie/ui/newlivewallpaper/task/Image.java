package com.caloriecounter.calorie.ui.newlivewallpaper.task;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

public class Image implements Serializable {
    //
    private boolean isSelectedAuto = true;

    //
    private Uri uri;
    private Bitmap thumb;
    private String fileName;
    private long fileSize;
    private boolean isChecked = false;
    private boolean isUpSuccess = false;
    private long date;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isUpSuccess() {
        return isUpSuccess;
    }

    public void setUpSuccess(boolean upSuccess) {
        isUpSuccess = upSuccess;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isSelectedAuto() {
        return isSelectedAuto;
    }

    public void setSelectedAuto(boolean selectedAuto) {
        isSelectedAuto = selectedAuto;
    }
}
