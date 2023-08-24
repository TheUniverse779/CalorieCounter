package com.caloriecounter.calorie.ui.slideimage.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.databinding.DialogOptionDownloadBinding;
import com.caloriecounter.calorie.iinterface.OnClickVariant;
import com.caloriecounter.calorie.ui.main.model.image.Variation;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.caloriecounter.calorie.ui.slideimage.event.OnDialogDismiss;

import org.greenrobot.eventbus.EventBus;

public class OptionDownloadDialog extends BottomSheetDialogFragment {
    private DialogOptionDownloadBinding binding;
    private OnClickVariant onClickVariant;
    private Context context;

    private Variation variation;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_option_download, container, false);
        if (this.getDialog().getWindow() != null) {
            this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.getRoot().setBackgroundColor(Color.TRANSPARENT);
        initData();
        setClickListener();
        return binding.getRoot();
    }

    private void setClickListener() {
        binding.btnVariant1.setOnClickListener(v -> {
            try {
                onClickVariant.onClickVariant(variation.getAdapted());
                dismiss();
                WeatherApplication.trackingEvent("Click_Download_Variant_1");
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Toast.makeText(context, "Something went wrong, please try again later!", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        binding.btnVariant2.setOnClickListener(v -> {
            try {
                onClickVariant.onClickVariant(variation.getAdapted_landscape());
                dismiss();
                WeatherApplication.trackingEvent("Click_Download_Variant_2");
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Toast.makeText(context, "Something went wrong, please try again later!", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        binding.btnOriginal.setOnClickListener(v -> {
            try {
                onClickVariant.onClickVariant(variation.getOriginal());
                dismiss();
                WeatherApplication.trackingEvent("Click_Download_Variant_Original");
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Toast.makeText(context, "Something went wrong, please try again later!", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.btnVariant1.performClick();
            }
        }, 100);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialog -> {
            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            int height = displayMetrics.heightPixels;
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setSkipCollapsed(true);
            behavior.setPeekHeight(height);

            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });


        return bottomSheetDialog;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
    }

    public void setCallback(OnClickVariant onClickVariant) {
        this.onClickVariant = onClickVariant;
    }

    public void setVariation(Variation variation) {
        this.variation = variation;
    }

    private void initData() {
        try {
            float finalValue = variation.getAdapted().getSize() / 1048576f;
            String s = String.format("%.2f", finalValue);
            binding.btnVariant1.setText("Portrait wallpaper: " + variation.getAdapted().getResolution().getWidth() + " x " + variation.getAdapted().getResolution().getHeight() + "(" + s + "MB" + ")");
        } catch (Exception e) {
            e.printStackTrace();
            binding.btnVariant1.setVisibility(View.GONE);
        }

        try {
            double finalValue1 = Math.round(variation.getAdapted_landscape().getSize() / 1048576f * 10.0) / 10.0;
            String s1 = String.format("%.2f", finalValue1);
            binding.btnVariant2.setText("Landscape wallpaper: " + variation.getAdapted_landscape().getResolution().getWidth() + " x " + variation.getAdapted_landscape().getResolution().getHeight() + "(" + s1 + "MB" + ")");
        } catch (Exception e) {
            e.printStackTrace();
            binding.btnVariant2.setVisibility(View.GONE);
        }

        try {
            double finalValue2 = Math.round(variation.getOriginal().getSize() / 1048576f * 10.0) / 10.0;
            String s2 = String.format("%.2f", finalValue2);
            binding.btnOriginal.setText("Original wallpaper: " + variation.getOriginal().getResolution().getWidth() + " x " + variation.getOriginal().getResolution().getHeight() + "(" + s2 + "MB" + ")");
        } catch (Exception e) {
            e.printStackTrace();
            binding.btnOriginal.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        EventBus.getDefault().post(new OnDialogDismiss());
    }
}

