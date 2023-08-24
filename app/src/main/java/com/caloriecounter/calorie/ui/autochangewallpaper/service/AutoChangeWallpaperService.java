package com.caloriecounter.calorie.ui.autochangewallpaper.service;

import android.app.WallpaperManager;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import com.caloriecounter.calorie.model.Schedule;
import com.caloriecounter.calorie.ui.newlivewallpaper.task.Image;
import com.caloriecounter.calorie.ui.newlivewallpaper.task.LoadAllImageFromFolder;
import com.caloriecounter.calorie.util.SharedPrefUtil;
import com.caloriecounter.calorie.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AutoChangeWallpaperService extends WallpaperService {

    private List<Image> images = new ArrayList<>();
    private List<Image> imagesForDoing = new ArrayList<>();

    private CountDownTimer countDownTimer;
    private long countTime = 60000;
    private WallpaperManager wallpaperManager;
    private SharedPreferences sharedPreferences;
    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }

    class VideoEngine extends Engine {

        public VideoEngine() {
            super();

        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            Log.e("VideoEngine", "onSurfaceCreated");
//            wallpaperManager = WallpaperManager.getInstance(AutoChangeWallpaperService.this);
//            getImage();
//
//            getCountTime();
//            startLoop();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            Log.e("VideoEngine", "onSurfaceDestroyed");
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            wallpaperManager = WallpaperManager.getInstance(AutoChangeWallpaperService.this);
            getImage();

            getCountTime();
            startLoop();
        }
    }

    private void startLoop(){
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(countTime, countTime) {
            @Override
            public void onTick(long l) {
                Log.e("Time", countTime+"");
            }

            @Override
            public void onFinish() {

//                try {
//                    if(imagesForDoing.size() == 0){
//                        imagesForDoing = new ArrayList<>(images);
//                    }
//                    int max, min, index;
//                    min = 0;
//                    Random random = new Random();
//                    max = imagesForDoing.size() - 1;
//                    index = random.nextInt(max - min + 1) + min;
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagesForDoing.get(index).getUri());
//                    imagesForDoing.remove(index);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
//                    } else {
//                        wallpaperManager.setBitmap(bitmap);
//                    }
//                } catch ( Exception e) {
//                    Log.e("", "");
//                }
                startLoop();
            }
        };

        countDownTimer.start();
    }

    private void getImage(){
        File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String savedPath = sdCard.getAbsolutePath() + "/EZT4KWallpaper/";
        LoadAllImageFromFolder loadAllImageFromFolder = new LoadAllImageFromFolder(getContentResolver());
        loadAllImageFromFolder.setFolderPath(savedPath);
        loadAllImageFromFolder.setOnLoadDoneListener(new LoadAllImageFromFolder.OnLoadDoneListener() {
            @Override
            public void onLoadDone(List<Image> images) {
                AutoChangeWallpaperService.this.images = images;
                imagesForDoing = new ArrayList<>(AutoChangeWallpaperService.this.images);
            }

            @Override
            public void onLoadError() {

            }
        });
        loadAllImageFromFolder.execute();
    }


    private void getCountTime(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ArrayList<Schedule> schedules = new Gson().fromJson(Util.loadJSONFromAsset(this, "schedule"), new TypeToken<List<Schedule>>(){}.getType());
        int position = SharedPrefUtil.getTimeSchedule(sharedPreferences);
//        long countTime = Long.parseLong(schedules.get(position).getTime());
        long countTime = 10000;
        this.countTime = countTime;
    }

}
