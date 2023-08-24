package com.caloriecounter.calorie.ui.slideimage.view;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import com.caloriecounter.calorie.databinding.DialogInformationBinding;
import com.caloriecounter.calorie.ui.main.model.image.Image;
import com.caloriecounter.calorie.ui.slideimage.adapter.TagAdapter;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.caloriecounter.calorie.ui.slideimage.event.OnDialogDismiss;

import org.greenrobot.eventbus.EventBus;

public class InformationDialog extends BottomSheetDialogFragment {
    private DialogInformationBinding binding;
    private Context context;
    private Image image;
    private TagAdapter tagAdapter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_information, container, false);
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
        binding.tvAuthorName.setText(image.getAuthor());
        binding.tvDownload.setText(image.getDownloads());
        binding.tvLicense.setText(image.getLicense());
        binding.tvSource.setText(image.getSource_link());

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
        tagAdapter = new TagAdapter(context, image.getTags());
        binding.rclTag.setAdapter(tagAdapter);
        binding.rclTag.setLayoutManager(flexboxLayoutManager);

        binding.tvSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Source", binding.tvSource.getText().toString());
                Toast.makeText(context, "Copied link to clipboard", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialog -> {
            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();

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

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        EventBus.getDefault().post(new OnDialogDismiss());
    }
}

