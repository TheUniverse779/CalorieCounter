package com.caloriecounter.calorie.ui.charging.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.base.BaseFragment;
import com.caloriecounter.calorie.databinding.FragmentSettingBinding;
import com.caloriecounter.calorie.ui.charging.AnimationSetting;

public class ChargingAnimationSettingFragment extends BaseFragment<FragmentSettingBinding> {
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        TextView tvPlayDuration = getBinding().getRoot().findViewById(R.id.tv_play_duration);
        TextView tvClosing = getBinding().getRoot().findViewById(R.id.tv_closing_method);
        SwitchCompat swOnlyShowOnLockScreen = getBinding().getRoot().findViewById(R.id.sw_show_lock_screen);
        SwitchCompat swShowBatteryLevel = getBinding().getRoot().findViewById(R.id.sw_show_battery_level);
        SwitchCompat swPlaySound = getBinding().getRoot().findViewById(R.id.sw_play_sound);
        swOnlyShowOnLockScreen.setChecked(AnimationSetting.isOnlyShowLockScreen());
        swShowBatteryLevel.setChecked(AnimationSetting.isShowBatteryLevel());
        swPlaySound.setChecked(AnimationSetting.isPlaySoundWithAnimation());

        swOnlyShowOnLockScreen.setOnCheckedChangeListener((buttonView, isChecked) -> AnimationSetting.setOnlyShowLockScreen(isChecked));
        swShowBatteryLevel.setOnCheckedChangeListener((buttonView, isChecked) -> AnimationSetting.setShowBatteryLevel(isChecked));
        swPlaySound.setOnCheckedChangeListener((buttonView, isChecked) -> AnimationSetting.setPlaySoundWithAnimation(isChecked));

        tvClosing.setText(AnimationSetting.getClosingMethod()==0?"One Click":"Double Click");
        getBinding().getRoot().findViewById(R.id.bt_clothing_method).setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(mActivity,tvClosing);
            popupMenu.getMenu().add(0,0,0,"One Click");
            popupMenu.getMenu().add(1,1,1,"Double Click");
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    AnimationSetting.setClosingMethod(item.getItemId());
                    tvClosing.setText(AnimationSetting.getClosingMethod()==0?"One Click":"Double Click");
                    return false;
                }
            });
        });

        tvPlayDuration.setText(getDuration(AnimationSetting.getPlayDuration()));
        getBinding().getRoot().findViewById(R.id.bt_play_duration).setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(mActivity,tvClosing);
            popupMenu.getMenu().add(0,0,0,"5 secs");
            popupMenu.getMenu().add(1,1,1,"10 secs");
            popupMenu.getMenu().add(2,2,2,"15 secs");
            popupMenu.getMenu().add(3,3,3,"30 secs");
            popupMenu.getMenu().add(4,4,4,"1 min");
            popupMenu.getMenu().add(5,5,5,"3 mins");
            popupMenu.getMenu().add(6,6,6,"Loop");
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    AnimationSetting.setPlayDuration(item.getItemId());
                    tvPlayDuration.setText(getDuration(AnimationSetting.getPlayDuration()));
                    return false;
                }
            });
        });

        getBinding().btBack.setOnClickListener(v -> {
            mActivity.onBackPressed();
        });

    }

    @Override
    public void setListener() {

    }

    @Override
    public void setObserver() {

    }

    private String getDuration(int playDuration) {
        switch (playDuration){
            case 0: return "5 secs";
            case 1: return "10 secs";
            case 2: return "15 secs";
            case 3: return "30 secs";
            case 4: return "1 min";
            case 5: return "3 mins";
            default: return "Loop";
        }
    }

    public static ChargingAnimationSettingFragment newInstance() {
        Bundle args = new Bundle();
        ChargingAnimationSettingFragment fragment = new ChargingAnimationSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getFrame() {
        return R.id.mainFrame;
    }
}
