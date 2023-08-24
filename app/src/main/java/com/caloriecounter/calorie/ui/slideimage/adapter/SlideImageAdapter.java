package com.caloriecounter.calorie.ui.slideimage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.ui.main.model.image.Image;

import java.util.ArrayList;

public class SlideImageAdapter extends PagerAdapter {
    private ArrayList<Image> images = new ArrayList<>();

    private Context mContext;
    private LoadBitmap loadBitmap;
    private ClickImage clickImage;

    public SlideImageAdapter(Context context, ArrayList<Image> images, LoadBitmap loadBitmap, ClickImage clickImage) {
        mContext = context;
        this.images = images;
        this.loadBitmap = loadBitmap;
        this.clickImage = clickImage;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_image_slide, collection, false);
        ImageView imageView = layout.findViewById(R.id.img);
        ImageView imageViewCache = layout.findViewById(R.id.imgCache);

        Glide.with(mContext)
                .load(images.get(position).getVariations().getPreview_small().getUrl())
                .into(imageViewCache);

        String link = images.get(position).getVariations().getAdapted().getUrl();

//        Glide.with(mContext)
//                .asBitmap()
//                .load(link)
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        images.get(position).setBitmap(resource);
//                        imageView.setImageBitmap(resource);
//                        Glide.with(mContext).load(resource).into(imageView);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                        Log.e("", "");
//                    }
//
//                    @Override
//                    public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
//                        super.onLoadFailed(errorDrawable);
//                    }
//                });


        Glide.with(mContext).load(link).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickImage.onClick();
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
    public interface ClickImage{
        void onClick();
    }

}
