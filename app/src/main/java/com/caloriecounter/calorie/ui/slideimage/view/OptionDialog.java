package com.caloriecounter.calorie.ui.slideimage.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.caloriecounter.calorie.databinding.DialogOptionBinding;
import com.caloriecounter.calorie.iinterface.OnClick;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class OptionDialog extends BottomSheetDialogFragment {
    private DialogOptionBinding binding;
    private OnClick onClick;
    private Context context;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_option, container, false);
        if (this.getDialog().getWindow() != null) {
            this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.getRoot().setBackgroundColor(Color.TRANSPARENT);
        setClickListener();
        return binding.getRoot();
    }

    private void setClickListener(){
        binding.btnLockScreen.setOnClickListener(v -> {
            try {
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

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

}

