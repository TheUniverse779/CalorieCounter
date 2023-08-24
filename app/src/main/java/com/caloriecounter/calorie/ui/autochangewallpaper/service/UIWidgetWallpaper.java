package com.caloriecounter.calorie.ui.autochangewallpaper.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.caloriecounter.calorie.Constant;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.model.Schedule;
import com.caloriecounter.calorie.ui.newlivewallpaper.LWApplication;
import com.caloriecounter.calorie.ui.newlivewallpaper.task.Image;
import com.caloriecounter.calorie.util.PreferenceUtil;
import com.caloriecounter.calorie.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UIWidgetWallpaper extends WallpaperService {
    private String TAG = "UIWidgetWallpaper";

    private List<Image> images = new ArrayList<>();
    private List<Image> imagesForDoing = new ArrayList<>();

    private CountDownTimer countDownTimer;
    private long countTime = 60000;

    private SharedPreferences sharedPreferences;


    final static int pixFormat = PixelFormat.RGBA_8888;
    protected ImageView imageView;
    protected WindowManager windowManager;
    protected WindowManager.LayoutParams layoutParams;
    protected WidgetGroup widgetGroup;
    protected SurfaceHolder surfaceHolder;


    private int width = 0;
    private int height = 0;

    

    @Override
    public Engine onCreateEngine() {

        return new UIWidgetWallpaperEngine();
    }

    public class WidgetGroup extends ViewGroup {

        private final String TAG = getClass().getSimpleName();

        public WidgetGroup(Context context) {
            super(context);
            Log.i(TAG, "WidgetGroup");
            setWillNotDraw(true);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            layout(l, t, r, b);
        }

    }

    public class UIWidgetWallpaperEngine extends Engine implements Callback {


        @Override
        public void onCreate(SurfaceHolder holder) {
            Log.i(TAG, "onCreate-"+isPreview());
            super.onCreate(holder);
            try {
                getImage();
                surfaceHolder = holder;
                surfaceHolder.addCallback(this);
                imageView = new ImageView(UIWidgetWallpaper.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setClickable(false);
                imageView.setImageResource( R.drawable.wallpaper_default );
                widgetGroup = new WidgetGroup(UIWidgetWallpaper.this);
                widgetGroup.setBackgroundDrawable(getWallpaper());
                widgetGroup.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
                widgetGroup.setAddStatesFromChildren(true);
                holder.setFormat(pixFormat);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                widgetGroup.addView(imageView, imageParams);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i(TAG, "surfaceChanged: " + isPreview());
            try {
                synchronized (surfaceHolder) {
                    UIWidgetWallpaper.this.width = width;
                    UIWidgetWallpaper.this.height = height;

    //                Canvas canvas = surfaceHolder.lockCanvas();
    //                widgetGroup.layout(0, 0, width, height);
    //                imageView.layout(0, 0, width, height);
    //                widgetGroup.draw(canvas);
    //                surfaceHolder.unlockCanvasAndPost(canvas);
                    refreshView();

                    if(isPreview()){
                        getRandomPreviewImage();
                    }else {
                        getCountTime();
                        startLoop();
                        try {
                            countDownTimer.onFinish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "surfaceCreated-"+isPreview());

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i(TAG, "surfaceDestroyed-"+isPreview());
            if(!isPreview()) {
                try {
                    surfaceHolder = null;
                    countDownTimer.cancel();
                    countDownTimer = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void startLoop() {
        try {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            countDownTimer = new CountDownTimer(countTime, countTime) {
                @Override
                public void onTick(long l) {
                    Log.e(TAG, "Time: "+ countTime);
                }

                @Override
                public void onFinish() {
                    try {
                        if (imagesForDoing.size() == 0) {
                            imagesForDoing = new ArrayList<>(images);
                        }
                        int max, min, index;
                        min = 0;
                        Random random = new Random();
                        max = imagesForDoing.size() - 1;
                        if(max == 0){
                            index = 0;
                        }else {
                            index = random.nextInt(max - min + 1) + min;
                        }

    //                    imageView.setImageResource( R.drawable.avatar_word );
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Glide.with(UIWidgetWallpaper.this).load(imagesForDoing.get(index).getUri()).listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    refreshView();
                                                    imagesForDoing.remove(index);
                                                }
                                            }, 1000);
                                            return false;
                                        }
                                    }).into(imageView);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "Time lỗi: "+ e.getMessage());
                                }
                            }
                        });
                    } catch (Exception e) {
                        Log.e("", "");
                        if(images == null || (images != null && images.size() == 0)){
                            countDownTimer.cancel();
                            return;
                        }
                    }

                    startLoop();
                }
            };

            countDownTimer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getImage() {
        try {
            UIWidgetWallpaper.this.images = LWApplication.getListImageDownloaded();
            imagesForDoing = new ArrayList<>(UIWidgetWallpaper.this.images);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getCountTime() {
        try {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            ArrayList<Schedule> schedules = new Gson().fromJson(Util.loadJSONFromAsset(this, "schedule"), new TypeToken<List<Schedule>>() {
            }.getType());
            int position = PreferenceUtil.getInstance(UIWidgetWallpaper.this).getValue(Constant.PrefKey.SCHEDULE, 3);
            this.countTime = Long.parseLong(schedules.get(position).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getRandomPreviewImage(){
        try {
            int max, min, index;
            min = 0;
            Random random = new Random();
            max = imagesForDoing.size() - 1;
            if(max == 0){
                index = 0;
            }else {
                index = random.nextInt(max - min + 1) + min;
            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Glide.with(UIWidgetWallpaper.this).load(imagesForDoing.get(index).getUri()).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Canvas canvas = surfaceHolder.lockCanvas();
                                            widgetGroup.layout(0, 0, width, height);
                                            imageView.layout(0, 0, width, height);
                                            widgetGroup.draw(canvas);
                                            surfaceHolder.unlockCanvasAndPost(canvas);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 1000);
                                return false;
                            }


                        }).into(imageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "Time lỗi: "+ e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshView(){
        try {
            Canvas canvas = surfaceHolder.lockCanvas();
            widgetGroup.layout(0, 0, width, height);
            imageView.layout(0, 0, width, height);
            widgetGroup.draw(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
