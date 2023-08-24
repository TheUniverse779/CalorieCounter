package com.caloriecounter.calorie.ui.livewallpaper.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.base.BaseDialogFragment;
import com.caloriecounter.calorie.databinding.DialogNotifyLiveBinding;


public class NotifiLiveDialog extends BaseDialogFragment {
    private DialogNotifyLiveBinding binding;
    private ClickButton clickButton;


    public static NotifiLiveDialog newInstance( ClickButton clickButton) {
        NotifiLiveDialog oneButtonTitleDialog = new NotifiLiveDialog();
        oneButtonTitleDialog.clickButton = clickButton;
        return oneButtonTitleDialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_notify_live, container, false);
        setLabel();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }

    private void setLabel() {
        binding.tvLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickButton.onClickButton();
                dismiss();
                WeatherApplication.trackingEvent("Click_OK_Live_noti");
            }
        });
    }

    public interface ClickButton {
        void onClickButton();
    }



}
