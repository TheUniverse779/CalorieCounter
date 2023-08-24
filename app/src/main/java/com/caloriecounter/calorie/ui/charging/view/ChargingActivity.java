package com.caloriecounter.calorie.ui.charging.view;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.ui.charging.AnimationLoader;
import com.caloriecounter.calorie.ui.charging.AnimationSetting;
import com.caloriecounter.calorie.ui.charging.DebugLog;
import com.caloriecounter.calorie.ui.charging.battery.BatteryService;
import com.caloriecounter.calorie.ui.charging.iinterface.BatteryCallback;
import com.caloriecounter.calorie.ui.charging.model.ChargingAnimation;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;


import java.io.IOException;

public class ChargingActivity extends AppCompatActivity implements BatteryCallback {

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        fullScreen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        ChargingAnimation chargingAnimation = AnimationSetting.getChargingAnimation();
        if(chargingAnimation==null){
            finish();
            return;
        }

        findViewById(R.id.bt_finnish).setOnClickListener(v -> stopChargingVideo());

        findViewById(R.id.main).setAlpha(chargingAnimation.getAlpha());
        FrameLayout frameVideo = findViewById(R.id.frame_video);

        try {
            View viewAnimation = null;
            if(chargingAnimation.getFileName().contains("video")){
                ScalableVideoView videoView = new ScalableVideoView(this);
                videoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                frameVideo.removeAllViews();
                frameVideo.addView(videoView);
                videoView.setDataSource(this, Uri.parse(chargingAnimation.getFileName()));
                videoView.setLooping(true);
                videoView.prepare();
                videoView.start();
                videoView.setScalableType(ScalableType.CENTER_CROP);
                if(!AnimationSetting.isPlaySoundWithAnimation()){
                    videoView.setVolume(0f,0f);
                }else {
                    videoView.setVolume(1f,1f);
                }
                viewAnimation = videoView;
            }else if(chargingAnimation.getFileName().contains("image")){
                ImageView imgView = new ImageView(this);
                imgView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                frameVideo.removeAllViews();
                frameVideo.addView(imgView);
                Glide.with(this)
                        .load(chargingAnimation.getFileName())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE))
                        .skipMemoryCache(true)
                        .transition(withCrossFade())
                        .into(imgView);
                imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                viewAnimation = imgView;
            }else {
                ScalableVideoView videoView = new ScalableVideoView(this);
                videoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                frameVideo.removeAllViews();
                frameVideo.addView(videoView);
                videoView.setDataSource(AnimationLoader.getVideoPath(chargingAnimation.getFileName()));
                videoView.setLooping(true);
                videoView.prepare();
                videoView.start();
                videoView.setScalableType(ScalableType.CENTER_CROP);
                if(!AnimationSetting.isPlaySoundWithAnimation()){
                    videoView.setVolume(0f,0f);
                }else {
                    videoView.setVolume(1f,1f);
                }
                viewAnimation = videoView;
            }


            View viewBatteryLevel = findViewById(R.id.view_battery_level);
            TextView tvBatteryLevel = findViewById(R.id.tv_battery_level);
            ImageView imgFlash = findViewById(R.id.img_flash);
            imgFlash.setColorFilter(Color.parseColor(chargingAnimation.getTextColor()));
            tvBatteryLevel.setTextColor(Color.parseColor(chargingAnimation.getTextColor()));
            View finalViewAnimation = viewAnimation;
            viewAnimation.post(new Runnable() {
                @Override
                public void run() {
                    viewBatteryLevel.setAlpha(0);
                    viewBatteryLevel.setVisibility(View.VISIBLE);
                    float x = (float) (chargingAnimation.getX()* finalViewAnimation.getWidth()/2);
                    float y = (float) (chargingAnimation.getY()* finalViewAnimation.getHeight()/2);
                    viewBatteryLevel.setTranslationX(x);
                    viewBatteryLevel.setTranslationY(y);
                    viewBatteryLevel.animate().alpha(0.8f).setDuration(500).start();
                    DebugLog.e("translate","x "+x+" / y "+y);
                    DebugLog.e("translate",chargingAnimation.getTextColor());
                    int level = BatteryService.get().getBatteryPercentage();
                    long mili;
                    if(level>10){
                        mili = 1000;
                        tvBatteryLevel.setText(level-10+"%");
                    }else {
                        mili = level* 100L;
                        tvBatteryLevel.setText(0+"%");
                    }

                    new CountDownTimer(mili, 100){
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tvBatteryLevel.setText(level-(millisUntilFinished/100) +"%");
                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onFinish() {
                            tvBatteryLevel.setText(level +"%");
                            BatteryService.get().addListen(ChargingActivity.this);
                        }
                    }.start();
                }
            });

            DebugLog.e("download","start "+chargingAnimation.getFileName());
        } catch (IOException e) {
            e.printStackTrace();
            DebugLog.e("download",e.getMessage());
        }

        startAnimation(findViewById(R.id.img_flash),1.1f);

        frameVideo.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(ChargingActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    DebugLog.d("TEST", "onDoubleTap");
                    if(AnimationSetting.getClosingMethod()!=0){
                        stopChargingVideo();
                    }
                    return super.onDoubleTap(e);
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    if(AnimationSetting.getClosingMethod()==0){
                        stopChargingVideo();
                    }
                    super.onShowPress(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    if(AnimationSetting.getClosingMethod()==0){
                        stopChargingVideo();
                    }
                    return super.onSingleTapConfirmed(e);
                }
            });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        checkTime();

    }

    private final Handler countDownTimer = new Handler();
    private final Runnable runnable = () -> {
        if(!isDestroyed()){
            stopChargingVideo();
        }
    };
    private void checkTime() {
        int time = getDuration(AnimationSetting.getPlayDuration());
        if(time!=0){
            countDownTimer.postDelayed(runnable,time);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.removeCallbacks(runnable);
        countDownTimer.removeCallbacksAndMessages(null);
    }

    private int getDuration(int playDuration) {
        switch (playDuration){
            case 0: return 5000;
            case 1: return 10000;
            case 2: return 15000;
            case 3: return 30000;
            case 4: return 60000;
            case 5: return 18000;
            default: return 0;
        }
    }

    private void stopChargingVideo() {
        findViewById(R.id.main).animate().alpha(0).setDuration(500)
                .withEndAction(ChargingActivity.this::finish).start();
    }

    private void startAnimation(View view, float scale) {
        view.animate().scaleX(scale).scaleY(scale)
                .setDuration(300).withEndAction(() -> {
            if(view.getScaleX()==1){
                startAnimation(view,1.1f);
            }else {
                startAnimation(view,1f);
            }
        }).start();
    }


    @Override
    public void onBackPressed() {

    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void fullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        try { setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); } catch (Exception ignore) { }

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void OnBatteryConnected() {

    }

    @Override
    public void OnBatteryDisconnected() {
        stopChargingVideo();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void OnBatteryChanged() {
        TextView tvBatteryLevel = findViewById(R.id.tv_battery_level);
        tvBatteryLevel.setText(BatteryService.get().getBatteryPercentage()+"%");
    }
}