package com.caloriecounter.calorie.ui.downloaded.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;



import com.bumptech.glide.Glide;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.ui.newlivewallpaper.task.Image;

import java.util.ArrayList;

public class SlideDownloadedImageAdapter extends PagerAdapter {
    private ArrayList<Image> images = new ArrayList<>();

    private Context mContext;
    private Bitmap currentBitmap;
    private LoadBitmap loadBitmap;

    public SlideDownloadedImageAdapter(Context context, ArrayList<Image> images, LoadBitmap loadBitmap) {
        mContext = context;
        this.images = images;
        this.loadBitmap = loadBitmap;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_image_slide, collection, false);
        ImageView imageView = layout.findViewById(R.id.img);

        Glide.with(mContext)
                .load(images.get(position).getUri())
                .into(imageView);

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
            e.printStackTrace();
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
