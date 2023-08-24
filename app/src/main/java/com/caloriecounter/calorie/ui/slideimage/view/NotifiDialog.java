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

import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.base.BaseDialogFragment;
import com.caloriecounter.calorie.databinding.DialogNotifyBinding;
import com.caloriecounter.calorie.ui.slideimage.event.OnDialogDismiss;

import org.greenrobot.eventbus.EventBus;


public class NotifiDialog extends BaseDialogFragment {
    private DialogNotifyBinding binding;
    private String description;
    private String label;
    private ClickButton clickButton;

    public static NotifiDialog newInstance(String description, String label, ClickButton clickButton) {
        NotifiDialog oneButtonTitleDialog = new NotifiDialog();
        oneButtonTitleDialog.label = label;
        oneButtonTitleDialog.description =description;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_notify, container, false);
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
        binding.tvDescription.setText(description);
        binding.tvLabel.setText((label != null && !label.isEmpty())?label:context.getString(R.string.ok));
        binding.tvLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherApplication.trackingEvent("Click_OK_Noti_Dialog");
                clickButton.onClickButton();
                dismiss();
            }
        });
    }

    public interface ClickButton {
        void onClickButton();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        EventBus.getDefault().post(new OnDialogDismiss());
    }


}
