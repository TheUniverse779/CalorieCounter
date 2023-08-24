package com.caloriecounter.calorie.ui.slideimage.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.base.BaseDialogFragment;
import com.caloriecounter.calorie.databinding.DialogDailyGiftBinding;
import com.caloriecounter.calorie.databinding.DialogNotifyBinding;
import com.caloriecounter.calorie.iinterface.DialogState;
import com.caloriecounter.calorie.ui.main.model.image.Image;
import com.caloriecounter.calorie.ui.slideimage.event.OnDialogDismiss;

import org.greenrobot.eventbus.EventBus;


public class DailyGiftDialog2 extends BaseDialogFragment {
    private DialogDailyGiftBinding binding;
    private String description;
    private String label;
    private ClickButton clickButton;

    private Image image;

    public static DailyGiftDialog2 newInstance(ClickButton clickButton) {
        DailyGiftDialog2 oneButtonTitleDialog = new DailyGiftDialog2();
        oneButtonTitleDialog.clickButton = clickButton;
        return oneButtonTitleDialog;
    }


    private Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_daily_gift, container, false);
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
        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogState.onDialogDismiss();
                dismissAllowingStateLoss();
            }
        });

//        Glide.with(context)
//                .load(image.getVariations().getPreview_small().getUrl())
//                .into(binding.imgCache);

        Glide.with(context).load(image.getVariations().getAdapted().getUrl()).into(binding.img);
        binding.tvCoin.setText(image.getCost());

        binding.btnGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickButton.onClickButton();
                dismissAllowingStateLoss();
            }
        });

    }

    public interface ClickButton {
        void onClickButton();
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

    public void setImage(Image image) {
        this.image = image;
    }
}
