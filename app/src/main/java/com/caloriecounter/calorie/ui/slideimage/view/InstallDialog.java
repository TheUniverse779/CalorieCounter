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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.base.BaseDialogFragment;
import com.caloriecounter.calorie.databinding.DialogInstallBinding;
import com.caloriecounter.calorie.iinterface.OnClick;
import com.caloriecounter.calorie.ui.slideimage.event.OnDialogDismiss;

import org.greenrobot.eventbus.EventBus;

public class InstallDialog extends BaseDialogFragment {
    private DialogInstallBinding binding;

    private OnClick onClick;
    private Context context;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_install, container, false);
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
        binding.btnLockScreen.setOnClickListener(v -> {
            try {
                WeatherApplication.trackingEvent("Click_Set_Lock_Screen");
                onClick.onClickToLockScreen();
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Toast.makeText(context, "Something went wrong, please try again later!", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        binding.btnMainScreen.setOnClickListener(v -> {
            try {
                WeatherApplication.trackingEvent("Click_Set_Main_Screen");
                onClick.onClickToMainScreen();
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Toast.makeText(context, "Something went wrong, please try again later!", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        binding.btnBoth.setOnClickListener(v -> {
            try {
                WeatherApplication.trackingEvent("Click_Both_Screen");
                onClick.onClickToBoth();
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Toast.makeText(context, "Something went wrong, please try again later!", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        EventBus.getDefault().post(new OnDialogDismiss());
    }

}
