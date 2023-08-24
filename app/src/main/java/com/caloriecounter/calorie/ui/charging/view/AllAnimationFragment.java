package com.caloriecounter.calorie.ui.charging.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.base.BaseFragment;
import com.caloriecounter.calorie.databinding.FragmentHomeAnimationBinding;
import com.caloriecounter.calorie.ui.charging.AnimationLoader;
import com.caloriecounter.calorie.ui.charging.PermissionUtils;
import com.caloriecounter.calorie.ui.charging.adapter.ChargingAnimationAdapter;
import com.caloriecounter.calorie.ui.charging.model.ChargingAnimation;
import com.caloriecounter.calorie.ui.charging.model.OnUpdateList;
import com.caloriecounter.calorie.ui.main.view.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class AllAnimationFragment extends BaseFragment<FragmentHomeAnimationBinding> {
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_home_animation;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        try {
            AnimationLoader.load(result -> {
                if(result==null){
    //                btReload.setVisibility(View.VISIBLE);
                }else {
                    setUp(result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setListener() {

    }

    @Override
    public void setObserver() {

    }

    private ChargingAnimationAdapter animationAdapter;
    private void setUp(ArrayList<ChargingAnimation> result) {
        try {
            getBinding().rcvAnimation.setLayoutManager(new GridLayoutManager(mActivity,2));
            getBinding().rcvAnimation.setAdapter(animationAdapter = new ChargingAnimationAdapter(mActivity,result) {
                @Override
                public void OnItemClick(int position) {
                    if(!PermissionUtils.isOverLay(getContext())){
                        ((MainActivity)mActivity).showPermission();
                    }else {
                        try {
                            WeatherApplication.trackingEvent("Click_item_charging", "name", result.get(position).getFileName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ChargingDetailActivity.Companion.startScreen(mActivity, new Gson().toJson(result.get(position)));
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(){
        if(animationAdapter!=null){
            animationAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onUpdate(OnUpdateList onUpdateList){
        update();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getFrame() {
        return R.id.mainFrame;
    }
}
