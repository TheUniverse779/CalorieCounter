
package com.caloriecounter.calorie.ui.newlivewallpaper;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.ads.Callback;
import com.caloriecounter.calorie.ads.InterAds;
import com.caloriecounter.calorie.iinterface.DataChange;
import com.caloriecounter.calorie.ui.newlivewallpaper.task.LoadAllVideoFromFolder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CardAdapter.OnCardClickedListener {
    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";
    private static final String FIRST_START_PREF = "firstStartPref";
    private static final String SHOWED_TIPS_KEY = "showedTipsKey";
    private static final int SELECT_REQUEST_CODE = 3;
    private static final int PREVIEW_REQUEST_CODE = 7;
    private LinearLayout coordinatorLayout = null;
    private CardAdapter cardAdapter = null;
    private AlertDialog addDialog = null;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout layoutTip;
    private ArrayList<WallpaperCard> listCard = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        setFullScreen();

        progressBar = findViewById(R.id.progressBar);
        layoutTip = findViewById(R.id.layoutTip);
        final SharedPreferences pref = getSharedPreferences(
                FIRST_START_PREF, MODE_PRIVATE
        );

        cardAdapter = new CardAdapter(
                this, listCard, this, new DataChange() {
            @Override
            public void onDataChange(boolean isShow) {
                layoutTip.setVisibility(isShow?View.VISIBLE:View.GONE);
            }
        }
        );

//        File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        String savedPath = sdCard.getAbsolutePath() + "/EZT4KWallpaper/";
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//
//        intent.setDataAndType(Uri.parse(savedPath), "file/*");
//        startActivityForResult(intent, 123);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(cardAdapter);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 789);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 789);
//
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getMyCard();
                }
            }, 100);

        }


        coordinatorLayout = findViewById(R.id.coordinator_layout);

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setFullScreen(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == SELECT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
        } else if (requestCode == PREVIEW_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                LWApplication.setCurrentWallpaperCard(
                        this, LWApplication.getPreviewWallpaperCard()
                );
                // Rebind adapter.

            }
            // Don't forget to delete preview card.
            LWApplication.setPreviewWallpaperCard(null);
        }
    }

    @Override
    public void onCardClicked(@NonNull final WallpaperCard wallpaperCard) {
        LWApplication.testCard = wallpaperCard;
        // When card is clicked we go to preview mode.
        LWApplication.setPreviewWallpaperCard(wallpaperCard);
        final Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, GLWallpaperService.class)
        );

        startActivityForResult(intent, PREVIEW_REQUEST_CODE);


    }

    @Override
    public void onApplyButtonClicked(@NonNull final WallpaperCard wallpaperCard) {

    }

    @Override
    public void onCardInvalid(@NonNull final WallpaperCard wallpaperCard) {

    }


    private void getMyCard() {
        File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String savedPath = sdCard.getAbsolutePath() + "/EZT4KWallpaper/";
        LoadAllVideoFromFolder loadAllImageFromFolder = new LoadAllVideoFromFolder(getContentResolver());
        loadAllImageFromFolder.setFolderPath(savedPath);
        loadAllImageFromFolder.setOnLoadDoneListener(new LoadAllVideoFromFolder.OnLoadDoneListener() {
            @Override
            public void onLoadDone(List<WallpaperCard> images) {
                listCard.clear();
                listCard.addAll(images);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cardAdapter.notifyDataSetChanged();
                    }
                });


            }

            @Override
            public void onLoadError() {

            }
        });
        loadAllImageFromFolder.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 789) {
            if (grantResults != null && grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMyCard();
                } else {
                }
            } else {
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Ticket", "onPause");
//        WeatherApplication.get().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Ticket", "onResume");
//        WeatherApplication.get().onResume();
    }

    @Override
    public void onBackPressed() {
        InterAds.showAdsBreak(this, new Callback() {
            @Override
            public void callback() {
                MainActivity.super.onBackPressed();
            }
        });

    }
}
