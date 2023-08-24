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

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.databinding.DialogDailyGiftBinding;
import com.caloriecounter.calorie.databinding.DialogOptionAfterDownloadedBinding;
import com.caloriecounter.calorie.iinterface.OnClick;


public class DailyGiftDialog extends BottomSheetDialogFragment {
    private DialogDailyGiftBinding binding;
    private OnClick onClick;
    private Context context;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_daily_gift, container, false);
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
//        binding.btnLockScreen.setOnClickListener(v -> {
//            try {
//                onClick.onClickToLockScreen();
//                dismissAllowingStateLoss();
//            } catch (Exception e) {
//                e.printStackTrace();
//                try {
//                    Toast.makeText(context, "Something went wrong, please try again later!", Toast.LENGTH_SHORT).show();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
//
//        binding.btnMainScreen.setOnClickListener(v -> {
//            try {
//                onClick.onClickToMainScreen();
//                dismissAllowingStateLoss();
//            } catch (Exception e) {
//                e.printStackTrace();
//                try {
//                    Toast.makeText(context, "Something went wrong, please try again later!", Toast.LENGTH_SHORT).show();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
//
//        binding.btnBoth.setOnClickListener(v -> {
//            try {
//                onClick.onClickToBoth();
//                dismissAllowingStateLoss();
//            } catch (Exception e) {
//                e.printStackTrace();
//                try {
//                    Toast.makeText(context, "Something went wrong, please try again later!", Toast.LENGTH_SHORT).show();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
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

    private void initData() {
    }
}

