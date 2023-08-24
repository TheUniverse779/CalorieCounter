package com.caloriecounter.calorie.ui.slideimage.view;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.caloriecounter.calorie.Constant;
import com.caloriecounter.calorie.base.BaseDialogFragment;
import com.caloriecounter.calorie.iinterface.DialogState;
import com.caloriecounter.calorie.ui.main.event.CoinChange;
import com.caloriecounter.calorie.ui.main.model.image.Image;
import com.caloriecounter.calorie.ui.premium.PremiumActivity;
import com.caloriecounter.calorie.ui.slideimage.event.OnDialogDismiss;
import com.caloriecounter.calorie.util.PreferenceUtil;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.databinding.DialogShowImageBinding;

import org.greenrobot.eventbus.EventBus;


public class ShowImageDialog extends BaseDialogFragment {
    private DialogShowImageBinding binding;
    private String description;
    private String label;
    private ClickButton clickButton;

    private Image image;

    public static ShowImageDialog newInstance(ClickButton clickButton) {
        ShowImageDialog oneButtonTitleDialog = new ShowImageDialog();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_show_image, container, false);
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
        binding.tvCoinneed.setText(image.getCost());

        if(image.getCost().equals("0")){
            binding.btnWatchAd2.setVisibility(View.VISIBLE);
            binding.btnWatchAd.setVisibility(View.GONE);
        }else {
            binding.btnWatchAd2.setVisibility(View.GONE);
            binding.btnWatchAd.setVisibility(View.VISIBLE);
        }

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogState.onDialogDismiss();
                dismissAllowingStateLoss();
            }
        });

        if(image.getVideoVariations() != null){
            Glide.with(context).load(image.getImageVariations().getAdapted().getUrl()).into(binding.imgAvatar);
        }else {
            Glide.with(context).load(image.getVariations().getAdapted().getUrl()).into(binding.imgAvatar);
        }



        binding.btnGoPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                Intent intent = new Intent(context, PremiumActivity.class);
                context.startActivity(intent);
            }
        });

        binding.btnWatchAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceUtil.getInstance(context).getValue(Constant.SharePrefKey.COIN, 0) < Integer.parseInt(image.getCost())) {
                    Toast.makeText(context, "You don't have enough coin", Toast.LENGTH_SHORT).show();
                } else {
                    int coin = PreferenceUtil.getInstance(context).getValue(Constant.SharePrefKey.COIN, 0) - Integer.parseInt(image.getCost());
                    PreferenceUtil.getInstance(context).setValue(Constant.SharePrefKey.COIN, coin);
                    EventBus.getDefault().post(new CoinChange());
                    dismissAllowingStateLoss();
                    clickButton.onClickButton();
                }

            }
        });

        binding.btnWatchAd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
                clickButton.onClickViewAd();
            }
        });

//        if(PreferenceUtil.getInstance(context).getValue(Constant.SharePrefKey.COIN, 0) < Integer.parseInt(image.getCost())){
//            binding.btnWatchAd.setAlpha(0.2f);
//            binding.btnWatchAd.setClickable(false);
//        }else {
//            binding.btnWatchAd.setAlpha(1.0f);
//            binding.btnWatchAd.setClickable(true);
//        }

    }

    public interface ClickButton {
        void onClickButton();

        void onClickViewAd();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        EventBus.getDefault().post(new OnDialogDismiss());
        dialogState.onDialogDismiss();
    }


    private DialogState dialogState;
    public void setDialogState(DialogState dialogState) {
        this.dialogState = dialogState;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
