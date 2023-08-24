package com.caloriecounter.calorie.base;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.ads.BannerAds;
import com.caloriecounter.calorie.ads.Prefs;


public abstract class BaseActivityNew<T extends ViewDataBinding> extends BaseActivityBlank {

    public abstract int getLayoutRes();
    public abstract int getFrame();
    public abstract void getDataFromIntent();
    public abstract void doAfterOnCreate();
    public abstract void setListener();
    public abstract BaseFragment initFragment();
    private T binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            BannerAds.initBannerAdsOptimize(this);
            doFirstMethod();
            afterSetContentView();
            binding = DataBindingUtil.setContentView(this, getLayoutRes());
            getDataFromIntent();
            doAfterOnCreate();
            setListener();
            getSupportFragmentManager().beginTransaction().replace(getFrame(), initFragment()).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doFirstMethod() {

    }

    public T getBinding(){
        return binding;
    }

    public void afterSetContentView(){

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    public void initBanner(FrameLayout viewGroup){
        try {
            if(BannerAds.getViewRoot() != null){
                if(BannerAds.getViewRoot().getParent() != null) {
                    ((ViewGroup)BannerAds.getViewRoot().getParent()).removeView(BannerAds.getViewRoot()); // <- fix
                }
            }
            int isPro = new Prefs(WeatherApplication.get()).getPremium();
            boolean isSub = new Prefs(WeatherApplication.get()).isRemoveAd();
            if (isPro == 1) {
                return;
            } else if (isSub) {
                return;
            }

            viewGroup.addView(BannerAds.getViewRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
