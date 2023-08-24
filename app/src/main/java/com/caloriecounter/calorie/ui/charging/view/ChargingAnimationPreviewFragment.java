package com.caloriecounter.calorie.ui.charging.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadManager;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.base.BaseFragment;
import com.caloriecounter.calorie.databinding.FragmentChargingAnimationPreviewBinding;
import com.caloriecounter.calorie.ui.charging.AnimationLoader;
import com.caloriecounter.calorie.ui.charging.AnimationSetting;
import com.caloriecounter.calorie.ui.charging.DebugLog;
import com.caloriecounter.calorie.ui.charging.battery.BatteryService;
import com.caloriecounter.calorie.ui.charging.model.ChargingAnimation;
import com.caloriecounter.calorie.ui.charging.model.OnUpdateList;
import com.caloriecounter.calorie.ui.charging.services.WorkService;
import com.caloriecounter.calorie.util.AppUtil;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;


public class ChargingAnimationPreviewFragment extends BaseFragment<FragmentChargingAnimationPreviewBinding> {

    private ChargingAnimation chargingAnimation;

    public static ChargingAnimationPreviewFragment newInstance(String jsonCharging) {
        Bundle args = new Bundle();
        args.putString("json", jsonCharging);
        ChargingAnimationPreviewFragment fragment = new ChargingAnimationPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_charging_animation_preview;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

        String jsonCharging = getArguments().getString("json");
        chargingAnimation = new Gson().fromJson(jsonCharging, ChargingAnimation.class);

        checkDownload();

        getBinding().getRoot().findViewById(R.id.bt_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(ChargingAnimationSettingFragment.newInstance());
            }
        });

        getBinding().getRoot().findViewById(R.id.bt_preview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChargingPreviewActivity.start(mActivity, jsonCharging);
            }
        });

        getBinding().btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.onBackPressed();
            }
        });

        initDone();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void setObserver() {

    }

    @Override
    public int getFrame() {
        return R.id.mainFrame;
    }

    @SuppressLint("SetTextI18n")
    private void initDone() {
        View btDone = getBinding().getRoot().findViewById(R.id.bt_done);
        TextView tvDone = getBinding().getRoot().findViewById(R.id.tv_apply);
        ImageView imgApply = getBinding().getRoot().findViewById(R.id.img_done);
        if (chargingAnimation.getFileName().equals(AnimationSetting.getChargingAnimationFile())) {
            btDone.setAlpha(0.5f);
            tvDone.setText("Applied");
            imgApply.setImageResource(R.drawable.apply);
            btDone.setBackgroundResource(R.drawable.bg_add_buttonnn);
//            btDone.getBackground().setColorFilter(Color.parseColor("#444444"), PorterDuff.Mode.SRC_IN);
            tvDone.setTextColor(Color.parseColor("#50FFFFFF"));
            imgApply.setColorFilter(Color.parseColor("#50FFFFFF"), PorterDuff.Mode.SRC_IN);
            getBinding().tvExplain.setVisibility(View.VISIBLE);

            btDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopService();
                    AnimationSetting.clearAnimation();
                    AnimationSetting.clearAnimationFile();
                    initDone();
                }
            });
        } else {
            getBinding().tvExplain.setVisibility(View.INVISIBLE);
            btDone.setAlpha(1f);
            tvDone.setText("Apply");
            btDone.setBackgroundResource(R.drawable.bg_add_button3);
            tvDone.setTextColor(Color.parseColor("#FFFFFF"));
            imgApply.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
            imgApply.setImageResource(R.drawable.ic_round_charging_station_24);
            btDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startWorkService();
                    AnimationSetting.setChargingAnimation(chargingAnimation);
                    EventBus.getDefault().post(new OnUpdateList());
                    initDone();
                    SetChargingAnimSuccess setChargingAnimSuccess = SetChargingAnimSuccess.newInstance("Charging animation setting is complete, please plug in the charger to experience", "OK", new SetChargingAnimSuccess.ClickButton() {
                        @Override
                        public void onClickButton() {

                        }
                    });
                    setChargingAnimSuccess.show(mActivity.getSupportFragmentManager(), null);
                    try {
                        WeatherApplication.trackingEvent("Set_Charging_anim_success", "name", chargingAnimation.getFileName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        }
    }

    private TextView tvDownload;
    private final DownloadManager downloadManager = new ThinDownloadManager();
    private DownloadRequest downloadRequest;

    private void checkDownload() {
        tvDownload = getBinding().getRoot().findViewById(R.id.tv_download);
        boolean isDownloaded = AnimationLoader.isDownload(chargingAnimation.getFileName());
        if (isDownloaded) {
            tvDownload.setVisibility(View.GONE);
            preview();
        } else {
            download(chargingAnimation.getFileName());
        }
    }

    @SuppressLint("SetTextI18n")
    private void preview() {
        tvDownload.setVisibility(View.GONE);
        FrameLayout frameVideo = getBinding().getRoot().findViewById(R.id.frame_video);
        try {
            ScalableVideoView videoView = new ScalableVideoView(mActivity);
            videoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            frameVideo.removeAllViews();
            frameVideo.addView(videoView);
            videoView.setDataSource(AnimationLoader.getVideoPath(chargingAnimation.getFileName()));
            videoView.setLooping(true);
            videoView.prepare();
            videoView.start();
            videoView.setScalableType(ScalableType.CENTER_CROP);
            videoView.setVolume(0f, 0f);

            View viewBatteryLevel = getBinding().getRoot().findViewById(R.id.view_battery_level);
            TextView tvBatteryLevel = getBinding().getRoot().findViewById(R.id.tv_battery_level);
            ImageView imgFlash = getBinding().getRoot().findViewById(R.id.img_flash);
            imgFlash.setColorFilter(Color.parseColor(chargingAnimation.getTextColor()));
            tvBatteryLevel.setTextColor(Color.parseColor(chargingAnimation.getTextColor()));
            videoView.post(() -> {
                viewBatteryLevel.setAlpha(0);
                viewBatteryLevel.setVisibility(View.VISIBLE);
                float x = (float) (chargingAnimation.getX() * videoView.getWidth() / 2);
                float y = (float) (chargingAnimation.getY() * videoView.getHeight() / 2);
                viewBatteryLevel.setTranslationX(x);
                viewBatteryLevel.setTranslationY(y);
                viewBatteryLevel.animate().alpha(0.8f).setDuration(500).start();
                DebugLog.e("translate", "x " + x + " / y " + y);
                DebugLog.e("translate", chargingAnimation.getTextColor());
                int level = BatteryService.get().getBatteryPercentage();
                long mili = 100L * (level % 10);
                tvBatteryLevel.setText(level / 10 * 10 + "%");
                new CountDownTimer(mili, 100) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvBatteryLevel.setText(level / 10 * 10 + ((mili - millisUntilFinished) / 100) + "%");
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onFinish() {
                        tvBatteryLevel.setText(level + "%");
                    }
                }.start();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        startAnimation(getBinding().getRoot().findViewById(R.id.img_flash), 1.1f);
    }

    private void startAnimation(View view, float scale) {
        view.animate().scaleX(scale).scaleY(scale)
                .setDuration(300).withEndAction(() -> {
                    if (view.getScaleX() == 1) {
                        startAnimation(view, 1.1f);
                    } else {
                        startAnimation(view, 1f);
                    }
                }).start();
    }

    @SuppressLint("SetTextI18n")
    private void download(String file) {
        if (downloadRequest != null) {
            downloadRequest.cancel();
        }
        tvDownload.setVisibility(View.VISIBLE);
        tvDownload.setText("Downloading...");
        downloadRequest = new DownloadRequest(Uri.parse(AnimationLoader.getVideoLink(file)))
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(Uri.fromFile(new File(AnimationLoader.getVideoPath(file))))
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDeleteDestinationFileOnFailure(true)
                .setStatusListener(new DownloadStatusListenerV1() {
                    @Override
                    public void onDownloadComplete(DownloadRequest downloadRequest) {
//                        mActivity.updateAnimation();   commmmm
                        EventBus.getDefault().post(new OnUpdateList());
                        tvDownload.setVisibility(View.GONE);
                        checkDownload();
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                        tvDownload.setText("Download Failed!");
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                        tvDownload.setText("Downloading...\n" + (downloadedBytes / 1024) + "/" + (totalBytes / 1024) + " kb");
                    }
                });
        downloadManager.add(downloadRequest);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (downloadRequest != null) {
            downloadRequest.cancel();
        }
    }

    private void startWorkService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mActivity.startForegroundService(new Intent(mActivity, WorkService.class));
        } else {
            mActivity.startService(new Intent(mActivity, WorkService.class));
        }
    }


    private void stopService() {
        if (AppUtil.isMyServiceRunning(WorkService.class, mActivity)) {
            Intent stopIntent = new Intent(mActivity, WorkService.class);
            stopIntent.setAction("STOPFOREGROUND_ACTION");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mActivity.startForegroundService(stopIntent);
            } else {
                mActivity.startService(stopIntent);
            }
        } else {

        }
    }
}
