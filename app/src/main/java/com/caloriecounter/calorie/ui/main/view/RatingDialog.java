package com.caloriecounter.calorie.ui.main.view;

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
import com.caloriecounter.calorie.databinding.DialogRatingBinding;

public class RatingDialog extends BaseDialogFragment {
    private DialogRatingBinding binding;
    private String description;
    private String label;
    private ClickButton clickButton;


    public static RatingDialog newInstance(String description, String label, ClickButton clickButton) {
        RatingDialog oneButtonTitleDialog = new RatingDialog();
        oneButtonTitleDialog.label = label;
        oneButtonTitleDialog.description =description;
        oneButtonTitleDialog.clickButton = clickButton;
        return oneButtonTitleDialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_rating, container, false);
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
                WeatherApplication.trackingEvent("Click_Rate");
                clickButton.onClickButton();
                dismiss();
            }
        });

        binding.btnLate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherApplication.trackingEvent("Click_Dismiss_Rate");
                dismiss();
            }
        });
    }

    public interface ClickButton {
        void onClickButton();
    }

}
