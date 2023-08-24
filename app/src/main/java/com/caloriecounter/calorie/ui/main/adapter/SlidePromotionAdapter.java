package com.caloriecounter.calorie.ui.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.ui.main.event.OnClickPromotion;
import com.caloriecounter.calorie.ui.main.model.Promotion;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class SlidePromotionAdapter extends PagerAdapter {
    private ArrayList<Promotion> promotions = new ArrayList<>();

    private Context mContext;

    public SlidePromotionAdapter(Context context, ArrayList<Promotion> promotions) {
        mContext = context;
        this.promotions = promotions;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_promotion, collection, false);
        ImageView imageView = layout.findViewById(R.id.imgAvatar);
        TextView tvName = layout.findViewById(R.id.tvName);
        tvName.setText(promotions.get(position).getLabel());
        switch (promotions.get(position).getId()){
            case "auto":
                Glide.with(mContext).load(R.drawable.promotion_1).into(imageView);
                break;
            case "live":
                Glide.with(mContext).load(R.drawable.avatar_food).into(imageView);
                break;
            case "category":
                Glide.with(mContext).load(R.drawable.avatar_black).into(imageView);
                break;
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    WeatherApplication.trackingEvent("Click_Promotion_"+position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new OnClickPromotion(promotions.get(position).getId()));
            }
        });
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        try {
            return promotions.size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    public interface LoadBitmap{
        void onLoadBitmap(Bitmap bitmap);
    }

}
