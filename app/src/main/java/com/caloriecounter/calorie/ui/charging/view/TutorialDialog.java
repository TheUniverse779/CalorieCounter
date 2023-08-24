package com.caloriecounter.calorie.ui.charging.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.caloriecounter.calorie.R;


public class TutorialDialog extends Dialog{

    private int pager = 1;

    public TutorialDialog(@NonNull Context context) {
        super(context, R.style.AppTheme);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        setContentView(R.layout.dialog_tutorial);
        View btNext1 = findViewById(R.id.bt_next1);
        View btNext2 = findViewById(R.id.bt_next2);
        View btNext3 = findViewById(R.id.bt_next3);
        View btBack = findViewById(R.id.bt_back);
        pager = 1;
        updateScreen();

        Glide.with(getContext())
                .load(R.drawable.tutorial_11)
                .into((ImageView) findViewById(R.id.img_tutorial_1));

        Glide.with(getContext())
                .load(R.drawable.tutorial_22)
                .into((ImageView) findViewById(R.id.img_tutorial_2));

        Glide.with(getContext())
                .load(R.drawable.tutorial_33)
                .into((ImageView) findViewById(R.id.img_tutorial_3));

        btNext1.setOnClickListener(v -> {
            pager = 2;
            updateScreen();
        });
        btNext2.setOnClickListener(v -> {
            pager = 3;
            updateScreen();
        });
        btNext3.setOnClickListener(v -> {
            dismiss();
        });

        btBack.setOnClickListener(v -> {
            if(pager==1){
                dismiss();
            }else {
                pager--;
                updateScreen();
            }
        });

    }

    private void updateScreen() {
        View screen1 = findViewById(R.id.tutorial_screen1);
        View screen2 = findViewById(R.id.tutorial_screen2);
        View screen3 = findViewById(R.id.tutorial_screen3);
        screen1.setVisibility(View.GONE);
        screen2.setVisibility(View.GONE);
        screen3.setVisibility(View.GONE);
        if(pager==1){
            screen1.setAlpha(0);
            screen1.setVisibility(View.VISIBLE);
            screen1.animate().alpha(1).setDuration(300).start();
        }else if(pager==2){
            screen2.setAlpha(0);
            screen2.setVisibility(View.VISIBLE);
            screen2.animate().alpha(1).setDuration(300).start();
        }else {
            screen3.setAlpha(0);
            screen3.setVisibility(View.VISIBLE);
            screen3.animate().alpha(1).setDuration(300).start();
        }
    }

}
