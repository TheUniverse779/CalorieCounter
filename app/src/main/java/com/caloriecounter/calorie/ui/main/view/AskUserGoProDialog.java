package com.caloriecounter.calorie.ui.main.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.caloriecounter.calorie.Constant;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.base.BaseDialogFragment;
import com.caloriecounter.calorie.databinding.DialogAskUserGoProBinding;
import com.caloriecounter.calorie.databinding.DialogAskUserViewAds2Binding;
import com.caloriecounter.calorie.iinterface.ClickViewAds;
import com.caloriecounter.calorie.iinterface.DialogState;
import com.caloriecounter.calorie.ui.premium.PremiumActivity;
import com.caloriecounter.calorie.ui.slideimage.event.OnDialogDismiss;
import com.caloriecounter.calorie.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;

public class AskUserGoProDialog extends BaseDialogFragment {
    private DialogAskUserGoProBinding binding;
    private ClickViewAds onClick;


    private Context context;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_ask_user_go_pro, container, false);
        setLabel();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialogState.onDialogShow();
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
        binding.btnWatchAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    WeatherApplication.trackingEvent("Show_reward");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onClick.onClickViewAds();
                dialogState.onDialogDismiss();
                dismissAllowingStateLoss();
            }
        });

        binding.btnGoPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PremiumActivity.class);
                context.startActivity(intent);
                dialogState.onDialogDismiss();
                dismissAllowingStateLoss();
            }
        });

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogState.onDialogDismiss();
                dismissAllowingStateLoss();
            }
        });

        binding.tvCoinCount.setText(String.valueOf(PreferenceUtil.getInstance(context).getValue(Constant.SharePrefKey.COIN, 0)));
    }

    public void setOnClick(ClickViewAds onClick) {
        this.onClick = onClick;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dialogState.onDialogDismiss();
        EventBus.getDefault().post(new OnDialogDismiss());
    }

    private DialogState dialogState;
    public void setDialogState(DialogState dialogState) {
        this.dialogState = dialogState;
    }
}
