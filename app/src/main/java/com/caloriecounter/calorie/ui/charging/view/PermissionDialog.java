package com.caloriecounter.calorie.ui.charging.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;

import com.bumptech.glide.Glide;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.ui.charging.PermissionUtils;
import com.caloriecounter.calorie.ui.charging.TutorialHelper;
import com.caloriecounter.calorie.ui.charging.iinterface.PermissionCallback;


public class PermissionDialog extends Dialog{

    private View btNext1;
    private View btSkip;

    private final PermissionCallback permissionCallback;

    private final boolean isMain;

    public PermissionDialog(@NonNull Context context, PermissionCallback permissionCallback, boolean isMain) {
        super(context, R.style.AppTheme);
        init();
        this.isMain = isMain;
        this.permissionCallback = permissionCallback;
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        setContentView(R.layout.dialog_permission);
        setCancelable(!isMain);
        btNext1 = findViewById(R.id.bt_next1);
        View btBack = findViewById(R.id.bt_back);
        btSkip = findViewById(R.id.bt_skip);

        Glide.with(getContext())
                .load(R.drawable.tutorial_1)
                .into((ImageView) findViewById(R.id.img_tutorial_1));


        btNext1.setOnClickListener(v -> {
            dismiss();
            if(permissionCallback !=null){
                permissionCallback.OnSkip();
            }
        });
        btSkip.setOnClickListener(v -> {
            dismiss();
            if(permissionCallback !=null){
                permissionCallback.OnSkip();
            }
        });
        btBack.setOnClickListener(v -> {
            if(permissionCallback !=null){
                permissionCallback.OnBack(this);
            }
        });

        initPermission();

    }

    private void initPermission() {
        SwitchCompat swBattery = findViewById(R.id.sw_battery);
        swBattery.setChecked(PermissionUtils.isIgnoringBatteryOptimizations(getContext()));
        swBattery.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                if(!PermissionUtils.isIgnoringBatteryOptimizations(getContext())){
                    PermissionUtils.startBatteryOptimization(getContext());
                }
            }else {
                if(PermissionUtils.isIgnoringBatteryOptimizations(getContext())){
                    swBattery.setChecked(true);
                }
            }
        });
        SwitchCompat swOverlay = findViewById(R.id.sw_overlay);
        swOverlay.setChecked(PermissionUtils.isOverLay(getContext()));
        swOverlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!PermissionUtils.isOverLay(getContext())){
                        PermissionUtils.startOverLay(getContext());
                    }
                }else {
                    if(PermissionUtils.isOverLay(getContext())){
                        swOverlay.setChecked(true);
                    }
                }
            }
        });

        if(swBattery.isChecked()&&swOverlay.isChecked()){
            btNext1.setEnabled(true);
            btSkip.setEnabled(true);
            btNext1.setAlpha(1f);
            btSkip.setAlpha(1f);
        }else {
            btNext1.setEnabled(false);
            btSkip.setEnabled(false);
            btNext1.setAlpha(0.5f);
            btSkip.setAlpha(0.5f);
        }
    }

    public void update(){
        initPermission();
    }

    @Override
    public void show() {
        TutorialHelper.setShowed();
        super.show();
    }

    @Override
    public void onBackPressed() {
        if(isMain){
            super.onBackPressed();
        }
    }
}
