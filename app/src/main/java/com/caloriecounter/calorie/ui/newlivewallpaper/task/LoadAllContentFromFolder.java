package com.caloriecounter.calorie.ui.newlivewallpaper.task;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.caloriecounter.calorie.ui.newlivewallpaper.DownloadedContent;
import com.caloriecounter.calorie.ui.newlivewallpaper.WallpaperCard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadAllContentFromFolder extends AsyncTask<Void, Void, List<DownloadedContent>> {

    private ContentResolver contentResolver;
    private List<DownloadedContent> imageList;
    private OnLoadDoneListener onLoadDoneListener;
    private String folderPath;

    public LoadAllContentFromFolder(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
        this.imageList = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected List<DownloadedContent> doInBackground(Void... voids) {
        loadLocalImage();
        return imageList;
    }

    @Override
    protected void onPostExecute(List<DownloadedContent> localImages) {
        super.onPostExecute(localImages);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void loadLocalImage() {
        try {
            getInternalImage(new File(folderPath));

            if (onLoadDoneListener != null) {
                onLoadDoneListener.onLoadDone(imageList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (onLoadDoneListener != null) {
                onLoadDoneListener.onLoadError();
            }
        }

    }

//    private List<Image> removeDuplicate() {
//        //loai bo cac anh trung ten + trung size + co' size = 0
//        List<Image> listRemoveDuplicate = new ArrayList<>();
//        if (imageList.size() == 0) {
//            return listRemoveDuplicate;
//        }
//        HashMap<String, Image> hashMap = new HashMap<>();
//        for (Image image : imageList) {
//            if (image.getFileSize() > 0) {
//                String key = image.getFileName() + "-" + image.getFileSize();
//                hashMap.put(key, image);
//            }
//        }
//        listRemoveDuplicate.addAll(hashMap.values());
//        return listRemoveDuplicate;
//
//    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    private void loadImageFromGalley() {
//        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME,
//                MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATE_TAKEN};
//        final String orderBy = MediaStore.Images.Media._ID;
//        //Stores all the images from the gallery in Cursor
//        Cursor cursor;
//
//        cursor = contentResolver.query(
//                MediaStore.Images.Media.INTERNAL_CONTENT_URI, columns, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + folderPath + "%"},
//                null);
//
//        int count = cursor.getCount();
//
//        //Create an array to store path to all the images
//        String[] arrPath = new String[count];
//
//
//        for (int i = 0; i < count; i++) {
//            try {
//                cursor.moveToPosition(i);
//
//                Image localImage = new Image();
//
//                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//                int takenDate = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
//
//                localImage.setUri(Uri.parse(cursor.getString(dataColumnIndex)));
//                File fileToUpload = new File(localImage.getUri().getPath());
//                localImage.setFileName(fileToUpload.getName());
//                localImage.setFileSize(fileToUpload.length());
//                localImage.setDate(cursor.getLong(takenDate));
//                if (fileToUpload.exists()) {
//                    imageList.add(localImage);
//                }
//
//                //Store the path of the image
//                arrPath[i] = cursor.getString(dataColumnIndex);
//                Log.i("PATH", arrPath[i]);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//        // The cursor should be freed up after use with close()
//        cursor.close();
//    }


    public OnLoadDoneListener getOnLoadDoneListener() {
        return onLoadDoneListener;
    }

    public void setOnLoadDoneListener(OnLoadDoneListener onLoadDoneListener) {
        this.onLoadDoneListener = onLoadDoneListener;
    }

    private void getInternalImage(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (File file : listFile) {
                if (file.isDirectory()) {
                    getInternalImage(file);
                } else {
                    if (file.getName().toLowerCase().endsWith(".jpg")
                            || file.getName().toLowerCase().endsWith(".jpeg")) {
                        Image localImage = new Image();
                        localImage.setFileName(file.getName());
                        localImage.setFileSize(file.length());
                        localImage.setUri(Uri.fromFile(file));
                        File fileToUpload = new File(localImage.getUri().getPath());
                        localImage.setDate(fileToUpload.lastModified());
                        if (fileToUpload.exists()) {
                            DownloadedContent downloadedContent = new DownloadedContent();
                            downloadedContent.setType(0);
                            downloadedContent.setImage(localImage);
                            imageList.add(downloadedContent);
                        }
//                        imageList.add(localImage);
                    }else if (file.getName().toLowerCase().endsWith(".mp4")) {
                        WallpaperCard localImage = new WallpaperCard(file.getName(), file.getPath(), Uri.fromFile(file), WallpaperCard.Type.EXTERNAL,  Bitmap.createBitmap(1080, 2340, Bitmap.Config.ARGB_8888));
                        File fileToUpload = new File(localImage.getUri().getPath());
                        if (fileToUpload.exists()) {
                            DownloadedContent downloadedContent = new DownloadedContent();
                            downloadedContent.setType(1);
                            downloadedContent.setWallpaperCard(localImage);
                            imageList.add(downloadedContent);
                        }
//                        imageList.add(localImage);
                    }
                }
            }
        }
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public interface OnLoadDoneListener {
        void onLoadDone(List<DownloadedContent> images);

        void onLoadError();
    }
}
