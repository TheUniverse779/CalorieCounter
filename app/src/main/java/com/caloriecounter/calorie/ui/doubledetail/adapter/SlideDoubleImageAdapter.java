package com.caloriecounter.calorie.ui.doubledetail.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.ui.main.model.image.Variation;

import java.util.ArrayList;

public class SlideDoubleImageAdapter extends PagerAdapter {
    private ArrayList<Variation> images = new ArrayList<>();

    private Context mContext;
    private LoadBitmap loadBitmap;

    public SlideDoubleImageAdapter(Context context, ArrayList<Variation> images, LoadBitmap loadBitmap) {
        mContext = context;
        this.images = images;
        this.loadBitmap = loadBitmap;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_image_slide, collection, false);
        ImageView imageView = layout.findViewById(R.id.img);
        ImageView imageViewCache = layout.findViewById(R.id.imgCache);

        Glide.with(mContext)
                .load(images.get(position).getPreview_small().getUrl())
                .into(imageViewCache);

        String link = images.get(position).getAdapted().getUrl();


        Glide.with(mContext).load(link).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            return images.size();
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
