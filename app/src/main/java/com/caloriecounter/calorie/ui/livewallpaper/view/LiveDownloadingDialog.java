package com.caloriecounter.calorie.ui.livewallpaper.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.databinding.DialogLiveDownloadingBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;

public class LiveDownloadingDialog extends BottomSheetDialogFragment {
    private DialogLiveDownloadingBinding binding;
    private Context context;
    private String url;
    private String name;
    private DownloadListener downloadListener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_live_downloading, container, false);
        if (this.getDialog().getWindow() != null) {
            this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.getRoot().setBackgroundColor(Color.TRANSPARENT);
        setClickListener();
        download(url, name);
        return binding.getRoot();
    }

    private void setClickListener(){
        binding.btnCancelDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherApplication.trackingEvent("Click_Cancel_Download_Live");
                PRDownloader.cancelAll();
                Toast.makeText(context, context.getString(R.string.download_cancel), Toast.LENGTH_SHORT).show();
                dismiss();
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



    private void download(String url, String name){
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String file_path = context.getExternalFilesDir(null) + File.separator + "/4KLiveWallpaper";
        int downloadId = PRDownloader.download(url, downloadDir.getAbsolutePath() + "/EZT4KWallpaper/", name)
                .build()
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        double percent = ((double)progress.currentBytes / (double) progress.totalBytes) * 100;
                        binding.progressBar.setProgress((int)percent);
                        binding.tvPercent.setText((int)percent + "%");
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        try {
                            downloadListener.onDownloadComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(downloadDir.getAbsolutePath() + "/EZT4KWallpaper/"+name))) );
                        } catch (Exception e) {
                        }
                        try {
                            dismissAllowingStateLoss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Error error) {
                        try {
                            downloadListener.onDownloadError();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            dismissAllowingStateLoss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }


    public void setUrl(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }


    public interface DownloadListener{
        public void onDownloadComplete();
        public void onDownloadError();
    }
}

